import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.rdd.RDD;
import org.apache.spark.*;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import static org.elasticsearch.spark.rdd.api.java.JavaEsSpark.*;
import java.util.List;
import java.util.Map;

public class testESspark {
    public static void main(String[] args) {

        SparkConf conf = new SparkConf()
                .setAppName("test")
                .setMaster("local[*]")
                .set("spark.driver.allowMultipleContexts", "true")
                .set("es.index.auto.create", "true")
                .set("es.resource", "/gtordjma/tutoriels")
                .set("es.port", "9200")
                .set("es.query", "?q=*");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        JavaRDD<Map<String, Object>> esRDD =
                JavaEsSpark.esRDD(jsc, "gtordjma/tutoriels", "?q=*").values();

        System.out.println(esRDD.rdd().count());
        System.out.println(esRDD.rdd().collect().toString());

    }
}
