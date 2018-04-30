package spark.template.handlebars;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;

import static spark.Spark.get;

public class HandlebarsHelper extends HandlebarsTemplateEngine {

    public HandlebarsHelper() {
        super();
        handlebars.registerHelpers(this);
    }

    public Boolean ifCond(Object a, Object b) {
        System.out.println("Test");
        /*System.out.println(a);
        System.out.println(b);*/
        return true;
    }
    public String tralli(int a,String b,String c) {
        return "Greg" + Integer.toString(a) + b + c;
    }

    // Example of a helper, write {{now}} in template file.
    public String now() {
        return new Date().toString();
    }
}