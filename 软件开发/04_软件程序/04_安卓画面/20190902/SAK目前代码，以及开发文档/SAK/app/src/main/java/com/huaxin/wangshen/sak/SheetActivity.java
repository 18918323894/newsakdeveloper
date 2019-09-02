package com.huaxin.wangshen.sak;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;

import com.huaxin.wangshen.sak.SheetModel.FixedEquipmentBean;
import com.huaxin.wangshen.sak.SheetModel.GetLightBean;
import com.huaxin.wangshen.sak.SheetModel.PutLightBean;
import com.huaxin.wangshen.sak.SheetModel.ToolsBean;

import java.util.ArrayList;
import java.util.List;
public class SheetActivity extends AppCompatActivity {

    private SmartTable<PutLightBean> table_putlight;
    Column<String> material_rack_putlight;
    Column<String> Input_putlight;
    Column<String> operation_putlight;

    List<PutLightBean> codeList_put = new ArrayList<PutLightBean>();
    private SmartTable<GetLightBean> table_getlight;
    Column<String> Material_rack;
    Column<String> Input;
    Column<String> operation;
    List<GetLightBean> codeList_get = new ArrayList<GetLightBean>();

    List<ToolsBean> codeList_tools = new ArrayList<ToolsBean>();
    private SmartTable<ToolsBean> table_tools;
    Column<String> tool;
    Column<String> OKSignal;
    Column<String> NGSignal;
    Column<String> PowerSupplySignal;

    List<FixedEquipmentBean> codeList_fixed = new ArrayList<FixedEquipmentBean>();
    private SmartTable<FixedEquipmentBean> table_FixedEquipment;
    Column<String> Equipment;
    Column<String> ConfirmInput;
    Column<String> operation_FixedEquipment;


    private SmartTable <FixedEquipmentBean>table;
    private Button effective_button;
    private Button btn_table_getlight_on;
    private Button btn_table_getlight_off;
    private Button btn_table_putlight_on;
    private Button btn_table_putlight_off;
    private Button btn_workspace;
    private ListView listView;
    private Boolean workspace_flag = false;
    Thread thread = new Thread();

    private Boolean isEffective;

    public SheetActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        effective_button = findViewById(R.id.Effective_Button);
        btn_table_getlight_on = findViewById(R.id.Table1_On_Button);
        btn_table_getlight_off = findViewById(R.id.Table1_Off_Button);
        btn_table_putlight_on = findViewById(R.id.Table2_On_Button);
        btn_table_putlight_off = findViewById(R.id.Table2_Off_Button);
        DataInit();
        effective_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEffective){
                    Effective_all_on(isEffective);
                    effective_button.setBackgroundColor(Color.YELLOW);
                    //  table_tools.getConfig().setBackgroundColor(R.color.gray);
                    SetTableBackgroundColor(table,table_getlight,table_putlight,table_tools, R.color.light_gray);
                    init_table_putlight(isEffective);
                    init_table_getlight(isEffective);
                    init_table_tools(isEffective);
                    init_table_fixed_equipment(isEffective);
                    isEffective =!isEffective;
                    SetItemBackgroundColor();
                    try {
                        thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    effective_button.setBackgroundColor(Color.GRAY);
                    SetTableBackgroundColor(table,table_getlight,table_putlight,table_tools,R.color.gray);
                    init_table_putlight(isEffective);
                    init_table_getlight(isEffective);
                    init_table_tools(isEffective);
                    init_table_fixed_equipment(isEffective);
                    isEffective =!isEffective;
                    try {
                        thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        });

        btn_table_getlight_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (GetLightBean list:codeList_get
                        ) {
                    list.operation = "ON";
                }
                init_table_getlight(isEffective);
                initTableStyle(table, table_getlight, table_putlight, table_tools);
            }
        });
        btn_table_getlight_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (GetLightBean list:codeList_get
                        ) {
                    list.operation = "OFF";
                }
                init_table_getlight(isEffective);
                initTableStyle(table, table_getlight, table_putlight, table_tools);
            }
        });

        btn_table_putlight_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PutLightBean list:codeList_put
                        ) {
                    list.operation = "ON";
                }
                init_table_putlight(isEffective);
                initTableStyle(table, table_getlight, table_putlight, table_tools);
            }
        });
        btn_table_putlight_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PutLightBean list:codeList_put
                        ) {
                    list.operation = "OFF";
                }
                init_table_putlight(isEffective);
                initTableStyle(table, table_getlight, table_putlight, table_tools);
            }
        });
        init_Effective();
        init_table_putlight(false);
        init_table_getlight(false);
        init_table_tools(false);
        init_table_fixed_equipment(false);
        initTableStyle(table, table_getlight, table_putlight, table_tools);
        try {
            thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init_Effective();
        init_table_putlight(false);
        init_table_getlight(false);
        init_table_tools(false);
        init_table_fixed_equipment(false);
        initTableStyle(table, table_getlight, table_putlight, table_tools);
    }
    public void initTableStyle(SmartTable table, SmartTable table_getlight, SmartTable table_putlight, SmartTable table_tools) {
        SetItemBackgroundColor();
        table_getlight.getConfig().setContentStyle(new FontStyle(10, Color.GRAY));
        table_getlight.getConfig().setTableTitleStyle(new FontStyle(10, Color.GRAY));
        table_getlight.getConfig().setColumnTitleStyle(new FontStyle(10, Color.GRAY));
        table_getlight.getConfig().setShowTableTitle(false);
        table_getlight.getConfig().setShowXSequence(false);
        table_getlight.getConfig().setShowYSequence(false);
        table_putlight.getConfig().setContentStyle(new FontStyle(10, Color.GRAY));
        table_putlight.getConfig().setColumnTitleStyle(new FontStyle(10, Color.GRAY));
        table_putlight.getConfig().setShowXSequence(false);
        table_putlight.getConfig().setShowYSequence(false);
        table_putlight.getConfig().setShowTableTitle(false);
        table.getConfig().setContentStyle(new FontStyle(10, Color.GRAY));
        table.getConfig().setColumnTitleStyle(new FontStyle(10, Color.GRAY));
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.getConfig().setShowTableTitle(false);
        table_tools.getConfig().setContentStyle(new FontStyle(10, Color.GRAY));
        table_tools.getConfig().setColumnTitleStyle(new FontStyle(10, Color.GRAY));
        table_tools.getConfig().setShowXSequence(false);
        table_tools.getConfig().setShowYSequence(false);

        table_tools.getConfig().setShowTableTitle(false);

        table_getlight.getConfig().setColumnTitleHorizontalPadding(0);
        table_getlight.getConfig().setHorizontalPadding(20);
        table_putlight.getConfig().setColumnTitleHorizontalPadding(0);
        table_putlight.getConfig().setHorizontalPadding(20);
        table.getConfig().setColumnTitleHorizontalPadding(0);
        table.getConfig().setHorizontalPadding(20);
        table_tools.getConfig().setColumnTitleHorizontalPadding(0);
        table_tools.getConfig().setHorizontalPadding(16);
    }

    public void init_table_putlight(Boolean flag) {
        table_putlight = (SmartTable<PutLightBean>) findViewById(R.id.table2);


        material_rack_putlight = new Column<>("物料架", "Material_rack");
        Input_putlight = new Column<>("输入", "Input");
        int size = DensityUtils.dp2px(this, 30);
        operation_putlight = new Column<>("输出", "operation");
        if (flag == true) {
            operation_putlight.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                @Override
                public void onClick(Column<String> column, String value, String s, int position) {
                    if (operation_putlight.getDatas().get(position)=="OFF") {
                        //showName(position, false);
                        SetItemBackgroundColor();
                        operation_putlight.getDatas().set(position,"ON");
                        // table.getConfig().setContentBackground(R.color.green);
                    } else {
                        //  showName(position, true);
                        operation_putlight.getDatas().set(position, "OFF");
                    }
                    table_putlight.refreshDrawableState();
                    table_putlight.invalidate();
                }
            });
        }
        final TableData<PutLightBean> tableData = new TableData<>("测试标题", codeList_put, material_rack_putlight, Input_putlight, operation_putlight);
        table_putlight.getConfig().setShowTableTitle(false);
        table_putlight.setTableData(tableData);
    }

    public void init_table_getlight(Boolean flag) {


        table_getlight = (SmartTable<GetLightBean>) findViewById(R.id.table1);


        Material_rack = new Column<>("物料架", "Material_rack");

        Input = new Column<>("输入", "Input");

        int size = DensityUtils.dp2px(this, 30);

        operation = new Column<>("输出", "operation");
//        operation_putlight.setComputeWidth(40);
        if (flag == true) {
            operation.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                @Override
                public void onClick(Column<String> column, String value, String s, int position) {
                    if (operation.getDatas().get(position)=="OFF") {
                        //showName(position, false);
                        SetItemBackgroundColor();
                        operation.getDatas().set(position,"ON");
                        // table.getConfig().setContentBackground(R.color.green);
                    } else {
                        //  showName(position, true);
                        operation.getDatas().set(position, "OFF");
                    }
                    table_getlight.refreshDrawableState();
                    table_getlight.invalidate();
                }
            });
        }
        final TableData<GetLightBean> tableData = new TableData<>("测试标题", codeList_get, Material_rack, Input, operation);
        table_getlight.getConfig().setShowTableTitle(false);
        table_getlight.setTableData(tableData);
    }

    public void init_table_tools(Boolean flag) {
        table_tools = (SmartTable<ToolsBean>) findViewById(R.id.table3);
         tool = new Column<>("工具", "tool");
        OKSignal = new Column<>("OK信号", "OKSignal");
        int size = DensityUtils.dp2px(this, 30);
        NGSignal = new Column<>("NG信号", "NGSignal");
        PowerSupplySignal = new Column<>("电源信号", "PowerSupplySignal");

        if (flag == true) {
            if (flag == true) {
                PowerSupplySignal.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                    @Override
                    public void onClick(Column<String> column, String value, String s, int position) {
                        if (PowerSupplySignal.getDatas().get(position)=="OFF") {
                            //showName(position, false);
                            SetItemBackgroundColor();
                            PowerSupplySignal.getDatas().set(position,"ON");
                            // table.getConfig().setContentBackground(R.color.green);
                        } else {
                            //  showName(position, true);
                            PowerSupplySignal.getDatas().set(position, "OFF");
                        }
                        table_tools.refreshDrawableState();
                        table_tools.invalidate();
                    }
                });
            }
        }
        final TableData<ToolsBean> tableData = new TableData<>("测试标题", codeList_tools, tool, OKSignal, NGSignal, PowerSupplySignal);
        table_tools.getConfig().setShowTableTitle(false);
        table_tools.setTableData(tableData);
    }

    public void init_table_fixed_equipment(Boolean flag) {
        table = (SmartTable<FixedEquipmentBean>) findViewById(R.id.table4);
         Equipment = new Column<>("设备", "Equipment");
        ConfirmInput = new Column<>("确认输入", "ConfirmInput");
        int size = DensityUtils.dp2px(this, 30);
        operation_FixedEquipment = new Column<>("输出", "operation_FixedEquipment");
        operation_FixedEquipment.setComputeWidth(40);
        if (flag == true) {
            operation_FixedEquipment.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                @Override
                public void onClick(Column<String> column, String value, String s, int position) {
                    if (operation_FixedEquipment.getDatas().get(position)=="OFF") {
                        //showName(position, false);
                        SetItemBackgroundColor();
                        operation_FixedEquipment.getDatas().set(position,"ON");
                        // table.getConfig().setContentBackground(R.color.green);
                    } else {
                        //  showName(position, true);
                        operation_FixedEquipment.getDatas().set(position, "OFF");
                    }
                    table.refreshDrawableState();
                    table.invalidate();
                }
            });
        }
        final TableData<FixedEquipmentBean> tableData = new TableData<>("测试标题", codeList_fixed, Equipment, ConfirmInput, operation_FixedEquipment);
        table.getConfig().setShowTableTitle(false);
        table.setTableData(tableData);
    }
    public void init_Effective(){
        isEffective=true;   }

    public void Effective_all_on(boolean flag){
        for (GetLightBean list:codeList_get
                ) {
            if (flag==true) {
                list.operation = "ON";
            }else{
                list.operation = "OFF";
            }
        }
        for (PutLightBean list:codeList_put
                ) {
            if (flag==true) {
                list.operation = "ON";
            }else{
                list.operation = "OFF";
            }
            // list.operation = flag;
        }
        for (ToolsBean list:codeList_tools
                ) {
            if (flag==true) {
                list.PowerSupplySignal = "ON";
            }else{
                list.PowerSupplySignal = "OFF";
            }
            //list.PowerSupplySignal = flag;
        }
        for (FixedEquipmentBean list:codeList_fixed
                ) {
            if (flag==true) {
                list.operation_FixedEquipment = "ON";
            }else{
                list.operation_FixedEquipment = "OFF";
            }
            //    list.operation_FixedEquipment = flag;
        }
    }

    public void SetTableBackgroundColor(SmartTable table1, SmartTable table2, SmartTable table3, SmartTable table4, final int color){
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if(cellInfo.row %1 == 0) {
                    return ContextCompat.getColor(SheetActivity.this, color);
                }
                return TableConfig.INVALID_COLOR;
            }
        };
        table1.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table2.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table3.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table4.getConfig().setContentCellBackgroundFormat(backgroundFormat);
    }

    public void SetItemBackgroundColor(){
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if("ON".equals(cellInfo.value)&&cellInfo.col==2) {
                    return ContextCompat.getColor(SheetActivity.this, R.color.green);
                }else if ("OFF".equals(cellInfo.value)&&cellInfo.col==2){
                    return ContextCompat.getColor(SheetActivity.this, R.color.red);
                }
                return 1;
            }
        };
        ICellBackgroundFormat<CellInfo> backgroundFormat1 = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if("ON".equals(cellInfo.value)&&cellInfo.col==3) {
                    return ContextCompat.getColor(SheetActivity.this, R.color.green);
                }else if ("OFF".equals(cellInfo.value)&&cellInfo.col==3){
                    return ContextCompat.getColor(SheetActivity.this, R.color.red);
                }
                return 1;
            }
        };
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table_putlight.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table_getlight.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table_tools.getConfig().setContentCellBackgroundFormat(backgroundFormat1);
    }

    public void DataInit(){
        codeList_put.add(new PutLightBean("user_01", "ON", "OFF"));
        codeList_put.add(new PutLightBean("user_01", "ON", "OFF"));
        codeList_put.add(new PutLightBean("user_01", "ON", "OFF"));
        codeList_put.add(new PutLightBean("user_01", "ON", "OFF"));
        codeList_put.add(new PutLightBean("user_01", "ON", "OFF"));
        codeList_put.add(new PutLightBean("user_01", "ON", "OFF"));
        codeList_get.add(new GetLightBean("user_01", "ON", "OFF"));
        codeList_get.add(new GetLightBean("user_01", "ON", "OFF"));
        codeList_get.add(new GetLightBean("user_01", "ON", "OFF"));
        codeList_get.add(new GetLightBean("user_01", "ON", "OFF"));
        codeList_get.add(new GetLightBean("user_01", "ON", "OFF"));
        codeList_get.add(new GetLightBean("user_01", "ON", "OFF"));
        codeList_fixed.add(new FixedEquipmentBean("user_01", "ON", "OFF"));
        codeList_fixed.add(new FixedEquipmentBean("user_01", "ON", "OFF"));
        codeList_fixed.add(new FixedEquipmentBean("user_01", "ON", "OFF"));
        codeList_fixed.add(new FixedEquipmentBean("user_01", "ON", "OFF"));
        codeList_fixed.add(new FixedEquipmentBean("user_01", "ON", "OFF"));

        codeList_tools.add(new ToolsBean("tool_01", "ON", "ON", "OFF"));
        codeList_tools.add(new ToolsBean("tool_02", "ON", "ON", "OFF"));
        codeList_tools.add(new ToolsBean("tool_03", "ON", "ON", "OFF"));
        codeList_tools.add(new ToolsBean("tool_04", "ON", "ON", "OFF"));
        codeList_tools.add(new ToolsBean("tool_05", "ON", "ON", "OFF"));
        codeList_tools.add(new ToolsBean("tool_06", "ON", "ON", "OFF"));

    }
}