package com.huaxin.wangshen.sak.RunModel;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "")

public class ShujuBeen {
    public ShujuBeen(String kong, String zhuangju,String time,String jiaodu){

        this.kong = kong;
        this.zhuanju = zhuangju;
        this.time = time;
        this.jiaodu = jiaodu;
    }
    @SmartColumn(id = 0, name = "")
    private String kong;
    @SmartColumn(id = 1, name = "转矩(Nm)")
    private String zhuanju;
    @SmartColumn(id = 2, name = "时间(秒)")
    private String time;
    @SmartColumn(id = 3, name = "角度(度)")
    private String jiaodu;
}
