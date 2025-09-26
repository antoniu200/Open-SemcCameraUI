// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.content.pm;

import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.content.pm.ShortcutInfo$Builder;
import android.content.pm.ShortcutInfo;
import java.util.Arrays;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.content.pm.PackageManager$NameNotFoundException;
import android.os.Parcelable;
import android.content.Intent;
import android.support.v4.graphics.drawable.IconCompat;
import android.content.Context;
import android.content.ComponentName;

public class ShortcutInfoCompat
{
    ComponentName mActivity;
    Context mContext;
    CharSequence mDisabledMessage;
    IconCompat mIcon;
    String mId;
    Intent[] mIntents;
    boolean mIsAlwaysBadged;
    CharSequence mLabel;
    CharSequence mLongLabel;
    
    ShortcutInfoCompat() {
    }
    
    Intent addToIntent(final Intent intent) {
        intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)this.mIntents[this.mIntents.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        if (this.mIcon == null) {
            return intent;
        }
        Drawable loadIcon = null;
        final Drawable drawable = null;
        Label_0100: {
            if (!this.mIsAlwaysBadged) {
                break Label_0100;
            }
            final PackageManager packageManager = this.mContext.getPackageManager();
            Drawable activityIcon = drawable;
            while (true) {
                if (this.mActivity == null) {
                    break Label_0081;
                }
                try {
                    activityIcon = packageManager.getActivityIcon(this.mActivity);
                    if ((loadIcon = activityIcon) == null) {
                        loadIcon = this.mContext.getApplicationInfo().loadIcon(packageManager);
                    }
                    this.mIcon.addToShortcutIntent(intent, loadIcon, this.mContext);
                    return intent;
                }
                catch (final PackageManager$NameNotFoundException ex) {
                    activityIcon = drawable;
                    continue;
                }
                break;
            }
        }
    }
    
    @Nullable
    public ComponentName getActivity() {
        return this.mActivity;
    }
    
    @Nullable
    public CharSequence getDisabledMessage() {
        return this.mDisabledMessage;
    }
    
    @NonNull
    public String getId() {
        return this.mId;
    }
    
    @NonNull
    public Intent getIntent() {
        return this.mIntents[this.mIntents.length - 1];
    }
    
    @NonNull
    public Intent[] getIntents() {
        return Arrays.copyOf(this.mIntents, this.mIntents.length);
    }
    
    @Nullable
    public CharSequence getLongLabel() {
        return this.mLongLabel;
    }
    
    @NonNull
    public CharSequence getShortLabel() {
        return this.mLabel;
    }
    
    @RequiresApi(25)
    public ShortcutInfo toShortcutInfo() {
        final ShortcutInfo$Builder setIntents = new ShortcutInfo$Builder(this.mContext, this.mId).setShortLabel(this.mLabel).setIntents(this.mIntents);
        if (this.mIcon != null) {
            setIntents.setIcon(this.mIcon.toIcon());
        }
        if (!TextUtils.isEmpty(this.mLongLabel)) {
            setIntents.setLongLabel(this.mLongLabel);
        }
        if (!TextUtils.isEmpty(this.mDisabledMessage)) {
            setIntents.setDisabledMessage(this.mDisabledMessage);
        }
        if (this.mActivity != null) {
            setIntents.setActivity(this.mActivity);
        }
        return setIntents.build();
    }
    
    public static class Builder
    {
        private final ShortcutInfoCompat mInfo;
        
        public Builder(@NonNull final Context mContext, @NonNull final String mId) {
            this.mInfo = new ShortcutInfoCompat();
            this.mInfo.mContext = mContext;
            this.mInfo.mId = mId;
        }
        
        @NonNull
        public ShortcutInfoCompat build() {
            if (TextUtils.isEmpty(this.mInfo.mLabel)) {
                throw new IllegalArgumentException("Shortcut must have a non-empty label");
            }
            if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
                return this.mInfo;
            }
            throw new IllegalArgumentException("Shortcut must have an intent");
        }
        
        @NonNull
        public Builder setActivity(@NonNull final ComponentName mActivity) {
            this.mInfo.mActivity = mActivity;
            return this;
        }
        
        public Builder setAlwaysBadged() {
            this.mInfo.mIsAlwaysBadged = true;
            return this;
        }
        
        @NonNull
        public Builder setDisabledMessage(@NonNull final CharSequence mDisabledMessage) {
            this.mInfo.mDisabledMessage = mDisabledMessage;
            return this;
        }
        
        @NonNull
        public Builder setIcon(final IconCompat mIcon) {
            this.mInfo.mIcon = mIcon;
            return this;
        }
        
        @NonNull
        public Builder setIntent(@NonNull final Intent intent) {
            return this.setIntents(new Intent[] { intent });
        }
        
        @NonNull
        public Builder setIntents(@NonNull final Intent[] mIntents) {
            this.mInfo.mIntents = mIntents;
            return this;
        }
        
        @NonNull
        public Builder setLongLabel(@NonNull final CharSequence mLongLabel) {
            this.mInfo.mLongLabel = mLongLabel;
            return this;
        }
        
        @NonNull
        public Builder setShortLabel(@NonNull final CharSequence mLabel) {
            this.mInfo.mLabel = mLabel;
            return this;
        }
    }
}
