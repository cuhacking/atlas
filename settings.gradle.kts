rootProject.name = "Atlas"

include( ":server")

// -Pserveronly=true will only include the server. Used for docker builds
if (startParameter.projectProperties["serveronly"] == null) {
    include(":android", ":common", ":mapbox", ":web")
}