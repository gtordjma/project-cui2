import java.util.ArrayList;

public class User {
    private String username;

    public User(String usr, String pwd) {
        if (pwd.equals("greg")){
            username = usr;
        }else{
            username = null;
        }
    }

    public String getUsername(){return username;}

}