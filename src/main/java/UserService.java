import com.sun.applet2.Applet2;

import java.awt.*;
import java.applet.*;
import java.util.List;
import java.util.Map;

public class UserService extends Applet{

	private static Db database = new Db();

	public Result signup(String firstname,String lastname, String email, String pwd) {
		Result result = new Result();
		User user = new User();
		user.setEmail(email);
		if (checkEmail(user) == null) {
			User user_register = new User(firstname, lastname, email, pwd);
			result.setUser(user_register);
		}else{
			result.setError("This email  already exists in our system. please choose a different one");
		}
		return result;
	}

	public void follow(User user, int id) {
		database.follow(user, id);
	}

	public void unfollow(User user, int id) {
		database.unfollow(user, id);
	}

	public List<Map<String, Object>> Search(String s){
	    return database.Search(s);
    }

    public void modify(String email, String pwd, int id){
		database.modify(email, pwd, id);
	}


	public Result login(String email, String pwd) {
		Result result = new Result();
		User userlive = new User();
		userlive.setEmail(email);
		userlive.setPwd(pwd);
		User userbdd = checkEmail(userlive);
		if (userbdd == null) {
			result.setError("Invalid email");
		} else if (checkPwd(userbdd, userlive) == null) {
			result.setError("Invalid password");
		} else {
			result.setUser(userbdd);
		}
		return result;
	}

	public User checkEmail(User user){
		try {
			User userFound = database.sql_where("email = '" + user.getEmail() + "'");
			if (userFound == null) {
				return null;
			} else {
				return userFound;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public User checkPwd(User userbdd, User userlive){
		try {
			if (!database.verify_pwd(userlive.getPwd(), userbdd.getPwd())) {
				return null;
			} else {
				return userbdd;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}




/*	public User updateUser(String id, String name, String email) {
		User user = users.get(id);
		if (user == null) {
			throw new IllegalArgumentException("No user with id '" + id + "' found");
		}
		failIfInvalid(name, email);
		user.setName(name);
		user.setEmail(email);
		return user;
	}*/

/*	private void failIfInvalid(String name, String email) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Parameter 'name' cannot be empty");
		}
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Parameter 'email' cannot be empty");
		}
	}*/
}
