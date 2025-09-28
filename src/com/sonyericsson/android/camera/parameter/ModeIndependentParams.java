package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.CameraKey;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.configuration.parameters.HelpGuide;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.configuration.parameters.ResetSettings;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ModeIndependentParams {
    public static final String TAG = "ModeIndependentParams";
    UserSettingValueHolder<CameraKey> mBurstByCameraKey = new UserSettingValueHolder<>(CameraKey.TAKE_PHOTO);
    UserSettingValueHolder<Flash> mFlash = new UserSettingValueHolder<>(Flash.OFF);
    UserSettingValueHolder<DisplayFlash> mDisplayFlash = new UserSettingValueHolder<>(DisplayFlash.DISPLAY_AUTO);
    UserSettingValueHolder<PhotoLight> mPhotoLight = new UserSettingValueHolder<>(PhotoLight.OFF);
    UserSettingValueHolder<AutoReview> mAutoReview = new UserSettingValueHolder<>(AutoReview.FRONT_ONLY);
    UserSettingValueHolder<Geotag> mGeoTag = new UserSettingValueHolder<>(Geotag.OFF);
    UserSettingValueHolder<FastCapture> mFastCapture = new UserSettingValueHolder<>(FastCapture.LAUNCH_ONLY);
    UserSettingValueHolder<TouchCapture> mTouchCapture = new UserSettingValueHolder<>(TouchCapture.OFF);
    UserSettingValueHolder<ShutterSound> mShutterSound = new UserSettingValueHolder<>(ShutterSound.SOUND1);
    UserSettingValueHolder<DestinationToSave> mDestinationToSave = new UserSettingValueHolder<>(DestinationToSave.EMMC);
    UserSettingValueHolder<VolumeKey> mVolumeKey = new UserSettingValueHolder<>(VolumeKey.ZOOM);
    UserSettingValueHolder<GridLine> mGridLine = new UserSettingValueHolder<>(GridLine.OFF);
    UserSettingValueHolder<HelpGuide> mHelpGuide = new UserSettingValueHolder<>(HelpGuide.DUMMY_OFF);
    UserSettingValueHolder<FrontAngle> mFrontAngle = new UserSettingValueHolder<>(FrontAngle.DEFAULT);
    UserSettingValueHolder<DistortionCorrection> mDistortionCorrection = new UserSettingValueHolder<>(DistortionCorrection.OFF);
    UserSettingValueHolder<SideSense> mSideSense = new UserSettingValueHolder<>(SideSense.getDefaultValue());
    UserSettingValueHolder<ResetSettings> mResetSettings = new UserSettingValueHolder<>(ResetSettings.DUMMY_OFF);
    UserSettingValueHolder<PredictiveLaunch> mPredictiveLaunch = new UserSettingValueHolder<>(PredictiveLaunch.OFF);

    public void init(ActionMode actionMode, Storage storage) {
        DestinationToSave.setMountPoint(storage.getAvailableStorage());
        this.mBurstByCameraKey.setOptions(CameraKey.getOptions(actionMode));
        this.mFlash.setOptions(Flash.getOptions(actionMode));
        this.mDisplayFlash.setOptions(DisplayFlash.getOptions(actionMode));
        this.mPhotoLight.setOptions(PhotoLight.getOptions(actionMode));
        this.mAutoReview.setOptions(AutoReview.getOptions(actionMode));
        this.mGeoTag.setOptions(Geotag.getOptions());
        this.mFastCapture.setOptions(FastCapture.getOptions());
        this.mTouchCapture.setOptions(TouchCapture.getOptions());
        this.mShutterSound.setOptions(ShutterSound.getOptions(PlatformCapability.isForceSound(actionMode.mCameraId)));
        this.mDestinationToSave.setOptions(DestinationToSave.getOptions());
        this.mVolumeKey.setOptions(VolumeKey.getOptions());
        this.mGridLine.setOptions(GridLine.getOptions());
        this.mHelpGuide.setOptions(HelpGuide.getOptions());
        this.mFrontAngle.setOptions(FrontAngle.getOptions());
        this.mDistortionCorrection.setOptions(DistortionCorrection.getOptions());
        this.mSideSense.setOptions(SideSense.getOptions());
        this.mResetSettings.setOptions(ResetSettings.getOptions());
        this.mPredictiveLaunch.setOptions(PredictiveLaunch.getOptions());
    }

    public void clear(Storage storage) {
        DestinationToSave.setMountPoint(storage.getAvailableStorage());
        this.mBurstByCameraKey.setDefaultValue();
        this.mFlash.setDefaultValue();
        this.mDisplayFlash.setDefaultValue();
        this.mPhotoLight.setDefaultValue();
        this.mAutoReview.setDefaultValue();
        this.mGeoTag.setDefaultValue();
        this.mFastCapture.setDefaultValue();
        this.mTouchCapture.setDefaultValue();
        this.mShutterSound.setDefaultValue();
        this.mDestinationToSave.setDefaultValue();
        this.mVolumeKey.setDefaultValue();
        this.mGridLine.setDefaultValue();
        this.mHelpGuide.setDefaultValue();
        this.mFrontAngle.setDefaultValue();
        this.mDistortionCorrection.setDefaultValue();
        this.mResetSettings.setDefaultValue();
        this.mPredictiveLaunch.setDefaultValue();
    }

    public List<UserSettingValueHolder<?>> values() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mBurstByCameraKey);
        arrayList.add(this.mFlash);
        arrayList.add(this.mDisplayFlash);
        arrayList.add(this.mPhotoLight);
        arrayList.add(this.mAutoReview);
        arrayList.add(this.mGeoTag);
        arrayList.add(this.mFastCapture);
        arrayList.add(this.mTouchCapture);
        arrayList.add(this.mShutterSound);
        arrayList.add(this.mDestinationToSave);
        arrayList.add(this.mVolumeKey);
        arrayList.add(this.mGridLine);
        arrayList.add(this.mHelpGuide);
        arrayList.add(this.mFrontAngle);
        arrayList.add(this.mDistortionCorrection);
        arrayList.add(this.mSideSense);
        arrayList.add(this.mResetSettings);
        arrayList.add(this.mPredictiveLaunch);
        return arrayList;
    }

    public void setValues(ModeIndependentParams modeIndependentParams) {
        this.mBurstByCameraKey.set(modeIndependentParams.mBurstByCameraKey.get());
        this.mFlash.set(modeIndependentParams.mFlash.get());
        this.mDisplayFlash.set(modeIndependentParams.mDisplayFlash.get());
        this.mPhotoLight.set(modeIndependentParams.mPhotoLight.get());
        this.mAutoReview.set(modeIndependentParams.mAutoReview.get());
        this.mGeoTag.set(modeIndependentParams.mGeoTag.get());
        this.mFastCapture.set(modeIndependentParams.mFastCapture.get());
        this.mTouchCapture.set(modeIndependentParams.mTouchCapture.get());
        this.mShutterSound.set(modeIndependentParams.mShutterSound.get());
        this.mDestinationToSave.set(modeIndependentParams.mDestinationToSave.get());
        this.mVolumeKey.set(modeIndependentParams.mVolumeKey.get());
        this.mGridLine.set(modeIndependentParams.mGridLine.get());
        this.mHelpGuide.set(modeIndependentParams.mHelpGuide.get());
        this.mFrontAngle.set(modeIndependentParams.mFrontAngle.get());
        this.mDistortionCorrection.set(modeIndependentParams.mDistortionCorrection.get());
        this.mSideSense.set(modeIndependentParams.mSideSense.get());
        this.mResetSettings.set(modeIndependentParams.mResetSettings.get());
        this.mPredictiveLaunch.set(modeIndependentParams.mPredictiveLaunch.get());
    }
}
