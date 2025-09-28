package com.sonyericsson.android.camera.view.messagedialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class MessageDialogBuilder {
    protected abstract RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener);

    protected static class KeyEventKiller implements DialogInterface.OnKeyListener {
        @Override // android.content.DialogInterface.OnKeyListener
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            return i == 27 || i == 80 || i == 82;
        }

        protected KeyEventKiller() {
        }
    }

    protected static class MessageDialogCheckBoxListener implements CompoundButton.OnCheckedChangeListener, Runnable {
        private boolean mIsItemChecked = false;
        private final MessageSettings mMessageSettings;
        private final MessageType mMessageType;

        public MessageDialogCheckBoxListener(MessageSettings messageSettings, MessageType messageType) {
            this.mMessageSettings = messageSettings;
            this.mMessageType = messageType;
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            this.mIsItemChecked = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mMessageSettings.setNeverShow(this.mMessageType, this.mIsItemChecked);
            this.mMessageSettings.save();
        }
    }
}
