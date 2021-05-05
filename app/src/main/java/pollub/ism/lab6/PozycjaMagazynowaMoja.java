package pollub.ism.lab6;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WarzywniakHistoria")
public class PozycjaMagazynowaMoja {

    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String NAME;
    public String DATE;
    public int ELDER;
    public int NEWBIE;

}