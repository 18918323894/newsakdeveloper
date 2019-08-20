package com.huaxin.wangshen.sak.RunViews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.table.TableData;
import com.huaxin.wangshen.sak.RunModel.FeedBean;
import com.huaxin.wangshen.sak.MainActivity;
import com.huaxin.wangshen.sak.R;

import java.util.ArrayList;
import java.util.List;

public class FeedDialog extends Dialog {

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
    private  EditText editText_editStock;
    private  int position_row;
    private  int position_col;
    private  Button btn_cancel;
    String position_value;
    String supply;
    Context mContext;
    boolean editlock = true;

    public FeedDialog(Context context) {
        this(context, 0);
        this.context = scanForActivity(context);
//        mContext = scanForActivity(context);

    }

    public FeedDialog(Context context, int themeResId) {
        this(context, true, null);
    }

    protected FeedDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_dialog);
        btn_cancel = findViewById(R.id.cancel_button);
        edit_button = findViewById(R.id.edit_button);
        feed_button = findViewById(R.id.feed_button);
        btn_confirm = findViewById(R.id.btn1);
        determine_button = findViewById(R.id.determine_button);
        editText_editStock = findViewById(R.id.editText_editStock);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("in this function");
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
                    return ContextCompat.getColor(getContext(), R.color.green);
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
    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity)cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper)cont).getBaseContext());

        return null;
    }
    public void fun1(){
        this.dismiss();
    }
}
