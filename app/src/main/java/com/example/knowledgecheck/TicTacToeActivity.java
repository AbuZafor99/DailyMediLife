package com.example.knowledgecheck;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TicTacToeActivity extends AppCompatActivity implements View.OnClickListener {

    // Game board buttons
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private Button btnResetGame, btnResetAll;

    // Game state variables
    private boolean isPlayerXTurn = true;
    private int roundCount = 0;
    private int playerXWins = 0;
    private int playerOWins = 0;

    // UI elements
    private TextView tvStatus, tvPlayerX, tvPlayerO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticktacktoe);

        initViews();
        setClickListeners();
    }

    private void initViews() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        btnResetGame = findViewById(R.id.btnResetGame);
        btnResetAll = findViewById(R.id.btnResetAll);

        tvStatus = findViewById(R.id.tvStatus);
        tvPlayerX = findViewById(R.id.tvPlayerX);
        tvPlayerO = findViewById(R.id.tvPlayerO);

        updateStatusText();
        updateScoreDisplay();
    }

    private void setClickListeners() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        btnResetGame.setOnClickListener(this);
        btnResetAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnResetGame) {
            resetGame(false);
        } else if (view.getId() == R.id.btnResetAll) {
            resetGame(true);
        } else {
            Button btnClicked = (Button) view;
            if (!btnClicked.getText().toString().isEmpty()) return;

            if (isPlayerXTurn) {
                btnClicked.setText("X");
                btnClicked.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                btnClicked.setText("O");
                btnClicked.setTextColor(Color.parseColor("#FFFFFF"));
            }

            roundCount++;

            if (checkForWin()) {
                handleWin();
            } else if (roundCount == 9) {
                handleDraw();
            } else {
                isPlayerXTurn = !isPlayerXTurn;
                updateStatusText();
            }
        }
    }

    private boolean checkForWin() {
        String[][] board = {
                {btn1.getText().toString(), btn2.getText().toString(), btn3.getText().toString()},
                {btn4.getText().toString(), btn5.getText().toString(), btn6.getText().toString()},
                {btn7.getText().toString(), btn8.getText().toString(), btn9.getText().toString()}
        };

        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].isEmpty()) return true;
            if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].isEmpty()) return true;
        }

        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].isEmpty()) return true;
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].isEmpty()) return true;

        return false;
    }

    private void handleWin() {
        if (isPlayerXTurn) {
            playerXWins++;
            Toast.makeText(this, "Player X wins!", Toast.LENGTH_SHORT).show();
        } else {
            playerOWins++;
            Toast.makeText(this, "Player O wins!", Toast.LENGTH_SHORT).show();
        }
        updateScoreDisplay();
        disableButtons();
    }

    private void handleDraw() {
        Toast.makeText(this, "Game Draw!", Toast.LENGTH_SHORT).show();
        disableButtons();
    }

    private void disableButtons() {
        btn1.setEnabled(false); btn2.setEnabled(false); btn3.setEnabled(false);
        btn4.setEnabled(false); btn5.setEnabled(false); btn6.setEnabled(false);
        btn7.setEnabled(false); btn8.setEnabled(false); btn9.setEnabled(false);
    }

    private void enableButtons() {
        btn1.setEnabled(true); btn2.setEnabled(true); btn3.setEnabled(true);
        btn4.setEnabled(true); btn5.setEnabled(true); btn6.setEnabled(true);
        btn7.setEnabled(true); btn8.setEnabled(true); btn9.setEnabled(true);
    }

    private void resetGame(boolean resetScores) {
        btn1.setText(""); btn2.setText(""); btn3.setText("");
        btn4.setText(""); btn5.setText(""); btn6.setText("");
        btn7.setText(""); btn8.setText(""); btn9.setText("");

        enableButtons();
        roundCount = 0;
        isPlayerXTurn = true;
        updateStatusText();

        if (resetScores) {
            playerXWins = 0;
            playerOWins = 0;
            updateScoreDisplay();
        }
    }

    private void updateStatusText() {
        tvStatus.setText(isPlayerXTurn ? "Player X's Turn" : "Player O's Turn");
    }

    private void updateScoreDisplay() {
        tvPlayerX.setText("Player X: " + playerXWins);
        tvPlayerO.setText("Player O: " + playerOWins);
    }
}
