package com.albadi.gamerzman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addcoins extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    BillingProcessor bp;
    SharedPreferences prf;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Intent intent;
    int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcoins);
        Button b0 = (Button)findViewById(R.id.b0);
        Button b1 = (Button)findViewById(R.id.b1);
        Button b2 = (Button)findViewById(R.id.b2);
        Button b3 = (Button)findViewById(R.id.b3);
        Button b4 = (Button)findViewById(R.id.b4);
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
        intent = new Intent(addcoins.this, myaccount.class);
        name.setText(prf.getString("username", null));

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                         TextView coin = (TextView) findViewById(R.id.textView4);
                                                         Integer coi =dataSnapshot.child("users").child(prf.getString("uid", null)).child("coins").getValue(Integer.class);
                                                         coin.setText(coi.toString());

                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                     }
                                                 });

                bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy75Z+xaCJ/HFrr42bE6RjiNaYfSHkwZ+Y/ei/a3ibW1DdaiZez9j/p39On5lCQvXVUiDOpj5LAJWQpw9sOFtws6+Rc5n/0s4+A9u9VWyK5UkVmC4SiAumJcUyoSHuGISJ+8KET8D+7j/dy50yeu4XkmlNGwlRBNVshSXyxm8Z2R3oFxaBLm8gQ1xm28OBErirTx2NZbnP4xwqfLoOGbKHSt99iViGKTZPpXW0Woel8NicND9cx+Y9W1QL5WEWvkXT/Hv97xXcQb4mOodfjwWvTVVhdW1JCD7uisS/Izd9hOF14D66OC3JB83bJ4XYrNOkbsq4uXH+c6yZ6O/aaEygQIDAQAB", this);
        bp.initialize();
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coins = 150;

                bp.consumePurchase("coins_150");
                bp.purchase(addcoins.this, "coins_150");

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coins = 300;

                bp.consumePurchase("coins_300");
                bp.purchase(addcoins.this, "coins_300");

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coins = 500;
                bp.consumePurchase("coins_500");
                bp.purchase(addcoins.this, "coins_500");

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coins = 1000;
                bp.consumePurchase("coins_1000");
                bp.purchase(addcoins.this, "coins_1000");

            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coins = 2000;
                bp.consumePurchase("coins_2000");
                bp.purchase(addcoins.this, "coins_2000");

            }
        });
    }

    // IBillingHandler implementation

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                         Integer preves =dataSnapshot.child("users").child(prf.getString("uid", null)).child("coins").getValue(Integer.class);
                                                         mDatabase.child("users").child(prf.getString("uid", null)).child("coins").setValue(preves+coins);

                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                     }
                                                 });


                Context context = getApplicationContext();
        CharSequence text = "تم اضافة العملات الرقمية";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         *
         * Note - this includes handling the case where the user canceled the buy dialog:
         * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
         */
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}