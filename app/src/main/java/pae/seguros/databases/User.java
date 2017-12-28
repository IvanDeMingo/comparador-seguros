package pae.seguros.databases;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class User {

    @PrimaryKey
    public final int dni;
    public String name, surname, lastname;
    public String nationality;
    public String birthPlace;
    public String home;
    public String address;
    public int CAN;
    public long birthday;//EPOCH UNIX TIMESTAMP
    public boolean sex;

    public User(int dni, String name, String surname, String lastname, String nationality, String birthPlace,
                String home, String address, int CAN, long birthday, boolean sex) {
        this.dni          = dni;
        this.name         = name;
        this.surname      = surname;
        this.lastname     = lastname;
        this.nationality  = nationality;
        this.home         = home;
        this.birthPlace   = birthPlace;
        this.address      = address;
        this.CAN          = CAN;
        this.birthday     = birthday;
        this.sex          = sex;
    }

}