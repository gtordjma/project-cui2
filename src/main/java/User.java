
import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;
import static org.apache.commons.lang3.StringEscapeUtils.*;
public class User {

	private int id;
	private String first_name;
	private String last_name;
	private String email;
	private String pwd;
	private List <Integer>follows;
	private List <Integer>follower;
	private Db database;

	public User(String firstname,String lastname, String email, String pwd) {
		this.first_name = StringEscapeUtils.escapeHtml(firstname);
		this.last_name = StringEscapeUtils.escapeHtml(lastname);
		this.email = StringEscapeUtils.escapeHtml(email);
		this.pwd = StringEscapeUtils.escapeHtml(pwd);
		this.follows =new ArrayList <Integer>();
		this.follower =new ArrayList <Integer>();
		this.database = new Db();
		database.sql_request("INSERT INTO "+database.getBDD_name()+"(firstname, lastname, email, password) VALUES('"+this.first_name+"','"+this.last_name+"','"+this.email+"',crypt('"+this.pwd+"', gen_salt('bf',8)))");
	}

	public User(){
		this.follows =new ArrayList <Integer>();
		this.follower =new ArrayList <Integer>();
	}

	public void setId(int i) { this.id = i; }
	public int getId() { return id; }

	public String getFirst_name() {return first_name;}
	public void setFirst_name(String firstname) {this.first_name = firstname;}

	public String getLast_name() { return last_name; }
	public void setLast_name(String lastname) { this.last_name = lastname; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPwd(){return this.pwd;}
	public void setPwd(String pass){this.pwd = pass;}

	public List<Integer> getFollows(){return this.follows;}
	public void setFollows(int id){ this.follows.add(id);}
	public void setFollows(int []id){
		for (int i = 0; i < id.length; i++)
			this.follows.add(id[i]);
	}

	public List<Integer>getFollower(){return this.follower;}
	public void setFollower(int id){ this.follower.add(id);}
	public void setFollower(int []id){
		for (int i = 0; i < id.length; i++)
			this.follows.add(id[i]);
	}

}
