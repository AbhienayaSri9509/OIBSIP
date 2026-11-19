# OIBSIP - Android App Development Track

**Developer:** Abhienaya Sri  
**Organization:** Oasis Infobyte (OIBSIP)  
**Track:** Android App Development  
**GitHub Repository:** [https://github.com/AbhienayaSri9509/OIBSIP.git](https://github.com/AbhienayaSri9509/OIBSIP.git)

---

## 📌 Overview

This repository contains the complete implementation for all **5 Android application development tasks** assigned under the Oasis Infobyte Android App Development Internship track. Each task has been built using **Java, XML, Android Studio standard project structure**, and follows Android development best practices.

---

## 📂 Project Directory Structure

```text
OIBSIP/
├── AbhienayaSri_Task1/     # Task 1 · Unit Converter Application
├── AbhienayaSri_Task2/     # Task 2 · To-Do App with Login (SQLite)
├── AbhienayaSri_Task3/     # Task 3 · Calculator Application
├── AbhienayaSri_Task4/     # Task 4 · Quiz Application
├── AbhienayaSri_Task5/     # Task 5 · Stopwatch Application
└── README.md
```

---

## 📱 Tasks & Feature Documentation

### 🔹 TASK 1 · Unit Converter Application
- **Directory:** `AbhienayaSri_Task1/`
- **Tech Stack:** Android Studio, Java, XML
- **Package:** `com.example.unitconverter`
- **Features Implemented:**
  - Category selector Spinner supporting 3 measurement categories: **Length**, **Weight**, and **Temperature**.
  - Dynamic Source (`From`) and Target (`To`) dropdown Spinners updating based on selected category.
  - Numeric input field (`EditText`) with input validation displaying Toast errors on empty or non-numeric entries.
  - Convert button computing accurate conversions across units:
    - **Length:** mm, cm, m, km, inch, foot, yard, mile (via standard SI meter baseline).
    - **Weight:** mg, g, kg, lb, oz (via gram baseline).
    - **Temperature:** Celsius, Fahrenheit, Kelvin with accurate temperature conversion equations.
  - Large styled result display label.

---

### 🔹 TASK 2 · To-Do App with Login
- **Directory:** `AbhienayaSri_Task2/`
- **Tech Stack:** Android Studio, Java, XML, SQLite
- **Package:** `com.example.todoapp`
- **Features Implemented:**
  - **User Authentication:** Login and Sign-up screens with form validation.
  - **Password Security:** Basic password hashing via MD5 (`MessageDigest`) before saving to SQLite database (no plain text storage).
  - **Session Management:** User session preserved with `SharedPreferences` and complete session clearing on Logout.
  - **User-Specific Tasks:** SQLite database (`DatabaseHelper`) storing tasks linked directly to each user ID (`user_id` foreign key).
  - **Task Management:**
    - Add task dialog/screen with title and optional notes.
    - Mark tasks complete/incomplete with dynamic strikethrough styling and state persistence.
    - Delete tasks permanently with instant list updates.
  - **Friendly Empty State:** Displays a helpful message when no tasks are present.

---

### 🔹 TASK 3 · Calculator Application
- **Directory:** `AbhienayaSri_Task3/`
- **Tech Stack:** Android Studio, Java, XML
- **Package:** `com.example.calculator`
- **Features Implemented:**
  - Responsive button grid layout implemented via XML `GridLayout` and `ConstraintLayout`.
  - Numeric buttons (`0–9`) and decimal point handling.
  - Arithmetic operations: addition (`+`), subtraction (`−`), multiplication (`×`), and division (`÷`).
  - Clear (`C`) to reset all state and Backspace (`⌫`) to remove the last character.
  - Percentage (`%`) calculations.
  - Division-by-zero error handling gracefully displaying `"Error"`.
  - Crash-proof arithmetic engine handling repeated and rapid taps.

---

### 🔹 TASK 4 · Quiz Application
- **Directory:** `AbhienayaSri_Task4/`
- **Tech Stack:** Android Studio, Java, XML
- **Package:** `com.example.quizapp`
- **Features Implemented:**
  - **Welcome Screen:** Engaging title, description, and "Start Quiz" button.
  - **Question Screen:**
    - Live question counter (e.g., "Question 3 of 10").
    - 4 multiple-choice answer option buttons.
    - 10 comprehensive general knowledge questions (`QuestionBank.java`).
    - Questions shuffled randomly at the start of every session (`Collections.shuffle`).
  - **Instant Visual Feedback:** Selected answer highlights in Green (`#4CAF50`) if correct, or Red (`#F44336`) if incorrect while simultaneously revealing the correct answer.
  - **Score Tracking & Results Screen:**
    - Detailed results screen showing total score, number correct, and number incorrect.
    - "Restart Quiz" button restarting the quiz with refreshed state.

---

### 🔹 TASK 5 · Stopwatch Application
- **Directory:** `AbhienayaSri_Task5/`
- **Tech Stack:** Android Studio, Java, XML
- **Package:** `com.example.stopwatch`
- **Features Implemented:**
  - Large monospace time display with millisecond precision in `MM:SS:cc` format.
  - Precise time tracking using Android `Handler` and `Runnable` posting at 10ms intervals.
  - Controls: **Start**, **Stop/Pause**, **Reset**, and **Lap**.
  - Dynamic button state handling (e.g., Start disables when running, Stop and Lap become enabled).
  - **Lifecycle Awareness:** Saves elapsed time and state on `onPause()` and seamlessly resumes on `onResume()`.
  - **Lap Functionality:** Dedicated Lap button recording timestamped splits into a dynamically updating scrollable list.

---

## 🛠️ How to Run in Android Studio

1. Clone the repository:
   ```bash
   git clone https://github.com/AbhienayaSri9509/OIBSIP.git
   ```
2. Open **Android Studio**.
3. Select **File -> Open...** and navigate to any specific task folder (e.g., `OIBSIP/AbhienayaSri_Task1`).
4. Allow Gradle to sync dependencies.
5. Select an Android Emulator or connected physical device (Android 5.0+ / API 21+).
6. Click **Run (`Shift + F10`)** to launch the application.

---

## 📄 License
This repository is submitted as part of the **Oasis Infobyte Internship Program (OIBSIP)**.
