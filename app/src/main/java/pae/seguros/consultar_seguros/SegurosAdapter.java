package pae.seguros.consultar_seguros;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pae.seguros.R;

/**
 * Created by rucadi on 12/01/2018.
 */

public class SegurosAdapter extends RecyclerView.Adapter<SegurosAdapter.SeguroViewHolder>{
    private List<SeguroItem> segurosList;

    public static class SeguroViewHolder extends RecyclerView.ViewHolder
    {


        CardView cv;
        TextView seguroName;
        TextView seguroPrice;
        ImageView seguroPhoto;

        public SeguroViewHolder(View itemView) {
            super(itemView);

            cv = (CardView)itemView.findViewById(R.id.cardViewSeguros);
            seguroName = (TextView)itemView.findViewById(R.id.seguro_name);
            seguroPrice = (TextView)itemView.findViewById(R.id.person_age);
            seguroPhoto = (ImageView)itemView.findViewById(R.id.seguro_foto);

        }
    }




    @Override
    public SegurosAdapter.SeguroViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seguros_cards, viewGroup, false);
        SeguroViewHolder pvh = new SeguroViewHolder(v);
        return pvh;

    }

    @Override
    public void onBindViewHolder(SeguroViewHolder holder, int position) {


            holder.seguroPrice.setText(Integer.toString(segurosList.get(position).precio)+ "€");
            holder.seguroName.setText(segurosList.get(position).compania);
            holder.seguroPhoto.setImageResource(segurosList.get(position).photoId);

    }


    public SegurosAdapter(List<SeguroItem> seed)
    {
        this.segurosList = seed;
    }
    @Override
    public int getItemCount() {
        return segurosList.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
