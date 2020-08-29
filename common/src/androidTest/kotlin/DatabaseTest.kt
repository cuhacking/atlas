import com.cuhacking.atlas.db.*
import com.cuhacking.atlas.db.Database.Companion.Schema
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class DatabaseTest {

    lateinit var driver: SqlDriver
    lateinit var database: Database

    private val sampleFeature = GeoJsonAdapter.decode("""{"type":"Feature","geometry":{"type":"Point","coordinates":[-75.0,45.0]},"properties":{}}""")
    private val feature1 = Feature(1, "River Building","Richcraft Hall","Building",null,null,null,sampleFeature)
    private val feature2 = Feature(2, "RB 2311", null, "room","River Building", "RB2",null,sampleFeature)
    private val feature3 = Feature(3, "HS 1301B", null, "room","Health Sciences Building", "HS1",null,sampleFeature)

    private fun featureToFTS(feature: Feature) = (Feature_fts(feature.common_name,feature.secondary_name,feature.type,feature.building,feature.floor,feature.search_tags))

    @Before
    fun setup() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        Schema.create(driver)
        database = driver.createDatabase()

        database.featureQueries.run {
            insertFeature(feature1)
            insertFeature(feature2)
        }
    }

    @After
    fun close(){
        driver.close()
    }

    @Test
    fun `check that inserting into feature also inserts into feature_fts`(){
        database.featureQueries.insertFeature(feature3)

        assertTrue(database.featureQueries.search("HS 1301B").executeAsList()
                .contains(featureToFTS(feature3))
        )
    }

    @Test fun `check that search query works`(){
        assertTrue(database.featureQueries.search("river").executeAsList().containsAll(
                listOf(featureToFTS(feature1), featureToFTS(feature2))
        ))
    }

    @Test fun `check that clearing feature_fts works`(){
        database.featureQueries.clearFts()
        assertTrue(database.featureQueries.search("river").executeAsOneOrNull() == null)
    }
}