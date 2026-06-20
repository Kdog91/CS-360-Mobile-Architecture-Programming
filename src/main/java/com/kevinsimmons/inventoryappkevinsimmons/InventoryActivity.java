package com.kevinsimmons.inventoryappkevinsimmons;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private static final int SMS_PERMISSION_CODE = 100;
    private static final int LOW_STOCK_THRESHOLD = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        databaseHelper = new DatabaseHelper(this);

        // Set up RecyclerView grid
        recyclerView = findViewById(R.id.recyclerViewInventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadInventory();

        // Ask for SMS permission when the screen loads
        requestSmsPermission();
    }

    // READ: Load all items from the database and display them
    private void loadInventory() {
        List<InventoryItem> items = databaseHelper.getAllItems();
        adapter = new InventoryAdapter(items, this);
        recyclerView.setAdapter(adapter);
    }

    // CREATE: Called when the Add Item button is clicked
    public void addItem(View view) {
        EditText itemNameField = findViewById(R.id.editItemName);
        EditText itemQtyField = findViewById(R.id.editItemQty);

        String name = itemNameField.getText().toString().trim();
        String qtyStr = itemQtyField.getText().toString().trim();

        if (name.isEmpty() || qtyStr.isEmpty()) {
            Toast.makeText(this, "Please enter item name and quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(qtyStr);
        databaseHelper.addItem(name, quantity);

        itemNameField.setText("");
        itemQtyField.setText("");

        loadInventory();
        checkLowStock(name, quantity);
    }

    // UPDATE: Called from the adapter when a user edits an item's quantity
    public void updateItemQuantity(InventoryItem item, int newQuantity) {
        databaseHelper.updateItem(item.getId(), item.getName(), newQuantity);
        loadInventory();
        checkLowStock(item.getName(), newQuantity);
    }

    // DELETE: Called from the adapter when a user deletes an item
    public void deleteItem(InventoryItem item) {
        databaseHelper.deleteItem(item.getId());
        loadInventory();
        Toast.makeText(this, item.getName() + " deleted", Toast.LENGTH_SHORT).show();
    }

    // ---------- SMS PERMISSION HANDLING ----------

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // If denied, the app continues to function without SMS notifications
                Toast.makeText(this, "SMS permission denied. Notifications disabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Check if stock is low and send SMS alert if permission was granted
    private void checkLowStock(String itemName, int quantity) {
        if (quantity < LOW_STOCK_THRESHOLD) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                sendLowStockSms(itemName, quantity);
            }
            // If permission was denied, the app simply skips sending the SMS
            // and continues functioning normally without crashing.
        }
    }

    private void sendLowStockSms(String itemName, int quantity) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String message = "Low stock alert: " + itemName + " has only " + quantity + " left.";
            // Using a placeholder number since this is a demo app running on an emulator
            smsManager.sendTextMessage("5551234567", null, message, null, null);
            Toast.makeText(this, "Low stock SMS sent for " + itemName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}