package com.huaxin.wangshen.sak.RunModel;

public class FeedBean {
    public FeedBean(String material_rack,String material_name,String material_model,String material_code,String stock,int supply_num,String batch){
        this.material_rack = material_rack;
        this.material_name = material_name;
        this.material_model = material_model;
        this.material_code = material_code;
        this.stock = stock;
        this.supply_num = supply_num;
        this.batch = batch;
    }

    public String material_rack;
    public String material_name;
    public String material_model;
    public String material_code;
    public String stock;
    public int supply_num;
    public String batch;
}
