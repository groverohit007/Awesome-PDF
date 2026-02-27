# Awesome PDF

Android app scaffold with Kotlin + Jetpack Compose + Material 3.

## Implemented
- Navigation flow: `Splash -> Onboarding -> Main`.
- Bottom navigation with 5 tabs: Home, Tools, Files, AI, Settings.
- Merge PDF flow end-to-end:
  - Pick 2+ PDFs from storage.
  - Merge with `WorkManager` in background.
  - Progress updates in UI.
  - Output saved to app documents folder (`/Android/data/com.awesomepdf/files/Documents/merged`).
  - Result screen showing output path.
- Material 3 theme setup.
- Unit tests for `SummarizePdfUseCase`, `EntitlementManager`, and `MergePdfUseCase`.

## Run
1. Open project in Android Studio.
2. Sync Gradle.
3. Run the `app` module on emulator/device.
