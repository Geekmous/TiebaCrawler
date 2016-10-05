package crawler;


import java.io.BufferedWriter;

import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStreamWriter;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class Parser {
    

	String UrlContent=null;	
	
	
	public Parser(String UrlContent) {
	    
		this.UrlContent = UrlContent;
	}
	
	public void setUrl(String UrlContent){
		this.UrlContent = UrlContent;
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
	
	private String getTail(Element elementPost) {
	    String tail = "";
	    Elements tails = elementPost.select("div.core_reply");
	    System.out.println("Tail.size() is  " + tails.size());
	    for(Element node : tails) {
	        Elements nodes = node.getAllElements();
	        System.out.println("nodes.size is " + nodes.size() + " --" +nodes.get(0).select("ul").size());	        
	    }
	    Elements p_tail = tails.get(0).getElementsByTag("ul");
	    //System.out.println("p_tail.size() is  " + p_tail.size());
	    return tail;
	}
	
	
	public void Processed() throws IOException{
	 	
		Document doc=Jsoup.parse(this.UrlContent);	
		
		Elements elementsPost = doc.select("div.j_l_post");
		
		//System.out.println("the number of Post is :" + elementsPost.size());
		System.out.println("Parser.java start Parser Content");
		
		for(Element elementPost : elementsPost) {
		    
		   // System.out.println("data-field::::" + elementPost.attr("data-field"));
		   // System.out.println("the author of this post is :" + this.getAuthor(elementPost));
		  // System.out.println("the content of this post is :" + this.getContent(elementPost));
		  //  System.out.println("the tail of this post is :" + this.getTail(elementPost));
		    
		    FileOutputStream outputAuthor = new FileOutputStream("./Author.txt",true);
		    FileOutputStream outputContent = new FileOutputStream("./Content.txt",true);
		    OutputStreamWriter authorWriter = new OutputStreamWriter(outputAuthor);
		    OutputStreamWriter contentWriter = new OutputStreamWriter(outputContent);
		    BufferedWriter authorBuf = new BufferedWriter(authorWriter);
		    BufferedWriter contentBuf = new BufferedWriter(contentWriter);
		    authorBuf.write(this.getAuthor(elementPost) + "\n");
		    contentBuf.write(this.getContent(elementPost) + "\n");

		    authorBuf.close();
		    contentBuf.close();
		}
		
		System.out.println("Parser.java Start to NextFloor");
		Elements pageLinks = doc.select("li.pb_list_pager");
		if(pageLinks.size() > 1 )
		{
		    Element Link = pageLinks.get(1);
		    Elements as = Link.getElementsByTag("a");
		        for(Element a : as) {      
		            if(a.text().contains("下一页")) {	        
		                String Url = "http://tieba.baidu.com" + a.attr("href");     
		                HttpVisitor httpVisitor = new HttpVisitor(Url);
		                System.out.println("a.text() is :" + a.text()  + "and the URL is " + a.attr("href"));
		                             
                        Parser parser = new Parser(httpVisitor.connect());
                        parser.Processed();
		            }
		        }
		    
		}
		//this.mysql.Connect();			
	}
	
/*	void ADDtoDB(Mysql mysql){
	    
		for(Post Temp:list){
		Temp.Author=FilterQuet(Temp.Author);
		Temp.Content=FilterQuet(Temp.Content);
		Temp.Bar=FilterQuet(Temp.Bar);
		Temp.Title=FilterQuet(Temp.Title);
		String Query="INSERT IGNORE INTO Post(UserName,Content,Time,Floor,Bar,Title,Url,MD5)values('"+Temp.Author+"','"+Temp.Content+"','"+Temp.Time+"','"+Temp.Floor+"','"+Temp.Bar+"','"+Temp.Title+"','"+Temp.Path+"','"+Temp.MD5+"')";
		//this.mysql.Insert(Query);		
		}
		
	}
	*/
	String FilterQuet(String str){
		str.replaceAll("[\"\']","&queto");
		return str;
	}
	
     public  static void main(String[] args){  
         HttpVisitor httpVisitor = new HttpVisitor("http://tieba.baidu.com/p/3276058372");
         Parser mParser=new Parser(httpVisitor.connect()); 			
         try {
             mParser.Processed();
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }     
     }
}

