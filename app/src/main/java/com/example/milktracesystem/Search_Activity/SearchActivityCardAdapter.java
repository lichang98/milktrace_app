package com.example.milktracesystem.Search_Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milktracesystem.Camera.SmallCaptureActivity;
import com.example.milktracesystem.Factory_Acticity.FactoryActivity;
import com.example.milktracesystem.R;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;


/**
 * Created by 李畅 on 2017/12/17.
 * 该类作为searchActivity 中卡片布局的适配器
 */

public class SearchActivityCardAdapter extends RecyclerView.Adapter<SearchActivityCardAdapter.SearchCardViewItemViewHolder>{

    private List<SearchActivityCardItem> items;
    private Context context;

    public SearchActivityCardAdapter(List<SearchActivityCardItem> items,Context context){

        this.items = items;
        this.context = context;
    }

    //自定义ViewHolder类
    static class SearchCardViewItemViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textView;
        ImageView imageView;

        public SearchCardViewItemViewHolder(final View itemView){

            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.search_card_view);
            textView = (TextView)itemView.findViewById(R.id.search_card_view_text);
            imageView = (ImageView)itemView.findViewById(R.id.search_card_view_image);
            textView.setBackgroundColor(Color.argb(20,0,0,0));

        }
    }


    @Override
    public SearchCardViewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_activity_carditem,parent,false);
        SearchCardViewItemViewHolder searchCardViewItemViewHolder = new SearchCardViewItemViewHolder(view);
        return searchCardViewItemViewHolder;
    }

    /**
     * 绑定
     * 在此方法内设置组件的点击事件
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(SearchCardViewItemViewHolder holder, final int position) {

        holder.textView.setText(items.get(position).getText());
        holder.imageView.setImageResource(items.get(position).getImgId());
        //对卡片的整体设置点击事件监听
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (position){
                    //扫描二维码
                    case 0:
                        Toast.makeText(context, "点击扫描", Toast.LENGTH_SHORT).show();
                        IntentIntegrator integrator = new IntentIntegrator((Activity)context);
                        integrator.setOrientationLocked(false);     //根据seneor 调整方向
                        integrator.setCaptureActivity(SmallCaptureActivity.class);
                        integrator.initiateScan();
                        break;
                    case 1:         //处理短信查询
                        android.support.v7.app.AlertDialog.Builder builder =
                                new android.support.v7.app.AlertDialog.Builder(context);
                        final android.support.v7.app.AlertDialog alertDialog = builder
                                .setTitle("提示")
                                .setMessage("是否发送短信?")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Uri uri = Uri.parse("smsto:"+12315);
                                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                        context.startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .create();
                        alertDialog.show();

                        break;
                    case 2:     //处理电话查询
                        android.support.v7.app.AlertDialog.Builder builder1 =
                                new android.support.v7.app.AlertDialog.Builder(context);
                        android.support.v7.app.AlertDialog alertDialog1 = builder1
                                .setTitle("提示")
                                .setMessage("是否拨打查询电话")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Uri uri = Uri.parse("tel:"+12315);
                                        Intent intent  = new Intent(Intent.ACTION_DIAL,uri);
                                        context.startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create();
                        alertDialog1.show();
                        break;

                        //FIXME 将企业信息展示移动到主界面中
//                    case 1:
//                        Toast.makeText(context, "点击搜索", Toast.LENGTH_SHORT).show();
//                        //跳转至FactoryActivity ，浏览企业信息
//                        Intent intent = new Intent((Activity)context, FactoryActivity.class);
//                        ((Activity)context).startActivity(intent);
//                        break;
//                    case 2:     //连接蓝牙BLE 模块获取数据
//                        Toast.makeText(context, "点击连接BLE 蓝牙模块", Toast.LENGTH_SHORT).show();
//                        Intent intent1 = new Intent((Activity)context,BleModuleLink.class);
//                        ((Activity)context).startActivity(intent1);
//                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
