package pae.seguros.databases;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(primaryKeys = { "userDni", "carPlate" },
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "dni",
                        childColumns = "userDni"),
                @ForeignKey(entity = Car.class,
                        parentColumns = "plate",
                        childColumns = "carPlate")
        })
public class Insurance {
    @NonNull
    public String userDni;
    @NonNull
    public String carPlate;
    //TODO: añadir campos

    public Insurance(String userDni, String carPlate) {
        this.userDni = userDni;
        this.carPlate = carPlate;
    }
}