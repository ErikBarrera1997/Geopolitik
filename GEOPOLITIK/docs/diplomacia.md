# Diplomacy Windows Documentation

> This document describes the diplomacy UI **as it is currently implemented**. Several parts are still placeholders or are not wired to game logic yet; they are listed in [Pending integration](#pending-integration). Update this file as new diplomacy features are added.

## 1. Window overview

| Window | FXML | Controller | Stylesheet | Opened from |
|---|---|---|---|---|
| Diplomacy | `views/diplomacyViews/diplomacyView.fxml` | `controllers.diplomacy.diplomacyCountryController` | `styles/diplomacyStyles/diplomacyStyles.css` | `MenuController.openDiplomacia()` |
| Diplomacy actions | `views/diplomacyViews/diplomacyActions.fxml` | `controllers.diplomacy.diplomacyActionsController` | `styles/diplomacyStyles/diplomacyActionsStyles.css` | Ally / enemy row in the diplomacy window |
| Commercial agreement / treaty | `views/diplomacyViews/tradeView.fxml` | `controllers.diplomacy.treatyController` | `styles/countryStyles/provinceStyles.css` | Any row in `listaDiplomacia` of the diplomacy window |
| War declaration | `views/diplomacyViews/warDeclarationView.fxml` | `controllers.diplomacy.warDeclarationController` | `styles/diplomacyStyles/warDeclarationStyles.css` | "Declarar la guerra" in the enemy action list |
| Cancel alliance | `views/diplomacyViews/cancelTreaty.fxml` | `controllers.diplomacy.cancelTreatyController` | `styles/diplomacyStyles/cancelTreatyStyles.css` | "Romper alianza comercial" / "Romper alianza militar" in the ally action list |
| Country relations | `views/diplomacyViews/countryDiplomacyView.fxml` | `controllers.RelacionesController` | `styles/diplomacyStyles/countryDiplomacyStyles.css` | `CountryViewControllers.openCountryDiplomacia()` |
| Transaction confirmation | `views/diplomacyViews/transactionView.fxml` | `controllers.TreatyConfirmationController` (does not exist) | none | Not referenced by any code |

All paths above are classpath resources under `src/main/resources/com/msservices/geopolitik/`.

## 2. Window flow

```
Main menu
  └─ index 5 → Diplomacy (diplomacyView.fxml)
                 ├─ click row in listaAliados   → Diplomacy actions (ally set)
                 │                                  └─ click "Romper alianza comercial" / "Romper alianza militar" → Cancel alliance (cancelTreaty.fxml)
                 ├─ click row in listaEnemigos  → Diplomacy actions (enemy set)
                 │                                  └─ click "Declarar la guerra" → War declaration (warDeclarationView.fxml)
                 └─ click a listaDiplomacia row → Action window (tradeView.fxml, action-specific)

Country view
  └─ openCountryDiplomacia() → Country relations (countryDiplomacyView.fxml)
```

## 3. Entry points

### Main menu → Diplomacy

`MenuController` maps the lower menu index `5` to `openDiplomacia()` (`MenuController.java:239`). It loads `diplomacyView.fxml`, creates an `APPLICATION_MODAL` `Stage`, sets the owner to `imagenPrincipal`'s window, titles it `"Diplomacia"` and calls `showAndWait()`.

### Country view → Country relations

`CountryViewControllers.openCountryDiplomacia()` loads `countryDiplomacyView.fxml`, calls `RelacionesController.setCountryId(currentCountryId)` and shows the stage modally. This is a separate window from `diplomacyView.fxml` and is not connected to the diplomacy-actions dialog.

## 4. Diplomacy window (`diplomacyView.fxml`)

Controller: `diplomacyCountryController`. The player country is hardcoded:

```java
private static final int COUNTRY_ID = 11; // Israel
```

### FXML elements

| `fx:id` | Type | Behaviour |
|---|---|---|
| `iconSuperior` | `ImageView` | Player flag, 120×60, from `loadFlags` |
| `listaSucesos` | `VBox` inside `ScrollPane` | Declared but **never populated** |
| `listaAliados` | `VBox` inside `ScrollPane` | One row from `CountryDiplomacyStatus.allied` |
| `listaEnemigos` | `VBox` inside `ScrollPane` | Rows from `CountryDiplomacyStatus.enemy` and `in_war` |
| `botonStatus` | `Button` | `onStatusClick()` is empty |
| `listaDiplomacia` | `VBox` | Seven static rows; each row opens `tradeView` with its action |
| `botonAtras` | `Button` | Closes the stage via `goBack()` |

### Data loading

`initialize()` wires the buttons and calls `loadData()`. `loadData()`:

1. Loads the flag for `COUNTRY_ID` through `loadFlags.getFlag(11)` and sets `iconSuperior`.
2. Creates `diplomacyQueries(DatabaseConnection.getInstance().getConnection())`.
3. Calls `getCountryDiplomacyStatus(COUNTRY_ID)`.
4. Clears `listaAliados` and `listaEnemigos`.
5. Adds the ally row, the enemy row and the war row (if their names are non-null).

### Row construction

`buildRow(int countryId, String countryName)` builds every country row imperatively:

- `ImageView` 24×16 with `loadFlags.getFlag(countryId).path()`.
- `Label` with inline style (`-fx-text-fill: #1a3a1a; -fx-font-size: 13px;`).
- `HBox` with spacing 8, `Pos.CENTER_LEFT`, style class `fila-pais`.

### Click behaviour

| Source list | Row data | Action |
|---|---|---|
| `listaAliados` | `status.alliedId()` / `status.alliedName()` | Opens diplomacy actions with the **ally** set |
| `listaEnemigos` | `status.enemyId()` / `status.enemyName()` | Opens diplomacy actions with the **enemy** set |
| `listaEnemigos` | `status.warId()` / `status.warName()` | Opens diplomacy actions with the **enemy** set |

Country rows get `Cursor.HAND`. `openDiplomacyActions(int countryId, String countryName, boolean allied)` loads `diplomacyActions.fxml`, obtains the controller, calls `setContext(countryId, countryName, allied)`, sets the title to `"Acciones diplomáticas - " + countryName`, configures an `APPLICATION_MODAL` stage owned by `botonAtras`'s window and calls `showAndWait()`. The id is kept so the war declaration window can show the selected enemy's flag.

### `listaDiplomacia`

The rows are declared statically in FXML and are not generated from the database:

- Firmar tratado de no agresión
- Acuerdo comercial
- Crear alianza militar
- Solicitar acceso a espacio aéreo
- Enviar apoyo económico
- Enviar apoyo militar
- Enviar ultimátum

All seven rows have a click handler and hand cursor. The handler extracts the row's `Label` text and calls `openTradeView(actionName)`. That method opens `tradeView.fxml` and calls `treatyController.setCountryData(11, 11, actionName)`. The action name personalizes the window; the target country is still **not** propagated (the same `11` is used as source and target).

The stage title is the action name. `treatyController` receives the action name and decides which blocks of `tradeView.fxml` are visible.

## 5. Diplomacy actions window (`diplomacyActions.fxml`)

Controller: `diplomacyActionsController`.

The window contains two independent lists with the same visual component and style (`lista-acciones`). Exactly one is visible at a time, decided by the boolean passed from the diplomacy window.

| `fx:id` | Type | Purpose |
|---|---|---|
| `allyScroll` | `ScrollPane` | Container of the ally list |
| `actionsList` | `VBox` | Ally actions (original component) |
| `enemyScroll` | `ScrollPane` | Container of the enemy list; hidden by default |
| `enemyActionsList` | `VBox` | Enemy actions |
| `backButton` | `Button` | Closes the stage |

### Action sets

Ally context (`setContext(id, name, true)`):

1. Romper alianza comercial
2. Romper alianza militar
3. Revisar tratados
4. Pedir ayuda económica
5. Retirar embajada
6. Pedir ayuda militar

Enemy context (`setContext(id, name, false)`):

1. Declarar la guerra
2. Exigir recursos
3. Pedir tregua
4. Firmar tratado de paz
5. Retirar embajada

`initialize()` fills both `VBox` containers with `buildRow(String)` and calls `applyContext()`. `setContext(int, String, boolean)` stores the selected country id/name and the ally/enemy flag, then calls `applyContext()`, which toggles `visible`/`managed` on `allyScroll` and `enemyScroll`. The id and name are used by the war declaration window.

Both `ScrollPane`s use computed width: `prefWidth` is not set in FXML. The visible list's longest action text determines the width, and the hidden list uses `managed="false"` so it does not affect the calculation.

`buildRow` creates an `HBox` with style class `fila-accion`, a 20×20 `ImageView` using the `"diplomacy"` icon from `loadImages`, and a `Label` with the action text.

Three rows have click handlers: "Declarar la guerra" calls `openWarDeclaration()`; "Romper alianza comercial" and "Romper alianza militar" call `openCancelTreaty(action)`. Both methods load their FXML, pass the player id (`11`) plus the selected country id/name through the controller's setter, build the title as `"<action> - <country>"` and open the window modally over the actions window. The remaining action rows have no handlers yet.

### 5.1 War declaration window (`warDeclarationView.fxml`)

Controller: `warDeclarationController`; stylesheet `warDeclarationStyles.css` (same green/gold palette as the other diplomacy windows).

- `setCountryData(int sourceCountryId, int targetCountryId)` sets `flag1` and `flag2` from `loadFlags` and loads the `military_actions` icon into `iconLabel`.
- The Casus Belli list is a `VBox` (`casusBelliList`) inside a `ScrollPane`; it is currently empty.
- `rejectButton` closes the window through `goBack()`.
- `declareWarButton` has no handler yet; no war state or `CountryDiplomacyStatus` row is modified.
- `declarationLabel` is currently empty.

### 5.2 Cancel alliance window (`cancelTreaty.fxml`)

Controller: `cancelTreatyController`; stylesheet `cancelTreatyStyles.css` (same green/gold palette as the other diplomacy windows).

- `setContext(int sourceCountryId, int targetCountryId, String countryName, String action)` selects the commercial or military wording from `"Romper alianza comercial"` / `"Romper alianza militar"`.
- It sets `titleLabel`, writes the consequence into `infoLabel`, fills `flag1`/`flag2` from `loadFlags`, loads the `diplomacy` icon into `optionIcon`, and adds one `fila-accion` row to `conflictList` describing the affected alliance.
- `rejectButton` closes the window through `goBack()`.
- `acceptButton` has no handler yet; no alliance or `CountryDiplomacyStatus` row is modified.

## 6. Commercial agreement / action window (`tradeView.fxml`)

Controller: `treatyController`. It is opened from any `listaDiplomacia` row of `diplomacyView.fxml`, and its layout depends on the selected action.

### Public API

```java
public void setCountryData(int sourceCountryId, int targetCountryId, String actionName)
public void setActionInfo(String text)
```

`setCountryData` stores both ids and the action name, sets `treatyNameLabel` to the action name, loads both flags and applies the action-specific layout. `loadResources()` runs only for "Acuerdo comercial"; `loadMoney()` runs only for "Enviar apoyo económico". Both list containers (`militaryList`, `ultimatumList`) are cleared. `setActionInfo` fills `actionInfoLabel`; the diplomacy window does not call it yet.

The diplomacy window currently passes `(11, 11, actionName)`, so the target country is a placeholder.

### Action-specific layout

| Action | Resources + % + agreement | Money label + field | Empty list | Gauge |
|---|---|---|---|---|
| Acuerdo comercial | visible | hidden | - | visible |
| Firmar tratado de no agresión | hidden | hidden | - | visible |
| Crear alianza militar | hidden | hidden | - | visible |
| Solicitar acceso a espacio aéreo | hidden | hidden | - | visible |
| Enviar apoyo económico | hidden | visible | - | hidden |
| Enviar apoyo militar | hidden | hidden | `militaryListScroll` | hidden |
| Enviar ultimátum | hidden | hidden | `ultimatumListScroll` (above gauge) | visible |

`applyActionLayout()` toggles `visible` and `managed` on each block so hidden blocks collapse. The gauge block (`gaugeBox`) includes the "Probabilidad de Éxito" title, the gauge bar and the Baja/Alta labels. `actionInfoLabel` is visible for every action and is currently empty.

### FXML elements

| `fx:id` | Type | Use |
|---|---|---|
| `treatyImage` | `Label` | Declared, no image assigned |
| `treatyNameLabel` | `Label` | Set to the selected action name |
| `sourceCountryFlag` | `Label` | Source flag as CSS background image |
| `treatyTypeImage` | `Label` | Declared, no image assigned |
| `targetCountryFlag` | `Label` | Target flag as CSS background image |
| `actionInfoLabel` | `Label` | Common action text; empty until provided |
| `resourcesBox` | `HBox` | Groups `resourceScroll` + percentage field |
| `resourceList` | `VBox` | Resources built imperatively |
| `resourceScroll` | `ScrollPane` | Resource list container |
| `percentField` | `TextField` | Percentage, disabled until a resource is selected |
| `percentLabel` | `Label` | Static `"%"` |
| `agreement` | `TextField` | Generated agreement text |
| `moneyBox` | `HBox` | Groups money label + amount field |
| `moneyLabel` | `Label` | Current country money |
| `moneyField` | `TextField` | Amount to send (integers only) |
| `militaryListScroll` | `ScrollPane` | Container of the empty military support list |
| `militaryList` | `VBox` | Empty for now |
| `ultimatumListScroll` | `ScrollPane` | Container of the empty ultimatum list |
| `ultimatumList` | `VBox` | Empty for now |
| `gaugeBox` | `VBox` | Title + gauge + Baja/Alta labels |
| `signTreatyButton` | `Button` | **Not wired** |
| `backButton` | `Button` | Closes the stage |

The probability gauge nodes (`probabilityGauge`, `gaugeBackground`, `probabilityIndicator`) are inside `gaugeBox`; no probability is calculated yet.

### Money (economic support)

`loadMoney()` uses `countryQueries.getCountryMoney(sourceCountryId)` (sum of `TotalResources.total_amount` where `Resources.name = 'Dinero'`). It displays `moneyLabel` as `"$" + String.format("%.0f", money)`. `moneyField` accepts only digits (up to 9) through a `TextFormatter`.

The new lists (`militaryList`, `ultimatumList`) are empty. Their `ScrollPane`s have a `200px` minimum width and `100px` height so the empty list remains visible.

### Flags

`loadFlags()` uses `loadFlags.getFlag(id)` and applies:

```css
-fx-background-image: url('<flag path>');
-fx-background-size: cover;
-fx-background-repeat: no-repeat;
-fx-background-position: center;
```

### Resources

For `Acuerdo comercial`, `loadResources()` uses `countryResourceQueries.getCountryResources(sourceCountryId)`. For each `countryResource` it builds an `HBox` (`fila-dato`, hand cursor) containing an icon from `loadImages.getIcon(...)` and a `Label` with `resourceName`.

`resourceIcon(String)` maps resource names to `loadImages` keys:

| Resource name | Icon key |
|---|---|
| `Agrícultura` / `Agricultura` | `farming` |
| `Petróleo` | `oil` |
| `Industria` | `industry` |
| `Madera` | `wood` |
| `Gas natural` | `gas` |
| `Pesca` | `fishing` |
| `Oro` | `gold` |
| `Uranio` | `uranium` |
| `Acero` | `steel` |
| `Electricidad` / `electricidad` | `electricity` |
| `Dinero` | `money` |
| anything else | `null` (no icon) |

### Selection and percentage (`Acuerdo comercial` only)

Clicking a resource row calls `selectResource(row, resource)`:

- Clears the previous selection border.
- Marks the row with `-fx-border-color: #1e90ff; -fx-border-width: 2;`.
- Enables `percentField`.
- Calls `updateAgreement()`.

`percentField` uses a `TextFormatter` that accepts only 1–3 digits and values from `1` to `100`. A text listener rebuilds the agreement string as `"<resource>: <percent>%"`, or just `"<resource>"` when the field is empty.

**Not implemented:** `signTreatyButton` has no handler, and no treaty, trade agreement or resource transfer is persisted.

## 7. Country relations window (`countryDiplomacyView.fxml`)

Controller: `RelacionesController`.

- `setCountryId(int countryId)` is called after `FXMLLoader.load()`.
- Loads the country flag into `iconSuperior`.
- Queries `diplomacyQueries.getCountryDiplomacyStatus(countryId)`.
- `listaAliados`: `allied` country.
- `listaEnemigos`: `enemy` and `in_war` countries.
- `listaEstado`: human-readable rows for `in_peace`, `neutral`, `vassal`, `trade_agreement` and `military_alliance` (`"En paz con:"`, `"Neutral:"`, `"Vasallo:"`, `"Acuerdo comercial:"`, `"Alianza militar:"`).
- Rows use `fila-pais`; status rows add a bold relation label.
- No row in this window opens the diplomacy-actions dialog.

## 8. Queries and data access

All query classes receive a `java.sql.Connection` in the constructor. The connection comes from `DatabaseConnection.getInstance().getConnection()`; `DatabaseConnection.init()` is executed by `loadDataBases.load()` during `loadGlobalVariables.load()` at application start.

### `diplomacyQueries`

`getCountryDiplomacyStatus(int idCountry)` reads from `CountryDiplomacyStatus` joined to `Countries`:

- `CountryDiplomacyStatus`: `id_country` (PK), `allied`, `neutral`, `enemy`, `in_peace`, `in_war`, `vassal`, `trade_agreement`, `military_alliance`.
- `Countries`: `id_country`, `name` (used for the country name and each relation name).
- Each relation id is `LEFT JOIN`ed to `Countries` so a missing relation produces `NULL`.
- The table also has `CHECK` constraints preventing a country from being related to itself.

The result is the record `CountryDiplomacyStatus`:

| Field | Meaning |
|---|---|
| `idCountry`, `countryName` | Selected country |
| `alliedId`, `alliedName` | Ally |
| `enemyId`, `enemyName` | Enemy |
| `peaceId`, `peaceName` | In peace with |
| `warId`, `warName` | At war with |
| `neutralId`, `neutralName` | Neutral towards |
| `vassalId`, `vassalName` | Vassal of |
| `tradeId`, `tradeName` | Trade agreement with |
| `militaryId`, `militaryName` | Military alliance with |

`getDiplomacyRate(int idCountry, int neighborId)` reads `DiplomacyRate` (`morale`, `friendship_rate`) and returns the record `DiplomacyRate`. It is **not used** by any diplomacy controller yet.

### `countryResourceQueries`

`getCountryResources(int countryId)` runs:

```sql
SELECT r.name, COALESCE(SUM(tr.total_amount), 0) AS total_amount
FROM TotalResources tr
JOIN Resources r ON r.id_resource = tr.id_resource
WHERE tr.id_country = ?
GROUP BY r.id_resource, r.name
ORDER BY r.name
```

It returns `List<countryResource>`, where `countryResource` is `record countryResource(String resourceName, int amount)`.

### Tables relevant to diplomacy

| Table | Used by |
|---|---|
| `CountryDiplomacyStatus` | `diplomacyQueries.getCountryDiplomacyStatus` |
| `Countries` | Joins for country/relation names; flags are mapped separately |
| `DiplomacyRate` | `diplomacyQueries.getDiplomacyRate` (not used yet) |
| `TotalResources` | `countryResourceQueries.getCountryResources` |
| `Resources` | Resource names for the treaty window |
| `NonAgressionAgreement` | Not used by any diplomacy code yet |
| `ComercialAgreement` | Not used by any diplomacy code yet |
| `InternationalComunity` | Not used by any diplomacy code yet |
| `LastEvents` | Not used; `listaSucesos` is empty |

The working directory must be `GEOPOLITIK/`: `DatabaseConnection` opens `db/middleeastdb.db` without a fallback, and image paths are relative to the same directory.

## 9. Images and styles

### Flags (`init.loadFlags`)

- Reads `img/flags` (falls back to `../img/flags`).
- Maps file names to country ids via a fixed `FLAGS` map: `tukiye=1`, `georgia=2`, `azerbaijan=3`, `armenia=4`, `iraq=5`, `iran=6`, `turkmenistan=7`, `uzbekistan=8`, `afganistan=9`, `lebanon=10`, `israel=11`, `jordania=12`, `kwait=13`, `egypt=14`, `saudi arabia=15`, `bahrain=16`, `qatar=17`, `uae=18`, `oman=19`, `yemen=20`, `cyprus=21`, `syria=22`.
- `getFlag(int)` returns `flag(id, path)` or `null`.

### Icons (`init.loadImages`)

- Diplomacy window/rows: key `"diplomacy"` → `img/icons/diplomacy.jpg` (24×24 in the cache; scaled to 20×20 in action rows).
- Resource icons: `farming`, `oil`, `industry`, `wood`, `gas`, `fishing`, `gold`, `uranium`, `steel`, `electricity`, `money` (all `img/icons/*.png`).
- Missing files are skipped, so `getIcon` can return `null`; callers such as `treatyController` guard against it.

### Stylesheets

- `diplomacyStyles.css`: `panel-diplomacia`, `titulo`, `subtitulo`, `texto-dato`, `icono-blanco`, `lista-scroll`, `lista-diplomacia`, `hbox-fila`, `fila-pais`, `boton-status`, `boton-atras`.
- `diplomacyActionsStyles.css`: `panel-acciones`, `titulo`, `lista-acciones`, `fila-accion`, `boton-atras`.
- `warDeclarationStyles.css`: `main-container`, `flag-label`, `icon-label`, `declaration-label`, `section-title`, `lista-scroll`, `scroll-pane`, `primary-button`, `secondary-button`.
- `cancelTreatyStyles.css`: `main-container`, `titulo`, `flag-label`, `icon-label`, `info-label`, `section-title`, `lista-scroll`, `fila-accion`, `scroll-pane`, `primary-button`, `secondary-button`.
- `countryDiplomacyStyles.css`: `panel-relaciones` and the same list/row classes used by `RelacionesController`.
- `tradeView.fxml` reuses `countryStyles/provinceStyles.css`.

## 10. Pending integration

- `COUNTRY_ID = 11` is hardcoded in `diplomacyCountryController`; the player country is not read from match data.
- `listaSucesos` and `botonStatus` are empty/no-op; `LastEvents` is not queried.
- `listaDiplomacia` is static; all seven rows open `tradeView`, but no action is persisted or validated.
- In the diplomacy-actions window only "Declarar la guerra" has a handler; the other rows are display only and none modify `CountryDiplomacyStatus`.
- In the war declaration window `declareWarButton` is not wired and the casus belli list is empty, so no war is declared.
- In the cancel alliance window `acceptButton` is not wired, so the commercial/military alliance is not actually removed.
- `openTradeView` passes `(11, 11, actionName)` to `treatyController`; the target country clicked in `listaAliados` / `listaEnemigos` is not propagated.
- `signTreatyButton` has no handler; no probability is calculated for the gauge; `treatyImage` and `treatyTypeImage` are never assigned.
- `actionInfoLabel` is empty because `setActionInfo` is not called yet.
- `militaryList` and `ultimatumList` are intentionally empty; no items are loaded.
- The economic support amount is not checked against the displayed money total.
- `tradeController` and `transactionController` are empty classes.
- `transactionView.fxml` points to `controllers.TreatyConfirmationController`, which does not exist in the source tree, and no code loads `transactionView.fxml`.
- `countryDiplomacyView.fxml` / `RelacionesController` does not open the diplomacy-actions window.
- `getDiplomacyRate` and the `DiplomacyRate`, `NonAgressionAgreement`, `ComercialAgreement` and `InternationalComunity` tables are unused.
- No diplomacy action persists changes; there are no INSERT/UPDATE/DELETE statements in the diplomacy query classes.

## 11. File index

```
src/main/java/com/msservices/geopolitik/
  controllers/
    MenuController.java                      # opens diplomacyView
    CountryViewControllers.java               # opens countryDiplomacyView
    RelacionesController.java                 # country relations window
    diplomacy/
      diplomacyCountryController.java         # diplomacy window
      diplomacyActionsController.java         # actions window (ally/enemy sets)
      warDeclarationController.java           # war declaration window
      cancelTreatyController.java             # cancel alliance window
      treatyController.java                   # tradeView
      tradeController.java                    # empty
      transactionController.java              # empty
  connection/queries/diplomacy/
    diplomacyQueries.java
    CountryDiplomacyStatus.java
    DiplomacyRate.java
    countryResourceQueries.java
    countryResource.java
  init/
    loadFlags.java
    loadImages.java

src/main/resources/com/msservices/geopolitik/
  views/diplomacyViews/
    diplomacyView.fxml
    diplomacyActions.fxml
    warDeclarationView.fxml
    cancelTreaty.fxml
    tradeView.fxml
    transactionView.fxml
    countryDiplomacyView.fxml
  styles/diplomacyStyles/
    diplomacyStyles.css
    diplomacyActionsStyles.css
    warDeclarationStyles.css
    cancelTreatyStyles.css
    countryDiplomacyStyles.css
```
