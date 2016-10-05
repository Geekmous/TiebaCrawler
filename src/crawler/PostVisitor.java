package crawler;




import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class PostVisitor {
    
    private String url;
    private Document doc;
    private HttpVisitor visitor;
    
    PostVisitor(String url) {
        this.url = url;
    }
    
    private String getAuthor(Element elementPost) {

        String authorName = null;
        Elements author = elementPost.select("a.p_author_name");
        if(author.size() == 1) {
            authorName = author.get(0).text();
        }
        return authorName;      
    }
    
    private String getContent(Element elementPost) {
        String content = null;
        Elements contents = elementPost.select("div.j_d_post_content");
        if(contents.size() == 1) {
            content = contents.text();
        }
        return content;
    }
    
    private String getTime(Element elementPost) {
        
        String Time = null;
        
        Elements tails = elementPost.select("div.core_reply");
    //    System.out.println("tails.size() is  " + tails.size());
        for(Element node : tails) { 
            
            Elements nodes = node.select("span.tail-info");
            //Elements nodes2 = doc.select("ul");
            
            //System.out.println("nodes.size : " + nodes.size());
            //System.out.println("nodes2.size : " + nodes2.size());
            if(nodes.size() > 0) {
                
            
                for(Element time : nodes) {
               
                    Pattern pattern = Pattern.compile("\\d+-\\d+-\\d+.+");
                    Matcher matcher = pattern.matcher(time.text());
               
                    if(matcher.matches()) {
                        Time = time.text();
                    }
                }
            }
            
            
   /*        //if(nodes2.size() > 0) {
            //    for(Element time : nodes2) {
                    
                    //Pattern pattern = Pattern.compile("\\d+-\\d+-\\d+.+");
                    //Matcher matcher = pattern.matcher(time.text());
               
                    //if(matcher.matches()) {
                    //    Time = time.text();
                   // }
                    
                    //System.out.println("time : " + time.text());
                }
            }
    */
            //System.out.println("nodes.size is " + nodes.size() + " --" +nodes.get(0).select("ul").size());          
        
        }
        
        //Elements p_tail = tails.get(0).getElementsByTag("ul");
        //System.out.println("p_tail.size() is  " + p_tail.size());
       
        return Time;
    }
    
    private String getFromBar() {
        String bar = null;
        Elements links = doc.select("a.card_title_fname");
        for(Element frombar : links) {
            bar = frombar.text();
        }
        
        return bar;
    }
    
    private String getTitle() {
        String title = null;
        Elements ele = doc.getElementsByClass("core_title_txt");
        for(Element e : ele) {
            title = e.text();
        }
        
        return title;
    }
    public String toNext() {
        String nextUrl = null;
        Elements pageLinks = doc.select("li.pb_list_pager");

        if(pageLinks.size() > 1 )
        {
            Element Link = pageLinks.get(1);
            Elements as = Link.getElementsByTag("a");
            for(Element a : as) {
                if(a.text().contains("下一页")) {

                    nextUrl = "http://tieba.baidu.com" + a.attr("href");

                    System.out.println("a.text() is :" + a.text()  + "and the URL is " + a.attr("href"));


                }
            }
        }

        return nextUrl;
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
    public void start() {
        Database database = new Database();
        
        visitor = new HttpVisitor(this.url);
        
        String urlContent = visitor.connect();
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./error.html")));
            output.write(urlContent);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(urlContent != null) {
            doc = Jsoup.parse(urlContent); 
            Elements elementsPost = doc.select("div.j_l_post");

            System.out.println("Parser.java start Parser Content");    
 
            for(Element elementPost : elementsPost) {      
                    Post post = new Post();
                    
                    post.setAuthor(this.getAuthor(elementPost));
                    
                    post.setContent(this.getContent(elementPost));
                    
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
                    
                    MessageDigest md5 = null;
                    try {
                        md5 = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    md5.update(sb.toString().getBytes());
                   
                    byte[] md = md5.digest();
                    char hexdigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'A', 'B', 'C', 'D', 'E', 'F'};
                    char[] str = new char[md.length * 2];
                    
                    int k = 0;
                    for(int i = 0; i < md.length; i++) {
                        byte b = md[i];
                        str[k++] = hexdigits[b >>> 4 & 0xf];
                        str[k++] = hexdigits[b & 0xf];
                    }
                    
                    String postID = new String(str);
                    
                    post.setPostID(postID);
                    
                    getImage(postID, elementPost);
                    
                    database.addData(post);

            }
        }
        else System.out.println("connect to " + this.url + "fail ");  
        database.close();
    }

    public static void main(String...strings) {
        PostVisitor post = new PostVisitor("http://tieba.baidu.com/p/4779451205");
        post.start();
    }
    
    
}
