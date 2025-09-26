// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.messagedialog;

import android.widget.CompoundButton;
import com.sonyericsson.android.camera.setting.MessageType;
import android.widget.CompoundButton$OnCheckedChangeListener;
import android.view.KeyEvent;
import android.content.DialogInterface;
import android.content.DialogInterface$OnKeyListener;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnClickListener;
import com.sonyericsson.android.camera.setting.MessageSettings;
import android.content.Context;

public abstract class MessageDialogBuilder
{
    protected abstract RotatableDialog create(final Context p0, final int p1, final MessageSettings p2, final MessageDialogRequest p3, final DialogInterface$OnClickListener p4, final DialogInterface$OnClickListener p5, final DialogInterface$OnCancelListener p6, final DialogInterface$OnDismissListener p7);
    
    protected static class KeyEventKiller implements DialogInterface$OnKeyListener
    {
        public boolean onKey(final DialogInterface dialogInterface, final int n, final KeyEvent keyEvent) {
            return n == 27 || n == 80 || n == 82;
        }
    }
    
    protected static class MessageDialogCheckBoxListener implements CompoundButton$OnCheckedChangeListener, Runnable
    {
        private boolean mIsItemChecked;
        private final MessageSettings mMessageSettings;
        private final MessageType mMessageType;
        
        public MessageDialogCheckBoxListener(final MessageSettings mMessageSettings, final MessageType mMessageType) {
            this.mIsItemChecked = false;
            this.mMessageSettings = mMessageSettings;
            this.mMessageType = mMessageType;
        }
        
        public void onCheckedChanged(final CompoundButton compoundButton, final boolean mIsItemChecked) {
            this.mIsItemChecked = mIsItemChecked;
        }
        
        public void run() {
            this.mMessageSettings.setNeverShow(this.mMessageType, this.mIsItemChecked);
            this.mMessageSettings.save();
        }
    }
}
