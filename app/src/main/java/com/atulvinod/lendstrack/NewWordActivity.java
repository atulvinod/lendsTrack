package com.atulvinod.lendstrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.atulvinod.lendstrack.R;

public class NewWordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    public static final String AMOUNT_REPLY = "com.example.android.wordlistsql.AMOUNT";
    public static final String DESC = "com.example.android.wordlistsql.DESC";
    public static final String TRANS = "com.example.android.wordlistsql.TRANS";


    private EditText text,amount,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        text = findViewById(R.id.edit_word);
        amount = findViewById(R.id.numberInput);
        desc = findViewById(R.id.initialDesriptionText);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reply = new Intent();
                if(TextUtils.isEmpty(text.getText())){
                    setResult(RESULT_CANCELED,reply);
                }else{
                    String textWord = text.getText().toString();
                    String amountText = amount.getText().toString();
                    String description = desc.getText().toString();

                    RadioGroup transacationIndicator = findViewById(R.id.transactionIndicator);
                    RadioButton rButton = findViewById(transacationIndicator.getCheckedRadioButtonId());
                    String transactionType = "";
                    if(rButton.getText().equals("Gave money")){
                    transactionType = MainActivity.GIVEN_MONEY;
                    }else{
                        transactionType = MainActivity.TAKEN_MONEY;
                    }
                    reply.putExtra(EXTRA_REPLY,textWord);
                    reply.putExtra(AMOUNT_REPLY,amountText);
                    reply.putExtra(DESC,description);
                    reply.putExtra(TRANS,transactionType);
                    setResult(RESULT_OK,reply);
                }
                finish();
            }

        });


    }

}
