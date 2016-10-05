package crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class BarUrl {
    Document doc;

    String URL;

    public BarUrl(){
    }
    
    
    public BarUrl(String URL) {
        
        this.URL = URL;
        
        HttpVisitor httpVisitor = new HttpVisitor(this.URL);
        String result = httpVisitor.connect();
        doc = Jsoup.parse(result);
    }

    
    public  String toNext() {
        
        String nextUrl = null;
        System.out.println("BarUrl.java Start to Next Page");

        Elements PageNumbers = doc.select("div#frs_list_pager");

        System.out.println("PageNumbers is :" + PageNumbers.size());
        
        if(PageNumbers.size() > 0) {
            
            Elements Links = PageNumbers.get(0).getElementsByTag("a");
            
            System.out.println("Links size is " + Links.size());
            
            for(Element Link : Links) {
                
                if(Link.text().contains("下一页")) {


                    String link = Link.attr("href");

                    String url = null;

                    if(link.contains("tieba.baidu.com") ) {
                        url = link;
                    }
                    else {
                        url = "http://tieba.baidu.com" + link;
                    }

                    System.out.println("next url is " + url);                    
                    nextUrl = url;

                }            
            }
            
        }
        

        return nextUrl;
    }

    public void start() {

        Elements titlePost = doc.select("div.threadlist_title");
        Elements elementtitle = doc.select("a.card_title_fname");
        String name = elementtitle.text();
        Database database = new Database();
        
        for(Element title : titlePost) {
            
            Elements link = title.select("a.j_th_tit");

            String Url = "http://tieba.baidu.com" + link.attr("href");
            Elements elementsReplyDate = title.select("span.threadlist_reply_date");

            SimpleDateFormat s = new SimpleDateFormat();
            Date replyDate = null;
            try {
                replyDate = s.parse(elementsReplyDate.text());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(replyDate == null)
                database.addData(new barurl(Url, name, "null"));
            else database.addData(new barurl(Url, name, replyDate.toString())); 
        }
        database.close();

      
    }
    
    
    class barurl implements Datable{
        String url;
        String lastReplyDate;
        String name;
        barurl(String url, String name, String replydate) {
            this.url = url;
            lastReplyDate = replydate;
            this.name = name;
        }
        @Override
        public String getTableName() {
            return "bar";
        }
        @Override
        public String createTable() {
            String sql = "CREATE TABLE  bar(URL TEXT NOT NULL"
                    + ",name text not null"
                    + ",lastupdate text not null)";
            return sql;
        }
        
        @Override
        public String insertData() {
            
            String sql = "INSERT INTO bar(URL, NAME, LASTUPDATE)VALUES("
                    + "\'" + url + "\',"
                    + "\'" + name + "\',"
                    + "\'" + lastReplyDate + "\')";
            
            return sql;
        }
    }   
    
    public static void main(String...strings) {
        
    }
}


