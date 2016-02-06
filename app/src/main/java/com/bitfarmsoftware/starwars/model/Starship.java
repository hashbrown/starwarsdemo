package com.bitfarmsoftware.starwars.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class Starship implements Serializable{

    @SerializedName("url")
    private Uri id;
    private String name;
    private String model;
    private String manufacturer;
    @SerializedName("cost_in_credits")
    private String cost;
    private String length;
    @SerializedName("max_atmosphering_speed")
    private String maxAtmospheringSpeed;
    private String crew;
    private String passengers;
    @SerializedName("cargo_capacity")
    private String cargoCapacity;
    @SerializedName("hyperdrive_rating")
    private String hyperdriveRating;
    @SerializedName("MGLT")
    private String megalights;
    @SerializedName("starship_class")
    private String starshipClass;
    private List<Uri> pilots;
    private List<Uri> films;

    public String getCargoCapacity() {
        return cargoCapacity;
    }

    public String getCost() {
        return cost;
    }

    public String getCrew() {
        return crew;
    }

    public List<Uri> getFilms() {
        return films;
    }

    public String getHyperdriveRating() {
        return hyperdriveRating;
    }

    public Uri getId() {
        return id;
    }

    public String getLength() {
        return length;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getMaxAtmospheringSpeed() {
        return maxAtmospheringSpeed;
    }

    public String getMegalights() {
        return megalights;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getPassengers() {
        return passengers;
    }

    public List<Uri> getPilots() {
        return pilots;
    }

    public String getStarshipClass() {
        return starshipClass;
    }
}
