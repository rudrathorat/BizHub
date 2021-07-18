package com.bd.bizhub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bizhub.model.Member;
import com.bd.bizhub.model.MemberAdapter;
import com.bd.bizhub.model.Task;
import com.bd.bizhub.model.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;
import io.realm.mongodb.sync.SyncConfiguration;
import kotlin.collections.CollectionsKt;

public class MemberActivity extends AppCompatActivity {

    User user;
    RecyclerView recyclerView;
    MemberAdapter adapter;
    FloatingActionButton fab;
    private ArrayList members;
    String projectName;
    App app;

    @Override
    public void onStart() {
        super.onStart();
        app = ((RealmDb) this.getApplication()).getApp();
        user = app.currentUser();

        projectName = getIntent().getStringExtra("PROJECT NAME");

        setUpRecyclerView();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        getSupportActionBar().setTitle("Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.project_users_list);
        fab = findViewById(R.id.floating_action_button);


        fab.setOnClickListener(v -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);


            View viewInflated = getLayoutInflater().inflate(R.layout.alert_add_member, null);

            TextInputLayout input = viewInflated.findViewById(R.id.input_email);
            EditText inputET = input.getEditText();

            builder.setView(viewInflated);

            builder.setTitle("Enter email:");

            builder.setCancelable(true);
            // add a button
            builder.setPositiveButton("Add User", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Remove Member
                    App app = new App(new AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                            .build());

                    User user = app.currentUser();
                    assert user != null;
                    Functions functionsManager = app.getFunctions(user);


                    if (inputET.length() == 0 || !isValidEmail(inputET.getText().toString())) {
                        showSnackBar("Invalid email entered");
                        inputET.requestFocus();
                    }else {
                        List<String> args = Arrays.asList(inputET.getText().toString());
                        functionsManager.callFunctionAsync("addTeamMember", args, Document.class, result -> {
                            builder.create().dismiss();
                            if (result.isSuccess()) {
                                Log.v("Add Success", "Attempted to add team member:  " + result.get());
                                setUpRecyclerView();
                            } else {
                                Toast.makeText(v.getContext(), result.getError().toString(), Toast.LENGTH_LONG).show();
                                Log.e("Add Fail", "failed to call add function with: " + result.getError());
                            }
                        });

                    }


                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.cancel();
            });
            builder.show();

        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private final void setUpRecyclerView() {

        Functions functionsManager = app.getFunctions(user);

        ArrayList<String> arr = new ArrayList<>();
        functionsManager.callFunctionAsync("getMyTeamMembers", arr, ArrayList.class, result -> {

            if (result.isSuccess()) {
                Log.v("Fetch Success", "successfully fetched team members. Number of team members:  " + result.get().stream().iterator());



               // ArrayList document = result.get().stream().map((item,name) -> new Member(item,name));
               // Log.v("result.get()", "Membs" + ArrayList);



                Collection destination = (Collection)(new ArrayList());
                Iterator itr = result.get().stream().iterator();

                while(itr.hasNext()){
                    Object element = itr.next();

                    Log.d("(Object)", "element: " + element);
                    Document doc = (Document) element;
                    Log.d("(Document)", "doc " + doc.get("_id","Err"));
                    //Member var15 = qwe;
                    Member temp_memb = new Member(doc);
                    destination.add(temp_memb);
                }

                List var14 = (List)destination;
                Collection var16 = (Collection)var14;

                members = new ArrayList(var16);
                Log.d("TAG", "setUpRecyclerView: " + members.toString());

                adapter = new MemberAdapter(null,true, members, user);

                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                recyclerView.setAdapter(adapter);

                recyclerView.setHasFixedSize(true);

                adapter.notifyDataSetChanged();

            } else {

                Log.e("Fetch Fail", "failed to call get function with: " + result.getError());
            }
        });

    }

    private void showSnackBar(String msg) {
        try {
            Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}


