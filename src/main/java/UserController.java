import java.util.HashMap;
import java.util.Map;
import java.util.List;
import spark.ModelAndView;
import spark.Redirect;
import spark.template.handlebars.HandlebarsTemplateEngine;
import org.apache.commons.lang3.*;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.Request;
import java.lang.Object;
import java.util.Map;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;

import spark.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import static j2html.TagCreator.*;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;

import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import static spark.Spark.*;

public class UserController {

	private static final String USER_SESSION_ID = "user";

	public UserController(final UserService userService) {
		staticFileLocation("/public");
		port(4567);
/*
		get("/users", (req, res) -> userService.getAllUsers(), JsonUtil.json());

		get("/users/:id", (req, res) -> {
			String id = req.params(":id");
			User user = userService.getUser(id);
			if (user != null) {
				return user;
			}
			res.status(400);
			return new ResponseError("No user with id '%s' found", id);
		}, JsonUtil.json());

		put("/users/:id", (req, res) -> userService.updateUser(
				req.params(":id"),
				req.queryParams("name"),
				req.queryParams("email")
		), JsonUtil.json());
*/

		get("/", (request, response) -> { //request for route happens at this location
            Map<String, Object> model = model_user_info(request); // new model is made to store information
			return new ModelAndView(model, "index.hbs"); // assemble individual pieces and render
		}, new HandlebarsTemplateEngine()); //

		get("/tweet", (request, response) -> { //request for route happens at this location
            Map<String, Object> model = model_user_info(request);
			SparkConf conf = new SparkConf().setAppName("test").setMaster("local");
			conf.set("spark.driver.allowMultipleContexts", "true");
			conf.set("es.index.auto.create", "true");
			conf.set("es.nodes","192.168.0.20");
			conf.set("es.port","9200");
			JavaSparkContext jsc = new JavaSparkContext(conf);

			JavaRDD<Map<String, Object>> esRDD =
					JavaEsSpark.esRDD(jsc, "gtordjma/tutoriels", "?q=PHP*").values();
			List<Map<String, Object>> lst = esRDD.collect();
			for (int i = 0;i < lst.size();i++){
				Map<String, Object> map = lst.get(i);
				for(String key:map.keySet()){

					System.out.println("treeMap: [key: "+key+" , value: "+map.get(key)+"]");
				}

			}
/*			Map<String, Object> json = new HashMap<String, Object>();
			json.put("user","kimchy");
			json.put("postDate",new Date());
			json.put("message","trying out Elasticsearch");*/
			return new ModelAndView(model, "index.hbs"); // assemble individual pieces and render
		}, new HandlebarsTemplateEngine()); //

		get("/signup", (request, response) -> {
			if (getAuthenticatedUser(request) != null)
				response.redirect("/profile");
			Map<String, Object> model = new HashMap<String, Object>();
			return new ModelAndView(model, "signup.hbs");
		}, new HandlebarsTemplateEngine()); //
		//inscription
		//curl -d "firstname=Marvin&lastname=Tordjman&email=m@g.co&pwd=toto" -X POST 0.0.0.0:4567/users
		post("/signup", (req, res) -> {
			userService.createUser(
					req.queryParams("firstname"),
					req.queryParams("lastname"),
					req.queryParams("email"),
					req.queryParams("pwd"));
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("res", "Your account has been successfully registered !");
			return new ModelAndView(model, "signup.hbs");
		}, new HandlebarsTemplateEngine()); //
		get("/login", (request, response) -> { //request for route happens at this location
			if (getAuthenticatedUser(request) != null)
				response.redirect("/profile");
			Map<String, Object> model = new HashMap<String, Object>(); // new model is made to store information
			return new ModelAndView(model, "login.hbs"); // assemble individual pieces and render
		}, new HandlebarsTemplateEngine()); //

		post("/login", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
			Result result = userService.login(req.queryParams("email"),req.queryParams("pwd"));
			if(result.getUser() != null) {
			    addAuthenticatedUser(req, result.getUser());
			    model.put("username", result.getUser().getFirst_name());
			    res.redirect("/profile");
			} else {
				model.put("error", result.getError());
			}
			return new ModelAndView(model, "login.hbs");
		}, new HandlebarsTemplateEngine());
		/*after((req, res) -> {
			res.type("application/json");
		});*/

		get("/profile", (request, response) -> { //request for route happens at this location
			if (getAuthenticatedUser(request) == null)
				response.redirect("/");
			Map<String, Object> model = model_user_info(request);
			return new ModelAndView(model, "profile.hbs"); // assemble individual pieces and render
		}, new HandlebarsTemplateEngine()); //

		get("/logout", (req, res) -> {
			removeAuthenticatedUser(req);
			res.redirect("/");
			return null;
		});


		exception(IllegalArgumentException.class, (e, req, res) -> {
			res.status(400);
			res.body(JsonUtil.toJson(new ResponseError(e)));
		});
	}

	private Map<String, Object> model_user_info(Request request){
		Map<String, Object> model = new HashMap<String, Object>();
		User usr = getAuthenticatedUser(request);
		if (usr != null) {
			model.put("id", usr.getId());
			model.put("firstname", usr.getFirst_name());
			model.put("lastname", usr.getLast_name());
			model.put("email", usr.getEmail());
			model.put("follower", usr.getFollower());
			model.put("follows", usr.getFollows());
		}
		return model;
	}

	private void addAuthenticatedUser(Request request, User u) {
		request.session().attribute(USER_SESSION_ID, u);

	}

	private void removeAuthenticatedUser(Request request) {
		request.session().removeAttribute(USER_SESSION_ID);

	}

	private User getAuthenticatedUser(Request request) {
		return request.session().attribute(USER_SESSION_ID);
	}
}
