package com.sonyericsson.android.camera.view.hint;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.font.FontUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HintTextView extends FrameLayout {
    private Button mButton;
    private LinearLayout mLayoutContainer;
    private TextView mMessage;
    private View mMessageBackground;
    private int mOrientation;
    private Runnable mSendAccessibilityEventTask;
    private TextView mSubMessage;
    private boolean mTransparentBackground;

    public HintTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTransparentBackground = true;
        this.mSendAccessibilityEventTask = new Runnable() { // from class: com.sonyericsson.android.camera.view.hint.HintTextView.1
            @Override // java.lang.Runnable
            public void run() {
                HintTextView.this.sendAccessibilityEvent(32);
            }
        };
    }

    public static HintTextView inflate(Context context) {
        return (HintTextView) View.inflate(context, R.layout.hint_text_indicator, null);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mLayoutContainer = (LinearLayout) findViewById(R.id.hint_text_layout_container);
        this.mMessage = (TextView) findViewById(R.id.hint_text_message);
        this.mSubMessage = (TextView) findViewById(R.id.hint_text_sub_message);
        this.mButton = (Button) findViewById(R.id.button_show_hint_text_dialog);
        this.mMessageBackground = findViewById(R.id.hint_text_message_background);
        FontUtil.setRobotoFont(this.mMessage, FontUtil.RobotoFontType.MEDIUM);
        FontUtil.setRobotoFont(this.mSubMessage, FontUtil.RobotoFontType.MEDIUM);
        FontUtil.setRobotoFont(this.mButton, FontUtil.RobotoFontType.MEDIUM);
    }

    public void setOnButtonClickListener(View.OnClickListener onClickListener) {
        this.mButton.setOnClickListener(onClickListener);
    }

    public void setContent(HintTextContent hintTextContent) throws Resources.NotFoundException {
        setMessageContents(hintTextContent.getMessageResourceId());
        setSubMessage(hintTextContent.getSubMessage());
        setButtonMessage(hintTextContent.getButtonMessageResourceId());
        setMessageDescription(hintTextContent.getMessageDescriptionResourceId());
        setButtonDescription(hintTextContent.getButtonDescriptionResourceId());
        setTransparentBackground(hintTextContent.isTransparentBackground());
        if (hintTextContent.getMessageDescriptionResourceId() == -1) {
            setImportantForAccessibility(2);
        } else {
            setImportantForAccessibility(1);
        }
        if (getVisibility() == 0) {
            postAccessibilityEvent();
        }
    }

    private void setMessageContents(int i) {
        if (i == -1) {
            this.mMessage.setVisibility(8);
            this.mMessageBackground.setVisibility(8);
        } else {
            if (this.mMessage.getVisibility() != 0) {
                this.mMessage.setVisibility(0);
                this.mMessageBackground.setVisibility(0);
            }
            this.mMessage.setText(i);
        }
    }

    private void setSubMessage(String str) {
        View viewFindViewById = findViewById(R.id.message_space);
        View viewFindViewById2 = findViewById(R.id.sub_message_space);
        if (str == null) {
            this.mSubMessage.setVisibility(8);
            viewFindViewById.setVisibility(8);
            viewFindViewById2.setVisibility(8);
        } else {
            if (this.mSubMessage.getVisibility() != 0) {
                this.mSubMessage.setVisibility(0);
                viewFindViewById.setVisibility(0);
                viewFindViewById2.setVisibility(0);
            }
            this.mSubMessage.setText(str);
        }
    }

    private void setButtonMessage(int i) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout_button_container);
        View viewFindViewById = findViewById(R.id.button_space);
        if (i == -1) {
            this.mButton.setVisibility(8);
            frameLayout.setVisibility(8);
            viewFindViewById.setVisibility(0);
        } else {
            if (this.mButton.getVisibility() != 0) {
                this.mButton.setVisibility(0);
                frameLayout.setVisibility(0);
                viewFindViewById.setVisibility(8);
            }
            this.mButton.setText(i);
        }
    }

    private void setMessageDescription(int i) throws Resources.NotFoundException {
        if (i == -1) {
            return;
        }
        this.mMessage.setContentDescription(getResources().getString(i));
    }

    private void setButtonDescription(int i) throws Resources.NotFoundException {
        if (i == -1) {
            this.mButton.setContentDescription("");
        } else {
            this.mButton.setContentDescription(getResources().getString(i));
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        boolean z = getVisibility() != 0 && i == 0;
        super.setVisibility(i);
        if (z) {
            postAccessibilityEvent();
        }
    }

    private void postAccessibilityEvent() {
        removeCallbacks(this.mSendAccessibilityEventTask);
        post(this.mSendAccessibilityEventTask);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        updateRotation();
        if (getHeight() < this.mLayoutContainer.getWidth()) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mLayoutContainer.getLayoutParams();
            layoutParams.width = getHeight();
            this.mLayoutContainer.setLayoutParams(layoutParams);
            super.onMeasure(i, i2);
        }
    }

    public void setUiOrientation(Rect rect, Context context, LayoutDependencyResolver.ScreenAspect screenAspect, int i) {
        this.mOrientation = i;
        findViewById(R.id.button_space).getLayoutParams().height = getBottomMarginPixelSize(i);
        findViewById(R.id.button_space).requestLayout();
        float fMax = Math.max(rect.width(), rect.height()) * 1.0f;
        if ((Math.min(rect.width(), rect.height()) * 1.0f) / fMax < 0.75f && this.mOrientation == 1) {
            setPadding(0, 0, 0, ((int) fMax) - LayoutDependencyResolver.getSurfaceViewRect(context, 0.75f, screenAspect).height());
        } else {
            setPadding(0, 0, 0, 0);
        }
        updateRotation();
    }

    private int getBottomMarginPixelSize(int i) {
        if (i == 1) {
            return getResources().getDimensionPixelSize(R.dimen.hint_text_button_margin_portrait);
        }
        return getResources().getDimensionPixelSize(R.dimen.hint_text_button_margin_landscape);
    }

    private void updateRotation() {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        float angle = RotationUtil.getAngle(this.mOrientation);
        if (angle != getRotation()) {
            setRotation(angle);
        }
        if (measuredWidth != 0) {
            float f = angle != 0.0f ? (measuredWidth - measuredHeight) / 2.0f : 0.0f;
            if (((int) f) != ((int) getTranslationX())) {
                setTranslationX(f);
            }
        }
    }

    private void setTransparentBackground(boolean z) {
        this.mTransparentBackground = z;
        if (this.mMessage != null && this.mTransparentBackground) {
            this.mMessageBackground.setBackground(null);
        } else {
            this.mMessageBackground.setBackgroundResource(R.drawable.cam_dialog_background_icn_for_toast);
        }
    }
}
