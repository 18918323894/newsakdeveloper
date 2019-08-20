package com.huaxin.wangshen.sak.RunModel;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "")

public class ModeBeen {
    public ModeBeen(int no,String miaoshu,int lotid,int maiid,String banben,int biaozhun,int zuoye,int wancheng){
        this.no = no;
        this.miaoshu = miaoshu;
        this.lotid = lotid;
        this.maiid = maiid;
        this.banben = banben;
        this.biaozhun = biaozhun;
        this.zuoye = zuoye;
        this.wancheng = wancheng;
    }

//    @SmartColumn(id = 0, name = "No")
    private int no;
//    @SmartColumn(id = 1, name = "MAI描述",minWidth = 160)
    private String miaoshu;
//    @SmartColumn(id = 2, name = "sub lot ID",minWidth = 160)
    private int lotid;
//    @SmartColumn(id = 3, name = "MAI ID",minWidth = 160)
    private int maiid;
//    @SmartColumn(id = 4, name = "产品版本")
    private String banben;
//    @SmartColumn(id = 5, name = "标准时间")
    private int biaozhun;
//    @SmartColumn(id = 6, name = "作业时间")
    private int zuoye;
//    @SmartColumn(id = 7, name = "完成数")
    private int wancheng;
}
