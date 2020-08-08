import com.cuhacking.atlas.db.GeoJsonAdapter
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlin.test.Test
import kotlin.test.assertEquals

class GeoJsonAdapterTest {

    @Test
    fun `Check if GeoJsonAdapter encoding and decoding works`() {

        val featureAsObject= Feature(point(-75.0, 45.0))
        val featureAsJson =
            """
            {   "type": "Feature",
                "geometry": {
                    "type": "Point",
                    "coordinates": [-75.0,45.0]
                },
                "properties": {}
            }
            """.replace(Regex("[\\s\n]"),"")

        val encodedFeature = GeoJsonAdapter.encode(featureAsObject)
        val decodedString = GeoJsonAdapter.decode(featureAsJson)

        assertEquals(encodedFeature,featureAsJson, "Feature conversion to String failed")
        assertEquals(decodedString,featureAsObject, "String conversion to Feature failed")

    }
}

