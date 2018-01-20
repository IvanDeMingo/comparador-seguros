package pae.seguros.conductores;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pae.seguros.R;
import pae.seguros.databases.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    List<User> userList;
    private int selectedItem;
    Boolean highlighted;

    UserAdapter(List<User> userList, Boolean highlighted){
        this.userList = userList;
        selectedItem = -1;
        this.highlighted = highlighted;
    }

    public User getSelectedItem() {
        return selectedItem >= 0 ? userList.get(selectedItem) : null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.title.setText(String.format("%s %s %s", user.name, user.surname, user.lastname));
        holder.subtitle.setText(user.dni);
        holder.photo.setImageResource(R.drawable.ic_conductor_icono2);
        if(selectedItem == position) holder.cv.setBackgroundColor(Color.parseColor("#ccff00"));
    }

    @Override
    public int getItemCount() {
        return userList.size();
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
                cv.setBackgroundColor(Color.parseColor("#ccff00"));
            }
            else {
                Toast.makeText(v.getContext(), "You have already selected a driver, first deselect him to be able to choose another",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateUsers(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

}
