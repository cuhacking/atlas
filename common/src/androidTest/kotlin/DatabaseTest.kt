import com.cuhacking.atlas.db.*
import com.cuhacking.atlas.db.Database.Companion.Schema
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

@Suppress("MaxLineLength")
class DatabaseTest {

    lateinit var driver: SqlDriver
    lateinit var database: Database

    private val sampleFeature = GeoJsonAdapter.decode("""{"type":"Feature","geometry":{"type":"Point","coordinates":[-75.0,45.0]},"properties":{}}""")
    private val feature1 = Feature(1, "River Building","Richcraft Hall","Building",null,null,null,sampleFeature)
    private val feature2 = Feature(2, "RB 2311", null, "room","River Building", "RB2",null,sampleFeature)
    private val feature3 = Feature(3, "HS 1301B", null, "room","Health Sciences Building", "HS1",null,sampleFeature)

    private fun Feature.toFTS() = (Feature_fts(common_name,secondary_name,type,building,floor,search_tags))

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
                .contains(feature3.toFTS())
        )
    }

    @Test fun `check that search query works`(){
        assertTrue(database.featureQueries.search("river").executeAsList().containsAll(
                listOf(feature1.toFTS(), feature2.toFTS())
        ))
    }

    @Test fun `check that clearing feature_fts works`(){
        database.featureQueries.clearFts()
        assertTrue(database.featureQueries.search("river").executeAsOneOrNull() == null)
    }
}
