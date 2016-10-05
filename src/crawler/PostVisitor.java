package crawler;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;




public class PostVisitor {
    
    private String url;
    private Document doc;
    private HttpVisitor visitor;
    
    //algorithm
    private IParser parser;
    //private IImageDown;
    
    
    
    PostVisitor(String url, IParser parser) {
        this.url = url;
        this.parser = parser;
    }
    
      
    public boolean hasNext() {
        return parser.hasNext(doc);
    }

    public String Next() {

        return parser.getNextUrl(doc);
    }

    
    public void start() {
        
        Database database = Database.getInstance();
        
        visitor = new HttpVisitor(this.url);
        
        String urlContent = visitor.connect();
        
        
        if(urlContent != null) {         
            doc = Jsoup.parse(urlContent); 
            List<Post> list = parser.parse(doc, this.url);         
            for(Post post : list) {
                database.addData(post);
            }
     
        }
        
        
        if(hasNext()) {
            new PostVisitor(Next(), new Parser()).start();
        } 
        else {
            System.out.println("finish");
        }
    }

    public static void main(String...strings) {
        PostVisitor post = new PostVisitor("http://tieba.baidu.com/p/4241510695", new Parser());
        post.start();
    }
    
    
}
