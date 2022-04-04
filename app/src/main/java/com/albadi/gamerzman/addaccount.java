package com.albadi.gamerzman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addaccount extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    SharedPreferences prf;
    Intent intent;
    TextView mass;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaccount);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(addaccount.this,myaccount.class);
 mass = (TextView)findViewById(R.id.textView11);
mass.setVisibility(View.GONE);


        Button add = (Button)findViewById(R.id.button2);
        Button cancle = (Button)findViewById(R.id.button3);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addd();

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                canclee();

            }
        });

    }


    public void addd()
    {



        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                EditText name =(EditText)findViewById(R.id.editText);
                EditText skin =(EditText)findViewById(R.id.editText3);
                EditText dance =(EditText)findViewById(R.id.editText4);
                EditText axe =(EditText)findViewById(R.id.editText5);

                Integer acc =dataSnapshot.child("users").child(prf.getString("uid", null)).child("account").getValue(Integer.class);
                Integer max =dataSnapshot.child("users").child(prf.getString("uid", null)).child("maxAcc").getValue(Integer.class);
                if(acc<max)
                {
                   name.getText().toString();
                    if(name.getText().toString().matches(""))
                    {
                        mass.setText("ادخل اسم الحساب");
                        mass.setVisibility(View.VISIBLE);
                    }else{

                        mDatabase.child("users").child(prf.getString("uid", null)).child("account").setValue(acc+1);
                        acc++;
                        mDatabase.child("account").child(prf.getString("uid", null)).child(acc.toString()).child("name").setValue(name.getText().toString());
                        mDatabase.child("account").child(prf.getString("uid", null)).child(acc.toString()).child("skin").setValue(skin.getText().toString());
                        mDatabase.child("account").child(prf.getString("uid", null)).child(acc.toString()).child("dance").setValue(dance.getText().toString());
                        mDatabase.child("account").child(prf.getString("uid", null)).child(acc.toString()).child("axe").setValue(axe.getText().toString());
                        Intent myaccount_intent = new Intent(getApplicationContext(), myaccount.class);
                        startActivity( myaccount_intent );
                    }



                }
                else
                {
                    mass.setText("وصلت الحد الاقصى من الحسابات المسجلة");
                    mass.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void canclee()
    {
        Intent myaccount_intent = new Intent(getApplicationContext(), myaccount.class);
        startActivity( myaccount_intent );
    }
}
