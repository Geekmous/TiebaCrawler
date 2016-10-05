package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpVisitor {
    String url;
    
    public HttpVisitor() {
        
    }
    
    public HttpVisitor(String url) {
        this.url = url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    @SuppressWarnings("finally")
    public String connect() {
        String Content = null;

        try {
            URL url = new URL(this.url);
            
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            //httpConnection.setConnectTimeout(5000);
            //httpConnection.connect();
            
            InputStreamReader input = new InputStreamReader(httpConnection.getInputStream());
            BufferedReader bufferReader = new BufferedReader(input);

            StringBuilder stringBuilter = new StringBuilder();
            String temp = null;

            while((temp = bufferReader.readLine()) != null) {
                stringBuilter.append(temp);
                stringBuilter.append('\n');
            }
            Content = stringBuilter.toString();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();     
        }

        return Content;

    }

}
