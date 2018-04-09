import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        System.out.println("HELLO");
        SparkConf conf = new SparkConf().setAppName("test").setMaster("local[*]");
        conf.set("spark.driver.allowMultipleContexts", "true");
        conf.set("es.index.auto.create", "true");
        conf.set("es.resource", "/gtordjma/tutoriels");
/*        conf.set("es.nodes", "192.168.0.20");
        conf.set("es.port", "9200");*/
        conf.set("es.port", "9200");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        System.out.println("HELLO");

/*        JavaRDD<Map<String, Object>> esRDD =
                JavaEsSpark.esRDD(jsc, "gtordjma/tutoriels", "?q=PHP*").values();*/

        JavaPairRDD<String, Map<String, Object>> esRDD =
                JavaEsSpark.esRDD(jsc, "gtordjma/tutoriels");

        JavaRDD<Map<String, Object>> v = esRDD.values();
        
        v.collect();
/*        List<Map<String, Object>> lst = v.collect();
        System.out.println("HELLO");

        for (int i = 0; i < lst.size(); i++) {
            Map<String, Object> map = lst.get(i);
            for (String key : map.keySet()) {

                System.out.println("treeMap: [key: " + key + " , value: " + map.get(key) + "]");
            }

        }*/

/*        System.out.println("HELLO");

        List<Map<String, Object>> lst = esRDD.collect();
        System.out.println("HELLO");

        for (int i = 0; i < lst.size(); i++) {
            Map<String, Object> map = lst.get(i);
            for (String key : map.keySet()) {

                System.out.println("treeMap: [key: " + key + " , value: " + map.get(key) + "]");
            }

        }
        */
    }
}
