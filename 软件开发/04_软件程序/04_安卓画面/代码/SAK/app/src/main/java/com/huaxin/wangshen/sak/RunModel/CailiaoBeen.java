package com.huaxin.wangshen.sak.RunModel;

import android.graphics.Paint;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "使用材料")

public class CailiaoBeen {
    public CailiaoBeen(int jia, String ming, String xinghao, String daima, int liang, int qu, int kucun, String buhege) {
        this.jia = jia;
        this.ming = ming;
        this.xinghao = xinghao;
        this.daima = daima;
        this.liang = liang;
        this.qu = qu;
        this.kucun = kucun;
        this.buhege = buhege;
    }

    @SmartColumn(id = 0, name = "物料架",minWidth = 60)
    private int jia;
    @SmartColumn(id = 1, name = "物料名称",minWidth = 80)
    private String ming;
    @SmartColumn(id = 2, name = "物料型号",minWidth = 80)
    private String xinghao;
    @SmartColumn(id = 3, name = "物料代码",minWidth = 80)
    private String daima;
    @SmartColumn(id = 4, name = "用料量",minWidth = 80)
    private int liang;
    @SmartColumn(id = 5, name = "取料量")
    private int qu;
    @SmartColumn(id = 6, name = "库存")
    private int kucun;
    @SmartColumn(id = 7, name = "不合格")
    private String buhege;
}
