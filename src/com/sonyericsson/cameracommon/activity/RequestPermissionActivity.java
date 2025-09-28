package com.sonyericsson.cameracommon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.InputDeviceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.intent.IntentConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RequestPermissionActivity extends Activity {
    private static int HIGHEST_PRIORITY = 0;
    private static int INVALID_ID = -1;
    private static int LOWEST_PRIORITY = 2147483646;
    public static final String TAG = "RequestPermissionActivity";
    private final int REQUEST_CODE_FOR_PERMISSION = 256;
    private final int ID_FOR_PRE_DIALOG = InputDeviceCompat.SOURCE_DPAD;
    private final int ID_FOR_POST_DIALOG = 514;
    private List<PermissionState> mPermissionStateList = null;
    private PermissionState mCurrentPermissionState = null;
    private AlertDialog mCurrentShownDialog = null;

    enum PermissionAction {
        UPDATE_STATE,
        DO_NOTHING,
        REQUEST_PERMISSIONS,
        SHOW_POST_DIALOG,
        FINISH
    }

    enum PermissionGroup {
        CAMERA("android.permission-group.CAMERA", Arrays.asList("android.permission.CAMERA"), RequestPermissionActivity.INVALID_ID, R.string.cam_strings_runtime_permission_rationale_camera_txt),
        MIC("android.permission-group.MICROPHONE", Arrays.asList("android.permission.RECORD_AUDIO"), RequestPermissionActivity.INVALID_ID, R.string.cam_strings_runtime_permission_rationale_microphone_txt),
        STORAGE("android.permission-group.STORAGE", Arrays.asList("android.permission.WRITE_EXTERNAL_STORAGE"), RequestPermissionActivity.INVALID_ID, R.string.cam_strings_runtime_permission_rationale_storage_txt),
        LOCATION("android.permission-group.LOCATION", Arrays.asList("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"), R.string.cam_strings_runtime_permission_rationale_location_txt, R.string.cam_strings_runtime_permission_rationale_location_txt);

        private String mPermissionGroupName;
        private List<String> mPermissionList;
        private int mPostDialogMessageId;
        private int mPreDialogMessageId;

        PermissionGroup(String str, @NonNull List list, int i, int i2) {
            this.mPermissionGroupName = str;
            this.mPermissionList = list;
            this.mPreDialogMessageId = i;
            this.mPostDialogMessageId = i2;
        }

        public String getGroupName() {
            return this.mPermissionGroupName;
        }

        public List<String> getPermissionList() {
            return this.mPermissionList;
        }

        public boolean contains(String str) {
            return this.mPermissionList.contains(str);
        }

        public int getPreDialogMessageId() {
            return this.mPreDialogMessageId;
        }

        public int getPostDialogMessageId() {
            return this.mPostDialogMessageId;
        }
    }

    enum PermissionCategory {
        MANDATORY(Arrays.asList(PermissionGroup.CAMERA, PermissionGroup.MIC, PermissionGroup.STORAGE), RequestPermissionActivity.HIGHEST_PRIORITY),
        OPTIONAL(Arrays.asList(PermissionGroup.LOCATION), RequestPermissionActivity.HIGHEST_PRIORITY + 1);

        private List<PermissionGroup> mGroupList;
        private int mPriority;

        PermissionCategory(List list, int i) {
            this.mGroupList = list;
            this.mPriority = i;
        }

        public List<PermissionGroup> getGroupList() {
            return this.mGroupList;
        }

        public int getPriority() {
            return this.mPriority;
        }
    }

    class PermissionState {
        private final PermissionCategory mCategory;
        private final List<PermissionGroup> mRequestGroupList;
        private boolean mRequested = false;

        PermissionState(PermissionCategory permissionCategory, List<PermissionGroup> list) {
            this.mCategory = permissionCategory;
            this.mRequestGroupList = list;
        }

        public PermissionCategory getCategory() {
            return this.mCategory;
        }

        public List<PermissionGroup> getRequestGroupList() {
            return this.mRequestGroupList;
        }

        public boolean isRequested() {
            return this.mRequested;
        }

        public void setRequested() {
            this.mRequested = true;
        }

        public String[] getRequestPermissionList() {
            ArrayList arrayList = new ArrayList();
            if (this.mRequestGroupList != null) {
                Iterator<PermissionGroup> it = this.mRequestGroupList.iterator();
                while (it.hasNext()) {
                    Iterator<String> it2 = it.next().getPermissionList().iterator();
                    while (it2.hasNext()) {
                        arrayList.add(it2.next());
                    }
                }
                return (String[]) arrayList.toArray(new String[0]);
            }
            return new String[0];
        }

        public boolean areAllPermissionsGranted() {
            for (String str : getRequestPermissionList()) {
                if (RequestPermissionActivity.this.checkSelfPermission(str) != 0) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            return this.mCategory.name() + ": Group num=" + this.mRequestGroupList.size() + ", requested=" + this.mRequested;
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate() start");
        }
        super.onCreate(bundle);
        setContentView(R.layout.activity_request_permission);
        if (isRestrictedMode()) {
            if (isSecure()) {
                getWindow().addFlags(524288);
            } else {
                dismissKeyguard();
            }
        }
        Intent intent = getIntent();
        if (intent == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("onCreate() finish():intent = null");
            }
            finishActivity();
            return;
        }
        this.mPermissionStateList = createPermissionStateList(intent.getStringArrayListExtra(IntentConstants.EXTRA_PERMISSIONS_LIST));
        updateCurrentState();
        if (this.mCurrentPermissionState == null) {
            finishActivity();
            if (CamLog.VERBOSE) {
                CamLog.d("onCreate() finish()");
                return;
            }
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate() end");
        }
    }

    private void dismissKeyguard() {
        ((KeyguardManager) getSystemService(KeyguardManager.class)).requestDismissKeyguard(this, null);
    }

    private void finishActivity() {
        setResult(-1, new Intent());
        finish();
    }

    private List<PermissionState> createPermissionStateList(List<String> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("createPermissionStateList() start");
        }
        ArrayList arrayList = new ArrayList();
        for (PermissionCategory permissionCategory : PermissionCategory.values()) {
            ArrayList arrayList2 = new ArrayList();
            for (PermissionGroup permissionGroup : permissionCategory.getGroupList()) {
                if (list != null) {
                    Iterator<String> it = list.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            String next = it.next();
                            if (permissionGroup.contains(next) && checkSelfPermission(next) != 0) {
                                arrayList2.add(permissionGroup);
                                break;
                            }
                        }
                    }
                }
            }
            if (arrayList2.size() != 0) {
                arrayList.add(new PermissionState(permissionCategory, arrayList2));
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("createPermissionStateList() end:PermissionState num:" + arrayList.size() + ", " + arrayList.toString());
        }
        return arrayList;
    }

    private void updateCurrentState() {
        if (CamLog.VERBOSE) {
            CamLog.d("updateCurrentState() start");
        }
        int priority = HIGHEST_PRIORITY - 1;
        if (this.mCurrentPermissionState != null) {
            priority = this.mCurrentPermissionState.getCategory().getPriority();
            if (CamLog.VERBOSE) {
                CamLog.d("updateCurrentState() before state=" + this.mCurrentPermissionState.getCategory().name());
            }
        } else if (CamLog.VERBOSE) {
            CamLog.d("updateCurrentState() before state=null");
        }
        int i = LOWEST_PRIORITY + 1;
        int i2 = i;
        PermissionState permissionState = null;
        for (PermissionState permissionState2 : this.mPermissionStateList) {
            int priority2 = permissionState2.getCategory().getPriority();
            boolean zIsRequested = permissionState2.isRequested();
            if (priority < priority2 && priority2 < i2 && !zIsRequested) {
                permissionState = permissionState2;
                i2 = priority2;
            }
        }
        if (this.mCurrentPermissionState == permissionState) {
            this.mCurrentPermissionState = null;
        } else {
            this.mCurrentPermissionState = permissionState;
        }
        if (CamLog.VERBOSE) {
            if (this.mCurrentPermissionState != null) {
                CamLog.d("updateCurrentState() after state=" + this.mCurrentPermissionState.getCategory().name());
            } else {
                CamLog.d("updateCurrentState() after state=null");
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("updateCurrentState() end");
        }
    }

    @Override // android.app.Activity
    protected void onResume() throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("onResume() start");
        }
        super.onResume();
        if (CamLog.VERBOSE) {
            if (this.mCurrentPermissionState != null) {
                CamLog.d("onResume() state=" + this.mCurrentPermissionState.getCategory().name());
            } else {
                CamLog.d("onResume() state=null");
            }
        }
        doNextAction();
        if (CamLog.VERBOSE) {
            CamLog.d("onResume() end");
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        if (CamLog.VERBOSE) {
            CamLog.d("onStop() start");
        }
        super.onStop();
        if (this.mCurrentShownDialog != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("onStop() Current dialog is shown.");
            }
            finishActivity();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onStop() end");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doNextAction() throws Resources.NotFoundException {
        PermissionAction permissionActionDecideNextAction = decideNextAction();
        while (permissionActionDecideNextAction == PermissionAction.UPDATE_STATE) {
            updateCurrentState();
            permissionActionDecideNextAction = decideNextAction();
        }
        boolean zDoAction = doAction(permissionActionDecideNextAction);
        if (permissionActionDecideNextAction == PermissionAction.SHOW_POST_DIALOG) {
            updateCurrentState();
        }
        if (zDoAction) {
            return;
        }
        doNextAction();
    }

    private PermissionAction decideNextAction() {
        PermissionAction permissionAction;
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
        if (this.mCurrentPermissionState.isRequested()) {
            if (this.mCurrentPermissionState.areAllPermissionsGranted()) {
                permissionAction = PermissionAction.UPDATE_STATE;
            } else {
                permissionAction = PermissionAction.SHOW_POST_DIALOG;
            }
        } else if (isSecure() && isRestrictedMode()) {
            permissionAction = PermissionAction.SHOW_POST_DIALOG;
        } else {
            permissionAction = PermissionAction.REQUEST_PERMISSIONS;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("decideNextAction() end:" + permissionAction.name());
        }
        return permissionAction;
    }

    private boolean doAction(PermissionAction permissionAction) throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("doAction() start:" + permissionAction.name());
        }
        String[] requestPermissionList = new String[0];
        if (this.mCurrentPermissionState != null) {
            requestPermissionList = this.mCurrentPermissionState.getRequestPermissionList();
        }
        switch (permissionAction) {
            case REQUEST_PERMISSIONS:
                ArrayList arrayList = new ArrayList();
                for (String str : requestPermissionList) {
                    if (shouldShowRequestPermissionRationale(str)) {
                        Iterator<PermissionGroup> it = this.mCurrentPermissionState.getRequestGroupList().iterator();
                        while (true) {
                            if (it.hasNext()) {
                                PermissionGroup next = it.next();
                                if (next.contains(str) && next.getPreDialogMessageId() != INVALID_ID && !arrayList.contains(next)) {
                                    arrayList.add(next);
                                }
                            }
                        }
                    }
                }
                if (arrayList.size() == 0) {
                    requestPermissions(requestPermissionList);
                    break;
                } else {
                    showPermissionPreDialog(arrayList);
                    break;
                }
                break;
            case SHOW_POST_DIALOG:
                ArrayList arrayList2 = new ArrayList();
                for (String str2 : requestPermissionList) {
                    if (checkSelfPermission(str2) != 0) {
                        Iterator<PermissionGroup> it2 = this.mCurrentPermissionState.getRequestGroupList().iterator();
                        while (true) {
                            if (it2.hasNext()) {
                                PermissionGroup next2 = it2.next();
                                if (next2.contains(str2) && next2.getPostDialogMessageId() != INVALID_ID && !arrayList2.contains(next2)) {
                                    arrayList2.add(next2);
                                }
                            }
                        }
                    }
                }
                if (arrayList2.size() == 0) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("doAction() end:not done");
                    }
                    return false;
                }
                showPermissionPostDialog(arrayList2);
                break;
                break;
            case FINISH:
                finishActivity();
                break;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("doAction() end:done");
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestPermissions(String[] strArr) {
        if (CamLog.VERBOSE) {
            CamLog.d("requestPermissions() start");
        }
        requestPermissions(strArr, 256);
        this.mCurrentPermissionState.setRequested();
        if (CamLog.VERBOSE) {
            CamLog.d("requestPermissions() end");
        }
    }

    private void showPermissionPreDialog(List<PermissionGroup> list) throws Resources.NotFoundException {
        showPermissionDialog(InputDeviceCompat.SOURCE_DPAD, list);
    }

    private void showPermissionPostDialog(List<PermissionGroup> list) throws Resources.NotFoundException {
        showPermissionDialog(514, list);
    }

    private void showPermissionDialog(int i, List<PermissionGroup> list) throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("showPermissionDialog() start");
        }
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String string = getResources().getString(getApplicationInfo().labelRes);
        if (i == 513) {
            ViewGroup viewGroup = (ViewGroup) layoutInflaterFrom.inflate(R.layout.permission_pre_dialog, (ViewGroup) null);
            ((TextView) viewGroup.findViewById(R.id.alert_dialog_header_txt)).setText(String.format(Locale.US, getResources().getString(R.string.cam_strings_runtime_permission_dialog1_message_txt), string));
            ListView listView = (ListView) viewGroup.findViewById(R.id.permission_list);
            if (listView != null) {
                listView.setAdapter((ListAdapter) new PermissionAdapter(this, i, list));
            }
            builder.setTitle(String.format(Locale.US, getResources().getString(R.string.cam_strings_runtime_permission_dialog1_title_txt), string));
            builder.setView(viewGroup);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.sonyericsson.cameracommon.activity.RequestPermissionActivity.1
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialogInterface) {
                    RequestPermissionActivity.this.requestPermissions(RequestPermissionActivity.this.mCurrentPermissionState.getRequestPermissionList());
                    RequestPermissionActivity.this.mCurrentShownDialog = null;
                }
            });
            builder.setPositiveButton(R.string.cam_strings_ok_txt, new DialogInterface.OnClickListener() { // from class: com.sonyericsson.cameracommon.activity.RequestPermissionActivity.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                }
            });
            this.mCurrentShownDialog = builder.create();
            this.mCurrentShownDialog.getWindow().addFlags(128);
            this.mCurrentShownDialog.show();
        } else if (i == 514) {
            ViewGroup viewGroup2 = (ViewGroup) layoutInflaterFrom.inflate(R.layout.permission_post_dialog, (ViewGroup) null);
            ((TextView) viewGroup2.findViewById(R.id.alert_dialog_header_txt)).setText(R.string.cam_strings_runtime_permission_dialog2_message1_txt);
            ListView listView2 = (ListView) viewGroup2.findViewById(R.id.permission_list);
            if (listView2 != null) {
                listView2.setAdapter((ListAdapter) new PermissionAdapter(this, i, list));
            }
            ((TextView) viewGroup2.findViewById(R.id.alert_dialog_footer_txt)).setText(R.string.cam_strings_runtime_permission_dialog2_message2_txt);
            builder.setTitle(String.format(Locale.US, getResources().getString(R.string.cam_strings_runtime_permission_dialog2_title_txt), string));
            builder.setView(viewGroup2);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.cam_strings_runtime_permission_continue_button_txt, new DialogInterface.OnClickListener() { // from class: com.sonyericsson.cameracommon.activity.RequestPermissionActivity.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + RequestPermissionActivity.this.getPackageName()));
                    try {
                        RequestPermissionActivity.this.startActivity(intent);
                        if (CamLog.VERBOSE) {
                            CamLog.d("showPermissionDialog() launchApplicationSettings: " + intent);
                        }
                    } catch (ActivityNotFoundException e) {
                        CamLog.e("showPermissionDialog() launchApplicationSettings: failed.", e);
                    }
                    RequestPermissionActivity.this.mCurrentShownDialog = null;
                }
            });
            builder.setNegativeButton(R.string.cam_strings_cancel_txt, new DialogInterface.OnClickListener() { // from class: com.sonyericsson.cameracommon.activity.RequestPermissionActivity.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) throws Resources.NotFoundException {
                    if (CamLog.VERBOSE) {
                        CamLog.d("showPermissionDialog() don't show global settings dialog");
                    }
                    RequestPermissionActivity.this.mCurrentShownDialog = null;
                    RequestPermissionActivity.this.doNextAction();
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.sonyericsson.cameracommon.activity.RequestPermissionActivity.5
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialogInterface) {
                    RequestPermissionActivity.this.mCurrentShownDialog = null;
                }
            });
            this.mCurrentShownDialog = builder.create();
            this.mCurrentShownDialog.show();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("showPermissionDialog() end");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getPermissionGroupLabel(com.sonyericsson.cameracommon.activity.RequestPermissionActivity.PermissionGroup r6) throws android.content.pm.PackageManager.NameNotFoundException {
        /*
            r5 = this;
            boolean r0 = com.sonyericsson.android.camera.util.CamLog.VERBOSE
            if (r0 == 0) goto Ld
            java.lang.String r0 = "getPermissionGroupLabel() start"
            java.lang.String[] r0 = new java.lang.String[]{r0}
            com.sonyericsson.android.camera.util.CamLog.d(r0)
        Ld:
            java.lang.String r0 = ""
            java.lang.String r6 = r6.getGroupName()
            r1 = 0
            r2 = 1
            android.content.pm.PackageManager r3 = r5.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L54
            r4 = 128(0x80, float:1.8E-43)
            android.content.pm.PermissionGroupInfo r3 = r3.getPermissionGroupInfo(r6, r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L54
            if (r3 == 0) goto L52
            android.content.pm.PackageManager r5 = r5.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L54
            java.lang.CharSequence r5 = r3.loadLabel(r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L54
            boolean r3 = android.text.TextUtils.isEmpty(r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L54
            if (r3 != 0) goto L52
            java.lang.String r5 = r5.toString()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L54
            boolean r0 = com.sonyericsson.android.camera.util.CamLog.VERBOSE     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L50
            if (r0 == 0) goto L6e
            java.lang.String[] r0 = new java.lang.String[r2]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L50
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L50
            r3.<init>()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L50
            java.lang.String r4 = "getPermissionGroupLabel label :"
            r3.append(r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L50
            r3.append(r6)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L50
            java.lang.String r6 = r3.toString()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L50
            r0[r1] = r6     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L50
            com.sonyericsson.android.camera.util.CamLog.d(r0)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L50
            goto L6e
        L50:
            r6 = move-exception
            goto L56
        L52:
            r5 = r0
            goto L6e
        L54:
            r6 = move-exception
            r5 = r0
        L56:
            java.lang.String[] r0 = new java.lang.String[r2]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "getPermissionGroupLabel(): "
            r2.append(r3)
            r2.append(r6)
            java.lang.String r6 = r2.toString()
            r0[r1] = r6
            com.sonyericsson.android.camera.util.CamLog.e(r0)
        L6e:
            boolean r6 = com.sonyericsson.android.camera.util.CamLog.VERBOSE
            if (r6 == 0) goto L7b
            java.lang.String r6 = "getPermissionGroupLabel() end"
            java.lang.String[] r6 = new java.lang.String[]{r6}
            com.sonyericsson.android.camera.util.CamLog.d(r6)
        L7b:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.activity.RequestPermissionActivity.getPermissionGroupLabel(com.sonyericsson.cameracommon.activity.RequestPermissionActivity$PermissionGroup):java.lang.String");
    }

    @Override // android.app.Activity
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

    private boolean isRestrictedMode() {
        return ((KeyguardManager) getSystemService("keyguard")).isKeyguardLocked();
    }

    private boolean isSecure() {
        return ((KeyguardManager) getSystemService("keyguard")).isKeyguardSecure();
    }

    private class PermissionAdapter implements ListAdapter {
        private final Context mContext;
        private List<PermissionGroup> mGroupList;
        private final int mId;

        @Override // android.widget.ListAdapter
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // android.widget.Adapter
        public int getViewTypeCount() {
            return 1;
        }

        @Override // android.widget.Adapter
        public boolean hasStableIds() {
            return false;
        }

        @Override // android.widget.ListAdapter
        public boolean isEnabled(int i) {
            return false;
        }

        @Override // android.widget.Adapter
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        }

        @Override // android.widget.Adapter
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        }

        public PermissionAdapter(Context context, int i, List<PermissionGroup> list) {
            this.mContext = context;
            this.mId = i;
            this.mGroupList = list;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (this.mGroupList == null) {
                return 0;
            }
            return this.mGroupList.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            if (this.mGroupList == null) {
                return null;
            }
            return this.mGroupList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_permission, (ViewGroup) null);
            }
            PermissionGroup permissionGroup = (PermissionGroup) getItem(i);
            TextView textView = (TextView) view.findViewById(R.id.name);
            TextView textView2 = (TextView) view.findViewById(R.id.description);
            if (this.mId == 513 && permissionGroup != null && permissionGroup.getPreDialogMessageId() != RequestPermissionActivity.INVALID_ID) {
                textView.setText(RequestPermissionActivity.this.getPermissionGroupLabel(permissionGroup));
                textView2.setText(RequestPermissionActivity.this.getResources().getString(permissionGroup.getPreDialogMessageId()));
            } else if (this.mId == 514 && permissionGroup != null && permissionGroup.getPostDialogMessageId() != RequestPermissionActivity.INVALID_ID) {
                textView.setText(RequestPermissionActivity.this.getPermissionGroupLabel(permissionGroup));
                textView2.setText(RequestPermissionActivity.this.getResources().getString(permissionGroup.getPostDialogMessageId()));
            }
            return view;
        }

        @Override // android.widget.Adapter
        public boolean isEmpty() {
            return getCount() < 1;
        }
    }
}
