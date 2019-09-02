package com.huaxin.wangshen.sak.RunViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huaxin.wangshen.sak.R;

public class LoginDialog extends Dialog {
    private String UserName;
    private String Password;
    ImageButton close_imagebutton;
    Context context1;
    Button login_button;
    EditText username_edittext;
    EditText password_edittext;
    TextView PasswordTextView;

    public LoginDialog(Context context) {
        this(context,0);
        this.context1 = context1;
    }

    public LoginDialog(Context context, int themeResId) {
        this(context, true,null);
    }

    protected LoginDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);
        login_button = findViewById(R.id.LoginButton);

        username_edittext = findViewById(R.id.UserNameEditText);
        password_edittext = findViewById(R.id.PasswordEditText);
        close_imagebutton = findViewById(R.id.Close_ImageButton);
        PasswordTextView = findViewById(R.id.PasswordTextView);
        password_edittext.setVisibility(View.INVISIBLE);
        PasswordTextView.setVisibility(View.INVISIBLE);
        login_button.setVisibility(View.INVISIBLE);
        username_edittext.setFocusable(true);
        close_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        username_edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        username_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction()))
                {
                        System.out.println(username_edittext.getText());
                        if ("admin".equals(username_edittext.getText().toString())) {
                                dismiss();
                            //是管理员的操作
                        }else{
                            password_edittext.setVisibility(View.VISIBLE);
                            PasswordTextView.setVisibility(View.VISIBLE);
                            login_button.setVisibility(View.VISIBLE);
                    }
                    return true;
                }else{
                    return false;
                }

            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(username_edittext.getText().toString())||"".equals(password_edittext.getText().toString())){
                    return;
                }
                System.out.println(username_edittext.getText().toString()+"------"+password_edittext.getText().toString());
            }
        });
    }
}
