package com.bd.bizhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.bson.Document;

import java.util.concurrent.atomic.AtomicReference;


import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register";
    Button mButtonSignUp;
    Realm realm;
    App app;
    Boolean reg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                "Uname", Context.MODE_PRIVATE);

        realm = Realm.getDefaultInstance();
        Log.e("EXAMPLE", "Successfully opened a realm at: " + realm.getPath());


        TextInputLayout emailTV = findViewById(R.id.Email);
        TextInputLayout passwordTV = findViewById(R.id.Password);
        TextInputLayout Cfrm_passwordTV = findViewById(R.id.Password_confirm);

        mButtonSignUp = findViewById(R.id.button_ereg);



        mButtonSignUp.setOnClickListener(v -> {

            EditText emailET = emailTV.getEditText();
            EditText passwordET = passwordTV.getEditText();
            EditText Cfrm_passwordET = Cfrm_passwordTV.getEditText();

            String password = passwordET.getText().toString();
            String cpswd =  Cfrm_passwordET.getText().toString();

            if (emailET.length() ==0 || !isValidEmail(emailET.getText())) {
                showSnackBar("Enter a valid email");
                emailET.requestFocus();
            }else if (passwordET.length() ==0) {
                showSnackBar("Enter a valid password");
                passwordET.requestFocus();
            }else if (Cfrm_passwordET.length() == 0) {
                showSnackBar("Re enter your password");
                Cfrm_passwordET.requestFocus();
            }else if(!password.equals(cpswd)){
                showSnackBar("Passwords do not match");
            }
            else {

                try{

                    app = ((RealmDb) this.getApplication()).getApp();

                  //  Credentials emailPasswordCredentials = Credentials.emailPassword(emailET.getText().toString(), passwordET.getText().toString());

                    AtomicReference<User> user = new AtomicReference<>();
                    app.getEmailPassword().registerUserAsync(emailET.getText().toString(), passwordET.getText().toString(), it -> { //loginAsync
                        if (it.isSuccess()) {
                            Log.v("AUTH", "Successfully created User");
                            showSnackBar("Successfully created user");
                            user.set(app.currentUser());

                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            i.putExtra("Registering",true);
                            i.putExtra("email",emailET.getText().toString());
                            i.putExtra("pass", passwordET.getText().toString());

                            startActivity(i);
                            RegisterActivity.this.finish();
                        } else {
                            Log.e("AUTH", it.getError().toString());
                        }
                    });

                } catch (RealmPrimaryKeyConstraintException e){
                    e.printStackTrace();
                    showSnackBar("User found on db.");
                }


            }
        });

      //  realm.close();

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void showSnackBar(String msg) {
        try {
            Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }



}