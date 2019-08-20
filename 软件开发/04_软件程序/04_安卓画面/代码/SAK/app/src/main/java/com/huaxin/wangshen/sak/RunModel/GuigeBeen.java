package com.huaxin.wangshen.sak.RunModel;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "使用工具规格")

public class GuigeBeen {


    public GuigeBeen(int guige, String name, String xinghao, String code, int num){

        this.guige = guige;
        this.name = name;
        this.xinghao = xinghao;
        this.code = code;
        this.num = num;
    }
    @SmartColumn(id = 0, name = "工具规格",minWidth = 60)
    private int guige;
    @SmartColumn(id = 1, name = "工具规格名称",minWidth = 60)
    private String name;
    @SmartColumn(id = 0, name = "工具规格型号",minWidth = 60)
    private String xinghao;
    @SmartColumn(id = 0, name = "工具规格代码",minWidth = 60)
    private String code;
    @SmartColumn(id = 0, name = "拧紧计数",minWidth = 60)
    private int num;
}
