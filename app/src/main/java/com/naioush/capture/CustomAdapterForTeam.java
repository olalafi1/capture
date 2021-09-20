package com.naioush.capture;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class CustomAdapterForTeam extends RecyclerView.Adapter<CustomAdapterForTeam.ViewHolderList> {

    private String[] localDataSet;
    List<Team> team;
Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolderList extends RecyclerView.ViewHolder {
        TextView teamName,memberNum;
        public ViewHolderList(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            teamName=view.findViewById(R.id.teamname);
            memberNum=view.findViewById(R.id.MemberNum);

        }
        public void setUserData(String teamName,String MemberNum)  {
            Log.e("DATA",teamName+" "+MemberNum);
            this.teamName.setText(teamName);
            this.memberNum.setText(MemberNum);



        }

    }

    public CustomAdapterForTeam(Context context, List<Team> team) {
        this.context=context;
        this.team = team;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolderList onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.teamview, viewGroup, false);

        return new ViewHolderList(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolderList viewHolder, final int position) {
viewHolder.setUserData(team.get(position).name,team.get(position).num);



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return team.size();
    }
}
