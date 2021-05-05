package pollub.ism.lab6;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PozycjaMagazynowaMojaDAO {

    @Insert(entity = PozycjaMagazynowaMoja.class)
    public void insertHistory(PozycjaMagazynowaMoja pozycjaMoja);

    @Query("SELECT * FROM warzywniakhistoria WHERE NAME = :wybraneWarzywoNazwa") //Automatyczna kwerenda wystarczy
    public Cursor selectHistory(String wybraneWarzywoNazwa);

}