package crawler;



import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

public class Parser implements IParser{
    
	Document doc;
	String url;
	IDigest md5;
	boolean hasNext;
	public Parser() {  	
	}
	
	@Override
	public List<Post> parse(Document doc, String url){
	    this.doc = doc;
	    this.url = url;
	    md5 = new DigestMD5();
	    List<Post> list = new ArrayList();
	    Elements elementsPost = doc.select("div.j_l_post");

        for(Element elementPost : elementsPost) {
            Post post = new Post();
            
            post.setAuthor(getAuthor(elementPost));
            
            post.setContent(getContent(elementPost));
            
            post.setUrl(this.url);  
            
            post.setTime(getTime(elementPost));
            
            post.setTitle(getTitle());
            
            post.setBar(getFromBar());
            
            StringBuilder sb = new StringBuilder();
            sb.append(post.getAuthor());
            sb.append(post.getContent());
            sb.append(post.getUrl());
            sb.append(post.getTime());
            sb.append(post.getTitle());
            sb.append(post.getBar());
            
            md5.update(sb.toString());
            
            post.setPostID(md5.digest());
            
            getImage(post.getPostID(), elementPost);
            
            list.add(post);
        }
        return list;    
	};
	
	public String getNextUrl(Document doc) {
	    this.doc = doc;
	    
	    String nextUrl = null;
        Elements pageLinks = doc.select("li.pb_list_pager");
        if(pageLinks.size() > 1 ) {
            Element Link = pageLinks.get(1);
            Elements as = Link.getElementsByTag("a");
            for(Element a : as) {
                
                if(a.text().contains("下一页")) {
                    nextUrl = "http://tieba.baidu.com" + a.attr("href");
                } // if
            }// for
        } // if
        
        return nextUrl;
	}
	
	public boolean hasNext(Document doc) {
	    this.doc = doc;

        Elements pageLinks = doc.select("li.pb_list_pager");
        if(pageLinks.size() > 1 ) {
            Element Link = pageLinks.get(1);
            Elements as = Link.getElementsByTag("a");
            for(Element a : as) {
                //System.out.println(a.text());
                if(a.text().contains("下一页")) {
                    //System.out.println("hax");
                    return true;
                } // if
                
            }// for
        } // if
	    
	    
	    return false;
	}
	
	
    private String getAuthor(Element elementPost) {

        String authorName = "";
        Elements author = elementPost.select("a.p_author_name");
        if(author.size() == 1) {
            authorName = author.get(0).text();
        }
        return authorName;      
    }
    
    private String getContent(Element elementPost) {
        String content = "";
        Elements contents = elementPost.select("div.j_d_post_content");
        if(contents.size() == 1) {
            content = contents.text().replaceAll("\"", "&quote").replaceAll("\'", "");
        }
        return content;
    }
    
    private String getTime(Element elementPost) {   
        String Time = "";
        Elements tails = elementPost.select("div.core_reply");
        for(Element node : tails) {          
            Elements nodes = node.select("span.tail-info");
            if(nodes.size() > 0) {
                for(Element time : nodes) {    
                    Pattern pattern = Pattern.compile("\\d+-\\d+-\\d+.+");
                    Matcher matcher = pattern.matcher(time.text());
                    if(matcher.matches()) {
                        Time = time.text().replaceAll("\"", "&quote");
                    }
                }
            }       
        }     
        return Time;
    }
    
    private String getFromBar() {
        String bar = "";
        Elements links = doc.select("a.card_title_fname");
        for(Element frombar : links) {
            bar = frombar.text();
        }  
        return bar;
    }
    
    private String getTitle() {
        String title = "";
        Elements ele = doc.getElementsByClass("core_title_txt");
        for(Element e : ele) {
            //System.out.println(e.text());
            title = e.text();
        } 
        
        return title;
    }
        

    private void getImage(String PostID, Element Post) {
        
        Elements cc = Post.getElementsByTag("cc");
        
        for(Element content : cc) {
            
            Elements imgs = content.getElementsByTag("img");
            
            for(Element img : imgs) {
                String imgurl = img.attr("src");
  
                new ImgeDownloader(PostID, imgurl);
        
            }
            
        }
        
    }
	
}

