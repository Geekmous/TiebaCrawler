package crawler;

public class Post implements Datable{
    private String author;
    private String content;
    private String floor;
    private String url;
    private String time;
    private String title;
    private String bar;
    private String path;
    private String postID;
    
    public void setAuthor(String author) {
        this.author = author.replace("\"", "$quote");
    }
    
    public void setContent(String content) {
            this.content = content.replace("\"", "$quote");
           // System.out.println(content);
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    void setFloor(String Floor){
        this.floor=Floor;
    }

    void setTime(String Time){
        this.time=Time;
    }

    void setTitle(String Title){
        this.title=Title.replace("\"", "$quote");
    }

    void setBar(String Bar){
        if(Bar != null)
            this.bar=Bar.replace("\"", "$quote");
    }

    void setPath(String Path){
        this.path=Path;
    }
    
    void setPostID(String PostID) {
        postID = PostID;
    }

    public String getAuthor() {
        return this.author;
    }
    
    public String getContent() {
        return this.content;
    }

    public String getUrl() {
        return this.url;
    }

    public String getTime(){
        return this.time;
    }

    public String getTitle(){
        return this.title;
    }

    public String getBar(){
        return this.bar;
    }

    public String getPath(){
        return this.path;
    }
    
    public String getPostID() {

        return postID;
    }
    
    public String getFoolr() {
        return floor;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(author);
        sb.append(" : ");
        sb.append(content);
        sb.append("  :  ");
        sb.append(url);
        sb.append("  :  ");
        sb.append(time);
        sb.append("  :  ");
        sb.append(title);
        sb.append("  :  ");
        sb.append(bar);
        return sb.toString();
    }
    
    @Override
    public String getTableName() {
        return "Post";
    }
    
    @Override
    public String createTable() {
        String createTable = "CREATE TABLE Post(" +
                "UserName TEXT NOT NULL," +
                "Content TEXT NOT NULL," +
                "Time TEXT NOT NULL," +
                "Title TEXT NOT NULL," +
                "Bar TEXT NOT NULL," +
                "PostID TEXT UNIQUE NOT NULL," +
                "Url TEXT NOT NULL)";
        return createTable;
    }
    
    public String insertData() {
        String insert = "REPLACE INTO Post(UserName, Content,Time, Title, Bar, PostID, Url)Values(\'" +
                this.getAuthor() + "\',\'" +
                this.getContent() + "\',\'" +
                getTime() + "\',\'" +
                getTitle() + "\',\'" +
                getBar() + "\',\'" +
                getPostID() + "\',\'" +
                this.getUrl() + "\')";
        return insert;
    }
}
