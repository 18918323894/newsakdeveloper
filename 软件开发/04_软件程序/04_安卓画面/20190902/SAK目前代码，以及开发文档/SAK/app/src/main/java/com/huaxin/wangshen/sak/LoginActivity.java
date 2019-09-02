package com.huaxin.wangshen.sak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.huaxin.wangshen.sak.Util.FirstDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private boolean isButton = true;
    private EditText Username=null;
    private EditText Password=null;
    private CheckBox RememberPasswordCheckbox=null;
    private Button Loginbutton=null;
    private EditText UrleditText;
    private TextView PasswordTextview;
    private Button HintOptionbutton;
    private TextView UrltextView;
    private TextView UserInfotextView;
    private TextView HintOptionsTextview;
    SharedPreferences sp=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Loginbutton = (Button)findViewById(R.id.Login_Button);
        HintOptionbutton = (Button)findViewById(R.id.HintOption_Button);
        UrleditText = (EditText)findViewById(R.id.Url_EditText);
        UrltextView = (TextView)findViewById(R.id.Url_TextView);
        UserInfotextView = (TextView)findViewById(R.id.UserInfo_TextView);
        RememberPasswordCheckbox = (CheckBox)findViewById(R.id.RememberPassword_Checkbox);
        HintOptionsTextview = (TextView)findViewById(R.id.HintOptions_Textview);
        Username = (EditText)findViewById(R.id.UserName_EditText);
        Password = (EditText)findViewById(R.id.PassWord_EditText);
        PasswordTextview = (TextView)findViewById(R.id.PassWord_TextView) ;
        sp = this.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        init();
        //不显示密码
        Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //判断用户名隐藏密码栏
       Username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
               if (actionId==EditorInfo.IME_ACTION_SEND||actionId==EditorInfo.IME_ACTION_DONE||(event !=null&&KeyEvent.KEYCODE_ENTER==event.getKeyCode()&&KeyEvent.ACTION_DOWN==event.getAction())) {
                   String str = UrleditText.getText().toString();
                   if (!isIPAddressByRegex(str)) {
                       FirstDialog url_error_dialog = new FirstDialog(LoginActivity.this, FirstDialog.DialogStyle.ERROR, "请输入正确的服务器地址");
                       return true;
                   }else {
                       String AdminUsername = Username.getText().toString();
                       if ("admin".equals(AdminUsername)){
                           Password.setVisibility(View.GONE);
                           PasswordTextview.setVisibility(View.GONE);
                           Loginbutton.setVisibility(View.GONE);
                           String s = getIntent().getStringExtra("login");
                           if (s == null){
                               s = "";
                           }
                           if (s.equals("logining")){
                               finish();
                           }else {
                               Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                               Intent intent = new Intent(LoginActivity.this,SheetActivity.class);
//                               Intent intent = new Intent(LoginActivity.this,WebviewActivity1.class);
//                               Intent intent = new Intent(LoginActivity.this,WebActivity2.class);

                               startActivity(intent);
                           }
                       } else if (AdminUsername.length()==0){
                           Password.setVisibility(View.GONE);
                           PasswordTextview.setVisibility(View.GONE);
                           Loginbutton.setVisibility(View.GONE);
                       }else{
                           Password.setVisibility(View.VISIBLE);
                           PasswordTextview.setVisibility(View.VISIBLE);
                           Loginbutton.setVisibility(View.VISIBLE);
                       }
                   }
               }
               return false;
           }
       });
        UrleditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    String str = UrleditText.getText().toString();
                    if (isIPAddressByRegex(str)){
                        String AdminUsername = Username.getText().toString();
                        if ("admin".equals(AdminUsername)){
                            String s = getIntent().getStringExtra("login");
                            if (s == null){
                                s = "";
                            }
                            if (s.equals("logining")){
                                finish();
                            }else {
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }else if (AdminUsername.length()==0){
                            Password.setVisibility(View.GONE);
                            PasswordTextview.setVisibility(View.GONE);
                            Loginbutton.setVisibility(View.GONE);
                        }else{
                            Password.setVisibility(View.VISIBLE);
                            PasswordTextview.setVisibility(View.VISIBLE);
                            Loginbutton.setVisibility(View.VISIBLE);
                        }
                    }else{
                        FirstDialog url_error_dialog = new FirstDialog(LoginActivity.this, FirstDialog.DialogStyle.ERROR, "请输入正确的服务器地址");
                        return true;
                    }
                }
                return false;
            }
        });
        //判断服务器地址是否正确
        UrleditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus){
                    String str = UrleditText.getText().toString();
                    if (!isIPAddressByRegex(str)){
                         FirstDialog url_error_dialog = new FirstDialog(LoginActivity.this,FirstDialog.DialogStyle.ERROR,"请输入正确的服务器地址");
                    }else{
                        return;
                    }
                }
            }
        });
        //隐藏高级设置
        HintOptionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isButton){
                    UrleditText.setVisibility(View.VISIBLE);
                    UserInfotextView.setVisibility(View.VISIBLE);
                    UrltextView.setVisibility(View.VISIBLE);
                    RememberPasswordCheckbox.setVisibility(View.VISIBLE);
                    HintOptionbutton.setBackgroundResource(R.drawable.up);
                    HintOptionsTextview.setText("隐藏高级设置");
                    isButton = false;
                }else {
                    UrleditText.setVisibility(View.GONE);
                    UserInfotextView.setVisibility(View.GONE);
                    UrltextView.setVisibility(View.GONE);
                    RememberPasswordCheckbox.setVisibility(View.GONE);
                    HintOptionbutton.setBackgroundResource(R.drawable.down);
                    HintOptionsTextview.setText("显示高级设置");
                    isButton = true;
                }
            }
        });

    }
    //登录记住密码
    private void init() {
        Username = (EditText) findViewById(R.id.UserName_EditText);
        Password = (EditText) findViewById(R.id.PassWord_EditText);
        RememberPasswordCheckbox = (CheckBox) findViewById(R.id.RememberPassword_Checkbox);
        Loginbutton = (Button) findViewById(R.id.Login_Button);
        if (sp.getBoolean("checkboxBoolean", false))
        {
            Username.setText(sp.getString(" Username", null));
            Password.setText(sp.getString("Password", null));
            RememberPasswordCheckbox.setChecked(true);

        }
        //登录按钮的监听
        Loginbutton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == Loginbutton){
            String name =  Username.getText().toString();
            String pass = Password.getText().toString();
            String str = UrleditText.getText().toString();
            if(name.trim().equals("")){
                FirstDialog url_error_dialog = new FirstDialog(LoginActivity.this,FirstDialog.DialogStyle.ERROR,"请输入用户名");
                return;
            }
            if(pass.trim().equals("")){
                FirstDialog url_error_dialog = new FirstDialog(LoginActivity.this,FirstDialog.DialogStyle.ERROR,"请输入密码");
                return;
            }
            boolean CheckBoxLoginbutton = RememberPasswordCheckbox.isChecked();
            if(name.equals("tcy")&&pass.equals("123456")&&isIPAddressByRegex(str)){
                String s = getIntent().getStringExtra("login");
                if (s == null){
                    s = "";
                }
                if (s.equals("logining")){
                    finish();
                }else {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            } else if(!isIPAddressByRegex(str)){
                FirstDialog url_error_dialog = new FirstDialog(LoginActivity.this,FirstDialog.DialogStyle.ERROR,"请输入正确的服务器地址");
            }else{
                FirstDialog url_error_dialog = new FirstDialog(LoginActivity.this,FirstDialog.DialogStyle.ERROR,"用户名或密码错误");
            }
            //记住密码
            if (CheckBoxLoginbutton)
            {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(" Username", name);
                editor.putString("Password", pass);
                editor.putBoolean("checkboxBoolean", true);
                editor.commit();
            }
            else
            {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(" Username", null);
                editor.putString("Password", null);
                editor.putBoolean("checkboxBoolean", false);
                editor.commit();
            }

        }

    }
    //正则表达式判断IP地址
    public boolean isIPAddressByRegex(String str) {
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        // 判断ip地址是否与正则表达式匹配
        if (str.matches(regex)) {
            String[] arr = str.split("\\.");
            for (int i = 0; i < 4; i++) {
                int temp = Integer.parseInt(arr[i]);
                //如果某个数字不是0到255之间的数 就返回false
                if (temp < 0 || temp > 255)
                    return false;
            }
            return true;
        } else return false;

    }

}
