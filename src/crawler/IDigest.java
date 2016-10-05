package crawler;

public interface IDigest {
    public void update(String str);
    public String digest();
}
