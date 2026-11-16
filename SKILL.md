# Project Skill Guidelines

## Rules
- **Code style**: Use `camelCase` for code identifiers (methods, variables, fields, parameters, packages/classes follow existing project conventions where already established).
- **Global variables and constants**: Use uppercase with underscores (`VARIABLE_GLOBAL`).
- **Comments**: Only comment important functions and methods. Avoid obvious or redundant comments.
- **Comment language**: Write comments in English.

## UI consistency (windows/views)
- New or modified windows must keep the same colors, fonts, styles, shading, borders, spacing, and control treatments as the existing views to maintain visual consistency.
- Reuse the established style classes/patterns instead of introducing ad-hoc inline styling when an equivalent class already exists.
- Use `countryView` as the reference example:
  - View: `GEOPOLITIK/src/main/resources/com/msservices/geopolitik/views/countryViews/countryView.fxml`
  - Stylesheet: `GEOPOLITIK/src/main/resources/com/msservices/geopolitik/styles/countryStyles/countryStyles.css`
- Reference palette and treatments from `countryStyles.css`:
  - Root/background: font `"Georgia"` with dark green backgrounds (e.g. `#2e4e2e`, panels like `#3a5a3a`, borders `#1a3a1a`).
  - Accents/content surfaces: parchment/gold tones (e.g. lists `#d9c97a`, rows `#c4b06a`, values `#d9c97a`), light text `#f0ead0`.
  - Buttons: gold gradient treatment (e.g. `.boton-atras` `linear-gradient(to bottom, #c49a3a, #a8842a)` with hover/pressed states), dark border, rounded corners, hand cursor.
  - Depth/shading: consistent `dropshadow(...)` effects on panels, controls, and titles (as in `.panel-mapa`, `.bandera`, `.lista-provincias`, `.boton-atras`, `.titulo`).
  - Typography: clear hierarchy via `.titulo`, `.subtitulo`, `.label`, `.valor`; keep font sizes/weights consistent with existing views.
  - Interactive rows/lists: bordered, rounded, padded containers with hover/pressed feedback (e.g. `.fila-accion`, `.lista-provincias`).
- Match the equivalent component styles of the closest existing view when `countryView` is not the direct analogue (e.g. combat, diplomacy, laboratory, weapons views) so all windows feel part of the same application.
