package com.sonyericsson.android.camera.view.messagedialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class OkWithCheckBoxDialogBuilder extends MessageDialogBuilder {
    @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
    protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, final DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
        RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
        builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
        View viewInflate = LayoutInflater.from(context).inflate(messageDialogRequest.mDialogId.layoutResourceID, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.header_text)).setText(messageDialogRequest.mDialogId.messageResourceID);
        CheckBox checkBox = (CheckBox) viewInflate.findViewById(R.id.check_box);
        final MessageDialogBuilder.MessageDialogCheckBoxListener messageDialogCheckBoxListener = new MessageDialogBuilder.MessageDialogCheckBoxListener(messageSettings, messageDialogRequest.mDialogId.getMessageType());
        checkBox.setOnCheckedChangeListener(messageDialogCheckBoxListener);
        checkBox.setChecked(messageDialogRequest.mDialogId.hasOnCheckBox);
        builder.setViewAsScrollable(viewInflate);
        builder.setOrientation(i);
        builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
        final Activity activity = (Activity) context;
        builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, new DialogInterface.OnClickListener() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkWithCheckBoxDialogBuilder.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                activity.runOnUiThread(messageDialogCheckBoxListener);
                onClickListener.onClick(dialogInterface, i2);
            }
        });
        builder.setOnCancelListener(onCancelListener);
        builder.setOnDismissListener(onDismissListener);
        builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
        return builder.createRotatableDialog();
    }
}
