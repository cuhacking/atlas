# Atlas (Under Construction :construction: :construction_worker:)

Also known as "the Map Project".

## Getting Started

### Required Tools

This project requires the latest Android Studio 4.2 Canary to be installed along with JDK 8 and the latest Android SDK (29).

If developing on macOS or for iOS, the latest version of XCode will also need to be installed.

For development on the web client, the latest version of Node 12 needs to be installed.

### Building

- **Android**: Open project folder in Android Studio and execute the `android` run configuration.
- **iOS**: Open the `.xcworkspace` file in XCode or AppCode and run.
- **Web**:
  - Run `./gradlew :common:jsBrowserDevelopmentWebpack` in the root directory
  - Run `yarn install` in the `web` directory
  - Then run `yarn start` in the `web` directory

## Project Structure

This project is a Kotlin Multiplatform project targetting Android, iOS, and Web.

```
.
├── README.md
├── android     - Android App
├── buildSrc    - Gradle build configuration (dependency and plugin versions)
├── common      - Kotlin Multiplatform module for apps
├── ios         - iOS App
├── mapbox      - Multiplatform Mapbox bindings
└── web         - Web App
```
