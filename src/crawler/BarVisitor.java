package crawler;

public class BarVisitor {
    BarUrl visitor;
    String url;
    BarVisitor(){}
    
    BarVisitor(String url) {
        this.url = url;
    }
    
    public void start() {
        visitor = new BarUrl(url);
        do {
            visitor.start();
            String url = null;           
            if((url = visitor.toNext()) != null) {      
                visitor = new BarUrl(url);
            }
            else break;
        }while(true);
    }
    
    
    public static void main(String[] args) {
        
        Thread thread1 = new Thread() {
            public void run() {
                String url = "http://tieba.baidu.com/f?ie=utf-8&kw=90%E5%90%8E%E7%BE%8E%E5%A5%B3";
                BarVisitor visitor = new BarVisitor(url);
                visitor.start();
            }
        };
        
        thread1.start();
        PostDown post = new PostDown();
        post.start();
    }
}
