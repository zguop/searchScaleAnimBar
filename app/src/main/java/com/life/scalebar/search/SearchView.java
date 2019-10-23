package com.life.scalebar.search;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.life.scalebar.R;
import com.life.scalebar.shape.Shape;


/**
 * auth aboom
 * date 2018/8/23
 */
public class SearchView extends LinearLayout {

    private OnSearchListener mSearchListener;
    private OnFocusChangeListener onFocusChangeListener;
    private EditText mEdit;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_search, this);
        initViews();
    }

    private void initViews() {
        final ImageView clear = findViewById(R.id.clear);
        mEdit = findViewById(R.id.edit);
        RelativeLayout mRlSearch = findViewById(R.id.rl_search);

        mRlSearch.setBackground(Shape.getShape(GradientDrawable.RECTANGLE)
                .setRadius(SizeUtils.dp2px(15))
                .setSolid("#f7f7f7")
                .create());

        mEdit.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (mEdit.hasFocus() && !TextUtils.isEmpty(s.toString())) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }
        });
        mEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v1, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v1, hasFocus);
                }
                if (hasFocus && !TextUtils.isEmpty(mEdit.getText().toString())) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }
        });
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit.setText("");
                mEdit.requestFocus();
                KeyboardUtils.showSoftInput(mEdit);
            }
        });

        mEdit.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (mSearchListener != null) {
                        mSearchListener.search(mEdit.getText().toString().trim());
                    }
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        });
    }

    public interface OnSearchListener {
        void search(String search);
    }

    public void setSearchListener(OnSearchListener searchListener) {
        this.mSearchListener = searchListener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    public void setSearchHint(String hint) {
        mEdit.setHint(hint);
    }

    public void setSearchHintColor(int color) {
        mEdit.setHintTextColor(color);
    }

    public void setSearchText(String text) {
        mEdit.setText(text);
        mEdit.setSelection(mEdit.length());
    }

    public void clearSearch() {
        if (!TextUtils.isEmpty(mEdit.getText())) {
            mEdit.setText("");
        }
        if (mEdit.hasFocus()) {
            mEdit.clearFocus();
        }
    }

    public EditText getEditView() {
        return mEdit;
    }

    /**
     * 输入类型
     */
    public void setInputType(int inputType) {
        int type;
        switch (inputType) {
            case 1://数字
                type = InputType.TYPE_CLASS_NUMBER;
                break;
            case 2://带小数点的数字
                type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                break;
            case 3://文字密码
                type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;
            case 4://纯英文
                type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI;
                break;
            case 5://电话号码
                type = InputType.TYPE_CLASS_PHONE;
                break;
            case 6://密码显示
                type = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;
            default:
                type = InputType.TYPE_CLASS_TEXT;
        }
        mEdit.setInputType(type);
    }

    /**
     * 设置自定义的输入条件,包括系统自带的{@link InputFilter.LengthFilter}长度限制,输入字符限制
     */
    public void setFilter(InputFilter[] filters) {
        if (filters == null) {
            filters = mEdit.getFilters();
        }
        mEdit.setFilters(filters);
    }
}
