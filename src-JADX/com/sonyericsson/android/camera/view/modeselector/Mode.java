package com.sonyericsson.android.camera.view.modeselector;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class Mode {
    public static final int INVALID_ID = 0;
    protected final Context mContext;
    protected String mId;
    protected OnStateChangeListener mStateChangeListener;

    public interface OnStateChangeListener {
        void onAvailabilityChanged(Mode mode, boolean z);
    }

    protected abstract String generateSmallIconMappingName();

    protected abstract String getModeName();

    public abstract int getSelectorIconResId();

    public boolean isAvailable() {
        return true;
    }

    protected Mode(@NonNull Context context, @NonNull String str) {
        this.mContext = context;
        this.mId = str;
    }

    public String getId() {
        return this.mId;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mStateChangeListener = onStateChangeListener;
    }

    public void loadSmallIcon(@NonNull ImageView imageView) throws Resources.NotFoundException {
        String strGenerateSmallIconMappingName = generateSmallIconMappingName();
        int identifier = this.mContext.getResources().getIdentifier(strGenerateSmallIconMappingName, ResourceUtil.RES_TYPE_NAME_DRAWABLE, this.mContext.getPackageName());
        if (identifier != 0) {
            imageView.setPadding(0, 0, 0, 0);
            imageView.setImageResource(identifier);
        } else {
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.mru_button_selector_icon_padding_size);
            imageView.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
            int selectorIconResId = getSelectorIconResId();
            int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R.dimen.mru_button_container_size);
            imageView.setImageBitmap(ResourceUtil.getBitmap(this.mContext, strGenerateSmallIconMappingName, selectorIconResId, dimensionPixelSize2, dimensionPixelSize2));
        }
        String modeName = getModeName();
        if (modeName != null) {
            imageView.setContentDescription(modeName);
        }
    }

    public boolean compare(Mode mode) {
        if (mode == null) {
            return false;
        }
        return mode.getId().equals(getId());
    }
}
