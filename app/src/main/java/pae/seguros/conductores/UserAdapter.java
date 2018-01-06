package pae.seguros.conductores;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pae.seguros.R;
import pae.seguros.databases.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    List<User> userList;

    UserAdapter(List<User> userList){
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(String.valueOf(userList.get(position).dni));
        holder.subtitle.setText(userList.get(position).name);
        holder.photo.setImageResource(R.drawable.ic_conductor_icono2);
    }

    @Override
    public int getItemCount() {
        return userList.size();
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

    public void updateUsers(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

}
