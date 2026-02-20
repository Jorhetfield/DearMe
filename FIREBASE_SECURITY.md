# Firebase Security Setup

## ⚠️ IMPORTANTE: google-services.json

El archivo `google-services.json` contiene **credenciales sensibles de Firebase** y **NUNCA debe ser committeado a control de versiones**.

### ✅ Ya realizado:
- ✓ `.gitignore` actualizado para ignorar `google-services.json`
- ✓ Firebase plugin habilitado en `build.gradle.kts`
- ✓ Dependencias de Firebase configuradas

### 📝 Setup local necesario:

1. **Descarga tu `google-services.json`** desde Firebase Console:
   - Ve a Project Settings en Firebase Console
   - Descarga el archivo `google-services.json`
   - Colócalo en `app/google-services.json` (ya existe un placeholder)

2. **Verifica que no se commit**:
   ```bash
   git status  # No debe mostrar app/google-services.json
   ```

3. **Si localmente necesitas el archivo**:
   ```bash
   # El archivo no se trackeará con git
   # Pero Android Studio lo leerá automáticamente
   ```

### 🔒 Otros archivos sensibles ignorados:

Estos archivos también están protegidos en `.gitignore`:
- `.env` - Variables de entorno
- `secrets.properties` - Propiedades secretas
- `signing.properties` - Configuración de firma
- `*.key` - Claves privadas
- `*.p12` - Certificados PKCS12
- `*.pfx` - Certificados PFX

### ⚠️ Limpieza del histórico (opcional pero recomendado):

Si anteriormente se committeó `google-services.json`, puedes limpiarlo del histórico usando:

```bash
# Opción 1: Usar git-filter-repo (recomendado)
pip install git-filter-repo
git filter-repo --path app/google-services.json --invert-paths

# Opción 2: Usar BFG Repo-Cleaner
bfg --delete-files google-services.json

# Opción 3: Fuerza push (solo si trabajas solo)
git push --force-with-lease
```

### ✅ Checklist de seguridad:

- [ ] `google-services.json` está en `.gitignore`
- [ ] El archivo local existe en `app/google-services.json`
- [ ] `git status` no muestra `google-services.json`
- [ ] Firebase plugin está habilitado en gradle
- [ ] Dependencias de Firebase están configuradas
- [ ] Nunca hacer `git add -f app/google-services.json`
- [ ] Nunca hacer `git add .` en la raíz del proyecto (podría includirlo accidentalmente)

### 🚀 Siguiente: Implementar autenticación Firebase

Ahora puedes usar Firebase en tu proyecto:

1. **Auth**: Para login/registro de usuarios
2. **Firestore**: Para almacenamiento de cápsulas en la nube
3. **Storage**: Para almacenar media (fotos, audios)

---

**Última actualización**: Febrero 2026
