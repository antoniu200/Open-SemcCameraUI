package com.sonyericsson.android.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.SettingUtil;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogController;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogRequest;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import com.sonyericsson.cameracommon.utility.ProductConfig;
import com.sonymobile.cameracommon.research.parameters.Event;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SetupWizardActivity extends Activity {
    static final int BACK_KEY = 1;
    static final String GEO_TAG_RESULT = "geo_tag_result";
    private static final long ON_RESUME_DELAY_MILLIS = 15;
    static final String SIDE_SENSE_RESULT = "side_sense_result";
    private static final String TAG = "SetupWizardActivity";
    private static final boolean TRACE = true;
    private Handler mMainHandler;
    private OrientationEventListener mOrientationEventListener;
    String[] REQUEST_LOCATION_PERMISSION = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    private final int REQUEST_CODE_FOR_PERMISSION = 256;
    private TutorialController mTutorial = null;
    private ViewGroup mRootView = null;
    private RotatableDialog mOptionalRuntimePermissionDialog = null;
    private int mOrientation = 0;
    private Intent mResultData = new Intent();
    private boolean mSkippedFirstOnResume = false;
    private boolean mIsGeotagEnabled = false;
    private MessageDialogController mMessageDialog = null;
    private MessageDialogCallbackAdapter mMessageCallback = new MessageDialogCallbackAdapter();
    private MessageDialogController.MessageDialogOnClickListener mPositiveClickListener = new MessageDialogController.MessageDialogOnClickListener() { // from class: com.sonyericsson.android.camera.SetupWizardActivity.1
        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnClickListener
        public void onClick(MessageDialogRequest messageDialogRequest) {
            switch (AnonymousClass11.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()]) {
                case 1:
                    SetupWizardActivity.this.mIsGeotagEnabled = true;
                    SetupWizardActivity.this.toExternalSettings(InterruptedBy.LOCATION_SETTING);
                    break;
                case 2:
                    SetupWizardActivity.this.toExternalSettings(InterruptedBy.SIDE_SENSE_SETTING);
                    break;
            }
        }
    };
    private MessageDialogController.MessageDialogOnClickListener mNegativeClickListener = new MessageDialogController.MessageDialogOnClickListener() { // from class: com.sonyericsson.android.camera.SetupWizardActivity.2
        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnClickListener
        public void onClick(MessageDialogRequest messageDialogRequest) {
            switch (AnonymousClass11.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()]) {
                case 1:
                    SetupWizardActivity.this.mIsGeotagEnabled = false;
                    if (!SetupWizardActivity.this.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
                        SetupWizardActivity.this.close();
                        break;
                    } else {
                        SetupWizardActivity.this.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
                        break;
                    }
                case 2:
                    SetupWizardActivity.this.setSideSenseResult(false);
                    SetupWizardActivity.this.setupCompleted();
                    break;
            }
        }
    };
    private MessageDialogController.MessageDialogOnDismissListener mDismissListener = new MessageDialogController.MessageDialogOnDismissListener() { // from class: com.sonyericsson.android.camera.SetupWizardActivity.3
        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnDismissListener
        public void onDismiss(MessageDialogRequest messageDialogRequest) {
            if (AnonymousClass11.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()] != 1) {
                return;
            }
            SetupWizardActivity.this.setGeoTagResult(SetupWizardActivity.this.mIsGeotagEnabled);
        }
    };
    private InterruptedBy mInterruptedBy = InterruptedBy.NONE;
    private final Runnable mOnResumeTasks = new Runnable() { // from class: com.sonyericsson.android.camera.SetupWizardActivity.6
        @Override // java.lang.Runnable
        public void run() {
            if (SetupWizardActivity.this.mSkippedFirstOnResume) {
                SetupWizardActivity.trace("Runnable --> onResumeTasks()");
                SetupWizardActivity.this.mSkippedFirstOnResume = false;
                SetupWizardActivity.this.onResumeTasks();
            }
        }
    };
    private final TutorialController.OnClickSetupWizardButtonListener mOnClickTutorialButtonListener = new TutorialController.OnClickSetupWizardButtonListener() { // from class: com.sonyericsson.android.camera.SetupWizardActivity.7
        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialController.OnClickSetupWizardButtonListener
        public void onAccepted(TutorialController.TutorialType tutorialType) throws Resources.NotFoundException {
            switch (tutorialType) {
                case SAVE_LOCATION:
                    if (!SetupWizardActivity.this.isRestrictedMode() || !SetupWizardActivity.this.isSecure()) {
                        if (SetupWizardActivity.this.isRestrictedMode() && !SetupWizardActivity.this.isSecure()) {
                            SetupWizardActivity.this.dismissKeyguard();
                        }
                        SetupWizardActivity.this.toExternalSettings(InterruptedBy.REQUEST_PERMISSION);
                        break;
                    } else if (PermissionsUtil.arePermissionsGranted(SetupWizardActivity.this, SetupWizardActivity.this.REQUEST_LOCATION_PERMISSION)) {
                        SetupWizardActivity.this.setGeoTagResult(true);
                        if (GeotagManager.isGeoTagEnabled(Geotag.ON, SetupWizardActivity.this)) {
                            if (!SetupWizardActivity.this.mTutorial.hasNext(tutorialType)) {
                                SetupWizardActivity.this.close();
                                break;
                            } else {
                                SetupWizardActivity.this.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
                                break;
                            }
                        } else {
                            MessageDialogRequest messageDialogRequest = new MessageDialogRequest();
                            messageDialogRequest.mDialogId = DialogId.LOCATION_SERVICE_DISABLE_ON_LAUNCH;
                            SetupWizardActivity.this.mMessageDialog.request(messageDialogRequest);
                            break;
                        }
                    } else {
                        SetupWizardActivity.this.setGeoTagResult(false);
                        SetupWizardActivity.this.showOptionalRuntimePermissionDialog();
                        break;
                    }
                    break;
                case SIDE_SENSE:
                    if (SettingUtil.isSideSenseEnabled(false)) {
                        SetupWizardActivity.this.setSideSenseResult(true);
                        SetupWizardActivity.this.setupCompleted();
                        break;
                    } else {
                        MessageDialogRequest messageDialogRequest2 = new MessageDialogRequest();
                        messageDialogRequest2.mDialogId = DialogId.SIDE_SENSE_DISABLE_ON_LAUNCH;
                        SetupWizardActivity.this.mMessageDialog.request(messageDialogRequest2);
                        break;
                    }
            }
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialController.OnClickSetupWizardButtonListener
        public void onDenied(TutorialController.TutorialType tutorialType) {
            switch (tutorialType) {
                case SAVE_LOCATION:
                    SetupWizardActivity.this.setGeoTagResult(false);
                    break;
                case SIDE_SENSE:
                    SetupWizardActivity.this.setSideSenseResult(false);
                    SetupWizardActivity.this.setupCompleted();
                    break;
            }
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialController.OnClickSetupWizardButtonListener
        public void onClose(List<TutorialController.TutorialType> list) {
            if (list.contains(TutorialController.TutorialType.SIDE_SENSE)) {
                SetupWizardActivity.this.setSideSenseResult(SettingUtil.isSideSenseEnabled(false));
            }
            SetupWizardActivity.this.findViewById(R.id.setup_wizard_background).setVisibility(8);
            SetupWizardActivity.this.mTutorial.close();
            SetupWizardActivity.this.setupCompleted();
        }
    };

    private enum InterruptedBy {
        NONE,
        REQUEST_PERMISSION,
        LOCATION_SETTING,
        SIDE_SENSE_SETTING
    }

    private boolean in(int i, int i2, int i3) {
        return i >= i2 && i < i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void trace(String str) {
        CamLog.d(str);
    }

    private static class MessageDialogCallbackAdapter implements MessageDialogController.MessageDialogOnDismissListener, MessageDialogController.MessageDialogOnOpenListener, MessageDialogController.MessageDialogOnCancelListener {
        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnCancelListener
        public void onCancel(MessageDialogRequest messageDialogRequest) {
        }

        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnDismissListener
        public void onDismiss(MessageDialogRequest messageDialogRequest) {
        }

        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnOpenListener
        public void onOpen(MessageDialogRequest messageDialogRequest) {
        }

        private MessageDialogCallbackAdapter() {
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        trace("onCreate() E");
        super.onCreate(bundle);
        if (isCalledFromOnLockScreen() && isRestrictedMode()) {
            getWindow().addFlags(524288);
        }
        getWindow().addFlags(256);
        getWindow().addFlags(512);
        this.mMessageDialog = new MessageDialogController(this, null, this.mPositiveClickListener, this.mNegativeClickListener, this.mMessageCallback, this.mDismissListener, this.mMessageCallback);
        this.mOrientation = LayoutOrientationResolver.getInstance().getConfigurationOrientation();
        this.mOrientationEventListener = new OrientationEventListener(this) { // from class: com.sonyericsson.android.camera.SetupWizardActivity.4
            @Override // android.view.OrientationEventListener
            public void onOrientationChanged(int i) throws Resources.NotFoundException {
                int orientation = SetupWizardActivity.this.getOrientation(i);
                if (SetupWizardActivity.this.mOrientation != orientation) {
                    SetupWizardActivity.this.mOrientation = orientation;
                    if (SetupWizardActivity.this.isPortrait()) {
                        SetupWizardActivity.trace("change to PORTRAIT.");
                    } else {
                        SetupWizardActivity.trace("change to LANDSCAPE.");
                    }
                    if (SetupWizardActivity.this.mMessageDialog != null) {
                        SetupWizardActivity.this.mMessageDialog.setSensorOrientation(orientation);
                    }
                    if (SetupWizardActivity.this.mTutorial != null) {
                        SetupWizardActivity.this.mTutorial.setUiOrientation(orientation);
                    }
                    if (SetupWizardActivity.this.mOptionalRuntimePermissionDialog != null) {
                        SetupWizardActivity.this.mOptionalRuntimePermissionDialog.setOrientation(orientation);
                    }
                }
            }
        };
        this.mOrientationEventListener.enable();
        setContentView(R.layout.activity_setup_wizard);
        this.mRootView = (ViewGroup) findViewById(R.id.setup_wizard_root_view);
        this.mTutorial = new TutorialController(this.mRootView, getWindow());
        this.mTutorial.setOnClickTutorialButtonListener(this.mOnClickTutorialButtonListener);
        this.mRootView.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() { // from class: com.sonyericsson.android.camera.SetupWizardActivity.5
            @Override // android.view.ViewTreeObserver.OnWindowAttachListener
            public void onWindowAttached() throws Resources.NotFoundException {
                SetupWizardActivity.trace("onWindowAttached() E");
                SetupWizardActivity.this.setupLayout();
                SetupWizardActivity.this.mMessageDialog.setSensorOrientation(SetupWizardActivity.this.mOrientation);
                SetupWizardActivity.this.mTutorial.setUiOrientation(SetupWizardActivity.this.mOrientation);
                SetupWizardActivity.this.mTutorial.open(TutorialController.OpenType.create(TutorialController.DisplayTrigger.SETUP_WIZARD), null, null);
                SetupWizardActivity.trace("onWindowAttached() X");
            }

            @Override // android.view.ViewTreeObserver.OnWindowAttachListener
            public void onWindowDetached() {
                SetupWizardActivity.trace("onWindowDetached() E");
                SetupWizardActivity.trace("onWindowDetached() X");
            }
        });
        trace("onCreate() X");
    }

    @Override // android.app.Activity
    protected void onResume() {
        trace("onResume() E");
        if (this.mMainHandler == null) {
            this.mMainHandler = new Handler(getMainLooper());
        }
        this.mMainHandler.removeCallbacks(this.mOnResumeTasks);
        if (this.mIsGeotagEnabled) {
            setGeoTagResult(GeotagManager.isGeoTagEnabled(Geotag.ON, this));
        }
        if (!this.mSkippedFirstOnResume) {
            this.mSkippedFirstOnResume = true;
            trace("onResume() --> postDelayed(mOnResumeTasks," + ON_RESUME_DELAY_MILLIS + ")");
            this.mMainHandler.postDelayed(this.mOnResumeTasks, ON_RESUME_DELAY_MILLIS);
        } else {
            trace("onResume() --> onResumeTasks()");
            this.mSkippedFirstOnResume = false;
            onResumeTasks();
        }
        super.onResume();
        trace("onResume() X");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onResumeTasks() {
        trace("onResumeTasks() E");
        this.mOrientationEventListener.enable();
        fromExternalSettings();
        trace("onResumeTasks() X");
    }

    @Override // android.app.Activity
    protected void onPause() {
        trace("onPause() E");
        if (this.mMainHandler != null) {
            this.mMainHandler.removeCallbacks(this.mOnResumeTasks);
        }
        if (!this.mSkippedFirstOnResume) {
            if (CamLog.VERBOSE) {
                CamLog.d("onPause() --> onPauseTasks()");
            }
            onPauseTasks();
        }
        super.onPause();
        trace("onPause() X");
    }

    private void onPauseTasks() {
        trace("onPauseTasks() E");
        this.mOrientationEventListener.disable();
        if (this.mInterruptedBy == InterruptedBy.NONE) {
            setResult(0, this.mResultData);
            LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.OTHER);
            LocalResearchUtil.getInstance().closeSetupWizard();
            this.mTutorial.close();
            finish();
        }
        trace("onPauseTasks() X");
    }

    @Override // android.app.Activity
    public void onStop() {
        trace("onStop() E");
        super.onStop();
        trace("onStop() X");
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        trace("onDestroy() E");
        super.onDestroy();
        trace("onDestroy() X");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPortrait() {
        return this.mOrientation == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissKeyguard() {
        ((KeyguardManager) getSystemService(KeyguardManager.class)).requestDismissKeyguard(this, null);
    }

    private boolean isCalledFromOnLockScreen() {
        if (getCallingActivity() == null) {
            return false;
        }
        return CameraActivityOnLockScreen.class.getName().equals(getCallingActivity().getClassName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRestrictedMode() {
        return ((KeyguardManager) getSystemService("keyguard")).isKeyguardLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSecure() {
        return ((KeyguardManager) getSystemService("keyguard")).isKeyguardSecure();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupLayout() {
        trace("setupLayout() E");
        ViewGroup.LayoutParams layoutParams = this.mRootView.getLayoutParams();
        Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(this);
        int iHeight = viewFinderSize.height();
        int iWidth = viewFinderSize.width();
        layoutParams.width = iWidth;
        layoutParams.height = iHeight;
        this.mRootView.setLayoutParams(layoutParams);
        this.mRootView.setPivotX(0.0f);
        this.mRootView.setPivotY(0.0f);
        this.mRootView.setRotation(90.0f);
        this.mRootView.setTranslationX(iHeight);
        View viewFindViewById = findViewById(R.id.setup_wizard_background);
        ViewGroup.LayoutParams layoutParams2 = viewFindViewById.getLayoutParams();
        layoutParams2.width = iHeight;
        layoutParams2.height = iWidth - LayoutDependencyResolver.getNavigationBarMargin(this);
        viewFindViewById.setLayoutParams(layoutParams2);
        viewFindViewById.setBackgroundResource(R.drawable.cam_launch_screen_wizard_port_icn);
        trace("setupLayout() X");
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (this.mTutorial.backToPreviousPage()) {
            return;
        }
        LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.BACK_KEY);
        LocalResearchUtil.getInstance().closeSetupWizard();
        if (this.mResultData.hasExtra(GEO_TAG_RESULT)) {
            if (this.mTutorial.getTutorialTypes().contains(TutorialController.TutorialType.SIDE_SENSE)) {
                setSideSenseResult(SettingUtil.isSideSenseEnabled(false));
            }
            setupCompleted();
        } else {
            setResult(1, this.mResultData);
            super.onBackPressed();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 27) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 27) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    @SuppressLint({"StringFormatInvalid"})
    public void showOptionalRuntimePermissionDialog() throws Resources.NotFoundException {
        RotatableDialog.Builder builder = new RotatableDialog.Builder(this);
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this);
        builder.setOnKeyListener(new KeyEventKiller());
        builder.setTitle(String.format(Locale.US, getString(R.string.cam_strings_runtime_permission_dialog2_title_txt), getResources().getString(getApplicationInfo().labelRes)));
        ViewGroup viewGroup = (ViewGroup) layoutInflaterFrom.inflate(R.layout.permission_post_dialog_save_location, (ViewGroup) null);
        TextView textView = (TextView) viewGroup.findViewById(R.id.alert_dialog_header_txt);
        TextView textView2 = (TextView) viewGroup.findViewById(R.id.name);
        TextView textView3 = (TextView) viewGroup.findViewById(R.id.description);
        TextView textView4 = (TextView) viewGroup.findViewById(R.id.alert_dialog_footer_txt);
        textView.setText(R.string.cam_strings_runtime_permission_dialog2_message1_txt);
        textView2.setText(getPermissionGroupLabel("android.permission.ACCESS_FINE_LOCATION"));
        textView3.setText(R.string.cam_strings_runtime_permission_rationale_location_txt);
        textView4.setText(R.string.cam_strings_runtime_permission_dialog2_message2_txt);
        builder.setViewAsScrollable(viewGroup);
        builder.setPositiveButton(R.string.cam_strings_runtime_permission_continue_button_txt, new DialogInterface.OnClickListener() { // from class: com.sonyericsson.android.camera.SetupWizardActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (SetupWizardActivity.this.isRestrictedMode() && !SetupWizardActivity.this.isSecure()) {
                    SetupWizardActivity.this.dismissKeyguard();
                }
                try {
                    SetupWizardActivity.this.startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + SetupWizardActivity.this.getPackageName())));
                } catch (ActivityNotFoundException e) {
                    CamLog.e("showOptionalRuntimePermissionDialog() launchApplicationSettings: failed.", e);
                }
                SetupWizardActivity.this.mOptionalRuntimePermissionDialog = null;
            }
        });
        builder.setNegativeButton(R.string.cam_strings_cancel_txt, new DialogInterface.OnClickListener() { // from class: com.sonyericsson.android.camera.SetupWizardActivity.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!SetupWizardActivity.this.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
                    SetupWizardActivity.this.close();
                } else {
                    SetupWizardActivity.this.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
                }
                SetupWizardActivity.this.mOptionalRuntimePermissionDialog = null;
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.sonyericsson.android.camera.SetupWizardActivity.10
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                if (!SetupWizardActivity.this.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
                    SetupWizardActivity.this.close();
                } else {
                    SetupWizardActivity.this.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
                }
                SetupWizardActivity.this.mOptionalRuntimePermissionDialog = null;
            }
        });
        builder.setCancelable(RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT);
        builder.setOrientation(this.mOrientation);
        this.mOptionalRuntimePermissionDialog = builder.createRotatableDialog();
        this.mOptionalRuntimePermissionDialog.show();
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x007c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String getPermissionGroupLabel(java.lang.String r6) throws android.content.pm.PackageManager.NameNotFoundException {
        /*
            r5 = this;
            boolean r0 = com.sonyericsson.android.camera.util.CamLog.VERBOSE
            if (r0 == 0) goto Ld
            java.lang.String r0 = "getPermissionGroupLabel() start"
            java.lang.String[] r0 = new java.lang.String[]{r0}
            com.sonyericsson.android.camera.util.CamLog.d(r0)
        Ld:
            java.lang.String r0 = ""
            r1 = 0
            r2 = 1
            android.content.pm.PackageManager r3 = r5.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            r4 = 128(0x80, float:1.8E-43)
            android.content.pm.PermissionInfo r6 = r3.getPermissionInfo(r6, r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            java.lang.String r6 = r6.group     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            java.lang.String r6 = r6.toString()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            android.content.pm.PackageManager r3 = r5.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            android.content.pm.PermissionGroupInfo r3 = r3.getPermissionGroupInfo(r6, r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            if (r3 == 0) goto L5c
            android.content.pm.PackageManager r5 = r5.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            java.lang.CharSequence r5 = r3.loadLabel(r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            boolean r3 = android.text.TextUtils.isEmpty(r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            if (r3 != 0) goto L5c
            java.lang.String r5 = r5.toString()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5e
            boolean r0 = com.sonyericsson.android.camera.util.CamLog.VERBOSE     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            if (r0 == 0) goto L78
            java.lang.String[] r0 = new java.lang.String[r2]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            r3.<init>()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            java.lang.String r4 = "getPermissionGroupLabel label :"
            r3.append(r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            r3.append(r6)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            java.lang.String r6 = r3.toString()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            r0[r1] = r6     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            com.sonyericsson.android.camera.util.CamLog.d(r0)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            goto L78
        L5a:
            r6 = move-exception
            goto L60
        L5c:
            r5 = r0
            goto L78
        L5e:
            r6 = move-exception
            r5 = r0
        L60:
            java.lang.String[] r0 = new java.lang.String[r2]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "getPermissionGroupLabel(): "
            r2.append(r3)
            r2.append(r6)
            java.lang.String r6 = r2.toString()
            r0[r1] = r6
            com.sonyericsson.android.camera.util.CamLog.e(r0)
        L78:
            boolean r6 = com.sonyericsson.android.camera.util.CamLog.VERBOSE
            if (r6 == 0) goto L85
            java.lang.String r6 = "getPermissionGroupLabel() end"
            java.lang.String[] r6 = new java.lang.String[]{r6}
            com.sonyericsson.android.camera.util.CamLog.d(r6)
        L85:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.android.camera.SetupWizardActivity.getPermissionGroupLabel(java.lang.String):java.lang.String");
    }

    private static class KeyEventKiller implements DialogInterface.OnKeyListener {
        @Override // android.content.DialogInterface.OnKeyListener
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            return i == 27 || i == 80 || i == 82;
        }

        private KeyEventKiller() {
        }
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) throws Resources.NotFoundException {
        trace("onRequestPermissionsResult() E");
        super.onRequestPermissionsResult(i, strArr, iArr);
        HashMap map = new HashMap();
        if (strArr.length != 0) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                map.put(strArr[i2], Integer.valueOf(iArr[i2]));
            }
        }
        if (map.containsKey(this.REQUEST_LOCATION_PERMISSION[0]) && map.containsKey(this.REQUEST_LOCATION_PERMISSION[1])) {
            if (((Integer) map.get(this.REQUEST_LOCATION_PERMISSION[0])).intValue() == 0 && ((Integer) map.get(this.REQUEST_LOCATION_PERMISSION[1])).intValue() == 0) {
                setGeoTagResult(true);
                if (GeotagManager.isGeoTagEnabled(Geotag.ON, this)) {
                    if (!this.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
                        close();
                    } else {
                        this.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
                    }
                } else {
                    MessageDialogRequest messageDialogRequest = new MessageDialogRequest();
                    messageDialogRequest.mDialogId = DialogId.LOCATION_SERVICE_DISABLE_ON_LAUNCH;
                    this.mMessageDialog.request(messageDialogRequest);
                }
            } else {
                setGeoTagResult(false);
                showOptionalRuntimePermissionDialog();
            }
        }
        trace("onRequestPermissionsResult() X");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOrientation(int i) {
        int mountAngle = (i + (360 - ProductConfig.getMountAngle(this))) % 360;
        int i2 = isPortrait() ? 60 : 30;
        int i3 = 90 + i2;
        if (in(mountAngle, 90 - i2, i3)) {
            return 1;
        }
        int i4 = 270 - i2;
        return (!in(mountAngle, i3, i4) && in(mountAngle, i4, 270 + i2)) ? 1 : 2;
    }

    /* renamed from: com.sonyericsson.android.camera.SetupWizardActivity$11, reason: invalid class name */
    static /* synthetic */ class AnonymousClass11 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId;

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$SetupWizardActivity$InterruptedBy[InterruptedBy.LOCATION_SETTING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$SetupWizardActivity$InterruptedBy[InterruptedBy.REQUEST_PERMISSION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$SetupWizardActivity$InterruptedBy[InterruptedBy.SIDE_SENSE_SETTING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType = new int[TutorialController.TutorialType.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SAVE_LOCATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SIDE_SENSE.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId = new int[DialogId.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.LOCATION_SERVICE_DISABLE_ON_LAUNCH.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.SIDE_SENSE_DISABLE_ON_LAUNCH.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toExternalSettings(InterruptedBy interruptedBy) {
        this.mInterruptedBy = interruptedBy;
        switch (this.mInterruptedBy) {
            case LOCATION_SETTING:
                setGeoTagResult(true);
                ApplicationLauncher.launchLocationSourceSettings(this);
                break;
            case REQUEST_PERMISSION:
                setGeoTagResult(true);
                requestPermissions(this.REQUEST_LOCATION_PERMISSION, 256);
                break;
            case SIDE_SENSE_SETTING:
                ApplicationLauncher.launchSideSenseSettings(this);
                break;
        }
    }

    private void fromExternalSettings() {
        int i = AnonymousClass11.$SwitchMap$com$sonyericsson$android$camera$SetupWizardActivity$InterruptedBy[this.mInterruptedBy.ordinal()];
        if (i != 1) {
            if (i == 3) {
                setSideSenseResult(SettingUtil.isSideSenseEnabled(false));
                setupCompleted();
            }
        } else if (!this.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
            close();
        } else {
            this.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
        }
        this.mInterruptedBy = InterruptedBy.NONE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSideSenseResult(boolean z) {
        this.mResultData.putExtra(SIDE_SENSE_RESULT, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setGeoTagResult(boolean z) {
        this.mResultData.putExtra(GEO_TAG_RESULT, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupCompleted() {
        setResult(-1, this.mResultData);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void close() {
        findViewById(R.id.setup_wizard_background).setVisibility(8);
        this.mTutorial.close();
        setupCompleted();
    }
}
