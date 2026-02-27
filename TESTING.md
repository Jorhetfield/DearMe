# 🧪 Testing Guide - DearMe

## 📥 How to Download and Install the APK

### Method 1: From GitHub Releases (Recommended for Team Members)

1. **Navigate to Releases**
   - Go to your repository
   - Click on the **Releases** tab on the right sidebar

2. **Download the APK**
   - Find the latest release
   - Scroll down to **Assets**
   - Click on `dearme-release.apk` to download

3. **Transfer to Your Device**
   - Via USB cable: Drag the file to your device's Downloads folder
   - Via Email/Drive: Send yourself the file and download on device
   - Via AirDrop (iOS): Not applicable for Android

4. **Install on Device**
   - Open **Files** app on your Android device
   - Navigate to Downloads
   - Tap the `dearme-release.apk` file
   - Tap **Install**
   - If prompted: Enable "Install from Unknown Sources"
     - Settings → Security → Unknown Sources (enable)
     - Then try installing again
   - Wait for installation to complete
   - Tap **Open** to launch the app

---

### Method 2: From GitHub Actions (Latest Build)

For the most recent build from development:

1. **Go to Actions Tab**
   - Repository → **Actions** tab
   - Select the latest successful workflow run

2. **Download Artifacts**
   - Scroll to **Artifacts** section
   - Download `dearme-debug-apk` or `dearme-release-apk`

3. **Follow same installation steps as Method 1**

---

### Method 3: Build Yourself (For Developers)

If you want to build from source:

```bash
# Clone the repo
git clone https://github.com/Jorhetfield/DearMe.git
cd DearMe

# Build debug APK
./gradlew assembleDebug

# Build release APK (optimized)
./gradlew assembleRelease

# APK location: app/build/outputs/apk/[debug|release]/app-[debug|release].apk
```

Then install using `adb`:
```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

---

## 🧪 Testing Checklist

### 🔐 Authentication
- [ ] Sign up with new email
- [ ] Login with existing account
- [ ] Logout functionality works
- [ ] Password reset flow
- [ ] Firebase authentication sync

### ✉️ Capsule Creation
- [ ] Create new capsule
- [ ] Add text content
- [ ] Set unlock date (past, future, today)
- [ ] Add images/attachments
- [ ] Save capsule locally
- [ ] Sync to Firebase

### 👁️ Capsule Viewing
- [ ] View all capsules in vault
- [ ] Open unlocked capsules
- [ ] Attempt to open locked capsules (should be blocked)
- [ ] View capsule details
- [ ] Edit capsule (if allowed)
- [ ] Delete capsule

### 🔔 Notifications
- [ ] Notification permission request
- [ ] Receive notification when capsule unlocks
- [ ] Tap notification → navigate to capsule
- [ ] Notification appears in system tray

### 👤 User Profile
- [ ] View profile information
- [ ] Edit profile (name, picture, etc)
- [ ] See capsule statistics
- [ ] Account settings

### 🎨 UI/UX
- [ ] App opens without crashes
- [ ] Navigation between screens smooth
- [ ] Material Design 3 colors display correctly
- [ ] Responsive layout on different screen sizes
- [ ] Proper loading indicators
- [ ] Error messages are clear

### 🔄 Edge Cases
- [ ] No internet connection handling
- [ ] Low battery mode
- [ ] Screen rotation (portrait/landscape)
- [ ] Minimize and return to app
- [ ] Device sleep/wake
- [ ] Multiple sign-ins from different devices

---

## 🐛 Reporting Bugs

Found an issue? Please create a detailed GitHub Issue:

### Issue Template:
```
**Device Info**
- Device: [e.g., Samsung Galaxy S23]
- Android Version: [e.g., 13.0]
- App Version: [e.g., 1.0]

**Steps to Reproduce**
1. First step
2. Second step
3. Expected result
4. Actual result

**Screenshots**
[Attach if applicable]

**Logs**
[Attach if available: adb logcat output]
```

---

## 📱 Device Recommendations

### Minimum for Testing
- **Android 8.0 (API 26)** emulator or device
- **2GB RAM** minimum
- **Internet connection**

### Recommended
- Real device (not emulator only)
- **Android 13+** device
- Various screen sizes (phone, tablet)
- Test on both WiFi and cellular data

---

## 🆘 Troubleshooting

### "App not installed"
- Clear app cache: Settings → Apps → DearMe → Clear Cache
- Try Debug APK instead of Release
- Check storage space (needs at least 100MB free)

### "Unknown sources disabled"
- Settings → Security → Toggle "Unknown Sources" ON
- Try again with APK

### "App crashes on startup"
- Check logs: `adb logcat | grep dearme`
- Clear app data: Settings → Apps → DearMe → Clear Data
- Rebuild and reinstall

### "Notifications not working"
- Check notification permission granted: Settings → Apps → DearMe → Permissions
- Enable notifications in app settings
- Device shouldn't have "Do Not Disturb" enabled

### "Firebase errors"
- Check internet connection
- Ensure device time is synchronized
- Check if Firebase project is configured correctly

---

## 📊 Test Results Template

When reporting testing results, please use this template:

```
## Test Report - Version 1.0
**Tester:** [Name]
**Device:** [Device Name & Android Version]
**Date:** [Date]
**Build:** [APK filename/version]

### Results Summary
- Total Tests: X
- Passed: X
- Failed: X
- Issues: [List any critical issues]

### Detailed Results
[Include checklist results and any notes]
```

---

## 📞 Need Help?

- Check [README.md](README.md) for general information
- Open a GitHub Issue for bugs
- Check existing issues for similar problems
- Review logs using: `adb logcat -c && adb logcat | grep dearme`

---

**Happy Testing! 🎉**