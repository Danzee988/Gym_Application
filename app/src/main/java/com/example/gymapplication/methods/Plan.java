package com.example.gymapplication.methods;

public class Plan {
    private String name;

    private Exercise[] exercises;

    private int sets;

    private int reps;


    //-----------------------------Constructors-------------------------------
    public Plan(String name, Exercise[] exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    public Plan() {
    }

    //---------------------Getters---------------------
    public String getName() {
        return name;
    }

    public Exercise[] getExercises() {
        return exercises;
    }


    //---------------------Setters---------------------
    public void setName(String name) {
        this.name = name;
    }

    public void setExercises(Exercise[] exercises) {
        this.exercises = exercises;
    }

    //---------------------Methods---------------------
    public String toString() {
        String result = "";
        for (int i = 0; i < exercises.length; i++) {
            result += exercises[i].getName() + "\n";
        }
        return result;
    }
}
