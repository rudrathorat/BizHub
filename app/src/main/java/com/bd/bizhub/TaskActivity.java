package com.bd.bizhub;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bizhub.databinding.ActivityMainBinding;
import com.bd.bizhub.model.ProjectAdapter;
import com.bd.bizhub.model.Task;
import com.bd.bizhub.model.TaskAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.ClientResetRequiredError;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.mongodb.sync.SyncSession;

public class TaskActivity extends AppCompatActivity {
    Realm projectRealm;
    User user;
    RecyclerView recyclerView;
    TaskAdapter adapter;
    FloatingActionButton fab;
    String partition,projectName;

    @Override
    public void onStart() {
        super.onStart();


        App app = ((RealmDb) this.getApplication()).getApp();
        user = app.currentUser();


        partition = getIntent().getStringExtra("PARTITION");
        projectName = getIntent().getStringExtra("PROJECT NAME");


        projectRealm = Realm.getDefaultInstance();

        // display the name of the project in the action bar via the title member variable of the Activity
//        getActionBar().setTitle(projectName);

        SyncConfiguration config = new SyncConfiguration.Builder(user, partition)
                .build();

        // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                projectRealm = realm;
                setUpRecyclerView(realm, user, partition);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_task);
        getSupportActionBar().setTitle("Tasks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.task_list);
        fab = findViewById(R.id.floating_action_button);

        fab.setOnClickListener(v->{


            AlertDialog.Builder builder = new AlertDialog.Builder(this);


            View viewInflated = getLayoutInflater().inflate(R.layout.alert_create_task, null);

            TextInputLayout input = viewInflated.findViewById(R.id.input);
            TextInputLayout input2 = viewInflated.findViewById(R.id.input2);
            EditText inputET = input.getEditText();
            EditText input2ET = input2.getEditText();


            builder.setView(viewInflated);

            builder.setTitle("Enter task name:");

            builder.setCancelable(true);
            // add a button
            builder.setPositiveButton("Create", (dialog, which) -> {

                // Validate and create task
                if (inputET.length() == 0) {
                    showSnackBar("Enter Task Name");
                    inputET.requestFocus();
                } else if (input2ET.length() == 0) {
                    showSnackBar("Enter the task description");
                    input2ET.requestFocus();
                }else {
                    Task task = new Task(inputET.getText().toString(), input2ET.getText().toString());
                    Log.d("Input Text","Input Task Name -------- "+inputET.getText().toString());

                    projectRealm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            //Insert task into realm
                            realm.insert(task);
                        }
                    });

                    dialog.dismiss();
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

    private final void setUpRecyclerView(Realm realm, User user, String partition) {
        recyclerView = findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("Proj_list",""+realm.where(Task.class).sort("id").findAll().toString());
        adapter = new TaskAdapter(realm.where(Task.class).sort("id").findAll(), user, partition);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

    }
    private void showSnackBar(String msg) {
        try {
            Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //projectRealm.close();
    }



}