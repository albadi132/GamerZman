package com.albadi.gamerzman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class buyone extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences prf;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Intent intent;
    int coins;
    String nameof , url, describe , available;
    int forsell;
    int price;
    int offer = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyone);

        Button cancle = (Button)findViewById(R.id.button);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myaccount_intent = new Intent(getApplicationContext(), myaccount.class);
                startActivity( myaccount_intent );

            }
        });




        TextView name = (TextView) findViewById(R.id.textView3);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        intent = new Intent(buyone.this, myaccount.class);
        name.setText(prf.getString("username", null));

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView coin = (TextView) findViewById(R.id.textView4);
                Integer coi =dataSnapshot.child("users").child(prf.getString("uid", null)).child("coins").getValue(Integer.class);
                coin.setText(coi.toString());

          forsell=dataSnapshot.child("store").child("number").getValue(Integer.class);

            for(int i=1;i <= forsell;i++)
            {
               nameof = dataSnapshot.child("store").child("product").child(String.valueOf(i)).child("name").getValue().toString();
                url = dataSnapshot.child("store").child("product").child(String.valueOf(i)).child("url").getValue().toString();
                describe = dataSnapshot.child("store").child("product").child(String.valueOf(i)).child("describe").getValue().toString();
                available= dataSnapshot.child("store").child("product").child(String.valueOf(i)).child("available").getValue().toString();
                price =  dataSnapshot.child("store").child("product").child(String.valueOf(i)).child("price").getValue(Integer.class);
               // if(available == "yes")
                    addacc();

            }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void addacc()
    {
        TextView textView = new TextView(this);
        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);
        textView.setText(nameof);
        textView1.setText(describe);
        textView2.setText( " عملة رقمية "+price);
        ImageView glow = new ImageView(this);
        ImageView axe = new ImageView(this);

        glow.setImageResource(R.drawable.glow);
        axe.setImageResource(R.drawable.mint);




        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        textView.setGravity(Gravity.CENTER);
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        textView1.setGravity(Gravity.CENTER);

        LinearLayout layoutH = new LinearLayout(this);
        layoutH.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout layoutV = new LinearLayout(this);
        layoutV.setOrientation(LinearLayout.VERTICAL);
        Button centerButton = new Button(this);
        centerButton.setText(" عملة رقمية( شراء ) "+price);
        centerButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        centerButton.setTextColor(Color.WHITE);
        centerButton.setPadding(12, 12, 12, 12);
        centerButton.setTag(offer);
        centerButton.setOnClickListener(this);

        LinearLayout layout = (LinearLayout) findViewById(R.id.contant);
        layout.addView(layoutH);

        if(offer == 1){
        layoutH.addView(glow);}
        if(offer == 2){
            layoutH.addView(axe);}



        layoutH.addView(layoutV);
        layoutV.addView(textView);
        layoutV.addView(textView1);
        layoutV.addView(centerButton);

        //imageskin.getLayoutParams().height=300;
       // imageskin.getLayoutParams().width=400;
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                5
        ));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));

        layout.addView(v);
        offer++;
    }


    @Override
    public void onClick(View view) {

        Context context = getApplicationContext();
        int result = Integer.parseInt(view.getTag().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(buyone.this);

        int duration = Toast.LENGTH_SHORT;
        if(result == 1) {


            builder.setTitle("تأكيد عملية الشراء");
            builder.setMessage("هل أنت متأكد من رغبتك في شراء glow skin");

            builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TextView coin = (TextView) findViewById(R.id.textView4);
                            Integer coi =dataSnapshot.child("users").child(prf.getString("uid", null)).child("coins").getValue(Integer.class);
                            String email =dataSnapshot.child("users").child(prf.getString("uid", null)).child("Email").getValue().toString();

                            if (coi > 150)
                            {
                                mDatabase.child("users").child(prf.getString("uid", null)).child("coins").setValue(coi-150);
                                Date currentTime = Calendar.getInstance().getTime();
                                DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                                String strDate = dateFormat.format(currentTime);
                                DateFormat datee = new SimpleDateFormat("yyyy-mm-dd hh:mm");
                                String strdatee = datee.format(currentTime);
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("name").setValue(prf.getString("username", null));
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("date").setValue(strdatee);
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("product").setValue("Glow Skin");
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("email").setValue(email);
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("done").setValue(0);
                                Toast.makeText(getApplicationContext(),"لقد تمت عملة الشراء بنجاح",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"ليس لديك عملات نقدية كافية",Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    Toast.makeText(getApplicationContext(),"تم إلغاء عملية الشراء",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        if(result == 2){



            builder.setTitle("تأكيد عملية الشراء");
            builder.setMessage("هل أنت متأكد من رغبتك في شراء Minty Axe Pickaxe");

            builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TextView coin = (TextView) findViewById(R.id.textView4);
                            Integer coi =dataSnapshot.child("users").child(prf.getString("uid", null)).child("coins").getValue(Integer.class);
                            String email =dataSnapshot.child("users").child(prf.getString("uid", null)).child("Email").getValue().toString();

                            if (coi > 900)
                            {
                                mDatabase.child("users").child(prf.getString("uid", null)).child("coins").setValue(coi-900);
                                Date currentTime = Calendar.getInstance().getTime();
                                DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                                String strDate = dateFormat.format(currentTime);
                                DateFormat datee = new SimpleDateFormat("yyyy-mm-dd- hh:mm");
                                String strdatee = datee.format(currentTime);
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("name").setValue(prf.getString("username", null));
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("date").setValue(strdatee);
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("product").setValue("Minty Axe Pickaxe");
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("email").setValue(email);
                                mDatabase.child("purchases").child(prf.getString("uid", null)).child(strDate).child("done").setValue(0);
                                Toast.makeText(getApplicationContext(),"لقد تمت عملة الشراء بنجاح",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"ليس لديك عملات نقدية كافية",Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    Toast.makeText(getApplicationContext(),"تم إلغاء عملية الشراء",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            AlertDialog axe = builder.create();
            axe.show();
        }


    }
}
