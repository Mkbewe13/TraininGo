package com.example.trainingo20;

public class Exercise {

    private String name;
    private int reps;
    private int series;
    private boolean fieldOk;

    public Exercise(String Name,int Reps,int Series,boolean FieldOk)
    {
        name = Name;
        reps = Reps;
        series = Series;
        fieldOk = FieldOk;

    }

    public Exercise(String Name)
    {
        name = Name;
        reps = 0;
        series = 0;
        fieldOk = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public void setFieldOk(boolean val)
    {
        fieldOk = val;
    }

    public boolean getFieldOk()
    {
        return fieldOk;
    }
}
