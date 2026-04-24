package com.example.hw_1;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button openDialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDialogButton = findViewById(R.id.openDialogButton);

        openDialogButton.setOnClickListener(v -> showCustomDialog());
    }

    private void showCustomDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        Button okButton = dialog.findViewById(R.id.okButton);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        okButton.setOnClickListener(v -> {
            Toast.makeText(this, "Натиснуто OK", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}