package com.atulvinod.lendstrack;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atulvinod.lendstrack.R;
import com.atulvinod.lendstrack.RecordViewModel;
import com.atulvinod.lendstrack.IndiaCurrencyFormatter;
import com.atulvinod.lendstrack.MainActivity;
import com.atulvinod.lendstrack.RecordRepository;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class History extends AppCompatActivity {
    public RecordViewModel viewModel;
    RecordRepository rp = null;
    public RecyclerView view;
    IndiaCurrencyFormatter formatter;
    int avalibleAmount;
    TextView history_avail;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        history_avail = findViewById(R.id.history);

        setSupportActionBar(toolbar);
        formatter = new IndiaCurrencyFormatter();
        Intent i = getIntent();
        int ID = i.getExtras().getInt("TRANSACTION_ID");
        avalibleAmount=i.getExtras().getInt("AVALIBLE_AMOUNT");

       getRepo r = new getRepo(ID);
       r.execute(this.getApplication());

        try {
            rp = r.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<Record> rc = rp.getRecordList();
        view = findViewById(R.id.transationView);
        ViewAdapter adapter = new ViewAdapter(rc,this,history_avail);
        history_avail.setVisibility(View.VISIBLE);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(adapter);
        adapter.touchhelper.attachToRecyclerView(view);


    }



    class getRepo extends AsyncTask<Application,Void,RecordRepository>{
       int ID = 0;
        public RecordRepository repo;
        public getRepo(int Id){
            this.ID = Id;
        }

        @Override
        protected RecordRepository doInBackground(Application... contexts) {
            repo = new RecordRepository(contexts[0],ID);
            return repo;
        }
    }
    class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.holder>{

        private final LayoutInflater inflater;
        private List<Record> records;
        private Context c;
        TextView text;
        public ViewAdapter(List<Record> r,Context c,TextView text){
            records  =r;
            this.c = c;
            inflater = LayoutInflater.from(c);
            this.text = text;

        }
        @NonNull
        @Override
        public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = inflater.inflate(R.layout.transactioncard,parent,false);

            return new holder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull holder holder, int position) {
            Record r = records.get(position);
            String desc = r.getDescription();
            if (desc==""||desc == " "||desc.equals("")||desc.equals(" ")){
                desc = "No description available";
            }

            holder.discription.setText(desc);
            holder.dateTime.setText(r.getDate());
            holder.amount.setText(r.getAmount());
            if(r.getAmount().toString().contains("+")){
                holder.card.setBackgroundColor(Color.parseColor("#4e9525"));
            }else{
                holder.card.setBackgroundColor(Color.parseColor("#c1224f"));

            }
            holder.card.setRadius(20);
            holder.setSno(r.getSno());

        }

        @Override
        public int getItemCount() {
            if(records==null) {
                text.setVisibility(View.VISIBLE);
                ((ViewGroup)view.getParent()).removeView(view);
                return 0;
            }else{

          text.setVisibility(View.INVISIBLE);
                return records.size();
            }
        }

        class holder extends RecyclerView.ViewHolder{
            TextView amount,dateTime,discription;
            CardView card;
            int sno;
            void setSno(int sno){
                this.sno = sno;
            }
            int getSno(){
                return sno;
            }
            public holder(View V){
                super(V);

                amount = V.findViewById(R.id.amountView);
                dateTime = V.findViewById(R.id.dateTimeView);
                discription = V.findViewById(R.id.discriptionView);
                card = V.findViewById(R.id.card);
            }
        }

        /*A callback is created to handle the swipe gestures performed on the list elements*/
        ItemTouchHelper.SimpleCallback touchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                /*Code to remove the swiped element form the main array*/
                holder hold = (holder)viewHolder;  //casts the viewholder to our custom viewholder

                /*Creates builds our dialogplus dialog box*/
                final DialogPlusBuilder dialog = DialogPlus.newDialog(c).setGravity(Gravity.BOTTOM).setContentHolder(new ViewHolder(R.layout.reflect_changes_dialog_layout)).setExpanded(true);

                final DialogPlus d = dialog.create();

                /*Gets the view of our dialogplus box*/
                View v = d.getHolderView();

                /*Init the buttons*/
                Button yes = v.findViewById(R.id.yesButton);
                Button no = v.findViewById(R.id.noButton);

                /*Linear search the record from the array and set the delete variable to its equal*/
                Record delete = null;
                for(int i =0;i<records.size();i++){
                    if(records.get(i).SNo==hold.getSno()){
                        delete = records.get(i);
                    }
                }
                /*Initializes some variables to be used inside the onClick listener*/
                final int  i = delete.getID();
                final String am = delete.getAmount().split(" ")[1];
                final String operator = delete.getAmount().split(" ")[0];

                /*Sets on click listeners*/
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(operator.equals("+")){
                        MainActivity.model.update (new EntityData(i,avalibleAmount-Integer.parseInt(am)));
                        Log.d("OPERATION","DEDUCT");
                        Toast.makeText(c,"The amount was deducted from the account.\nPrevious amount was "+ formatter.formatAmount(avalibleAmount)+", updated amount is "+formatter.formatAmount(avalibleAmount-Integer.parseInt(am)),Toast.LENGTH_LONG).show();
                    }else{
                        MainActivity.model.update (new EntityData(i,avalibleAmount+Integer.parseInt(am)));
                        Toast.makeText(c,"The amount was added to the account.\nPrevious amount was "+formatter.formatAmount(avalibleAmount)+", updated amount is "+formatter.formatAmount(avalibleAmount+Integer.parseInt(am)),Toast.LENGTH_LONG).show();
                        Log.d("OPERATION","ADD");
                    }
                    d.dismiss();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                /*Dialog box is displayed*/
                d.show();

                Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();

                TextView desc = v.findViewById(R.id.reflectChanges_discription);

                /*Modifies our dialog box discription*/
                int amount = Integer.parseInt(delete.getAmount().split(" ")[1]);
                if(operator.equals("+")){
                    desc.setText("Do you want to deduct "+formatter.formatAmount(amount)+" back to the account?");
                }else{
                    desc.setText("Do you want to add "+formatter.formatAmount(amount)+" back to the account?");
                }

                /*Delete the record form the database , notify the dataset has been changed and remove the record from the array itself*/
                rp.deleteRecord(hold.getSno());
                records.remove(delete);
                notifyDataSetChanged();

            }
        };

        /*creates our touchhelper*/
        ItemTouchHelper touchhelper = new ItemTouchHelper(touchCallback);




    }

}
