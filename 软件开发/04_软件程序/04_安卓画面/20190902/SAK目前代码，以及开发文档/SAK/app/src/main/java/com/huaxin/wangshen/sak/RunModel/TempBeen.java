package com.huaxin.wangshen.sak.RunModel;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

//@SmartTable(name = "作业员：Admin         编号：0001")

public class TempBeen {
    public TempBeen(int no,String name,int num,String xingzhi,String kind){
        this.no = no;
        this.name = name;
        this.num = num;
        this.xingzhi = xingzhi;
        this.kind = kind;
    }
//    @SmartColumn(id = 0, name = "No")
    private int no;
//    @SmartColumn(id = 1, name = "名称",minWidth = 100)
    private String name;

//    @SmartColumn(id = 2, name = "分支号")
    private int num;

//    @SmartColumn(id = 3, name = "工序性质")
    private String xingzhi;

//    @SmartColumn(id = 4, name = "模式种类")
    public String kind;

}
