# Awesome PDF

Initial Android project structure using Kotlin + Jetpack Compose + Material 3.

## Implemented in this step
- App entry + Material 3 Compose theme.
- Navigation flow: `Splash -> Onboarding -> Main`.
- Main area with bottom navigation (5 tabs): Home, Tools, Files, AI, Settings.
- Placeholder screens for all tabs.

## Project layout
- `app/src/main/java/com/awesomepdf/presentation/navigation` - navigation graph
- `app/src/main/java/com/awesomepdf/presentation/splash` - splash screen
- `app/src/main/java/com/awesomepdf/presentation/onboarding` - onboarding screen
- `app/src/main/java/com/awesomepdf/presentation/main/*` - tab placeholder screens
- `app/src/main/java/com/awesomepdf/ui/theme` - Material 3 theme files

## Run
1. Open in Android Studio.
2. Sync Gradle.
3. Run the `app` configuration on an emulator/device.
