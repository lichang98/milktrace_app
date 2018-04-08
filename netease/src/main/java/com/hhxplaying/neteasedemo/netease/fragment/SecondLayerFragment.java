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
import com.hhxplaying.neteasedemo.netease.bean.Ads;
import com.hhxplaying.neteasedemo.netease.bean.Imgextra;
import com.hhxplaying.neteasedemo.netease.bean.OneNewsItemBean;
import com.hhxplaying.neteasedemo.netease.config.Global;
import com.hhxplaying.neteasedemo.netease.config.URLs;
import com.hhxplaying.neteasedemo.netease.factory.RequestSingletonFactory;
import com.hhxplaying.neteasedemo.netease.vollley.MySingleton;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
					switch(tabName){
						case "头条":
							newsList = getDataFromWebSite();
							break;
						case "科技":
							newsList = getMainNewsFromWeb();
							break;
						default:
							break;
					}
//					newsList = getDataFromWebSite();
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
	//乳制品新闻头条
	private ArrayList<OneNewsItemBean> getDataFromWebSite() throws IOException {
		//FIXME 根据tabname 选择载入不同的新闻
	    ArrayList<OneNewsItemBean> newsList = new ArrayList<>();
	    OneNewsItemBean itemBean = null;

	    Connection connection = Jsoup.connect("http://www.chinadairy.net/");
	    connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
	    Document document = connection.get();
//		Document document = Jsoup.connect("http://www.chinadairy.net/").get();
		Elements elements = document.getElementsByClass("pic");

		int i=0;
		for(Element ele : elements.get(0).children().select("a")){
			Log.i("my_get_message:",ele.attr("title"));
			Log.i("my_get_imgsrc:",ele.select("img").attr("src"));
			Log.i("my_get_subtitle:",ele.select("img").attr("text"));
			itemBean = new OneNewsItemBean();
			itemBean.setUrl(ele.attr("href"));	//超链接的地址，链接新闻详情界面
			itemBean.setTitle(ele.attr("title"));
			itemBean.setDigest(ele.select("img").attr("text"));
			itemBean.setImgsrc(ele.select("img").attr("src"));
			itemBean.setOrder(++i);
			newsList.add(itemBean);
		}
		return newsList;
	}

	/**
	 * 每日要闻
	 */
	private ArrayList<OneNewsItemBean> getMainNewsFromWeb() throws IOException {
		ArrayList<OneNewsItemBean> newsList = new ArrayList<>();
		OneNewsItemBean itemBean = null;

		Connection connection = Jsoup.connect("http://www.chinadairy.net/Item/list.asp?id=690");
		connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
		Document document = connection.get();
//		Document document = Jsoup.connect("http://www.chinadairy.net/Item/list.asp?id=690").get();

		Elements elements = document.getElementsByClass("nl_con1 clearfix");
		int i=0;

		for(Element ele : elements){
			Element content = ele.getElementsByTag("a").get(0);	//标题与链接在该标签中
			String title = content.attr("title");
			String url = content.attr("href");
			Log.i("获取到的新闻标题：",title);
			Log.i("获取到的新闻具体内容URL：",url);
			itemBean = new OneNewsItemBean();
			itemBean.setTitle(title);
			itemBean.setUrl(url);
			//提前获取主要内容中的一张图片用于显示在列表中
			Document documentInner = Jsoup.connect(url).get();
			Elements innerContent = documentInner.getElementsByAttributeValue("id","MyContent")
					.select("img");
			itemBean.setImgsrc(innerContent.attr("src"));
			Log.i("预加载的图片的URL ：",innerContent.attr("src"));
			itemBean.setOrder(2);

			newsList.add(itemBean);
		}

		return newsList;
	}

}
