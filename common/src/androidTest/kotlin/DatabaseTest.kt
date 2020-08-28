import com.cuhacking.atlas.db.*
import com.cuhacking.atlas.db.Database.Companion.Schema
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

lateinit var driver: SqlDriver
lateinit var queries: FeatureQueries

class DatabaseTest {

    private val sampleFeature = GeoJsonAdapter.decode("""{"type":"Feature","geometry":{"type":"Point","coordinates":[-75.0,45.0]},"properties":{}}""")

    @Before
    fun setup() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        Schema.create(driver)
        val database = driver.createDatabase()
        queries = database.featureQueries

        val feature1 = Feature(1, "River Building","Richcraft Hall","Building",null,null,null,sampleFeature)
        val feature2 = Feature(2, "RB 2311", null, "room","River Building", "RB2",null,sampleFeature)

        queries.insertFeature(feature1)
        queries.insertFeature(feature2)
    }

    @After
    fun close(){
        driver.close()
    }

    @Test
    fun `check that inserting into feature also inserts into feature_fts`(){
        val feature3 = Feature(3, "HS 1301B", null, "room","Health Sciences Building", "HS1",null,sampleFeature)
        queries.insertFeature(feature3)

        assertTrue(queries.search("HS 1301B").executeAsList()
                .contains(
                    Feature_fts(
                            common_name= "HS 1301B",
                            secondary_name= null,
                            type= "room",
                            building= "Health Sciences Building",
                            floor= "HS1",
                            search_tags= null
                    )

                )
        )
    }

    @Test fun `check that search query works`(){
        assertTrue(queries.search("river").executeAsList().containsAll(
                listOf(
                        Feature_fts(
                            common_name= "River Building",
                            secondary_name= "Richcraft Hall",
                            type= "Building",
                            building= null,
                            floor= null,
                            search_tags= null
                        ),
                        Feature_fts(
                                common_name= "RB 2311",
                                secondary_name= null,
                                type= "room",
                                building= "River Building",
                                floor= "RB2",
                                search_tags= null
                        )
                )
        ))
    }

    @Test fun `check that clearing feature_fts works`(){
        queries.clearFts()
        assertTrue(queries.search("river").executeAsOneOrNull() == null)
    }
}