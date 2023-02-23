package com.example.final_project_backgammon;

import static com.example.final_project_backgammon.ChipRow.COLOR.BROWN;
import static com.example.final_project_backgammon.ChipRow.COLOR.WHITE;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Chip extends RecyclerView.Adapter<Adapter_Chip.ChipViewHolder> {

    ArrayList<ChipsRow> chipsRows;
    private CallBack_chipProtocol callBack_chipProtocol;

    private UserDetails theUser;

    public Adapter_Chip(ArrayList<ChipsRow> chipsRows) {
        this.chipsRows = chipsRows;
    }

    public void setTheUser(UserDetails theUser) {
        this.theUser = theUser;
    }

    public void setCallBack_chipProtocol(CallBack_chipProtocol callBack_chipProtocol) {
        this.callBack_chipProtocol = callBack_chipProtocol;
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_block, parent, false);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChipViewHolder holder, final int position) {
        ChipsRow chipsRow = chipsRows.get(position);
        turnOn(chipsRow.getWhites(), holder.gameblock_LAY_chipContainer2);
        turnOn(chipsRow.getBrowns(), holder.gameblock_LAY_chipContainer1);
        initViews(holder, position);
    }

    private void turnOn(ArrayList<ChipRow> chips, LinearLayoutCompat container) {
        for (int i = 0; i < chips.size(); i++) {
            if (chips.get(i).getIsVisible() == true)
                container.getChildAt(i).setVisibility(View.VISIBLE);
            else
                container.getChildAt(i).setVisibility(View.INVISIBLE);
        }
    }

    private void initViews(final ChipViewHolder holder, final int position) {
        for(int i = 0; i < 13; i++) {
            int finalI = i;
            if(theUser.getChipColor() == UserDetails.CHIP.BROWN) {
                holder.gameblock_LAY_chipContainer1.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder.gameblock_LAY_chipContainer1.getChildAt(finalI).getVisibility() == View.VISIBLE)
                            clicked(chipsRows.get(position).getBrowns().get(finalI));
                    }
                });
            }
            else if(theUser.getChipColor() == UserDetails.CHIP.WHITE){
                holder.gameblock_LAY_chipContainer2.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder.gameblock_LAY_chipContainer2.getChildAt(finalI).getVisibility() == View.VISIBLE)
                            clicked(chipsRows.get(position).getWhites().get(finalI));
                    }
                });
            }

        }
    }

    private void clicked(ChipRow chip) {
        if(chip.getColor() == BROWN) {
            if(chip.getX() < 15) { // brown chip in upper board
                if(callBack_chipProtocol != null) {
                    callBack_chipProtocol.chip(chip, false);
                }
            }
            if(chip.getX() >= 15 && chip.getX() < 32) {
                if(callBack_chipProtocol != null) { // brown chip in bottom bord
                    callBack_chipProtocol.chip(chip, true);
                }
            }
        }
        if(chip.getColor() == WHITE) {
            if(chip.getX() < 15) { // white chip in upper board
                if(callBack_chipProtocol != null) {
                    callBack_chipProtocol.chip(chip, true);
                }
            }
            if(chip.getX() >= 15 && chip.getX() < 32) { // white chip in bottom board
                if(callBack_chipProtocol != null) {
                    callBack_chipProtocol.chip(chip, false);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return chipsRows == null ? 0 : chipsRows.size() ;
    }

    class ChipViewHolder extends RecyclerView.ViewHolder {
        private LinearLayoutCompat gameblock_LAY_chipContainer1;
        private LinearLayoutCompat gameblock_LAY_chipContainer2;

        public ChipViewHolder(View itemView) {
            super(itemView);
            gameblock_LAY_chipContainer1 = itemView.findViewById(R.id.gameblock_LAY_chipContainer1);
            gameblock_LAY_chipContainer2 = itemView.findViewById(R.id.gameblock_LAY_chipContainer2);
        }
    }
}
