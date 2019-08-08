package com.atulvinod.lendstrack;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atulvinod.lendstrack.R;
import com.atulvinod.lendstrack.EntityViewModel;
import com.atulvinod.lendstrack.FingerprintHelper;
import com.atulvinod.lendstrack.MainActivity;
import com.atulvinod.lendstrack.RecordRepository;

public class DeleteDialog extends Dialog {
    EntityViewModel model;
    private Activity mActivity;
    static Button yes,no;
    static TextView fingerprintStatus;
    FingerprintHelper fingerprint;
    RecordRepository repo;
   int ID;
    public DeleteDialog(Activity e,EntityViewModel model,int id){
        super(e);
        mActivity = e;
        this.model = model;
        fingerprint = new FingerprintHelper(e);
        this.ID = id;
        new getRepo(ID).execute(e.getApplication());

    }
    class getRepo extends AsyncTask<Application,Void,Void>{
        int ID;
        public getRepo(int ID){
            this.ID = ID;


        }
        @Override
        protected Void doInBackground(Application... contexts) {
            repo = new RecordRepository(contexts[0],ID);
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);
        yes = findViewById(R.id.yesButton);
        yes.setEnabled(false);
        no = findViewById(R.id.noButton);
        fingerprint.startAuth(MainActivity.module.getFingerprintManager(),MainActivity.module.getCryptoObject());
        fingerprintStatus = findViewById(R.id.fingerprint);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity.getApplicationContext(),"Changes were saved",Toast.LENGTH_SHORT).show();
                model.delete(ID);
                repo.deleteAll(ID);
                dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
