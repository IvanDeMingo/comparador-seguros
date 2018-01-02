package pae.seguros.seguros;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pae.seguros.R;
import pae.seguros.databases.Insurance;

public class InsuranceAdapter extends RecyclerView.Adapter<InsuranceAdapter.ViewHolder>{

    List<Insurance> insuranceList;

    InsuranceAdapter(List<Insurance> insuranceList){
        this.insuranceList = insuranceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(insuranceList.get(position).carPlate);
        holder.subtitle.setText(String.valueOf(insuranceList.get(position).userDni));
        holder.photo.setImageResource(R.drawable.ic_seguro_icono);
    }

    @Override
    public int getItemCount() {
        return insuranceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView subtitle;
        ImageView photo;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            subtitle = (TextView)itemView.findViewById(R.id.subtitle);
            photo = (ImageView)itemView.findViewById(R.id.photo);
        }
    }

}
