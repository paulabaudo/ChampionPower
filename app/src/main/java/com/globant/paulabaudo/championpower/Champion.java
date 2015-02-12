package com.globant.paulabaudo.championpower;

import java.util.ArrayList;

/**
 * Created by paula.baudo on 12/02/2015.
 */
public class Champion {

    private String name;
    private int id;
    private ArrayList<String> spells;

    public Champion() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getSpells() {
        return spells;
    }

    public void setSpells(ArrayList<String> spells) {
        this.spells = spells;
    }
}
