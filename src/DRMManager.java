package com.saavn.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import com.saavn.android.downloadManager.DownloadFileIntentService;
import com.saavn.android.utils.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class DRMManager {
    public static String CURRENT_PLAYING_SONG_FILENAME = "curr.mp3";
    private static DRMManager instance = null;
    private static final String secKey = "$..@.V..n..67.44.1abcdefghijklmn";
    public static boolean useNative;
    Context ctx;

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
            stringBuffer.append(Integer.toString((b & MotionEventCompat.ACTION_MASK) + 256, 16).substring(1));
        }
        return stringBuffer.toString();
    }

    protected String decryptSecKey() {
        return secKey;
    }

    public String decryptSong(String str) {
        try {
            Log.i("OfflineMode", "Song decryption started");
            InputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(Utils.GetExternalFilesDirectory(DownloadFileIntentService.SONGS_DIRECTORY_NAME), CURRENT_PLAYING_SONG_FILENAME));
            Key secretKeySpec = new SecretKeySpec(decryptSecKey().getBytes(), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(2, secretKeySpec);
            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, instance);
            byte[] bArr = new byte[AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_END];
            while (true) {
                int read = cipherInputStream.read(bArr);
                if (read == -1) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    cipherInputStream.close();
                    File file = new File(Utils.GetExternalFilesDirectory(DownloadFileIntentService.SONGS_DIRECTORY_NAME), CURRENT_PLAYING_SONG_FILENAME);
                    Log.i("OfflineMode", "Song decrypted successfully");
                    return file.getAbsolutePath();
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.i("OfflineMode", "Song decrypted Failed");
            return null;
        } catch (InvalidKeyException e2) {
            e2.printStackTrace();
            Log.i("OfflineMode", "Song decrypted Failed");
            return null;
        } catch (NoSuchPaddingException e3) {
            e3.printStackTrace();
            Log.i("OfflineMode", "Song decrypted Failed");
            return null;
        } catch (IOException e4) {
            e4.printStackTrace();
            Log.i("OfflineMode", "Song decrypted Failed");
            return null;
        }
    }

    public boolean encryptSongFromCache(String str, String str2) {
        try {
            Log.i("OfflineMode", "Song encryption started");
            FileInputStream fileInputStream = new FileInputStream(new StringBuilder(String.valueOf(str)).append("/").append(str2).toString());
            OutputStream fileOutputStream = new FileOutputStream(new File(Utils.GetExternalFilesDirectory(DownloadFileIntentService.SONGS_DIRECTORY_NAME), str2));
            Key secretKeySpec = new SecretKeySpec(decryptSecKey().getBytes(), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(1, secretKeySpec);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, instance);
            byte[] bArr = new byte[AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START];
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
            Log.i("OfflineMode", "Song encrypted successfully");
            return true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            new File(Utils.GetExternalFilesDirectory(DownloadFileIntentService.SONGS_DIRECTORY_NAME), str2).delete();
            return true;
        } catch (InvalidKeyException e2) {
            new File(Utils.GetExternalFilesDirectory(DownloadFileIntentService.SONGS_DIRECTORY_NAME), str2).delete();
            e2.printStackTrace();
            return false;
        } catch (NoSuchPaddingException e3) {
            new File(Utils.GetExternalFilesDirectory(DownloadFileIntentService.SONGS_DIRECTORY_NAME), str2).delete();
            e3.printStackTrace();
            return false;
        } catch (IOException e4) {
            new File(Utils.GetExternalFilesDirectory(DownloadFileIntentService.SONGS_DIRECTORY_NAME), str2).delete();
            e4.printStackTrace();
            return false;
        } finally {
            new File(new StringBuilder(String.valueOf(str)).append("/").append(str2).toString()).delete();
        }
    }

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

    public native int ndkdecrypt(String str, String str2, String str3);

    public native int ndkdecryptPartial(String str, String str2, String str3);

    public native int ndkencrypt(String str, String str2, String str3);

    public native int ndkencryptPartial(String str, String str2, String str3);
}

public class DRMManager {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
