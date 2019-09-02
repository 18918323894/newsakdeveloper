package com.huaxin.wangshen.sak.SheetModel;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;


public class FixedEquipmentBean {
    public FixedEquipmentBean(String Equipment, String ConfirmInput, String operation_FixedEquipment) {
        this.ConfirmInput = ConfirmInput;
        this.Equipment = Equipment;
        this.operation_FixedEquipment = operation_FixedEquipment;

    }

    //    name：版块名称，count：目标值，restaurant：餐饮数量，ka：KA数量，wholesale：流通批发数量，industry：工业加工数量，other：其它数量



    private String Equipment;

    private String ConfirmInput;

    public String operation_FixedEquipment;



}