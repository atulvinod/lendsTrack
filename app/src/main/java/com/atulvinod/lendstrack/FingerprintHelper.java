package com.atulvinod.lendstrack;

import android.Manifest;
import android.annotation.TargetApi;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.atulvinod.lendstrack.UpdateDialog;


@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHelper extends FingerprintManager.AuthenticationCallback {

    // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
    // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//

    private CancellationSignal cancellationSignal;
    private Context context;
    private View v;

    public boolean getAUTHENTICATION_STATUS() {
        return AUTHENTICATION_STATUS;
    }

    public void setAUTHENTICATION_STATUS(boolean AUTHENTICATION_STATUS) {
        this.AUTHENTICATION_STATUS = AUTHENTICATION_STATUS;
    }

    private boolean AUTHENTICATION_STATUS = false;


    public FingerprintHelper(Context mContext) {
        context = mContext;
    }
    public FingerprintHelper(Context c, View v){
        this.context = c;
        this.v = v;
    }


    //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    //onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters//

    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        //I’m going to display the results of fingerprint authentication as a series of toasts.
        //Here, I’m creating the message that’ll be displayed if an error occurs//


            ///Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();

        setAUTHENTICATION_STATUS(false);

    }

    @Override

    //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//

    public void onAuthenticationFailed() {
        if(UpdateDialog.fingerprintStatus!=null){
            UpdateDialog.fingerprintStatus.setText("Authentication Failed");
        }else
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
        setAUTHENTICATION_STATUS(false);
        if(DeleteDialog.yes!=null){
            DeleteDialog.yes.setEnabled(false);
            DeleteDialog.fingerprintStatus.setText("Authentication Failed");
        }
    }

    @Override

    //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
    //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        setAUTHENTICATION_STATUS(false);
        if(UpdateDialog.fingerprintStatus!=null){
            UpdateDialog.fingerprintStatus.setText(helpString);
        }
        if(DeleteDialog.yes!=null){
            DeleteDialog.fingerprintStatus.setText(helpString);
        }
        Toast.makeText(context,helpString, Toast.LENGTH_LONG).show();
    }
    @Override
    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        setAUTHENTICATION_STATUS(true);
        if (UpdateDialog.add != null & UpdateDialog.deduct != null) {
            UpdateDialog.add.setEnabled(true);
            UpdateDialog.deduct.setEnabled(true);
            UpdateDialog.fingerprintStatus.setText("Authentication Successful");
            Toast.makeText(context, "Fingerprint Authenticated", Toast.LENGTH_SHORT).show();

        }


        if(DeleteDialog.yes!=null){
            DeleteDialog.yes.setEnabled(true);
            DeleteDialog.fingerprintStatus.setText("Authentication Successful");
        }
        Toast.makeText(context, "Fingerprint Authenticated", Toast.LENGTH_LONG).show();

    }

}
