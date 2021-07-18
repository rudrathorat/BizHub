package com.bd.bizhub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.bson.Document;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;
import io.realm.mongodb.functions.Functions;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Home";
    TextView Create;
    private Realm realm;
    boolean reg;
    App app;
    Button google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        realm = Realm.getDefaultInstance();
        Log.e("EXAMPLE", "Successfully opened a realm at: " + realm.getPath());
        app = ((RealmDb) LoginActivity.this.getApplication()).getApp();




        reg = getIntent().getBooleanExtra("Registering",false);
        if (reg == true){


            setContentView(R.layout.loading);
            Credentials emailPasswordCredentials = Credentials.emailPassword(getIntent().getStringExtra("email"),getIntent().getStringExtra("pass"));

            AtomicReference<User> user = new AtomicReference<>();
            app.loginAsync(emailPasswordCredentials, it -> {
                if (it.isSuccess()) {
                    Log.v("AUTH", "Successfully authenticated using an email and password.");
                    //showSnackBar("Login Success");
                    user.set(app.currentUser());
                    Intent i = new Intent(LoginActivity.this, NavigationActivity.class); //----------+++++++++++++
                    startActivity(i);

                }
                else {
                    Log.e("AUTH", it.getError().toString());
                    //showSnackBar("Login Failed: "+it.getError().toString());
                }
            });

            return;

        }


        TextInputLayout emailTV = findViewById(R.id.Email);
        TextInputLayout passwordTV = findViewById(R.id.Password);

        Button mButtonLogin = findViewById(R.id.button_elogin);
        google = findViewById(R.id.button_google);

        Create = findViewById(R.id.CreateAcc);



        Create.setOnClickListener(v -> {

            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
            LoginActivity.this.finish();

        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText emailET = emailTV.getEditText();
                EditText passwordET = passwordTV.getEditText();

               // assert emailET != null;
                if (emailET.length() == 0 && !isValidEmail(emailET.getText().toString())) {
                    showSnackBar("Enter EMAIL");
                    emailET.requestFocus();
                } else  if (passwordET.length() == 0) {
                        showSnackBar("Enter password");
                        passwordET.requestFocus();

                } else {
                    //    App app = ((RealmDb) LoginActivity.this.getApplication()).getApp();

                    Credentials emailPasswordCredentials = Credentials.emailPassword(emailET.getText().toString(),passwordET.getText().toString());

                    AtomicReference<User> user = new AtomicReference<>();
                    app.loginAsync(emailPasswordCredentials, it -> {
                        if (it.isSuccess()) {
                            Log.v("AUTH", "Successfully authenticated using an email and password.");
                            showSnackBar("Login Success");
                            user.set(app.currentUser());
                            Intent i = new Intent(LoginActivity.this, NavigationActivity.class); //++++++++++++____________
                            startActivity(i);

                        }
                        else {
                            Log.e("AUTH", it.getError().toString());
                            showSnackBar("Login Failed: "+it.getError().toString());
                        }
                    });

                }

            }

            private void showSnackBar(String msg) {
                try {
                    Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }


        });
      //  realm.close();


        google.setOnClickListener(v->{
            signInWithGoogle();
        });

    }


    private void signInWithGoogle() {
        // Replace with your api key
        String serverClientId = "406586912954-jfvkejlogdhv9cq7lb08h8vasi1cb175.apps.googleusercontent.com";
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestServerAuthCode(serverClientId)
                //.requestIdToken(serverClientId)
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 123);

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

            if (requestCode == 123) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }


    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("a",account.toString());
            String authorizationCode = account.getServerAuthCode();
            Log.d("b",authorizationCode + "");

            AtomicReference<User> user = new AtomicReference<>();
            Credentials googleCredentials = Credentials.google(authorizationCode, GoogleAuthType.AUTH_CODE);
            app.loginAsync(googleCredentials, it -> {

                if (it.isSuccess()) {
                    Log.v("AUTH", "Successfully logged in to MongoDB Realm using Google OAuth.");
                    user.set(app.currentUser());
                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);  //++++______________
                    startActivity(intent);


                } else {
                    google.callOnClick();
                    Log.e("AUTH", "Failed to log in to MongoDB Realm", it.getError());
                }



            });
        } catch (ApiException e) {
            Log.w("AUTH", "Failed to log in with Google OAuth: " + e.getMessage());
        }
    }

}
