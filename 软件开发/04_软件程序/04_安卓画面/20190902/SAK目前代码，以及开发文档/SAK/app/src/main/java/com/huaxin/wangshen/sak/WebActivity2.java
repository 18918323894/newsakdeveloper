package com.huaxin.wangshen.sak;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.huaxin.wangshen.sak.AnalyseAdapter.MultiLevelTestAdapter;
import com.huaxin.wangshen.sak.AnalyseModel.ClassA;
import com.huaxin.wangshen.sak.AnalyseModel.ClassB;
import com.huaxin.wangshen.sak.AnalyseModel.ClassC;
import com.lijianxun.multilevellist.adapter.MultiLevelAdapter;
import com.lijianxun.multilevellist.model.MultiLevelModel;

import java.util.ArrayList;
import java.util.List;

public class WebActivity2 extends AppCompatActivity {
    private android.webkit.WebView mWVmhtml;
    List<ClassA> list = new ArrayList<>();
    MultiLevelTestAdapter adapter;
    private ListView check_list;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web2);
        mWVmhtml = findViewById(R.id.mWVmhtml);
        //获取控件对象
        //加载本地html文件
        // mWVmhtml.loadUrl("file:///android_asset/hello.html");
        //加载网络URL
        //mWVmhtml.loadUrl("https://blog.csdn.net/qq_36243942/article/details/82187204");
        //设置JavaScrip
        mWVmhtml.getSettings().setJavaScriptEnabled(true);
        //访问百度首页
        mWVmhtml.loadUrl("file:///android_asset/TableCompare.html");
        mWVmhtml.setInitialScale(66);
        //设置在当前WebView继续加载网页
        ListView analyse_list = findViewById(R.id.analyse_list);


        List<String> list31 = new ArrayList<>();
        list31.add("表头内容");
        list31.add("开始时间");
        list31.add("结束时间");

        List<String> list32 = new ArrayList<>();
        list32.add("条件1");
        list32.add("条件2");
        list32.add("条件3");

        List<String> list33 = new ArrayList<>();
        list33.add("字段1");
        list33.add("字段2");
        list33.add("字段3");
        list33.add("字段4");

        List<String> list2 = new ArrayList<>();
        list2.add("通用");
        list2.add("条件");
        list2.add("显示字段");


        int count = 0;
        for (int i = 0; i < 3; i++) {
            ClassA a = new ClassA(i, " 数据源" );
            a.setChildren(new ArrayList());
            list.add(a);
            count += 1;
            Log.e("TAG 1", a.getName());

            for (int j = 0; j < 3; j++) {
                ClassB b = new ClassB(j, list2.get(j) );
                a.getChildren().add(b);
//                count += 1;
                Log.e("TAG 2", b.getLabel());
                for (int k = 0; k < 3; k++) {
                    if (j == 0){
                        ClassC c = new ClassC(list31.get(k));
                        b.getChildren().add(c);
                    }
                    if (j == 1){
                        ClassC c = new ClassC(list32.get(k));
                        b.getChildren().add(c);
                    }
                    if (j == 2){
                        ClassC c = new ClassC(list33.get(k));
                        b.getChildren().add(c);
                    }

//                    count += 1;
//                    Log.e("TAG 3", c.getName());
//                    for (int l = 0; l < 3; l++) {
//                        ClassD d = new ClassD(" A" + i + " B" + j + " C" + k + " D" + l
//                                , "D");
//                        c.getChildren().add(d);
//                        count += 1;
//                        Log.e("TAG 4", d.getName());
//                    }
                }
            }
        }

        adapter = new MultiLevelTestAdapter(this, true, false
                , 0);
        adapter.setOnMultiLevelListener(new MultiLevelAdapter.OnMultiLevelListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id
                    , MultiLevelModel current, MultiLevelModel max) {
                Toast.makeText(WebActivity2.this, "position = " + position + "" +
                                " , current level = " + current.getLevel() + " , outside level = "
                                + max.getLevel()
                        , Toast.LENGTH_SHORT).show();
                if (position==2&&current.getLevel()==2&&max.getLevel()==0){
                    System.out.println("in function=========---------------------------------------------------------------------------");
                }

            }
        });
        analyse_list.setOnItemClickListener(adapter);
        analyse_list.setAdapter(adapter);
        adapter.setList(list);
    }
}
