package pollub.ism.lab6;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PozycjaMagazynowaMoja.class}, version = BazaMagazynowaMoja.WERSJA, exportSchema = false)
public abstract class BazaMagazynowaMoja extends RoomDatabase {

    public static final String NAZWA_BAZY = "Stoisko z warzywami historia";
    public static final int WERSJA = 1;

    public abstract PozycjaMagazynowaMojaDAO pozycjaMagazynowaMojaDAO();
}