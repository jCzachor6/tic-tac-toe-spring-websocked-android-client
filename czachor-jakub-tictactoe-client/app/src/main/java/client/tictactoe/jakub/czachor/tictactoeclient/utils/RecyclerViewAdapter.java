package client.tictactoe.jakub.czachor.tictactoeclient.utils;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.model.Room;

/**
 * @author jakub
 * Created on 12.05.2019.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Room> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public RecyclerViewAdapter(Context context, List<Room> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.room_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String playerOne = data.get(position).getPlayerOneName();
        holder.playerOne.setText(StringUtils.defaultString(playerOne, "-empty-"));
        String playerTwo = data.get(position).getPlayerTwoName();
        holder.playerTwo.setText(StringUtils.defaultString(playerTwo, "-empty-"));
        String roomId = "Room " + data.get(position).getId() + ": ";
        holder.roomId.setText(roomId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView playerOne;
        TextView playerTwo;
        TextView roomId;

        ViewHolder(View itemView) {
            super(itemView);
            playerOne = itemView.findViewById(R.id.player_one_name);
            playerTwo = itemView.findViewById(R.id.player_two_name);
            roomId = itemView.findViewById(R.id.room_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public Room getItem(int id) {
        return data.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
