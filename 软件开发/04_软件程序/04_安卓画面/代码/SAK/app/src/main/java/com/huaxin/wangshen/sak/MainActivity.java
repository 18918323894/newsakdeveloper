package com.huaxin.wangshen.sak;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.column.ColumnInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.title.ITitleDrawFormat;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnClickListener;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.helloworld.library.MiddleDialogConfig;
import com.helloworld.library.utils.DialogEnum;
import com.huaxin.wangshen.sak.RunModel.CailiaoBeen;
import com.huaxin.wangshen.sak.RunModel.GuigeBeen;
import com.huaxin.wangshen.sak.RunModel.ModeBeen;
import com.huaxin.wangshen.sak.RunModel.ShujuBeen;
import com.huaxin.wangshen.sak.RunModel.TempBeen;
import com.huaxin.wangshen.sak.RunViews.DropDownView.DropDownView;
import com.huaxin.wangshen.sak.RunViews.FeedDialog;
import com.huaxin.wangshen.sak.RunViews.SplitLayout;
import com.huaxin.wangshen.sak.Util.ConstraintUtil;
import com.huaxin.wangshen.sak.Util.DoubleClickListener;
import com.huaxin.wangshen.sak.Util.FirstDialog;
import com.luck.picture.lib.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

import static com.huaxin.wangshen.sak.R.drawable.hidetemp;

public class MainActivity extends AppCompatActivity {
private SmartTable cailiao_table;
private SmartTable guige_table;
private SmartTable temp_table;
private SmartTable mode_table;
private SmartTable shuju_table;
private TextView timelabel;

private boolean is_hide = false;
private boolean is_start = false;
private boolean loop_on;
private ImageView bigimg;
private Timer timer;
private TimerTask timerTask;
private List<String> temp_color_list = new ArrayList<>();
private List<TempBeen> temp_list;
private int index = 0;
private TextView moshilabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        set_btns();
        set_modelayout();
        set_showlayout();
        set_steplayout();
        set_others();

    }
    private FeedDialog dialog_feed;

    private void hide_stemp(){
        ConstraintLayout main_layout = findViewById(R.id.main_activity_layout);
        ConstraintUtil util = new ConstraintUtil(main_layout);
        ConstraintUtil.ConstraintBegin begin = util.beginWithAnim();
        if (is_hide){
            begin.setWidth(R.id.step_layout,dpToPx(400));
            begin.commit();
            is_hide = false;
            hide_btn.setBackgroundResource(R.drawable.hidetemp);
            table_short();
        }else {
            begin.setWidth(R.id.step_layout,dpToPx(50));
            begin.commit();
            is_hide = true;
            hide_btn.setBackgroundResource(R.drawable.showtemp);
            table_long();
        }
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_hide:{
                    hide_stemp();
                    break;
                }
                case R.id.main_stop:{

                    suspend_timer();
                    break;
                }
                case R.id.main_back:{
                    dialog_feed = new FeedDialog(MainActivity.this);
                    dialog_feed.show();
                    break;
                }
                case R.id.main_start:{
                    if (is_start){
                        mode_table.setVisibility(View.VISIBLE);
                        moshi_btn.setVisibility(View.VISIBLE);
                        moshilabel.setVisibility(View.VISIBLE);
                        is_start = false;
                        start_btn.setText("启动");
                        suspend_timer();
                    }else {
                        mode_table.setVisibility(View.GONE);
                        moshi_btn.setVisibility(View.GONE);
                        moshilabel.setVisibility(View.GONE);
                        is_start = true;
                        start_btn.setText("暂停");
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        intent.putExtra("login","logining");
                        startActivity(intent);
                        start_timer();
                    }
                    break;
                }
                case R.id.main_next:{
                    if (is_start){

                    }
                    temp_color_list.set(index,"lv");
                    index = index + 1;
                    if (index < 14){
                        if (temp_list.get(index).kind.equals("三人")){
                            Intent intent = new Intent(MainActivity.this,ManyActivity.class);
                            startActivity(intent);
                        }
                        temp_color_list.set(index,"huang");
                    }else {
                        if (loop_on){
                            index = 0;
                            init_list();
                            update_temp_table();
                        }else {
                            index = temp_color_list.size() -1;
                            return;
                        }
                    }
                    set_steplayout();
                    stop_timer();
                    start_timer();
                    int n = 0;
                    for (String s:temp_color_list){
                        if (s.equals("lv")){
                            n ++;
                        }
                    }
                    bar.setProgress((int) n*100/temp_list.size());
                    break;
                }
                    case R.id.main_loop:{
                    if (loop_on){
                        loop_btn.setText("循环OFF");
                        loop_on = false;
                    }else {
                        loop_btn.setText("循环ON");
                        loop_on = true;
                    }
                    break;
                }
                case R.id.main_finishi:{
                    break;
                }
                case R.id.main_moshibtn:{
                    if (is_zhankai){
                        yunxing.setVisibility(View.GONE);
                        chanpin.setVisibility(View.GONE);
                        is_zhankai = false;
                    }else {
                        yunxing.setVisibility(View.VISIBLE);
                        chanpin.setVisibility(View.VISIBLE);
                        is_zhankai = true;
                    }
                    break;
                }
                case R.id.main_chanpin:{
                    is_zhankai = false;
                    yunxing.setVisibility(View.GONE);
                    chanpin.setVisibility(View.GONE);
                    moshi_btn.setSelectName("产品模式");
                    break;
                }
                case R.id.main_yunxing:{
                    is_zhankai = false;
                    yunxing.setVisibility(View.GONE);
                    chanpin.setVisibility(View.GONE);
                    moshi_btn.setSelectName("运行模式");
                    break;
                }
                case R.id.main_bottom_hide:{
                    ConstraintLayout main_layout = findViewById(R.id.main_activity_layout);
                    ConstraintUtil util = new ConstraintUtil(main_layout);
                    ConstraintUtil.ConstraintBegin begin = util.beginWithAnim();
                    begin.setHeight(R.id.status_layout,dpToPx(200));
                    begin.commit();
                    start_bottom_timer();
                    bottom_top.setVisibility(View.GONE);
                }
            }
        }
    }

    private boolean is_zhankai = false;
    private void update_showlayout(boolean cailiao,boolean guige,boolean shuju){
        ConstraintLayout layout1 = findViewById(R.id.main_show_top_layout);
//        SplitLayout layout2 = findViewById(R.id.split_v_layout_child);
        ConstraintLayout layout3 = findViewById(R.id.main_show_bottom_top_layout);
        ConstraintLayout layout4 = findViewById(R.id.main_show_bottom_bottom_layout);
        if (cailiao){
            layout1.setVisibility(View.VISIBLE);
        }else {
            layout1.setVisibility(View.GONE);
        }
        if (guige){
            layout3.setVisibility(View.VISIBLE);
        }else {
            layout3.setVisibility(View.GONE);
        }
        if (shuju){
            layout4.setVisibility(View.VISIBLE);
        }else {
            layout4.setVisibility(View.GONE);
        }
//                    ConstraintLayout layout1 = findViewById(R.id.main_show_top_layout);
//                    layout1.setVisibility(View.GONE);
//                    SplitLayout layout2 = findViewById(R.id.split_v_layout_child);
//                    layout2.setVisibility(View.GONE);
//        ConstraintLayout layout3 = findViewById(R.id.main_show_bottom_top_layout);
//        layout3.setVisibility(View.GONE);
//                    ConstraintLayout layout4 = findViewById(R.id.main_show_bottom_bottom_layout);
//                    layout4.setVisibility(View.GONE);
    }
    private int mode_long = 35;
    private int mode_short = 20;
    private int cailiao_long = 24;
    private int cailiao_short = 8;
    private int guige_long = 38;
    private int guige_short = 13;
    private int shuju_long = 64;
    private int shuju_short = 32;
    private int temp_long = 10;
    private void table_long(){
        mode_table.setTableData(mode_table_data);
        mode_table.getConfig().setColumnTitleHorizontalPadding(mode_column_padding_long);
        cailiao_table.setTableData(cailiao_table_data);
        cailiao_table.getConfig().setColumnTitleHorizontalPadding(cailiao_column_padding_long);
        guige_table.setTableData(guige_table_data);
        guige_table.getConfig().setColumnTitleHorizontalPadding(guige_column_padding_long);
        shuju_table.setTableData(shuju_table_data);
        shuju_table.getConfig().setColumnTitleHorizontalPadding(shuju_column_padding_long);
    }
    private void table_short(){
        mode_table.setTableData(mode_table_data);
        mode_table.getConfig().setColumnTitleHorizontalPadding(mode_column_padding_short);
        cailiao_table.setTableData(cailiao_table_data);
        cailiao_table.getConfig().setColumnTitleHorizontalPadding(cailiao_column_padding_short);
        guige_table.setTableData(guige_table_data);
        guige_table.getConfig().setColumnTitleHorizontalPadding(guige_column_padding_short);
        shuju_table.setTableData(shuju_table_data);
        shuju_table.getConfig().setColumnTitleHorizontalPadding(shuju_column_padding_short);
    }

    private OnClick onClick = new OnClick();
    private Button hide_btn;
    private Button stop_btn;
    private Button back_btn;
    private Button start_btn;
    private Button next_btn;
    private Button loop_btn;
    private Button finish_btn;
    private DropDownView moshi_btn;

    private void init_list(){
        temp_color_list.clear();
        for (int i = 0;i < 13;i ++){
            if (i == 0){
                temp_color_list.add("huang");
            }
            temp_color_list.add("bai");
        }
    }

    private void update_temp_table(){
        ICellBackgroundFormat<CellInfo> format = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                for (int i = 0;i < temp_color_list.size();i ++){
                    if (cellInfo.row == i){
                        String c = temp_color_list.get(i);
                        int color_num = 0;
                        if (c.equals("lv")){
                            color_num = R.color.green;
                        }
                        if (c.equals("hong")){
                            color_num = R.color.red;
                        }
                        if (c.equals("huang")){
                            color_num = R.color.yellow;
                        }
                        if (c.equals("bai")){
                            color_num = R.color.white_color;
                        }
                        if (c.equals("hui")){
                            color_num = R.color.bar_grey_90;
                        }
                        return ContextCompat.getColor(MainActivity.this,color_num);
                    }
                }
                return TableConfig.INVALID_COLOR;
            }
        };
        temp_table.getConfig().setContentCellBackgroundFormat(format);
    }
    private TextView chanpin;
    private TextView yunxing;
    private void set_btns(){
        init_list();

        hide_btn = findViewById(R.id.main_hide);
        hide_btn.setOnClickListener(onClick);

        stop_btn = findViewById(R.id.main_stop);
        stop_btn.setOnClickListener(onClick);

        back_btn = findViewById(R.id.main_back);
        back_btn.setOnClickListener(onClick);

        start_btn = findViewById(R.id.main_start);
        start_btn.setOnClickListener(onClick);

        next_btn = findViewById(R.id.main_next);
        next_btn.setOnClickListener(onClick);

        loop_btn = findViewById(R.id.main_loop);
        loop_btn.setOnClickListener(onClick);

        finish_btn = findViewById(R.id.main_finishi);
        finish_btn.setOnClickListener(onClick);
        finish_btn.setVisibility(View.INVISIBLE);

        moshi_btn = findViewById(R.id.main_moshibtn);
//        List<Map<String,Object>>list_map = new ArrayList<>();
//
//        Map<String,Object> map=new HashMap<>();
//        map.put("name", "name" );
//        map.put("key", "key" );
////            map.put("checked", false);
//        list_map.add(map);
//        moshi_btn.setupDataList(list_map);
        moshi_btn.setOnClickListener(onClick);
        chanpin = findViewById(R.id.main_chanpin);
        chanpin.setVisibility(View.GONE);
        chanpin.setOnClickListener(onClick);
        yunxing = findViewById(R.id.main_yunxing);
        yunxing.setVisibility(View.GONE);
        yunxing.setOnClickListener(onClick);
    }

    private ZzHorizontalProgressBar bar;
    private ImageView part_img;
    private TextView attention;
    private TextView specs_l;
    private TextView number_l;
    private TextView name_l;

    private void set_others(){
        bar = findViewById(R.id.speed_bar);
        bar.setProgress(0);
        //set padding
        bar.setPadding(0);
        //set bacground color
//        bar.setBgColor(Color.RED);
        //set progress color
//        bar.setProgressColor(Color.BLUE);
        //set max value
        bar.setMax(100);

        part_img = findViewById(R.id.part_img);
        bigimg = findViewById(R.id.main_bigimg);
        part_img.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                bigimg.setVisibility(View.VISIBLE);
            }
        });
        bigimg.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                bigimg.setVisibility(View.GONE);
            }
        });

        attention = findViewById(R.id.main_attention);
        specs_l = findViewById(R.id.main_guige_l);
        number_l = findViewById(R.id.main_bianhao_l);
        name_l = findViewById(R.id.main_name_l);

    }
    private List<CailiaoBeen> cailiao_list;
    private List<GuigeBeen> guige_list;
    private List<ShujuBeen> shuju_list;
    private void set_showlayout(){
        init_cailiao_table();
        init_guige_table();
        init_shuju_table();
    }

    private List<Column>cailiao_column_list;
    private TableData<CailiaoBeen> cailiao_table_data;
    private void init_cailiao_table(){
        cailiao_table = findViewById(R.id.cailiao_table);
        cailiao_list = new ArrayList<>();
        cailiao_list.add(new CailiaoBeen(38,"led液晶", "7601-11", "154k", 1, 0, 100, "abc"));
        cailiao_list.add(new CailiaoBeen(38,"led液晶", "7601-11", "154k", 1, 0, 100, "abc"));

        cailiao_column_list = new ArrayList<>();
        cailiao_column_list.add(new Column<Integer>("物料架","jia"));
        cailiao_column_list.add(new Column<String>("物料名称","ming"));
        cailiao_column_list.add(new Column<String>("物料型号","xinghao"));
        cailiao_column_list.add(new Column<String>("物料代码","daima"));
        cailiao_column_list.add(new Column<Integer>("用料量","liang"));
        cailiao_column_list.add(new Column<Integer>("取料量","qu"));
        cailiao_column_list.add(new Column<Integer>("库存","kucun"));
        cailiao_column_list.add(new Column<String>("不合格","buhege"));

        cailiao_table_data = new TableData<CailiaoBeen>("",cailiao_list,cailiao_column_list);
        ICellBackgroundFormat<CellInfo> format = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                return ContextCompat.getColor(MainActivity.this,R.color.white_color);
            }
        };
        cailiao_table.getConfig().setContentCellBackgroundFormat(format);
        cailiao_table.setTableData(cailiao_table_data);
        cailiao_table.getConfig().setShowTableTitle(false);
        cailiao_table.getConfig().setShowXSequence(false);
        cailiao_table.getConfig().setHorizontalPadding(0);
        cailiao_table.getConfig().setColumnTitleHorizontalPadding(8);
        cailiao_table.getConfig().setShowYSequence(false);

        cailiao_table.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                choose_columnInfo = columnInfo;
                final FirstDialog dialog = new FirstDialog(MainActivity.this,FirstDialog.DialogStyle.DEFAULT,"显示全部列或隐藏该列");
                dialog.add_btn("显示全部", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        huifu_table("cailiao");
                        dialog.dismiss();
                    }
                },0);
                if (cailiao_column_list.size() > 1){
                    dialog.add_btn("隐藏该列", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hide_column("cailiao");
                            dialog.dismiss();
                        }
                    },0);
                }
            }
        });
        cailiao_column_padding_long = cailiao_long;
        cailiao_column_padding_short = cailiao_short;
    }
    private List<Column>guige_column_list;
    private TableData<GuigeBeen> guige_table_data;
    private void init_guige_table(){
        guige_table = findViewById(R.id.guige_table);
        guige_list = new ArrayList<>();
        guige_list.add(new GuigeBeen(3,"美工刀", "", "美工刀", 1));

        guige_column_list = new ArrayList<>();
        guige_column_list.add(new Column<Integer>("工具规格","guige"));
        guige_column_list.add(new Column<String>("工具规格名称","name"));
        guige_column_list.add(new Column<String>("工具规格型号","xinghao"));
        guige_column_list.add(new Column<String>("工具规格代码","code"));
        guige_column_list.add(new Column<Integer>("拧紧计数","num"));
        guige_table_data = new TableData<GuigeBeen>("",guige_list,guige_column_list);

        guige_table.setTableData(guige_table_data);
        ICellBackgroundFormat<CellInfo> format = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                return ContextCompat.getColor(MainActivity.this,R.color.white_color);
            }
        };
        guige_table.getConfig().setContentCellBackgroundFormat(format);
        guige_table.getConfig().setShowTableTitle(false);
        guige_table.getConfig().setShowXSequence(false);
        guige_table.getConfig().setHorizontalPadding(0);
        guige_table.getConfig().setColumnTitleHorizontalPadding(13);
        guige_table.getConfig().setShowYSequence(false);
        guige_table.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                choose_columnInfo = columnInfo;
                final FirstDialog dialog = new FirstDialog(MainActivity.this,FirstDialog.DialogStyle.DEFAULT,"显示全部列或隐藏该列");
                dialog.add_btn("显示全部", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        huifu_table("guige");
                        dialog.dismiss();
                    }
                },0);
                if (guige_column_list.size() > 1){
                    dialog.add_btn("隐藏该列", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hide_column("guige");
                            dialog.dismiss();
                        }
                    },0);
                }
            }
        });
        guige_column_padding_long = guige_long;
        guige_column_padding_short = guige_short;
    }
    private List<ModeBeen>mode_list;
    private List<Column>mode_column_list;
    private TableData<ModeBeen> mode_table_data;
    private void set_modelayout(){
        moshilabel = findViewById(R.id.main_moshi_label);
        mode_table = findViewById(R.id.mode_table);
        mode_list = new ArrayList<>();
        mode_list.add(new ModeBeen(1,"四方监控屏",555,555,"EE",3491,0,2));
        mode_column_list = new ArrayList<>();
        mode_column_list.add(new Column<Integer>("No","no"));
        mode_column_list.add(new Column<String>("MA描述","miaoshu"));
        mode_column_list.add(new Column<Integer>("sub lot ID","lotid"));
        mode_column_list.add(new Column<Integer>("MAI ID","maiid"));
        mode_column_list.add(new Column<String>("产品版本","banben"));
        mode_column_list.add(new Column<Integer>("标准时间","biaozhun"));
        mode_column_list.add(new Column<Integer>("作业时间","zuoye"));
        mode_column_list.add(new Column<Integer>("完成数","wancheng"));
        mode_table_data = new TableData<ModeBeen>("",mode_list,mode_column_list);
        ICellBackgroundFormat<CellInfo> format = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                return ContextCompat.getColor(MainActivity.this,R.color.white_color);
            }
        };
        mode_table.getConfig().setContentCellBackgroundFormat(format);
        mode_table.setTableData(mode_table_data);
        mode_table.getConfig().setShowTableTitle(false);
        mode_table.getConfig().setShowXSequence(false);
        mode_table.getConfig().setHorizontalPadding(0);
        mode_table.getConfig().setColumnTitleHorizontalPadding(20);
        mode_table.getConfig().setShowYSequence(false);
        mode_table.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                choose_columnInfo = columnInfo;
                final FirstDialog dialog = new FirstDialog(MainActivity.this,FirstDialog.DialogStyle.DEFAULT,"显示全部列或隐藏该列");
                dialog.add_btn("显示全部", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        huifu_table("mode");
                        dialog.dismiss();
                    }
                },0);
                if (mode_column_list.size() > 1){
                    dialog.add_btn("隐藏该列", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hide_column("mode");
                            dialog.dismiss();
                        }
                    },0);
                }
            }
        });

        mode_column_padding_short = mode_short;
        mode_column_padding_long = mode_long;
        timelabel = findViewById(R.id.main_timelabel);
        time_left = findViewById(R.id.main_time_left);
        last_label = findViewById(R.id.main_last_label);
        this_label = findViewById(R.id.main_this_label);
        next_label = findViewById(R.id.main_next_label);


    }
    private TextView last_label;
    private TextView this_label;
    private TextView next_label;

    private TextView time_left;
    private Button bottom_top;
    private List<Column>shuju_column_list;
    private TableData<ShujuBeen> shuju_table_data;
    private void init_shuju_table(){
        bottom_top = findViewById(R.id.main_bottom_hide);
        bottom_top.setVisibility(View.GONE);
        bottom_top.setOnClickListener(onClick);
        shuju_table = findViewById(R.id.shuju_table);
        shuju_list = new ArrayList<>();
        shuju_list.add(new ShujuBeen("下限值","0.00","0.00","0.00"));
        shuju_list.add(new ShujuBeen("上限值","0.00","0.00","0.00"));

        shuju_column_list = new ArrayList<>();
        shuju_column_list.add(new Column<String>("","kong"));
        shuju_column_list.add(new Column<String>("转矩(Nm)","zhuanju"));
        shuju_column_list.add(new Column<String>("时间(秒)","time"));
        shuju_column_list.add(new Column<String>("角度(秒)","jiaodu"));
        shuju_table_data = new TableData<ShujuBeen>("",shuju_list,shuju_column_list);
        ICellBackgroundFormat<CellInfo> format = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                return ContextCompat.getColor(MainActivity.this,R.color.white_color);
            }
        };
        shuju_table.getConfig().setContentCellBackgroundFormat(format);
        shuju_table.setTableData(shuju_table_data);
        shuju_table.getConfig().setShowTableTitle(false);
        shuju_table.getConfig().setShowXSequence(false);
        shuju_table.getConfig().setHorizontalPadding(0);
        shuju_table.getConfig().setColumnTitleHorizontalPadding(32);
        shuju_table.getConfig().setShowYSequence(false);
        shuju_table.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                choose_columnInfo = columnInfo;
                final FirstDialog dialog = new FirstDialog(MainActivity.this,FirstDialog.DialogStyle.DEFAULT,"显示全部列或隐藏该列");
                dialog.add_btn("显示全部", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        huifu_table("shuju");
                        dialog.dismiss();
                    }
                },0);
                if (shuju_column_list.size() > 1){
                    dialog.add_btn("隐藏该列", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hide_column("shuju");
                            dialog.dismiss();
                        }
                    },0);
                }
            }
        });
        shuju_column_padding_short = shuju_short;
        shuju_column_padding_long = shuju_long;

        start_bottom_timer();
    }
    private int time_s = 0;
    private void suspend_timer(){
        if (timer != null){
            timer.cancel();
        }
        timer = null;
        timerTask = null;
    }
    private void stop_timer(){
        time_s = 0;
        if (timer != null){
            timer.cancel();
        }
        timer = null;
        timerTask = null;
    }

    private int biaozhun = 20;
    private void start_timer(){
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            time_s ++;
                            timelabel.setText("当前步骤实际用时(秒)"+time_s);
                            if (time_s > biaozhun){
                                timelabel.setTextColor(getResources().getColor(R.color.red));
                            }else {
                                timelabel.setTextColor(getResources().getColor(R.color.black));
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
    }
    Timer bottom_timer;
    TimerTask bottom_timer_task;
    int bottom_time_s = 0;
    private void start_bottom_timer(){
        if (bottom_timer == null && timerTask == null) {
            bottom_timer = new Timer();
            bottom_timer_task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bottom_time_s ++;
                            if (bottom_time_s >5){
                                ConstraintLayout main_layout = findViewById(R.id.main_activity_layout);
                                ConstraintUtil util = new ConstraintUtil(main_layout);
                                ConstraintUtil.ConstraintBegin begin = util.beginWithAnim();
                                begin.setHeight(R.id.status_layout,dpToPx(40));
                                begin.commit();
                                stop_bottom_timer();
                                bottom_top.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            };
            bottom_timer.schedule(bottom_timer_task, 0, 1000);
        }
    }
    private void stop_bottom_timer(){
        if (bottom_timer != null){
            bottom_timer.cancel();
        }
        bottom_timer = null;
        bottom_timer_task = null;
        bottom_time_s = 0;
    }
    private void jump(int p){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.putExtra("login","logining");
        startActivity(intent);
        temp_color_list.set(index,"hong");
        index = p;
        temp_color_list.set(index,"huang");
        update_temp_table();
        if (temp_list.get(index).kind.equals("三人")){
            Intent intentt = new Intent(MainActivity.this,ManyActivity.class);
            startActivity(intentt);
        }
    }

    private int mode_column_padding_short;
    private int mode_column_padding_long;
    private int cailiao_column_padding_short;
    private int cailiao_column_padding_long;
    private int guige_column_padding_short;
    private int guige_column_padding_long;
    private int shuju_column_padding_short;
    private int shuju_column_padding_long;

    private void huifu_table(String table_str){
        if (table_str.equals("temp")){
            Column<Integer> no = new Column<>("No", "no");
            Column<String> name = new Column<>("名称", "name");
            Column<Integer> num = new Column<>("分支号", "num");
            Column<String> xingzhi = new Column<>("工序性质", "xingzhi");
            Column<String> kind = new Column<>("模式种类", "kind");
            temp_column_list = new ArrayList<>();
            temp_column_list.add(no);
            temp_column_list.add(name);
            temp_column_list.add(num);
            temp_column_list.add(xingzhi);
            temp_column_list.add(kind);
            temp_table_data= new TableData<TempBeen>("",temp_list,temp_column_list);
            temp_table.setTableData(temp_table_data);
            temp_table.getConfig().setColumnTitleHorizontalPadding(temp_long);
            temp_column_padding = temp_long;
        }
        if (table_str.equals("mode")){
            mode_column_list = new ArrayList<>();
            mode_column_list.add(new Column<Integer>("No","no"));
            mode_column_list.add(new Column<String>("MA描述","miaoshu"));
            mode_column_list.add(new Column<Integer>("sub lot ID","lotid"));
            mode_column_list.add(new Column<Integer>("MAI ID","maiid"));
            mode_column_list.add(new Column<String>("产品版本","banben"));
            mode_column_list.add(new Column<Integer>("标准时间","biaozhun"));
            mode_column_list.add(new Column<Integer>("作业时间","zuoye"));
            mode_column_list.add(new Column<Integer>("完成数","wancheng"));
            mode_table_data = new TableData<ModeBeen>("",mode_list,mode_column_list);
            mode_table.setTableData(mode_table_data);
            if (is_hide){
                mode_table.getConfig().setColumnTitleHorizontalPadding(mode_long);
            }else {
                mode_table.getConfig().setColumnTitleHorizontalPadding(mode_short);
            }
            mode_column_padding_short = mode_short;
            mode_column_padding_long = mode_long;
        }
        if (table_str.equals("cailiao")){
            cailiao_column_list = new ArrayList<>();
            cailiao_column_list.add(new Column<Integer>("物料架","jia"));
            cailiao_column_list.add(new Column<String>("物料名称","ming"));
            cailiao_column_list.add(new Column<String>("物料型号","xinghao"));
            cailiao_column_list.add(new Column<String>("物料代码","daima"));
            cailiao_column_list.add(new Column<Integer>("用料量","liang"));
            cailiao_column_list.add(new Column<Integer>("取料量","qu"));
            cailiao_column_list.add(new Column<Integer>("库存","kucun"));
            cailiao_column_list.add(new Column<String>("不合格","buhege"));
            cailiao_table_data = new TableData<CailiaoBeen>("",cailiao_list,cailiao_column_list);
            cailiao_table.setTableData(cailiao_table_data);
            if (is_hide){
                cailiao_table.getConfig().setColumnTitleHorizontalPadding(cailiao_long);
            }else {
                cailiao_table.getConfig().setColumnTitleHorizontalPadding(cailiao_short);
            }
            cailiao_column_padding_short = cailiao_short;
            cailiao_column_padding_long = cailiao_long;
        }
        if (table_str.equals("guige")){
            guige_column_list = new ArrayList<>();
            guige_column_list.add(new Column<Integer>("工具规格","guige"));
            guige_column_list.add(new Column<String>("工具规格名称","name"));
            guige_column_list.add(new Column<String>("工具规格型号","xinghao"));
            guige_column_list.add(new Column<String>("工具规格代码","code"));
            guige_column_list.add(new Column<Integer>("拧紧计数","num"));
            guige_table_data = new TableData<GuigeBeen>("",guige_list,guige_column_list);
            guige_table.setTableData(guige_table_data);
            if (is_hide){
                guige_table.getConfig().setColumnTitleHorizontalPadding(guige_long);
            }else {
                guige_table.getConfig().setColumnTitleHorizontalPadding(guige_short);
            }
            guige_column_padding_short = guige_short;
            guige_column_padding_long = guige_long;
        }
        if (table_str.equals("shuju")){
            shuju_column_list = new ArrayList<>();
            shuju_column_list.add(new Column<String>("","kong"));
            shuju_column_list.add(new Column<String>("转矩(Nm)","zhuanju"));
            shuju_column_list.add(new Column<String>("时间(秒)","time"));
            shuju_column_list.add(new Column<String>("角度(秒)","jiaodu"));
            shuju_table_data = new TableData<ShujuBeen>("",shuju_list,shuju_column_list);
            shuju_table.setTableData(shuju_table_data);
            if (is_hide){
                shuju_table.getConfig().setColumnTitleHorizontalPadding(shuju_long);
            }else {
                shuju_table.getConfig().setColumnTitleHorizontalPadding(shuju_short);
            }
            shuju_column_padding_short = shuju_short;
            shuju_column_padding_long = shuju_long;
        }

    }

    private TableData<TempBeen> temp_table_data;
    private List<Column> temp_column_list;
    private ColumnInfo choose_columnInfo;
    private int temp_column_padding;
    private TextView temp_user_name;
    private TextView temp_user_num;

    private void set_steplayout(){
        temp_table = findViewById(R.id.step_table);
        temp_list = new ArrayList<>();
        temp_list.add(new TempBeen(1,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(2,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(3,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(4,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(5,"扫码工单号记录",1,"abc","三人"));
        temp_list.add(new TempBeen(6,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(7,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(8,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(9,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(10,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(11,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(12,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(13,"扫码工单号记录",1,"abc","单人"));
        temp_list.add(new TempBeen(14,"扫码工单号记录",1,"abc","单人"));
        temp_column_list = new ArrayList<>();
        Column<Integer> no = new Column<>("No", "no");
        Column<String> name = new Column<>("名称", "name");
        Column<Integer> num = new Column<>("分支号", "num");
        Column<String> xingzhi = new Column<>("工序性质", "xingzhi");
        Column<String> kind = new Column<>("模式种类", "kind");
        temp_column_list.add(no);
        temp_column_list.add(name);
        temp_column_list.add(num);
        temp_column_list.add(xingzhi);
        temp_column_list.add(kind);
        temp_table_data= new TableData<TempBeen>("",temp_list,temp_column_list);
        temp_table_data.setOnRowClickListener(new TableData.OnRowClickListener<TempBeen>() {
            @Override
            public void onClick(Column column, TempBeen tempBeen, int col, int row) {
                jump(row);
            }
        });
        temp_table.setTableData(temp_table_data);
        temp_table.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                choose_columnInfo = columnInfo;
                final FirstDialog dialog = new FirstDialog(MainActivity.this,FirstDialog.DialogStyle.DEFAULT,"显示全部列或隐藏该列");
                dialog.add_btn("显示全部", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        huifu_table("temp");
                        dialog.dismiss();
                    }
                },0);
                if (temp_column_list.size() > 1){
                    dialog.add_btn("隐藏该列", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hide_column("temp");
                            dialog.dismiss();
                        }
                    },0);
                }

            }
        });
        temp_table.getConfig().setShowXSequence (false);
        temp_table.getConfig().setHorizontalPadding(0);
        temp_table.getConfig().setColumnTitleHorizontalPadding(10);
        temp_table.getConfig().setShowYSequence(false);
        temp_table.getConfig().setShowTableTitle(false);
        update_temp_table();
        temp_column_padding = temp_long;

        temp_user_name = findViewById(R.id.main_zuoyeyuan_name);
        temp_user_num = findViewById(R.id.main_bianhao_no);
    }

    private void hide_column(String table_str){
        if (table_str.equals("temp")){
            temp_table.setTableData(temp_table_data);
            temp_column_list.remove(choose_columnInfo.column);
            temp_table_data.setColumns(temp_column_list);
            int new_counts = temp_column_list.size();
            int column_with = choose_columnInfo.width;
            int add_padding = (column_with/new_counts + 1)/2;
            temp_column_padding = temp_column_padding + add_padding;
            temp_table.getConfig().setColumnTitleHorizontalPadding(temp_column_padding);
        }
        if (table_str.equals("mode")){
            mode_table.setTableData(mode_table_data);
            mode_column_list.remove(choose_columnInfo.column);
            mode_table_data.setColumns(mode_column_list);
            int new_counts = mode_column_list.size();
            int column_with = choose_columnInfo.width;
            int add_padding = (column_with/new_counts + 1)/2;
            if (is_hide){
                mode_column_padding_long = mode_column_padding_long + add_padding;
                mode_table.getConfig().setColumnTitleHorizontalPadding(mode_column_padding_long);
            }else {
                mode_column_padding_short = mode_column_padding_short + add_padding;
                mode_table.getConfig().setColumnTitleHorizontalPadding(mode_column_padding_short);
            }
        }
        if (table_str.equals("cailiao")){
            cailiao_table.setTableData(cailiao_table_data);
            cailiao_column_list.remove(choose_columnInfo.column);
            cailiao_table_data.setColumns(cailiao_column_list);
            int new_counts = cailiao_column_list.size();
            int column_with = choose_columnInfo.width;
            int add_padding = (column_with/new_counts + 1)/2;
            if (is_hide){
                cailiao_column_padding_long = cailiao_column_padding_long + add_padding;
                cailiao_table.getConfig().setColumnTitleHorizontalPadding(mode_column_padding_long);
            }else {
                cailiao_column_padding_short = cailiao_column_padding_short + add_padding;
                cailiao_table.getConfig().setColumnTitleHorizontalPadding(cailiao_column_padding_short);
            }
        }
        if (table_str.equals("guige")){
            guige_table.setTableData(guige_table_data);
            guige_column_list.remove(choose_columnInfo.column);
            guige_table_data.setColumns(guige_column_list);
            int new_counts = guige_column_list.size();
            int column_with = choose_columnInfo.width;
            int add_padding = (column_with/new_counts + 1)/2;
            if (is_hide){
                guige_column_padding_long = guige_column_padding_long + add_padding;
                guige_table.getConfig().setColumnTitleHorizontalPadding(guige_column_padding_long);
            }else {
                guige_column_padding_short = guige_column_padding_short + add_padding;
                guige_table.getConfig().setColumnTitleHorizontalPadding(guige_column_padding_short);
            }
        }
        if (table_str.equals("shuju")){
            shuju_table.setTableData(shuju_table_data);
            shuju_column_list.remove(choose_columnInfo.column);
            shuju_table_data.setColumns(shuju_column_list);
            int new_counts = shuju_column_list.size();
            int column_with = choose_columnInfo.width;
            int add_padding = (column_with/new_counts + 1)/2;
            if (is_hide){
                shuju_column_padding_long = shuju_column_padding_long + add_padding;
                shuju_table.getConfig().setColumnTitleHorizontalPadding(shuju_column_padding_long);
            }else {
                shuju_column_padding_short = shuju_column_padding_short + add_padding;
                shuju_table.getConfig().setColumnTitleHorizontalPadding(shuju_column_padding_short);
            }
        }
    }

    private Button back;
    private Button start;
    private Button next;
    private Button loop;
    private Button finish;



    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected void onDestroy(){
        if (dialog_feed != null){
            dialog_feed.dismiss();
        }
        super.onDestroy();
    }
}
