import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Redirect;
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

public class App {
//Class App
/*    public static void main(String[] args) {
        staticFileLocation("/public");

        get("/", (request, response) -> { //request for route happens at this location
            Map<String, Object> model = new HashMap<String, Object>(); // new model is made to store information
            return new ModelAndView(model, "hello.hbs"); // assemble individual pieces and render
        }, new HandlebarsTemplateEngine()); //

        get("/favorite_photos", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "favorite_photos.hbs");
        }, new HandlebarsTemplateEngine());

        get("/form", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "form.hbs");
        }, new HandlebarsTemplateEngine());

        get("/login", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "login.hbs");
        }, new HandlebarsTemplateEngine());



        post("/greeting_card", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String recipient = StringEscapeUtils.escapeHtml4(request.queryParams("recipient"));
            String sender = request.queryParams("sender");
            model.put("recipient", recipient);
            model.put("sender", sender);
            return new ModelAndView(model, "greeting_card.hbs");
        }, new HandlebarsTemplateEngine());

        post("/verif", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String username = StringEscapeUtils.escapeHtml4(request.queryParams("username"));
            String pwd = StringEscapeUtils.escapeHtml4(request.queryParams("pwd"));
            User greg = new User(username, pwd);
            if(greg.getUsername() != null){
                *//*greg.create_session();*//*
                model.put("recipient", greg.getUsername());
                model.put("sender", greg.getUsername());
            }else{
                response.status(404);

            }

*//*            model.put("username", username);
            model.put("pwd", pwd);*//*
            return new ModelAndView(model, "greeting_card.hbs");
        }, new HandlebarsTemplateEngine());

        get("/rectangle", (req, res) -> {
            //just for testing - make two new objects so we have something to retrieve
            Rectangle rectangle = new Rectangle(3,2);
            Rectangle otherRectangle = new Rectangle(12, 12);

            Map<String, ArrayList<Rectangle>> model = new HashMap<>();
            ArrayList myRectangleArrayList = Rectangle.getAll();
            model.put("myRectangles", myRectangleArrayList);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());*/
    /*}*/
}