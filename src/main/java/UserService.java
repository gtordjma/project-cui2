/*
package com.mscharhag.sparkdemo;
*/

import java.util.*;

public class UserService {

	private static Db database = new Db();

	private Map<Integer, User> users = new HashMap<>();

	public List<User> getAllUsers() {
		return new ArrayList<>(users.values());
	}

	public User getUser(String id) {
		return users.get(id);
	}

	public User createUser(String firstname,String lastname, String email, String pwd) {
		/*failIfInvalid(name, email);*/
		User user = new User(firstname,lastname,email,pwd);
		users.put(user.getId(), user);
		return user;
	}


	public Result login(String email, String pwd) {
		/*failIfInvalid(name, email);*/
		User user = new User();
		user.setEmail(email);
		user.setPwd(pwd);
/*		users.put(user.getId(), user);*/
		return checkUser(user);
	}

	public Result checkUser(User user) {
		try {
			Result result = new Result();
			User userFound = database.sql_where("email = '" + user.getEmail() + "'");
			if (userFound == null) {
				result.setError("Invalid email");
			} else if (!database.verify_pwd(user.getPwd(), userFound.getPwd())) {
				result.setError("Invalid password");
			} else {
				result.setUser(userFound);
			}
			return result;
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
