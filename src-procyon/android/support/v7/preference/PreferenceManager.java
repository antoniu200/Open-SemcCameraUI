// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.preference;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.os.Build$VERSION;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.support.annotation.RestrictTo;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.content.SharedPreferences$Editor;
import android.content.Context;

public class PreferenceManager
{
    public static final String KEY_HAS_SET_DEFAULT_VALUES = "_has_set_default_values";
    private static final int STORAGE_DEFAULT = 0;
    private static final int STORAGE_DEVICE_PROTECTED = 1;
    private Context mContext;
    @Nullable
    private SharedPreferences$Editor mEditor;
    private long mNextId;
    private boolean mNoCommit;
    private OnDisplayPreferenceDialogListener mOnDisplayPreferenceDialogListener;
    private OnNavigateToScreenListener mOnNavigateToScreenListener;
    private OnPreferenceTreeClickListener mOnPreferenceTreeClickListener;
    private PreferenceComparisonCallback mPreferenceComparisonCallback;
    @Nullable
    private PreferenceDataStore mPreferenceDataStore;
    private PreferenceScreen mPreferenceScreen;
    @Nullable
    private SharedPreferences mSharedPreferences;
    private int mSharedPreferencesMode;
    private String mSharedPreferencesName;
    private int mStorage;
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PreferenceManager(final Context mContext) {
        this.mNextId = 0L;
        this.mStorage = 0;
        this.mContext = mContext;
        this.setSharedPreferencesName(getDefaultSharedPreferencesName(mContext));
    }
    
    public static SharedPreferences getDefaultSharedPreferences(final Context context) {
        return context.getSharedPreferences(getDefaultSharedPreferencesName(context), getDefaultSharedPreferencesMode());
    }
    
    private static int getDefaultSharedPreferencesMode() {
        return 0;
    }
    
    private static String getDefaultSharedPreferencesName(final Context context) {
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getPackageName());
        sb.append("_preferences");
        return sb.toString();
    }
    
    public static void setDefaultValues(final Context context, final int n, final boolean b) {
        setDefaultValues(context, getDefaultSharedPreferencesName(context), getDefaultSharedPreferencesMode(), n, b);
    }
    
    public static void setDefaultValues(final Context context, final String sharedPreferencesName, final int sharedPreferencesMode, final int n, final boolean b) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences("_has_set_default_values", 0);
        if (b || !sharedPreferences.getBoolean("_has_set_default_values", false)) {
            final PreferenceManager preferenceManager = new PreferenceManager(context);
            preferenceManager.setSharedPreferencesName(sharedPreferencesName);
            preferenceManager.setSharedPreferencesMode(sharedPreferencesMode);
            preferenceManager.inflateFromResource(context, n, null);
            sharedPreferences.edit().putBoolean("_has_set_default_values", true).apply();
        }
    }
    
    private void setNoCommit(final boolean mNoCommit) {
        if (!mNoCommit && this.mEditor != null) {
            this.mEditor.apply();
        }
        this.mNoCommit = mNoCommit;
    }
    
    public PreferenceScreen createPreferenceScreen(final Context context) {
        final PreferenceScreen preferenceScreen = new PreferenceScreen(context, null);
        preferenceScreen.onAttachedToHierarchy(this);
        return preferenceScreen;
    }
    
    public Preference findPreference(final CharSequence charSequence) {
        if (this.mPreferenceScreen == null) {
            return null;
        }
        return this.mPreferenceScreen.findPreference(charSequence);
    }
    
    public Context getContext() {
        return this.mContext;
    }
    
    SharedPreferences$Editor getEditor() {
        if (this.mPreferenceDataStore != null) {
            return null;
        }
        if (this.mNoCommit) {
            if (this.mEditor == null) {
                this.mEditor = this.getSharedPreferences().edit();
            }
            return this.mEditor;
        }
        return this.getSharedPreferences().edit();
    }
    
    long getNextId() {
        synchronized (this) {
            final long mNextId = this.mNextId;
            this.mNextId = 1L + mNextId;
            return mNextId;
        }
    }
    
    public OnDisplayPreferenceDialogListener getOnDisplayPreferenceDialogListener() {
        return this.mOnDisplayPreferenceDialogListener;
    }
    
    public OnNavigateToScreenListener getOnNavigateToScreenListener() {
        return this.mOnNavigateToScreenListener;
    }
    
    public OnPreferenceTreeClickListener getOnPreferenceTreeClickListener() {
        return this.mOnPreferenceTreeClickListener;
    }
    
    public PreferenceComparisonCallback getPreferenceComparisonCallback() {
        return this.mPreferenceComparisonCallback;
    }
    
    @Nullable
    public PreferenceDataStore getPreferenceDataStore() {
        return this.mPreferenceDataStore;
    }
    
    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceScreen;
    }
    
    public SharedPreferences getSharedPreferences() {
        if (this.getPreferenceDataStore() != null) {
            return null;
        }
        if (this.mSharedPreferences == null) {
            Context context;
            if (this.mStorage != 1) {
                context = this.mContext;
            }
            else {
                context = ContextCompat.createDeviceProtectedStorageContext(this.mContext);
            }
            this.mSharedPreferences = context.getSharedPreferences(this.mSharedPreferencesName, this.mSharedPreferencesMode);
        }
        return this.mSharedPreferences;
    }
    
    public int getSharedPreferencesMode() {
        return this.mSharedPreferencesMode;
    }
    
    public String getSharedPreferencesName() {
        return this.mSharedPreferencesName;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PreferenceScreen inflateFromResource(final Context context, final int n, final PreferenceScreen preferenceScreen) {
        this.setNoCommit(true);
        final PreferenceScreen preferenceScreen2 = (PreferenceScreen)new PreferenceInflater(context, this).inflate(n, preferenceScreen);
        preferenceScreen2.onAttachedToHierarchy(this);
        this.setNoCommit(false);
        return preferenceScreen2;
    }
    
    public boolean isStorageDefault() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = true;
        if (sdk_INT >= 24) {
            if (this.mStorage != 0) {
                b = false;
            }
            return b;
        }
        return true;
    }
    
    public boolean isStorageDeviceProtected() {
        if (Build$VERSION.SDK_INT >= 24) {
            final int mStorage = this.mStorage;
            boolean b = true;
            if (mStorage != 1) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    public void setOnDisplayPreferenceDialogListener(final OnDisplayPreferenceDialogListener mOnDisplayPreferenceDialogListener) {
        this.mOnDisplayPreferenceDialogListener = mOnDisplayPreferenceDialogListener;
    }
    
    public void setOnNavigateToScreenListener(final OnNavigateToScreenListener mOnNavigateToScreenListener) {
        this.mOnNavigateToScreenListener = mOnNavigateToScreenListener;
    }
    
    public void setOnPreferenceTreeClickListener(final OnPreferenceTreeClickListener mOnPreferenceTreeClickListener) {
        this.mOnPreferenceTreeClickListener = mOnPreferenceTreeClickListener;
    }
    
    public void setPreferenceComparisonCallback(final PreferenceComparisonCallback mPreferenceComparisonCallback) {
        this.mPreferenceComparisonCallback = mPreferenceComparisonCallback;
    }
    
    public void setPreferenceDataStore(final PreferenceDataStore mPreferenceDataStore) {
        this.mPreferenceDataStore = mPreferenceDataStore;
    }
    
    public boolean setPreferences(final PreferenceScreen mPreferenceScreen) {
        if (mPreferenceScreen != this.mPreferenceScreen) {
            if (this.mPreferenceScreen != null) {
                this.mPreferenceScreen.onDetached();
            }
            this.mPreferenceScreen = mPreferenceScreen;
            return true;
        }
        return false;
    }
    
    public void setSharedPreferencesMode(final int mSharedPreferencesMode) {
        this.mSharedPreferencesMode = mSharedPreferencesMode;
        this.mSharedPreferences = null;
    }
    
    public void setSharedPreferencesName(final String mSharedPreferencesName) {
        this.mSharedPreferencesName = mSharedPreferencesName;
        this.mSharedPreferences = null;
    }
    
    public void setStorageDefault() {
        if (Build$VERSION.SDK_INT >= 24) {
            this.mStorage = 0;
            this.mSharedPreferences = null;
        }
    }
    
    public void setStorageDeviceProtected() {
        if (Build$VERSION.SDK_INT >= 24) {
            this.mStorage = 1;
            this.mSharedPreferences = null;
        }
    }
    
    boolean shouldCommit() {
        return this.mNoCommit ^ true;
    }
    
    public void showDialog(final Preference preference) {
        if (this.mOnDisplayPreferenceDialogListener != null) {
            this.mOnDisplayPreferenceDialogListener.onDisplayPreferenceDialog(preference);
        }
    }
    
    public interface OnDisplayPreferenceDialogListener
    {
        void onDisplayPreferenceDialog(final Preference p0);
    }
    
    public interface OnNavigateToScreenListener
    {
        void onNavigateToScreen(final PreferenceScreen p0);
    }
    
    public interface OnPreferenceTreeClickListener
    {
        boolean onPreferenceTreeClick(final Preference p0);
    }
    
    public abstract static class PreferenceComparisonCallback
    {
        public abstract boolean arePreferenceContentsTheSame(final Preference p0, final Preference p1);
        
        public abstract boolean arePreferenceItemsTheSame(final Preference p0, final Preference p1);
    }
    
    public static class SimplePreferenceComparisonCallback extends PreferenceComparisonCallback
    {
        @Override
        public boolean arePreferenceContentsTheSame(final Preference preference, final Preference preference2) {
            if (preference.getClass() != preference2.getClass()) {
                return false;
            }
            if (preference == preference2 && preference.wasDetached()) {
                return false;
            }
            if (!TextUtils.equals(preference.getTitle(), preference2.getTitle())) {
                return false;
            }
            if (!TextUtils.equals(preference.getSummary(), preference2.getSummary())) {
                return false;
            }
            final Drawable icon = preference.getIcon();
            final Drawable icon2 = preference2.getIcon();
            return (icon == icon2 || (icon != null && icon.equals(icon2))) && preference.isEnabled() == preference2.isEnabled() && preference.isSelectable() == preference2.isSelectable() && (!(preference instanceof TwoStatePreference) || ((TwoStatePreference)preference).isChecked() == ((TwoStatePreference)preference2).isChecked()) && (!(preference instanceof DropDownPreference) || preference == preference2);
        }
        
        @Override
        public boolean arePreferenceItemsTheSame(final Preference preference, final Preference preference2) {
            return preference.getId() == preference2.getId();
        }
    }
}
