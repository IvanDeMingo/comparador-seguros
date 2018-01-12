package pae.seguros.consultar_seguros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pae.seguros.R;

public class BuquedaSeguros extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;



    String getNameFromInt(int value)
    {
        switch(value)
        {
            case 0: return "Axa";
            case 1: return "Fiatc";
            case 2: return "Linea Directa";
            case 3: return "Mapfre";
            case 4: return "Racc";
        }
        return "";
    }

    int getSeguroImageId(int value)
    {
        switch(value)
        {
            case 0: return R.drawable.c_axa;
            case 1: return R.drawable.c_fiatc;
            case 2: return R.drawable.c_lineadirecta;
            case 3: return R.drawable.c_mapfre;
            case 4: return R.drawable.c_racc;
        }
        return 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int seed = getIntent().getIntExtra("seed",0);
        List<SeguroItem> segurosList = new ArrayList<>();
        Random random = new Random(seed);
        segurosList = new ArrayList<SeguroItem>();
        int numero_de_seguros = random.nextInt(5)+5;

        for(int i=0;i<=numero_de_seguros;++i)
        {
            int price = random.nextInt(1200)+600;       //600-1800
            int seguro = random.nextInt(5);
            String name = getNameFromInt(seguro);
            int photoId = getSeguroImageId(seguro);

            segurosList.add(new SeguroItem(name,price,photoId));

        }


        Log.e("size",String.valueOf(segurosList.size()));
        setContentView(R.layout.activity_buqueda_seguros);

         recyclerView = (RecyclerView)findViewById(R.id.rvseg);
          mAdapter = new SegurosAdapter(segurosList);
        recyclerView.setAdapter(mAdapter);

         layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);






    }
}
