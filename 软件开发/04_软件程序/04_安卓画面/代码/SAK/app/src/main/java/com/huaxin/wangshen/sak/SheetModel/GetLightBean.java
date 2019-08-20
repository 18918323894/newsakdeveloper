package com.huaxin.wangshen.sak.SheetModel;

import android.widget.TextView;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;


public class GetLightBean {
    public GetLightBean(String Material_rack, String Input, Boolean operation) {
        this.Material_rack = Material_rack;
        this.Input = Input;
        this.operation = operation;

    }

    //    name：版块名称，count：目标值，restaurant：餐饮数量，ka：KA数量，wholesale：流通批发数量，industry：工业加工数量，other：其它数量
    //@SmartColumn(id = 0, name = "部门/渠道", autoMerge = true)



    private String Material_rack;

    private String Input;

    public Boolean operation;
}

