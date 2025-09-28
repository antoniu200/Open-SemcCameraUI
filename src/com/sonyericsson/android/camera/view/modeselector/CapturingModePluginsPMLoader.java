package com.sonyericsson.android.camera.view.modeselector;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.RemoteException;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CapturingModePluginsPMLoader {
    private static final String FINGERPRINT = "android.os.Build.FINGERPRINT";
    private static final String INTENT_FILTER_ACTION_NAME_FOR_QUERY = "com.sonymobile.camera.addon.action.REGISTER_MODE";
    private static final String META_DATA_NAME = "com.sonymobile.camera.addon.MODE_ATTRIBUTES";
    private static final String MODES_TAG = "modes";
    private static final String MODE_ATTRIBUTE_DESCRIPTION_LABEL = "descriptionLabel";
    private static final String MODE_ATTRIBUTE_NAME = "name";
    private static final String MODE_ATTRIBUTE_SELECTOR_ICON = "selectorIcon";
    private static final String MODE_ATTRIBUTE_SELECTOR_LABEL = "selectorLabel";
    private static final String MODE_ATTRIBUTE_SHORTCUT_ICON = "shortcutIcon";
    private static final String MODE_ATTRIBUTE_SHORTCUT_LABEL = "shortcutLabel";
    private static final String MODE_DESCRIPTION_LABEL = "com.sonymobile.camera.addon.MODE_DESCRIPTION_LABEL";
    private static final String MODE_SELECTOR_ICON = "com.sonymobile.camera.addon.MODE_SELECTOR_ICON";
    private static final String MODE_SELECTOR_LABEL = "com.sonymobile.camera.addon.MODE_SELECTOR_LABEL";
    private static final String MODE_SELECTOR_NAME = "com.sonymobile.camera.addon.MODE_NAME";
    private static final String MODE_SHORTCUT_ICON = "com.sonymobile.camera.addon.MODE_SHORTCUT_ICON";
    private static final String MODE_SHORTCUT_LABEL = "com.sonymobile.camera.addon.MODE_SHORTCUT_LABEL";
    private static final String MODE_TAG = "mode";
    private static final String[] PluginConfigurationList = {"HighFrameRateVideoActivity", "enable_timeshift_video_rec_app", "HighResolutionVideoActivity", "enable_4k_video_rec_app"};
    private static final String SHARED_PREFERENCE_NAME = "com.sonyericsson.cameracommon.appsui.fingerprint_sharedprefs";
    private static final String TAG = "CapturingModePluginsPMLoader";
    private static final String USES_FEATURE_ATTRIBUTE_NAME = "name";
    private static final String USES_FEATURE_TAG = "uses-feature";
    private static final String XMLNS_ANDROID = "http://schemas.android.com/apk/res/android";
    private static List<CapturingModeAttributes> mPackageManagerPluginList;
    private Context mContext;
    private TmpUiAttributes mTmpAttributes;

    private static class TmpUiAttributes {
        String mActivityName;
        int mDescriptionLabelId;
        CapturingModeAttributes.InternalCaptureType mInternalCaptureType;
        boolean mIsVisibleNormal;
        boolean mIsVisibleOneshot;
        boolean mIsVisibleShortcut;
        String mModeName;
        String mPackageName;
        int mSelectorIconId;
        int mSelectorLabelId;
        int mShortcutIconId;
        int mShortcutLabelId;

        private TmpUiAttributes() {
        }
    }

    public CapturingModePluginsPMLoader(Context context) {
        this.mContext = context;
    }

    public void updatePluginsInDB() throws Resources.NotFoundException, PackageManager.NameNotFoundException, RemoteException, OperationApplicationException {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        if (isCapturingModeFingerprintModified(sharedPreferences)) {
            clearCapturingModes();
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putString("android.os.Build.FINGERPRINT", Build.FINGERPRINT);
            editorEdit.commit();
        }
        getPluginsFromPackageManager();
        CapturingModeCollection.register(this.mContext.getContentResolver(), mPackageManagerPluginList, this.mContext.getPackageManager());
    }

    private boolean isCapturingModeFingerprintModified(SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString("android.os.Build.FINGERPRINT", "");
        String str = Build.FINGERPRINT;
        if (string.equals(str)) {
            return false;
        }
        if (CamLog.DEBUG) {
            CamLog.d("version mismatch: cached: " + string + ", current : " + str);
        }
        return true;
    }

    private void clearCapturingModes() {
        this.mContext.getContentResolver().delete(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI, null, null);
    }

    private void getPluginsFromPackageManager() throws Resources.NotFoundException, PackageManager.NameNotFoundException {
        boolean modeAttributesFromMetaData;
        if (mPackageManagerPluginList == null) {
            mPackageManagerPluginList = new ArrayList();
        }
        mPackageManagerPluginList.clear();
        PackageManager packageManager = this.mContext.getPackageManager();
        for (ResolveInfo resolveInfo : packageManager.queryIntentActivities(new Intent(INTENT_FILTER_ACTION_NAME_FOR_QUERY), 0)) {
            if (isPluginConfigurationOn(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)) {
                this.mTmpAttributes = new TmpUiAttributes();
                this.mTmpAttributes.mPackageName = resolveInfo.activityInfo.packageName;
                this.mTmpAttributes.mActivityName = resolveInfo.activityInfo.name;
                if (packageManager.checkPermission(CapturingModeUtil.CAMERA_ADDON_PERMISSION_NAME, this.mTmpAttributes.mPackageName) != 0) {
                    CamLog.e("This application has no CAMERA_ADDON permission: " + resolveInfo.activityInfo.packageName);
                    CamLog.e("Failed to add plugin for this package.");
                } else {
                    try {
                        ActivityInfo activityInfo = this.mContext.getPackageManager().getActivityInfo(new ComponentName(this.mTmpAttributes.mPackageName, this.mTmpAttributes.mActivityName), 128);
                        if (parsePluginFromMetaData(resolveInfo, activityInfo)) {
                            addPluginToList();
                        } else {
                            try {
                                Resources resourcesForApplication = packageManager.getResourcesForApplication(resolveInfo.activityInfo.packageName);
                                try {
                                    modeAttributesFromMetaData = parseModeAttributesFromMetaData(resolveInfo, activityInfo, resourcesForApplication);
                                } catch (IOException unused) {
                                    CamLog.e("IOException: Problem when parse mode attributes xml file.");
                                } catch (XmlPullParserException unused2) {
                                    CamLog.e("XmlPullParserException: Problem when parse mode attributes xml file.");
                                }
                                if (resourcesForApplication != null && modeAttributesFromMetaData) {
                                    if (parsePluginFromModeAttributesXmlFile(activityInfo, resourcesForApplication)) {
                                        addPluginToList();
                                    } else {
                                        CamLog.e("Failed to add plugin for this package: " + resolveInfo.activityInfo.packageName);
                                    }
                                } else if (parsePluginFromApplicationInfo(resolveInfo)) {
                                    addPluginToList();
                                } else {
                                    CamLog.e("Failed to add plugin for this package: " + resolveInfo.activityInfo.packageName);
                                }
                            } catch (PackageManager.NameNotFoundException unused3) {
                                CamLog.e("NameNotFoundException: Problem when geting Application Resources with packageName: " + resolveInfo.activityInfo.packageName);
                                CamLog.e("Failed to add plugin for this package.");
                            }
                        }
                    } catch (PackageManager.NameNotFoundException unused4) {
                        CamLog.e("NameNotFoundException: Problem when geting Application META_DATA with packageName: " + resolveInfo.activityInfo.packageName);
                        CamLog.e("Failed to add plugin for this package.");
                    }
                }
            }
        }
    }

    private boolean parseModeAttributesFromMetaData(ResolveInfo resolveInfo, ActivityInfo activityInfo, Resources resources) throws XmlPullParserException, Resources.NotFoundException, IOException {
        if (activityInfo.metaData == null) {
            CamLog.e("This activity has no meta-data: " + resolveInfo.activityInfo.name);
            return false;
        }
        if (resources == null) {
            return false;
        }
        int i = activityInfo.metaData.getInt(META_DATA_NAME, 0);
        if (i == 0) {
            return true;
        }
        try {
            XmlResourceParser xml = resources.getXml(i);
            Throwable th = null;
            try {
                if (xml == null) {
                    CamLog.e("Problem when get XmlResourceParser from mode attributes xml resource Id.");
                    if (xml != null) {
                        xml.close();
                    }
                    return false;
                }
                int next = xml.next();
                boolean z = true;
                while (true) {
                    if (next == 3 && MODE_TAG.equals(xml.getName())) {
                        break;
                    }
                    if (next == 2 && USES_FEATURE_TAG.equals(xml.getName())) {
                        String attributeValue = xml.getAttributeValue(XMLNS_ANDROID, "name");
                        if (!this.mContext.getPackageManager().hasSystemFeature(attributeValue)) {
                            CamLog.w("Mode of \"" + resolveInfo.activityInfo.packageName + "\" requires \"" + attributeValue + "\" but this platform doesn't support the feature.");
                            z = false;
                        }
                    }
                    next = xml.next();
                }
                if (xml != null) {
                    xml.close();
                }
                return z;
            } catch (Throwable th2) {
                if (xml != null) {
                    if (0 != 0) {
                        try {
                            xml.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                    } else {
                        xml.close();
                    }
                }
                throw th2;
            }
        } catch (IOException | XmlPullParserException unused) {
            CamLog.e("Problem when parse mode attributes xml file");
            return false;
        }
    }

    private boolean parsePluginFromModeAttributesXmlFile(ActivityInfo activityInfo, Resources resources) throws XmlPullParserException, Resources.NotFoundException, IOException {
        int i;
        if (resources != null && (i = activityInfo.metaData.getInt(META_DATA_NAME, 0)) != 0) {
            try {
                XmlResourceParser xml = resources.getXml(i);
                Throwable th = null;
                if (xml != null) {
                    try {
                        parsePluginXMLData(xml, resources);
                        if (xml != null) {
                            xml.close();
                        }
                        return true;
                    } catch (Throwable th2) {
                        if (xml != null) {
                            if (th != null) {
                                try {
                                    xml.close();
                                } catch (Throwable th3) {
                                    th.addSuppressed(th3);
                                }
                            } else {
                                xml.close();
                            }
                        }
                        throw th2;
                    }
                }
                if (xml != null) {
                    xml.close();
                }
            } catch (IOException | XmlPullParserException unused) {
                CamLog.e("Problem when parse mode attributes xml file.");
            }
        }
        return false;
    }

    private void addPluginToList() {
        this.mTmpAttributes.mInternalCaptureType = CapturingModeAttributes.InternalCaptureType.Photo;
        this.mTmpAttributes.mIsVisibleNormal = true;
        this.mTmpAttributes.mIsVisibleOneshot = false;
        this.mTmpAttributes.mIsVisibleShortcut = (this.mTmpAttributes.mShortcutLabelId == 0 || this.mTmpAttributes.mShortcutIconId == 0) ? false : true;
        mPackageManagerPluginList.add(new CapturingModeAttributes(this.mTmpAttributes.mPackageName, this.mTmpAttributes.mActivityName, this.mTmpAttributes.mModeName, this.mTmpAttributes.mSelectorIconId, this.mTmpAttributes.mSelectorLabelId, this.mTmpAttributes.mDescriptionLabelId, this.mTmpAttributes.mShortcutIconId, this.mTmpAttributes.mShortcutLabelId, this.mTmpAttributes.mInternalCaptureType, this.mTmpAttributes.mIsVisibleNormal, this.mTmpAttributes.mIsVisibleOneshot, this.mTmpAttributes.mIsVisibleShortcut, (Object) null));
    }

    private void parsePluginXMLData(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        while (eventType != 1) {
            int next = xmlPullParser.next();
            while (true) {
                if (next != 3 || !MODES_TAG.equals(xmlPullParser.getName())) {
                    if (next == 2 && MODE_TAG.equals(xmlPullParser.getName())) {
                        parsePluginXMLModeTag(xmlPullParser, resources);
                    }
                    next = xmlPullParser.next();
                }
            }
            eventType = xmlPullParser.next();
        }
    }

    private void parsePluginXMLModeTag(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            String attributeValue = xmlPullParser.getAttributeValue(i);
            if (attributeName != null) {
                if ("name".equals(attributeName)) {
                    this.mTmpAttributes.mModeName = attributeValue;
                } else if (MODE_ATTRIBUTE_SELECTOR_ICON.equals(attributeName)) {
                    this.mTmpAttributes.mSelectorIconId = getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                } else if (MODE_ATTRIBUTE_SELECTOR_LABEL.equals(attributeName)) {
                    this.mTmpAttributes.mSelectorLabelId = getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                } else if (MODE_ATTRIBUTE_DESCRIPTION_LABEL.equals(attributeName)) {
                    this.mTmpAttributes.mDescriptionLabelId = getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                } else if (MODE_ATTRIBUTE_SHORTCUT_ICON.equals(attributeName)) {
                    this.mTmpAttributes.mShortcutIconId = getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                } else if (MODE_ATTRIBUTE_SHORTCUT_LABEL.equals(attributeName)) {
                    this.mTmpAttributes.mShortcutLabelId = getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                }
            }
        }
    }

    private int getResIDFromXmlString(String str, String str2) {
        try {
            return this.mContext.getPackageManager().getResourcesForApplication(str).getIdentifier(str2, null, str);
        } catch (PackageManager.NameNotFoundException unused) {
            CamLog.e("Could not get resource id for plugin");
            return 0;
        }
    }

    private boolean isPluginConfigurationOn(String str, String str2) {
        for (int i = 0; i < PluginConfigurationList.length; i += 2) {
            if (PluginConfigurationList[i].equals(str2.substring(str2.lastIndexOf(".") + 1))) {
                return isValueOn(PluginConfigurationList[i + 1], str);
            }
        }
        return true;
    }

    private boolean isValueOn(String str, String str2) throws PackageManager.NameNotFoundException {
        try {
            Resources resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication(str2);
            int identifier = resourcesForApplication.getIdentifier(str, "bool", str2);
            if (identifier != 0) {
                return resourcesForApplication.getBoolean(identifier);
            }
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            CamLog.e("Error reading out plugin configuration value");
            return true;
        }
    }

    private boolean parsePluginFromMetaData(ResolveInfo resolveInfo, ActivityInfo activityInfo) {
        if (activityInfo.metaData == null) {
            CamLog.e("This activity has no meta-data: " + resolveInfo.activityInfo.name);
            return false;
        }
        this.mTmpAttributes.mModeName = activityInfo.metaData.getString(MODE_SELECTOR_NAME);
        this.mTmpAttributes.mSelectorIconId = activityInfo.metaData.getInt(MODE_SELECTOR_ICON);
        this.mTmpAttributes.mSelectorLabelId = activityInfo.metaData.getInt(MODE_SELECTOR_LABEL);
        this.mTmpAttributes.mDescriptionLabelId = activityInfo.metaData.getInt(MODE_DESCRIPTION_LABEL);
        this.mTmpAttributes.mShortcutIconId = activityInfo.metaData.getInt(MODE_SHORTCUT_ICON);
        this.mTmpAttributes.mShortcutLabelId = activityInfo.metaData.getInt(MODE_SHORTCUT_LABEL);
        return (this.mTmpAttributes.mModeName == null && this.mTmpAttributes.mSelectorIconId == 0 && this.mTmpAttributes.mSelectorLabelId == 0 && this.mTmpAttributes.mDescriptionLabelId == 0 && this.mTmpAttributes.mShortcutIconId == 0 && this.mTmpAttributes.mShortcutLabelId == 0) ? false : true;
    }

    private boolean parsePluginFromApplicationInfo(ResolveInfo resolveInfo) {
        try {
            ApplicationInfo applicationInfo = this.mContext.getPackageManager().getPackageInfo(resolveInfo.activityInfo.packageName, 0).applicationInfo;
            this.mTmpAttributes.mModeName = applicationInfo.loadLabel(this.mContext.getPackageManager()).toString();
            this.mTmpAttributes.mSelectorIconId = applicationInfo.icon;
            this.mTmpAttributes.mSelectorLabelId = applicationInfo.labelRes;
            if (applicationInfo.descriptionRes != 0) {
                this.mTmpAttributes.mDescriptionLabelId = applicationInfo.descriptionRes;
            } else {
                this.mTmpAttributes.mDescriptionLabelId = this.mTmpAttributes.mSelectorLabelId;
            }
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            CamLog.e("NameNotFoundException: " + resolveInfo.activityInfo.packageName);
            return false;
        }
    }
}
