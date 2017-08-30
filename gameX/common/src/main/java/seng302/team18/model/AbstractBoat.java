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
    private BodyMass bodyMass = new BodyMass();

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


    public void setLength(double length) {
        bodyMass.setRadius(length / 2);
    }


    public double getLength() {
        return bodyMass.getRadius() * 2;
    }

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

    public BodyMass getBodyMass() {
        return bodyMass;
    }


    public void setBodyMass(BodyMass bodyMass) {
        this.bodyMass = bodyMass;
    }


    public void setCoordinate(Coordinate coordinate) {
        bodyMass.setLocation(coordinate);
    }


    public Coordinate getCoordinate() {
        return bodyMass.getLocation();
    }


    public double getWeight() {
        return bodyMass.getWeight();
    }


    public void setWeight(double weight) {
        bodyMass.setWeight(weight);
    }
}


