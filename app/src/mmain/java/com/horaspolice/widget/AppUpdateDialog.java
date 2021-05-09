package com.horaspolice.widget;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.horaspolice.R;
import com.horaspolice.databinding.ActivityInformationsAppBinding;
import com.horaspolice.databinding.ViewAppUpdateBinding;

public class AppUpdateDialog extends Dialog {

    Context context;

    String title;
    String description;
    private boolean isCancelable;
    private ViewAppUpdateBinding binding;


    public AppUpdateDialog(Context context, String title, String description, boolean isCancelable) {
        super(context);
        // TODO Day selector
        this.context = context;
        this.title = title;
        this.description = description;
        this.isCancelable = isCancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ViewAppUpdateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



        if (isCancelable)
            binding.ivClose.setVisibility(View.VISIBLE);
        else
            binding.ivClose.setVisibility(View.GONE);

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        if(!TextUtils.isEmpty(title))
            binding.tvTitle.setText(title);
        else
            binding.tvTitle.setVisibility(View.GONE);


        if(!TextUtils.isEmpty(description))
            binding.tvMessage.setText(String.format(description));
        else
            binding.tvMessage.setVisibility(View.GONE);

        binding.tvUpdateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getPackageName() from Context or Activity object
                final String appPackageName = context.getPackageName();

                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException activityNotFoundException) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

    }

}

