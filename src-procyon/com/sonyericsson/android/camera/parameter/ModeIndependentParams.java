// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import java.util.ArrayList;
import java.util.List;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.ResetSettings;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.HelpGuide;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.CameraKey;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;

public class ModeIndependentParams
{
    public static final String TAG = "ModeIndependentParams";
    UserSettingValueHolder<AutoReview> mAutoReview;
    UserSettingValueHolder<CameraKey> mBurstByCameraKey;
    UserSettingValueHolder<DestinationToSave> mDestinationToSave;
    UserSettingValueHolder<DisplayFlash> mDisplayFlash;
    UserSettingValueHolder<DistortionCorrection> mDistortionCorrection;
    UserSettingValueHolder<FastCapture> mFastCapture;
    UserSettingValueHolder<Flash> mFlash;
    UserSettingValueHolder<FrontAngle> mFrontAngle;
    UserSettingValueHolder<Geotag> mGeoTag;
    UserSettingValueHolder<GridLine> mGridLine;
    UserSettingValueHolder<HelpGuide> mHelpGuide;
    UserSettingValueHolder<PhotoLight> mPhotoLight;
    UserSettingValueHolder<PredictiveLaunch> mPredictiveLaunch;
    UserSettingValueHolder<ResetSettings> mResetSettings;
    UserSettingValueHolder<ShutterSound> mShutterSound;
    UserSettingValueHolder<SideSense> mSideSense;
    UserSettingValueHolder<TouchCapture> mTouchCapture;
    UserSettingValueHolder<VolumeKey> mVolumeKey;
    
    public ModeIndependentParams() {
        this.mBurstByCameraKey = new UserSettingValueHolder<CameraKey>(CameraKey.TAKE_PHOTO);
        this.mFlash = new UserSettingValueHolder<Flash>(Flash.OFF);
        this.mDisplayFlash = new UserSettingValueHolder<DisplayFlash>(DisplayFlash.DISPLAY_AUTO);
        this.mPhotoLight = new UserSettingValueHolder<PhotoLight>(PhotoLight.OFF);
        this.mAutoReview = new UserSettingValueHolder<AutoReview>(AutoReview.FRONT_ONLY);
        this.mGeoTag = new UserSettingValueHolder<Geotag>(Geotag.OFF);
        this.mFastCapture = new UserSettingValueHolder<FastCapture>(FastCapture.LAUNCH_ONLY);
        this.mTouchCapture = new UserSettingValueHolder<TouchCapture>(TouchCapture.OFF);
        this.mShutterSound = new UserSettingValueHolder<ShutterSound>(ShutterSound.SOUND1);
        this.mDestinationToSave = new UserSettingValueHolder<DestinationToSave>(DestinationToSave.EMMC);
        this.mVolumeKey = new UserSettingValueHolder<VolumeKey>(VolumeKey.ZOOM);
        this.mGridLine = new UserSettingValueHolder<GridLine>(GridLine.OFF);
        this.mHelpGuide = new UserSettingValueHolder<HelpGuide>(HelpGuide.DUMMY_OFF);
        this.mFrontAngle = new UserSettingValueHolder<FrontAngle>(FrontAngle.DEFAULT);
        this.mDistortionCorrection = new UserSettingValueHolder<DistortionCorrection>(DistortionCorrection.OFF);
        this.mSideSense = new UserSettingValueHolder<SideSense>(SideSense.getDefaultValue());
        this.mResetSettings = new UserSettingValueHolder<ResetSettings>(ResetSettings.DUMMY_OFF);
        this.mPredictiveLaunch = new UserSettingValueHolder<PredictiveLaunch>(PredictiveLaunch.OFF);
    }
    
    public void clear(final Storage storage) {
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
    
    public void init(final ActionMode actionMode, final Storage storage) {
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
    
    public void setValues(final ModeIndependentParams modeIndependentParams) {
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
    
    public List<UserSettingValueHolder<?>> values() {
        final ArrayList list = new ArrayList();
        list.add(this.mBurstByCameraKey);
        list.add(this.mFlash);
        list.add(this.mDisplayFlash);
        list.add(this.mPhotoLight);
        list.add(this.mAutoReview);
        list.add(this.mGeoTag);
        list.add(this.mFastCapture);
        list.add(this.mTouchCapture);
        list.add(this.mShutterSound);
        list.add(this.mDestinationToSave);
        list.add(this.mVolumeKey);
        list.add(this.mGridLine);
        list.add(this.mHelpGuide);
        list.add(this.mFrontAngle);
        list.add(this.mDistortionCorrection);
        list.add(this.mSideSense);
        list.add(this.mResetSettings);
        list.add(this.mPredictiveLaunch);
        return list;
    }
}
