// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.device.CameraInfo;
import java.util.Iterator;
import java.util.List;
import com.sonyericsson.android.camera.ActionMode;

public class LedOptionsResolver
{
    public static final String TAG = "LedOptionsResolver";
    private static LedOptionsResolver sInstance;
    private Resolver mResolver;
    
    static {
        LedOptionsResolver.sInstance = new LedOptionsResolver();
    }
    
    private LedOptionsResolver() {
        this.mResolver = (Resolver)new Unsolved();
    }
    
    public static LedOptionsResolver getInstance() {
        return LedOptionsResolver.sInstance;
    }
    
    public DisplayFlash getDefaultDisplayFlash() {
        return this.mResolver.getDefaultDisplayFlash();
    }
    
    public Flash getDefaultFlash() {
        return this.mResolver.getDefaultFlash();
    }
    
    public DisplayFlash[] getDisplayFlashOptions(final ActionMode actionMode, final List<String> list) {
        if (list != null && list.size() != 0) {
            return this.mResolver.getDisplayFlashOptions(actionMode, list);
        }
        return new DisplayFlash[0];
    }
    
    public Flash[] getFlashOptions(final ActionMode actionMode, final List<String> list) {
        if (list != null && list.size() != 0) {
            return this.mResolver.getFlashOptions(actionMode, list);
        }
        return new Flash[0];
    }
    
    public int getParameterKeyTextId() {
        return this.mResolver.getParameterKeyTextId();
    }
    
    public int getParameterKeyTitleTextId() {
        return this.mResolver.getParameterKeyTitleTextId();
    }
    
    public PhotoLight[] getPhotoLightOptions(final ActionMode actionMode, final List<String> list) {
        return this.mResolver.getPhotoLightOptions(actionMode, list);
    }
    
    private class FlashIn extends Resolver
    {
        final LedOptionsResolver this$0;
        
        private FlashIn(final LedOptionsResolver this$0) {
        }
        
        @Override
        public DisplayFlash getDefaultDisplayFlash() {
            return DisplayFlash.DISPLAY_AUTO;
        }
        
        @Override
        public Flash getDefaultFlash() {
            return Flash.OFF;
        }
        
        @Override
        public DisplayFlash[] getDisplayFlashOptions(final ActionMode actionMode, final List<String> list) {
            if (DisplayFlash.isSupported(actionMode.mCameraId) && actionMode.mType != 2) {
                return new DisplayFlash[] { DisplayFlash.DISPLAY_AUTO, DisplayFlash.DISPLAY_ON, DisplayFlash.DISPLAY_OFF };
            }
            return new DisplayFlash[0];
        }
        
        @Override
        public Flash[] getFlashOptions(final ActionMode actionMode, final List<String> list) {
            if (actionMode.mType == 2) {
                return new Flash[0];
            }
            return new Flash[] { Flash.AUTO, Flash.ON, Flash.RED_EYE, Flash.OFF, Flash.LED_ON };
        }
        
        @Override
        public int getParameterKeyTextId() {
            return 2131689842;
        }
        
        @Override
        public int getParameterKeyTitleTextId() {
            return 2131689840;
        }
        
        @Override
        public PhotoLight[] getPhotoLightOptions(final ActionMode actionMode, final List<String> list) {
            if (actionMode.mType == 2) {
                return PhotoLight.values();
            }
            return new PhotoLight[0];
        }
    }
    
    private abstract class Resolver
    {
        final LedOptionsResolver this$0;
        
        private Resolver(final LedOptionsResolver this$0) {
            this.this$0 = this$0;
        }
        
        public abstract DisplayFlash getDefaultDisplayFlash();
        
        public abstract Flash getDefaultFlash();
        
        public abstract DisplayFlash[] getDisplayFlashOptions(final ActionMode p0, final List<String> p1);
        
        public abstract Flash[] getFlashOptions(final ActionMode p0, final List<String> p1);
        
        public abstract int getParameterKeyTextId();
        
        public abstract int getParameterKeyTitleTextId();
        
        public abstract PhotoLight[] getPhotoLightOptions(final ActionMode p0, final List<String> p1);
    }
    
    private class FlashNotSupported extends PhotoLightIn
    {
        final LedOptionsResolver this$0;
        
        private FlashNotSupported(final LedOptionsResolver this$0) {
        }
        
        @Override
        public DisplayFlash[] getDisplayFlashOptions(final ActionMode actionMode, final List<String> list) {
            return new DisplayFlash[0];
        }
        
        @Override
        public Flash[] getFlashOptions(final ActionMode actionMode, final List<String> list) {
            if (actionMode.mType == 2) {
                return new Flash[0];
            }
            return new Flash[] { Flash.PHOTO_LIGHT_ON_AS_FLASH, Flash.LED_OFF };
        }
        
        @Override
        public PhotoLight[] getPhotoLightOptions(final ActionMode actionMode, final List<String> list) {
            if (actionMode.mType == 2) {
                return PhotoLight.values();
            }
            return new PhotoLight[0];
        }
    }
    
    private class PhotoLightIn extends Resolver
    {
        final LedOptionsResolver this$0;
        
        private PhotoLightIn(final LedOptionsResolver this$0) {
        }
        
        @Override
        public DisplayFlash getDefaultDisplayFlash() {
            return DisplayFlash.DISPLAY_OFF;
        }
        
        @Override
        public Flash getDefaultFlash() {
            return Flash.LED_OFF;
        }
        
        @Override
        public DisplayFlash[] getDisplayFlashOptions(final ActionMode actionMode, final List<String> list) {
            return new DisplayFlash[0];
        }
        
        @Override
        public Flash[] getFlashOptions(final ActionMode actionMode, final List<String> list) {
            return new Flash[0];
        }
        
        @Override
        public int getParameterKeyTextId() {
            return 2131689841;
        }
        
        @Override
        public int getParameterKeyTitleTextId() {
            return 2131689841;
        }
        
        @Override
        public PhotoLight[] getPhotoLightOptions(final ActionMode actionMode, final List<String> list) {
            return PhotoLight.values();
        }
    }
    
    private class Unsolved extends Resolver
    {
        final LedOptionsResolver this$0;
        
        private Unsolved(final LedOptionsResolver this$0) {
        }
        
        private Resolver getResolver(final int n, final List<String> list) {
            if (list != null && !list.isEmpty()) {
                for (final String anObject : list) {
                    if (Flash.ON.getValue().equals(anObject) || DisplayFlash.DISPLAY_ON.getValue().equals(anObject)) {
                        return new FlashIn();
                    }
                    if (!Flash.LED_ON.getValue().equals(anObject)) {
                        continue;
                    }
                    if (n == 1) {
                        return new FlashNotSupported();
                    }
                    return new PhotoLightIn();
                }
            }
            return null;
        }
        
        @Override
        public DisplayFlash getDefaultDisplayFlash() {
            final Resolver resolver = this.getResolver(1, PlatformCapability.getCameraCapability(CameraInfo.CameraId.FRONT).FLASH.get());
            if (resolver != null) {
                this.this$0.mResolver = resolver;
                return this.this$0.mResolver.getDefaultDisplayFlash();
            }
            return DisplayFlash.DISPLAY_OFF;
        }
        
        @Override
        public Flash getDefaultFlash() {
            final Resolver resolver = this.getResolver(1, PlatformCapability.getCameraCapability(CameraInfo.CameraId.BACK).FLASH.get());
            if (resolver != null) {
                this.this$0.mResolver = resolver;
                return this.this$0.mResolver.getDefaultFlash();
            }
            return Flash.LED_OFF;
        }
        
        @Override
        public DisplayFlash[] getDisplayFlashOptions(final ActionMode actionMode, final List<String> list) {
            final Resolver resolver = this.getResolver(actionMode.mType, list);
            if (resolver != null) {
                this.this$0.mResolver = resolver;
                return this.this$0.mResolver.getDisplayFlashOptions(actionMode, list);
            }
            return new DisplayFlash[0];
        }
        
        @Override
        public Flash[] getFlashOptions(final ActionMode actionMode, final List<String> list) {
            final Resolver resolver = this.getResolver(actionMode.mType, list);
            if (resolver != null) {
                this.this$0.mResolver = resolver;
                return this.this$0.mResolver.getFlashOptions(actionMode, list);
            }
            return new Flash[0];
        }
        
        @Override
        public int getParameterKeyTextId() {
            return -1;
        }
        
        @Override
        public int getParameterKeyTitleTextId() {
            return -1;
        }
        
        @Override
        public PhotoLight[] getPhotoLightOptions(final ActionMode actionMode, final List<String> list) {
            final Resolver resolver = this.getResolver(actionMode.mType, list);
            if (resolver != null) {
                this.this$0.mResolver = resolver;
                return this.this$0.mResolver.getPhotoLightOptions(actionMode, list);
            }
            return new PhotoLight[0];
        }
    }
}
