package com.huaxin.wangshen.sak.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxin.wangshen.sak.R;


public class FirstDialog extends Dialog{
    public enum DialogStyle{
        WARNNING,DEFAULT,ERROR,OTHER;
    }
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private TextView content_text;
    private String _content;
    private ImageView content_image;
    private TextView dialog_info;
    private int num = 0;
    private Context Context;

    public FirstDialog(Context context,DialogStyle style, String content) {
        this(context,0);
        this.Context = context;
        _content = content;
        this.show();
        switch (style){
            case WARNNING:{
                content_image.setImageResource(R.mipmap.warning_image);
                dialog_info.setText("警告");
                break;
            }
            case DEFAULT:{
                content_image.setImageResource(R.mipmap.true_image);
                dialog_info.setText("正常");
                break;
            }
            case ERROR:{
                content_image.setImageResource(R.mipmap.error_image);
                dialog_info.setText("错误");
                break;
            }
            case OTHER:{
                break;
            }
        }
        content_text.setText(_content);
    }

    public FirstDialog(Context context, int themeResId) {
        this(context, true,null);
    }

    protected FirstDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void add_btn(String btn_content,View.OnClickListener callBackDialog,int img){ ;
        Drawable drawable;
        if (img == 0){
            drawable = null;
        }else {
            drawable=this.Context.getResources().getDrawable(img);
            drawable.setBounds(0,0,20,20);//第一0是距左边距离，第二0是距上边距离，30、35分别是长宽
        }
        num ++;
        if (num == 5){
            num = 4;
        }
        switch (num){
            case 1:{
                btn1.setVisibility(View.VISIBLE);
                btn1.setOnClickListener(callBackDialog);
                btn1.setText(btn_content);
                btn1.setCompoundDrawables(drawable,null,null,null);
                break;
            }
            case 2:{
                btn2.setVisibility(View.VISIBLE);
                btn2.setOnClickListener(callBackDialog);
                btn2.setText(btn_content);
                btn2.setCompoundDrawables(drawable,null,null,null);
                break;
            }
            case 3:{
                btn3.setVisibility(View.VISIBLE);
                btn3.setOnClickListener(callBackDialog);
                btn3.setText(btn_content);
                btn3.setCompoundDrawables(drawable,null,null,null);

                break;
            }
            case 4:{
                btn4.setVisibility(View.VISIBLE);
                btn4.setOnClickListener(callBackDialog);
                btn4.setText(btn_content);
                btn4.setCompoundDrawables(drawable,null,null,null);

                break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        btn1 = (Button)findViewById(R.id.dialog_btn_1);
        btn1.setVisibility(LinearLayout.GONE);
        System.out.println(btn1);
        btn2 = (Button)findViewById(R.id.dialog_btn_2);
        btn2.setVisibility(View.GONE);
        btn3 = (Button)findViewById(R.id.dialog_btn_3);
        btn3.setVisibility(View.GONE);
        btn4 = (Button)findViewById(R.id.dialog_btn_4);
        btn4.setVisibility(View.GONE);
        Button btn_close = findViewById(R.id.btn_close);
        content_text = findViewById(R.id.dialog_content);
//        info_image = findViewById(R.id.info_image);
        content_image = findViewById(R.id.content_image);
        dialog_info = findViewById(R.id.dialog_info);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println(_content);
////                call_back_dialog.click();
//                dismiss();
//            }
//        });
//
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println(_content);
////                call_back_dialog.click();
//            }
//        });
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                call_back_dialog.click();
//            }
//        });
//
//        btn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                call_back_dialog.click();
//            }
//        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}



