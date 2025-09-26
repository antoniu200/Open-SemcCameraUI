// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.messagedialog;

import java.io.Serializable;
import android.content.DialogInterface;
import java.util.Comparator;
import java.util.Collections;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnClickListener;
import com.sonyericsson.android.camera.setting.MessageType;
import java.util.ArrayList;
import android.app.Activity;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import java.util.List;
import android.content.Context;

public class MessageDialogController
{
    private static String TAG = "MessageDialogController";
    private static final boolean TRACE = true;
    private final Context mContext;
    private DialogId mCurrentDialogId;
    private final List<RotatableDialog> mDialogList;
    private final List<MessageDialogRequest> mMessageList;
    private final MessageSettings mMessageSettings;
    private final MessageDialogOnCancelListener mOnCancelListener;
    private final MessageDialogOnClickListener mOnClickNegativeListener;
    private final MessageDialogOnClickListener mOnClickPositiveListener;
    private final MessageDialogOnDismissListener mOnDismissListener;
    private final MessageDialogOnOpenListener mOnOpenListener;
    private int mSensorOrientation;
    
    public MessageDialogController(final Activity mContext, final MessageSettings mMessageSettings, final MessageDialogOnClickListener mOnClickPositiveListener, final MessageDialogOnClickListener mOnClickNegativeListener, final MessageDialogOnCancelListener mOnCancelListener, final MessageDialogOnDismissListener mOnDismissListener, final MessageDialogOnOpenListener mOnOpenListener) {
        this.mMessageList = new ArrayList<MessageDialogRequest>();
        this.mDialogList = new ArrayList<RotatableDialog>();
        this.mSensorOrientation = 0;
        this.mContext = (Context)mContext;
        this.mMessageSettings = mMessageSettings;
        this.mOnClickPositiveListener = mOnClickPositiveListener;
        this.mOnClickNegativeListener = mOnClickNegativeListener;
        this.mOnCancelListener = mOnCancelListener;
        this.mOnDismissListener = mOnDismissListener;
        this.mOnOpenListener = mOnOpenListener;
    }
    
    private boolean isNeverShow(final DialogId dialogId) {
        return dialogId.getMessageType() != MessageType.NO_MESSAGE && this.mMessageSettings.isNeverShow(dialogId.getMessageType());
    }
    
    private void show() {
        trace("show() E");
        if (this.mMessageList.isEmpty()) {
            return;
        }
        if (this.mMessageList.get(0).mDialogId.priority != Priority.IMMEDIATELY && this.mCurrentDialogId != null) {
            trace("show() Message is shown");
            return;
        }
        this.mDialogList.add(this.mMessageList.get(0).mDialogId.builderType.create(this.mContext, this.mSensorOrientation, this.mMessageSettings, this.mMessageList.get(0), (DialogInterface$OnClickListener)new OnClickPositiveListener(this.mMessageList.get(0)), (DialogInterface$OnClickListener)new OnClickNegativeListener(this.mMessageList.get(0)), (DialogInterface$OnCancelListener)new OnCancelListener(this.mMessageList.get(0)), (DialogInterface$OnDismissListener)new OnDismissListener(this.mMessageList.get(0))));
        this.mOnOpenListener.onOpen(this.mMessageList.get(0));
        this.mDialogList.get(this.mDialogList.size() - 1).show();
        this.mCurrentDialogId = this.mMessageList.get(0).mDialogId;
        this.mMessageList.remove(0);
        trace("show() X");
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    public void clear() {
        trace("clear()");
        for (final RotatableDialog rotatableDialog : this.mDialogList) {
            rotatableDialog.setOnDismissListener(null);
            rotatableDialog.dismiss();
        }
        this.mDialogList.clear();
        this.mMessageList.clear();
        this.mCurrentDialogId = null;
    }
    
    public boolean isCurrentDialogInList(final List<DialogId> list) {
        if (this.mCurrentDialogId == null) {
            return false;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("isCurrentDialogInList = ");
        sb.append(list.contains(this.mCurrentDialogId));
        trace(sb.toString());
        return list.contains(this.mCurrentDialogId);
    }
    
    public boolean isOpened() {
        if (this.mCurrentDialogId != null) {
            trace("isOpened() true");
            return true;
        }
        trace("isOpened() false");
        return false;
    }
    
    public void removeDialogsInList(final List<DialogId> list) {
        if (this.mCurrentDialogId != null && list.contains(this.mCurrentDialogId)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("removeDialogsInList dismiss = ");
            sb.append(this.mCurrentDialogId);
            trace(sb.toString());
            this.mDialogList.get(0).setOnDismissListener(null);
            this.mDialogList.get(0).dismiss();
            this.mCurrentDialogId = null;
        }
        final Iterator<MessageDialogRequest> iterator = this.mMessageList.iterator();
        while (iterator.hasNext()) {
            final MessageDialogRequest messageDialogRequest = iterator.next();
            if (list.contains(messageDialogRequest.mDialogId)) {
                iterator.remove();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("removeDialogsInList remove = ");
                sb2.append(messageDialogRequest.mDialogId);
                trace(sb2.toString());
            }
        }
    }
    
    public boolean request(final MessageDialogRequest messageDialogRequest) {
        final StringBuilder sb = new StringBuilder();
        sb.append("request() E DLG_ID = ");
        sb.append(messageDialogRequest.mDialogId);
        trace(sb.toString());
        if (this.mCurrentDialogId != null && this.mCurrentDialogId.priority == Priority.IMMEDIATELY) {
            trace("request() current showing");
            return true;
        }
        if (this.isNeverShow(messageDialogRequest.mDialogId)) {
            trace("request() isNeverShow = true");
            return false;
        }
        this.mMessageList.add(messageDialogRequest);
        Collections.sort(this.mMessageList, new PriorityComparator());
        this.show();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("request() X DLG_ID = ");
        sb2.append(messageDialogRequest.mDialogId);
        trace(sb2.toString());
        return true;
    }
    
    public void setSensorOrientation(final int n) {
        this.mSensorOrientation = n;
        final Iterator<RotatableDialog> iterator = this.mDialogList.iterator();
        while (iterator.hasNext()) {
            iterator.next().setOrientation(n);
        }
    }
    
    public interface MessageDialogOnCancelListener
    {
        void onCancel(final MessageDialogRequest p0);
    }
    
    public interface MessageDialogOnClickListener
    {
        void onClick(final MessageDialogRequest p0);
    }
    
    public interface MessageDialogOnDismissListener
    {
        void onDismiss(final MessageDialogRequest p0);
    }
    
    public interface MessageDialogOnOpenListener
    {
        void onOpen(final MessageDialogRequest p0);
    }
    
    private class OnCancelListener implements DialogInterface$OnCancelListener
    {
        private final MessageDialogRequest mParameter;
        final MessageDialogController this$0;
        
        OnCancelListener(final MessageDialogController this$0, final MessageDialogRequest mParameter) {
            this.this$0 = this$0;
            this.mParameter = mParameter;
        }
        
        public void onCancel(final DialogInterface dialogInterface) {
            trace("onCancelListener onCancel()");
            this.this$0.mOnCancelListener.onCancel(this.mParameter);
        }
    }
    
    private class OnClickNegativeListener implements DialogInterface$OnClickListener
    {
        private final MessageDialogRequest mParameter;
        final MessageDialogController this$0;
        
        OnClickNegativeListener(final MessageDialogController this$0, final MessageDialogRequest mParameter) {
            this.this$0 = this$0;
            this.mParameter = mParameter;
        }
        
        public void onClick(final DialogInterface dialogInterface, final int n) {
            trace("onClickNegativeListener onClick()");
            this.this$0.mOnClickNegativeListener.onClick(this.mParameter);
        }
    }
    
    private class OnClickPositiveListener implements DialogInterface$OnClickListener
    {
        private final MessageDialogRequest mParameter;
        final MessageDialogController this$0;
        
        OnClickPositiveListener(final MessageDialogController this$0, final MessageDialogRequest mParameter) {
            this.this$0 = this$0;
            this.mParameter = mParameter;
        }
        
        public void onClick(final DialogInterface dialogInterface, final int n) {
            trace("onClickPositiveListener onClick()");
            this.this$0.mOnClickPositiveListener.onClick(this.mParameter);
        }
    }
    
    private class OnDismissListener implements DialogInterface$OnDismissListener
    {
        private final MessageDialogRequest mParameter;
        final MessageDialogController this$0;
        
        OnDismissListener(final MessageDialogController this$0, final MessageDialogRequest mParameter) {
            this.this$0 = this$0;
            this.mParameter = mParameter;
        }
        
        public void onDismiss(final DialogInterface dialogInterface) {
            trace("onDismissListener onDismiss()");
            this.this$0.mOnDismissListener.onDismiss(this.mParameter);
            this.this$0.mDialogList.clear();
            this.this$0.mCurrentDialogId = null;
            this.this$0.show();
        }
    }
    
    enum Priority
    {
        private static final Priority[] $VALUES;
        
        IMMEDIATELY(0), 
        LOW(2), 
        NORMAL(1);
        
        protected final int priority;
        
        static {
            $VALUES = new Priority[] { Priority.IMMEDIATELY, Priority.NORMAL, Priority.LOW };
        }
        
        private Priority(final int priority) {
            this.priority = priority;
        }
    }
    
    private static class PriorityComparator implements Comparator<MessageDialogRequest>, Serializable
    {
        @Override
        public int compare(final MessageDialogRequest messageDialogRequest, final MessageDialogRequest messageDialogRequest2) {
            final int priority = messageDialogRequest.mDialogId.priority.priority;
            final int priority2 = messageDialogRequest2.mDialogId.priority.priority;
            if (priority < priority2) {
                return -1;
            }
            if (priority > priority2) {
                return 1;
            }
            return 0;
        }
    }
}
