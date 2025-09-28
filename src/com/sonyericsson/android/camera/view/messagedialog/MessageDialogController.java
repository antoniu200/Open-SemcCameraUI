package com.sonyericsson.android.camera.view.messagedialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MessageDialogController {
    private static String TAG = "MessageDialogController";
    private static final boolean TRACE = true;
    private final Context mContext;
    private DialogId mCurrentDialogId;
    private final MessageSettings mMessageSettings;
    private final MessageDialogOnCancelListener mOnCancelListener;
    private final MessageDialogOnClickListener mOnClickNegativeListener;
    private final MessageDialogOnClickListener mOnClickPositiveListener;
    private final MessageDialogOnDismissListener mOnDismissListener;
    private final MessageDialogOnOpenListener mOnOpenListener;
    private final List<MessageDialogRequest> mMessageList = new ArrayList();
    private final List<RotatableDialog> mDialogList = new ArrayList();
    private int mSensorOrientation = 0;

    public interface MessageDialogOnCancelListener {
        void onCancel(MessageDialogRequest messageDialogRequest);
    }

    public interface MessageDialogOnClickListener {
        void onClick(MessageDialogRequest messageDialogRequest);
    }

    public interface MessageDialogOnDismissListener {
        void onDismiss(MessageDialogRequest messageDialogRequest);
    }

    public interface MessageDialogOnOpenListener {
        void onOpen(MessageDialogRequest messageDialogRequest);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void trace(String str) {
        CamLog.d(str);
    }

    enum Priority {
        IMMEDIATELY(0),
        NORMAL(1),
        LOW(2);

        protected final int priority;

        Priority(int i) {
            this.priority = i;
        }
    }

    private static class PriorityComparator implements Comparator<MessageDialogRequest>, Serializable {
        private PriorityComparator() {
        }

        @Override // java.util.Comparator
        public int compare(MessageDialogRequest messageDialogRequest, MessageDialogRequest messageDialogRequest2) {
            int i = messageDialogRequest.mDialogId.priority.priority;
            int i2 = messageDialogRequest2.mDialogId.priority.priority;
            if (i < i2) {
                return -1;
            }
            return i > i2 ? 1 : 0;
        }
    }

    public MessageDialogController(Activity activity, MessageSettings messageSettings, MessageDialogOnClickListener messageDialogOnClickListener, MessageDialogOnClickListener messageDialogOnClickListener2, MessageDialogOnCancelListener messageDialogOnCancelListener, MessageDialogOnDismissListener messageDialogOnDismissListener, MessageDialogOnOpenListener messageDialogOnOpenListener) {
        this.mContext = activity;
        this.mMessageSettings = messageSettings;
        this.mOnClickPositiveListener = messageDialogOnClickListener;
        this.mOnClickNegativeListener = messageDialogOnClickListener2;
        this.mOnCancelListener = messageDialogOnCancelListener;
        this.mOnDismissListener = messageDialogOnDismissListener;
        this.mOnOpenListener = messageDialogOnOpenListener;
    }

    public void setSensorOrientation(int i) {
        this.mSensorOrientation = i;
        Iterator<RotatableDialog> it = this.mDialogList.iterator();
        while (it.hasNext()) {
            it.next().setOrientation(i);
        }
    }

    public boolean request(MessageDialogRequest messageDialogRequest) throws Resources.NotFoundException {
        trace("request() E DLG_ID = " + messageDialogRequest.mDialogId);
        if (this.mCurrentDialogId != null && this.mCurrentDialogId.priority == Priority.IMMEDIATELY) {
            trace("request() current showing");
            return true;
        }
        if (isNeverShow(messageDialogRequest.mDialogId)) {
            trace("request() isNeverShow = true");
            return false;
        }
        this.mMessageList.add(messageDialogRequest);
        Collections.sort(this.mMessageList, new PriorityComparator());
        show();
        trace("request() X DLG_ID = " + messageDialogRequest.mDialogId);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void show() throws Resources.NotFoundException {
        trace("show() E");
        if (this.mMessageList.isEmpty()) {
            return;
        }
        if (this.mMessageList.get(0).mDialogId.priority != Priority.IMMEDIATELY && this.mCurrentDialogId != null) {
            trace("show() Message is shown");
            return;
        }
        this.mDialogList.add(this.mMessageList.get(0).mDialogId.builderType.create(this.mContext, this.mSensorOrientation, this.mMessageSettings, this.mMessageList.get(0), new OnClickPositiveListener(this.mMessageList.get(0)), new OnClickNegativeListener(this.mMessageList.get(0)), new OnCancelListener(this.mMessageList.get(0)), new OnDismissListener(this.mMessageList.get(0))));
        this.mOnOpenListener.onOpen(this.mMessageList.get(0));
        this.mDialogList.get(this.mDialogList.size() - 1).show();
        this.mCurrentDialogId = this.mMessageList.get(0).mDialogId;
        this.mMessageList.remove(0);
        trace("show() X");
    }

    private boolean isNeverShow(DialogId dialogId) {
        if (dialogId.getMessageType() != MessageType.NO_MESSAGE) {
            return this.mMessageSettings.isNeverShow(dialogId.getMessageType());
        }
        return false;
    }

    public void clear() {
        trace("clear()");
        for (RotatableDialog rotatableDialog : this.mDialogList) {
            rotatableDialog.setOnDismissListener(null);
            rotatableDialog.dismiss();
        }
        this.mDialogList.clear();
        this.mMessageList.clear();
        this.mCurrentDialogId = null;
    }

    public void removeDialogsInList(List<DialogId> list) {
        if (this.mCurrentDialogId != null && list.contains(this.mCurrentDialogId)) {
            trace("removeDialogsInList dismiss = " + this.mCurrentDialogId);
            this.mDialogList.get(0).setOnDismissListener(null);
            this.mDialogList.get(0).dismiss();
            this.mCurrentDialogId = null;
        }
        Iterator<MessageDialogRequest> it = this.mMessageList.iterator();
        while (it.hasNext()) {
            MessageDialogRequest next = it.next();
            if (list.contains(next.mDialogId)) {
                it.remove();
                trace("removeDialogsInList remove = " + next.mDialogId);
            }
        }
    }

    public boolean isOpened() {
        if (this.mCurrentDialogId != null) {
            trace("isOpened() true");
            return true;
        }
        trace("isOpened() false");
        return false;
    }

    public boolean isCurrentDialogInList(List<DialogId> list) {
        if (this.mCurrentDialogId == null) {
            return false;
        }
        trace("isCurrentDialogInList = " + list.contains(this.mCurrentDialogId));
        return list.contains(this.mCurrentDialogId);
    }

    private class OnClickPositiveListener implements DialogInterface.OnClickListener {
        private final MessageDialogRequest mParameter;

        OnClickPositiveListener(MessageDialogRequest messageDialogRequest) {
            this.mParameter = messageDialogRequest;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            MessageDialogController.trace("onClickPositiveListener onClick()");
            MessageDialogController.this.mOnClickPositiveListener.onClick(this.mParameter);
        }
    }

    private class OnClickNegativeListener implements DialogInterface.OnClickListener {
        private final MessageDialogRequest mParameter;

        OnClickNegativeListener(MessageDialogRequest messageDialogRequest) {
            this.mParameter = messageDialogRequest;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            MessageDialogController.trace("onClickNegativeListener onClick()");
            MessageDialogController.this.mOnClickNegativeListener.onClick(this.mParameter);
        }
    }

    private class OnCancelListener implements DialogInterface.OnCancelListener {
        private final MessageDialogRequest mParameter;

        OnCancelListener(MessageDialogRequest messageDialogRequest) {
            this.mParameter = messageDialogRequest;
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            MessageDialogController.trace("onCancelListener onCancel()");
            MessageDialogController.this.mOnCancelListener.onCancel(this.mParameter);
        }
    }

    private class OnDismissListener implements DialogInterface.OnDismissListener {
        private final MessageDialogRequest mParameter;

        OnDismissListener(MessageDialogRequest messageDialogRequest) {
            this.mParameter = messageDialogRequest;
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) throws Resources.NotFoundException {
            MessageDialogController.trace("onDismissListener onDismiss()");
            MessageDialogController.this.mOnDismissListener.onDismiss(this.mParameter);
            MessageDialogController.this.mDialogList.clear();
            MessageDialogController.this.mCurrentDialogId = null;
            MessageDialogController.this.show();
        }
    }
}
