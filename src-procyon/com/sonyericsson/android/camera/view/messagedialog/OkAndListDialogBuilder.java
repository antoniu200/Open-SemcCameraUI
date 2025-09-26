// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.messagedialog;

import android.content.res.Resources;
import android.view.View;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.content.DialogInterface$OnKeyListener;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnClickListener;
import com.sonyericsson.android.camera.setting.MessageSettings;
import android.content.Context;

public class OkAndListDialogBuilder extends MessageDialogBuilder
{
    @Override
    protected RotatableDialog create(final Context context, final int orientation, final MessageSettings messageSettings, final MessageDialogRequest messageDialogRequest, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final DialogInterface$OnClickListener dialogInterface$OnClickListener2, final DialogInterface$OnCancelListener onCancelListener, final DialogInterface$OnDismissListener onDismissListener) {
        final RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
        builder.setOnKeyListener((DialogInterface$OnKeyListener)new KeyEventKiller());
        builder.setOrientation(orientation);
        builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
        builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, dialogInterface$OnClickListener);
        builder.setOnCancelListener(onCancelListener);
        builder.setOnDismissListener(onDismissListener);
        builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
        final StringBuilder sb = new StringBuilder();
        final Resources resources = context.getResources();
        if (messageDialogRequest.mDialogId.messageResourceID != -1) {
            sb.append(resources.getString(messageDialogRequest.mDialogId.messageResourceID));
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }
        if (messageDialogRequest.mMessageList != null) {
            sb.append(messageDialogRequest.mMessageList);
        }
        if (messageDialogRequest.mDialogId.messageFooterResourceID != -1) {
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
            sb.append(resources.getString(messageDialogRequest.mDialogId.messageFooterResourceID));
        }
        if (this.isLargeTextEnabled(context)) {
            final LayoutInflater from = LayoutInflater.from(context);
            if (from == null) {
                return null;
            }
            final TextView view = (TextView)from.inflate(messageDialogRequest.mDialogId.layoutResourceID, (ViewGroup)null);
            view.setMovementMethod((MovementMethod)new ScrollingMovementMethod());
            view.setText((CharSequence)sb.toString());
            builder.setView((View)view);
        }
        else {
            builder.setMessage((CharSequence)sb.toString());
        }
        return builder.createRotatableDialog();
    }
    
    protected boolean isLargeTextEnabled(final Context context) {
        return context.getResources().getConfiguration().fontScale > 1.0f;
    }
}
