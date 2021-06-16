package com.example.bankingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankingapp.Models.User;
import com.example.bankingapp.R;

import java.util.List;



public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private ClickHandler clickHandler;
    private List<User> userList;

    public interface ClickHandler {
        void onClick(int position);
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void setClickHandler(UserListAdapter.ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_user,parent, false);
        return new UserViewHolder(view, clickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.balance.setText(String.valueOf(user.getCurrent_balance()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView email;
        private final TextView balance;
        public UserViewHolder(@NonNull View itemView, ClickHandler clickHandler) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            balance = itemView.findViewById(R.id.balance);

            itemView.setOnClickListener(view -> {
                if(clickHandler != null) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        clickHandler.onClick(pos);
                    }
                }
            });

        }
    }
}
