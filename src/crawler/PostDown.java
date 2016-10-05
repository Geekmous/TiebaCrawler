package crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDown {
    ResultSet cursor;
    List<String> list = new ArrayList<String>();
    String barTitle;
    PostDown() {
        
    }
    
    PostDown(String barTitle) {
        this.barTitle = barTitle;
    }
    
    private void getCursor() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:./tieba.db");
            Statement stmt = con.createStatement();
            
            String sql = "SELECT * FROM BAR ORDER BY lastupdate DESC";
            cursor = stmt.executeQuery(sql);
            while(cursor.next()) {
                list.add(cursor.getString(1));
            }
            stmt.close();
            con.close();
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public void getCursor(String barTitle) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:./tieba.db");
            Statement stmt = con.createStatement();
            
            String sql = "SELECT * FROM BAR where name = " + "\'" + barTitle + "\' ORDER BY lastupdate DESC";
            cursor = stmt.executeQuery(sql);
            while(cursor.next()) {
                list.add(cursor.getString(1));
            }
            stmt.close();
            con.close();
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void start() {
        if(barTitle == null)
            getCursor();
        else getCursor(this.barTitle);
        
        for(String cursor : list) {
            String url = cursor;
            PostVisitor visitor = new PostVisitor(url);
            visitor.start();          
            Connection con;
            try {
                con = DriverManager.getConnection("jdbc:sqlite:./tieba.db");
                Statement stmt = con.createStatement();
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("YY-MM-DD HH:MM:SS");
                String d = format.format(date);
                
                stmt.execute("UPDATE BAR SET lastupdate = \'" + d + "\' WHERE url = \'" + url +"\'");
                
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
    }
    
    
    public static void main(String ...strings) {
        new PostDown().start();
    }
    
    
}
