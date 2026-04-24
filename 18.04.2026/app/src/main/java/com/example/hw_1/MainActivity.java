package com.example.hw_1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText wordInput;
    Button findButton;
    TextView rhymesText;

    ArrayList<String> words = new ArrayList<>();

    final String VOWELS = "аеєиіїоуюя";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordInput = findViewById(R.id.wordInput);
        findButton = findViewById(R.id.findButton);
        rhymesText = findViewById(R.id.rhymesText);

        loadWordsFromFile();

        findButton.setOnClickListener(v -> {
            String word = wordInput.getText().toString().trim().toLowerCase();

            if (word.isEmpty()) {
                rhymesText.setText("Введіть слово");
                return;
            }

            findRhymes(word);
        });
    }

    private void loadWordsFromFile() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.ua_words);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();

                if (!line.isEmpty()) {
                    words.add(line);
                }
            }

            reader.close();

        } catch (Exception e) {
            rhymesText.setText("Помилка читання файлу");
        }
    }

    private void findRhymes(String inputWord) {
        String inputEnding = getRhymeEnding(inputWord);

        StringBuilder result = new StringBuilder();
        int count = 0;

        for (String word : words) {
            if (!word.equals(inputWord) && word.endsWith(inputEnding)) {
                result.append(word).append("\n");
                count++;
            }
        }

        if (count == 0) {
            rhymesText.setText("Рими не знайдено");
        } else {
            rhymesText.setText("Знайдено рим: " + count + "\n\n" + result);
        }
    }

    private String getRhymeEnding(String word) {
        word = word.toLowerCase();

        for (int i = word.length() - 1; i >= 0; i--) {
            char ch = word.charAt(i);

            if (VOWELS.indexOf(ch) != -1) {
                return word.substring(i);
            }
        }

        if (word.length() >= 3) {
            return word.substring(word.length() - 3);
        }

        return word;
    }
}