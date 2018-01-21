package pae.seguros.vehiculos;

import android.content.Context;
import android.graphics.Color;
import android.renderscript.BaseObj;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pae.seguros.R;
import pae.seguros.databases.Car;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder>{

    List<Car> carList;
    private int selectedItem;
    Boolean highlighted;
    private Context mContext;

    CarAdapter(List<Car> carList, Boolean highlighted, Context context){
        this.carList = carList;
        selectedItem = -1;
        this.highlighted = highlighted;
        mContext = context;
    }

    public Car getSelectedItem() {
        return selectedItem >= 0 ? carList.get(selectedItem) : null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.title.setText(String.format("%s %s", car.company, car.model));
        holder.subtitle.setText(car.plate);
        holder.photo.setImageResource(R.drawable.ic_car_color);
        if(selectedItem == position) holder.cv.setBackgroundColor(Color.parseColor("#ccff00"));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            if (highlighted) itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (selectedItem == (getAdapterPosition())) {
                selectedItem = -1;
                cv.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            else if (selectedItem < 0){
                selectedItem = getAdapterPosition();
                cv.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
            }
            else {
                Toast.makeText(v.getContext(), "You have already selected a car, first deselect him to be able to choose another",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateCars(List<Car> carList) {
        this.carList = carList;
        notifyDataSetChanged();
    }

}
