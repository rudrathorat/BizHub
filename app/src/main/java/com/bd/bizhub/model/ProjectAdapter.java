package com.bd.bizhub.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bd.bizhub.LoginActivity;
import com.bd.bizhub.MemberActivity;
import com.bd.bizhub.OnboardingActivity;
import com.bd.bizhub.R;
import com.bd.bizhub.TaskActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.mongodb.User;



public class ProjectAdapter extends RealmRecyclerViewAdapter<Project, ProjectAdapter.ProjectViewHolder> {

    private RealmList<Project> project;
    private User user;
    private Context mContext;


    public ProjectAdapter(@androidx.annotation.Nullable @Nullable OrderedRealmCollection<Project> data,
                          boolean autoUpdate,
                          RealmList<Project> project,  User user, Context context) {
        super(data, autoUpdate);
        this.project = project;
        this.user = user;
        this.mContext = context;
    }

    @NonNull
    @NotNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project proj = project.get(position);
        holder.setData(proj);
        holder.getName().setText(proj.getName());
        holder.getDescription().setText(proj.getDescription());
        holder.getStatus().setText(proj.getCreated());

        // ensure that this view is always Invisible when bound, since it is sometimes visible
        holder.getMenu().setVisibility(View.INVISIBLE);


        // if the project described by this view is NOT the user's project, hide the menu button

        if (proj.getPartition().contains("project="+user.getId().toString())) {
            holder.getMenu().setVisibility(View.VISIBLE);
        }

        //Menu On click -> to members
        holder.getMenu().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v("Opening Members", "Opening membership for project: "+proj.getPartition());

                if (proj.getPartition().contains("project="+user.getId().toString())){
                    Intent intent = new Intent(v.getContext(), MemberActivity.class);

                    intent.putExtra("PARTITION", proj.getPartition());
                    intent.putExtra("PROJECT NAME", proj.getName());

                    Bundle bundle = new Bundle();
                    bundle.putString("PARTITION", proj.getPartition());
                    bundle.putString("PROJECT NAME", proj.getName());


                    v.getContext().startActivity(intent);
                }else {
                    Toast.makeText(v.getContext(), "You can only edit the membership of your own project.", Toast.LENGTH_LONG).show();
                }

            }

        });

        holder.itemView.findViewById(R.id.linearLayout2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v("Opening Tasks", "Opening Tasks");
                Intent intent = new Intent(v.getContext(), TaskActivity.class);

                intent.putExtra("PARTITION", proj.getPartition());

                intent.putExtra("PROJECT NAME", proj.getName());
                Log.v("Opening Tasks", "Name: "+ proj.getName() +" Partition: "+proj.getPartition());
                v.getContext().startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return project.size();
    }


    public class ProjectViewHolder extends RecyclerView.ViewHolder {
        @NotNull
        private TextView name;
        @NotNull
        private TextView status;
        @Nullable
        private Project data;
        @Nullable
        private TextView description;

        @NotNull
        private TextView menu;

        @NotNull
        public final TextView getName() {
            return this.name;
        }

        public final void setName(@NotNull TextView var1) {
            this.name = var1;
        }

        @NotNull
        public final TextView getStatus() {
            return this.status;
        }

        public final void setStatus(@NotNull TextView var1) {
            this.status = var1;
        }

        @Nullable
        public final Project getData() {
            return this.data;
        }

        public final void setData(@Nullable Project var1) {
            this.data = var1;
        }

        @Nullable
        public final TextView getDescription() {
            return this.description;
        }

        public final void setDescription(@Nullable TextView var1) {
            this.description = var1;
        }


        @NotNull
        public final TextView getMenu() {
            return this.menu;
        }

        public final void setMenu(@NotNull TextView var1) {
            this.menu = var1;
        }

        public ProjectViewHolder(View view) {
            super(view);

            this.name =  view.findViewById(R.id.item_project_title);

            this.status = view.findViewById(R.id.item_status);

            this.description = view.findViewById(R.id.item_description);

            this.menu = view.findViewById(R.id.menu);

        }
    }

}

