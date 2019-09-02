package com.huaxin.wangshen.sak;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.column.ColumnInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnClickListener;
import com.huaxin.wangshen.sak.RunModel.CailiaoBeen;
import com.huaxin.wangshen.sak.RunModel.FeedBean;
import com.huaxin.wangshen.sak.RunModel.GuigeBeen;
import com.huaxin.wangshen.sak.RunModel.ModeBeen;
import com.huaxin.wangshen.sak.RunModel.ScanBeen;
import com.huaxin.wangshen.sak.RunModel.ShujuBeen;
import com.huaxin.wangshen.sak.RunModel.TempBeen;
import com.huaxin.wangshen.sak.RunModel.WaitBeen;
import com.huaxin.wangshen.sak.RunViews.DropDownView.DropDownView;
import com.huaxin.wangshen.sak.RunViews.FeedDialog;
import com.huaxin.wangshen.sak.RunViews.LoginDialog;
import com.huaxin.wangshen.sak.RunViews.SplitLayout;
import com.huaxin.wangshen.sak.Util.ConstraintUtil;
import com.huaxin.wangshen.sak.Util.DoubleClickListener;
import com.huaxin.wangshen.sak.Util.FirstDialog;

import net.grandcentrix.tray.AppPreferences;
import net.grandcentrix.tray.core.ItemNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

public class MainActivity extends AppCompatActivity  {

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
private ImageView right_photo;
private Timer timer;
private TimerTask timerTask;
private List<String> temp_color_list = new ArrayList<>();
private List<TempBeen> temp_list;
private int index = 0;
private TextView moshilabel;
final String SPLITLEFTPOSITION = "layout_split_img_mSplitPosition";
final String SPLITRIGHTPOSITION = "layout_split_right_mSplitPosition";
final String SPLITRIGHTBOTTOMPOSITION = "layout_split_rightbottom_mSplitPosition";
final String SPLITMPOSITION = "layout_split_mSplitPosition";

    public enum StepStyle{
        Check, //检查     /
        Wait,//等待       /
        Tool,//工具操作   /
        Scan,//条码采集    /
        Reclaim,//取料    /
        StartUp,//开工    /
        Other,//其他     /
        Report,//报工    /
        Photo; //拍照    /
    }

    private ConstraintLayout buliao_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        set_btns();
        set_modelayout();
        set_steplayout();
        set_showlayout();
        set_others();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        final AppPreferences appPreferences = new AppPreferences(MainActivity.this);
        appPreferences.put(SPLITLEFTPOSITION,layout_split_img.mSplitPosition);
        appPreferences.put(SPLITRIGHTPOSITION,layout_split_right.mSplitPosition);
        appPreferences.put(SPLITRIGHTBOTTOMPOSITION,layout_split_rightbottom.mSplitPosition);
        appPreferences.put(SPLITMPOSITION,layout_split_big.mSplitPosition);

        if (mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
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
    private int temp_num = 0;
    private static Activity scanForActivity(Context cont) {
//        if (cont == null)
//            return null;
//        else if (cont instanceof Activity)
//            return (Activity)cont;
//        else if (cont instanceof ContextWrapper)
//            return scanForActivity(((ContextWrapper)cont).getBaseContext());
//
//        return null;
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity)cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper)cont).getBaseContext());

        return null;
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
//                    dialog_feed = new FeedDialog(MainActivity.this);
//                    dialog_feed.show();
                    buliao_dialog.setVisibility(View.VISIBLE);
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
                    update_showlayout(temp_style_list.get(index));
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
    private void update_showlayout(StepStyle style){
        switch (style){
            case Check:{
                layout_constraint_show_top.setVisibility(View.VISIBLE);
                layout_constraint_show_bottom_top.setVisibility(View.VISIBLE);
                layout_constraint_show_bottom_bottom.setVisibility(View.VISIBLE);
                layout_constraint_wait.setVisibility(View.GONE);
                layout_constraint_quliao.setVisibility(View.GONE);
                layout_constraint_other.setVisibility(View.GONE);
                layout_constraint_old.setVisibility(View.VISIBLE);
                layout_constraint_tool.setVisibility(View.GONE);
                layout_constraint_scan.setVisibility(View.GONE);
                layout_constraint_guige.setVisibility(View.VISIBLE);
                layout_constraint_photo.setVisibility(View.GONE);
                layout_constraint_phototext.setVisibility(View.GONE);


                layout_split_right.mSplitPosition = 150;
                layout_split_rightbottom.mSplitPosition = 130;

                break;
            }
            case Wait:{
                layout_constraint_show_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_bottom.setVisibility(View.VISIBLE);
                layout_constraint_other.setVisibility(View.GONE);
                layout_constraint_wait.setVisibility(View.VISIBLE);
                layout_constraint_quliao.setVisibility(View.GONE);
                layout_constraint_old.setVisibility(View.GONE);
                layout_constraint_tool.setVisibility(View.GONE);
                layout_constraint_scan.setVisibility(View.GONE);
                layout_constraint_guige.setVisibility(View.VISIBLE);
                layout_constraint_photo.setVisibility(View.GONE);
                layout_constraint_phototext.setVisibility(View.GONE);


                layout_split_right.mSplitPosition = 0;
                layout_split_rightbottom.mSplitPosition = 0;
                layout_split_right.mHandleDrawable = null;
                layout_split_rightbottom.mHandleDrawable = null;

                break;
            }
            case Tool:{
                layout_constraint_show_top.setVisibility(View.VISIBLE);
                layout_constraint_show_bottom_top.setVisibility(View.VISIBLE);
                layout_constraint_show_bottom_bottom.setVisibility(View.VISIBLE);
                layout_constraint_wait.setVisibility(View.GONE);
                layout_constraint_other.setVisibility(View.GONE);
                layout_constraint_quliao.setVisibility(View.GONE);
                layout_constraint_old.setVisibility(View.GONE);
                layout_constraint_tool.setVisibility(View.VISIBLE);
                layout_constraint_scan.setVisibility(View.GONE);
                layout_constraint_guige.setVisibility(View.VISIBLE);
                layout_constraint_photo.setVisibility(View.GONE);
                layout_constraint_phototext.setVisibility(View.GONE);


                layout_split_right.mSplitPosition = 150;
                layout_split_rightbottom.mSplitPosition = 130;

                break;
            }
            case Scan:{
                layout_constraint_show_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_bottom.setVisibility(View.VISIBLE);
                layout_constraint_wait.setVisibility(View.GONE);
                layout_constraint_quliao.setVisibility(View.GONE);
                layout_constraint_other.setVisibility(View.GONE);
                layout_constraint_old.setVisibility(View.GONE);
                layout_constraint_tool.setVisibility(View.GONE);
                layout_constraint_scan.setVisibility(View.VISIBLE);
                layout_constraint_guige.setVisibility(View.VISIBLE);
                layout_constraint_photo.setVisibility(View.GONE);
                layout_constraint_phototext.setVisibility(View.GONE);


                layout_split_right.mSplitPosition = 0;
                layout_split_rightbottom.mSplitPosition = 0;
                layout_split_right.mHandleDrawable = null;
                layout_split_rightbottom.mHandleDrawable = null;

                break;
            }
            case Reclaim:{
                layout_constraint_show_top.setVisibility(View.VISIBLE);
                layout_constraint_show_bottom_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_bottom.setVisibility(View.VISIBLE);
                layout_constraint_wait.setVisibility(View.GONE);
                layout_constraint_quliao.setVisibility(View.VISIBLE);
                layout_constraint_other.setVisibility(View.GONE);
                layout_constraint_old.setVisibility(View.GONE);
                layout_constraint_tool.setVisibility(View.GONE);
                layout_constraint_scan.setVisibility(View.GONE);
                layout_constraint_guige.setVisibility(View.VISIBLE);
                layout_constraint_photo.setVisibility(View.GONE);
                layout_constraint_phototext.setVisibility(View.GONE);


                layout_split_right.mSplitPosition = 150;
                layout_split_rightbottom.mSplitPosition = 0;
                layout_split_rightbottom.mHandleDrawable = null;

                break;
            }
            case StartUp:{
                layout_constraint_show_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_bottom.setVisibility(View.GONE);
                layout_split_right.mSplitPosition = 0;
                layout_split_rightbottom.mSplitPosition = 0;
                layout_split_right.mHandleDrawable = null;
                layout_split_rightbottom.mHandleDrawable = null;
//                layout_split_big.mSplitPosition = 0.99f;

                break;
            }
            case Other:{
                layout_constraint_show_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_bottom.setVisibility(View.GONE);
                layout_split_right.mSplitPosition = 0;
                layout_split_rightbottom.mSplitPosition = 0;
                layout_split_right.mHandleDrawable = null;
                layout_split_rightbottom.mHandleDrawable = null;

                break;
            }
            case Report:{
                layout_constraint_show_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_bottom.setVisibility(View.GONE);
                layout_split_right.mSplitPosition = 0;
                layout_split_rightbottom.mSplitPosition = 0;
                layout_split_right.mHandleDrawable = null;
                layout_split_rightbottom.mHandleDrawable = null;

                break;
            }
            case Photo:{
                layout_constraint_show_top.setVisibility(View.GONE);
                layout_constraint_show_bottom_top.setVisibility(View.VISIBLE);
                layout_constraint_show_bottom_bottom.setVisibility(View.VISIBLE);
                layout_constraint_wait.setVisibility(View.GONE);
                layout_constraint_quliao.setVisibility(View.GONE);
                layout_constraint_other.setVisibility(View.GONE);
                layout_constraint_old.setVisibility(View.GONE);
                layout_constraint_tool.setVisibility(View.GONE);
                layout_constraint_scan.setVisibility(View.GONE);
                layout_constraint_guige.setVisibility(View.GONE);
                layout_constraint_photo.setVisibility(View.VISIBLE);
                layout_constraint_phototext.setVisibility(View.VISIBLE);

                layout_split_right.mHandleDrawable = null;
                layout_split_right.mSplitPosition = 0;
                layout_split_rightbottom.mSplitPosition = 130;
                break;
            }

        }

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
    private int wait_long = 30;
    private int wait_short = 10;
    private void table_long(){
        mode_table.setTableData(mode_table_data);
        mode_table.getConfig().setColumnTitleHorizontalPadding(mode_column_padding_long);
        cailiao_table.setTableData(cailiao_table_data);
        cailiao_table.getConfig().setColumnTitleHorizontalPadding(cailiao_column_padding_long);
        guige_table.setTableData(guige_table_data);
        guige_table.getConfig().setColumnTitleHorizontalPadding(guige_column_padding_long);
        shuju_table.setTableData(shuju_table_data);
        shuju_table.getConfig().setColumnTitleHorizontalPadding(shuju_column_padding_long);
        wait_table.setTableData(wait_table_data);
        wait_table.getConfig().setColumnTitleHorizontalPadding(wait_column_padding_long);
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
        wait_table.setTableData(wait_table_data);
        wait_table.getConfig().setColumnTitleHorizontalPadding(wait_column_padding_short);
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

    private List<String> wait_color_list = new ArrayList<>();
    private void init_list(){
        temp_color_list.clear();
        for (int i = 0;i < 13;i ++){
            if (i == 0){
                temp_color_list.add("huang");
            }
            temp_color_list.add("bai");
        }
        wait_color_list.clear();
        for (int i = 0;i < 13;i ++){
            if (i == 0){
                wait_color_list.add("lv");
            }
            wait_color_list.add("bai");
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
    private void update_wait_table(){
        ICellBackgroundFormat<CellInfo> format = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                for (int i = 0;i < wait_color_list.size();i ++){
                    if (cellInfo.row == i){
                        String c = wait_color_list.get(i);
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
        wait_table.getConfig().setContentCellBackgroundFormat(format);
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
    private VideoView videoView;
//    private String video_url="https://www.yunqishi.net/upload/mp4/201908/17-4.mp4";

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
        right_photo = findViewById(R.id.right_photo);
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

        videoView=findViewById(R.id.part_video);

//        set_mp4("https://www.yunqishi.net/upload/mp4/201908/17-4.mp4");
        set_mp3("http://sc1.111ttt.cn:8282/2018/1/03m/13/396131229550.m4a");
        init_buliao();
    }

    private String name;
    private String Authority = "admin";
    private Boolean edit_flag = false;
    SmartTable<FeedBean> table_feed;
    Column<String> material_rack;
    Column<String> material_name;
    Column<String> material_model;
    Column<String> material_code;
    Column<Integer> stock;
    Column<Integer> supply_num;
    Column<String> batch;
    List<FeedBean> code_list = new ArrayList<FeedBean>();
    private  TableData<FeedBean> tableData;
    private  Context context;
    private  Button edit_button;
    private  Button feed_button;
    private  Button determine_button;
    private  Button btn_confirm;
    private EditText editText_editStock;
    private  int position_row;
    private  int position_col;
    public   Button btn_cancel;
    String position_value;
    String supply;
    boolean editlock = true;
    private void init_buliao(){
        buliao_dialog = findViewById(R.id.main_buliao_dialog);
        buliao_dialog.bringToFront();
        buliao_dialog.setVisibility(View.GONE);

        btn_cancel = findViewById(R.id.cancel_button);
        edit_button = findViewById(R.id.edit_button);
        feed_button = findViewById(R.id.feed_button);
        btn_confirm = findViewById(R.id.btn1);
        determine_button = findViewById(R.id.determine_button);
        editText_editStock = findViewById(R.id.editText_editStock);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fun1();
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editlock = false;
                if ("admin".equals(Authority)) {
                    System.out.println("yes root please operation");
                    if (!edit_flag) {
                        edit_button.setBackgroundColor(Color.rgb(6, 157, 112));
                        editText_editStock.setVisibility(View.VISIBLE);
                        btn_confirm.setVisibility(View.VISIBLE);
                        editText_editStock.setText(position_value);
                        editText_editStock.requestFocus();
                        System.out.println(edit_flag);
                        edit_flag = !edit_flag;

                    } else {
                        edit_button.setBackgroundColor(Color.rgb(214, 215, 215));
                        editText_editStock.setVisibility(View.GONE);
                        btn_confirm.setVisibility(View.GONE);
                        System.out.println(edit_flag);
                        edit_flag = !edit_flag;

                    }
                } else {
                    System.out.println("no root");
                    return;
                }
            }
        });
        feed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for (FeedBean fb:code_list
                        ) {
                    if (count==position_row){
                        fb.stock = Integer.toString((Integer.parseInt(supply) + Integer.parseInt(position_value)));
                        System.out.println(fb.stock+"库存数zzzzzzzzzzzzzzzzzzzz");
                        tableData = new TableData<FeedBean>("ss", code_list, material_rack, material_name, material_model, material_code, stock, supply_num, batch);
                        table_feed.setTableData(tableData);
                        table_feed.refreshDrawableState();
                        table_feed.invalidate();
                    }
                    count++;
                }
            }
        });
        determine_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editlock) {
                    for (FeedBean fb : code_list
                            ) {
                        if (isNotNull(fb.material_code) && isNotNull(fb.material_model) && isNotNull(fb.material_name) && isNotNull(fb.material_rack) && isNotNull(fb.stock) && isNotNull(fb.material_code)) {
                            System.out.println("非空校验通过");
                        } else {
                            System.out.println("非空校验未通过----------------------");
                            break;
                        }

                        if (isNotIllegalCharacters(fb.material_code) && isNotIllegalCharacters(fb.material_rack) && isNotIllegalCharacters(fb.stock)) {
                            System.out.println("非法字符校验通过");
                        } else {
                            System.out.println("非法字符校验未通过----------------------------");
                            break;
                        }
                    }
                }
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editlock = true;
                int count = 0;
                for (FeedBean fb : code_list
                        ) {
                    if (count == position_row) {
                        fb.stock = editText_editStock.getText().toString();
                        System.out.println(fb.stock);
                    }
                    count++;
                }
                tableData = new TableData<FeedBean>("ss", code_list, material_rack, material_name, material_model, material_code, stock, supply_num, batch);
                table_feed.setTableData(tableData);
                table_feed.refreshDrawableState();
                table_feed.invalidate();
                editText_editStock.setVisibility(View.GONE);
                btn_confirm.setVisibility(View.GONE);
                edit_button.setBackgroundColor(Color.rgb(214, 215, 215));
                edit_flag = !edit_flag;
            }
        });
        initTable();
    }
    public void initTable() {
        table_feed = findViewById(R.id.feed_table);
        code_list.add(new FeedBean("1", "PCBA", "监控屏电源板", "701050000477100", "0", 0, "test1"));
        code_list.add(new FeedBean("1", "PCBA", "监控屏电源板", "701050000477100", "0", 0, "test1"));
        code_list.add(new FeedBean("1", "PCBA", "监控屏电源板", "701050000477100", "0", 27, "test1"));
        code_list.add(new FeedBean("1", "PCBA", "监控屏电源板", "701050000477100", "0", 0, "test1"));
        code_list.add(new FeedBean("1", "PCBA", "监控屏电源板", "701050000477100", "0", 0, "test1"));
        code_list.add(new FeedBean("1", "PCBA", "监控屏电源板", "701050000477100", "0", 0, "test1"));
        code_list.add(new FeedBean("1", "PCBA", "监控屏电源板", "701050000477100", "0", 0, "test1"));
        code_list.add(new FeedBean("1", "PCBA", "监控屏电源板", "701050000477100", "0", 0, "test1"));
        material_rack = new Column<String>("物料架", "material_rack");
        material_name = new Column<String>("物料名", "material_name");
        material_model = new Column<String>("物料型号", "material_model");
        material_code = new Column<String>("物料代码", "material_code");
        stock = new Column<Integer>("库存数", "stock");
        supply_num = new Column<Integer>("供给数", "supply_num");
        batch = new Column<String>("批次", "batch");
        tableData = new TableData<FeedBean>("ss", code_list, material_rack, material_name, material_model, material_code, stock, supply_num, batch);
        table_feed.getConfig().setShowTableTitle(false);
        table_feed.getConfig().setShowXSequence(false);
        table_feed.getConfig().setShowYSequence(false);
        table_feed.getConfig().setColumnTitleHorizontalPadding(0);
        table_feed.getConfig().setHorizontalPadding(22);


        tableData.setOnRowClickListener(new TableData.OnRowClickListener<FeedBean>() {
            @Override
            public void onClick(Column column, FeedBean feedBean, int col, int row) {
                position_col = col;
                position_row = row;
                fun();
            }
        });

        table_feed.setTableData(tableData);

    }
    public void fun() {
        ICellBackgroundFormat<CellInfo> format = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                //  System.out.println(row+"row");
//                System.out.println(cellInfo.col+"列----"+cellInfo.row+"行------"+cellInfo.data+"<----data-------------"+cellInfo.value+"<-----value--------");
//                if (cellInfo.row == row) {
////                    return Color.rgb(0, 0, 255);
//                    for (int i=col;i>0;i--){
                //  System.out.println(cellInfo.col+"列----"+cellInfo.row+"行------"+cellInfo.data+"<----data-------------"+cellInfo.value+"<-----value--------");
//
//                        return ContextCompat.getColor(mContext,R.color.green);
//                }}
                if (cellInfo.row == position_row && cellInfo.col == 4) {
                    System.out.println(cellInfo.col + "列----" + cellInfo.row + "行------" + cellInfo.data + "<----data-------------" + cellInfo.value + "<-----value--------");
                    position_value = cellInfo.value;
                    System.out.println(position_col + "col---" + position_row + "row---" + position_value + "value---");
                    return ContextCompat.getColor(MainActivity.this, R.color.green);
                }
                if (cellInfo.row == position_row && cellInfo.col == 5){
                    supply = cellInfo.value;
                }
                return TableConfig.INVALID_COLOR;

            }
        };
        table_feed.getConfig().setContentCellBackgroundFormat(format);
    }

    public boolean isNotNull(String str) {
        if ("".equals(str) || str == null) {
            return false;
        }else{
            return true;
        }
    }
    public  boolean isNotIllegalCharacters(String str){
        if(str.matches("[a-zA-Z0-9_\u4e00-\u9fa5]*")) {
            //不是非法字符
            return true;
        }else {
            return false;
        }
    }

    public void fun1(){
//        LinearLayout linearLayout = findViewById(R.id.feed_main_layout);
//        table_feed = null;
//        linearLayout.removeAllViews();
//        table_feed.setTableData(tableData);
        buliao_dialog.setVisibility(View.GONE);
    }

    private void set_mp4(String video_url){
        part_img.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoPath(video_url);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
    }
    private MediaPlayer mediaPlayer;
//    private String audio_url = "http://sc1.111ttt.cn:8282/2018/1/03m/13/396131229550.m4a";
    private void set_mp3(String audio_url){

        part_img.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audio_url);
            mediaPlayer.prepareAsync();
            mediaPlayer.start();
            mediaPlayer.setLooping(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<CailiaoBeen> cailiao_list;
    private List<GuigeBeen> guige_list;
    private List<ShujuBeen> shuju_list;
    private SplitLayout layout_split_big;
    private SplitLayout layout_split_img;
    private SplitLayout layout_split_right;
    private SplitLayout layout_split_rightbottom;
    private ConstraintLayout layout_constraint_show_top;
    private ConstraintLayout layout_constraint_show_bottom_top;
    private ConstraintLayout layout_constraint_show_bottom_bottom;
    private ConstraintLayout layout_constraint_wait;
    private ConstraintLayout layout_constraint_old;
    private ConstraintLayout layout_constraint_quliao;
    private ConstraintLayout layout_constraint_other;
    private ConstraintLayout layout_constraint_tool;
    private ConstraintLayout layout_constraint_scan;
    private ConstraintLayout layout_constraint_guige;
    private ConstraintLayout layout_constraint_photo;
    private ConstraintLayout layout_constraint_phototext;




    private void set_showlayout(){

        layout_constraint_show_top = findViewById(R.id.main_show_top_layout);
        layout_constraint_show_bottom_top = findViewById(R.id.main_show_bottom_top_layout);
        layout_constraint_show_bottom_bottom = findViewById(R.id.main_show_bottom_bottom_layout);
        layout_constraint_wait = findViewById(R.id.show_constraint_wait);
        layout_constraint_old = findViewById(R.id.show_constraint_old);
        layout_constraint_quliao = findViewById(R.id.show_constraint_quliao);
        layout_constraint_other = findViewById(R.id.show_constraint_other);
        layout_constraint_tool = findViewById(R.id.show_constraint_tool);
        layout_constraint_scan = findViewById(R.id.show_constraint_scan);
        layout_constraint_guige = findViewById(R.id.show_constraint_guige);
        layout_constraint_photo = findViewById(R.id.show_constraint_photo);
        layout_constraint_phototext = findViewById(R.id.show_constraint_phototext);





        layout_split_big = findViewById(R.id.split_h_layout);

        layout_split_img = findViewById(R.id.split_img_layout);
        layout_split_img.mHeight = 500;
        layout_split_right = findViewById(R.id.split_v_layout);
        layout_split_right.mHeight = 500;
        layout_split_rightbottom = findViewById(R.id.split_v_layout_child);
        layout_split_rightbottom.mHeight = 250;


        init_cailiao_table();
        init_guige_table();
        init_shuju_table();
        init_wait_table();
//        init_scan_table();
        update_showlayout(temp_style_list.get(index));
        final AppPreferences appPreferences = new AppPreferences(MainActivity.this);
        if (appPreferences!=null){
            try {
                layout_split_img.mSplitPosition = appPreferences.getFloat(SPLITLEFTPOSITION);
                layout_split_right.mSplitPosition = appPreferences.getFloat(SPLITRIGHTPOSITION);
                layout_split_rightbottom.mSplitPosition = appPreferences.getFloat(SPLITRIGHTBOTTOMPOSITION);
                layout_split_big.mSplitPosition = appPreferences.getFloat(SPLITMPOSITION);
            } catch (ItemNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            layout_split_img.mSplitPosition = 350;
            layout_split_right.mSplitPosition = 150;
            layout_split_rightbottom.mSplitPosition = 130;
        }
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
//        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//        intent.putExtra("login","logining");
//        startActivity(intent);
        LoginDialog loginDialog = new LoginDialog(MainActivity.this);
        loginDialog.show();
        temp_color_list.set(index,"hong");
        index = p;
        temp_color_list.set(index,"huang");
        update_temp_table();
        update_showlayout(temp_style_list.get(index));
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
            have_name = true;
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
        if (table_str.equals("wait")){
            wait_column_list = new ArrayList<>();
            Column<Integer> no = new Column<>("步骤号", "no");
            Column<String> name = new Column<>("步骤名称", "name");
            Column<Integer> time = new Column<>("等待时间", "time");
            Column<String> tiaojian = new Column<>("条件", "tiaojian");
            Column<String> zhuangtai = new Column<>("状态", "zhuangtai");
            Column<Integer> suoding = new Column<>("锁定步骤号", "suoding");

            wait_column_list.add(no);
            wait_column_list.add(name);
            wait_column_list.add(time);
            wait_column_list.add(tiaojian);
            wait_column_list.add(zhuangtai);
            wait_column_list.add(suoding);
            wait_table_data = new TableData<WaitBeen>("",wait_list,wait_column_list);
            wait_table.setTableData(wait_table_data);
            if (is_hide){
                wait_table.getConfig().setColumnTitleHorizontalPadding(wait_long);
            }else {
                wait_table.getConfig().setColumnTitleHorizontalPadding(wait_short);
            }
            wait_column_padding_short = wait_short;
            wait_column_padding_long = wait_long;
        }

    }

    private TableData<TempBeen> temp_table_data;
    private List<Column> temp_column_list;
    private List<StepStyle> temp_style_list;
    private ColumnInfo choose_columnInfo;
    private int temp_column_padding;
    private TextView temp_user_name;
    private TextView temp_user_num;

    private void set_steplayout(){
        temp_style_list = new ArrayList<>();
        temp_style_list.add(StepStyle.Photo);
        temp_style_list.add(StepStyle.Report);
        temp_style_list.add(StepStyle.Other);
        temp_style_list.add(StepStyle.StartUp);
        temp_style_list.add(StepStyle.Reclaim);
        temp_style_list.add(StepStyle.Scan);
        temp_style_list.add(StepStyle.Tool);
        temp_style_list.add(StepStyle.Wait);
        temp_style_list.add(StepStyle.Check);
        temp_style_list.add(StepStyle.Scan);
        temp_style_list.add(StepStyle.Reclaim);
        temp_style_list.add(StepStyle.Report);
        temp_style_list.add(StepStyle.Photo);
        temp_style_list.add(StepStyle.Reclaim);

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

    private TableData<WaitBeen> wait_table_data;
    private List<Column> wait_column_list;
    private ColumnInfo wait_columnInfo;
    private int wait_column_padding;
    private List<WaitBeen>wait_list;
    private SmartTable wait_table;

    private void init_wait_table(){

        wait_table = findViewById(R.id.wait_table);
        wait_list = new ArrayList<>();
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));
        wait_list.add(new WaitBeen(1,"拧螺丝",20,"嗯嗯嗯","等待开始",1));

        wait_column_list = new ArrayList<>();
        Column<Integer> no = new Column<>("步骤号", "no");
        Column<String> name = new Column<>("步骤名称", "name");
        Column<Integer> time = new Column<>("等待时间", "time");
        Column<String> tiaojian = new Column<>("条件", "tiaojian");
        Column<String> zhuangtai = new Column<>("状态", "zhuangtai");
        Column<Integer> suoding = new Column<>("锁定步骤号", "suoding");

        wait_column_list.add(no);
        wait_column_list.add(name);
        wait_column_list.add(time);
        wait_column_list.add(tiaojian);
        wait_column_list.add(zhuangtai);
        wait_column_list.add(suoding);
        wait_table_data= new TableData<WaitBeen>("",wait_list,wait_column_list);
        wait_table.setTableData(wait_table_data);
        wait_table.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                choose_columnInfo = columnInfo;
                final FirstDialog dialog = new FirstDialog(MainActivity.this,FirstDialog.DialogStyle.DEFAULT,"显示全部列或隐藏该列");
                dialog.add_btn("显示全部", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        huifu_table("wait");
                        dialog.dismiss();
                    }
                },0);
                if (temp_column_list.size() > 1){
                    dialog.add_btn("隐藏该列", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hide_column("wait");
                            dialog.dismiss();
                        }
                    },0);
                }

            }
        });
        wait_table.getConfig().setShowXSequence (false);
        wait_table.getConfig().setHorizontalPadding(0);
        wait_table.getConfig().setColumnTitleHorizontalPadding(10);
        wait_table.getConfig().setShowYSequence(false);
        wait_table.getConfig().setShowTableTitle(false);
        update_wait_table();
        wait_column_padding_long = wait_long;
        wait_column_padding_short = wait_short;

    }
    private TableData<ScanBeen> scan_table_data;
    private List<Column> scan_column_list;
    private ColumnInfo scan_columnInfo;
    private int scan_column_padding;
    private List<ScanBeen>scan_list;
    private SmartTable scan_table;
    private void init_scan_table(){

        scan_table = findViewById(R.id.wait_table);
        scan_list = new ArrayList<>();
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));
        scan_list.add(new ScanBeen(1,"拧螺丝","嗯嗯嗯"));

        scan_column_list = new ArrayList<>();
        Column<Integer> scan = new Column<>("采集条码:", "scan");
        Column<Integer> xuhao = new Column<>("序号:", "xuhao");
        scan.addChildren(xuhao);

        Column<String> name = new Column<>("接口调用显示文字", "name");
        Column<String>mingcheng = new Column<String>("名称","mingcheng");
        Column<String>tiaoma = new Column<String>("条码","tiaoma");
        name.addChildren(mingcheng);
        name.addChildren(tiaoma);

        Column<String> bendi = new Column<>("(MES采集或本地采集)", "bendi");
        Column<String>canji1 = new Column<String>("","caiji1");
        Column<String>canji2 = new Column<String>("","caiji2");
        Column<String>canji3 = new Column<String>("","caiji3");
        bendi.addChildren(canji1);
        bendi.addChildren(canji2);
        bendi.addChildren(canji3);


        scan_column_list.add(scan);
        scan_column_list.add(name);
        scan_column_list.add(bendi);
        scan_table_data= new TableData<ScanBeen>("",scan_list,scan_column_list);
        scan_table.setTableData(scan_table_data);
//        scan_table.setOnColumnClickListener(new OnColumnClickListener() {
//            @Override
//            public void onClick(ColumnInfo columnInfo) {
//                choose_columnInfo = columnInfo;
//                final FirstDialog dialog = new FirstDialog(MainActivity.this,FirstDialog.DialogStyle.DEFAULT,"显示全部列或隐藏该列");
//                dialog.add_btn("显示全部", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        huifu_table("wait");
//                        dialog.dismiss();
//                    }
//                },0);
//                if (scan_column_list.size() > 1){
//                    dialog.add_btn("隐藏该列", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            hide_column("wait");
//                            dialog.dismiss();
//                        }
//                    },0);
//                }
//
//            }
//        });
        scan_table.getConfig().setShowXSequence (false);
        scan_table.getConfig().setHorizontalPadding(0);
        scan_table.getConfig().setColumnTitleHorizontalPadding(10);
        scan_table.getConfig().setShowYSequence(false);
        scan_table.getConfig().setShowTableTitle(false);
//        update_wait_table();
//        wait_column_padding_long = wait_long;
//        wait_column_padding_short = wait_short;

    }
    private boolean have_name = true;
    private void hide_column(String table_str){
        if (table_str.equals("temp")){
            temp_table.setTableData(temp_table_data);
            if (choose_columnInfo.value.equals("名称")){
                int new_counts = temp_column_list.size();
                int column_with = choose_columnInfo.width;
                int add_padding = (column_with/new_counts + 1)/2;
                temp_column_padding = temp_column_padding + add_padding;
                temp_table.getConfig().setColumnTitleHorizontalPadding(temp_column_padding);
                temp_column_list.remove(choose_columnInfo.column);
                temp_table_data.setColumns(temp_column_list);
                have_name = false;
            }else {
                if (have_name == false){
                    int new_counts = temp_column_list.size();
                    int column_with = choose_columnInfo.width;
                    int add_padding = (column_with/new_counts + 1)/2;
                    temp_column_padding = temp_column_padding + add_padding;
                    temp_table.getConfig().setColumnTitleHorizontalPadding(temp_column_padding);
                    temp_column_list.remove(choose_columnInfo.column);
                    temp_table_data.setColumns(temp_column_list);
                }else {
                    for (Column column:temp_column_list){
                        if (column.getColumnName().equals("名称")){
                            column.setWidth(column.getWidth() + choose_columnInfo.width);
                            have_name = false;
                        }
                    }
                    temp_column_list.remove(choose_columnInfo.column);
                    temp_table_data.setColumns(temp_column_list);
                }
            }

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
        if (table_str.equals("wait")){
            wait_table.setTableData(wait_table_data);
            wait_column_list.remove(choose_columnInfo.column);
            wait_table_data.setColumns(wait_column_list);
            int new_counts = wait_column_list.size();
            int column_with = choose_columnInfo.width;
            int add_padding = (column_with/new_counts + 1)/2;
            if (is_hide){
                wait_column_padding_long = wait_column_padding_long + add_padding;
                wait_table.getConfig().setColumnTitleHorizontalPadding(wait_column_padding_long);
            }else {
                wait_column_padding_short = wait_column_padding_short + add_padding;
                wait_table.getConfig().setColumnTitleHorizontalPadding(wait_column_padding_short);
            }
        }
    }

    private int wait_column_padding_long;
    private int wait_column_padding_short;
    private Button back;
    private Button start;
    private Button next;
    private Button loop;
    private Button finish;



    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


}
