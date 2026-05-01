package com.example.hw1;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView ratesText;
    private ProgressBar progressBar;

    private static final String URL =
            "https://api.frankfurter.dev/v2/rates?base=UAH&quotes=USD,EUR,GBP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratesText = findViewById(R.id.ratesText);
        progressBar = findViewById(R.id.progressBar);

        loadRates();
    }

    private void loadRates() {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        double usd = 0, eur = 0, gbp = 0;

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            String quote = obj.getString("quote");
                            double rate = obj.getDouble("rate");

                            switch (quote) {
                                case "USD": usd = rate; break;
                                case "EUR": eur = rate; break;
                                case "GBP": gbp = rate; break;
                            }
                        }

                        String result =
                                "1 UAH = " + usd + " USD\n\n" +
                                        "1 UAH = " + eur + " EUR\n\n" +
                                        "1 UAH = " + gbp + " GBP";

                        ratesText.setText(result);

                    } catch (Exception e) {
                        ratesText.setText("Помилка парсингу");
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    ratesText.setText("Помилка запиту");
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Android Volley App");
                return headers;
            }
        };

        queue.add(request);
    }
}