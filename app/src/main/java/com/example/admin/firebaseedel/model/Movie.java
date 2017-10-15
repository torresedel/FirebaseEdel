package com.example.admin.firebaseedel.model;

/**
 * Created by Admin on 9/21/2017.
 */

public class Movie {

    String name;
    String director;
    String year;

    public Movie() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getName() {

        return name;
    }

    public String getDirector() {
        return director;
    }

    public String getYear() {
        return year;
    }

    public Movie(String name, String director, String year) {

        this.name = name;
        this.director = director;
        this.year = year;
    }
}
