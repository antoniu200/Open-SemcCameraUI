// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.preference;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;

final class CollapsiblePreferenceGroupController
{
    private final Context mContext;
    private boolean mHasExpandablePreference;
    final PreferenceGroupAdapter mPreferenceGroupAdapter;
    
    CollapsiblePreferenceGroupController(final PreferenceGroup preferenceGroup, final PreferenceGroupAdapter mPreferenceGroupAdapter) {
        this.mHasExpandablePreference = false;
        this.mPreferenceGroupAdapter = mPreferenceGroupAdapter;
        this.mContext = preferenceGroup.getContext();
    }
    
    private ExpandButton createExpandButton(final PreferenceGroup preferenceGroup, final List<Preference> list) {
        final ExpandButton expandButton = new ExpandButton(this.mContext, list, preferenceGroup.getId());
        expandButton.setOnPreferenceClickListener((Preference.OnPreferenceClickListener)new Preference.OnPreferenceClickListener(this, preferenceGroup) {
            final CollapsiblePreferenceGroupController this$0;
            final PreferenceGroup val$group;
            
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                this.val$group.setInitialExpandedChildrenCount(Integer.MAX_VALUE);
                this.this$0.mPreferenceGroupAdapter.onPreferenceHierarchyChange(preference);
                final PreferenceGroup.OnExpandButtonClickListener onExpandButtonClickListener = this.val$group.getOnExpandButtonClickListener();
                if (onExpandButtonClickListener != null) {
                    onExpandButtonClickListener.onExpandButtonClick();
                }
                return true;
            }
        });
        return expandButton;
    }
    
    private List<Preference> createInnerVisiblePreferencesList(final PreferenceGroup preferenceGroup) {
        int i = 0;
        this.mHasExpandablePreference = false;
        final boolean b = preferenceGroup.getInitialExpandedChildrenCount() != Integer.MAX_VALUE;
        final ArrayList list = new ArrayList();
        final ArrayList list2 = new ArrayList();
        final int preferenceCount = preferenceGroup.getPreferenceCount();
        int n = 0;
        while (i < preferenceCount) {
            final Preference preference = preferenceGroup.getPreference(i);
            if (preference.isVisible()) {
                if (b && n >= preferenceGroup.getInitialExpandedChildrenCount()) {
                    list2.add(preference);
                }
                else {
                    list.add(preference);
                }
                if (!(preference instanceof PreferenceGroup)) {
                    ++n;
                }
                else {
                    final PreferenceGroup preferenceGroup2 = (PreferenceGroup)preference;
                    if (preferenceGroup2.isOnSameScreenAsChildren()) {
                        final List<Preference> innerVisiblePreferencesList = this.createInnerVisiblePreferencesList(preferenceGroup2);
                        if (b && this.mHasExpandablePreference) {
                            throw new IllegalArgumentException("Nested expand buttons are not supported!");
                        }
                        final Iterator<Preference> iterator = innerVisiblePreferencesList.iterator();
                        int n2 = n;
                        while (true) {
                            n = n2;
                            if (!iterator.hasNext()) {
                                break;
                            }
                            final Preference preference2 = iterator.next();
                            if (b && n2 >= preferenceGroup.getInitialExpandedChildrenCount()) {
                                list2.add(preference2);
                            }
                            else {
                                list.add(preference2);
                            }
                            ++n2;
                        }
                    }
                }
            }
            ++i;
        }
        if (b && n > preferenceGroup.getInitialExpandedChildrenCount()) {
            list.add(this.createExpandButton(preferenceGroup, list2));
        }
        this.mHasExpandablePreference |= b;
        return list;
    }
    
    public List<Preference> createVisiblePreferencesList(final PreferenceGroup preferenceGroup) {
        return this.createInnerVisiblePreferencesList(preferenceGroup);
    }
    
    public boolean onPreferenceVisibilityChange(final Preference preference) {
        if (!(preference instanceof PreferenceGroup) && !this.mHasExpandablePreference) {
            return false;
        }
        this.mPreferenceGroupAdapter.onPreferenceHierarchyChange(preference);
        return true;
    }
    
    static class ExpandButton extends Preference
    {
        private long mId;
        
        ExpandButton(final Context context, final List<Preference> summary, final long n) {
            super(context);
            this.initLayout();
            this.setSummary(summary);
            this.mId = n + 1000000L;
        }
        
        private void initLayout() {
            this.setLayoutResource(R.layout.expand_button);
            this.setIcon(R.drawable.ic_arrow_down_24dp);
            this.setTitle(R.string.expand_button_title);
            this.setOrder(999);
        }
        
        private void setSummary(final List<Preference> list) {
            final ArrayList list2 = new ArrayList();
            final Iterator<Preference> iterator = list.iterator();
            CharSequence string = null;
            while (iterator.hasNext()) {
                final Preference preference = iterator.next();
                final CharSequence title = preference.getTitle();
                final boolean b = preference instanceof PreferenceGroup;
                if (b && !TextUtils.isEmpty(title)) {
                    list2.add(preference);
                }
                if (list2.contains(preference.getParent())) {
                    if (!b) {
                        continue;
                    }
                    list2.add(preference);
                }
                else {
                    if (TextUtils.isEmpty(title)) {
                        continue;
                    }
                    if (string == null) {
                        string = title;
                    }
                    else {
                        string = this.getContext().getString(R.string.summary_collapsed_preference_list, new Object[] { string, title });
                    }
                }
            }
            this.setSummary(string);
        }
        
        public long getId() {
            return this.mId;
        }
        
        @Override
        public void onBindViewHolder(final PreferenceViewHolder preferenceViewHolder) {
            super.onBindViewHolder(preferenceViewHolder);
            preferenceViewHolder.setDividerAllowedAbove(false);
        }
    }
}
