package com.apcpdcl.departmentapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.activities.CheckReadingActivity;
import com.apcpdcl.departmentapp.activities.ComplaintsListActivity;
import com.apcpdcl.departmentapp.activities.DTRTrackingActivity;
import com.apcpdcl.departmentapp.activities.GeoDashBoardActivity;
import com.apcpdcl.departmentapp.activities.Home;
import com.apcpdcl.departmentapp.activities.LMRegistrationListActivity;
import com.apcpdcl.departmentapp.activities.MeterChangeReqListActivity;
import com.apcpdcl.departmentapp.activities.MeterExceptionListActivity;
import com.apcpdcl.departmentapp.activities.PendingDTRComplaintsActivity;
import com.apcpdcl.departmentapp.activities.ReconnectionList;
import com.apcpdcl.departmentapp.activities.ServiceDetailsActivity;
import com.apcpdcl.departmentapp.activities.WebViewActivity;
import com.apcpdcl.departmentapp.models.DashBoard;
import com.apcpdcl.departmentapp.shared.AppPrefs;

import java.util.ArrayList;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.MyViewHolder> {
    private OnItemClick listener;

    public interface OnItemClick {
        void onClick(ArrayList<DashBoard> board);
    }

//    public interface OnItemClick {
//        void onClick(ArrayList<String> title);
//    }

    private ArrayList<DashBoard> items;
    //    private ArrayList<String> title;
//    private ArrayList<String> imageUrl;
    private Context context;

    public DashBoardAdapter(ArrayList<DashBoard> boardArrayList, OnItemClick myListener, Context context) {

        this.items = boardArrayList;
//        this.title = title;
//        this.imageUrl = imageUrl;
        this.listener = myListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup prent, int i) {
        View v = LayoutInflater.from(prent.getContext()).inflate(R.layout.modulue_layout, prent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") int position) {
//
        DashBoard board = items.get(position);
        myViewHolder.tvTitle.setText(board.getTitle());
//        myViewHolder.ivLogo.setImageResource(board.getImageId());
//        myViewHolder.ivLogo.setImageResource();

//        String myTitle = title.get(position);
//        myViewHolder.tvTitle.setText(myTitle);
//
//        String myImage = imageUrl.get(position);
        String myImage = board.getImageUrl();

        Resources res = myViewHolder.itemView.getContext().getResources();
        int result = res.getIdentifier("@drawable/" + myImage, null, context.getPackageName());

        myViewHolder.ivLogo.setImageResource(result);


        myViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String selectedItem = items.get(position).getTitle();
                String loginUser = AppPrefs.getInstance(context).getString("DESIG", "");
                if (loginUser.contains("SS"))
                    loginUser = "SS";
                if (loginUser.contains("LM") || loginUser.contains("LI") || loginUser.contains("ALM"))
                    loginUser = "LM";
                if (loginUser.contains("EA"))
                    loginUser = "EA";
                if (loginUser.contains("ADE") || loginUser.contains("DEE"))
                    loginUser = "ADE";
                if (loginUser.contains("AE"))
                    loginUser = "AE";
                if ((selectedItem.equalsIgnoreCase("DC \n Operations") && (loginUser.contains("LM") || loginUser.contains("EA") || loginUser.contains("AE") || loginUser.contains("ADE"))) ) {
                    Intent in = new Intent(v.getContext(), Home.class);
                    v.getContext().startActivity(in);
                } else if((selectedItem.equalsIgnoreCase("Re-Connection \n") && (loginUser.contains("LM") || loginUser.contains("EA") || loginUser.contains("AE") || loginUser.contains("ADE"))) ){
                    Intent in = new Intent(v.getContext(), ReconnectionList.class);
                    v.getContext().startActivity(in);
                }
                else if ((selectedItem.equalsIgnoreCase("Meter Change Requests") && (loginUser.contains("LM") || loginUser.contains("EA") || loginUser.contains("AE") || loginUser.contains("ADE"))) ) {
                    //Intent in = new Intent(v.getContext(), LMMeterChangeDashBoardActivity.class);
                    Intent in = new Intent(v.getContext(), MeterChangeReqListActivity.class);
                    v.getContext().startActivity(in);
                } else if (selectedItem.equalsIgnoreCase("Consumer Mobile Updation") && (loginUser.contains("LM") || loginUser.contains("EA"))) {
//                    Intent in = new Intent(v.getContext(), AadharMobileActivity.class);
                    Intent in = new Intent(v.getContext(), WebViewActivity.class);
                    v.getContext().startActivity(in);
                } else if ((selectedItem.equalsIgnoreCase("Check Reading \n") && (loginUser.contains("LM") || loginUser.contains("EA") || loginUser.contains("AE") || loginUser.contains("ADE"))) ) {
                    Intent in = new Intent(v.getContext(), CheckReadingActivity.class);
                    v.getContext().startActivity(in);
                } else if ((selectedItem.equalsIgnoreCase("New Connection \n Release") && (loginUser.contains("LM") || loginUser.contains("EA") || loginUser.contains("AE") || loginUser.contains("ADE"))) ) {
//                    Intent in = new Intent(v.getContext(), NewConnectionDashBoardActivity.class);
                    Intent in = new Intent(v.getContext(), LMRegistrationListActivity.class);
                    v.getContext().startActivity(in);
                } else if ((selectedItem.equalsIgnoreCase("CCC \n Complaints") && (loginUser.contains("LM") || loginUser.contains("EA") || loginUser.contains("AE") || loginUser.contains("ADE"))) ) {
                    Intent in = new Intent(v.getContext(), ComplaintsListActivity.class);
                    v.getContext().startActivity(in);
                } else if ((selectedItem.equalsIgnoreCase("AGL Services GEO Tagging") &&  (loginUser.contains("LM") || loginUser.contains("EA") || loginUser.contains("AE") || loginUser.contains("ADE"))) ) {
                    Intent in = new Intent(v.getContext(), GeoDashBoardActivity.class);
                    v.getContext().startActivity(in);
                } else if ((selectedItem.equalsIgnoreCase("Service Details \n") && (loginUser.contains("LM") || loginUser.contains("EA") || loginUser.contains("AE") || loginUser.contains("ADE"))) ) {
                    Intent in = new Intent(v.getContext(), ServiceDetailsActivity.class);
                    v.getContext().startActivity(in);
                } else if (selectedItem.equalsIgnoreCase("DTR Tracking \n") && (loginUser.contains("AE") || loginUser.contains("ADE")) ) {
                    Intent in = new Intent(v.getContext(), DTRTrackingActivity.class);
                    v.getContext().startActivity(in);
                } else if (selectedItem.equalsIgnoreCase("DTR Tracking Photos Upload") && (loginUser.contains("AE") || loginUser.contains("ADE")) ) {
                    Intent in = new Intent(v.getContext(), PendingDTRComplaintsActivity.class);
//                    Intent in = new Intent(v.getContext(), WebViewActivity.class);
                    v.getContext().startActivity(in);
                } else if (selectedItem.equalsIgnoreCase("Feeder Operations \n") && loginUser.contains("SS")) {
                    Intent in = new Intent(v.getContext(), WebViewActivity.class);
                    v.getContext().startActivity(in);
                } else if (selectedItem.equalsIgnoreCase("Service Details \n") && loginUser.contains("SS")) {
                    Intent in = new Intent(v.getContext(), ServiceDetailsActivity.class);
                    v.getContext().startActivity(in);
                }
//                listener.onClick(items.get(position));
//                listener.onClick(title.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivLogo;
        public TextView tvTitle;
        public RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            layout = itemView.findViewById(R.id.mainLayout);
        }
    }
}