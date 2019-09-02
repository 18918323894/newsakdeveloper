package com.huaxin.wangshen.sak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.huaxin.wangshen.sak.RunModel.TempBeen;

import java.util.ArrayList;
import java.util.List;

public class ManyActivity extends AppCompatActivity {
private SmartTable table1;
    private SmartTable table2;
    private SmartTable table3;

    private List<TempBeen> temp_list;
    private List<Column> temp_column_list;
    private TableData<TempBeen> temp_table_data;

    private List<TempBeen> temp_list2;
    private List<Column> temp_column_list2;
    private TableData<TempBeen> temp_table_data2;

    private List<TempBeen> temp_list3;
    private List<Column> temp_column_list3;
    private TableData<TempBeen> temp_table_data3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_many);

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
                finish();
            }
        });
        table1 = findViewById(R.id.many_table1);
        table1.setTableData(temp_table_data);
        table1.getConfig().setShowXSequence (false);
        table1.getConfig().setHorizontalPadding(0);
        table1.getConfig().setColumnTitleHorizontalPadding(10);
        table1.getConfig().setShowYSequence(false);
        table1.getConfig().setShowTableTitle(false);


        temp_list2 = new ArrayList<>();
        temp_list2.add(new TempBeen(1,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(2,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(3,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(4,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(5,"扫码工单号记录",1,"abc","三人"));
        temp_list2.add(new TempBeen(6,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(7,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(8,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(9,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(10,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(11,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(12,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(13,"扫码工单号记录",1,"abc","单人"));
        temp_list2.add(new TempBeen(14,"扫码工单号记录",1,"abc","单人"));
        temp_column_list2 = new ArrayList<>();
        Column<Integer> no2 = new Column<>("No", "no");
        Column<String> name2 = new Column<>("名称", "name");
        Column<Integer> num2 = new Column<>("分支号", "num");
        Column<String> xingzhi2 = new Column<>("工序性质", "xingzhi");
        Column<String> kind2 = new Column<>("模式种类", "kind");
        temp_column_list2.add(no2);
        temp_column_list2.add(name2);
        temp_column_list2.add(num2);
        temp_column_list2.add(xingzhi2);
        temp_column_list2.add(kind2);
        temp_table_data2= new TableData<TempBeen>("",temp_list2,temp_column_list2);
        temp_table_data2.setOnRowClickListener(new TableData.OnRowClickListener<TempBeen>() {
            @Override
            public void onClick(Column column, TempBeen tempBeen, int col, int row) {
                finish();
            }
        });
        table2 = findViewById(R.id.many_table2);
        table2.setTableData(temp_table_data2);
        table2.getConfig().setShowXSequence (false);
        table2.getConfig().setHorizontalPadding(0);
        table2.getConfig().setColumnTitleHorizontalPadding(10);
        table2.getConfig().setShowYSequence(false);
        table2.getConfig().setShowTableTitle(false);

        temp_list3 = new ArrayList<>();
        temp_list3.add(new TempBeen(1,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(2,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(3,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(4,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(5,"扫码工单号记录",1,"abc","三人"));
        temp_list3.add(new TempBeen(6,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(7,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(8,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(9,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(10,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(11,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(12,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(13,"扫码工单号记录",1,"abc","单人"));
        temp_list3.add(new TempBeen(14,"扫码工单号记录",1,"abc","单人"));
        temp_column_list3 = new ArrayList<>();
        Column<Integer> no3 = new Column<>("No", "no");
        Column<String> name3 = new Column<>("名称", "name");
        Column<Integer> num3 = new Column<>("分支号", "num");
        Column<String> xingzhi3 = new Column<>("工序性质", "xingzhi");
        Column<String> kind3 = new Column<>("模式种类", "kind");
        temp_column_list3.add(no3);
        temp_column_list3.add(name3);
        temp_column_list3.add(num3);
        temp_column_list3.add(xingzhi3);
        temp_column_list3.add(kind3);
        temp_table_data3= new TableData<TempBeen>("",temp_list3,temp_column_list3);
        temp_table_data3.setOnRowClickListener(new TableData.OnRowClickListener<TempBeen>() {
            @Override
            public void onClick(Column column, TempBeen tempBeen, int col, int row) {
                finish();
            }
        });
        table3= findViewById(R.id.many_table3);
        table3.setTableData(temp_table_data3);
        table3.getConfig().setShowXSequence (false);
        table3.getConfig().setHorizontalPadding(0);
        table3.getConfig().setColumnTitleHorizontalPadding(10);
        table3.getConfig().setShowYSequence(false);
        table3.getConfig().setShowTableTitle(false);

    }
}
