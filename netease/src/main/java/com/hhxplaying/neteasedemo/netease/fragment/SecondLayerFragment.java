package com.hhxplaying.neteasedemo.netease.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.hhxplaying.neteasedemo.netease.R;
import com.hhxplaying.neteasedemo.netease.adapter.NormalRecyclerViewAdapter;
import com.hhxplaying.neteasedemo.netease.bean.OneNewsItemBean;
import com.hhxplaying.neteasedemo.netease.config.Global;
import com.hhxplaying.neteasedemo.netease.config.URLs;
import com.hhxplaying.neteasedemo.netease.factory.RequestSingletonFactory;
import com.hhxplaying.neteasedemo.netease.vollley.MySingleton;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SecondLayerFragment extends LazyFragment {
	public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
	public static final String INTENT_INT_POSITION = "intent_int_position";
	private String tabName;
	private int position;
	private TextView textView;
    private ArrayList<OneNewsItemBean> mOneNewsItemList = new ArrayList<>();
	private NormalRecyclerViewAdapter normalRecyclerViewAdapter;

    private RecyclerView mRecyclerView;
	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		tabName = getArguments().getString(INTENT_STRING_TABNAME);
		position = getArguments().getInt(INTENT_INT_POSITION);
		//临时处理下
		position = position % 5;

		setContentView(R.layout.fragment_tabmain_item);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于listview
		normalRecyclerViewAdapter = new NormalRecyclerViewAdapter(getActivity(), mOneNewsItemList, mRecyclerView);
        mRecyclerView.setAdapter(normalRecyclerViewAdapter);
	}


	@Override
	protected void onResumeLazy() {
		super.onResumeLazy();
		getIndexNews();
	}

	@Override
	protected void onDestroyViewLazy() {
		super.onDestroyViewLazy();
	}


//	private void getIndexNews() {
//        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue().add(
//                RequestSingletonFactory.getInstance().getGETStringRequest(getActivity(), URLs.getUrl(tabName), new Response.Listener() {
//                    @Override
//                    public void onResponse(Object response) {
//                        JSONObject obj;
//                        try {
//                            mOneNewsItemList.clear();
//                            obj = new JSONObject(response.toString());
//                            JSONArray itemArray = obj.getJSONArray(URLs.getUrlTag(tabName));
//                            ArrayList<OneNewsItemBean> newsList = new Gson().fromJson(itemArray.toString(), Global.NewsItemType);
//                            mOneNewsItemList.addAll(newsList);
//							normalRecyclerViewAdapter.notifyDataSetChanged();
//						} catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }));
//	}

    private ArrayList<OneNewsItemBean> newsList  = new ArrayList<>();
	//FIXME
	private void getIndexNews(){

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					newsList = getDataFromWebSite();
					mOneNewsItemList.clear();
					mOneNewsItemList.addAll(newsList);
					getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            normalRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	//FIXME
	private ArrayList<OneNewsItemBean> getDataFromWebSite() throws IOException {
	    ArrayList<OneNewsItemBean> newsList = new ArrayList<>();
	    OneNewsItemBean itemBean = null;

		Document document = Jsoup.connect("http://www.chinadairy.net/").get();
		Elements elements = document.getElementsByClass("pic");
		for(Element ele : elements.get(0).children().select("a")){
		    itemBean = new OneNewsItemBean();
			Log.i("my_get_message:",ele.attr("title"));
			Log.i("my_get_imgsrc:",ele.select("img").attr("src"));
            itemBean.setHasImg(1);
            itemBean.setTitle(ele.attr("title"));
            itemBean.setImgsrc(ele.select("img").attr("src"));
            newsList.add(itemBean);
		}

		return newsList;
	}

}
