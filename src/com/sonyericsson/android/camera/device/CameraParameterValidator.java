package com.sonyericsson.android.camera.device;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class CameraParameterValidator {
    private static final String BASE_CONDITION_TAG = "base-condition";
    private static final String CHECK_CONDITION_TAG = "check-condition";
    private static final int KEY_ATTRIBUTE_INDEX = 0;
    private static final String KEY_VALUE_SET_TAG = "key-value-set";
    private static final boolean LOCAL_LOG = false;
    private static final String VALIDATION_SET_TAG = "validation-set";
    private static final String VALUE_ACTIVE_ARRAY_SIZE = "active-array-size";
    private static final int VALUE_ATTRIBUTE_INDEX = 1;
    private static Map<String, Rect> mActiveArraySizeMap;
    private static Map<String, List<CaptureRequest.Key<?>>> mKeysMap;
    private static List<ValidationCase> mValidSetList = new ArrayList();
    private static List<ValidationCase> mInvalidSetList = new ArrayList();

    CameraParameterValidator() {
    }

    public static class ParameterValidationError extends RuntimeException {
        private static final long serialVersionUID = 0;

        public ParameterValidationError(String str) {
            super(str);
        }
    }

    static synchronized void validate(String str, CaptureRequestHolder captureRequestHolder) {
        if (CamLog.DEBUG) {
            List<CaptureRequest.Key<?>> list = mKeysMap.get(str);
            Rect rect = mActiveArraySizeMap.get(str);
            for (ValidationCase validationCase : mInvalidSetList) {
                if (validationCase.isBaseCondition(list, captureRequestHolder)) {
                    validationCase.checkInvalid(list, captureRequestHolder, rect);
                }
            }
            for (ValidationCase validationCase2 : mValidSetList) {
                if (validationCase2.isBaseCondition(list, captureRequestHolder)) {
                    validationCase2.checkValid(list, captureRequestHolder, rect);
                }
            }
        }
    }

    private static class ValidationCase {
        private List<KeyValueSet> mBaseConditionList;
        private List<KeyValueSet> mCheckConditionList;

        private ValidationCase() {
            this.mBaseConditionList = new ArrayList();
            this.mCheckConditionList = new ArrayList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addBaseCondition(KeyValueSet keyValueSet) {
            this.mBaseConditionList.add(keyValueSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCheckCondition(KeyValueSet keyValueSet) {
            this.mCheckConditionList.add(keyValueSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isBaseCondition(List<CaptureRequest.Key<?>> list, CaptureRequestHolder captureRequestHolder) {
            for (KeyValueSet keyValueSet : this.mBaseConditionList) {
                CaptureRequest.Key targetKey = getTargetKey(list, keyValueSet);
                if (targetKey != null) {
                    if (!keyValueSet.value.equals(String.valueOf(captureRequestHolder.get(targetKey)))) {
                        return false;
                    }
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkValid(List<CaptureRequest.Key<?>> list, CaptureRequestHolder captureRequestHolder, Rect rect) {
            String strValueOf;
            for (KeyValueSet keyValueSet : this.mCheckConditionList) {
                CaptureRequest.Key targetKey = getTargetKey(list, keyValueSet);
                if (targetKey != null) {
                    if (keyValueSet.value.equals("active-array-size")) {
                        if (captureRequestHolder.get(targetKey) instanceof MeteringRectangle[]) {
                            strValueOf = ((MeteringRectangle[]) captureRequestHolder.get(targetKey))[0].getRect().toString();
                        } else {
                            strValueOf = String.valueOf(captureRequestHolder.get(targetKey));
                        }
                        if (!rect.toString().equals(strValueOf)) {
                            throw new ParameterValidationError("ERROR : [KEY=" + keyValueSet.key + "]\n  EXPECTED : [VALUE=" + rect + "]\n  ACTUAL   : [VALUE=" + strValueOf + "]\n");
                        }
                    } else {
                        String strValueOf2 = String.valueOf(captureRequestHolder.get(targetKey));
                        if (!keyValueSet.value.equals(strValueOf2)) {
                            throw new ParameterValidationError("ERROR : [KEY=" + keyValueSet.key + "]\n  EXPECTED : [VALUE=" + keyValueSet.value + "]\n  ACTUAL   : [VALUE=" + strValueOf2 + "]\n");
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkInvalid(List<CaptureRequest.Key<?>> list, CaptureRequestHolder captureRequestHolder, Rect rect) {
            String strValueOf;
            for (KeyValueSet keyValueSet : this.mCheckConditionList) {
                CaptureRequest.Key targetKey = getTargetKey(list, keyValueSet);
                if (targetKey != null) {
                    if (keyValueSet.value.equals("active-array-size")) {
                        if (captureRequestHolder.get(targetKey) instanceof MeteringRectangle[]) {
                            strValueOf = ((MeteringRectangle[]) captureRequestHolder.get(targetKey))[0].getRect().toString();
                        } else {
                            strValueOf = String.valueOf(captureRequestHolder.get(targetKey));
                        }
                        if (rect.toString().equals(strValueOf)) {
                            throw new ParameterValidationError("ERROR : [KEY=" + keyValueSet.key + "]\n  INVALID  : [VALUE=" + rect + "]\n");
                        }
                    } else {
                        if (keyValueSet.value.equals(String.valueOf(captureRequestHolder.get(targetKey)))) {
                            throw new ParameterValidationError("ERROR : [KEY=" + keyValueSet.key + "]\n  INVALID  : [VALUE=" + keyValueSet.value + "]\n");
                        }
                    }
                }
            }
        }

        private CaptureRequest.Key getTargetKey(List<CaptureRequest.Key<?>> list, KeyValueSet keyValueSet) {
            for (CaptureRequest.Key<?> key : list) {
                if (key.getName().equals(keyValueSet.key)) {
                    return getApplicationCaptureRequestKey(key);
                }
            }
            return null;
        }

        private CaptureRequest.Key<?> getApplicationCaptureRequestKey(CaptureRequest.Key<?> key) {
            String name = key.getName();
            if (name != null) {
                for (CaptureRequest.Key<?> key2 : SomcCameraDeviceInfo.getAllCaptureRequestKeys()) {
                    if (key2.getName().equals(name)) {
                        return key2;
                    }
                }
            }
            return key;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ValidationSet :\n");
            sb.append("  BaseConditionList :\n");
            for (KeyValueSet keyValueSet : this.mBaseConditionList) {
                sb.append("    ");
                sb.append(keyValueSet.toString());
                sb.append("\n");
            }
            sb.append("  CheckConditionList :\n");
            for (KeyValueSet keyValueSet2 : this.mCheckConditionList) {
                sb.append("    ");
                sb.append(keyValueSet2.toString());
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    private static class KeyValueSet {
        public final String key;
        public final String value;

        public KeyValueSet(String str, String str2) {
            this.key = str;
            this.value = str2;
        }

        public String toString() {
            return "KeyValueSet [KEY=" + this.key + "] [VALUE=" + this.value + "]";
        }
    }

    public static void loadCheckList(Context context) throws Resources.NotFoundException, CameraAccessException {
        if (CamLog.DEBUG) {
            loadAvailableKeys(context);
            loadCheckList(context, mInvalidSetList, R.xml.camera_parameter_validator_validation_invalid_list);
            loadCheckList(context, mValidSetList, R.xml.camera_parameter_validator_validation_valid_list);
        }
    }

    private static void loadAvailableKeys(Context context) throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) context.getSystemService("camera");
        try {
            mKeysMap = new HashMap();
            mActiveArraySizeMap = new HashMap();
            for (String str : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(str);
                mKeysMap.put(str, cameraCharacteristics.getAvailableCaptureRequestKeys());
                Rect rect = (Rect) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                mActiveArraySizeMap.put(str, new Rect(0, 0, rect.width(), rect.height()));
            }
        } catch (CameraAccessException e) {
            CamLog.e("Failed in getCameraCharacteristics", e);
        }
    }

    private static void loadCheckList(Context context, List<ValidationCase> list, int i) throws Resources.NotFoundException {
        try {
            XmlResourceParser xml = context.getResources().getXml(i);
            if (xml != null) {
                Throwable th = null;
                byte b = 0;
                try {
                    ValidationCase validationCase = null;
                    for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                        String name = xml.getName();
                        switch (eventType) {
                            case 2:
                                if (VALIDATION_SET_TAG.equals(name)) {
                                    validationCase = new ValidationCase();
                                    break;
                                } else if (BASE_CONDITION_TAG.equals(name)) {
                                    loadBaseCondition(validationCase, xml);
                                    break;
                                } else if (CHECK_CONDITION_TAG.equals(name)) {
                                    loadCheckCondition(validationCase, xml);
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (VALIDATION_SET_TAG.equals(name)) {
                                    list.add(validationCase);
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
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
            }
            if (xml != null) {
                xml.close();
            }
        } catch (IOException e) {
            CamLog.e("Fail to load of CheckList.", e);
            throw new ParameterValidationError("loadCheckList():[IOException]");
        } catch (XmlPullParserException e2) {
            CamLog.e("Fail to load of CheckList.", e2);
            throw new ParameterValidationError("loadCheckList():[XmlPullParserException]");
        }
    }

    private static void loadBaseCondition(ValidationCase validationCase, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType == 3 && BASE_CONDITION_TAG.equals(xmlPullParser.getName())) {
                return;
            }
            if (eventType == 2 && KEY_VALUE_SET_TAG.equals(xmlPullParser.getName())) {
                validationCase.addBaseCondition(new KeyValueSet(xmlPullParser.getAttributeValue(0), xmlPullParser.getAttributeValue(1)));
            }
            eventType = xmlPullParser.next();
        }
    }

    private static void loadCheckCondition(ValidationCase validationCase, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType == 3 && CHECK_CONDITION_TAG.equals(xmlPullParser.getName())) {
                return;
            }
            if (eventType == 2 && KEY_VALUE_SET_TAG.equals(xmlPullParser.getName())) {
                validationCase.addCheckCondition(new KeyValueSet(xmlPullParser.getAttributeValue(0), xmlPullParser.getAttributeValue(1)));
            }
            eventType = xmlPullParser.next();
        }
    }
}
