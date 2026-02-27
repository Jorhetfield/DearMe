# Git Flow Structure - DearMe

Este proyecto sigue el modelo **Git Flow** para la gestión de ramas y versionado.

## Estructura de Ramas

### Ramas Principales

- **`main`** - Rama de producción
  - Contiene código estable y en producción
  - Solo recibe merges desde `release/` y `hotfix/`
  - Cada commit en `main` debe ser un release

- **`develop`** - Rama de desarrollo
  - Rama de integración para features
  - Contiene la próxima versión en desarrollo
  - Base para todas las feature branches

### Ramas de Soporte

- **`feature/nombre-feature`** - Desarrollo de nuevas features
  - Se crean desde: `develop`
  - Se mergean hacia: `develop`
  - Nomenclatura: `feature/descripcion-corta` (ej: `feature/add-notification-system`)

- **`release/X.X.X`** - Preparación de releases
  - Se crean desde: `develop`
  - Se mergean hacia: `main` y `develop`
  - Nomenclatura: `release/1.0.0` (sigue semver)
  - Solo se permiten bug fixes

- **`hotfix/X.X.X`** - Parches de producción
  - Se crean desde: `main`
  - Se mergean hacia: `main` y `develop`
  - Nomenclatura: `hotfix/1.0.1`
  - Para bugs críticos en producción

## Flujo de Trabajo

### Empezar una Feature

```bash
# Actualizar develop
git checkout develop
git pull origin develop

# Crear feature branch
git checkout -b feature/descripcion-feature
```

### Terminar una Feature

```bash
# Asegurar que está actualizada
git pull origin develop

# Mergear a develop con pull request
git checkout develop
git pull origin develop
git merge --no-ff feature/descripcion-feature
git push origin develop

# Eliminar feature branch
git branch -d feature/descripcion-feature
git push origin --delete feature/descripcion-feature
```

### Crear un Release

```bash
git checkout develop
git pull origin develop
git checkout -b release/X.X.X

# Hacer cambios de versión y bug fixes
git commit -m "Bump version to X.X.X"

# Mergear a main
git checkout main
git pull origin main
git merge --no-ff release/X.X.X
git tag -a vX.X.X -m "Release version X.X.X"
git push origin main
git push origin vX.X.X

# Mergear de vuelta a develop
git checkout develop
git pull origin develop
git merge --no-ff release/X.X.X
git push origin develop

# Eliminar release branch
git branch -d release/X.X.X
git push origin --delete release/X.X.X
```

### Crear un Hotfix

```bash
git checkout main
git pull origin main
git checkout -b hotfix/X.X.X

# Hacer el fix
git commit -m "Fix: descripción del problema"

# Mergear a main
git checkout main
git merge --no-ff hotfix/X.X.X
git tag -a vX.X.X -m "Hotfix version X.X.X"
git push origin main
git push origin vX.X.X

# Mergear a develop
git checkout develop
git pull origin develop
git merge --no-ff hotfix/X.X.X
git push origin develop

# Eliminar hotfix branch
git branch -d hotfix/X.X.X
git push origin --delete hotfix/X.X.X
```

## Convenciones de Commits

- Usar mensajes descriptivos en inglés o español consistentemente
- Formato: `[tipo]: descripción`
- Tipos: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
- Ejemplo: `feat: add Firebase push notifications`

## Notas Importantes

- Siempre usar `--no-ff` al mergear para mantener el historial
- Las feature branches son efímeras, solo develop y main son permanentes
- Crear pull requests para code reviews antes de mergear
- Nunca hacer push directo a `main` o `develop`
