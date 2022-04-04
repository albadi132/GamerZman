package com.albadi.gamerzman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity {

    // TAG is for show some tag logs in LOG screen.
    public static final String TAG = "MainActivity";
    //session
    EditText username,uid;
    SharedPreferences pref;
    Intent intent;
    int creatAcc;
    // Request sing in code. Could be anything as you required.
    public static final int RequestSignInCode = 7;

    // Firebase Auth Object.
    public FirebaseAuth firebaseAuth;

    // Google API Client object.
    public GoogleApiClient googleApiClient;

    // Sing out button.
    Button SignOutButton;

    // Google Sign In button .
    com.google.android.gms.common.SignInButton signInButton;

    // TextView to Show Login User Email and Name.
    TextView LoginUserName, LoginUserEmail;
    private GifImageView loding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        creatAcc = 0;
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(MainActivity.this,myaccount.class);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);


        SignOutButton= (Button) findViewById(R.id.sign_out);

        LoginUserName = (TextView) findViewById(R.id.textViewName);

        LoginUserEmail = (TextView) findViewById(R.id.textViewEmail);
         loding = (GifImageView) findViewById(R.id.loding) ;

        signInButton = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);

// Getting Firebase Auth Instance into firebaseAuth object.
        firebaseAuth = FirebaseAuth.getInstance();

// Hiding the TextView on activity start up time.
        LoginUserEmail.setVisibility(View.GONE);
        LoginUserName.setVisibility(View.GONE);
        loding.setVisibility(View.GONE);

// Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

// Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


// Adding Click listener to User Sign in Google button.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSignInMethod();

            }
        });

// Adding Click Listener to User Sign Out button.
        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSignOutFunction();

            }
        });

    }


    // Sign In function Starts From Here.
    public void UserSignInMethod(){
        loding.setVisibility(View.VISIBLE);
        LoginUserName.setVisibility(View.VISIBLE);
        LoginUserName.setText(" جاري الاتصال ");
// Passing Google Api Client into Intent.
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestSignInCode){

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (googleSignInResult.isSuccess()){

                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                FirebaseUserAuth(googleSignInAccount);
            }

        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        Toast.makeText(MainActivity.this,""+ authCredential.getProvider(),Toast.LENGTH_LONG).show();

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> AuthResultTask) {

                        if (AuthResultTask.isSuccessful()){
                            LoginUserName.setText(" جاري الاتصال ");

// Getting Current Login user details.
                            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("users")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()) {

                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                   final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            if (dataSnapshot.child("users").hasChild(firebaseUser.getUid())) {
                                                                // run some code

                                                            } else
                                                            {
                                                                LoginUserName.setText(" انشاء حساب جديد ");
                                                                mDatabase.child("users").child(firebaseUser.getUid()).child("name").setValue(firebaseUser.getDisplayName());
                                                                mDatabase.child("users").child(firebaseUser.getUid()).child("Email").setValue(firebaseUser.getEmail());
                                                                mDatabase.child("users").child(firebaseUser.getUid()).child("PhoneNumber").setValue(firebaseUser.getPhoneNumber());
                                                                mDatabase.child("users").child(firebaseUser.getUid()).child("photo").setValue(firebaseUser.getPhotoUrl().toString());
                                                                mDatabase.child("users").child(firebaseUser.getUid()).child("coins").setValue(0);
                                                                mDatabase.child("users").child(firebaseUser.getUid()).child("account").setValue(0);
                                                                mDatabase.child("users").child(firebaseUser.getUid()).child("maxAcc").setValue(1);
                                                                mDatabase.child("users").child(firebaseUser.getUid()).child("role").setValue("user");



                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                    LoginUserName.setText(" تسجيل الدخول ");
                                                        startActivity(intent);
                                                        SharedPreferences.Editor editor = pref.edit();
                                                        editor.putString("username",firebaseUser.getDisplayName().toString());
                                                        editor.putString("uid",firebaseUser.getUid().toString());
                                                        editor.commit();

                                                signInButton.setVisibility(View.GONE);
                                                SignOutButton.setVisibility(View.VISIBLE);
                                                loding.setVisibility(View.GONE);
                                                LoginUserName.setVisibility(View.GONE);
                                                    Intent myaccount_intent = new Intent(getApplicationContext(), myaccount.class);
                                                    startActivity( myaccount_intent );


                                            } else {
                                                Log.w(TAG, "Error getting documents.", task.getException());
                                            }
                                        }
                                    });




// Intent Code
                           // Intent myaccount_intent = new Intent(getApplicationContext(), myaccount.class);
                          //  startActivity( myaccount_intent );
// Showing Log out button.

                            //SignOutButton.setVisibility(View.VISIBLE);

// Hiding Login in button.
                            //signInButton.setVisibility(View.GONE);

// Showing the TextView.
                          //  LoginUserEmail.setVisibility(View.VISIBLE);
                           // LoginUserName.setVisibility(View.VISIBLE);

// Setting up name into TextView.
                           //LoginUserName.setText("NAME = "+ firebaseUser.getDisplayName().toString());

// Setting up Email into TextView.
                           // LoginUserEmail.setText("Email = "+ firebaseUser.getEmail().toString());

                        }else {
                            Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void UserSignOutFunction() {

// Sing Out the User.
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

// Write down your any code here which you want to execute After Sign Out.

// Printing Logout toast message on screen.
                        Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();

                    }
                });

// After logout Hiding sign out button.
        SignOutButton.setVisibility(View.GONE);

// After logout setting up email and name to null.
        LoginUserName.setText(null);
        LoginUserEmail.setText(null);

// After logout setting up login button visibility to visible.
        signInButton.setVisibility(View.VISIBLE);
    }

}