package com.huaxin.wangshen.sak.AnalyseAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lijianxun.multilevellist.adapter.MultiLevelAdapter;
import com.lijianxun.multilevellist.model.MultiLevelModel;
import com.huaxin.wangshen.sak.R;
import com.huaxin.wangshen.sak.AnalyseModel.*;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by windows on 2017/12/29.
 */

public class MultiLevelTestAdapter extends MultiLevelAdapter {


    public MultiLevelTestAdapter(Context context, boolean isExpandable, boolean isExpandAll
            , int expandLevel) {
        super(context, isExpandable, isExpandAll, expandLevel);
    }

    @Override
    public View onCreateView(final int position, View convertView, ViewGroup parent) {
//                int ttt = getItemViewType(getViewTypeCount());

        Holder v = null;
        final MultiLevelModel model = (MultiLevelModel) getItem(position);

        if (model.getLevel()==0){
            convertView = mInflater.inflate(R.layout.first_item, null);
            v = new Holder(convertView);
            convertView.setTag(v);
            ImageView imageView = convertView.findViewById(R.id.item_jiantou);
            ConstraintLayout bc = convertView.findViewById(R.id.first_bc);
            if (model.isExpand() == false){
                imageView.setImageResource(R.mipmap.item_right);
                bc.setBackgroundResource(R.drawable.item_bc);
            }else {
                imageView.setImageResource(R.mipmap.item_bottom);
                bc.setBackgroundResource(R.drawable.item_bczhan);
            }
            ImageView close = convertView.findViewById(R.id.first_close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete_parent_item(position);
                }
            });

        }
        if (model.getLevel()==1){
            convertView = mInflater.inflate(R.layout.second_item, null);
            v = new Holder(convertView);
            convertView.setTag(v);
            ImageView second_close = convertView.findViewById(R.id.second_close);
            second_close.setVisibility(View.GONE);
            TextView second_item_title = convertView.findViewById(R.id.second_item_title);
            ClassB b = (ClassB)model;
            second_item_title.setText(b.getLabel());
            ImageView imageView = convertView.findViewById(R.id.item_jiantou_2);
            ConstraintLayout bc = convertView.findViewById(R.id.second_bc);
            if (model.isExpand() == false){
                imageView.setImageResource(R.mipmap.item_right);
                bc.setBackgroundResource(R.drawable.item_child_bc);
            }else {
                imageView.setImageResource(R.mipmap.item_bottom);
                bc.setBackgroundResource(R.drawable.item_child_bczhan);
            }
            if (getBelongSecondIndex(position) == 1){
                ClassC c = b.getChildren().get(0);
                c.setExpand(true);
            }

        }
        if (model.getLevel()==2){
            if (getBelongSecondIndex(position) == 0){
                convertView = mInflater.inflate(R.layout.tongyong_item, null);
                v = new Holder(convertView);
                convertView.setTag(v);
            }
            if (getBelongSecondIndex(position) == 1){
                convertView = mInflater.inflate(R.layout.add_item, null);
                v = new Holder(convertView);
                convertView.setTag(v);
                ImageView addchild = convertView.findViewById(R.id.add_tiaojian);
                addchild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClassC c = (ClassC)model;
                        c.getChildren().add(c.getChildren().get(0));
                        notifyDataSetChanged();
                    }
                });
            }if (getBelongSecondIndex(position) == 2){
                convertView = mInflater.inflate(R.layout.tongyong_item, null);
                v = new Holder(convertView);
                convertView.setTag(v);
            }if (getBelongSecondIndex(position) == 3){
                convertView = mInflater.inflate(R.layout.tongyong_item, null);
                v = new Holder(convertView);
                convertView.setTag(v);
            }
        }
        if (model.getLevel()==3){
            if (getBelongSecondIndex(position) == 1){
                convertView = mInflater.inflate(R.layout.second_item, null);
                v = new Holder(convertView);
                convertView.setTag(v);
                TextView t = convertView.findViewById(R.id.second_item_title);
                ClassD d = (ClassD) model;
                t.setText(d.getName());
                ImageView imageView = convertView.findViewById(R.id.item_jiantou_2);
                ConstraintLayout bc = convertView.findViewById(R.id.second_bc);
                if (model.isExpand() == false){
                    imageView.setImageResource(R.mipmap.item_right);
                    bc.setBackgroundResource(R.drawable.item_child_bc);
                }else {
                    imageView.setImageResource(R.mipmap.item_bottom);
                    bc.setBackgroundResource(R.drawable.item_child_bczhan);
                }
                ImageView close = convertView.findViewById(R.id.second_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete_child_item(position);
                    }
                });
            }else {
                convertView = mInflater.inflate(R.layout.third_item, null);
                v = new Holder(convertView);
                convertView.setTag(v);
            }

        }
        if (model.getLevel() == 4){
            convertView = mInflater.inflate(R.layout.tongyong_item, null);
            v = new Holder(convertView);
            convertView.setTag(v);
        }
        switch (model.getLevel()) {
            case 0:
                ClassA a = (ClassA) model;
                v.tv.setText( a.getName());
//                TextView  tv = (TextView) convertView.findViewById(R.id.first_item_title);
//                tv.setText(a.getName());
                convertView.setPadding(0 , 0, 0, 10);
                break;
            case 1:
//                ClassB b = (ClassB) model;
//                v.tv.setText( b.getLabel());
                convertView.setPadding(0 , 0, 0, 10);
                break;
            case 2:
//                ClassC c = (ClassC) model;
//                v.tv.setText(c.getName());
//                TextView  tv1 = (TextView) convertView.findViewById(R.id.second_item_title);
//                tv1.setText(c.getName());
                convertView.setPadding(5 , 0, 5, 5);
                break;
            case 3:
//                ClassD d = (ClassD) model;
//                v.tv.setText( d.getName());
                convertView.setPadding(5 , 0, 5, 10);
                break;
        }

        return convertView;
    }


    private void delete_parent_item(int p){
        mList.remove(getMainIndex(p));
        notifyDataSetChanged();
    }

    private int getMainIndex(int p){
        int index = 0;
        int num = 0;
        for (int i = 0;i < mList.size();i++){
            num ++;
            ClassA a = (ClassA) mList.get(i);
            if (a.isExpand() == true){
                for (int j = 0;j< a.getChildren().size();j++){
                    num ++;
                    ClassB b = a.getChildren().get(j);
                    if (b.isExpand() == true){
                        for (int k = 0;k < b.getChildren().size();k++){
                            num ++;
                            ClassC c = b.getChildren().get(k);
                            if (c.isExpand() == true){
                                for (int l = 0;l < c.getChildren().size();l++){
                                    num ++;
                                    ClassD d = c.getChildren().get(l);
                                    if (d.isExpand() == true){
                                        num ++;
                                        if ((num - 1)>=p){
                                            index = i;
                                            return index;
                                        }
                                    }
                                    if ((num - 1)>=p){
                                        index = i;
                                        return index;
                                    }
                                }
                            }
                            if ((num - 1)>=p){
                                index = i;
                                return index;
                            }
                        }
                    }
                    if ((num - 1)>=p){
                        index = i;
                        return index;
                    }
                }
            }
        }
        return index;
    }


    private int getBelongSecondIndex(int p){
        int index = 0;
        int num = 0;
        for (int i = 0;i < mList.size();i++){
            num ++;
            ClassA a = (ClassA) mList.get(i);
            if (a.isExpand() == true){
                for (int j = 0;j< a.getChildren().size();j++){
                    num ++;
                    ClassB b = a.getChildren().get(j);
                    if (b.isExpand() == true){
                        for (int k = 0;k < b.getChildren().size();k++){
                            num ++;
                            ClassC c = b.getChildren().get(k);
                            if (c.isExpand() == true){
                                for (int l = 0;l < c.getChildren().size();l++){
                                    num ++;
                                    ClassD d = c.getChildren().get(l);
                                    if (d.isExpand() == true){
                                        num ++;
                                        if ((num - 1)>=p){
                                            index = j;
                                            return index;
                                        }
                                    }
                                    if ((num - 1)>=p){
                                        index = j;
                                        return index;
                                    }
                                }
                            }
                            if ((num - 1)>=p){
                                index = j;
                                return index;
                            }
                        }
                    }
                    if ((num - 1)>=p){
                        index = j;
                        return index;
                    }
                }
            }
        }
        return index;
    }
    private void delete_child_item(int p){
        int index = 0;
        int num = 0;
        for (int i = 0;i < mList.size();i++){
            num ++;
            ClassA a = (ClassA) mList.get(i);
            if (a.isExpand() == true){
                for (int j = 0;j< a.getChildren().size();j++){
                    num ++;
                    ClassB b = a.getChildren().get(j);
                    if (b.isExpand() == true){
                        for (int k = 0;k < b.getChildren().size();k++){
                            num ++;
                            ClassC c = b.getChildren().get(k);
                            if (c.isExpand() == true){
                                num = num + c.getChildren().size();
                                for (int l = 0;l < c.getChildren().size();l++){
                                    ClassD d = c.getChildren().get(l);
                                    if (d.isExpand() == true){
                                        num ++;
                                        if ((num - 1)>=p){
                                            index = c.getChildren().size() - (num - 1 -p) -1;
                                            c.getChildren().remove(index);
                                            notifyDataSetChanged();
                                            return ;
                                        }
                                    }else {
                                        if ((num - 1)>=p){
                                            index = c.getChildren().size() - (num - 1 -p) -1;
                                            c.getChildren().remove(index);
                                            notifyDataSetChanged();
                                            return ;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    class Holder {
        TextView tv;
        public Holder(View view) {
            tv = (TextView) view.findViewById(R.id.first_item_title);
        }
    }

}
