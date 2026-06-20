package com.kevinsimmons.inventoryappkevinsimmons;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSayHello = findViewById(R.id.buttonSayHello);
        EditText nameText = findViewById(R.id.nameText);

        buttonSayHello.setEnabled(false);

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    buttonSayHello.setEnabled(false);
                } else {
                    buttonSayHello.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void SayHello(View view) {
        EditText nameText = findViewById(R.id.nameText);
        TextView textGreeting = findViewById(R.id.textGreeting);

        if (nameText.getText() == null) {
            return;
        }

        String name = nameText.getText().toString().trim();

        if (name.isEmpty()) {
            return;
        }

        textGreeting.setText("Hello " + name);
    }
}