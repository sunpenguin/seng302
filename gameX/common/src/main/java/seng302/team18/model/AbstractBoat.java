package seng302.team18.model;

import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.Random;

/**
 * Holds information about "boats", as conveyed in the AC35 protocol. Note that boats here means all boats (potentially
 * including marks, umpires and more), rather than just racing yachts.
 */
public abstract class AbstractBoat extends Observable {
    private String nameShort;
    private String nameStowe;
    private String name;
    private String hullNumber;
    private Integer id;
    private Color colour = Color.CHOCOLATE;

    public AbstractBoat() {
        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        colour = Color.hsb(hue, saturation, luminance);
    }


    public AbstractBoat(Integer id, String boatName, String shortName) {
        this.id = id;
        this.name = boatName;
        this.nameShort = shortName;

        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        colour = Color.hsb(hue, saturation, luminance);
    }

    public abstract Coordinate getCoordinate();

    public abstract double getLength();

    public String getShortName() {
        return nameShort;
    }


    public void setShortName(String nameShort) {
        this.nameShort = nameShort;
    }


    public String getStoweName() {
        return nameStowe;
    }


    public void setStoweName(String nameStowe) {
        this.nameStowe = nameStowe;
    }


    public String getName() {
        return name;
    }


    public void setBoatName(String name) {
        this.name = name;
    }


    public String getHullNumber() {
        return hullNumber;
    }


    public void setHullNumber(String hullNumber) {
        this.hullNumber = hullNumber;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public abstract BoatType getType();


    public Color getColour() {
        return colour;
    }


    public void setColour(Color colour) {
        if (colour != null) {
            this.colour = colour;
        }
    }
}


