package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private TextView timeDisplay;
    private Button btnStart, btnStop, btnLap, btnReset;
    private LinearLayout lapContainer;
    private ScrollView lapScroll;

    private Handler handler = new Handler(Looper.getMainLooper());
    private long elapsedMs = 0;
    private long startTimeMs = 0;
    private boolean isRunning = false;
    private boolean wasRunning = false;
    private int lapCount = 0;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedMs = SystemClock.elapsedRealtime() - startTimeMs;
            updateDisplay();
            handler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeDisplay = findViewById(R.id.timeDisplay);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnLap = findViewById(R.id.btnLap);
        btnReset = findViewById(R.id.btnReset);
        lapContainer = findViewById(R.id.lapContainer);
        lapScroll = findViewById(R.id.lapScroll);

        updateDisplay();
        setInitialState();

        btnStart.setOnClickListener(v -> startTimer());
        btnStop.setOnClickListener(v -> stopTimer());
        btnLap.setOnClickListener(v -> recordLap());
        btnReset.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        startTimeMs = SystemClock.elapsedRealtime() - elapsedMs;
        isRunning = true;
        handler.post(timerRunnable);
        setRunningState();
    }

    private void stopTimer() {
        isRunning = false;
        handler.removeCallbacks(timerRunnable);
        setPausedState();
    }

    private void resetTimer() {
        isRunning = false;
        wasRunning = false;
        handler.removeCallbacks(timerRunnable);
        elapsedMs = 0;
        lapCount = 0;
        updateDisplay();
        clearLaps();
        setInitialState();
    }

    private void recordLap() {
        lapCount++;
        String lapTime = formatTime(elapsedMs);
        TextView lapView = new TextView(this);
        lapView.setText("Lap " + lapCount + ": " + lapTime);
        lapView.setTextSize(16);
        lapView.setPadding(16, 8, 16, 8);
        lapView.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        lapContainer.addView(lapView, 0);
        lapScroll.post(() -> lapScroll.fullScroll(View.FOCUS_UP));
    }

    private void clearLaps() {
        lapContainer.removeAllViews();
    }

    private void updateDisplay() {
        timeDisplay.setText(formatTime(elapsedMs));
    }

    private String formatTime(long ms) {
        long totalSeconds = ms / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        long centiseconds = (ms % 1000) / 10;
        return String.format("%02d:%02d:%02d", minutes, seconds, centiseconds);
    }

    private void setInitialState() {
        btnStart.setText("START");
        btnStart.setEnabled(true);
        btnStart.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));

        btnStop.setEnabled(false);
        btnStop.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        btnLap.setEnabled(false);
        btnLap.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        btnReset.setEnabled(false);
        btnReset.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
    }

    private void setRunningState() {
        btnStart.setEnabled(false);
        btnStart.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        btnStop.setEnabled(true);
        btnStop.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.red));

        btnLap.setEnabled(true);
        btnLap.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue));

        btnReset.setEnabled(true);
        btnReset.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_dark));
    }

    private void setPausedState() {
        btnStart.setText("Resume");
        btnStart.setEnabled(true);
        btnStart.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));

        btnStop.setEnabled(false);
        btnStop.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        btnLap.setEnabled(false);
        btnLap.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        btnReset.setEnabled(true);
        btnReset.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_dark));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRunning) {
            wasRunning = true;
            handler.removeCallbacks(timerRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            wasRunning = false;
            startTimeMs = SystemClock.elapsedRealtime() - elapsedMs;
            handler.post(timerRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);
    }
}
