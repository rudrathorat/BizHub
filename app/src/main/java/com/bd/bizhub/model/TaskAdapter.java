package com.bd.bizhub.model;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bd.bizhub.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

import io.realm.Realm;

public class TaskAdapter extends RealmRecyclerViewAdapter<Task, TaskAdapter.TaskViewHolder> {
    @NotNull
    User user;
    String partition;
    private RealmList<Task> task;

    public TaskAdapter(@NotNull OrderedRealmCollection data, @NotNull User user, @NotNull String partition) {
        super(data, true);
        this.user = user;
        this.partition = partition;
    }


    @NotNull
    public TaskViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);

        return new TaskAdapter.TaskViewHolder(itemView);
    }


    public void onBindViewHolder(@NotNull final TaskAdapter.TaskViewHolder holder, int position) {
        Task obj = getItem(position);
        holder.setData(obj);
        holder.getName().setText(obj.getName());
        holder.getDescription().setText(obj.getDescription());
        holder.getStatus().setText(obj.getStatusEnum().getDisplayName());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(holder.itemView.getContext(), (View)holder.getMenu());
                Menu menu = popup.getMenu();

                if ((holder.getData().getStatusEnum()) != TaskStatus.Open) {
                    menu.add(0, TaskStatus.Open.ordinal(), 0, (CharSequence)TaskStatus.Open.getDisplayName());
                }

                if ((holder.getData().getStatusEnum()) != TaskStatus.InProgress) {
                    menu.add(0, TaskStatus.InProgress.ordinal(), 0, (CharSequence)TaskStatus.InProgress.getDisplayName());
                }

                if ((holder.getData().getStatusEnum()) != TaskStatus.Complete) {
                    menu.add(0, TaskStatus.Complete.ordinal(), 0, (CharSequence)TaskStatus.Complete.getDisplayName());
                }

                int deleteCode = -1;
                menu.add(0, deleteCode, 0, (CharSequence)"Delete Task");


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        TaskStatus status = null;

                        if (item.getItemId() == TaskStatus.Open.ordinal()) {
                            status = TaskStatus.Open;
                        } else if (item.getItemId() == TaskStatus.InProgress.ordinal()) {
                            status = TaskStatus.InProgress;
                        } else if (item.getItemId() == TaskStatus.Complete.ordinal()) {
                            status = TaskStatus.Complete;
                        } else if (item.getItemId() == deleteCode) {
                            removeAt(holder.getData().get_id());
                        }
                        if (status != null) {
                            changeStatus(status, holder.getData().get_id());
                        }
                        return true;
                    }
                });

                popup.show();

            }
        });

    }



    private void changeStatus(TaskStatus status, ObjectId id) {

        SyncConfiguration config = new SyncConfiguration.Builder(user, partition)
                .build();


        Realm realm = Realm.getInstance(config);
        realm.executeTransactionAsync((Realm.Transaction)(new Realm.Transaction() {
            public final void execute(Realm it) {

                Task item = (Task)it.where(Task.class).equalTo("id", id).findFirst();
                if (item != null) {
                    item.setStatusEnum(status);
                }

            }
        }));
        realm.close();
    }

    private final void removeAt(final ObjectId id) {
        SyncConfiguration config = new SyncConfiguration.Builder(user, partition)
                .build();

        Realm var10000 = Realm.getInstance(config);

        Realm realm = var10000;
        realm.executeTransactionAsync((Realm.Transaction)(new Realm.Transaction() {
            public final void execute(Realm it) {

                Task item = it.where(Task.class).equalTo("id", id).findFirst();
                if (item != null) {
                    item.deleteFromRealm();
                }

            }
        }));
        realm.close();
    }

    @NotNull
    public final User getUser() {
        return this.user;
    }






    public final class TaskViewHolder extends RecyclerView.ViewHolder {
        @NotNull
        private TextView name;
        @NotNull
        private TextView description;
        @NotNull
        private TextView status;
        @Nullable
        private Task data;
        @NotNull
        private TextView menu;

        @NotNull
        public final TextView getName() {
            return this.name;
        }

        @NotNull
        public final TextView getDescription() {
            return this.description;
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
        public final Task getData() {
            return this.data;
        }

        public final void setData(@Nullable Task var1) {
            this.data = var1;
        }

        @NotNull
        public final TextView getMenu() {
            return this.menu;
        }

        public final void setMenu(@NotNull TextView var1) {
            this.menu = var1;
        }

        public TaskViewHolder(@NotNull View view) {
            super(view);
            this.name = view.findViewById(R.id.name);
            this.description = view.findViewById(R.id.description);
            this.status = view.findViewById(R.id.status);
            this.menu = view.findViewById(R.id.menu);
        }
    }
}
