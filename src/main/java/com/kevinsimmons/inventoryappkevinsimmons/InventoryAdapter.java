package com.kevinsimmons.inventoryappkevinsimmons;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ItemViewHolder> {

    private List<InventoryItem> itemList;
    private InventoryActivity activity;

    public InventoryAdapter(List<InventoryItem> itemList, InventoryActivity activity) {
        this.itemList = itemList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        InventoryItem item = itemList.get(position);

        holder.itemName.setText(item.getName());
        holder.itemQty.setText(String.valueOf(item.getQuantity()));

        // UPDATE: increase quantity
        holder.buttonIncrease.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            activity.updateItemQuantity(item, newQty);
        });

        // UPDATE: decrease quantity
        holder.buttonDecrease.setOnClickListener(v -> {
            int newQty = item.getQuantity() - 1;
            if (newQty < 0) newQty = 0;
            activity.updateItemQuantity(item, newQty);
        });

        // DELETE: remove item
        holder.buttonDelete.setOnClickListener(v -> activity.deleteItem(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQty;
        View buttonIncrease, buttonDecrease, buttonDelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textItemName);
            itemQty = itemView.findViewById(R.id.textItemQty);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}