// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import android.hardware.camera2.params.MeteringRectangle;
import java.util.Iterator;
import android.content.res.XmlResourceParser;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.hardware.camera2.CameraAccessException;
import com.sonyericsson.android.camera.util.CamLog;
import android.hardware.camera2.CameraCharacteristics;
import java.util.HashMap;
import android.hardware.camera2.CameraManager;
import android.content.Context;
import java.util.ArrayList;
import android.hardware.camera2.CaptureRequest$Key;
import java.util.List;
import android.graphics.Rect;
import java.util.Map;

class CameraParameterValidator
{
    private static final String BASE_CONDITION_TAG = "base-condition";
    private static final String CHECK_CONDITION_TAG = "check-condition";
    private static final int KEY_ATTRIBUTE_INDEX = 0;
    private static final String KEY_VALUE_SET_TAG = "key-value-set";
    private static final boolean LOCAL_LOG = false;
    private static final String VALIDATION_SET_TAG = "validation-set";
    private static final String VALUE_ACTIVE_ARRAY_SIZE = "active-array-size";
    private static final int VALUE_ATTRIBUTE_INDEX = 1;
    private static Map<String, Rect> mActiveArraySizeMap;
    private static List<ValidationCase> mInvalidSetList;
    private static Map<String, List<CaptureRequest$Key<?>>> mKeysMap;
    private static List<ValidationCase> mValidSetList;
    
    static {
        CameraParameterValidator.mValidSetList = new ArrayList<ValidationCase>();
        CameraParameterValidator.mInvalidSetList = new ArrayList<ValidationCase>();
    }
    
    private static void loadAvailableKeys(final Context context) {
        final CameraManager cameraManager = (CameraManager)context.getSystemService("camera");
        try {
            CameraParameterValidator.mKeysMap = new HashMap<String, List<CaptureRequest$Key<?>>>();
            CameraParameterValidator.mActiveArraySizeMap = new HashMap<String, Rect>();
            for (final String s : cameraManager.getCameraIdList()) {
                final CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(s);
                CameraParameterValidator.mKeysMap.put(s, cameraCharacteristics.getAvailableCaptureRequestKeys());
                final Rect rect = (Rect)cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                CameraParameterValidator.mActiveArraySizeMap.put(s, new Rect(0, 0, rect.width(), rect.height()));
            }
        }
        catch (final CameraAccessException ex) {
            CamLog.e("Failed in getCameraCharacteristics", (Throwable)ex);
        }
    }
    
    private static void loadBaseCondition(final ValidationCase validationCase, final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        for (int n = xmlPullParser.getEventType(); n != 3 || !"base-condition".equals(xmlPullParser.getName()); n = xmlPullParser.next()) {
            if (n == 2) {
                if ("key-value-set".equals(xmlPullParser.getName())) {
                    validationCase.addBaseCondition(new KeyValueSet(xmlPullParser.getAttributeValue(0), xmlPullParser.getAttributeValue(1)));
                }
            }
        }
    }
    
    private static void loadCheckCondition(final ValidationCase validationCase, final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        for (int n = xmlPullParser.getEventType(); n != 3 || !"check-condition".equals(xmlPullParser.getName()); n = xmlPullParser.next()) {
            if (n == 2) {
                if ("key-value-set".equals(xmlPullParser.getName())) {
                    validationCase.addCheckCondition(new KeyValueSet(xmlPullParser.getAttributeValue(0), xmlPullParser.getAttributeValue(1)));
                }
            }
        }
    }
    
    public static void loadCheckList(final Context context) {
        if (CamLog.DEBUG) {
            loadAvailableKeys(context);
            loadCheckList(context, CameraParameterValidator.mInvalidSetList, 2131886080);
            loadCheckList(context, CameraParameterValidator.mValidSetList, 2131886081);
        }
    }
    
    private static void loadCheckList(Context context, final List<ValidationCase> list, int i) {
        try {
            final XmlResourceParser xml = context.getResources().getXml(i);
            if (xml != null) {
                final Context context2 = context = null;
                while (true) {
                    try {
                        try {
                            i = xml.getEventType();
                            ValidationCase validationCase = null;
                            while (i != 1) {
                                context = context2;
                                final String name = xml.getName();
                                ValidationCase validationCase2 = null;
                                switch (i) {
                                    default: {
                                        validationCase2 = validationCase;
                                        break;
                                    }
                                    case 3: {
                                        validationCase2 = validationCase;
                                        context = context2;
                                        if ("validation-set".equals(name)) {
                                            context = context2;
                                            list.add(validationCase);
                                            validationCase2 = validationCase;
                                            break;
                                        }
                                        break;
                                    }
                                    case 2: {
                                        context = context2;
                                        if ("validation-set".equals(name)) {
                                            context = context2;
                                            validationCase2 = new ValidationCase();
                                            break;
                                        }
                                        context = context2;
                                        if ("base-condition".equals(name)) {
                                            context = context2;
                                            loadBaseCondition(validationCase, (XmlPullParser)xml);
                                            validationCase2 = validationCase;
                                            break;
                                        }
                                        validationCase2 = validationCase;
                                        context = context2;
                                        if ("check-condition".equals(name)) {
                                            context = context2;
                                            loadCheckCondition(validationCase, (XmlPullParser)xml);
                                            validationCase2 = validationCase;
                                            break;
                                        }
                                        break;
                                    }
                                }
                                context = context2;
                                i = xml.next();
                                validationCase = validationCase2;
                            }
                        }
                        finally {
                            if (xml != null) {
                                if (context != null) {
                                    final XmlResourceParser xmlResourceParser = xml;
                                    xmlResourceParser.close();
                                }
                                else {
                                    xml.close();
                                }
                            }
                        }
                    }
                    catch (final Throwable t) {}
                    try {
                        final XmlResourceParser xmlResourceParser = xml;
                        xmlResourceParser.close();
                        continue;
                    }
                    catch (final Throwable t2) {}
                    break;
                }
            }
            if (xml != null) {
                xml.close();
            }
        }
        catch (final IOException ex) {
            CamLog.e("Fail to load of CheckList.", ex);
            throw new ParameterValidationError("loadCheckList():[IOException]");
        }
        catch (final XmlPullParserException ex2) {
            CamLog.e("Fail to load of CheckList.", (Throwable)ex2);
            throw new ParameterValidationError("loadCheckList():[XmlPullParserException]");
        }
    }
    
    static void validate(final String s, final CaptureRequestHolder captureRequestHolder) {
        synchronized (CameraParameterValidator.class) {
            if (CamLog.DEBUG) {
                final List list = CameraParameterValidator.mKeysMap.get(s);
                final Rect rect = CameraParameterValidator.mActiveArraySizeMap.get(s);
                for (final ValidationCase validationCase : CameraParameterValidator.mInvalidSetList) {
                    if (validationCase.isBaseCondition(list, captureRequestHolder)) {
                        validationCase.checkInvalid(list, captureRequestHolder, rect);
                    }
                }
                for (final ValidationCase validationCase2 : CameraParameterValidator.mValidSetList) {
                    if (validationCase2.isBaseCondition(list, captureRequestHolder)) {
                        validationCase2.checkValid(list, captureRequestHolder, rect);
                    }
                }
            }
        }
    }
    
    private static class KeyValueSet
    {
        public final String key;
        public final String value;
        
        public KeyValueSet(final String key, final String value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("KeyValueSet [KEY=");
            sb.append(this.key);
            sb.append("] [VALUE=");
            sb.append(this.value);
            sb.append("]");
            return sb.toString();
        }
    }
    
    public static class ParameterValidationError extends RuntimeException
    {
        private static final long serialVersionUID = 0L;
        
        public ParameterValidationError(final String message) {
            super(message);
        }
    }
    
    private static class ValidationCase
    {
        private List<KeyValueSet> mBaseConditionList;
        private List<KeyValueSet> mCheckConditionList;
        
        private ValidationCase() {
            this.mBaseConditionList = new ArrayList<KeyValueSet>();
            this.mCheckConditionList = new ArrayList<KeyValueSet>();
        }
        
        private void addBaseCondition(final KeyValueSet set) {
            this.mBaseConditionList.add(set);
        }
        
        private void addCheckCondition(final KeyValueSet set) {
            this.mCheckConditionList.add(set);
        }
        
        private void checkInvalid(final List<CaptureRequest$Key<?>> list, final CaptureRequestHolder captureRequestHolder, final Rect obj) {
            for (final KeyValueSet set : this.mCheckConditionList) {
                final CaptureRequest$Key targetKey = this.getTargetKey(list, set);
                if (targetKey == null) {
                    continue;
                }
                if (set.value.equals("active-array-size")) {
                    String anObject;
                    if (captureRequestHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)targetKey) instanceof MeteringRectangle[]) {
                        anObject = ((MeteringRectangle[])captureRequestHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)targetKey))[0].getRect().toString();
                    }
                    else {
                        anObject = String.valueOf(captureRequestHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)targetKey));
                    }
                    if (obj.toString().equals(anObject)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("ERROR : [KEY=");
                        sb.append(set.key);
                        sb.append("]\n  INVALID  : [VALUE=");
                        sb.append(obj);
                        sb.append("]\n");
                        throw new ParameterValidationError(sb.toString());
                    }
                    continue;
                }
                else {
                    if (set.value.equals(String.valueOf(captureRequestHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)targetKey)))) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("ERROR : [KEY=");
                        sb2.append(set.key);
                        sb2.append("]\n  INVALID  : [VALUE=");
                        sb2.append(set.value);
                        sb2.append("]\n");
                        throw new ParameterValidationError(sb2.toString());
                    }
                    continue;
                }
            }
        }
        
        private void checkValid(final List<CaptureRequest$Key<?>> list, final CaptureRequestHolder captureRequestHolder, final Rect obj) {
            for (final KeyValueSet set : this.mCheckConditionList) {
                final CaptureRequest$Key targetKey = this.getTargetKey(list, set);
                if (targetKey == null) {
                    continue;
                }
                if (set.value.equals("active-array-size")) {
                    String s;
                    if (captureRequestHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)targetKey) instanceof MeteringRectangle[]) {
                        s = ((MeteringRectangle[])captureRequestHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)targetKey))[0].getRect().toString();
                    }
                    else {
                        s = String.valueOf(captureRequestHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)targetKey));
                    }
                    if (!obj.toString().equals(s)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("ERROR : [KEY=");
                        sb.append(set.key);
                        sb.append("]\n  EXPECTED : [VALUE=");
                        sb.append(obj);
                        sb.append("]\n  ACTUAL   : [VALUE=");
                        sb.append(s);
                        sb.append("]\n");
                        throw new ParameterValidationError(sb.toString());
                    }
                    continue;
                }
                else {
                    final String value = String.valueOf(captureRequestHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)targetKey));
                    if (!set.value.equals(value)) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("ERROR : [KEY=");
                        sb2.append(set.key);
                        sb2.append("]\n  EXPECTED : [VALUE=");
                        sb2.append(set.value);
                        sb2.append("]\n  ACTUAL   : [VALUE=");
                        sb2.append(value);
                        sb2.append("]\n");
                        throw new ParameterValidationError(sb2.toString());
                    }
                    continue;
                }
            }
        }
        
        private CaptureRequest$Key<?> getApplicationCaptureRequestKey(final CaptureRequest$Key<?> captureRequest$Key) {
            final String name = captureRequest$Key.getName();
            if (name != null) {
                for (final CaptureRequest$Key captureRequest$Key2 : SomcCameraDeviceInfo.getAllCaptureRequestKeys()) {
                    if (captureRequest$Key2.getName().equals(name)) {
                        return (CaptureRequest$Key<?>)captureRequest$Key2;
                    }
                }
            }
            return captureRequest$Key;
        }
        
        private CaptureRequest$Key getTargetKey(final List<CaptureRequest$Key<?>> list, final KeyValueSet set) {
            for (final CaptureRequest$Key captureRequest$Key : list) {
                if (captureRequest$Key.getName().equals(set.key)) {
                    return this.getApplicationCaptureRequestKey((CaptureRequest$Key<?>)captureRequest$Key);
                }
            }
            return null;
        }
        
        private boolean isBaseCondition(final List<CaptureRequest$Key<?>> list, final CaptureRequestHolder captureRequestHolder) {
            for (final KeyValueSet set : this.mBaseConditionList) {
                final CaptureRequest$Key targetKey = this.getTargetKey(list, set);
                if (targetKey == null) {
                    continue;
                }
                if (!set.value.equals(String.valueOf(captureRequestHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)targetKey)))) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ValidationSet :\n");
            sb.append("  BaseConditionList :\n");
            for (final KeyValueSet set : this.mBaseConditionList) {
                sb.append("    ");
                sb.append(set.toString());
                sb.append("\n");
            }
            sb.append("  CheckConditionList :\n");
            for (final KeyValueSet set2 : this.mCheckConditionList) {
                sb.append("    ");
                sb.append(set2.toString());
                sb.append("\n");
            }
            return sb.toString();
        }
    }
}
