// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.messagedialog;

import android.view.View;
import android.content.DialogInterface;
import android.app.Activity;
import android.widget.CompoundButton$OnCheckedChangeListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.DialogInterface$OnKeyListener;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnClickListener;
import com.sonyericsson.android.camera.setting.MessageSettings;
import android.content.Context;

public class OkCancelWithCheckBoxDialogBuilder extends MessageDialogBuilder
{
    @Override
    protected RotatableDialog create(final Context context, final int orientation, final MessageSettings messageSettings, final MessageDialogRequest messageDialogRequest, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final DialogInterface$OnClickListener dialogInterface$OnClickListener2, final DialogInterface$OnCancelListener onCancelListener, final DialogInterface$OnDismissListener onDismissListener) {
        final RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
        builder.setOnKeyListener((DialogInterface$OnKeyListener)new KeyEventKiller());
        final View inflate = LayoutInflater.from(context).inflate(messageDialogRequest.mDialogId.layoutResourceID, (ViewGroup)null);
        ((TextView)inflate.findViewById(2131296413)).setText(messageDialogRequest.mDialogId.messageResourceID);
        final CheckBox checkBox = (CheckBox)inflate.findViewById(2131296351);
        final MessageDialogCheckBoxListener onCheckedChangeListener = new MessageDialogCheckBoxListener(messageSettings, messageDialogRequest.mDialogId.getMessageType());
        checkBox.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)onCheckedChangeListener);
        checkBox.setChecked(messageDialogRequest.mDialogId.hasOnCheckBox);
        builder.setViewAsScrollable(inflate);
        builder.setOrientation(orientation);
        builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
        final Activity activity = (Activity)context;
        builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this, activity, onCheckedChangeListener, dialogInterface$OnClickListener) {
            final OkCancelWithCheckBoxDialogBuilder this$0;
            final MessageDialogCheckBoxListener val$checkBoxListener;
            final Activity val$localActivity;
            final DialogInterface$OnClickListener val$localListenerPositive;
            
            public void onClick(final DialogInterface dialogInterface, final int n) {
                this.val$localActivity.runOnUiThread((Runnable)this.val$checkBoxListener);
                this.val$localListenerPositive.onClick(dialogInterface, n);
            }
        });
        builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this, activity, onCheckedChangeListener, dialogInterface$OnClickListener2) {
            final OkCancelWithCheckBoxDialogBuilder this$0;
            final MessageDialogCheckBoxListener val$checkBoxListener;
            final Activity val$localActivity;
            final DialogInterface$OnClickListener val$localListenerNegative;
            
            public void onClick(final DialogInterface dialogInterface, final int n) {
                this.val$localActivity.runOnUiThread((Runnable)this.val$checkBoxListener);
                this.val$localListenerNegative.onClick(dialogInterface, n);
            }
        });
        builder.setOnCancelListener(onCancelListener);
        builder.setOnDismissListener(onDismissListener);
        builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
        return builder.createRotatableDialog();
    }
}
