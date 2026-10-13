# AGENTS.md

JavaFX 21 + SQLite strategy game. Maven project lives in the **`GEOPOLITIK/` subdirectory**, not at the repo root.

## Commands

Run from `GEOPOLITIK/` (`mvn` is on PATH; `GEOPOLITIK/.mvn/` is empty so there is no wrapper):

```bash
mvn -B -q compile -DskipTests   # fastest verification
mvn -B verify                   # what CI runs (wrapped in xvfb-run)
mvn javafx:run                  # launch the GUI (mainClass is set in pom.xml)
```

There are **no tests**. `src/test/` does not exist and `pom.xml` has no JUnit dependency, so `mvn verify` is effectively a compile. Don't hunt for test suites or add a runner.

Inspect the schema directly rather than guessing column names:

```bash
sqlite3 GEOPOLITIK/db/middleeastdb.db ".tables" ".schema Improvements"
```

## Working directory is load-bearing

Only `db/middleeastdb.db` exists; there is no `db/` at the repo root. Load paths are inconsistent and **only `GEOPOLITIK/` satisfies all of them**:

| Loader | Path | `../` fallback? |
|---|---|---|
| `DatabaseConnection` | `jdbc:sqlite:db/middleeastdb.db` | **no** |
| `loadImages` (`img/icons`, `img/attack icons`, `img/weapons`) | `new File(icon.path())` | **no** |
| `loadCombatActions` | `data/combat_options_list.json`, `data/combat_descriptions.json` | **no** |
| `loadMatch.getJsonData` | `matchs/match.json` (repo root!) | yes |
| `loadMatch.getJsonColorList` | `data/middle_east_list.json` | yes |
| `loadFlags` | `img/flags` | yes |

Launching from the repo root makes SQLite silently create an empty `db/middleeastdb.db` there and every query returns nothing. Don't "fix" the inconsistency — just always run from `GEOPOLITIK/`.

Note `matchs/match.json` lives at the repo root while `data/` lives under `GEOPOLITIK/`, so neither directory alone is sufficient.

## Where the real entrypoint is

`App.start` calls `loadGlobalVariables.load()` → `loadDataBases.load()` (calls `DatabaseConnection.init()`), then populates static caches in the `init` package (`loadMatch`, `loadImages`, `loadFlags`, `loadWeapons`, `loadCombatActions`). Controllers then read those statics. Anything loaded there must be registered in `loadGlobalVariables.load()` or it will be `null` at runtime.

`DatabaseConnection.getInstance()` throws `IllegalStateException` if init hasn't run — the failure surfaces in whichever controller first touches the DB, not at startup.

`match.json` has a `"country": "Israel"` key, but `Match` has no `country` field — Gson drops it silently. This is why the player country id is hardcoded to `11` (Israel, per the `FLAGS` map in `loadFlags`) in `MenuController`, `AttackController`, `DiplomacyCountryController`, `LaboratoryController`. Treat any new hardcoded `11` as a known placeholder, not a bug to fix in passing. (`match.json`'s `"database": "middleeast.db"` is likewise stale and unused — `DatabaseConnection` hardcodes the filename.)

`data/combat_effects_dialogs.json` is unreferenced by any code. `connection/queries/spy/` is an empty package. Don't assume they matter.

## Naming is deliberately inconsistent — match it

| Thing | Convention | Examples |
|---|---|---|
| Query classes | lowerCamel | `countryQueries`, `improvementQueries`, `weaponQueries`, `attackQueries` |
| Init loaders | lowerCamel | `loadImages`, `loadFlags`, `loadGlobalVariables` |
| Records / entities | UpperCamel | `Improvement`, `Weapon`, `Province` |
| Controllers | mixed | `LaboratoryController`, `WeaponsMarketControllers` (plural) |
| Packages | lowerCamel, misspelled | `functions/combatFuctions`, `functions/improvementsFunctions` |

Query classes take a `Connection` in the constructor; they are never singletons and get instantiated per controller.

## FXML conventions

- Views in `views/`, grouped into `combatViews/`, `countryViews/`, `diplomacyViews/`, `weaponsViews/`. Resources live under `src/main/resources/com/msservices/geopolitik/`.
- CSS is in `styles/`, usually a subdirectory (`combatStyles/`, `countryStyles/`, `diplomacyStyles/`, `laboratoryStyles/`, `weaponsStyles/`) but sometimes flat (`newsStyles.css`, `populationStyles.css`, `warMinisterStyles.css`, `mainViewStyles.css`). Match the existing file, don't reorganize.
- FXML is only the skeleton. **Lists are empty `VBox`/`FlowPane` containers with an `fx:id`; rows are built imperatively in the controller.** Follow this, don't add `ListView`/`TableView`.
- Row construction lives in a `crearFila`/`buildRow` helper that applies CSS classes from a string constants pool.
- Escape `$` in FXML `text` attributes: `text="\$000000000"`. A leading `$` is parsed as a property binding and blows up at load.
- `initialize()` runs before the window is shown, so `getScene()` is `null` there. Never touch it during `initialize()`.

## Opening new windows

`MenuController` has one `openX()` method per modal dialog: `FXMLLoader` (absolute classpath resource) + `new Stage()` + `initModality(Modality.APPLICATION_MODAL)` + `initOwner(<ImageView>.getScene().getWindow())` + `stage.showAndWait()`. Data reaches the new controller through a public setter called on the instance from `loader.getController()`, because FXML instantiates controllers with a no-arg constructor. Add a setter rather than trying to inject.

Controllers implement `interfaces.interaction` (`goBack()`/`goAhead()`); `goBack()` conventionally closes the stage.

## Schema gotchas

- `Improvements.type` is exactly `'Civil'` or `'Militar'` (singular, Spanish). Filter comparisons must match verbatim — see `LaboratoryController.filtrar` and `attackQueries.getInfrastructureByType`.
- `Improvements.production_value` is nullable (8 of 18 rows), `price` is not. Nullable columns are `Double` in records, non-null are primitive `double`/`int`.
- Multi-line SQL uses Java text blocks.
- Views exist alongside tables (`totalCountryPopulation`, `militaryInfraestructureCountry`, `TotalCountryDefenses`, `civillianInfraestructureCountry`) and are queried directly.
- `CountryImprovements` exists but has 0 rows and no query class. Nothing persists research progress — in-memory state in `functions/improvementsFunctions/` is the current mechanism.

## Environment notes

- `git` is not on PATH in this environment; don't plan around git commands.
- `sqlite3` is available via the Android SDK (`platform-tools/sqlite3.exe`).

## CI

Two workflows, both with `working-directory: GEOPOLITIK`:

- `.github/workflows/java-ci.yml` — JDK 21 temurin, installs xvfb, runs `xvfb-run mvn -B verify`.
- `.github/workflows/sqlite-ci.yml` — only on `**.db` / `**.sql` changes; runs `PRAGMA integrity_check` on every `*.db` and syntax-checks `*.sql`. It does not build the app.