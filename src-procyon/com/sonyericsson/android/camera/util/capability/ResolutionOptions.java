// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import java.util.Iterator;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import android.graphics.Rect;
import java.util.List;
import android.content.Context;

public class ResolutionOptions
{
    public static final String TAG = "ResolutionOptions";
    private final String mDefaultResolution;
    private final String mDefaultVideoSize;
    private final String[] mResolutionOptions;
    private final String[] mSuperiorAutoResolutionOptions;
    private final String[] mVideoSizeOptions;
    
    public ResolutionOptions() {
        this.mResolutionOptions = new String[0];
        this.mSuperiorAutoResolutionOptions = new String[0];
        this.mVideoSizeOptions = new String[0];
        this.mDefaultResolution = "";
        this.mDefaultVideoSize = "";
    }
    
    public ResolutionOptions(final Context context, final String s, final List<Rect> list) {
        final Boolean value = ResolutionDependence.isDependOnAspect(context);
        int n = 0;
        Label_0340: {
            switch (s.hashCode()) {
                case 1983864131: {
                    if (s.equals("SEM13BS1")) {
                        n = 5;
                        break Label_0340;
                    }
                    break;
                }
                case 544590039: {
                    if (s.equals("MTM13BS0")) {
                        n = 11;
                        break Label_0340;
                    }
                    break;
                }
                case 34617135: {
                    if (s.equals("LGI13BS0")) {
                        n = 10;
                        break Label_0340;
                    }
                    break;
                }
                case 33842569: {
                    if (s.equals("LGI08BS0")) {
                        n = 6;
                        break Label_0340;
                    }
                    break;
                }
                case 33753042: {
                    if (s.equals("LGI05BN1")) {
                        n = 8;
                        break Label_0340;
                    }
                    break;
                }
                case 0: {
                    if (s.equals("")) {
                        n = 12;
                        break Label_0340;
                    }
                    break;
                }
                case -1853387926: {
                    if (s.equals("SOS20FW0")) {
                        n = 2;
                        break Label_0340;
                    }
                    break;
                }
                case -1879044877: {
                    if (s.equals("CHI08BS0")) {
                        n = 7;
                        break Label_0340;
                    }
                    break;
                }
                case -1879134404: {
                    if (s.equals("CHI05BN1")) {
                        n = 9;
                        break Label_0340;
                    }
                    break;
                }
                case -2139683387: {
                    if (s.equals("SOI20BSA")) {
                        n = 3;
                        break Label_0340;
                    }
                    break;
                }
                case -2139683402: {
                    if (s.equals("SOI20BS2")) {
                        n = 1;
                        break Label_0340;
                    }
                    break;
                }
                case -2139683404: {
                    if (s.equals("SOI20BS0")) {
                        n = 0;
                        break Label_0340;
                    }
                    break;
                }
                case -2140517551: {
                    if (s.equals("SOI13BS1")) {
                        n = 4;
                        break Label_0340;
                    }
                    break;
                }
            }
            n = -1;
        }
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (n) {
            default: {
                this.mResolutionOptions = this.getMaxResolutions(list);
                this.mSuperiorAutoResolutionOptions = this.mResolutionOptions;
                if (this.getMaxResolutions(list).length == 2) {
                    if (value) {
                        this.mDefaultResolution = this.getMaxResolutions(list)[0];
                    }
                    else {
                        this.mDefaultResolution = this.getMaxResolutions(list)[1];
                    }
                }
                else {
                    this.mDefaultResolution = this.getMaxResolutions(list)[0];
                }
                this.mVideoSizeOptions = context.getResources().getStringArray(2130903057);
                this.mDefaultVideoSize = context.getResources().getString(2131690340);
                return;
            }
            case 10:
            case 11: {
                n2 = 2130903044;
                n3 = 2130903050;
                n4 = 2130903059;
                n5 = 2131690325;
                n6 = 2131690343;
                break;
            }
            case 8:
            case 9: {
                n2 = 2130903043;
                n3 = 2130903049;
                n4 = 2130903058;
                n5 = 2131690324;
                n6 = 2131690342;
                break;
            }
            case 6:
            case 7: {
                n2 = 2130903045;
                n3 = 2130903051;
                n4 = 2130903060;
                n5 = 2131690326;
                n6 = 2131690344;
                break;
            }
            case 4:
            case 5: {
                n2 = 2130903040;
                n3 = 2130903046;
                n4 = 2130903054;
                n5 = 2131690321;
                n6 = 2131690337;
                break;
            }
            case 2:
            case 3: {
                n2 = 2130903042;
                n3 = 2130903048;
                if (this.is4KVideoSizeSupported(context)) {
                    n4 = 2130903053;
                }
                else {
                    n4 = 2130903056;
                }
                if (value) {
                    n5 = 2131690323;
                }
                else {
                    n5 = 2131690336;
                }
                n6 = 2131690339;
                break;
            }
            case 0:
            case 1: {
                n2 = 2130903041;
                n3 = 2130903047;
                if (this.is4KVideoSizeSupported(context)) {
                    n4 = 2130903052;
                }
                else {
                    n4 = 2130903055;
                }
                if (value) {
                    n5 = 2131690322;
                }
                else {
                    n5 = 2131690335;
                }
                n6 = 2131690338;
                break;
            }
        }
        this.mResolutionOptions = context.getResources().getStringArray(n2);
        this.mSuperiorAutoResolutionOptions = context.getResources().getStringArray(n3);
        this.mVideoSizeOptions = context.getResources().getStringArray(n4);
        this.mDefaultResolution = context.getResources().getString(n5);
        this.mDefaultVideoSize = context.getResources().getString(n6);
    }
    
    private Resolution findResolution(final Rect rect) {
        for (final Resolution resolution : Resolution.values()) {
            if (resolution.getPictureRect().width() == rect.width() && resolution.getPictureRect().height() == rect.height()) {
                return resolution;
            }
        }
        return null;
    }
    
    private String[] getMaxResolutions(final List<Rect> list) {
        final Iterator<Rect> iterator = list.iterator();
        Resolution resolution = null;
        Resolution resolution2 = null;
        while (iterator.hasNext()) {
            final Rect rect = iterator.next();
            if (isAspectRatio4_3(rect)) {
                final Resolution resolution3 = this.findResolution(rect);
                if (resolution3 == null || (resolution != null && rect.width() * rect.height() <= resolution.getPictureRect().width() * resolution.getPictureRect().height())) {
                    continue;
                }
                resolution = resolution3;
            }
            else {
                if (!isAspectRatio16_9(rect)) {
                    continue;
                }
                final Resolution resolution4 = this.findResolution(rect);
                if (resolution4 == null || (resolution2 != null && rect.width() * rect.height() <= resolution2.getPictureRect().width() * resolution2.getPictureRect().height())) {
                    continue;
                }
                resolution2 = resolution4;
            }
        }
        if (resolution != null && resolution2 != null) {
            return new String[] { resolution.getValue(), resolution2.getValue() };
        }
        if (resolution != null) {
            return new String[] { resolution.getValue() };
        }
        if (resolution2 != null) {
            return new String[] { resolution2.getValue() };
        }
        return new String[] { Resolution.VGA.getValue() };
    }
    
    private boolean is4KVideoSizeSupported(final Context context) {
        return context.getResources().getBoolean(2131034117);
    }
    
    private static boolean isAspectRatio16_9(final Rect rect) {
        boolean b = false;
        if (rect != null && rect.width() != 0 && rect.height() != 0) {
            if (rect.width() * 9 == rect.height() * 16) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    private static boolean isAspectRatio4_3(final Rect rect) {
        boolean b = false;
        if (rect != null && rect.width() != 0 && rect.height() != 0) {
            if (rect.width() * 3 == rect.height() * 4) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    public String getDefaultResolution() {
        return this.mDefaultResolution;
    }
    
    public String getDefaultVideoSize() {
        return this.mDefaultVideoSize;
    }
    
    public String[] getResolutionOptions() {
        return this.mResolutionOptions.clone();
    }
    
    public String[] getSuperiorAutoResolutionOptions() {
        return this.mSuperiorAutoResolutionOptions.clone();
    }
    
    public String[] getVideoSizeOptions() {
        return this.mVideoSizeOptions.clone();
    }
}
