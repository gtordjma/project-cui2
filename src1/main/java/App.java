import java.util.HashMap;
import java.util.Map;
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

public class App {
    App(){

    }

    public static void main(String[] args) {
        staticFileLocation("/public");

        get("/hello", (req, res) -> "Hello World");

    }
}
