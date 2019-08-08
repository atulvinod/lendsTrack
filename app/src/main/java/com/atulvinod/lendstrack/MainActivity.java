package com.atulvinod.lendstrack;

import android.Manifest;
import android.app.KeyguardManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atulvinod.lendstrack.NewWordActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_ENTITY_ACTIVITY_REQUEST_CODE = 1;

    public static EntityViewModel model;

    private static final String KEY_NAME = "yourKey";
    private static final String PREF= "preferences";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    public static FingerprintManager.CryptoObject cryptoObject;
    public static FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private IndiaCurrencyFormatter formatter;
    static FingerprintModule module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        formatter = new IndiaCurrencyFormatter();
        setContentView(R.layout.activity_main);

       module = new FingerprintModule(this);

        RecyclerView rv = findViewById(R.id.list);
        model = ViewModelProviders.of(this).get(EntityViewModel.class);
        final TextView totalLent = findViewById(R.id.totalLentView);
        final ListAdapter adapter = new ListAdapter(this,model,this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        model.getAllWords().observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(@Nullable List<Entity> entities) {
                int Total = 0;
                for(int i = 0;i<entities.size();i++){
                    Total = Total+entities.get(i).getAmount();
                }

                totalLent.setText(formatter.formatAmount(Total));
                adapter.setElements(entities);
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewWordActivity.class);
            startActivityForResult(i,NEW_ENTITY_ACTIVITY_REQUEST_CODE);
            }
        });

//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//            keyguardManager =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
//            fingerprintManager = (FingerprintManager)getSystemService((FINGERPRINT_SERVICE));
//
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(!fingerprintManager.isHardwareDetected()){
//                Toast.makeText(this,"Your device doesent Support FingerPrint Authentication",Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this,"App doesnt have the proper permissions granted",Toast.LENGTH_SHORT).show();
//                finish();
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!fingerprintManager.hasEnrolledFingerprints()) {
//                // If the user hasn’t configured any fingerprints, then display the following message//
//                Toast.makeText(this,"Device should have atleast one fingerprint enrolled",Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            if (!keyguardManager.isKeyguardSecure()) {
//                // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
//                Toast.makeText(this,"The device should be keyguard secured for proper security, enable it in device settings",Toast.LENGTH_SHORT).show();
//                finish();
//            }else{
//                try{
//                    generateKey();
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//                if(initCypher()){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        cryptoObject = new FingerprintManager.CryptoObject(cipher);
//                    }
//                  //  FingerprintHandler handler = new FingerprintHandler(this);
////helper.startAuth(fingerprintManager, cryptoObject);
//                }
//            }
//        }
//    }
//    private void generateKey() throws FingerprintException{
//        try{
//            keyStore = KeyStore.getInstance("AndroidKeyStore");
//            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
//            keyStore.load(null);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                keyGenerator.init(new
//
//                        //Specify the operation(s) this key can be used for//
//                        KeyGenParameterSpec.Builder(KEY_NAME,
//                        KeyProperties.PURPOSE_ENCRYPT |
//                                KeyProperties.PURPOSE_DECRYPT)
//                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//
//                        //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
//                        .setUserAuthenticationRequired(true)
//                        .setEncryptionPaddings(
//                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                        .build());
//            }
//            keyGenerator.generateKey();
//        }catch(Exception e){
//            e.printStackTrace();
//            throw  new FingerprintException(e);
//        }
//    }
//    public boolean initCypher(){
//        try {
//            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
//            cipher = Cipher.getInstance(
//                    KeyProperties.KEY_ALGORITHM_AES + "/"
//                            + KeyProperties.BLOCK_MODE_CBC + "/"
//                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//        } catch (NoSuchAlgorithmException |
//                NoSuchPaddingException e) {
//            throw new RuntimeException("Failed to get Cipher", e);
//        }
//
//        try {
//            keyStore.load(null);
//            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
//                    null);
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            //Return true if the cipher has been initialized successfully//
//            return true;
//        } catch (Exception e) {
//
//            //Return false if cipher initialization failed//
//            return false;
//        }
//    }
//    private class FingerprintException extends Exception {
//        public FingerprintException(Exception e) {
//            super(e);
//        }
   }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==NEW_ENTITY_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK){

            Entity entity = new Entity(Integer.parseInt(data.getStringExtra(NewWordActivity.AMOUNT_REPLY)),data.getStringExtra(NewWordActivity.EXTRA_REPLY),0);

            model.insert(entity);

        }
    }
}
