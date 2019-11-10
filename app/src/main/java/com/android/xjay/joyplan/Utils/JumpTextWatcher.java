package com.android.xjay.joyplan.Utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 用于在输入回车后跳转到下一个元素（令下一个元素获得焦点）。
 */
public class JumpTextWatcher implements TextWatcher {

    private EditText mThisView;
    private View mNextView = null;
    private Context mContext;

    public JumpTextWatcher(Context c, EditText vThis, View vNext) {
        super();
        mContext = c;
        mThisView = vThis;
        if (vNext != null) {
            mNextView = vNext;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString();
        /* 发现输入回车符或换行符 */
        if (str.indexOf("\r") >= 0 || str.indexOf("\n") >= 0) {
            /* 去掉回车符和换行符 */
            mThisView.setText(str.replace("\r", "").replace("\n", ""));
            if (mNextView != null) {
                /* 让下一个视图获得焦点，即将光标移到下个视图 */
                mNextView.requestFocus();
                if (mNextView instanceof EditText) {
                    EditText et = (EditText) mNextView;
                    /* 让光标自动移到编辑框内部的文本末尾
                       方式一：直接调用EditText的setSelection方法 */
                    et.setSelection(et.getText().length());
                    /* 方式二：调用Selection类的setSelection方法
                       Editable edit = et.getText();
                       Selection.setSelection(edit, edit.length());*/
                } else {
                    InputMethodManager imm = (InputMethodManager)
                            mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mThisView.getWindowToken(), 0);
                }
            }
        }
    }
}
