# Atlas (Under Construction :construction: :construction_worker:)

Also known as "the Map Project".

## Getting Started

### Required Tools

This project requires the latest Android Studio Arctic Fox Beta to be installed along with JDK 11 and the latest Android SDK (30).

If developing on macOS or for iOS, the latest version of XCode will also need to be installed.

For development on the web client, the latest version of Node 14 needs to be installed.

### Setup

Include the following in your `local.properties` file:

```properties
mapbox.key=<your mapbox access token>
server.url=<server instance url>
server.web.url=http://localhost:8080/api

mapbox.download.key=<your mapbox download token>
# This line is required for downloading the Mapbox cocoapods
mmapp.config.netrc=true
```

### Building

- **Android**: Open project folder in Android Studio and execute the `android` run configuration.
- **iOS**: Open the `.xcworkspace` file in XCode or AppCode and run.
- **Web**:
  - Run `npm install` in the `web` directory
  - Run `./gradlew build` in the home directory
  - Run `yarn` in the `web` directory
  - Then run `yarn start` in the `web` directory

## Project Structure

This project is a Kotlin Multiplatform project targeting Android, iOS, and Web.

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
