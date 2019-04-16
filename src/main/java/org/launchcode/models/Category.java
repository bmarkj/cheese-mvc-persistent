package org.launchcode.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    //Instance variables
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15)
    private String name;

    @OneToMany //one Category can have many Cheese objects connected with it
    @JoinColumn(name = "category_id") //use this column in cheese table to determine which cheese belong to a given category
    private List<Cheese> cheeses = new ArrayList<>();

    //Constructors
    public Category() {

    }

    public Category(String name) {
        this.name = name;
    }


    //Getters & Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
