// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.preference;

import android.support.v7.util.DiffUtil;
import java.util.Iterator;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.text.TextUtils;
import android.support.annotation.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.RecyclerView;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class PreferenceGroupAdapter extends Adapter<PreferenceViewHolder> implements OnPreferenceChangeInternalListener, PreferencePositionCallback
{
    private Handler mHandler;
    private PreferenceGroup mPreferenceGroup;
    private CollapsiblePreferenceGroupController mPreferenceGroupController;
    private List<PreferenceLayout> mPreferenceLayouts;
    private List<Preference> mPreferenceList;
    private List<Preference> mPreferenceListInternal;
    private Runnable mSyncRunnable;
    private PreferenceLayout mTempPreferenceLayout;
    
    public PreferenceGroupAdapter(final PreferenceGroup preferenceGroup) {
        this(preferenceGroup, new Handler());
    }
    
    private PreferenceGroupAdapter(final PreferenceGroup mPreferenceGroup, final Handler mHandler) {
        this.mTempPreferenceLayout = new PreferenceLayout();
        this.mSyncRunnable = new Runnable() {
            final PreferenceGroupAdapter this$0;
            
            @Override
            public void run() {
                this.this$0.syncMyPreferences();
            }
        };
        this.mPreferenceGroup = mPreferenceGroup;
        this.mHandler = mHandler;
        this.mPreferenceGroupController = new CollapsiblePreferenceGroupController(mPreferenceGroup, this);
        this.mPreferenceGroup.setOnPreferenceChangeInternalListener((Preference.OnPreferenceChangeInternalListener)this);
        this.mPreferenceList = new ArrayList<Preference>();
        this.mPreferenceListInternal = new ArrayList<Preference>();
        this.mPreferenceLayouts = new ArrayList<PreferenceLayout>();
        if (this.mPreferenceGroup instanceof PreferenceScreen) {
            ((RecyclerView.Adapter)this).setHasStableIds(((PreferenceScreen)this.mPreferenceGroup).shouldUseGeneratedIds());
        }
        else {
            ((RecyclerView.Adapter)this).setHasStableIds(true);
        }
        this.syncMyPreferences();
    }
    
    private void addPreferenceClassName(final Preference preference) {
        final PreferenceLayout preferenceLayout = this.createPreferenceLayout(preference, null);
        if (!this.mPreferenceLayouts.contains(preferenceLayout)) {
            this.mPreferenceLayouts.add(preferenceLayout);
        }
    }
    
    @VisibleForTesting
    static PreferenceGroupAdapter createInstanceWithCustomHandler(final PreferenceGroup preferenceGroup, final Handler handler) {
        return new PreferenceGroupAdapter(preferenceGroup, handler);
    }
    
    private PreferenceLayout createPreferenceLayout(final Preference preference, PreferenceLayout preferenceLayout) {
        if (preferenceLayout == null) {
            preferenceLayout = new PreferenceLayout();
        }
        preferenceLayout.mName = preference.getClass().getName();
        preferenceLayout.mResId = preference.getLayoutResource();
        preferenceLayout.mWidgetResId = preference.getWidgetLayoutResource();
        return preferenceLayout;
    }
    
    private void flattenPreferenceGroup(final List<Preference> list, final PreferenceGroup preferenceGroup) {
        preferenceGroup.sortPreferences();
        for (int preferenceCount = preferenceGroup.getPreferenceCount(), i = 0; i < preferenceCount; ++i) {
            final Preference preference = preferenceGroup.getPreference(i);
            list.add(preference);
            this.addPreferenceClassName(preference);
            if (preference instanceof PreferenceGroup) {
                final PreferenceGroup preferenceGroup2 = (PreferenceGroup)preference;
                if (preferenceGroup2.isOnSameScreenAsChildren()) {
                    this.flattenPreferenceGroup(list, preferenceGroup2);
                }
            }
            preference.setOnPreferenceChangeInternalListener((Preference.OnPreferenceChangeInternalListener)this);
        }
    }
    
    public Preference getItem(final int n) {
        if (n >= 0 && n < this.getItemCount()) {
            return this.mPreferenceList.get(n);
        }
        return null;
    }
    
    @Override
    public int getItemCount() {
        return this.mPreferenceList.size();
    }
    
    @Override
    public long getItemId(final int n) {
        if (!((RecyclerView.Adapter)this).hasStableIds()) {
            return -1L;
        }
        return this.getItem(n).getId();
    }
    
    @Override
    public int getItemViewType(int n) {
        this.mTempPreferenceLayout = this.createPreferenceLayout(this.getItem(n), this.mTempPreferenceLayout);
        n = this.mPreferenceLayouts.indexOf(this.mTempPreferenceLayout);
        if (n != -1) {
            return n;
        }
        n = this.mPreferenceLayouts.size();
        this.mPreferenceLayouts.add(new PreferenceLayout(this.mTempPreferenceLayout));
        return n;
    }
    
    @Override
    public int getPreferenceAdapterPosition(final Preference obj) {
        for (int size = this.mPreferenceList.size(), i = 0; i < size; ++i) {
            final Preference preference = this.mPreferenceList.get(i);
            if (preference != null && preference.equals(obj)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int getPreferenceAdapterPosition(final String s) {
        for (int size = this.mPreferenceList.size(), i = 0; i < size; ++i) {
            if (TextUtils.equals((CharSequence)s, (CharSequence)this.mPreferenceList.get(i).getKey())) {
                return i;
            }
        }
        return -1;
    }
    
    public void onBindViewHolder(final PreferenceViewHolder preferenceViewHolder, final int n) {
        this.getItem(n).onBindViewHolder(preferenceViewHolder);
    }
    
    public PreferenceViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        final PreferenceLayout preferenceLayout = this.mPreferenceLayouts.get(n);
        final LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        final TypedArray obtainStyledAttributes = viewGroup.getContext().obtainStyledAttributes((AttributeSet)null, R.styleable.BackgroundStyle);
        Drawable drawable;
        if ((drawable = obtainStyledAttributes.getDrawable(R.styleable.BackgroundStyle_android_selectableItemBackground)) == null) {
            drawable = ContextCompat.getDrawable(viewGroup.getContext(), 17301602);
        }
        obtainStyledAttributes.recycle();
        final View inflate = from.inflate(preferenceLayout.mResId, viewGroup, false);
        if (inflate.getBackground() == null) {
            ViewCompat.setBackground(inflate, drawable);
        }
        final ViewGroup viewGroup2 = (ViewGroup)inflate.findViewById(16908312);
        if (viewGroup2 != null) {
            if (preferenceLayout.mWidgetResId != 0) {
                from.inflate(preferenceLayout.mWidgetResId, viewGroup2);
            }
            else {
                viewGroup2.setVisibility(8);
            }
        }
        return new PreferenceViewHolder(inflate);
    }
    
    @Override
    public void onPreferenceChange(final Preference preference) {
        final int index = this.mPreferenceList.indexOf(preference);
        if (index != -1) {
            ((RecyclerView.Adapter)this).notifyItemChanged(index, preference);
        }
    }
    
    @Override
    public void onPreferenceHierarchyChange(final Preference preference) {
        this.mHandler.removeCallbacks(this.mSyncRunnable);
        this.mHandler.post(this.mSyncRunnable);
    }
    
    @Override
    public void onPreferenceVisibilityChange(final Preference preference) {
        if (!this.mPreferenceListInternal.contains(preference)) {
            return;
        }
        if (this.mPreferenceGroupController.onPreferenceVisibilityChange(preference)) {
            return;
        }
        if (preference.isVisible()) {
            int n = -1;
            for (final Preference obj : this.mPreferenceListInternal) {
                if (preference.equals(obj)) {
                    break;
                }
                if (!obj.isVisible()) {
                    continue;
                }
                ++n;
            }
            final List<Preference> mPreferenceList = this.mPreferenceList;
            ++n;
            mPreferenceList.add(n, preference);
            ((RecyclerView.Adapter)this).notifyItemInserted(n);
        }
        else {
            int size;
            int n2;
            for (size = this.mPreferenceList.size(), n2 = 0; n2 < size && !preference.equals(this.mPreferenceList.get(n2)); ++n2) {
                if (n2 == size - 1) {
                    return;
                }
            }
            this.mPreferenceList.remove(n2);
            ((RecyclerView.Adapter)this).notifyItemRemoved(n2);
        }
    }
    
    void syncMyPreferences() {
        final Iterator<Preference> iterator = this.mPreferenceListInternal.iterator();
        while (iterator.hasNext()) {
            iterator.next().setOnPreferenceChangeInternalListener(null);
        }
        final ArrayList mPreferenceListInternal = new ArrayList(this.mPreferenceListInternal.size());
        this.flattenPreferenceGroup(mPreferenceListInternal, this.mPreferenceGroup);
        final List<Preference> visiblePreferencesList = this.mPreferenceGroupController.createVisiblePreferencesList(this.mPreferenceGroup);
        final List<Preference> mPreferenceList = this.mPreferenceList;
        this.mPreferenceList = visiblePreferencesList;
        this.mPreferenceListInternal = mPreferenceListInternal;
        final PreferenceManager preferenceManager = this.mPreferenceGroup.getPreferenceManager();
        if (preferenceManager != null && preferenceManager.getPreferenceComparisonCallback() != null) {
            DiffUtil.calculateDiff((DiffUtil.Callback)new DiffUtil.Callback(this, mPreferenceList, visiblePreferencesList, preferenceManager.getPreferenceComparisonCallback()) {
                final PreferenceGroupAdapter this$0;
                final PreferenceManager.PreferenceComparisonCallback val$comparisonCallback;
                final List val$oldVisibleList;
                final List val$visiblePreferenceList;
                
                @Override
                public boolean areContentsTheSame(final int n, final int n2) {
                    return this.val$comparisonCallback.arePreferenceContentsTheSame(this.val$oldVisibleList.get(n), this.val$visiblePreferenceList.get(n2));
                }
                
                @Override
                public boolean areItemsTheSame(final int n, final int n2) {
                    return this.val$comparisonCallback.arePreferenceItemsTheSame(this.val$oldVisibleList.get(n), this.val$visiblePreferenceList.get(n2));
                }
                
                @Override
                public int getNewListSize() {
                    return this.val$visiblePreferenceList.size();
                }
                
                @Override
                public int getOldListSize() {
                    return this.val$oldVisibleList.size();
                }
            }).dispatchUpdatesTo(this);
        }
        else {
            ((RecyclerView.Adapter)this).notifyDataSetChanged();
        }
        final Iterator iterator2 = mPreferenceListInternal.iterator();
        while (iterator2.hasNext()) {
            ((Preference)iterator2.next()).clearWasDetached();
        }
    }
    
    private static class PreferenceLayout
    {
        String mName;
        int mResId;
        int mWidgetResId;
        
        PreferenceLayout() {
        }
        
        PreferenceLayout(final PreferenceLayout preferenceLayout) {
            this.mResId = preferenceLayout.mResId;
            this.mWidgetResId = preferenceLayout.mWidgetResId;
            this.mName = preferenceLayout.mName;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof PreferenceLayout;
            final boolean b2 = false;
            if (!b) {
                return false;
            }
            final PreferenceLayout preferenceLayout = (PreferenceLayout)o;
            boolean b3 = b2;
            if (this.mResId == preferenceLayout.mResId) {
                b3 = b2;
                if (this.mWidgetResId == preferenceLayout.mWidgetResId) {
                    b3 = b2;
                    if (TextUtils.equals((CharSequence)this.mName, (CharSequence)preferenceLayout.mName)) {
                        b3 = true;
                    }
                }
            }
            return b3;
        }
        
        @Override
        public int hashCode() {
            return 31 * ((527 + this.mResId) * 31 + this.mWidgetResId) + this.mName.hashCode();
        }
    }
}
