// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import android.annotation.SuppressLint;
import android.content.DialogInterface$OnDismissListener;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.widget.TextView;
import java.util.Locale;
import android.content.DialogInterface$OnKeyListener;
import android.view.LayoutInflater;
import java.util.HashMap;
import android.view.KeyEvent;
import com.sonyericsson.android.camera.view.tutorial.TutorialContentView;
import com.sonyericsson.android.camera.setting.StoredSettings;
import android.view.ViewTreeObserver$OnWindowAttachListener;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.android.camera.setting.MessageSettings;
import android.os.Bundle;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import android.view.View;
import android.graphics.Rect;
import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import android.text.TextUtils;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.ProductConfig;
import android.app.KeyguardManager$KeyguardDismissCallback;
import android.app.KeyguardManager;
import java.util.List;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import android.content.Context;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.android.camera.util.SettingUtil;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogRequest;
import android.view.ViewGroup;
import android.content.Intent;
import android.view.OrientationEventListener;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import android.os.Handler;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogController;
import android.app.Activity;

public class SetupWizardActivity extends Activity
{
    static final int BACK_KEY = 1;
    static final String GEO_TAG_RESULT = "geo_tag_result";
    private static final long ON_RESUME_DELAY_MILLIS = 15L;
    static final String SIDE_SENSE_RESULT = "side_sense_result";
    private static final String TAG = "SetupWizardActivity";
    private static final boolean TRACE = true;
    private final int REQUEST_CODE_FOR_PERMISSION;
    String[] REQUEST_LOCATION_PERMISSION;
    private MessageDialogController.MessageDialogOnDismissListener mDismissListener;
    private InterruptedBy mInterruptedBy;
    private boolean mIsGeotagEnabled;
    private Handler mMainHandler;
    private MessageDialogCallbackAdapter mMessageCallback;
    private MessageDialogController mMessageDialog;
    private MessageDialogController.MessageDialogOnClickListener mNegativeClickListener;
    private final TutorialController.OnClickSetupWizardButtonListener mOnClickTutorialButtonListener;
    private final Runnable mOnResumeTasks;
    private RotatableDialog mOptionalRuntimePermissionDialog;
    private int mOrientation;
    private OrientationEventListener mOrientationEventListener;
    private MessageDialogController.MessageDialogOnClickListener mPositiveClickListener;
    private Intent mResultData;
    private ViewGroup mRootView;
    private boolean mSkippedFirstOnResume;
    private TutorialController mTutorial;
    
    public SetupWizardActivity() {
        this.REQUEST_LOCATION_PERMISSION = new String[] { "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION" };
        this.REQUEST_CODE_FOR_PERMISSION = 256;
        this.mTutorial = null;
        this.mRootView = null;
        this.mOptionalRuntimePermissionDialog = null;
        this.mOrientation = 0;
        this.mResultData = new Intent();
        this.mSkippedFirstOnResume = false;
        this.mIsGeotagEnabled = false;
        this.mMessageDialog = null;
        this.mMessageCallback = new MessageDialogCallbackAdapter();
        this.mPositiveClickListener = new MessageDialogController.MessageDialogOnClickListener() {
            final SetupWizardActivity this$0;
            
            @Override
            public void onClick(final MessageDialogRequest messageDialogRequest) {
                switch (SetupWizardActivity$11.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()]) {
                    case 2: {
                        this.this$0.toExternalSettings(InterruptedBy.SIDE_SENSE_SETTING);
                        break;
                    }
                    case 1: {
                        this.this$0.mIsGeotagEnabled = true;
                        this.this$0.toExternalSettings(InterruptedBy.LOCATION_SETTING);
                        break;
                    }
                }
            }
        };
        this.mNegativeClickListener = new MessageDialogController.MessageDialogOnClickListener() {
            final SetupWizardActivity this$0;
            
            @Override
            public void onClick(final MessageDialogRequest messageDialogRequest) {
                switch (SetupWizardActivity$11.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()]) {
                    case 2: {
                        this.this$0.setSideSenseResult(false);
                        this.this$0.setupCompleted();
                        break;
                    }
                    case 1: {
                        this.this$0.mIsGeotagEnabled = false;
                        if (!this.this$0.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
                            this.this$0.close();
                            break;
                        }
                        this.this$0.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
                        break;
                    }
                }
            }
        };
        this.mDismissListener = new MessageDialogController.MessageDialogOnDismissListener() {
            final SetupWizardActivity this$0;
            
            @Override
            public void onDismiss(final MessageDialogRequest messageDialogRequest) {
                if (SetupWizardActivity$11.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()] == 1) {
                    this.this$0.setGeoTagResult(this.this$0.mIsGeotagEnabled);
                }
            }
        };
        this.mInterruptedBy = InterruptedBy.NONE;
        this.mOnResumeTasks = new Runnable() {
            final SetupWizardActivity this$0;
            
            @Override
            public void run() {
                if (this.this$0.mSkippedFirstOnResume) {
                    trace("Runnable --> onResumeTasks()");
                    this.this$0.mSkippedFirstOnResume = false;
                    this.this$0.onResumeTasks();
                }
            }
        };
        this.mOnClickTutorialButtonListener = new TutorialController.OnClickSetupWizardButtonListener() {
            final SetupWizardActivity this$0;
            
            @Override
            public void onAccepted(final TutorialType tutorialType) {
                switch (SetupWizardActivity$11.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()]) {
                    case 2: {
                        if (SettingUtil.isSideSenseEnabled(false)) {
                            this.this$0.setSideSenseResult(true);
                            this.this$0.setupCompleted();
                            break;
                        }
                        final MessageDialogRequest messageDialogRequest = new MessageDialogRequest();
                        messageDialogRequest.mDialogId = DialogId.SIDE_SENSE_DISABLE_ON_LAUNCH;
                        this.this$0.mMessageDialog.request(messageDialogRequest);
                        break;
                    }
                    case 1: {
                        if (!this.this$0.isRestrictedMode() || !this.this$0.isSecure()) {
                            if (this.this$0.isRestrictedMode() && !this.this$0.isSecure()) {
                                this.this$0.dismissKeyguard();
                            }
                            this.this$0.toExternalSettings(InterruptedBy.REQUEST_PERMISSION);
                            break;
                        }
                        if (!PermissionsUtil.arePermissionsGranted((Context)this.this$0, this.this$0.REQUEST_LOCATION_PERMISSION)) {
                            this.this$0.setGeoTagResult(false);
                            this.this$0.showOptionalRuntimePermissionDialog();
                            break;
                        }
                        this.this$0.setGeoTagResult(true);
                        if (!GeotagManager.isGeoTagEnabled(Geotag.ON, (Context)this.this$0)) {
                            final MessageDialogRequest messageDialogRequest2 = new MessageDialogRequest();
                            messageDialogRequest2.mDialogId = DialogId.LOCATION_SERVICE_DISABLE_ON_LAUNCH;
                            this.this$0.mMessageDialog.request(messageDialogRequest2);
                            break;
                        }
                        if (!this.this$0.mTutorial.hasNext(tutorialType)) {
                            this.this$0.close();
                            break;
                        }
                        this.this$0.mTutorial.doNextAction(TutorialType.SAVE_LOCATION);
                        break;
                    }
                }
            }
            
            @Override
            public void onClose(final List<TutorialType> list) {
                if (list.contains(TutorialType.SIDE_SENSE)) {
                    this.this$0.setSideSenseResult(SettingUtil.isSideSenseEnabled(false));
                }
                this.this$0.findViewById(2131296602).setVisibility(8);
                this.this$0.mTutorial.close();
                this.this$0.setupCompleted();
            }
            
            @Override
            public void onDenied(final TutorialType tutorialType) {
                switch (SetupWizardActivity$11.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()]) {
                    case 2: {
                        this.this$0.setSideSenseResult(false);
                        this.this$0.setupCompleted();
                        break;
                    }
                    case 1: {
                        this.this$0.setGeoTagResult(false);
                        break;
                    }
                }
            }
        };
    }
    
    private void close() {
        this.findViewById(2131296602).setVisibility(8);
        this.mTutorial.close();
        this.setupCompleted();
    }
    
    private void dismissKeyguard() {
        ((KeyguardManager)this.getSystemService((Class)KeyguardManager.class)).requestDismissKeyguard((Activity)this, (KeyguardManager$KeyguardDismissCallback)null);
    }
    
    private void fromExternalSettings() {
        final int n = SetupWizardActivity$11.$SwitchMap$com$sonyericsson$android$camera$SetupWizardActivity$InterruptedBy[this.mInterruptedBy.ordinal()];
        if (n != 1) {
            if (n == 3) {
                this.setSideSenseResult(SettingUtil.isSideSenseEnabled(false));
                this.setupCompleted();
            }
        }
        else if (!this.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
            this.close();
        }
        else {
            this.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
        }
        this.mInterruptedBy = InterruptedBy.NONE;
    }
    
    private int getOrientation(int n) {
        final int n2 = (n + (360 - ProductConfig.getMountAngle((Context)this))) % 360;
        if (this.isPortrait()) {
            n = 60;
        }
        else {
            n = 30;
        }
        final int n3 = 90 + n;
        if (this.in(n2, 90 - n, n3)) {
            return 1;
        }
        final int n4 = 270 - n;
        if (this.in(n2, n3, n4)) {
            return 2;
        }
        if (this.in(n2, n4, 270 + n)) {
            return 1;
        }
        return 2;
    }
    
    private String getPermissionGroupLabel(String string) {
        if (CamLog.VERBOSE) {
            CamLog.d("getPermissionGroupLabel() start");
        }
        Label_0181: {
            PackageManager$NameNotFoundException obj = null;
            Label_0145: {
                try {
                    final String string2 = this.getPackageManager().getPermissionInfo(string, 128).group.toString();
                    final PermissionGroupInfo permissionGroupInfo = this.getPackageManager().getPermissionGroupInfo(string2, 128);
                    if (permissionGroupInfo != null) {
                        final CharSequence loadLabel = permissionGroupInfo.loadLabel(this.getPackageManager());
                        if (!TextUtils.isEmpty(loadLabel)) {
                            final String s = string = loadLabel.toString();
                            try {
                                if (CamLog.VERBOSE) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("getPermissionGroupLabel label :");
                                    sb.append(string2);
                                    CamLog.d(sb.toString());
                                    string = s;
                                }
                                break Label_0181;
                            }
                            catch (final PackageManager$NameNotFoundException ex) {
                                string = s;
                                obj = ex;
                                break Label_0145;
                            }
                        }
                    }
                    string = "";
                    break Label_0181;
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
    
    private boolean in(final int n, final int n2, final int n3) {
        return n >= n2 && n < n3;
    }
    
    private boolean isCalledFromOnLockScreen() {
        return this.getCallingActivity() != null && CameraActivityOnLockScreen.class.getName().equals(this.getCallingActivity().getClassName());
    }
    
    private boolean isPortrait() {
        final int mOrientation = this.mOrientation;
        boolean b = true;
        if (mOrientation != 1) {
            b = false;
        }
        return b;
    }
    
    private boolean isRestrictedMode() {
        return ((KeyguardManager)this.getSystemService("keyguard")).isKeyguardLocked();
    }
    
    private boolean isSecure() {
        return ((KeyguardManager)this.getSystemService("keyguard")).isKeyguardSecure();
    }
    
    private void onPauseTasks() {
        trace("onPauseTasks() E");
        this.mOrientationEventListener.disable();
        if (this.mInterruptedBy == InterruptedBy.NONE) {
            this.setResult(0, this.mResultData);
            LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.OTHER);
            LocalResearchUtil.getInstance().closeSetupWizard();
            this.mTutorial.close();
            this.finish();
        }
        trace("onPauseTasks() X");
    }
    
    private void onResumeTasks() {
        trace("onResumeTasks() E");
        this.mOrientationEventListener.enable();
        this.fromExternalSettings();
        trace("onResumeTasks() X");
    }
    
    private void setGeoTagResult(final boolean b) {
        this.mResultData.putExtra("geo_tag_result", b);
    }
    
    private void setSideSenseResult(final boolean b) {
        this.mResultData.putExtra("side_sense_result", b);
    }
    
    private void setupCompleted() {
        this.setResult(-1, this.mResultData);
        this.finish();
    }
    
    private void setupLayout() {
        trace("setupLayout() E");
        final ViewGroup$LayoutParams layoutParams = this.mRootView.getLayoutParams();
        final Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize((Context)this);
        final int height = viewFinderSize.height();
        final int width = viewFinderSize.width();
        layoutParams.width = width;
        layoutParams.height = height;
        this.mRootView.setLayoutParams(layoutParams);
        this.mRootView.setPivotX(0.0f);
        this.mRootView.setPivotY(0.0f);
        this.mRootView.setRotation(90.0f);
        this.mRootView.setTranslationX((float)height);
        final View viewById = this.findViewById(2131296602);
        final ViewGroup$LayoutParams layoutParams2 = viewById.getLayoutParams();
        layoutParams2.width = height;
        layoutParams2.height = width - LayoutDependencyResolver.getNavigationBarMargin((Context)this);
        viewById.setLayoutParams(layoutParams2);
        viewById.setBackgroundResource(2131231240);
        trace("setupLayout() X");
    }
    
    private void toExternalSettings(final InterruptedBy mInterruptedBy) {
        this.mInterruptedBy = mInterruptedBy;
        switch (SetupWizardActivity$11.$SwitchMap$com$sonyericsson$android$camera$SetupWizardActivity$InterruptedBy[this.mInterruptedBy.ordinal()]) {
            case 3: {
                ApplicationLauncher.launchSideSenseSettings(this);
                break;
            }
            case 2: {
                this.setGeoTagResult(true);
                this.requestPermissions(this.REQUEST_LOCATION_PERMISSION, 256);
                break;
            }
            case 1: {
                this.setGeoTagResult(true);
                ApplicationLauncher.launchLocationSourceSettings(this);
                break;
            }
        }
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    public void onBackPressed() {
        if (this.mTutorial.backToPreviousPage()) {
            return;
        }
        LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.BACK_KEY);
        LocalResearchUtil.getInstance().closeSetupWizard();
        if (this.mResultData.hasExtra("geo_tag_result")) {
            if (this.mTutorial.getTutorialTypes().contains(TutorialController.TutorialType.SIDE_SENSE)) {
                this.setSideSenseResult(SettingUtil.isSideSenseEnabled(false));
            }
            this.setupCompleted();
            return;
        }
        this.setResult(1, this.mResultData);
        super.onBackPressed();
    }
    
    protected void onCreate(final Bundle bundle) {
        trace("onCreate() E");
        super.onCreate(bundle);
        if (this.isCalledFromOnLockScreen() && this.isRestrictedMode()) {
            this.getWindow().addFlags(524288);
        }
        this.getWindow().addFlags(256);
        this.getWindow().addFlags(512);
        this.mMessageDialog = new MessageDialogController(this, null, this.mPositiveClickListener, this.mNegativeClickListener, (MessageDialogController.MessageDialogOnCancelListener)this.mMessageCallback, this.mDismissListener, (MessageDialogController.MessageDialogOnOpenListener)this.mMessageCallback);
        this.mOrientation = LayoutOrientationResolver.getInstance().getConfigurationOrientation();
        (this.mOrientationEventListener = new OrientationEventListener(this, this) {
            final SetupWizardActivity this$0;
            
            public void onOrientationChanged(int n) {
                final int access$800 = this.this$0.getOrientation(n);
                if (this.this$0.mOrientation != access$800) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                if (n != 0) {
                    this.this$0.mOrientation = access$800;
                    if (this.this$0.isPortrait()) {
                        trace("change to PORTRAIT.");
                    }
                    else {
                        trace("change to LANDSCAPE.");
                    }
                    if (this.this$0.mMessageDialog != null) {
                        this.this$0.mMessageDialog.setSensorOrientation(access$800);
                    }
                    if (this.this$0.mTutorial != null) {
                        this.this$0.mTutorial.setUiOrientation(access$800);
                    }
                    if (this.this$0.mOptionalRuntimePermissionDialog != null) {
                        this.this$0.mOptionalRuntimePermissionDialog.setOrientation(access$800);
                    }
                }
            }
        }).enable();
        this.setContentView(2131492893);
        this.mRootView = (ViewGroup)this.findViewById(2131296603);
        (this.mTutorial = new TutorialController(this.mRootView, this.getWindow())).setOnClickTutorialButtonListener(this.mOnClickTutorialButtonListener);
        this.mRootView.getViewTreeObserver().addOnWindowAttachListener((ViewTreeObserver$OnWindowAttachListener)new ViewTreeObserver$OnWindowAttachListener(this) {
            final SetupWizardActivity this$0;
            
            public void onWindowAttached() {
                trace("onWindowAttached() E");
                this.this$0.setupLayout();
                this.this$0.mMessageDialog.setSensorOrientation(this.this$0.mOrientation);
                this.this$0.mTutorial.setUiOrientation(this.this$0.mOrientation);
                this.this$0.mTutorial.open(TutorialController.OpenType.create(TutorialController.DisplayTrigger.SETUP_WIZARD), null, null);
                trace("onWindowAttached() X");
            }
            
            public void onWindowDetached() {
                trace("onWindowDetached() E");
                trace("onWindowDetached() X");
            }
        });
        trace("onCreate() X");
    }
    
    protected void onDestroy() {
        trace("onDestroy() E");
        super.onDestroy();
        trace("onDestroy() X");
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return n == 27 || super.onKeyDown(n, keyEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return n == 27 || super.onKeyUp(n, keyEvent);
    }
    
    protected void onPause() {
        trace("onPause() E");
        if (this.mMainHandler != null) {
            this.mMainHandler.removeCallbacks(this.mOnResumeTasks);
        }
        if (!this.mSkippedFirstOnResume) {
            if (CamLog.VERBOSE) {
                CamLog.d("onPause() --> onPauseTasks()");
            }
            this.onPauseTasks();
        }
        super.onPause();
        trace("onPause() X");
    }
    
    public void onRequestPermissionsResult(int i, final String[] array, final int[] array2) {
        trace("onRequestPermissionsResult() E");
        super.onRequestPermissionsResult(i, array, array2);
        final HashMap hashMap = new HashMap();
        if (array.length != 0) {
            for (i = 0; i < array.length; ++i) {
                hashMap.put(array[i], array2[i]);
            }
        }
        if (hashMap.containsKey(this.REQUEST_LOCATION_PERMISSION[0]) && hashMap.containsKey(this.REQUEST_LOCATION_PERMISSION[1])) {
            if ((int)hashMap.get(this.REQUEST_LOCATION_PERMISSION[0]) == 0 && (int)hashMap.get(this.REQUEST_LOCATION_PERMISSION[1]) == 0) {
                this.setGeoTagResult(true);
                if (GeotagManager.isGeoTagEnabled(Geotag.ON, (Context)this)) {
                    if (!this.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
                        this.close();
                    }
                    else {
                        this.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
                    }
                }
                else {
                    final MessageDialogRequest messageDialogRequest = new MessageDialogRequest();
                    messageDialogRequest.mDialogId = DialogId.LOCATION_SERVICE_DISABLE_ON_LAUNCH;
                    this.mMessageDialog.request(messageDialogRequest);
                }
            }
            else {
                this.setGeoTagResult(false);
                this.showOptionalRuntimePermissionDialog();
            }
        }
        trace("onRequestPermissionsResult() X");
    }
    
    protected void onResume() {
        trace("onResume() E");
        if (this.mMainHandler == null) {
            this.mMainHandler = new Handler(this.getMainLooper());
        }
        this.mMainHandler.removeCallbacks(this.mOnResumeTasks);
        if (this.mIsGeotagEnabled) {
            this.setGeoTagResult(GeotagManager.isGeoTagEnabled(Geotag.ON, (Context)this));
        }
        if (!this.mSkippedFirstOnResume) {
            this.mSkippedFirstOnResume = true;
            final StringBuilder sb = new StringBuilder();
            sb.append("onResume() --> postDelayed(mOnResumeTasks,");
            sb.append(15L);
            sb.append(")");
            trace(sb.toString());
            this.mMainHandler.postDelayed(this.mOnResumeTasks, 15L);
        }
        else {
            trace("onResume() --> onResumeTasks()");
            this.mSkippedFirstOnResume = false;
            this.onResumeTasks();
        }
        super.onResume();
        trace("onResume() X");
    }
    
    public void onStop() {
        trace("onStop() E");
        super.onStop();
        trace("onStop() X");
    }
    
    @SuppressLint({ "StringFormatInvalid" })
    public void showOptionalRuntimePermissionDialog() {
        final RotatableDialog.Builder builder = new RotatableDialog.Builder((Context)this);
        final LayoutInflater from = LayoutInflater.from((Context)this);
        builder.setOnKeyListener((DialogInterface$OnKeyListener)new KeyEventKiller());
        builder.setTitle((CharSequence)String.format(Locale.US, this.getString(2131690051), this.getResources().getString(this.getApplicationInfo().labelRes)));
        final ViewGroup viewAsScrollable = (ViewGroup)from.inflate(2131492965, (ViewGroup)null);
        final TextView textView = (TextView)viewAsScrollable.findViewById(2131296289);
        final TextView textView2 = (TextView)viewAsScrollable.findViewById(2131296477);
        final TextView textView3 = (TextView)viewAsScrollable.findViewById(2131296384);
        final TextView textView4 = (TextView)viewAsScrollable.findViewById(2131296288);
        textView.setText(2131690049);
        textView2.setText((CharSequence)this.getPermissionGroupLabel("android.permission.ACCESS_FINE_LOCATION"));
        textView3.setText(2131690053);
        textView4.setText(2131690050);
        builder.setViewAsScrollable((View)viewAsScrollable);
        builder.setPositiveButton(2131690046, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this) {
            final SetupWizardActivity this$0;
            
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (this.this$0.isRestrictedMode() && !this.this$0.isSecure()) {
                    this.this$0.dismissKeyguard();
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("package:");
                sb.append(this.this$0.getPackageName());
                final Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse(sb.toString()));
                try {
                    this.this$0.startActivity(intent);
                }
                catch (final ActivityNotFoundException ex) {
                    CamLog.e("showOptionalRuntimePermissionDialog() launchApplicationSettings: failed.", (Throwable)ex);
                }
                this.this$0.mOptionalRuntimePermissionDialog = null;
            }
        });
        builder.setNegativeButton(2131689666, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this) {
            final SetupWizardActivity this$0;
            
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (!this.this$0.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
                    this.this$0.close();
                }
                else {
                    this.this$0.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
                }
                this.this$0.mOptionalRuntimePermissionDialog = null;
            }
        });
        builder.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener(this) {
            final SetupWizardActivity this$0;
            
            public void onDismiss(final DialogInterface dialogInterface) {
                if (!this.this$0.mTutorial.hasNext(TutorialController.TutorialType.SAVE_LOCATION)) {
                    this.this$0.close();
                }
                else {
                    this.this$0.mTutorial.doNextAction(TutorialController.TutorialType.SAVE_LOCATION);
                }
                this.this$0.mOptionalRuntimePermissionDialog = null;
            }
        });
        builder.setCancelable(RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT);
        builder.setOrientation(this.mOrientation);
        (this.mOptionalRuntimePermissionDialog = builder.createRotatableDialog()).show();
    }
    
    private enum InterruptedBy
    {
        private static final InterruptedBy[] $VALUES;
        
        LOCATION_SETTING, 
        NONE, 
        REQUEST_PERMISSION, 
        SIDE_SENSE_SETTING;
        
        static {
            $VALUES = new InterruptedBy[] { InterruptedBy.NONE, InterruptedBy.REQUEST_PERMISSION, InterruptedBy.LOCATION_SETTING, InterruptedBy.SIDE_SENSE_SETTING };
        }
    }
    
    private static class KeyEventKiller implements DialogInterface$OnKeyListener
    {
        public boolean onKey(final DialogInterface dialogInterface, final int n, final KeyEvent keyEvent) {
            return n == 27 || n == 80 || n == 82;
        }
    }
    
    private static class MessageDialogCallbackAdapter implements MessageDialogOnDismissListener, MessageDialogOnOpenListener, MessageDialogOnCancelListener
    {
        @Override
        public void onCancel(final MessageDialogRequest messageDialogRequest) {
        }
        
        @Override
        public void onDismiss(final MessageDialogRequest messageDialogRequest) {
        }
        
        @Override
        public void onOpen(final MessageDialogRequest messageDialogRequest) {
        }
    }
}
