package com.example.final_project_backgammon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class Adapter_Game extends RecyclerView.Adapter<Adapter_Game.GameViewHolder> {

    private Context context;
    private ArrayList<Room> waitingList;
    private CallBack_startGameProtocol callBack_startGameProtocol;

    public void setCallBack_startGameProtocol(CallBack_startGameProtocol callBack_startGameProtocol) {
        this.callBack_startGameProtocol = callBack_startGameProtocol;
    }

    public Adapter_Game(Context context, ArrayList<Room> waitingList ){
        this.context=context;
        this.waitingList= waitingList;

    }


    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waitinglist_room, parent, false);
        GameViewHolder gameViewHolder = new GameViewHolder(view);
        return gameViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Game.GameViewHolder holder, int position) {
        Room waitingRoom = waitingList.get(position);
        if(waitingRoom.getHost().getName() != null)
            holder.room_LBL_host.setText(waitingRoom.getHost().getName());

        holder.listgame_BTN_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack_startGameProtocol.game(waitingRoom);
            }
        });

    }

    @Override
    public int getItemCount() {
        return   waitingList == null || waitingList.size() ==0 ? 0: waitingList.size();
    }

    class GameViewHolder extends RecyclerView.ViewHolder{

        private MaterialTextView room_LBL_host;
        private Button listgame_BTN_join;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);

            findViews(itemView);
        }

        private void findViews(View itemView) {
            room_LBL_host = itemView.findViewById(R.id.room_LBL_host);
            listgame_BTN_join = itemView.findViewById(R.id.listgame_BTN_join);
        }
    }
}
