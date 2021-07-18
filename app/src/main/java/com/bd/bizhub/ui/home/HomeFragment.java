package com.bd.bizhub.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.os.BuildCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bizhub.BuildConfig;
import com.bd.bizhub.LoginActivity;
import com.bd.bizhub.R;
import com.bd.bizhub.RealmDb;
import com.bd.bizhub.databinding.FragmentHomeBinding;
import com.bd.bizhub.model.Project;
import com.bd.bizhub.model.ProjectAdapter;
import com.bd.bizhub.ui.faq.FaqActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    User user;
    Realm userRealm;
    View root;
    App app;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Uname",Context.MODE_PRIVATE);

        String Uname = sharedPref.getString("Uname", "NULL");

         app = ((RealmDb) getActivity().getApplication()).getApp();

        user = app.currentUser();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);


        CardView card_workspace = root.findViewById(R.id.card_workspace);
        CardView card_tasks = root.findViewById(R.id.card_tasks);
        CardView card_faq = root.findViewById(R.id.card_faq);
        CardView card_more = root.findViewById(R.id.card_more);
        TextView name = root.findViewById(R.id.textViewName);
        name.setText(user.getCustomData().get("name","Null"));


        card_workspace.setOnClickListener( v-> {

            BottomNavigationView navView = getActivity().findViewById(R.id.bottom_navigation_view);
            navView.setSelectedItemId(R.id.navigation_dashboard);
            //    navView.getSelectedItemId();
        });

        card_tasks.setOnClickListener( v-> {

            BottomNavigationView navView = getActivity().findViewById(R.id.bottom_navigation_view);
            navView.setSelectedItemId(R.id.navigation_notifications);

        });

        card_faq.setOnClickListener( v-> {

            Intent intent = new Intent(getContext(), FaqActivity.class);
            startActivity(intent);

        });

        card_more.setOnClickListener( v-> {

            DrawerLayout layout = getActivity().findViewById(R.id.drawer_layout);
            layout.openDrawer(GravityCompat.START);

        });


        return root;
    }

        @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of HomeFragment");
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(userRealm == null){
            userRealm = Realm.getDefaultInstance();
        }
    }
}