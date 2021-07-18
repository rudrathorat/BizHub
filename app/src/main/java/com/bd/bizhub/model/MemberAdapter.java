package com.bd.bizhub.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bd.bizhub.BuildConfig;
import com.bd.bizhub.R;
import com.bd.bizhub.RealmDb;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;
import io.realm.mongodb.sync.SyncConfiguration;

public class MemberAdapter extends RealmRecyclerViewAdapter<Member, MemberAdapter.MemberViewHolder> {
    @NotNull
    private ArrayList<Member> data;
    private io.realm.mongodb.User user;
    ViewGroup parent;


    public MemberAdapter(@androidx.annotation.Nullable @Nullable OrderedRealmCollection<Member> data, boolean autoUpdate,@NotNull ArrayList<Member> data1, @NotNull User user) {
        super(data, autoUpdate);
        this.user = user;
        this.data = data1;
    }


    @NotNull
    public MemberViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        this.parent = parent;
        Log.i("MemberAdapter", "Displaying a list of project members. Size: " + data.size());
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_view, parent, false);

        return new MemberAdapter.MemberViewHolder(itemView);
    }


    public void onBindViewHolder(@NotNull final MemberAdapter.MemberViewHolder holder, int position) {
        Member obj = data.get(position);
        holder.setData(obj);
        holder.getName().setText(obj.getName());

        holder.getDelete().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(parent.getContext());
                dialogBuilder.setMessage("Are you sure you want to remove this user from the project?");
                dialogBuilder.setCancelable(true);
                dialogBuilder.setTitle("Remove Team Member?");
                dialogBuilder.setPositiveButton("Remove User", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Remove Member
                                App app  = new App(new AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                                        .build());

                                User user = app.currentUser();
                                assert user != null;
                                Functions functionsManager = app.getFunctions(user);
                                List<String> args = Arrays.asList(obj.getName());
                                functionsManager.callFunctionAsync("removeTeamMember", args, Document.class, result -> {
                                    dialogBuilder.create().dismiss();
                                    if (result.isSuccess()) {
                                        Log.v("Remove Success", "Removed team member:: " + result.get());
                                        data.remove(position);
                                        notifyItemRemoved(position);
                                    } else {
                                        Toast.makeText(parent.getContext(), result.getError().toString(), Toast.LENGTH_LONG).show();
                                        Log.e("Remove Fail", "failed to call remove function with: " + result.getError());
                                    }
                                });



                            }
                        });

                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // cancel button
                                dialog.cancel();
                            }
                        });

                dialogBuilder.create();
                dialogBuilder.show();





            }
        });

    }

    @NotNull
    public final User getUser() {
        return this.user;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public final class MemberViewHolder extends RecyclerView.ViewHolder {
        @NotNull
        private TextView name;
        @Nullable
        private Member data;
        @NotNull
        private TextView delete;

        public MemberViewHolder(@NotNull View view) {
            super(view);
            this.name = view.findViewById(R.id.name);
            this.delete = view.findViewById(R.id.delete);
        }


        @NotNull
        public final TextView getName() {
            return this.name;
        }

        public final void setName(@NotNull TextView var1) {
            this.name = var1;
        }

        @Nullable
        public final Member getData() {
            return this.data;
        }

        public final void setData(@Nullable Member var1) {
            this.data = var1;
        }

        @NotNull
        public final TextView getDelete() {
            return this.delete;
        }

        public final void setDelete(@NotNull TextView var1) {
            this.delete = var1;
        }

    }
}
