package com.huaxin.wangshen.sak;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.huaxin.wangshen.sak.AnalyseAdapter.MultiLevelTestAdapter;
import com.huaxin.wangshen.sak.AnalyseModel.ClassA;
import com.huaxin.wangshen.sak.AnalyseModel.ClassB;
import com.huaxin.wangshen.sak.AnalyseModel.ClassC;
import com.huaxin.wangshen.sak.AnalyseModel.ClassD;
import com.huaxin.wangshen.sak.AnalyseModel.ClassE;
import com.lijianxun.multilevellist.adapter.MultiLevelAdapter;
import com.lijianxun.multilevellist.model.MultiLevelModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebviewActivity1 extends AppCompatActivity {


    //声明引用
    private android.webkit.WebView mWVmhtml;
    private android.webkit.WebView mWVmhtml1;
    List<ClassA> list = new ArrayList<>();
    MultiLevelTestAdapter adapter;
    private ListView check_list;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
//      activity_web_view  check_list = findViewById(R.id.check_list);
        //获取控件对象
        mWVmhtml=(android.webkit.WebView) findViewById(R.id.WV_Id);
        mWVmhtml1=(android.webkit.WebView) findViewById(R.id.WV_Id1);
        //加载本地html文件
        // mWVmhtml.loadUrl("file:///android_asset/hello.html");
        //加载网络URL
        //mWVmhtml.loadUrl("https://blog.csdn.net/qq_36243942/article/details/82187204");
        //设置JavaScrip
        mWVmhtml.getSettings().setJavaScriptEnabled(true);
        //访问百度首页
        mWVmhtml.loadUrl("file:///android_asset/Table.html");
        mWVmhtml.setInitialScale(66);
        //设置在当前WebView继续加载网页

        mWVmhtml1.getSettings().setJavaScriptEnabled(true);
        mWVmhtml1.loadUrl("file:///android_asset/Echarts.html");
        mWVmhtml1.setInitialScale(66);



        ListView analyse_list = findViewById(R.id.analyse_list);
        init_list();
        adapter = new MultiLevelTestAdapter(this, true, false
                , 0);
        adapter.setOnMultiLevelListener(new MultiLevelAdapter.OnMultiLevelListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id
                    , MultiLevelModel current, MultiLevelModel max) {
                Toast.makeText(WebviewActivity1.this, "position = " + position + "" +
                                " , current level = " + current.getLevel() + " , outside level = "
                                + max.getLevel()
                        , Toast.LENGTH_SHORT).show();
//                if (current.getLevel() == 0){
//                    MultiLevelModel children = (MultiLevelModel) current.getChildren().get(0);
//                    children.setExpand(true);
//                }
            }
        });
        analyse_list.setOnItemClickListener(adapter);
        analyse_list.setAdapter(adapter);
        adapter.setList(list);

        ImageView add = findViewById(R.id.add_item);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nums = 4;
                list.clear();
                init_list();
                adapter.setList(list);
            }
        });
    }
    private int nums = 3;
    private void init_list(){
        List<String> list31 = new ArrayList<>();
        list31.add("表头内容");
//        list31.add("开始时间");
//        list31.add("结束时间");

        List<String> list32 = new ArrayList<>();
        list32.add("条件1");
        list32.add("条件2");
        list32.add("条件3");

        List<String> list33 = new ArrayList<>();
        list33.add("字段1");
//        list33.add("字段2");
//        list33.add("字段3");
//        list33.add("字段4");

        List<String> list34 = new ArrayList<>();
        list34.add("设置1");
//        list34.add("设置2");
//        list34.add("设置3");
//        list34.add("设置4");

        List<String> list2 = new ArrayList<>();
        list2.add("通用");
        list2.add("条件");
        list2.add("显示字段");
        list2.add("图表设置");


        int count = 0;
        for (int i = 0; i < nums; i++) {
            ClassA a = new ClassA(i, " 数据源" );
            a.setChildren(new ArrayList());
            list.add(a);
            count += 1;
            Log.e("TAG 1", a.getName());

            for (int j = 0; j < 4; j++) {
                ClassB b = new ClassB(j, list2.get(j) );
                a.getChildren().add(b);
                Log.e("TAG 2", b.getLabel());
                ClassC c = null;
                if (j == 0){
                    for (int k = 0;k<1;k++){
                        c = new ClassC(list31.get(k));
                        b.getChildren().add(c);
                    }
                }
                if (j == 1){
                    for (int k = 0;k<1;k++){
                        c = new ClassC(list32.get(k));
                        b.getChildren().add(c);
                        for (int l = 0;l<3;l++){
                            ClassD d = new ClassD(list32.get(l),list32.get(l));
                            c.getChildren().add(d);
                            for (int m = 0;m<1;m++){
                                ClassE e = new ClassE(list32.get(l),list32.get(l));
                                d.getChildren().add(e);
                            }
                        }
                    }
                }
                if (j == 2){
                    for (int k = 0;k<1;k++){
                        c = new ClassC(list33.get(k));
                        b.getChildren().add(c);
                    }
                }
                if (j == 3){
                    for (int k = 0;k<1;k++){
                        c = new ClassC(list34.get(k));
                        b.getChildren().add(c);
                    }
                }
            }
        }
    }

}
