package com.example.hw_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    final int SIZE = 100;
    final int WIN_COUNT = 5;

    Button[][] buttons = new Button[SIZE][SIZE];
    String[][] field = new String[SIZE][SIZE];

    String currentPlayer = "X";
    boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Button button = new Button(this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 60;
                params.height = 60;
                button.setLayoutParams(params);

                button.setMinWidth(0);
                button.setMinHeight(0);
                button.setPadding(0, 0, 0, 0);
                button.setTextSize(10);

                int row = i;
                int col = j;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeMove(row, col);
                    }
                });

                buttons[i][j] = button;
                field[i][j] = "";
                gridLayout.addView(button);
            }
        }
    }

    private void makeMove(int row, int col) {
        if (gameOver || !field[row][col].equals("")) {
            return;
        }

        field[row][col] = currentPlayer;
        buttons[row][col].setText(currentPlayer);

        if (checkWin(row, col)) {
            Toast.makeText(this, "Переміг гравець " + currentPlayer, Toast.LENGTH_LONG).show();
            gameOver = true;
            return;
        }

        if (currentPlayer.equals("X")) {
            currentPlayer = "O";
        } else {
            currentPlayer = "X";
        }
    }

    private boolean checkWin(int row, int col) {
        return checkDirection(row, col, 1, 0) ||
                checkDirection(row, col, 0, 1) ||
                checkDirection(row, col, 1, 1) ||
                checkDirection(row, col, 1, -1);
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol) {
        int count = 1;

        count += countSymbols(row, col, dRow, dCol);
        count += countSymbols(row, col, -dRow, -dCol);

        return count >= WIN_COUNT;
    }

    private int countSymbols(int row, int col, int dRow, int dCol) {
        int count = 0;
        int r = row + dRow;
        int c = col + dCol;

        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE
                && field[r][c].equals(currentPlayer)) {
            count++;
            r += dRow;
            c += dCol;
        }

        return count;
    }
}