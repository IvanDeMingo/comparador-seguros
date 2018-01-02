package pae.seguros.databases;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface InsuranceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addInsurance(Insurance insurance);

    @Query("select * from insurance")
    public List<Insurance> getAllInsurances();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateInsurance(Insurance insurance);

    @Query("delete from insurance")
    void removeAllInsurances();

    @Query("SELECT * FROM insurance WHERE userDni=:userDni")
    List<Insurance> findInsuranceByUser(final int userDni);

    @Query("SELECT * FROM insurance WHERE carPlate=:carPlate")
    List<Insurance> findInsuranceByCar(final int carPlate);
}