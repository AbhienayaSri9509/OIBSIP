package com.example.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestionNumber;
    private TextView tvQuestion;
    private Button btnOptionA;
    private Button btnOptionB;
    private Button btnOptionC;
    private Button btnOptionD;
    private Button btnNext;

    private ArrayList<Question> questions;
    private int currentIndex = 0;
    private int score = 0;

    private static final String COLOR_CORRECT = "#4CAF50";
    private static final String COLOR_WRONG = "#F44336";
    private static final String COLOR_DEFAULT = "#FFFFFF";
    private static final String COLOR_DEFAULT_TEXT = "#000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnOptionA = findViewById(R.id.option_a);
        btnOptionB = findViewById(R.id.option_b);
        btnOptionC = findViewById(R.id.option_c);
        btnOptionD = findViewById(R.id.option_d);
        btnNext = findViewById(R.id.btnNext);

        questions = QuestionBank.getQuestions();
        Collections.shuffle(questions);

        loadQuestion();

        btnOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkAnswer(0); }
        });
        btnOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkAnswer(1); }
        });
        btnOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkAnswer(2); }
        });
        btnOptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkAnswer(3); }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if (currentIndex < questions.size()) {
                    loadQuestion();
                } else {
                    // All questions answered - go to result
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("SCORE", score);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void loadQuestion() {
        Question q = questions.get(currentIndex);
        tvQuestionNumber.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        tvQuestion.setText(q.getQuestion());

        String[] options = q.getOptions();
        btnOptionA.setText(options[0]);
        btnOptionB.setText(options[1]);
        btnOptionC.setText(options[2]);
        btnOptionD.setText(options[3]);

        // Reset button backgrounds and states
        resetButtonStyles(btnOptionA);
        resetButtonStyles(btnOptionB);
        resetButtonStyles(btnOptionC);
        resetButtonStyles(btnOptionD);

        // Enable all option buttons
        btnOptionA.setEnabled(true);
        btnOptionB.setEnabled(true);
        btnOptionC.setEnabled(true);
        btnOptionD.setEnabled(true);

        // Hide next button until answered
        btnNext.setVisibility(View.GONE);
    }

    private void resetButtonStyles(Button btn) {
        btn.setBackgroundColor(Color.parseColor("#E0E0E0"));
        btn.setTextColor(Color.parseColor(COLOR_DEFAULT_TEXT));
    }

    private void checkAnswer(int selectedIndex) {
        Question q = questions.get(currentIndex);
        int correctIndex = q.getCorrectIndex();

        // Disable all buttons
        btnOptionA.setEnabled(false);
        btnOptionB.setEnabled(false);
        btnOptionC.setEnabled(false);
        btnOptionD.setEnabled(false);

        // Highlight correct answer green
        getButtonByIndex(correctIndex).setBackgroundColor(Color.parseColor(COLOR_CORRECT));
        getButtonByIndex(correctIndex).setTextColor(Color.WHITE);

        if (selectedIndex == correctIndex) {
            score++;
        } else {
            // Highlight wrong selection red
            getButtonByIndex(selectedIndex).setBackgroundColor(Color.parseColor(COLOR_WRONG));
            getButtonByIndex(selectedIndex).setTextColor(Color.WHITE);
        }

        // Show next button
        btnNext.setVisibility(View.VISIBLE);
        if (currentIndex == questions.size() - 1) {
            btnNext.setText("FINISH");
        } else {
            btnNext.setText("NEXT");
        }
    }

    private Button getButtonByIndex(int index) {
        switch (index) {
            case 0: return btnOptionA;
            case 1: return btnOptionB;
            case 2: return btnOptionC;
            case 3: return btnOptionD;
            default: return btnOptionA;
        }
    }
}
