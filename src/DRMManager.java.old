import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class DRMManager {
    private static final String secKey = "$..@.V..n..67.44.1abcdefghijklmn";

    protected String decryptSecKey() {
        return secKey;
    }

    public boolean decryptSong() {
        try {
            InputStream fileInputStream = new FileInputStream("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/encrypt.mp3");
            FileOutputStream fileOutputStream = new FileOutputStream(new File("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/unencrypt.mp3"));
            Key secretKeySpec = new SecretKeySpec(decryptSecKey().getBytes(), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(2, secretKeySpec);
            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, instance);
            byte[] bArr = new byte[2097152];
            while (true) {
                int read = cipherInputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            cipherInputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean encryptSong() {
        try {
            FileInputStream fileInputStream = new FileInputStream("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/test_ori.mp3");
            OutputStream fileOutputStream = new FileOutputStream("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/encrypt.mp3");
            Key secretKeySpec = new SecretKeySpec(decryptSecKey().getBytes(), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(1, secretKeySpec);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, instance);
            byte[] bArr = new byte[1048576];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                cipherOutputStream.write(bArr, 0, read);
            }
            cipherOutputStream.flush();
            cipherOutputStream.close();
            fileInputStream.close();
          
            return true;
        } catch(Exception e) {
			System.out.print(e.getMessage());
			return false;
		}
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length==0)
		   {
		     System.out.println("Error: Bad command or filename. Syntax: java [filename.tpl]");
		     System.exit(0);
		   }
		
		DRMManager test = new DRMManager();
		boolean enc = test.encryptSong();
		System.out.println("encryption: "+enc);

	}

}
