package com.albadi.gamerzman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class myaccount extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "users";
    SharedPreferences prf;
    Intent intent;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String accName;
    String accSkin , accDance, accAxe;
    int acc = 1;
    int buyy = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        final Button addaccunt = (Button) findViewById(R.id.button);
final ImageView coins = (ImageView)findViewById(R.id.imageView3) ;
        final Button buy = (Button) findViewById(R.id.button4);

        TextView name = (TextView) findViewById(R.id.textView3);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        intent = new Intent(myaccount.this, MainActivity.class);
        name.setText(prf.getString("username", null));


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                         TextView coin = (TextView) findViewById(R.id.textView4);
                                                         Integer coi =dataSnapshot.child("users").child(prf.getString("uid", null)).child("coins").getValue(Integer.class);
                                                         buyy =dataSnapshot.child("users").child(prf.getString("uid", null)).child("account").getValue(Integer.class);
                                                         coin.setText(coi.toString());

                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                     }
                                                 });


                coins.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        prf = getSharedPreferences("user_details", MODE_PRIVATE);
                        intent = new Intent(myaccount.this, addcoins.class);
                        Intent addcoins_intent = new Intent(getApplicationContext(), addcoins.class);
                        startActivity(addcoins_intent);


                    }
                });
        addaccunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  goToaddAcc();


            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (buyy == 0)
                   {
                       Toast.makeText(getApplicationContext(),"أضف حساب أولاً",Toast.LENGTH_SHORT).show();}
               else {
                goToBuy();}


            }
        });


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer acc =dataSnapshot.child("users").child(prf.getString("uid", null)).child("account").getValue(Integer.class);

                for (int i = 1; i <= acc; i++) {
                    accName= dataSnapshot.child("account").child(prf.getString("uid", null)).child(String.valueOf(i)).child("name").getValue().toString();
                   accSkin = dataSnapshot.child("account").child(prf.getString("uid", null)).child(String.valueOf(i)).child("skin").getValue().toString();
                   accDance = dataSnapshot.child("account").child(prf.getString("uid", null)).child(String.valueOf(i)).child("dance").getValue().toString();
                   accAxe = dataSnapshot.child("account").child(prf.getString("uid", null)).child(String.valueOf(i)).child("axe").getValue().toString();
                    addacc();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void goToaddAcc()
    {

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(myaccount.this,addaccount.class);
        Intent addaccount_intent = new Intent(getApplicationContext(), addaccount.class);
        startActivity( addaccount_intent );

    }
    public void goToBuy()
    {

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(myaccount.this,buyone.class);
        Intent buyone_intent = new Intent(getApplicationContext(), buyone.class);
        startActivity( buyone_intent );

    }
    public void addacc()
    {
        TextView textView = new TextView(this);
        TextView line = new TextView(this);
        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);
        TextView textView3 = new TextView(this);
        textView.setText(accName);
        line.setText("-------------");
        textView1.setText(" | عدد السكنات " + accSkin);
        textView2.setText(" | عدد الرقصات " + accDance);
        textView3.setText(" عدد الفؤوس " + accAxe);
        ImageView imageskin = new ImageView(this);
        ImageView imagedance= new ImageView(this);
        ImageView imageaxe = new ImageView(this);

        imageskin.setImageResource(R.drawable.skin);
        imagedance.setImageResource(R.drawable.dance);
        imageaxe.setImageResource(R.drawable.axe);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        textView.setGravity(Gravity.CENTER);
        line.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        line.setGravity(Gravity.CENTER);

        LinearLayout layoutH = new LinearLayout(this);
        layoutH.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout layoutV = new LinearLayout(this);
        layoutV.setOrientation(LinearLayout.VERTICAL);
        Button centerButton = new Button(this);

        centerButton.setText("عرض فواتير الشراء");
        centerButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        centerButton.setTextColor(Color.WHITE);
        centerButton.setPadding(12, 12, 12, 12);
        centerButton.setTag(acc);
        centerButton.setOnClickListener(this);

        LinearLayout layout = (LinearLayout) findViewById(R.id.contant);

        layout.addView(layoutV);
        layoutV.addView(textView);
        layoutV.addView(line);
        layoutV.addView(layoutH);


        layoutH.addView(imageskin);
        layoutH.addView(textView1);
        layoutH.addView(imagedance);
        layoutH.addView(textView2);
        layoutH.addView(imageaxe);
        layoutH.addView(textView3);
        layoutV.addView(centerButton);


        imageskin.getLayoutParams().height=50;
        imageskin.getLayoutParams().width=50;
        imagedance.getLayoutParams().height=50;
        imagedance.getLayoutParams().width=50;
        imageaxe.getLayoutParams().height=50;
        imageaxe.getLayoutParams().width=50;



        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                5
        ));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));

        layout.addView(v);
      acc++;
    }


    @Override
    public void onClick(View v) {

        SharedPreferences.Editor editor = prf.edit();
        editor.putString("acc",v.getTag().toString());
        editor.commit();

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(myaccount.this,infoaccount.class);
        Intent infoacc_intent = new Intent(getApplicationContext(), infoaccount.class);
        startActivity( infoacc_intent );

    }
}

