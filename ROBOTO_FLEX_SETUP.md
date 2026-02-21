# Roboto Flex Font Setup

## 📥 Cómo Descargar Roboto Flex

### Opción 1: Google Fonts (Recomendado)
1. Ir a [Google Fonts - Roboto Flex](https://fonts.google.com/specimen/Roboto+Flex)
2. Hacer click en el botón "Download Family"
3. Descargar el archivo ZIP
4. Extraer y copiar estos archivos a `app/src/main/res/font/`:
   - `RobotoFlex-Regular.ttf` → renombrar a `roboto_flex_regular.ttf`
   - `RobotoFlex-Medium.ttf` → renombrar a `roboto_flex_medium.ttf`
   - `RobotoFlex-SemiBold.ttf` → renombrar a `roboto_flex_semibold.ttf`
   - `RobotoFlex-Bold.ttf` → renombrar a `roboto_flex_bold.ttf`

### Opción 2: Desde Terminal (Mac/Linux)
```bash
cd app/src/main/res/font

# Regular
curl -o roboto_flex_regular.ttf https://github.com/google/roboto/releases/download/2023.1/Roboto_Flex-Regular.ttf

# Medium
curl -o roboto_flex_medium.ttf https://github.com/google/roboto/releases/download/2023.1/Roboto_Flex-Medium.ttf

# SemiBold
curl -o roboto_flex_semibold.ttf https://github.com/google/roboto/releases/download/2023.1/Roboto_Flex-SemiBold.ttf

# Bold
curl -o roboto_flex_bold.ttf https://github.com/google/roboto/releases/download/2023.1/Roboto_Flex-Bold.ttf
```

## 📁 Estructura Final

Debería verse así:
```
app/src/main/res/font/
├── roboto_flex_regular.ttf
├── roboto_flex_medium.ttf
├── roboto_flex_semibold.ttf
└── roboto_flex_bold.ttf
```

## ✅ Verificación

Una vez agregados los archivos:
1. Sincroniza el proyecto con Gradle
2. Compila el proyecto - debería funcionar sin errores
3. Ejecuta la app - verás Roboto Flex en toda la UI

## 📝 Nota

El código ya está configurado en:
- `ui/theme/FontFamily.kt` - Define la familia de fuentes
- `ui/theme/Type.kt` - Usa Roboto Flex en toda la tipografía
- `ui/theme/DimensTokens.kt` - Espaciado centralizado (nuevo)
- `ui/theme/Shape.kt` - Bordes más redondeados (actualizado)
