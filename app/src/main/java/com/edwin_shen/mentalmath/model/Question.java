package com.edwin_shen.mentalmath.model;

import java.util.Random;

// to represent a question
public class Question {
    private int min;
    private int max;
    private int n1;
    private int n2;
    private String operation;
    public String question;
    public int answer;

    public Question(int min, int max, String operation) {
        this.min = min;
        this.max = max;
        this.n1 = new Random().nextInt(max - min + 1) + min;
        this.n2 = new Random().nextInt(max - min + 1) + min;
        this.operation = operation;
        this.question = n1 + " " + operation + " "+ n2;
        if(operation.equals("+")) {
            this.answer = n1 + n2;
        }

        else if (operation.equals("-")) {
            this.answer = n1 - n2;
        }

        else {
            this.answer = n1 * n2;
        }
    }



}
