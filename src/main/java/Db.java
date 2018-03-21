import java.util.HashMap;
import java.util.Map;
import org.apache.spark.sql.*;
import java.io.InputStream;

import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import  org.apache.spark.sql.types.*;
import org.postgresql.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import org.apache.commons.lang3.*;
import java.util.ArrayList;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import static j2html.TagCreator.*;
import static spark.Spark.*;



import static spark.Spark.*;
import static spark.Spark.*;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;



import static spark.Spark.*;

public class Db {
    //Class App
    public static void main(String[] args) {
        staticFileLocation("/public");
        /*SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();*/

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("Word Count")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();

        Dataset<Row> df = spark.read().json("src/main/resources/json/data.json");
        df.show();
        /*Dataset<Row> df = spark.read().json("/json/data.json");
        df.show();*/
    }
}
/*public class Db {


    Db() {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        Map<String, String> options = new HashMap<String, String>();
        options.put("url", "jdbc:postgresql://localhost:5432/dbname?user=postgresUser&password=rootPassword");
        options.put("dbtable", "test");
        options.put("driver", "org.postgresql.Driver");


        // SQL context create
        SQLContext sqlContext = new SQLContext(jsc);
        // establish JDBC connection and load table data in Spark DataFrame
        DataFrame dframe = sqlContext.load("jdbc", conOptions);
        //  display table data
        dframe.show();


        // Register as table
        dframe.registerTempTable("tempdata");
        // execute query
        DataFrame dataCount = sqlContext.sql("select id, count(*) as total from tempdata group by id");
    }
}*/
