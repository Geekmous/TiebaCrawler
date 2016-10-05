package crawler;

public class VisitorThread extends Thread{
    String url;
    
    VisitorThread(String url) {
        this.url = url;
    }
    @Override
    public void run() {
        PostVisitor visitor = new PostVisitor(url, new Parser());
        visitor.start();
    }
    
    
    public static void main(String ...strings) {
        String [] urls = {"http://tieba.baidu.com/p/4569647995",};
        for(String i : urls)
            new VisitorThread(i).start();
    }
}
