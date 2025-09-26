// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.activity;

import android.support.annotation.NonNull;
import java.util.Arrays;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.content.DialogInterface$OnClickListener;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.Locale;
import android.widget.TextView;
import android.view.ViewGroup;
import android.app.AlertDialog$Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import android.text.TextUtils;
import android.content.Intent;
import android.app.KeyguardManager$KeyguardDismissCallback;
import android.app.KeyguardManager;
import java.util.Iterator;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.List;
import android.app.AlertDialog;
import android.app.Activity;

public class RequestPermissionActivity extends Activity
{
    private static int HIGHEST_PRIORITY = 0;
    private static int INVALID_ID = -1;
    private static int LOWEST_PRIORITY = 2147483646;
    public static final String TAG = "RequestPermissionActivity";
    private final int ID_FOR_POST_DIALOG;
    private final int ID_FOR_PRE_DIALOG;
    private final int REQUEST_CODE_FOR_PERMISSION;
    private PermissionState mCurrentPermissionState;
    private AlertDialog mCurrentShownDialog;
    private List<PermissionState> mPermissionStateList;
    
    public RequestPermissionActivity() {
        this.REQUEST_CODE_FOR_PERMISSION = 256;
        this.ID_FOR_PRE_DIALOG = 513;
        this.ID_FOR_POST_DIALOG = 514;
        this.mPermissionStateList = null;
        this.mCurrentPermissionState = null;
        this.mCurrentShownDialog = null;
    }
    
    private List<PermissionState> createPermissionStateList(final List<String> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("createPermissionStateList() start");
        }
        final ArrayList list2 = new ArrayList();
        for (final PermissionCategory permissionCategory : PermissionCategory.values()) {
            final ArrayList list3 = new ArrayList();
            for (final PermissionGroup permissionGroup : permissionCategory.getGroupList()) {
                if (list != null) {
                    for (final String s : list) {
                        if (permissionGroup.contains(s) && this.checkSelfPermission(s) != 0) {
                            list3.add(permissionGroup);
                            break;
                        }
                    }
                }
            }
            if (list3.size() != 0) {
                list2.add(new PermissionState(permissionCategory, list3));
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("createPermissionStateList() end:PermissionState num:");
            sb.append(list2.size());
            sb.append(", ");
            sb.append(list2.toString());
            CamLog.d(sb.toString());
        }
        return list2;
    }
    
    private PermissionAction decideNextAction() {
        if (CamLog.VERBOSE) {
            CamLog.d("decideNextAction() start");
        }
        if (this.mCurrentShownDialog != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("decideNextAction() end:DO_NOTHING");
            }
            return PermissionAction.DO_NOTHING;
        }
        if (this.mCurrentPermissionState == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("decideNextAction() end:FINISH");
            }
            return PermissionAction.FINISH;
        }
        PermissionAction permissionAction;
        if (this.mCurrentPermissionState.isRequested()) {
            if (this.mCurrentPermissionState.areAllPermissionsGranted()) {
                permissionAction = PermissionAction.UPDATE_STATE;
            }
            else {
                permissionAction = PermissionAction.SHOW_POST_DIALOG;
            }
        }
        else if (this.isSecure() && this.isRestrictedMode()) {
            permissionAction = PermissionAction.SHOW_POST_DIALOG;
        }
        else {
            permissionAction = PermissionAction.REQUEST_PERMISSIONS;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("decideNextAction() end:");
            sb.append(permissionAction.name());
            CamLog.d(sb.toString());
        }
        return permissionAction;
    }
    
    private void dismissKeyguard() {
        ((KeyguardManager)this.getSystemService((Class)KeyguardManager.class)).requestDismissKeyguard((Activity)this, (KeyguardManager$KeyguardDismissCallback)null);
    }
    
    private boolean doAction(final PermissionAction permissionAction) {
        final boolean verbose = CamLog.VERBOSE;
        int i = 0;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("doAction() start:");
            sb.append(permissionAction.name());
            CamLog.d(sb.toString());
        }
        String[] requestPermissionList = new String[0];
        if (this.mCurrentPermissionState != null) {
            requestPermissionList = this.mCurrentPermissionState.getRequestPermissionList();
        }
        switch (RequestPermissionActivity$6.$SwitchMap$com$sonyericsson$cameracommon$activity$RequestPermissionActivity$PermissionAction[permissionAction.ordinal()]) {
            case 3: {
                this.finishActivity();
                break;
            }
            case 2: {
                final ArrayList list = new ArrayList();
                for (final String s : requestPermissionList) {
                    if (this.checkSelfPermission(s) != 0) {
                        for (final PermissionGroup permissionGroup : this.mCurrentPermissionState.getRequestGroupList()) {
                            if (permissionGroup.contains(s) && permissionGroup.getPostDialogMessageId() != RequestPermissionActivity.INVALID_ID && !list.contains(permissionGroup)) {
                                list.add(permissionGroup);
                                break;
                            }
                        }
                    }
                }
                if (list.size() == 0) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("doAction() end:not done");
                    }
                    return false;
                }
                this.showPermissionPostDialog(list);
                break;
            }
            case 1: {
                final ArrayList list2 = new ArrayList();
                while (i < requestPermissionList.length) {
                    final String s2 = requestPermissionList[i];
                    if (this.shouldShowRequestPermissionRationale(s2)) {
                        for (final PermissionGroup permissionGroup2 : this.mCurrentPermissionState.getRequestGroupList()) {
                            if (permissionGroup2.contains(s2) && permissionGroup2.getPreDialogMessageId() != RequestPermissionActivity.INVALID_ID && !list2.contains(permissionGroup2)) {
                                list2.add(permissionGroup2);
                                break;
                            }
                        }
                    }
                    ++i;
                }
                if (list2.size() == 0) {
                    this.requestPermissions(requestPermissionList);
                    break;
                }
                this.showPermissionPreDialog(list2);
                break;
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("doAction() end:done");
        }
        return true;
    }
    
    private void doNextAction() {
        PermissionAction permissionAction;
        for (permissionAction = this.decideNextAction(); permissionAction == PermissionAction.UPDATE_STATE; permissionAction = this.decideNextAction()) {
            this.updateCurrentState();
        }
        final boolean doAction = this.doAction(permissionAction);
        if (permissionAction == PermissionAction.SHOW_POST_DIALOG) {
            this.updateCurrentState();
        }
        if (!doAction) {
            this.doNextAction();
        }
    }
    
    private void finishActivity() {
        this.setResult(-1, new Intent());
        this.finish();
    }
    
    private String getPermissionGroupLabel(final PermissionGroup permissionGroup) {
        if (CamLog.VERBOSE) {
            CamLog.d("getPermissionGroupLabel() start");
        }
        final String groupName = permissionGroup.getGroupName();
        String string = null;
        Label_0168: {
            PackageManager$NameNotFoundException obj = null;
            Label_0132: {
                try {
                    final PermissionGroupInfo permissionGroupInfo = this.getPackageManager().getPermissionGroupInfo(groupName, 128);
                    if (permissionGroupInfo != null) {
                        final CharSequence loadLabel = permissionGroupInfo.loadLabel(this.getPackageManager());
                        if (!TextUtils.isEmpty(loadLabel)) {
                            final String s = string = loadLabel.toString();
                            try {
                                if (CamLog.VERBOSE) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("getPermissionGroupLabel label :");
                                    sb.append(groupName);
                                    CamLog.d(sb.toString());
                                    string = s;
                                }
                                break Label_0168;
                            }
                            catch (final PackageManager$NameNotFoundException ex) {
                                string = s;
                                obj = ex;
                                break Label_0132;
                            }
                        }
                    }
                    string = "";
                    break Label_0168;
                }
                catch (final PackageManager$NameNotFoundException obj) {
                    string = "";
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("getPermissionGroupLabel(): ");
            sb2.append(obj);
            CamLog.e(sb2.toString());
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getPermissionGroupLabel() end");
        }
        return string;
    }
    
    private boolean isRestrictedMode() {
        return ((KeyguardManager)this.getSystemService("keyguard")).isKeyguardLocked();
    }
    
    private boolean isSecure() {
        return ((KeyguardManager)this.getSystemService("keyguard")).isKeyguardSecure();
    }
    
    private void requestPermissions(final String[] array) {
        if (CamLog.VERBOSE) {
            CamLog.d("requestPermissions() start");
        }
        this.requestPermissions(array, 256);
        this.mCurrentPermissionState.setRequested();
        if (CamLog.VERBOSE) {
            CamLog.d("requestPermissions() end");
        }
    }
    
    private void showPermissionDialog(final int n, final List<PermissionGroup> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("showPermissionDialog() start");
        }
        final LayoutInflater from = LayoutInflater.from((Context)this);
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        final String string = this.getResources().getString(this.getApplicationInfo().labelRes);
        if (n == 513) {
            final ViewGroup view = (ViewGroup)from.inflate(2131492967, (ViewGroup)null);
            ((TextView)view.findViewById(2131296289)).setText((CharSequence)String.format(Locale.US, this.getResources().getString(2131690047), string));
            final ListView listView = (ListView)view.findViewById(2131296493);
            if (listView != null) {
                listView.setAdapter((ListAdapter)new PermissionAdapter((Context)this, n, list));
            }
            alertDialog$Builder.setTitle((CharSequence)String.format(Locale.US, this.getResources().getString(2131690048), string));
            alertDialog$Builder.setView((View)view);
            alertDialog$Builder.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener(this) {
                final RequestPermissionActivity this$0;
                
                public void onDismiss(final DialogInterface dialogInterface) {
                    this.this$0.requestPermissions(this.this$0.mCurrentPermissionState.getRequestPermissionList());
                    this.this$0.mCurrentShownDialog = null;
                }
            });
            alertDialog$Builder.setPositiveButton(2131689975, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this) {
                final RequestPermissionActivity this$0;
                
                public void onClick(final DialogInterface dialogInterface, final int n) {
                }
            });
            this.mCurrentShownDialog = alertDialog$Builder.create();
            this.mCurrentShownDialog.getWindow().addFlags(128);
            this.mCurrentShownDialog.show();
        }
        else if (n == 514) {
            final ViewGroup view2 = (ViewGroup)from.inflate(2131492964, (ViewGroup)null);
            ((TextView)view2.findViewById(2131296289)).setText(2131690049);
            final ListView listView2 = (ListView)view2.findViewById(2131296493);
            if (listView2 != null) {
                listView2.setAdapter((ListAdapter)new PermissionAdapter((Context)this, n, list));
            }
            ((TextView)view2.findViewById(2131296288)).setText(2131690050);
            alertDialog$Builder.setTitle((CharSequence)String.format(Locale.US, this.getResources().getString(2131690051), string));
            alertDialog$Builder.setView((View)view2);
            alertDialog$Builder.setCancelable(false);
            alertDialog$Builder.setPositiveButton(2131690046, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this) {
                final RequestPermissionActivity this$0;
                
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("package:");
                    sb.append(this.this$0.getPackageName());
                    final Intent obj = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse(sb.toString()));
                    try {
                        this.this$0.startActivity(obj);
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("showPermissionDialog() launchApplicationSettings: ");
                            sb2.append(obj);
                            CamLog.d(sb2.toString());
                        }
                    }
                    catch (final ActivityNotFoundException ex) {
                        CamLog.e("showPermissionDialog() launchApplicationSettings: failed.", (Throwable)ex);
                    }
                    this.this$0.mCurrentShownDialog = null;
                }
            });
            alertDialog$Builder.setNegativeButton(2131689666, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this) {
                final RequestPermissionActivity this$0;
                
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("showPermissionDialog() don't show global settings dialog");
                    }
                    this.this$0.mCurrentShownDialog = null;
                    this.this$0.doNextAction();
                }
            });
            alertDialog$Builder.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener(this) {
                final RequestPermissionActivity this$0;
                
                public void onDismiss(final DialogInterface dialogInterface) {
                    this.this$0.mCurrentShownDialog = null;
                }
            });
            (this.mCurrentShownDialog = alertDialog$Builder.create()).show();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("showPermissionDialog() end");
        }
    }
    
    private void showPermissionPostDialog(final List<PermissionGroup> list) {
        this.showPermissionDialog(514, list);
    }
    
    private void showPermissionPreDialog(final List<PermissionGroup> list) {
        this.showPermissionDialog(513, list);
    }
    
    private void updateCurrentState() {
        if (CamLog.VERBOSE) {
            CamLog.d("updateCurrentState() start");
        }
        final int n = RequestPermissionActivity.HIGHEST_PRIORITY - 1;
        int priority;
        if (this.mCurrentPermissionState != null) {
            priority = this.mCurrentPermissionState.getCategory().getPriority();
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("updateCurrentState() before state=");
                sb.append(this.mCurrentPermissionState.getCategory().name());
                CamLog.d(sb.toString());
                priority = priority;
            }
        }
        else {
            priority = n;
            if (CamLog.VERBOSE) {
                CamLog.d("updateCurrentState() before state=null");
                priority = n;
            }
        }
        int lowest_PRIORITY = RequestPermissionActivity.LOWEST_PRIORITY;
        final Iterator<PermissionState> iterator = this.mPermissionStateList.iterator();
        ++lowest_PRIORITY;
        PermissionState mCurrentPermissionState = null;
        while (iterator.hasNext()) {
            final PermissionState permissionState = iterator.next();
            final int priority2 = permissionState.getCategory().getPriority();
            final boolean requested = permissionState.isRequested();
            if (priority < priority2 && priority2 < lowest_PRIORITY && !requested) {
                mCurrentPermissionState = permissionState;
                lowest_PRIORITY = priority2;
            }
        }
        if (this.mCurrentPermissionState == mCurrentPermissionState) {
            this.mCurrentPermissionState = null;
        }
        else {
            this.mCurrentPermissionState = mCurrentPermissionState;
        }
        if (CamLog.VERBOSE) {
            if (this.mCurrentPermissionState != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("updateCurrentState() after state=");
                sb2.append(this.mCurrentPermissionState.getCategory().name());
                CamLog.d(sb2.toString());
            }
            else {
                CamLog.d("updateCurrentState() after state=null");
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("updateCurrentState() end");
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate() start");
        }
        super.onCreate(bundle);
        this.setContentView(2131492892);
        if (this.isRestrictedMode()) {
            if (this.isSecure()) {
                this.getWindow().addFlags(524288);
            }
            else {
                this.dismissKeyguard();
            }
        }
        final Intent intent = this.getIntent();
        if (intent == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("onCreate() finish():intent = null");
            }
            this.finishActivity();
            return;
        }
        this.mPermissionStateList = this.createPermissionStateList(intent.getStringArrayListExtra("permissions_list"));
        this.updateCurrentState();
        if (this.mCurrentPermissionState == null) {
            this.finishActivity();
            if (CamLog.VERBOSE) {
                CamLog.d("onCreate() finish()");
            }
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate() end");
        }
    }
    
    protected void onDestroy() {
        if (CamLog.VERBOSE) {
            CamLog.d("onDestroy() start");
        }
        super.onDestroy();
        this.mCurrentPermissionState = null;
        this.mPermissionStateList = null;
        this.mCurrentShownDialog = null;
        if (CamLog.VERBOSE) {
            CamLog.d("onDestroy() end");
        }
    }
    
    protected void onResume() {
        if (CamLog.VERBOSE) {
            CamLog.d("onResume() start");
        }
        super.onResume();
        if (CamLog.VERBOSE) {
            if (this.mCurrentPermissionState != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onResume() state=");
                sb.append(this.mCurrentPermissionState.getCategory().name());
                CamLog.d(sb.toString());
            }
            else {
                CamLog.d("onResume() state=null");
            }
        }
        this.doNextAction();
        if (CamLog.VERBOSE) {
            CamLog.d("onResume() end");
        }
    }
    
    protected void onStop() {
        if (CamLog.VERBOSE) {
            CamLog.d("onStop() start");
        }
        super.onStop();
        if (this.mCurrentShownDialog != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("onStop() Current dialog is shown.");
            }
            this.finishActivity();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onStop() end");
        }
    }
    
    enum PermissionAction
    {
        private static final PermissionAction[] $VALUES;
        
        DO_NOTHING, 
        FINISH, 
        REQUEST_PERMISSIONS, 
        SHOW_POST_DIALOG, 
        UPDATE_STATE;
        
        static {
            $VALUES = new PermissionAction[] { PermissionAction.UPDATE_STATE, PermissionAction.DO_NOTHING, PermissionAction.REQUEST_PERMISSIONS, PermissionAction.SHOW_POST_DIALOG, PermissionAction.FINISH };
        }
    }
    
    private class PermissionAdapter implements ListAdapter
    {
        private final Context mContext;
        private List<PermissionGroup> mGroupList;
        private final int mId;
        final RequestPermissionActivity this$0;
        
        public PermissionAdapter(final RequestPermissionActivity this$0, final Context mContext, final int mId, final List<PermissionGroup> mGroupList) {
            this.this$0 = this$0;
            this.mContext = mContext;
            this.mId = mId;
            this.mGroupList = mGroupList;
        }
        
        public boolean areAllItemsEnabled() {
            return false;
        }
        
        public int getCount() {
            if (this.mGroupList == null) {
                return 0;
            }
            return this.mGroupList.size();
        }
        
        public Object getItem(final int n) {
            if (this.mGroupList == null) {
                return null;
            }
            return this.mGroupList.get(n);
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public int getItemViewType(final int n) {
            return 0;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            View inflate = view;
            if (view == null) {
                inflate = LayoutInflater.from(this.mContext).inflate(2131492939, (ViewGroup)null);
            }
            final PermissionGroup permissionGroup = (PermissionGroup)this.getItem(n);
            final TextView textView = (TextView)inflate.findViewById(2131296477);
            final TextView textView2 = (TextView)inflate.findViewById(2131296384);
            if (this.mId == 513 && permissionGroup != null && permissionGroup.getPreDialogMessageId() != RequestPermissionActivity.INVALID_ID) {
                textView.setText((CharSequence)this.this$0.getPermissionGroupLabel(permissionGroup));
                textView2.setText((CharSequence)this.this$0.getResources().getString(permissionGroup.getPreDialogMessageId()));
            }
            else if (this.mId == 514 && permissionGroup != null && permissionGroup.getPostDialogMessageId() != RequestPermissionActivity.INVALID_ID) {
                textView.setText((CharSequence)this.this$0.getPermissionGroupLabel(permissionGroup));
                textView2.setText((CharSequence)this.this$0.getResources().getString(permissionGroup.getPostDialogMessageId()));
            }
            return inflate;
        }
        
        public int getViewTypeCount() {
            return 1;
        }
        
        public boolean hasStableIds() {
            return false;
        }
        
        public boolean isEmpty() {
            final int count = this.getCount();
            boolean b = true;
            if (count >= 1) {
                b = false;
            }
            return b;
        }
        
        public boolean isEnabled(final int n) {
            return false;
        }
        
        public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
        }
        
        public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
        }
    }
    
    enum PermissionCategory
    {
        private static final PermissionCategory[] $VALUES;
        
        MANDATORY(Arrays.asList(PermissionGroup.CAMERA, PermissionGroup.MIC, PermissionGroup.STORAGE), RequestPermissionActivity.HIGHEST_PRIORITY), 
        OPTIONAL(Arrays.asList(PermissionGroup.LOCATION), RequestPermissionActivity.HIGHEST_PRIORITY + 1);
        
        private List<PermissionGroup> mGroupList;
        private int mPriority;
        
        static {
            $VALUES = new PermissionCategory[] { PermissionCategory.MANDATORY, PermissionCategory.OPTIONAL };
        }
        
        private PermissionCategory(final List<PermissionGroup> mGroupList, final int mPriority) {
            this.mGroupList = mGroupList;
            this.mPriority = mPriority;
        }
        
        public List<PermissionGroup> getGroupList() {
            return this.mGroupList;
        }
        
        public int getPriority() {
            return this.mPriority;
        }
    }
    
    enum PermissionGroup
    {
        private static final PermissionGroup[] $VALUES;
        
        CAMERA("android.permission-group.CAMERA", Arrays.asList("android.permission.CAMERA"), RequestPermissionActivity.INVALID_ID, 2131690052), 
        LOCATION("android.permission-group.LOCATION", Arrays.asList("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"), 2131690053, 2131690053), 
        MIC("android.permission-group.MICROPHONE", Arrays.asList("android.permission.RECORD_AUDIO"), RequestPermissionActivity.INVALID_ID, 2131690054), 
        STORAGE("android.permission-group.STORAGE", Arrays.asList("android.permission.WRITE_EXTERNAL_STORAGE"), RequestPermissionActivity.INVALID_ID, 2131690055);
        
        private String mPermissionGroupName;
        private List<String> mPermissionList;
        private int mPostDialogMessageId;
        private int mPreDialogMessageId;
        
        static {
            $VALUES = new PermissionGroup[] { PermissionGroup.CAMERA, PermissionGroup.MIC, PermissionGroup.STORAGE, PermissionGroup.LOCATION };
        }
        
        private PermissionGroup(final String mPermissionGroupName, final List<String> mPermissionList, final int mPreDialogMessageId, final int mPostDialogMessageId) {
            this.mPermissionGroupName = mPermissionGroupName;
            this.mPermissionList = mPermissionList;
            this.mPreDialogMessageId = mPreDialogMessageId;
            this.mPostDialogMessageId = mPostDialogMessageId;
        }
        
        public boolean contains(final String s) {
            return this.mPermissionList.contains(s);
        }
        
        public String getGroupName() {
            return this.mPermissionGroupName;
        }
        
        public List<String> getPermissionList() {
            return this.mPermissionList;
        }
        
        public int getPostDialogMessageId() {
            return this.mPostDialogMessageId;
        }
        
        public int getPreDialogMessageId() {
            return this.mPreDialogMessageId;
        }
    }
    
    class PermissionState
    {
        private final PermissionCategory mCategory;
        private final List<PermissionGroup> mRequestGroupList;
        private boolean mRequested;
        final RequestPermissionActivity this$0;
        
        PermissionState(final RequestPermissionActivity this$0, final PermissionCategory mCategory, final List<PermissionGroup> mRequestGroupList) {
            this.this$0 = this$0;
            this.mCategory = mCategory;
            this.mRequestGroupList = mRequestGroupList;
            this.mRequested = false;
        }
        
        public boolean areAllPermissionsGranted() {
            final String[] requestPermissionList = this.getRequestPermissionList();
            for (int length = requestPermissionList.length, i = 0; i < length; ++i) {
                if (this.this$0.checkSelfPermission(requestPermissionList[i]) != 0) {
                    return false;
                }
            }
            return true;
        }
        
        public PermissionCategory getCategory() {
            return this.mCategory;
        }
        
        public List<PermissionGroup> getRequestGroupList() {
            return this.mRequestGroupList;
        }
        
        public String[] getRequestPermissionList() {
            final ArrayList list = new ArrayList();
            if (this.mRequestGroupList != null) {
                final Iterator<PermissionGroup> iterator = this.mRequestGroupList.iterator();
                while (iterator.hasNext()) {
                    final Iterator<String> iterator2 = iterator.next().getPermissionList().iterator();
                    while (iterator2.hasNext()) {
                        list.add(iterator2.next());
                    }
                }
                return (String[])list.toArray(new String[0]);
            }
            return new String[0];
        }
        
        public boolean isRequested() {
            return this.mRequested;
        }
        
        public void setRequested() {
            this.mRequested = true;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mCategory.name());
            sb.append(": Group num=");
            sb.append(this.mRequestGroupList.size());
            sb.append(", requested=");
            sb.append(this.mRequested);
            return sb.toString();
        }
    }
}
