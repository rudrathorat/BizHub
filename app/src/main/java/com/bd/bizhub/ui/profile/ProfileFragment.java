package com.bd.bizhub.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bd.bizhub.LoginActivity;
import com.bd.bizhub.NavigationActivity;
import com.bd.bizhub.OnboardingActivity;
import com.bd.bizhub.R;
import com.bd.bizhub.RealmDb;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.mongodb.App;
import io.realm.mongodb.User;

public class ProfileFragment extends Fragment {
    View root;
    boolean set = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        App app = ((RealmDb) getActivity().getApplication()).getApp();
        User user = app.currentUser();
        root = inflater.inflate(R.layout.fragment_profile, container, false);


        TextView name1 = root.findViewById(R.id.fullname_field);
        TextView uname = root.findViewById(R.id.username_field);
        TextInputLayout user_id = root.findViewById(R.id.user_id);
        TextInputLayout age = root.findViewById(R.id.age);
        TextInputLayout gender = root.findViewById(R.id.gender);
        TextInputLayout email = root.findViewById(R.id.email);
        TextInputLayout phone = root.findViewById(R.id.phone);
        Button update = root.findViewById(R.id.update);

        ImageView profile_pic = root.findViewById(R.id.profile_image);

        FloatingActionButton reset = root.findViewById(R.id.floatingActionButtonedit);
        FloatingActionButton logout = root.findViewById(R.id.floatingActionButton);


        if (user.getProviderType().toString().equals("GOOGLE")){
            name1.setText(user.getProfile().getFirstName());
            uname.setText(user.getProfile().getLastName());
            gender.getEditText().setText(user.getProfile().getGender());

        }

        //Log.d("Img",user.getProfile().getPictureUrl());
        //profile_pic.setImageBitmap(getBitmapFromURL(user.getProfile().getPictureUrl().toString()));

        user_id.getEditText().setText(user.getId());
        email.getEditText().setText(user.getCustomData().get("name").toString());


        update.setOnClickListener(v -> {
            if(!set){
                update.setText("Save");
                set = true;
            }else {
                update.setText("Edit");
                set = false;
            }
            user_id.setEnabled(set);
            gender.setEnabled(set);
            age.setEnabled(set);
            email.setEnabled(set);
            phone.setEnabled(set);
        });


        reset.setOnClickListener(v->{

            if (user.getProviderType().toString().equals("GOOGLE")){
                Toast.makeText(getContext(),"Login provider google, no password to reset",Toast.LENGTH_SHORT).show();
            }else{

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View viewInflated = getLayoutInflater().inflate(R.layout.alert_reset_password, null);

                TextInputLayout input = viewInflated.findViewById(R.id.input_pass);
                EditText inputET = input.getEditText();


                builder.setView(viewInflated);

                builder.setTitle("Enter new Password:");

                builder.setCancelable(true);
                // add a button
                builder.setPositiveButton("Reset Password", (dialog, which) -> {
                    // Change Password


                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try  {
                                app.getEmailPassword().callResetPasswordFunction(user.getCustomData().get("name").toString(),inputET.getText().toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Toast.makeText(getContext(),"Password reset to: " +"abcd11111",Toast.LENGTH_LONG).show();
                    thread.start();

                    dialog.dismiss();
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                });
                builder.show();
            }
        });

        logout.setOnClickListener(v->{


            user.logOutAsync( result -> {
                if (result.isSuccess()) {
                    Log.v("AUTH", "Successfully logged out.: ID:"+ user.getId());
                    startActivity(new Intent(getContext(), OnboardingActivity.class));
                    getActivity().finish();

                } else {
                    Log.e("AUTH", "Log out failed! Error: " + result.getError().toString());
                }
            });
        });




        return root;
    }

    public static Bitmap getBitmapFromURL(String src) {
        //Converting profile url to bitmap
        try {
            //Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
