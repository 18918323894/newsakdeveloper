package com.huaxin.wangshen.sak.AnalyseModel;

import com.lijianxun.multilevellist.model.MultiLevelModel;

public class ClassE extends MultiLevelModel {
    private String name;
    private String label;

    public ClassE(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
