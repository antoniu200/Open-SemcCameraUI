// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.widget;

import android.view.View;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;

public class ControlSwitch extends Switch
{
    public static final String TAG = "ControlSwitch";
    private boolean mIsUpsideDown;
    
    public ControlSwitch(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    private void reverseChildrenViews(final ViewGroup viewGroup) {
        final ArrayList list = new ArrayList();
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            list.add(viewGroup.getChildAt(i));
        }
        viewGroup.removeAllViews();
        for (int j = list.size() - 1; j >= 0; --j) {
            viewGroup.addView((View)list.get(j));
        }
    }
    
    public void setUiOrientation(final int n) {
        monitorenter(this);
        Label_0027: {
            switch (n) {
                case 2: {
                    break Label_0027;
                }
                case 1: {
                    Label_0077: {
                        break Label_0077;
                        try {
                            if (this.mIsUpsideDown) {
                                this.reverseChildrenViews((ViewGroup)this);
                                this.mText.setRotation(0.0f);
                                synchronized (this.mSwitchBundle) {
                                    this.mSwitchBundle.setRotation(0.0f);
                                    monitorexit(this.mSwitchBundle);
                                    this.mIsUpsideDown = false;
                                    break;
                                }
                                if (!this.mIsUpsideDown) {
                                    this.reverseChildrenViews((ViewGroup)this);
                                    this.mText.setRotation(-90.0f);
                                    synchronized (this.mSwitchBundle) {
                                        this.mSwitchBundle.setRotation(-90.0f);
                                        monitorexit(this.mSwitchBundle);
                                        this.mIsUpsideDown = true;
                                    }
                                }
                            }
                        }
                        finally {
                            monitorexit(this);
                        }
                    }
                    break;
                }
            }
        }
        monitorexit(this);
    }
}
