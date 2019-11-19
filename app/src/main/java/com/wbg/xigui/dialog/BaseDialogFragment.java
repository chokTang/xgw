package com.wbg.xigui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import com.wbg.xigui.R;

/**
 * 基础的DialogFragmnt
 *
 * @author longshao 2018年9月5日 10:47:19
 */
public abstract class BaseDialogFragment extends DialogFragment {

    @IntDef({FORM_TOP_TO_TOP, FORM_TOP_TO_BOTTOM, FORM_BOTTOM_TO_BOTTOM, FORM_BOTTOM_TO_TOP,
            FORM_RIGHT_TO_RIGHT, FORM_RIGHT_TO_LEFT, FORM_LEFT_TO_LEFT, FORM_LEFT_TO_RIGHT, CNTER, CENTER_DEFAULT})
    public @interface AnimationType {
    }

    public static final int FORM_TOP_TO_TOP = 0x200;
    public static final int FORM_TOP_TO_BOTTOM = 0x201;
    public static final int FORM_BOTTOM_TO_BOTTOM = 0x202;
    public static final int FORM_BOTTOM_TO_TOP = 0x203;
    public static final int FORM_RIGHT_TO_RIGHT = 0x204;
    public static final int FORM_RIGHT_TO_LEFT = 0x205;
    public static final int FORM_LEFT_TO_LEFT = 0x206;
    public static final int FORM_LEFT_TO_RIGHT = 0x207;
    public static final int CNTER = 0x208;
    public static final int CENTER_DEFAULT = 0x209;

    public abstract Object getResId();

    public abstract void initView();

    public abstract void initData();

    public abstract int getViewWidth();

    public abstract int getViewHeight();

    public abstract int getViewGravity();

    private Toast cdToast;

    @AnimationType
    public abstract int getAnimationType();

    protected View mView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.BaseDialogFragmentStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getResId() instanceof Integer) {
            mView = inflater.inflate((Integer) getResId(), container, false);
        } else if (getResId() instanceof View) {
            mView = (View) getResId();
        } else {
            throw new RuntimeException("getResId() must Integer or View");
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 300);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.windowAnimations = getAnimate();
        layoutParams.width = getViewWidth();
        layoutParams.height = getViewHeight();
        layoutParams.gravity = getViewGravity();
        window.setAttributes(layoutParams);
    }


    /**
     * 发送提示消息
     *
     * @param message
     */
    public void showToast(String message) {
        if (TextUtils.isEmpty(message) || message.contains("timeout") || message.contains("请求超时") || message.contains("No message available")) {
            return;
        }
        if (cdToast == null) {
            cdToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        } else {
            cdToast.setText(message);
            cdToast.setDuration(Toast.LENGTH_SHORT);
        }
        cdToast.show();
    }


    /**
     * 初始化View
     *
     * @param resId
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int resId) {
        if (resId <= 0) {
            return (T) mView;
        } else {
            return (T) mView.findViewById(resId);
        }
    }

    /**
     * 获取dialog对话框弹出的样式
     *
     * @return
     */
    @StyleRes
    private int getAnimate() {
        switch (getAnimationType()) {
            case FORM_BOTTOM_TO_BOTTOM:
                return R.style.ActionSheetDialogAnimation;
            case FORM_BOTTOM_TO_TOP:
                return R.style.FromBottomToTop;
            case FORM_TOP_TO_TOP:
                return R.style.FromTopToTop;
            case FORM_TOP_TO_BOTTOM:
                return R.style.FromTopToBottom;
            case FORM_LEFT_TO_LEFT:
                return R.style.FromLeftToLeft;
            case FORM_LEFT_TO_RIGHT:
                return R.style.FromLeftToRight;
            case FORM_RIGHT_TO_RIGHT:
                return R.style.FromRightToRight;
            case FORM_RIGHT_TO_LEFT:
                return R.style.FromRightToLeft;
            case CNTER:
                return R.style.centerIn;
            case CENTER_DEFAULT:
            default:
                return android.R.style.Animation;
        }
    }

    /**
     * 取消对话框
     */
    protected void onDailogDismiss() {
        if (getDialog() == null) return;
        getDialog().cancel();
    }

    /**
     * 点击物理按键，对话框是否消失
     *
     * @param iskeyBack
     */
    public void setDailogKeyBack(final boolean iskeyBack) {
        if (getDialog() == null) return;
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!iskeyBack) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void setCancelable(boolean iscancle) {
        if (getDialog() == null) return;
        getDialog().setCanceledOnTouchOutside(iscancle);
    }
}
