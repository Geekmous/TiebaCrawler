package crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by GEEK on 9/12/16.
 */
public class Database {
    Post data;
    Connection con;
    Statement stmt;
    private static Database database = null;
    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:./tieba.db");
           stmt = con.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static Database getInstance() {
        if(database == null) {
            synchronized(Database.class) {
                if(database == null) {
                    database = new Database();
                }
            }
        }
        
        return database;
    }
    
    public void  addData(Datable data) {
        synchronized (Database.class){
        try {
            String TableName = data.getTableName();
            ResultSet cursor = stmt.executeQuery("select * from tablelist where tables ==\'" + TableName + "\'");
            if(cursor.next()){
                
            }
            else {
              stmt.execute("INSERT INTO TABLELIST(tables)VALUES(\'" + TableName + "\')"); 
              stmt.executeUpdate(data.createTable());
              
            }
         
            stmt.executeUpdate(data.insertData());
   
        } catch (SQLException e) {
            System.out.println(data.insertData());
            e.printStackTrace();
        }
        }

    }
    
    public void execute(String sql) {
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void close() {
        try {
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
