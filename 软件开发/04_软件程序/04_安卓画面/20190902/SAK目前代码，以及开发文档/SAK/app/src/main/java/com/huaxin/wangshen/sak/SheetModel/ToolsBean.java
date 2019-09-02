package com.huaxin.wangshen.sak.SheetModel;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

public class ToolsBean {
    public ToolsBean(String tool, String OKSignal, String NGSignal, String PowerSupplySignal) {
        this.tool = tool;
        this.OKSignal = OKSignal;
        this.NGSignal = NGSignal;
        this.PowerSupplySignal = PowerSupplySignal;
    }

    //    name：版块名称，count：目标值，restaurant：餐饮数量，ka：KA数量，wholesale：流通批发数量，industry：工业加工数量，other：其它数量


    private String tool;

    private String OKSignal;

    private String NGSignal;

    public String PowerSupplySignal;

}
