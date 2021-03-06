package com.hhxplaying.neteasedemo.netease.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TintManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.hhxplaying.neteasedemo.netease.R;
import com.hhxplaying.neteasedemo.netease.bean.newstext.Img;
import com.hhxplaying.neteasedemo.netease.bean.newstext.NewRoot;
import com.hhxplaying.neteasedemo.netease.bean.newstext.NewsID;
import com.hhxplaying.neteasedemo.netease.config.Global;
import com.hhxplaying.neteasedemo.netease.factory.RequestSingletonFactory;
import com.hhxplaying.neteasedemo.netease.util.NeteaseURLParse;
import com.hhxplaying.neteasedemo.netease.util.URLImageParser;
import com.hhxplaying.neteasedemo.netease.vollley.MySingleton;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Created by HHX on 15/9/10.
 */
public class NewsDisplayActivity extends AppCompatActivity {
    private SystemBarTintManager tintManager;
    private Context context;
    private TextView content;
    private TextView title;
    private TextView authorAndTime;
    private String link;
    private final String template = "<p><img src='LINK'/></p>";

    private Document document;  //获取的网页数据
    private boolean hasLoaded = false;  //网页数据是否获取完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_news_display);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(" ");
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
        }


        String html = "Hello \n" +
                "<img src='http://ww1.sinaimg.cn/mw600/4dc7b570jw1drn1o8mrp0j.jpg' />" +
                " This is a test \n" +
                "<img src='http://att.bbs.duowan.com/forum/201311/01/0950172al0qkazlh20hh9n.png'/>";

        String htmlTest = "<p>　　在众多安卓手机中，Nexus系列一贯被视为Google的“亲儿子”，但其实只有设计来自Google，代工生产还是交给其他厂商，包括LG、HTC、三星、华为、摩托罗拉等等。<\\/p>" +
                "<p>　　不过有传闻称，Google打算完全自己玩儿了，因为一则iPhone在高端市场上不断蚕食市场份额，二则Nexus现在本身的表现也越来越不好：销售渠道过于狭窄，缺乏运营商合作，新的Nexus 6P/5X定位太高影响销售……<\\/p>" +
                "<p>　　Google CEO Sundar Pichai已经向员工和一些外部人士透露，计划将Nexus系列完全掌控在自己手中，从设计到生产都一手负责，不再依赖其他手机厂商，就像Pixel C笔记本那样变成纯粹的Google产品。<\\/p>" +
                "<p>　　这样一来，Nexus设备也不会再冠以其他厂商的牌子，只打Google自己的标识。<\\/p>" +
                "<p>　　虽然Google没有透露该计划的具体细节和执行时间，但是据了解，HTC内部人士对于Google的这种做法并不意外，HTC也可能成为最后一个代工Nexus的第三方厂商。<\\/p>" +
                "<p>　　此前有消息称，HTC今年将独自代工两款Nexus手机，分别为5.0英寸、5.5英寸。<\\/p><!--IMG#0-->";

        String body = htmlTest.replace("<!--IMG#0-->", template.replace("LINK", "http://img1.cache.netease.com/catchpic/5/59/59F9EB30B047D22DAD5F12B14DB4682E.jpg"));


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getWebNewsContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fr.castorflex.android.circularprogressbar.CircularProgressBar circularProgressBar
                                    = (fr.castorflex.android.circularprogressbar.CircularProgressBar)
                                    findViewById(R.id.progressbar_loading_content);
                            circularProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }).start();
//        WebView webView = (WebView)findViewById(R.id.news_content_wv);
//        webView.loadUrl(newsContentUrl);
//        content = (TextView)findViewById(R.id.tv_content);
//        title = (TextView)findViewById(R.id.tv_newstitle);
//        authorAndTime = (TextView)findViewById(R.id.tv_author_time);
//
////        URLImageParser p = new URLImageParser(content, this);
////        Spanned htmlSpan = Html.fromHtml(body, p, null);
////        content.setText(htmlSpan);
//
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//           link = extras.getString("NEWS_LINK");
//        }
//        getNews(link);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ScrollView scrollView;  //界面的scrollview组件
    public void getWebNewsContent() throws IOException {
        //FIXME 获取新闻的具体内容的URL 地址，显示新闻内容,使用JAVA 根据内容动态编写界面
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        //判断需要显示的内容是否是乳制品百科
        if(type != null && type.equals("baike")){
            getBaikeNewsContent(intent.getStringExtra("NEWS_CONTENT_URL"));
            return;
        }

        final String newsContentUrl = intent.getStringExtra("NEWS_CONTENT_URL");      //需要打开的新闻界面
        scrollView = (ScrollView)findViewById(R.id.news_content_root_scv);   //新闻界面scrollview作为rootparent
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        Connection connection = Jsoup.connect(newsContentUrl);
        connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
        document = connection.get();
//        document = Jsoup.connect(newsContentUrl).get();
        Elements mainContentsEles = document.getElementsByClass("articlecontent");  //div标签，网页中主要的新闻内容
        final Elements title = mainContentsEles.select("h3");     //标题
        final Elements articleInfo = document.getElementsByClass("info");
        Log.i("新闻标题",title.text());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = new TextView(NewsDisplayActivity.this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(30);
                textView.setTextColor(Color.BLACK);
                textView.setTextAlignment(ViewGroup.TEXT_ALIGNMENT_CENTER);
                linearLayout.addView(textView);
                textView.setText(title.text());
                //新闻时间与消息来源
                android.support.v7.widget.AppCompatTextView textViewInfo = new
                        android.support.v7.widget.AppCompatTextView(NewsDisplayActivity.this);
                textViewInfo.setTextSize(14);
                textViewInfo.setTextColor(Color.BLACK);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textViewInfo.setBackgroundDrawable(NewsDisplayActivity.this.getResources().getDrawable(R.drawable.actionbar_item));
                String newsInfo = articleInfo.get(0).text();
                int indexEnd = newsInfo.indexOf("【");
                newsInfo = newsInfo.substring(0,indexEnd);
                textViewInfo.setText(newsInfo);

                Log.i("新闻消息来源与时间：",articleInfo.get(0).text());
                linearLayout.addView(textViewInfo);
                //添加space分隔
                android.support.v4.widget.Space space = new android.support.v4.widget.Space(NewsDisplayActivity.this);
                space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                                ,20));
                linearLayout.addView(space);
            }
        });

        //依次添加所有可能的text与img
        final Elements content = document.getElementsByAttributeValue("id","MyContent");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.i("主要内容",content.html());
//                Log.i("content size =","----------------"+content.size());

                int i=0;
                for(Element ele : content.select("div")){
//                    Log.i("标签",ele.tagName());
//                    Log.i("标签内容",ele.html());
                    if(ele.html().contains("<img src=")){
                        i++;
                        if(i == 1){
                            continue;
                        }
                        Log.i("图片链接所在的html",ele.html());
                        Log.i("图片链接","---------------" +
                                ""+ele.select("img").attr("src"));
                        ImageView imageView = new ImageView(NewsDisplayActivity.this);
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        String imgpath = ele.select("img").attr("src");
                        if(imgpath != ""){
                            Glide.with(NewsDisplayActivity.this).load(imgpath).
                                    into(imageView);
                            Log.i("显示图片，图片的path is :",imgpath);
                        }
                        else{
                            Log.i("显示img path null","没有图片可以显示");
                            imageView.setImageResource(R.drawable.news_placeholder);
                        }

                        linearLayout.addView(imageView);
                    }else{
                        TextView textView = new TextView(NewsDisplayActivity.this);
                        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        textView.setTextSize(18);
                        textView.setTextColor(Color.BLACK);
                        linearLayout.addView(textView);
                        textView.setText(ele.text()+"\n");
//                        Log.i("获取到的网页文本：",textView.getText().toString());
                    }
//                    if(ele.is("div") && ele.attr("style") != null){
//                        //添加textView
//                        TextView textView = new TextView(NewsDisplayActivity.this);
//                        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT));
//                        textView.setTextSize(30);
//                        textView.setTextColor(Color.BLACK);
//                        scrollView.addView(textView);
//                        textView.setText(ele.text().replaceAll("&nbsp;","")
//                                .replaceAll("&rdquo;","")
//                                .replaceAll("&mdash;","")
//                                .replaceAll("&ldquo;",""));
//                        Log.i("获取到的网页文本：",textView.getText().toString());
//                    }else{
//                        ImageView imageView = new ImageView(NewsDisplayActivity.this);
//                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT));
//                        String imgpath = ele.select("img").attr("src");
//                        Glide.with(NewsDisplayActivity.this).load(imgpath).into(imageView);
//                        scrollView.addView(imageView);
//                    }
                }
            }
        });
    }

    /**
     * 乳制品百科具体内容的网址
     * @param contentUrl
     */
    public void getBaikeNewsContent(String contentUrl) throws IOException {
        scrollView = (ScrollView)findViewById(R.id.news_content_root_scv);  //乳制品百科界面
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) );
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        Connection connection = Jsoup.connect(contentUrl);      //连接具体信息的网址
        connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
        Document document = connection.get();

        Elements titleElement = document.getElementsByTag("h1"); //获取标题、来源等信息
        final String title = titleElement.get(1).text();
        Log.i("乳制品百科" , "获取到的标题为：" + title);
        String contentInfo = document.getElementsByClass("newsTime01").get(0).text();   //文章的其他属性信息，如:时间
        Log.i("乳制品百科","获取的文章的其他信息：" + contentInfo);
        final String contentTime = contentInfo.substring(0,contentInfo.indexOf("阅")); //发表时间
        final String source = contentInfo.substring(contentInfo.indexOf("来源："),contentInfo.indexOf("- 豆乳"));

        final Elements mainContentEle = document.getElementsByClass("div_text").get(0).children();  //新闻的主内容
        Log.i("乳制品百科","获取到的文章的主要内容：" + mainContentEle.html());

        //构建界面
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = new TextView(NewsDisplayActivity.this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(30);
                textView.setTextColor(Color.BLACK);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                linearLayout.addView(textView);
                textView.setText(title);    //插入标题

                TextView textViewInfo = new TextView(NewsDisplayActivity.this);
                textViewInfo.setTextSize(14);
                textViewInfo.setTextColor(Color.BLACK);
                textViewInfo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textViewInfo.setBackgroundDrawable(NewsDisplayActivity.this.getResources().getDrawable(R.drawable.actionbar_item));
                textViewInfo.setText("发表时间:" + contentTime + "   " + source);
                linearLayout.addView(textViewInfo);
                //添加space 分割
                Space space = new Space(NewsDisplayActivity.this);
                space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,20));
                linearLayout.addView(space);
                //依次添加所有的可能的内容
                for(Element element : mainContentEle){
                    Log.i("乳制品百科","当前标签{" + element.tagName());
                    Log.i("乳制品百科","标签中的内容{" + element.html());
                    if(element.html().contains("<img src=")){
                        Log.i("乳制品百科","图片连接地址：[" + element.select("img").attr("src"));
                        ImageView imageView = new ImageView(NewsDisplayActivity.this);
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        linearLayout.addView(imageView);
                        String imgPath = element.select("img").attr("src");
                        if(!imgPath.trim().equals("")){
                            Glide.with(NewsDisplayActivity.this).load(imgPath).into(imageView);

                        }
                    }else{      //显示文字
                        TextView textView1 = new TextView(NewsDisplayActivity.this);
                        textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        linearLayout.addView(textView1);
                        textView1.setTextSize(18);
                        if(element.html().contains("b")){
                            textView1.setTextSize(20);
                            textView1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        }
                        textView1.setTextColor(Color.BLACK);
                        textView1.setText(element.text()+"\n");
                    }
                }


            }
        });
    }

    private void getNews(final String link) {
        MySingleton.getInstance(context.getApplicationContext()).getRequestQueue().add(
                RequestSingletonFactory.getInstance().getGETStringRequest(context, link,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                JSONObject obj;
                                try {
                                    String id = NeteaseURLParse.getNewsID(link);
                                    String hold = response.toString().replace(id, "newsID");
                                    obj = new JSONObject(hold.toString());

                                    NewRoot newRoot = new Gson().fromJson(obj.toString(), Global.NewRoot);

                                    Log.i("RVA", "response: " + response.toString());
                                    Log.i("RVA", "newRoot: " + newRoot.toString());

                                    updateViewFromJSON(newRoot);

                                } catch (JSONException | JsonParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }));

    }


    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.tab_top_background));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateViewFromJSON(NewRoot newRoot) {
        NewsID hold = newRoot.getNewsID();
        //设置标题
        title.setText(hold.getTitle());

        //设置作者和时间
        int first = hold.getPtime().indexOf("-");
        int last = hold.getPtime().lastIndexOf(":");
        authorAndTime.setText(hold.getSource() + "    " + hold.getPtime().substring(first + 1, last));

        //设置正文
        String body = hold.getBody();
        for (Img img : hold.getImg()) {
            body = body.replace(img.getRef(), template.replace("LINK", img.getSrc()));
        }

        Log.i("RVA", "设置body： " + body);
        URLImageParser p = new URLImageParser(content, this);
        Spanned htmlSpan = Html.fromHtml(body, p, null);
        content.setText(htmlSpan);
        content.setTextSize(18);


    }
}
