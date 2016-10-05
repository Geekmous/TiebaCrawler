package crawler;

import java.util.List;

import org.jsoup.nodes.Document;

public interface IParser {
    
    public List<Post> parse(Document doc, String url);
    public String getNextUrl(Document doc);
    public boolean hasNext(Document doc);
    
}
