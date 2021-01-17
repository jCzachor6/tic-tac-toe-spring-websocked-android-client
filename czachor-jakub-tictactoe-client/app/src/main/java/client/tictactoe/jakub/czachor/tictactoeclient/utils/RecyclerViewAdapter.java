package client.tictactoe.jakub.czachor.tictactoeclient.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameListMessage;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameRoomDto;

/**
 * @author jakub
 * Created on 12.05.2019.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<GameRoomDto> rooms;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public RecyclerViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.rooms = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.room_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String playerOne = rooms.get(position).getPlayerOneName();
        holder.playerOne.setText(StringUtils.defaultString(playerOne, "-empty-"));
        String playerTwo = rooms.get(position).getPlayerTwoName();
        holder.playerTwo.setText(StringUtils.defaultString(playerTwo, "-empty-"));
        String roomId = "Room " + rooms.get(position).getId() + ": ";
        holder.roomId.setText(roomId);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
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

    public GameRoomDto getItem(int id) {
        return rooms.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void updateData(GameListMessage data) {
        String state = data.getState();
        List<GameRoomDto> rooms = data.getRooms();
        if (state.equals("ALL")) {
            this.rooms = rooms;
            notifyDataSetChanged();
        } else if (state.equals("UPDATED")) {
            for (int i = 0; i < rooms.size(); i++) {
                for (int j = 0; j < this.rooms.size(); j++) {
                    GameRoomDto newer = rooms.get(i);
                    GameRoomDto original = this.rooms.get(j);
                    if (rooms.get(i).getId().equals(original.getId())) {
                        original.setPlayerOneName(newer.getPlayerOneName());
                        original.setPlayerTwoName(newer.getPlayerTwoName());
                        notifyItemChanged(j);
                    }
                }
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
