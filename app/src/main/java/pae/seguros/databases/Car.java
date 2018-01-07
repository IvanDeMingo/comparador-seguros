package pae.seguros.databases;

/**
 * Created by ruben on 16/12/2017.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class Car {

    @PrimaryKey
    @NonNull
    public final String plate;
    public String model, company, colour;
    public String PhotoPATH;
    public long firstMatriculation;   //TODO: add Blob of data with the image and more information
    public String gas;
    public Double power;
    public long kmyear;
    public String garage;
    public long carDoors;


    public Car(String plate, String model, String company, String colour, long firstMatriculation, String PhotoPATH,
               String gas, Double power, long kmyear, String garage, long carDoors) {
        this.plate = plate;
        this.model = model;
        this.company = company;
        this.colour = colour;
        this.firstMatriculation = firstMatriculation;
        this.PhotoPATH = PhotoPATH;
        this.gas = gas;
        this.power = power;
        this.kmyear = kmyear;
        this.garage = garage;
        this.carDoors = carDoors;
    }
}
