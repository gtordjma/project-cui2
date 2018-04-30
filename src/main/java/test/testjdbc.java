import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;

public class testjdbc {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://localhost:5432/greg";
            String user = "greg";
            String passwd = "qwerty123";

            Connection conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");


            //Création d'un objet Statement
            Statement state = conn.createStatement();
            //L'objet ResultSet contient le résultat de la requête SQL
            ResultSet result = state.executeQuery("SELECT * FROM twitter");
            //On récupère les MetaData
            ResultSetMetaData resultMeta = result.getMetaData();

            System.out.println("\n**********************************");
            //On affiche le nom des colonnes
            for(int i = 1; i <= resultMeta.getColumnCount(); i++)
                System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");

            System.out.println("\n**********************************");

            while(result.next()){
                for(int i = 1; i <= resultMeta.getColumnCount(); i++)
                    System.out.print("\t" + result.getObject(i).toString() + "\t |");

                System.out.println("\n---------------------------------");

            }

            result.close();
            state.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
