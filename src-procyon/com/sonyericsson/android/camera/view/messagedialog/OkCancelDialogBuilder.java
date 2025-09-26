// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.messagedialog;

import android.content.DialogInterface$OnKeyListener;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnClickListener;
import com.sonyericsson.android.camera.setting.MessageSettings;
import android.content.Context;

public class OkCancelDialogBuilder extends MessageDialogBuilder
{
    @Override
    protected RotatableDialog create(final Context context, final int orientation, final MessageSettings messageSettings, final MessageDialogRequest messageDialogRequest, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final DialogInterface$OnClickListener dialogInterface$OnClickListener2, final DialogInterface$OnCancelListener onCancelListener, final DialogInterface$OnDismissListener onDismissListener) {
        final RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
        builder.setOnKeyListener((DialogInterface$OnKeyListener)new KeyEventKiller());
        builder.setOrientation(orientation);
        builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
        builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
        builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, dialogInterface$OnClickListener);
        builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, dialogInterface$OnClickListener2);
        builder.setOnCancelListener(onCancelListener);
        builder.setOnDismissListener(onDismissListener);
        builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
        return builder.createRotatableDialog();
    }
}
