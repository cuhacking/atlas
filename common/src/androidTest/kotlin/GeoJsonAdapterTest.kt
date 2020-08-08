import com.cuhacking.atlas.db.geoJsonAdapter
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlin.test.Test
import kotlin.test.assertEquals

class GeoJsonAdapterTest {

    @Test
    fun `Check if GeoJsonAdapter encoding and decoding works`() {

        val featureAsObject= Feature(point(-75.0, 45.0))
        val featureAsJson = "{" +
                "\"type\":\"Feature\"," +
                "\"geometry\":" +
                "{" +
                "\"type\":\"Point\"," +
                "\"coordinates\":[-75.0,45.0]}," +
                "\"properties\":{}" +
                "}"

        val encodedFeature = geoJsonAdapter.encode(featureAsObject)
        val decodedString = geoJsonAdapter.decode(featureAsJson)

        assertEquals(encodedFeature,featureAsJson, "Feature conversion to String failed")
        assertEquals(decodedString,featureAsObject, "String conversion to Feature failed")

    }
}

