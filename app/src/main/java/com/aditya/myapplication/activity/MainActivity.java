package com.aditya.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.aditya.myapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mega4tech.whatsappapilibrary.WhatsappApi;
import com.mega4tech.whatsappapilibrary.model.WContact;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private boolean isInstalled = false;
    private List<WContact> whatsAppContacts = new ArrayList<WContact>();
    private byte[] key = { (byte) 141, 75, 21, 92, (byte) 201, (byte) 255,
            (byte) 129, (byte) 229, (byte) 203, (byte) 246, (byte) 250, 120,
            25, 54, 106, 62, (byte) 198, 33, (byte) 166, 86, 65, 108,
            (byte) 215, (byte) 147 };

    private final byte[] iv = { 0x1E, 0x39, (byte) 0xF3, 0x69, (byte) 0xE9, 0xD,
            (byte) 0xB3, 0x3A, (byte) 0xA7, 0x3B, 0x44, 0x2B, (byte) 0xBB,
            (byte) 0xB6, (byte) 0xB0, (byte) 0xB9 };
    String backupPath, outputPath, TAG = "AdiMain";


    private String wantPermission = Manifest.permission.GET_ACCOUNTS;
    private Activity activity = MainActivity.this;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isInstalled = WhatsappApi.getInstance().isWhatsappInstalled();
//        whatsAppContacts.addAll(WhatsappApi.getInstance().getContacts(this, GetContactsListener));
        verifyStoragePermissions(this);
        getMessages();
        Log.d("MainActivity", isInstalled + "");
        if (!checkPermission(wantPermission)) {
            requestPermission(wantPermission);
        } else {
            getEmails();
        }
    }

    private void getMessages() {

//        private byte[] key = { (byte) 141, 75, 21, 92, (byte) 201, (byte) 255,
//                (byte) 129, (byte) 229, (byte) 203, (byte) 246, (byte) 250, 120,
//                25, 54, 106, 62, (byte) 198, 33, (byte) 166, 86, 65, 108,
//                (byte) 215, (byte) 147 };
//
//        private final byte[] iv = { 0x1E, 0x39, (byte) 0xF3, 0x69, (byte) 0xE9, 0xD,
//                (byte) 0xB3, 0x3A, (byte) 0xA7, 0x3B, 0x44, 0x2B, (byte) 0xBB,
//                (byte) 0xB6, (byte) 0xB0, (byte) 0xB9 };
        long start = System.currentTimeMillis();

        // create paths
//        backupPath = Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/WhatsApp/Databases/msgstore.db.crypt5";
        backupPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/WhatsApp/Databases/msgstore.db.crypt12";
//        backupPath = "/storage/emulated/0/WhatsApp/Databases/msgstore.db.crypt12";
        outputPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/WhatsApp/Databases/msgstore.db.decrypt";

        File backup = new File(backupPath);

        // check if file exists / is accessible
        if (!backup.isFile()) {
            Log.e(TAG, "Backup file not found! Path: " + backupPath);
            return;
        }


        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        AccountManager manager = AccountManager.get(this);
        // acquire account name
//        AccountManager manager = AccountManager.get(getApplicationContext());

//        Account[] accounts = manager.getAccountsByType("com.google");
        Account[] accounts = manager.getAccounts();

        if (accounts.length == 0) {
            Log.e(TAG, "Unable to fetch account!");
            return;
        }

        String account = accounts[0].name;


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }



        try {
            // calculate md5 hash over account name
            MessageDigest message = MessageDigest.getInstance("MD5");
            message.update(account.getBytes());
            byte[] md5 = message.digest();

            // generate key for decryption
            for (int i = 0; i < 24; i++)
                key[i] ^= md5[i & 0xF];

            // read encrypted byte stream
            byte[] data = new byte[(int) backup.length()];
            DataInputStream reader = new DataInputStream(new FileInputStream(
                    backup));
            reader.readFully(data);
            reader.close();

            // create output writer
            File output = new File(outputPath);
            DataOutputStream writer = new DataOutputStream(
                    new FileOutputStream(output));

            // decrypt file
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secret = new SecretKeySpec(key, "AES");
            IvParameterSpec vector = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secret, vector);
            writer.write(cipher.update(data));
            writer.write(cipher.doFinal());
            writer.close();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Could not acquire hash algorithm!", e);
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file!", e);
            return;
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong during the encryption!", e);
            return;
        }

        long end = System.currentTimeMillis();

        Log.i(TAG, "Success! It took " + (end - start) + "ms");

    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private void getEmails() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;

        // Getting all registered Google Accounts;
        // Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");

        // Getting all registered Accounts;
        Account[] accounts = AccountManager.get(this).getAccounts();

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                Log.d(TAG, String.format("%s - %s", account.name, account.type));
            }
        }
    }

    private boolean checkPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(activity, permission);
            if (result == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void requestPermission(String permission){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
            Toast.makeText(activity, "Get account permission allows us to get your email",
                    Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getEmails();
                } else {
                    Toast.makeText(activity,"Permission Denied.",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
