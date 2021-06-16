package com.example.bankingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankingapp.Models.Transfer;
import com.example.bankingapp.Models.User;
import com.example.bankingapp.R;

import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {
    private List<Transfer> transactions;
    private final User currentUser;

    public TransactionListAdapter(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setList(List<Transfer> transactions) {
        this.transactions = transactions;
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionListAdapter.TransactionViewHolder holder, int position) {
        Transfer transfer = transactions.get(position);
        holder.date.setText(transfer.getDate());
        if(currentUser.getId() == transfer.getTo_account()) {
            /* Debit Balance */
            holder.account.setText(String.valueOf(transfer.getFrom_account()));
            holder.debit.setText(String.valueOf(transfer.getAmount()));
            holder.credit.setText("-");
        } else {
            /* Credit Balance */
            holder.account.setText(String.valueOf(transfer.getTo_account()));
            holder.credit.setText(String.valueOf(transfer.getAmount()));
            holder.debit.setText("-");
        }

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        private final TextView date;
        private final TextView account;
        private final TextView debit;
        private final TextView credit;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_of_transfer);
            account = itemView.findViewById(R.id.concerned_acc);
            debit = itemView.findViewById(R.id.debit);
            credit = itemView.findViewById(R.id.credit);
        }
    }
}
