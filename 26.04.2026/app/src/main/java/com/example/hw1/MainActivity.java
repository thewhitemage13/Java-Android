package com.example.hw1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.example.hw1.BuildConfig;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_KEY = BuildConfig.TIKTOK_CLIENT_KEY;
    private static final String CLIENT_SECRET = BuildConfig.TIKTOK_CLIENT_SECRET;
    private static final String REDIRECT_URI = "myapp://tiktok-auth";

    private TextView resultText;
    private final OkHttpClient client = new OkHttpClient();
    private String csrfState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.tiktokLoginButton);
        resultText = findViewById(R.id.resultText);

        loginButton.setOnClickListener(v -> openTikTokAuth());

        handleRedirect(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleRedirect(intent);
    }

    private void openTikTokAuth() {
        csrfState = UUID.randomUUID().toString();

        Uri uri = Uri.parse("https://www.tiktok.com/v2/auth/authorize/")
                .buildUpon()
                .appendQueryParameter("client_key", CLIENT_KEY)
                .appendQueryParameter("scope", "user.info.basic")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("redirect_uri", REDIRECT_URI)
                .appendQueryParameter("state", csrfState)
                .build();

        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void handleRedirect(Intent intent) {
        Uri data = intent.getData();

        if (data == null) return;

        String code = data.getQueryParameter("code");
        String state = data.getQueryParameter("state");
        String error = data.getQueryParameter("error");

        if (error != null) {
            resultText.setText("Помилка авторизації: " + error);
            return;
        }

        if (code != null) {
            exchangeCodeForToken(code);
        }
    }

    private void exchangeCodeForToken(String code) {
        RequestBody body = new FormBody.Builder()
                .add("client_key", CLIENT_KEY)
                .add("client_secret", CLIENT_SECRET)
                .add("code", code)
                .add("grant_type", "authorization_code")
                .add("redirect_uri", REDIRECT_URI)
                .build();

        Request request = new Request.Builder()
                .url("https://open.tiktokapis.com/v2/oauth/token/")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> resultText.setText("Token error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (responseBody == null) return;

                try {
                    JSONObject json = new JSONObject(responseBody.string());
                    String accessToken = json.getString("access_token");
                    getUserInfo(accessToken);
                } catch (Exception e) {
                    runOnUiThread(() -> resultText.setText("JSON error: " + e.getMessage()));
                }
            }
        });
    }

    private void getUserInfo(String accessToken) {
        Request request = new Request.Builder()
                .url("https://open.tiktokapis.com/v2/user/info/?fields=open_id,union_id,avatar_url,display_name")
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> resultText.setText("User info error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (responseBody == null) return;

                String result = responseBody.string();

                runOnUiThread(() -> resultText.setText("TikTok API response:\n" + result));
            }
        });
    }
}