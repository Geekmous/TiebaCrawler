package crawler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class ImgeDownloader {
    String userName;
    String Url;
    String Path;
    String fromUrl;
    String postId;
    ImgeDownloader(String postID, String ImageUrl) {
        postId = postID;
        download(ImageUrl);
    }
    
    public void download(String url){
        File IMG =new File("./img");
        if(!IMG.exists())
            IMG.mkdir();
        String path = "./img/" + url.replaceAll("/", "");
        URL u;
        Database database = Database.getInstance();
        try {
            u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            File file = new File(path);
            Path = file.getAbsolutePath();
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            
            DataInputStream input = new DataInputStream(new BufferedInputStream(con.getInputStream()));
            int i = -1;
            while((i = input.read()) >= 0) {
                output.write(i);
            }
            
            database.addData(new ImageData(path, postId));
            
            output.close();
            input.close();
            con.disconnect();
            
            
            
            
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    class ImageData implements Datable {
        String path;
        String postId;
        
        ImageData(String Path, String PostId) {
            path = Path;
            postId = PostId;
        }

        public String getTableName() {
            return "IMG";
        }
        
        @Override
        public String createTable() {
            String sql = "CREATE TABLE IMG(PATH text not null, POSTID text not null)";
            return sql;
        }
        
        
        @Override
        public String insertData() {
            String sql = "INSERT INTO IMG(PATH, POSTID)VALUES(\'" +
                    path + "\',\'" +
                    postId + "\')";  
            return sql;
        }
    }// class  ImageData
}
