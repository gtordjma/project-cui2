import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsHelper;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.Request;
import java.lang.Object;
import com.github.jknack.handlebars.helper.*;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;

import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import static spark.Spark.*;

public class UserController {

	private static final String USER_SESSION_ID = "user";

	public UserController(final UserService userService) {
		staticFileLocation("/public");
		port(4567);

		HandlebarsTemplateEngine handle = new HandlebarsTemplateEngine();

		get("/", (request, response) -> { //request for route happens at this location
			ES es = new ES();
			Map<String, List<Map<String, Object>>> model = es.get_alltweet();
			model.put("userinfo", model_user_info(request).get("userinfo"));
			return new ModelAndView(model, "index.hbs"); // assemble individual pieces and render
		}, new HandlebarsHelper());

		get("/usertimeline", (request, response) -> { //request for route happens at this location
			if (getAuthenticatedUser(request) == null)
				response.redirect("/");
			ES es = new ES();
			Map<String, List<Map<String, Object>>> model = es.get_usertimeline(getAuthenticatedUser(request));
			model.put("userinfo", model_user_info(request).get("userinfo"));
			return new ModelAndView(model, "index.hbs"); // assemble individual pieces and render
		}, new HandlebarsHelper());

		post("/", (req, res) -> {
            ES es = new ES();
            Map<String, List<Map<String, Object>>> model = model_user_info(req);
            if (getAuthenticatedUser(req) != null) {
				Boolean b = es.tweet(req.queryParams("tweet"), getAuthenticatedUser(req));
			}
			model.put("tweets", es.get_alltweet().get("tweets"));
			return new ModelAndView(model, "index.hbs");
		}, new HandlebarsTemplateEngine());

		get("/signup", (request, response) -> {
			if (getAuthenticatedUser(request) != null)
				response.redirect("/profile");
			Map<String, Object> model = new HashMap<String, Object>();
			return new ModelAndView(model, "signup.hbs");
		}, new HandlebarsTemplateEngine());

		//inscription
		//curl -d "firstname=Marvin&lastname=Tordjman&email=m@g.co&pwd=toto" -X POST 0.0.0.0:4567/users
		post("/signup", (req, res) -> {
			Result result = userService.signup(
					req.queryParams("firstname"),
					req.queryParams("lastname"),
					req.queryParams("email"),
					req.queryParams("pwd"));
			Map<String, Object> model = new HashMap<String, Object>();
			if(result.getUser() != null) {
				model.put("res", "Your account has been successfully registered !");
			} else {
				model.put("error", result.getError());
			}
			return new ModelAndView(model, "signup.hbs");
		}, new HandlebarsTemplateEngine()); //

		get("/login", (request, response) -> {
			if (getAuthenticatedUser(request) != null)
				response.redirect("/profile");
			Map<String, Object> model = new HashMap<String, Object>();
			return new ModelAndView(model, "login.hbs");
		}, new HandlebarsTemplateEngine());

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

		get("/profile", (request, response) -> {
			if (getAuthenticatedUser(request) == null)
				response.redirect("/");
			ES es = new ES();
			Map<String, List<Map<String, Object>>> model = model_user_info(request);
			model.put("tweets", es.get_idtweet(Integer.toString(getAuthenticatedUser(request).getId())).get("tweets"));
			return new ModelAndView(model, "params.hbs");
		}, new HandlebarsHelper());

		get("/delete/:id", (request, response) -> {
			if (getAuthenticatedUser(request) == null)
				response.redirect("/");
			ES es = new ES();
			es.DELETE(request.params(":id"));
			Map<String, Object> model = new HashMap<String, Object>();
			response.redirect("/");
			return null;
		});

		get("/profile/:id", (request, response) -> {/*
			if (getAuthenticatedUser(request) == null)
				response.redirect("/");*/
			ES es = new ES();
            Map<String, Object> info = new HashMap<String, Object>();
            List<Map<String, Object>> list = new ArrayList<>();
			Map<String, List<Map<String, Object>>> model = es.get_idtweet(request.params(":id"));
			model.put("userinfo", model_user_info(request).get("userinfo"));
			if (getAuthenticatedUser(request) != null &&
					getAuthenticatedUser(request).getFollows().contains(Integer.parseInt(request.params(":id")))){
			    info.put("unfollow", true);
            }else if (getAuthenticatedUser(request) != null){
                info.put("follow", true);
            }
            list.add(info);
			model.put("test", list);
			return new ModelAndView(model, "profile.hbs");
		}, new HandlebarsHelper());

        get("/follow/:id", (req, res) -> {
            userService.follow(getAuthenticatedUser(req), Integer.parseInt(req.params(":id")));
            res.redirect("/profile/"+req.params(":id"));
            return null;
        });
        get("/unfollow/:id", (req, res) -> {
            userService.unfollow(getAuthenticatedUser(req), Integer.parseInt(req.params(":id")));
            res.redirect("/profile/"+req.params(":id"));
            return null;
        });

/*		get("/alltweet", (request, response) -> {
			ES es = new ES();
			Map<String, List<Map<String, Object>>> model = es.get_alltweet();
			return new ModelAndView(model, "alltweet.hbs");
		}, new HandlebarsTemplateEngine());*/

		get("/logout", (req, res) -> {
			removeAuthenticatedUser(req);
			res.redirect("/");
			return null;
		});

		exception(IllegalArgumentException.class, (e, req, res) -> {
			res.status(400);
			res.body(JsonUtil.toJson(new ResponseError(e)));
		});

		notFound((req, res) -> {
			res.type("application/json");
			return "{\"message\":\"Custom 404\"}";
		});
	}

	private Map<String, List<Map<String, Object>>> model_user_info(Request request){
		Map<String, List<Map<String, Object>>> model = new HashMap<String, List<Map<String, Object>>>();
		Map<String, Object> info = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		User usr = getAuthenticatedUser(request);
		if (usr != null) {
			info.put("id", usr.getId());
			info.put("firstname", usr.getFirst_name());
			info.put("lastname", usr.getLast_name());
			info.put("email", usr.getEmail());
			info.put("follower", usr.getFollower());
			info.put("follows", usr.getFollows());
		}
		list.add(info);
		model.put("userinfo", list);
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