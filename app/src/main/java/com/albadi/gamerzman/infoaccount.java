package com.albadi.gamerzman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class infoaccount extends AppCompatActivity {
    SharedPreferences prf;
    Intent intent;
    TextView mass;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DataSnapshot bill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoaccount);

        Button canclee = (Button)findViewById(R.id.button5);

        canclee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myaccountt_intent = new Intent(getApplicationContext(), myaccount.class);
                startActivity( myaccountt_intent );

            }
        });

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(infoaccount.this,myaccount.class);





        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView coin = (TextView) findViewById(R.id.textView4);
                Integer coi =dataSnapshot.child("users").child(prf.getString("uid", null)).child("coins").getValue(Integer.class);
                coin.setText(coi.toString());
                TextView name = (TextView) findViewById(R.id.textView3);
               // name.setText(dataSnapshot.child("users").child(prf.getString("uid", null)).child("name").getValue().toString());

if(dataSnapshot.child("purchases").hasChild(prf.getString("uid", null)))
{
    DataSnapshot contactSnapshot = dataSnapshot.child("purchases").child(prf.getString("uid", null));
    Iterable<DataSnapshot> contactChildren = contactSnapshot.getChildren();
    for (DataSnapshot contact : contactChildren) {
        bill = contact;
            addbill();

    }



}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addbill()
    {
String id = bill.getKey();
String name = bill.child("name").getValue().toString();
String datee =bill.child("date").getValue().toString();
String emaill = bill.child("email").getValue().toString();
String productt = bill.child("product").getValue().toString();
//int done = bill.child("done").getValue(Integer.class);

        TextView textView = new TextView(this);
        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);
        TextView textView3 = new TextView(this);
        TextView textView4 = new TextView(this);
    //    TextView line = new TextView(this);
   //     TextView line1 = new TextView(this);
          TextView billl = new TextView(this);
        TextView notee = new TextView(this);
    //    line.setText("_______________________");
   //     line1.setText("--------------");
          billl.setText("فاتورة الشراء");
        notee.setText("تواصل مع صاحب الرقم 94311152 مع صورة الفاتورة");



       textView.setText( " رقم الفاتورة: "+ id);
        textView1.setText(" صاحب الفاتورة: "+ name);
        textView2.setText(" تاريخ عملية الشراء: "+datee);
        textView3.setText(" الاميل: "+ emaill);
        textView4.setText("الحزمة: "+productt);


        LinearLayout layout = (LinearLayout) findViewById(R.id.contant);
        LinearLayout layoutV = new LinearLayout(this);
        layoutV.setOrientation(LinearLayout.VERTICAL);


        layout.addView(layoutV);
     //  layoutV.addView(line);
    //   layoutV.addView(line1);
       layoutV.addView(billl);
   //     layoutV.addView(line1);
       layoutV.addView(textView);
        layoutV.addView(textView1);
       layoutV.addView(textView3);
        layoutV.addView(textView2);
        layoutV.addView(textView4);
     //   layoutV.addView(notee);
   //     layoutV.addView(line);

        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                5
        ));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));

        layout.addView(v);

    }

}
