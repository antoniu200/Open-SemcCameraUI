package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LedOptionsResolver {
    public static final String TAG = "LedOptionsResolver";
    private static LedOptionsResolver sInstance = new LedOptionsResolver();
    private Resolver mResolver = new Unsolved();

    private LedOptionsResolver() {
    }

    public static LedOptionsResolver getInstance() {
        return sInstance;
    }

    public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
        if (list == null || list.size() == 0) {
            return new Flash[0];
        }
        return this.mResolver.getFlashOptions(actionMode, list);
    }

    public DisplayFlash[] getDisplayFlashOptions(ActionMode actionMode, List<String> list) {
        if (list == null || list.size() == 0) {
            return new DisplayFlash[0];
        }
        return this.mResolver.getDisplayFlashOptions(actionMode, list);
    }

    public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
        return this.mResolver.getPhotoLightOptions(actionMode, list);
    }

    public int getParameterKeyTextId() {
        return this.mResolver.getParameterKeyTextId();
    }

    public int getParameterKeyTitleTextId() {
        return this.mResolver.getParameterKeyTitleTextId();
    }

    public Flash getDefaultFlash() {
        return this.mResolver.getDefaultFlash();
    }

    public DisplayFlash getDefaultDisplayFlash() {
        return this.mResolver.getDefaultDisplayFlash();
    }

    private abstract class Resolver {
        public abstract DisplayFlash getDefaultDisplayFlash();

        public abstract Flash getDefaultFlash();

        public abstract DisplayFlash[] getDisplayFlashOptions(ActionMode actionMode, List<String> list);

        public abstract Flash[] getFlashOptions(ActionMode actionMode, List<String> list);

        public abstract int getParameterKeyTextId();

        public abstract int getParameterKeyTitleTextId();

        public abstract PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list);

        private Resolver() {
        }
    }

    private class Unsolved extends Resolver {
        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public int getParameterKeyTextId() {
            return -1;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public int getParameterKeyTitleTextId() {
            return -1;
        }

        private Unsolved() {
            super();
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
            Resolver resolver = getResolver(actionMode.mType, list);
            if (resolver != null) {
                LedOptionsResolver.this.mResolver = resolver;
                return LedOptionsResolver.this.mResolver.getFlashOptions(actionMode, list);
            }
            return new Flash[0];
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public DisplayFlash[] getDisplayFlashOptions(ActionMode actionMode, List<String> list) {
            Resolver resolver = getResolver(actionMode.mType, list);
            if (resolver != null) {
                LedOptionsResolver.this.mResolver = resolver;
                return LedOptionsResolver.this.mResolver.getDisplayFlashOptions(actionMode, list);
            }
            return new DisplayFlash[0];
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
            Resolver resolver = getResolver(actionMode.mType, list);
            if (resolver != null) {
                LedOptionsResolver.this.mResolver = resolver;
                return LedOptionsResolver.this.mResolver.getPhotoLightOptions(actionMode, list);
            }
            return new PhotoLight[0];
        }

        private Resolver getResolver(int i, List<String> list) {
            if (list != null && !list.isEmpty()) {
                for (String str : list) {
                    if (Flash.ON.getValue().equals(str) || DisplayFlash.DISPLAY_ON.getValue().equals(str)) {
                        return new FlashIn();
                    }
                    if (Flash.LED_ON.getValue().equals(str)) {
                        if (i == 1) {
                            return new FlashNotSupported();
                        }
                        return new PhotoLightIn();
                    }
                }
            }
            return null;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public Flash getDefaultFlash() {
            Resolver resolver = getResolver(1, PlatformCapability.getCameraCapability(CameraInfo.CameraId.BACK).FLASH.get());
            if (resolver != null) {
                LedOptionsResolver.this.mResolver = resolver;
                return LedOptionsResolver.this.mResolver.getDefaultFlash();
            }
            return Flash.LED_OFF;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public DisplayFlash getDefaultDisplayFlash() {
            Resolver resolver = getResolver(1, PlatformCapability.getCameraCapability(CameraInfo.CameraId.FRONT).FLASH.get());
            if (resolver != null) {
                LedOptionsResolver.this.mResolver = resolver;
                return LedOptionsResolver.this.mResolver.getDefaultDisplayFlash();
            }
            return DisplayFlash.DISPLAY_OFF;
        }
    }

    private class FlashIn extends Resolver {
        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public int getParameterKeyTextId() {
            return R.string.cam_strings_flash_txt;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public int getParameterKeyTitleTextId() {
            return R.string.cam_strings_flash_title_txt;
        }

        private FlashIn() {
            super();
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
            if (actionMode.mType == 2) {
                return new Flash[0];
            }
            return new Flash[]{Flash.AUTO, Flash.ON, Flash.RED_EYE, Flash.OFF, Flash.LED_ON};
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public DisplayFlash[] getDisplayFlashOptions(ActionMode actionMode, List<String> list) {
            if (!DisplayFlash.isSupported(actionMode.mCameraId) || actionMode.mType == 2) {
                return new DisplayFlash[0];
            }
            return new DisplayFlash[]{DisplayFlash.DISPLAY_AUTO, DisplayFlash.DISPLAY_ON, DisplayFlash.DISPLAY_OFF};
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
            if (actionMode.mType == 2) {
                return PhotoLight.values();
            }
            return new PhotoLight[0];
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public Flash getDefaultFlash() {
            return Flash.OFF;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public DisplayFlash getDefaultDisplayFlash() {
            return DisplayFlash.DISPLAY_AUTO;
        }
    }

    private class FlashNotSupported extends PhotoLightIn {
        private FlashNotSupported() {
            super();
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.PhotoLightIn, com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
            if (actionMode.mType == 2) {
                return new Flash[0];
            }
            return new Flash[]{Flash.PHOTO_LIGHT_ON_AS_FLASH, Flash.LED_OFF};
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.PhotoLightIn, com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public DisplayFlash[] getDisplayFlashOptions(ActionMode actionMode, List<String> list) {
            return new DisplayFlash[0];
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.PhotoLightIn, com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
            if (actionMode.mType == 2) {
                return PhotoLight.values();
            }
            return new PhotoLight[0];
        }
    }

    private class PhotoLightIn extends Resolver {
        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public int getParameterKeyTextId() {
            return R.string.cam_strings_flash_torch_txt;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public int getParameterKeyTitleTextId() {
            return R.string.cam_strings_flash_torch_txt;
        }

        private PhotoLightIn() {
            super();
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
            return new Flash[0];
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public DisplayFlash[] getDisplayFlashOptions(ActionMode actionMode, List<String> list) {
            return new DisplayFlash[0];
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
            return PhotoLight.values();
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public Flash getDefaultFlash() {
            return Flash.LED_OFF;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver.Resolver
        public DisplayFlash getDefaultDisplayFlash() {
            return DisplayFlash.DISPLAY_OFF;
        }
    }
}
