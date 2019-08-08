package com.atulvinod.lendstrack;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;


public class FingerprintModule {

    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;

    private KeyguardManager keyguardManager;
    static FingerprintManager.CryptoObject cryptoObject;
    static FingerprintManager fingerprintManager;
    Activity mActivity;
    private static final String KEY_NAME = "yourKey";



    public FingerprintModule(Activity activity) {
        this.mActivity = activity;
        init();
    }
    public FingerprintManager getFingerprintManager(){
        return fingerprintManager;
    }
    public FingerprintManager.CryptoObject getCryptoObject(){
        return  cryptoObject;
    }



    private void init(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            keyguardManager =(KeyguardManager)mActivity.getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager)mActivity.getSystemService((FINGERPRINT_SERVICE));

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!fingerprintManager.isHardwareDetected()){
                Toast.makeText(mActivity.getApplicationContext(),"Your device doesent Support FingerPrint Authentication",Toast.LENGTH_SHORT).show();
                mActivity.finish();
            }
        }
        if(ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(mActivity.getApplicationContext(),"App doesnt have the proper permissions granted",Toast.LENGTH_SHORT).show();
            mActivity.finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                // If the user hasn’t configured any fingerprints, then display the following message//
                Toast.makeText(mActivity.getApplicationContext(),"Device should have atleast one fingerprint enrolled",Toast.LENGTH_SHORT).show();
                mActivity.finish();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (!keyguardManager.isKeyguardSecure()) {
                // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
                Toast.makeText(mActivity.getApplicationContext(),"The device should be keyguard secured for proper security, enable it in device settings",Toast.LENGTH_SHORT).show();
                mActivity.finish();
            }else{
                try{
                    generateKey();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(initCypher()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    }
                    //  FingerprintHandler handler = new FingerprintHandler(this);
//helper.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
    }
    private void generateKey() throws FingerprintException{
        try{
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
            keyStore.load(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyGenerator.init(new

                        //Specify the operation(s) this key can be used for//
                        KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                        //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
            }
            keyGenerator.generateKey();
        }catch(Exception e){
            e.printStackTrace();
            throw  new FingerprintException(e);
        }
    }
    public boolean initCypher(){
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (Exception e) {

            //Return false if cipher initialization failed//
            return false;
        }
    }
    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }
    }






