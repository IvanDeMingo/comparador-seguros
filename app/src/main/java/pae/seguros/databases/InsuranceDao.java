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

    @Query("delete FROM insurance WHERE userDni=:userDni")
    void removeInsuranceByUser(final String userDni);

    @Query("delete FROM insurance WHERE carPlate=:carPlate")
    void removeInsuranceByCar(final String carPlate);

    @Query("SELECT * FROM insurance WHERE userDni=:userDni")
    List<Insurance> findInsuranceByUser(final String userDni);

    @Query("SELECT * FROM insurance WHERE carPlate=:carPlate")
    List<Insurance> findInsuranceByCar(final String carPlate);
}