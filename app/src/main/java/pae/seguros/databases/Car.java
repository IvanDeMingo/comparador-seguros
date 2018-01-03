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


    public Car(String plate, String model, String company, String colour, long firstMatriculation, String PhotoPATH) {
        this.plate = plate;
        this.model = model;
        this.company = company;
        this.colour = colour;
        this.firstMatriculation = firstMatriculation;
        this.PhotoPATH = PhotoPATH;
    }
}
