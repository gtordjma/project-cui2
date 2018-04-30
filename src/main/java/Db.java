import java.lang.reflect.Array;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Db {

    private static final String URL = "jdbc:postgresql://localhost:5432/greg";
    private static final String USER = "greg";
    private static final String PWD = "qwerty123";
    private static final String BDD = "test1";
    public static Connection connection;


    Db(){
        try {
            Class.forName("org.postgresql.Driver");
            //Connection a la BDD
            connection = DriverManager.getConnection(URL, USER, PWD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sql_request(String sql){
        try {
            //Création d'un objet Statement
            Statement state = connection.createStatement();
            //L'objet ResultSet contient le résultat de la requête SQL
            ResultSet result = state.executeQuery(sql);
            state.close();
            //On renvoie les MetaData

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User sql_where(String condition){

        try {
            //Création d'un objet Statement
            Statement state = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            //L'objet ResultSet contient le résultat de la requête SQL
            ResultSet result = state.executeQuery("SELECT * FROM "+BDD+" WHERE " +condition);
            if (result.next()){
                //On renvoie les MetaData
                User usr = new User();
                usr.setFirst_name(result.getString("firstname"));
                usr.setLast_name(result.getString("lastname"));
                usr.setEmail(result.getString("email"));
                usr.setPwd(result.getString("password"));
                usr.setId(result.getInt("id"));
                java.sql.Array arr_follower = result.getArray("follower");
                if (arr_follower != null) {
                    Object[] follower = (Object[]) arr_follower.getArray();
                    for (int i = 0; i < follower.length; i++) {
                        usr.setFollower((Integer) follower[i]);
                    }
                }
                java.sql.Array arr_follows = result.getArray("follows");
                if (arr_follows != null) {
                    Object[] follows = (Object[]) arr_follows.getArray();
                    for (int i = 0; i < follows.length; i++) {
                        System.out.print((Integer) follows[i]);
                        usr.setFollows((Integer) follows[i]);
                    }
                }
                state.close();
                return usr;
            }
            state.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> Search(String s){
        String query = "select id, firstname, lastname, email  from " + BDD + " where "
                + "firstname like '%"+s+"%' or "
                + "lastname like '%"+s+"%' or "
                + "email like '%"+s+"%'";
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
            try {
                //Création d'un objet Statement
                Statement state = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                //L'objet ResultSet contient le résultat de la requête SQL
                ResultSet result = state.executeQuery(query);
                while (result.next()){
                    map = new HashMap<>();
                    //On renvoie les MetaData
                    map.put("id", result.getString("id"));
                    map.put("firstname", result.getString("firstname"));
                    map.put("lastname", result.getString("lastname"));
                    map.put("email", result.getString("email"));
                    list.add(map);
                }
                state.close();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    public boolean follow(User user, int follow_id){
        user.setFollows(follow_id);
        try {
            //Création d'un objet Statement
            Statement state = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            //L'objet ResultSet contient le résultat de la requête SQL
            state.executeUpdate("update "+BDD+" SET follows = array_append(follows, "+follow_id+") where id = "+user.getId());
            state.executeUpdate("update "+BDD+" SET follower = array_append(follower, "+user.getId()+") where id = "+follow_id);
            state.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void modify(String email, String pwd, int id){
        try {
            //Création d'un objet Statement
            Statement state = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            //L'objet ResultSet contient le résultat de la requête SQL
            state.executeUpdate("update "+BDD+" SET email = '"+email+"' where id = "+id);
            if (pwd != null && pwd != "")
                state.executeUpdate("update "+BDD+" SET password = crypt('"+pwd+"', gen_salt('bf',8)) where id = "+id);
            state.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean unfollow(User user, int follow_id){
        user.unsetFollows(follow_id);
        try {
            //Création d'un objet Statement
            Statement state = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            //L'objet  ResultSet contient le résultat de la requête SQL
            state.executeUpdate("update "+BDD+" SET follows = array_remove(follows, "+follow_id+") where id = "+user.getId());
            state.executeUpdate("update "+BDD+" SET follower = array_remove(follower, "+user.getId()+") where id = "+follow_id);

            state.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verify_pwd(String pwd_tmp,String pwd_db){

        try {
            //Création d'un objet Statement
            Statement state = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            //L'objet ResultSet contient le résultat de la requête SQL
            ResultSet result = state.executeQuery("SELECT crypt('"+pwd_tmp+"', '"+pwd_db+"') = '"+pwd_db+"' as simple_auth_test");
            result.next();
            //On renvoie les MetaData
            Boolean b =result.getBoolean("simple_auth_test");
            state.close();
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getBDD_name(){return BDD;}
}

/*    public void aff(ResultSetMetaData res, ResultSet result){
        System.out.println("\n**********************************");
        //On affiche le nom des colonnes
        for(int i = 1; i <= res.getColumnCount(); i++)
            System.out.print("\t" + res.getColumnName(i).toUpperCase() + "\t *");

        System.out.println("\n**********************************");

        while(result.next()){
            for(int i = 1; i <= res.getColumnCount(); i++)
                System.out.print("\t" + result.getObject(i).toString() + "\t |");

            System.out.println("\n---------------------------------");

        }
        result.close();

    }*/