package pollub.ism.lab6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pollub.ism.lab6.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ArrayAdapter<CharSequence> adapter;

    private String wybraneWarzywoNazwa = null;
    private Integer wybraneWarzywoIlosc = null;

    public enum OperacjaMagazynowa {SKLADUJ, WYDAJ};

    private BazaMagazynowa bazaDanych;
    private BazaMagazynowaMoja bazaDanychHistoria;

    Date currentTime;
    DateFormat dateFull = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
    String currentTimeFormated;

    public String wholeHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
        PozycjaMagazynowaMoja pozycjaMoja = new PozycjaMagazynowaMoja();
//stąd
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = ArrayAdapter.createFromResource(this, R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinnerWybor.setAdapter(adapter);

        bazaDanych = Room.databaseBuilder(getApplicationContext(), BazaMagazynowa.class, BazaMagazynowa.NAZWA_BAZY)
                .allowMainThreadQueries().build();

        bazaDanychHistoria = Room.databaseBuilder(getApplicationContext(), BazaMagazynowaMoja.class, BazaMagazynowaMoja.NAZWA_BAZY)
                .allowMainThreadQueries().build();

        if(bazaDanych.pozycjaMagazynowaDAO().size() == 0){
            String[] asortyment = getResources().getStringArray(R.array.Asortyment);
            for(String nazwa : asortyment){
                PozycjaMagazynowa pozycjaMagazynowa = new PozycjaMagazynowa();
                pozycjaMagazynowa.NAME = nazwa; pozycjaMagazynowa.QUANTITY = 0;
                bazaDanych.pozycjaMagazynowaDAO().insert(pozycjaMagazynowa);
            }
        }


        binding.buttonSkladuj.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int hehe = Integer.parseInt(binding.editTextIlosc.getText().toString());
            zmienStan(OperacjaMagazynowa.SKLADUJ);

            currentTime = Calendar.getInstance().getTime();
            currentTimeFormated = dateFull.format(currentTime);

            pozycjaMoja.DATE = currentTimeFormated;
            pozycjaMoja.NAME = wybraneWarzywoNazwa;
            pozycjaMoja.ELDER = wybraneWarzywoIlosc - hehe;
            pozycjaMoja.NEWBIE = wybraneWarzywoIlosc ;
                    bazaDanychHistoria.pozycjaMagazynowaMojaDAO().insertHistory(pozycjaMoja);
            aktualizuj();
        }
    });

        binding.buttonWydaj.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int hehe = Integer.parseInt(binding.editTextIlosc.getText().toString());
            zmienStan(OperacjaMagazynowa.WYDAJ);

            currentTime = Calendar.getInstance().getTime();
            currentTimeFormated = dateFull.format(currentTime);

            pozycjaMoja.DATE = currentTimeFormated;
            pozycjaMoja.NAME = wybraneWarzywoNazwa;
            pozycjaMoja.ELDER = wybraneWarzywoIlosc + hehe;
            pozycjaMoja.NEWBIE = wybraneWarzywoIlosc ;
            bazaDanychHistoria.pozycjaMagazynowaMojaDAO().insertHistory(pozycjaMoja);
            aktualizuj();
        }
    });

        binding.spinnerWybor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            wybraneWarzywoNazwa = adapter.getItem(i).toString(); // <---
            aktualizuj();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //Nie będziemy implementować, ale musi być
        }
    });
aktualizuj();
}

    public String getTableAsString() {
            int pom=0;
        String tableString="";
        Cursor allRows  = bazaDanychHistoria.pozycjaMagazynowaMojaDAO().selectHistory(wybraneWarzywoNazwa);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();

            do {
                for (String name: columnNames) {
                    if(pom!=0)
                    tableString += String.format("%s ", allRows.getString(allRows.getColumnIndex(name)));
                    if(pom==3)
                        tableString += "-> ";
                    pom++;
                    if(pom==5)
                        pom=0;
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

    private void aktualizuj(){

        wholeHistory = getTableAsString();


        wybraneWarzywoIlosc = bazaDanych.pozycjaMagazynowaDAO().findQuantityByName(wybraneWarzywoNazwa);
        binding.textStan.setText("Stan magazynu dla " + wybraneWarzywoNazwa + " wynosi: " + wybraneWarzywoIlosc);
        binding.multiLine.setText(wholeHistory);
        binding.textEdycja.setText(currentTimeFormated);
    }


    private void zmienStan(OperacjaMagazynowa operacja){

        Integer zmianaIlosci = null, nowaIlosc = null;

        try {
            zmianaIlosci = Integer.parseInt(binding.editTextIlosc.getText().toString());
        }catch(NumberFormatException ex){
            return;
        }finally {
            binding.editTextIlosc.setText("");
        }

        switch (operacja){
            case SKLADUJ: nowaIlosc = wybraneWarzywoIlosc + zmianaIlosci; break;
            case WYDAJ: nowaIlosc = wybraneWarzywoIlosc - zmianaIlosci; break;
        }

        bazaDanych.pozycjaMagazynowaDAO().updateQuantityByName(wybraneWarzywoNazwa,nowaIlosc);

        aktualizuj();
    }

    }

