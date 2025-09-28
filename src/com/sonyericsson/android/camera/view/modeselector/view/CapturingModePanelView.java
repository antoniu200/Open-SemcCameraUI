package com.sonyericsson.android.camera.view.modeselector.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CapturingModePanelView extends AbsPanelView {
    private int mSelectorIconHeight;
    private ImageView mSelectorIconView;
    private int mSelectorIconWidth;
    protected TextView mSelectorLabelView;

    @Override // com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView
    public void setUiOrientation(int i) {
    }

    public CapturingModePanelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mSelectorIconView = (ImageView) findViewById(R.id.selector_app_icon);
        this.mSelectorIconView.setId(-1);
        this.mSelectorIconWidth = this.mSelectorIconView.getLayoutParams().width;
        this.mSelectorIconHeight = this.mSelectorIconView.getLayoutParams().height;
        this.mSelectorLabelView = (TextView) findViewById(R.id.selector_app_name_label);
        if (REGULAR_ROBOTO != null) {
            this.mSelectorLabelView.setTypeface(REGULAR_ROBOTO);
        }
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView
    public void setItem(AbsPanelView.PanelAttributes panelAttributes) {
        this.mSelectorLabelView.setText(panelAttributes.getTitle());
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView
    public int getAppIconWidth() {
        return this.mSelectorIconWidth;
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView
    public int getAppIconHeight() {
        return this.mSelectorIconHeight;
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView
    public ImageView getAppIconView() {
        return this.mSelectorIconView;
    }
}
