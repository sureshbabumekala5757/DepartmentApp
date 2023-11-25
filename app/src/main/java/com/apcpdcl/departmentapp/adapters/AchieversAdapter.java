package com.apcpdcl.departmentapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.customviews.ZoomableImageView;
import com.apcpdcl.departmentapp.model.AchieversModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;

/**
 * Created by Admin on 23-02-2018.
 */

public class AchieversAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<AchieversModel> achieversModels;

    public AchieversAdapter(Context context, ArrayList<AchieversModel> achieversModels) {
        mContext = context;
        this.achieversModels = achieversModels;
        inflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder {
        TextView tv_name;
        TextView tv_duration;
        ImageView civ_achiever;
        LinearLayout ll_main;
        ProgressBar pb_image;
    }

    @Override
    public int getCount() {
        return achieversModels.size();
    }

    @Override
    public AchieversModel getItem(int position) {
        return achieversModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        final AchieversAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_achievers_layout, parent, false);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            holder.civ_achiever = (ImageView) view.findViewById(R.id.civ_achiever);
            holder.ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
            holder.pb_image = (ProgressBar) view.findViewById(R.id.pb_image);
            view.setTag(holder);
        } else {
            holder = (AchieversAdapter.ViewHolder) view.getTag();
        }
        /*int color = Utility.randomColor(position+ 150);
        holder.ll_main.setBackgroundColor(color);*/
        holder.tv_name.setText(achieversModels.get(position).getCONTENT());
       // holder.tv_duration.setText(achieversModels.get(position).getDURATION());
        Utility.universalImageLoaderPicLoading(holder.civ_achiever, achieversModels.get(position).getIMAGES(),
                holder.pb_image, R.drawable.user_pic_place_holder);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(mContext,achieversModels.get(position).getCONTENT(),achieversModels.get(position).getIMAGES());
            }
        });
        return view;
    }

    private   void showImageDialog(Context context, String message, String imageUrl) {
        if (!Constants.isDialogOpen) {
            Constants.isDialogOpen = true;
            final Dialog dialog_confirm = new Dialog(context);
            dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_confirm.setContentView(R.layout.dialog_achievers);
            WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            Window window1 = dialog_confirm.getWindow();
            if (window1 != null)
                lp1.copyFrom(window1.getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            assert window1 != null;
            window1.setAttributes(lp1);
            Button btn_ok = (Button) dialog_confirm.findViewById(R.id.btn_ok);

            TextView txt_msz = (TextView) dialog_confirm.findViewById(R.id.txt_heading);
            ZoomableImageView civ_achiever = (ZoomableImageView) dialog_confirm.findViewById(R.id.civ_achiever);
            ProgressBar pb_image = (ProgressBar) dialog_confirm.findViewById(R.id.pb_image);
            Utility.universalImageLoaderPicLoading(civ_achiever, imageUrl,
                    pb_image, R.drawable.user_pic_place_holder);
            txt_msz.setText(message);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_confirm.dismiss();
                }
            });
            dialog_confirm.show();
            dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Constants.isDialogOpen = false;
                }
            });
        }
    }
}
