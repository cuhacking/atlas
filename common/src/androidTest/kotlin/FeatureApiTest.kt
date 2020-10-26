import com.cuhacking.atlas.common.FeatureApi
import com.cuhacking.atlas.db.Database
import com.cuhacking.atlas.db.GeoJsonAdapter
import com.cuhacking.atlas.db.Feature
import com.cuhacking.atlas.db.createDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MaxLineLength")
class FeatureApiTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: Database
    private val sampleFeatureCollection: String =
        """{
                "type": "FeatureCollection",
                "features": [
                    {
                        "type": "Feature",
                        "geometry":
                        {
                            "type": "Point",
                            "coordinates": [-75, 45]
                        },
                        "properties":
                        {
                            "id": 1,
                            "name": "sample name",
                            "type": "sample type"
                            
                        }
                    },
                    {
                        "type": "Feature",
                        "geometry":
                        {
                            "type": "Point",
                            "coordinates": [-75, 45]
                        },
                        "properties":
                        {
                            "id": 2,
                            "name": "sample name",
                            "type": "sample type",
                            "building": "building",
                            "floor": "floor"
                        }
                    }
                ]
            }"""
    private val feature1Json = GeoJsonAdapter.decode("""{"type":"Feature","geometry":{"type":"Point","coordinates":[-75.0,45.0]},"properties":{"id":1,"name":"sample name","type":"sample type"}}""")
    private val feature1 = Feature(1, "sample name", null, "sample type", null, null, "sample name", feature1Json)
    private val feature2Json = GeoJsonAdapter.decode("""{"type":"Feature","geometry":{"type":"Point","coordinates":[-75.0,45.0]},"properties":{"id":2,"name":"sample name","type":"sample type","building":"building","floor":"floor"}}""")
    private val feature2 = Feature(2, "sample name", null, "sample type", "building", "floor", "sample name", feature2Json)
    private val featureList = mutableListOf(feature1, feature2)

    private val client = HttpClient(MockEngine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        engine {
            addHandler {
                respond(sampleFeatureCollection, HttpStatusCode.OK, headersOf("Content-Type", ContentType.Application.Json.toString()))
            }
        }
    }
    private val badClient = HttpClient(MockEngine) {
        engine {
            addHandler {
                respond("Error", HttpStatusCode.BadRequest)
            }
        }
    }

    @Before
    fun setup() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        Database.Schema.create(driver)
        database = driver.createDatabase()

        database.featureQueries.run {
            deleteAll()
            clearFts()
        }
    }

    @After
    fun close() {
        driver.close()
    }

    @Test
    fun `check that data is downloaded and inserted to database`() = runBlocking {
        FeatureApi(database, client).getAndStoreFeatures()
        assertEquals(featureList, database.featureQueries.getAll().executeAsList())
    }

    @Test
    fun `check that network error is handled`() = runBlocking {
        FeatureApi(database, badClient).getAndStoreFeatures()
    }
}
