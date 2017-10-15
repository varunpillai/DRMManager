import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class test {

	public static void main(String[] args) {
/*		
		try {
            FileInputStream fileInputStream = new FileInputStream("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/testing/curr.mp3");
            OutputStream fileOutputStream = new FileOutputStream("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/testing/encrypt.mp3");
            Key secretKeySpec = new SecretKeySpec("$..@.V..n..67.44.1abcdefghijklmn".getBytes(), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(Cipher.ENCRYPT_MODE, secretKeySpec);
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
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
		
		
	       try {
	            InputStream fileInputStream = new FileInputStream("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/testing/Ni8m5_EF.mp3");
	            FileOutputStream fileOutputStream = new FileOutputStream(new File("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/testing/unencrypt.mp3"));
	            Key secretKeySpec = new SecretKeySpec("$..@.V..n..67.44.1abcdefghijklmn".getBytes("UTF-8"), "AES");
	            Cipher instance = Cipher.getInstance("AES");
	            instance.init(Cipher.DECRYPT_MODE, secretKeySpec);
	            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, instance);
	            byte[] bArr = new byte[2097152];
	            int i = 0;
	            while (true) {
	            	i++;
	            	System.out.println("i: "+i);
	            	System.out.println("barr: "+bArr[i]);
	                int read = cipherInputStream.read(bArr);
	                System.out.println(read);
	                if (read == -1) {
	                    break;
	                }
	                fileOutputStream.write(bArr, 0, read);
	            }
	            fileOutputStream.flush();
                fileOutputStream.close();
                cipherInputStream.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		*/
		
		try {
			DRMManager instance = DRMManager.getInstance();
			if (DRMManager.useNative) {
				int ndkdecryptPartial;
				ndkdecryptPartial = instance.ndkdecryptPartial(
						"C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/testing/Ni8m5_EF.mp3",
						"C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/testing", "curr.mp3");
				System.out.println("ndkdrm: " + ndkdecryptPartial);
			} else {
				boolean song = instance
						.decryptSong("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/testing/Ni8m5_EF.mp3");
				System.out.println(song);
			}

		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

}
