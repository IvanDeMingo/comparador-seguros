package pae.seguros.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by ruben on 16/12/2017.
 */

@Dao
public interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCar(Car car);

    @Query("select * from car")
    public List<Car> getAllCars();

    @Query("select * from car where plate = :plate")
    public List<Car> getCar(String plate);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(Car car);

    @Query("delete from car")
    void removeAllCars();
}