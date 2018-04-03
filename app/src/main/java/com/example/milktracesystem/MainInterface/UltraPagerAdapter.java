package com.example.milktracesystem.MainInterface;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.milktracesystem.R;

public class UltraPagerAdapter extends PagerAdapter {
    private boolean isMultiScr;

    public UltraPagerAdapter(boolean isMultiScr) {
        this.isMultiScr = isMultiScr;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view= (LinearLayout)object;
        container.removeView(view);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(container.getContext())
                .inflate(R.layout.layout_ultraview_child,null);
        ImageView imageView = (ImageView)linearLayout.findViewById(R.id.pager_imgview);
        linearLayout.setId(R.id.item_id);
        switch(position){
            case 0:
                imageView.setImageDrawable(linearLayout.getContext().getResources().getDrawable(R.drawable.showmilk1));
                break;
            case 1:
                imageView.setImageDrawable(linearLayout.getContext().getResources().getDrawable(R.drawable.showmilk2));
                break;
            case 2:
                imageView.setImageDrawable(linearLayout.getContext().getResources().getDrawable(R.drawable.showmilk3));
                break;

        }
        container.addView(linearLayout);
        return linearLayout;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
