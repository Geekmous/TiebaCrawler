package crawler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestMD5 implements IDigest{
    char hexdigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'A', 'B', 'C', 'D', 'E', 'F'};
    MessageDigest md5;
    
    public void update(String str) {
        
        if(md5 == null) {
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        md5.update(str.toString().getBytes());
    }
    
    public String digest() {
        byte[] md = md5.digest();
        
        char[] str = new char[md.length * 2]; 
        int k = 0;
        for(int i = 0; i < md.length; i++) {
            byte b = md[i];
            str[k++] = hexdigits[b >>> 4 & 0xf];
            str[k++] = hexdigits[b & 0xf];
        }
        
        md5.reset();;    
        return new String(str);
    }
}
