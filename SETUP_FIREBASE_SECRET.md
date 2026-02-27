# 🔐 Configurar Firebase Secret en GitHub

Este documento explica cómo configurar el `google-services.json` en GitHub Actions sin subirlo al repositorio.

## ¿Cómo funciona?

1. Tu archivo `google-services.json` **nunca se sube al repo** (está en `.gitignore`)
2. Lo guardas en **GitHub Secrets** (encriptado)
3. GitHub Actions lo **inyecta automáticamente** antes de compilar
4. El APK se compila con tus credenciales de Firebase

---

## 📋 Paso a Paso

### 1. Obtén tu `google-services.json`

Si ya lo tienes localmente:
```bash
# Ya lo tienes en:
app/google-services.json
```

Si no lo tienes:
1. Ve a [Firebase Console](https://console.firebase.google.com)
2. Selecciona tu proyecto "DearMe"
3. Project Settings (⚙️) → Your apps → Android
4. Descarga `google-services.json`

---

### 2. Copia el contenido del archivo

```bash
# En macOS/Linux:
cat app/google-services.json | pbcopy

# En Windows:
type app\google-services.json | clip

# O simplemente abre el archivo y copia su contenido
```

---

### 3. Guarda en GitHub Secrets

1. Ve a tu repositorio en GitHub
2. **Settings** → **Secrets and variables** → **Actions**
3. Click en **"New repository secret"**
4. **Name:** `GOOGLE_SERVICES_JSON`
5. **Value:** Pega el contenido completo del archivo JSON
6. Click **"Add secret"**

✅ ¡Listo! El secret está guardado de forma segura.

---

### 4. Verifica que el workflow esté actualizado

El workflow ya está configurado para usar el secret. Puedes ver en `.github/workflows/build-apk.yml`:

```yaml
- name: Create google-services.json from secret
  if: secrets.GOOGLE_SERVICES_JSON != ''
  run: echo '${{ secrets.GOOGLE_SERVICES_JSON }}' > app/google-services.json
```

---

## 🔄 Ahora cuando hagas push/release:

### **Opción 1: Build automático en cualquier push**

```bash
git push origin feature/nueva-feature
# GitHub Actions detecta el push
# → Crea el google-services.json desde el secret
# → Compila Debug APK
# → Lo sube como artifact
```

Descarga el APK:
1. Ve a tu repo → **Actions** tab
2. Selecciona el workflow más reciente
3. Descarga `dearme-debug-apk` en Artifacts

---

### **Opción 2: Release automático con tag**

```bash
# Crea un tag para una release
git tag -a v1.0.0 -m "Version 1.0.0"
git push origin v1.0.0

# GitHub Actions detecta el tag
# → Crea el google-services.json desde el secret
# → Compila Release APK optimizado
# → Sube automáticamente a GitHub Releases
```

---

## ✅ Verificación

Para verificar que todo funciona:

1. Haz un pequeño cambio en el código
2. Push a tu rama
3. Ve a **Actions** tab
4. Verifica que el workflow:
   - ✅ Crea `google-services.json`
   - ✅ Compila sin errores
   - ✅ Genera el APK

Si ves en los logs:
```
✓ Create google-services.json from secret
✓ Build Debug APK
✓ app-debug.apk (XXX MB)
```

¡Perfecto! ✨

---

## 🚨 Troubleshooting

### "Falta google-services.json"
- Verifica que el secret `GOOGLE_SERVICES_JSON` esté creado
- Comprueba que tenga el contenido completo del JSON
- El workflow debería crear el archivo automáticamente

### "Error: Invalid JSON"
- Asegúrate de que el secret contiene el JSON **completo**
- No debe tener saltos de línea extras al inicio/final
- Intenta copiar de nuevo el contenido

### "Archivo se sube accidentalmente"
- Verifica `.gitignore` tiene: `google-services.json`
- Si ya está en el repo, necesitas limpiarlo:
  ```bash
  git rm --cached app/google-services.json
  git commit -m "Remove google-services.json from tracking"
  ```

---

## 🔒 Seguridad

- ✅ El JSON **nunca aparece en los logs** de GitHub Actions
- ✅ Solo se usa para compilar, no se sube a ningún lado
- ✅ Solo los colaboradores con acceso al repo pueden ver los secrets
- ✅ GitHub encripta los secrets
- ✅ Cada build crea un archivo temporal que se borra después

---

## 📝 Notas

- **Local development:** Mantén tu `app/google-services.json` local (no commitees)
- **Múltiples entornos:** Puedes crear secrets separados (ej: `GOOGLE_SERVICES_JSON_PROD`, `GOOGLE_SERVICES_JSON_DEV`)
- **Renovar credenciales:** Si cambias el JSON en Firebase, actualiza el secret en GitHub

---

¿Preguntas? Abre un [Issue](../../issues) en el repositorio.