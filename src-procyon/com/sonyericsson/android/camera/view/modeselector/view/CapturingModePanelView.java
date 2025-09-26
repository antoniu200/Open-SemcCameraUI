// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector.view;

import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;

public class CapturingModePanelView extends AbsPanelView
{
    private int mSelectorIconHeight;
    private ImageView mSelectorIconView;
    private int mSelectorIconWidth;
    protected TextView mSelectorLabelView;
    
    public CapturingModePanelView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    @Override
    public int getAppIconHeight() {
        return this.mSelectorIconHeight;
    }
    
    @Override
    public ImageView getAppIconView() {
        return this.mSelectorIconView;
    }
    
    @Override
    public int getAppIconWidth() {
        return this.mSelectorIconWidth;
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        (this.mSelectorIconView = (ImageView)this.findViewById(2131296574)).setId(-1);
        this.mSelectorIconWidth = this.mSelectorIconView.getLayoutParams().width;
        this.mSelectorIconHeight = this.mSelectorIconView.getLayoutParams().height;
        this.mSelectorLabelView = (TextView)this.findViewById(2131296575);
        if (CapturingModePanelView.REGULAR_ROBOTO != null) {
            this.mSelectorLabelView.setTypeface(CapturingModePanelView.REGULAR_ROBOTO);
        }
    }
    
    @Override
    public void setItem(final PanelAttributes panelAttributes) {
        this.mSelectorLabelView.setText((CharSequence)panelAttributes.getTitle());
    }
    
    @Override
    public void setUiOrientation(final int n) {
    }
}
