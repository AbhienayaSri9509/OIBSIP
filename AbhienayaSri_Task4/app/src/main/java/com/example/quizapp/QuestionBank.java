package com.example.quizapp;

import java.util.ArrayList;

public class QuestionBank {

    public static ArrayList<Question> getQuestions() {
        ArrayList<Question> questions = new ArrayList<>();

        questions.add(new Question(
                "What is the capital of France?",
                new String[]{"Paris", "London", "Berlin", "Rome"},
                0
        ));

        questions.add(new Question(
                "Which planet is the largest?",
                new String[]{"Earth", "Mars", "Jupiter", "Saturn"},
                2
        ));

        questions.add(new Question(
                "Who painted the Mona Lisa?",
                new String[]{"Van Gogh", "Da Vinci", "Picasso", "Rembrandt"},
                1
        ));

        questions.add(new Question(
                "Chemical symbol for Gold?",
                new String[]{"Au", "Ag", "Fe", "Cu"},
                0
        ));

        questions.add(new Question(
                "How many continents on Earth?",
                new String[]{"5", "6", "7", "8"},
                2
        ));

        questions.add(new Question(
                "Speed of light (approx)?",
                new String[]{"300000 km/s", "150000 km/s", "500000 km/s", "1000 km/s"},
                0
        ));

        questions.add(new Question(
                "Who wrote Romeo and Juliet?",
                new String[]{"Dickens", "Shakespeare", "Hemingway", "Austen"},
                1
        ));

        questions.add(new Question(
                "Largest ocean on Earth?",
                new String[]{"Atlantic", "Indian", "Arctic", "Pacific"},
                3
        ));

        questions.add(new Question(
                "How many sides does a hexagon have?",
                new String[]{"5", "6", "7", "8"},
                1
        ));

        questions.add(new Question(
                "What gas do plants absorb?",
                new String[]{"Oxygen", "Nitrogen", "CO2", "Hydrogen"},
                2
        ));

        return questions;
    }
}
