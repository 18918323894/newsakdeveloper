package com.huaxin.wangshen.sak.RunViews;

import android.content.Context;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.listener.OnColumnClickListener;

public class TestSmartTable extends SmartTable {
    public TestSmartTable(Context context) {
        super(context);
    }
    public void setLongColumnClickListener(OnColumnClickListener onColumnClickListener){
        this.getProvider().setOnColumnClickListener(onColumnClickListener);
    }
}
