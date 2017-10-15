import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class DRMManager {
    public static String CURRENT_PLAYING_SONG_FILENAME = "curr.mp3";
    private static DRMManager instance = null;
    private static final String secKey = "$..@.V..n..67.44.1abcdefghijklmn";
    public static boolean useNative;

    static {
        boolean z;
        useNative = false;
        try {
            System.loadLibrary("ndkdrm");
            z = true;
        } catch (Throwable th) {
            z = false;
        }
        useNative = z;
    }

    protected DRMManager() {
    }

    public static DRMManager getInstance() {
        if (instance == null) {
            instance = new DRMManager();
        }
        return instance;
    }

    private String sha512(String str) throws Exception {
        MessageDigest instance = MessageDigest.getInstance("SHA-512");
        instance.update(str.getBytes());
        byte[] digest = instance.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : digest) {
            stringBuffer.append(Integer.toString((b & 255) + 256, 16).substring(1));
        }
        return stringBuffer.toString();
    }

    protected String decryptSecKey() {
        return secKey;
    }

    public boolean decryptSong(String str) {
        try {
            InputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(new File("C:/Users/Owner/Desktop/AWS_ML/DRMManager/content/unencrypt.mp3"));
            Key secretKeySpec = new SecretKeySpec(decryptSecKey().getBytes(), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(2, secretKeySpec);
            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, instance);
            byte[] bArr = new byte[2097152];
            while (true) {
                int read = cipherInputStream.read(bArr);
                if (read == -1) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    cipherInputStream.close();
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean encryptSongFromCache(String str, String str2) {
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
/*
    protected void genSecKey() throws Exception {
        SharedPreferences sharedPreferences = this.ctx.getSharedPreferences("app_state", 0);
        if (sharedPreferences.getString("secKey", null) == null) {
            String str = null;
            String devId = Utils.getDevId(null);
            String substring = sha512(new StringBuilder(String.valueOf(devId)).append(String.valueOf(System.currentTimeMillis())).toString()).substring(0, 16);
            Cipher instance = Cipher.getInstance("AES");
            try {
                instance.init(1, new SecretKeySpec(secKey.getBytes(), "AES"));
                str = new String(instance.doFinal());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Editor edit = sharedPreferences.edit();
            edit.putString("secKey", str);
            edit.commit();
        }
    }
*/
    public native int ndkdecrypt(String str, String str2, String str3);

    public native int ndkdecryptPartial(String str, String str2, String str3);

    public native int ndkencrypt(String str, String str2, String str3);

    public native int ndkencryptPartial(String str, String str2, String str3);
}
