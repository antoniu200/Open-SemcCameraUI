// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v14.preference;

import android.support.annotation.NonNull;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.EditTextPreference;
import android.graphics.drawable.Drawable;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.support.v7.preference.PreferenceRecyclerViewAccessibilityDelegate;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.preference.PreferenceGroupAdapter;
import android.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.annotation.XmlRes;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.Preference;
import android.view.View;
import android.os.Message;
import android.support.v7.preference.R;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.os.Handler;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceManager;
import android.app.Fragment;

public abstract class PreferenceFragment extends Fragment implements OnPreferenceTreeClickListener, OnDisplayPreferenceDialogListener, OnNavigateToScreenListener, TargetFragment
{
    public static final String ARG_PREFERENCE_ROOT = "android.support.v7.preference.PreferenceFragmentCompat.PREFERENCE_ROOT";
    private static final String DIALOG_FRAGMENT_TAG = "android.support.v14.preference.PreferenceFragment.DIALOG";
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final String PREFERENCES_TAG = "android:preferences";
    private final DividerDecoration mDividerDecoration;
    private final Handler mHandler;
    private boolean mHavePrefs;
    private boolean mInitDone;
    private int mLayoutResId;
    RecyclerView mList;
    private PreferenceManager mPreferenceManager;
    private final Runnable mRequestFocus;
    private Runnable mSelectPreferenceRunnable;
    private Context mStyledContext;
    
    public PreferenceFragment() {
        this.mLayoutResId = R.layout.preference_list_fragment;
        this.mDividerDecoration = new DividerDecoration();
        this.mHandler = new Handler() {
            final PreferenceFragment this$0;
            
            public void handleMessage(final Message message) {
                if (message.what == 1) {
                    this.this$0.bindPreferences();
                }
            }
        };
        this.mRequestFocus = new Runnable() {
            final PreferenceFragment this$0;
            
            @Override
            public void run() {
                this.this$0.mList.focusableViewAvailable((View)this.this$0.mList);
            }
        };
    }
    
    private void postBindPreferences() {
        if (this.mHandler.hasMessages(1)) {
            return;
        }
        this.mHandler.obtainMessage(1).sendToTarget();
    }
    
    private void requirePreferenceManager() {
        if (this.mPreferenceManager == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }
    
    private void scrollToPreferenceInternal(final Preference preference, final String s) {
        final Runnable mSelectPreferenceRunnable = new Runnable(this, preference, s) {
            final PreferenceFragment this$0;
            final String val$key;
            final Preference val$preference;
            
            @Override
            public void run() {
                final RecyclerView.Adapter adapter = this.this$0.mList.getAdapter();
                if (adapter instanceof PreferenceGroup.PreferencePositionCallback) {
                    int n;
                    if (this.val$preference != null) {
                        n = ((PreferenceGroup.PreferencePositionCallback)adapter).getPreferenceAdapterPosition(this.val$preference);
                    }
                    else {
                        n = ((PreferenceGroup.PreferencePositionCallback)adapter).getPreferenceAdapterPosition(this.val$key);
                    }
                    if (n != -1) {
                        this.this$0.mList.scrollToPosition(n);
                    }
                    else {
                        adapter.registerAdapterDataObserver(new ScrollToPreferenceObserver(adapter, this.this$0.mList, this.val$preference, this.val$key));
                    }
                    return;
                }
                if (adapter != null) {
                    throw new IllegalStateException("Adapter must implement PreferencePositionCallback");
                }
            }
        };
        if (this.mList == null) {
            this.mSelectPreferenceRunnable = mSelectPreferenceRunnable;
        }
        else {
            mSelectPreferenceRunnable.run();
        }
    }
    
    private void unbindPreferences() {
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.onDetached();
        }
        this.onUnbindPreferences();
    }
    
    public void addPreferencesFromResource(@XmlRes final int n) {
        this.requirePreferenceManager();
        this.setPreferenceScreen(this.mPreferenceManager.inflateFromResource(this.mStyledContext, n, this.getPreferenceScreen()));
    }
    
    void bindPreferences() {
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        if (preferenceScreen != null) {
            this.getListView().setAdapter(this.onCreateAdapter(preferenceScreen));
            preferenceScreen.onAttached();
        }
        this.onBindPreferences();
    }
    
    public Preference findPreference(final CharSequence charSequence) {
        if (this.mPreferenceManager == null) {
            return null;
        }
        return this.mPreferenceManager.findPreference(charSequence);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public Fragment getCallbackFragment() {
        return null;
    }
    
    public final RecyclerView getListView() {
        return this.mList;
    }
    
    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }
    
    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceManager.getPreferenceScreen();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected void onBindPreferences() {
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final TypedValue typedValue = new TypedValue();
        this.getActivity().getTheme().resolveAttribute(R.attr.preferenceTheme, typedValue, true);
        int n;
        if ((n = typedValue.resourceId) == 0) {
            n = R.style.PreferenceThemeOverlay;
        }
        this.mStyledContext = (Context)new ContextThemeWrapper((Context)this.getActivity(), n);
        (this.mPreferenceManager = new PreferenceManager(this.mStyledContext)).setOnNavigateToScreenListener((PreferenceManager.OnNavigateToScreenListener)this);
        String string;
        if (this.getArguments() != null) {
            string = this.getArguments().getString("android.support.v7.preference.PreferenceFragmentCompat.PREFERENCE_ROOT");
        }
        else {
            string = null;
        }
        this.onCreatePreferences(bundle, string);
    }
    
    protected RecyclerView.Adapter onCreateAdapter(final PreferenceScreen preferenceScreen) {
        return new PreferenceGroupAdapter(preferenceScreen);
    }
    
    public RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager((Context)this.getActivity());
    }
    
    public abstract void onCreatePreferences(final Bundle p0, final String p1);
    
    public RecyclerView onCreateRecyclerView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        if (this.mStyledContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            final RecyclerView recyclerView = (RecyclerView)viewGroup.findViewById(R.id.recycler_view);
            if (recyclerView != null) {
                return recyclerView;
            }
        }
        final RecyclerView recyclerView2 = (RecyclerView)layoutInflater.inflate(R.layout.preference_recyclerview, viewGroup, false);
        recyclerView2.setLayoutManager(this.onCreateLayoutManager());
        recyclerView2.setAccessibilityDelegateCompat(new PreferenceRecyclerViewAccessibilityDelegate(recyclerView2));
        return recyclerView2;
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, ViewGroup viewGroup, final Bundle bundle) {
        final TypedArray obtainStyledAttributes = this.mStyledContext.obtainStyledAttributes((AttributeSet)null, R.styleable.PreferenceFragment, TypedArrayUtils.getAttr(this.mStyledContext, R.attr.preferenceFragmentStyle, 16844038), 0);
        this.mLayoutResId = obtainStyledAttributes.getResourceId(R.styleable.PreferenceFragment_android_layout, this.mLayoutResId);
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.PreferenceFragment_android_divider);
        final int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.PreferenceFragment_android_dividerHeight, -1);
        final boolean boolean1 = obtainStyledAttributes.getBoolean(R.styleable.PreferenceFragment_allowDividerAfterLastItem, true);
        obtainStyledAttributes.recycle();
        final LayoutInflater cloneInContext = layoutInflater.cloneInContext(this.mStyledContext);
        final View inflate = cloneInContext.inflate(this.mLayoutResId, viewGroup, false);
        final View viewById = inflate.findViewById(16908351);
        if (!(viewById instanceof ViewGroup)) {
            throw new RuntimeException("Content has view with id attribute 'android.R.id.list_container' that is not a ViewGroup class");
        }
        viewGroup = (ViewGroup)viewById;
        final RecyclerView onCreateRecyclerView = this.onCreateRecyclerView(cloneInContext, viewGroup, bundle);
        if (onCreateRecyclerView == null) {
            throw new RuntimeException("Could not create RecyclerView");
        }
        (this.mList = onCreateRecyclerView).addItemDecoration((RecyclerView.ItemDecoration)this.mDividerDecoration);
        this.setDivider(drawable);
        if (dimensionPixelSize != -1) {
            this.setDividerHeight(dimensionPixelSize);
        }
        this.mDividerDecoration.setAllowDividerAfterLastItem(boolean1);
        if (this.mList.getParent() == null) {
            viewGroup.addView((View)this.mList);
        }
        this.mHandler.post(this.mRequestFocus);
        return inflate;
    }
    
    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mHandler.removeMessages(1);
        if (this.mHavePrefs) {
            this.unbindPreferences();
        }
        this.mList = null;
        super.onDestroyView();
    }
    
    public void onDisplayPreferenceDialog(final Preference preference) {
        boolean onPreferenceDisplayDialog;
        final boolean b = onPreferenceDisplayDialog = (this.getCallbackFragment() instanceof OnPreferenceDisplayDialogCallback && ((OnPreferenceDisplayDialogCallback)this.getCallbackFragment()).onPreferenceDisplayDialog(this, preference));
        if (!b) {
            onPreferenceDisplayDialog = b;
            if (this.getActivity() instanceof OnPreferenceDisplayDialogCallback) {
                onPreferenceDisplayDialog = ((OnPreferenceDisplayDialogCallback)this.getActivity()).onPreferenceDisplayDialog(this, preference);
            }
        }
        if (onPreferenceDisplayDialog) {
            return;
        }
        if (this.getFragmentManager().findFragmentByTag("android.support.v14.preference.PreferenceFragment.DIALOG") != null) {
            return;
        }
        PreferenceDialogFragment preferenceDialogFragment;
        if (preference instanceof EditTextPreference) {
            preferenceDialogFragment = EditTextPreferenceDialogFragment.newInstance(preference.getKey());
        }
        else if (preference instanceof ListPreference) {
            preferenceDialogFragment = ListPreferenceDialogFragment.newInstance(preference.getKey());
        }
        else {
            if (!(preference instanceof MultiSelectListPreference)) {
                throw new IllegalArgumentException("Tried to display dialog for unknown preference type. Did you forget to override onDisplayPreferenceDialog()?");
            }
            preferenceDialogFragment = MultiSelectListPreferenceDialogFragment.newInstance(preference.getKey());
        }
        preferenceDialogFragment.setTargetFragment((Fragment)this, 0);
        preferenceDialogFragment.show(this.getFragmentManager(), "android.support.v14.preference.PreferenceFragment.DIALOG");
    }
    
    public void onNavigateToScreen(final PreferenceScreen preferenceScreen) {
        if ((!(this.getCallbackFragment() instanceof OnPreferenceStartScreenCallback) || !((OnPreferenceStartScreenCallback)this.getCallbackFragment()).onPreferenceStartScreen(this, preferenceScreen)) && this.getActivity() instanceof OnPreferenceStartScreenCallback) {
            ((OnPreferenceStartScreenCallback)this.getActivity()).onPreferenceStartScreen(this, preferenceScreen);
        }
    }
    
    public boolean onPreferenceTreeClick(final Preference preference) {
        final String fragment = preference.getFragment();
        int onPreferenceStartFragment = 0;
        if (fragment != null) {
            if (this.getCallbackFragment() instanceof OnPreferenceStartFragmentCallback) {
                onPreferenceStartFragment = (((OnPreferenceStartFragmentCallback)this.getCallbackFragment()).onPreferenceStartFragment(this, preference) ? 1 : 0);
            }
            boolean onPreferenceStartFragment2 = onPreferenceStartFragment != 0;
            if (onPreferenceStartFragment == 0) {
                onPreferenceStartFragment2 = (onPreferenceStartFragment != 0);
                if (this.getActivity() instanceof OnPreferenceStartFragmentCallback) {
                    onPreferenceStartFragment2 = ((OnPreferenceStartFragmentCallback)this.getActivity()).onPreferenceStartFragment(this, preference);
                }
            }
            return onPreferenceStartFragment2;
        }
        return false;
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        if (preferenceScreen != null) {
            final Bundle bundle2 = new Bundle();
            preferenceScreen.saveHierarchyState(bundle2);
            bundle.putBundle("android:preferences", bundle2);
        }
    }
    
    public void onStart() {
        super.onStart();
        this.mPreferenceManager.setOnPreferenceTreeClickListener((PreferenceManager.OnPreferenceTreeClickListener)this);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener((PreferenceManager.OnDisplayPreferenceDialogListener)this);
    }
    
    public void onStop() {
        super.onStop();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(null);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(null);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected void onUnbindPreferences() {
    }
    
    public void onViewCreated(final View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle != null) {
            bundle = bundle.getBundle("android:preferences");
            if (bundle != null) {
                final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(bundle);
                }
            }
        }
        if (this.mHavePrefs) {
            this.bindPreferences();
            if (this.mSelectPreferenceRunnable != null) {
                this.mSelectPreferenceRunnable.run();
                this.mSelectPreferenceRunnable = null;
            }
        }
        this.mInitDone = true;
    }
    
    public void scrollToPreference(final Preference preference) {
        this.scrollToPreferenceInternal(preference, null);
    }
    
    public void scrollToPreference(final String s) {
        this.scrollToPreferenceInternal(null, s);
    }
    
    public void setDivider(final Drawable divider) {
        this.mDividerDecoration.setDivider(divider);
    }
    
    public void setDividerHeight(final int dividerHeight) {
        this.mDividerDecoration.setDividerHeight(dividerHeight);
    }
    
    public void setPreferenceScreen(final PreferenceScreen preferences) {
        if (this.mPreferenceManager.setPreferences(preferences) && preferences != null) {
            this.onUnbindPreferences();
            this.mHavePrefs = true;
            if (this.mInitDone) {
                this.postBindPreferences();
            }
        }
    }
    
    public void setPreferencesFromResource(@XmlRes final int n, @Nullable final String str) {
        this.requirePreferenceManager();
        Preference preference = this.mPreferenceManager.inflateFromResource(this.mStyledContext, n, null);
        if (str != null && !((preference = ((PreferenceGroup)preference).findPreference(str)) instanceof PreferenceScreen)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Preference object with key ");
            sb.append(str);
            sb.append(" is not a PreferenceScreen");
            throw new IllegalArgumentException(sb.toString());
        }
        this.setPreferenceScreen((PreferenceScreen)preference);
    }
    
    private class DividerDecoration extends ItemDecoration
    {
        private boolean mAllowDividerAfterLastItem;
        private Drawable mDivider;
        private int mDividerHeight;
        final PreferenceFragment this$0;
        
        DividerDecoration(final PreferenceFragment this$0) {
            this.this$0 = this$0;
            this.mAllowDividerAfterLastItem = true;
        }
        
        private boolean shouldDrawDividerBelow(final View view, final RecyclerView recyclerView) {
            final RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(view);
            if (!(childViewHolder instanceof PreferenceViewHolder) || !((PreferenceViewHolder)childViewHolder).isDividerAllowedBelow()) {
                return false;
            }
            boolean mAllowDividerAfterLastItem = this.mAllowDividerAfterLastItem;
            final int indexOfChild = recyclerView.indexOfChild(view);
            if (indexOfChild < recyclerView.getChildCount() - 1) {
                final RecyclerView.ViewHolder childViewHolder2 = recyclerView.getChildViewHolder(recyclerView.getChildAt(indexOfChild + 1));
                mAllowDividerAfterLastItem = (childViewHolder2 instanceof PreferenceViewHolder && ((PreferenceViewHolder)childViewHolder2).isDividerAllowedAbove());
            }
            return mAllowDividerAfterLastItem;
        }
        
        @Override
        public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
            if (this.shouldDrawDividerBelow(view, recyclerView)) {
                rect.bottom = this.mDividerHeight;
            }
        }
        
        @Override
        public void onDrawOver(final Canvas canvas, final RecyclerView recyclerView, final State state) {
            if (this.mDivider == null) {
                return;
            }
            final int childCount = recyclerView.getChildCount();
            final int width = recyclerView.getWidth();
            for (int i = 0; i < childCount; ++i) {
                final View child = recyclerView.getChildAt(i);
                if (this.shouldDrawDividerBelow(child, recyclerView)) {
                    final int n = (int)child.getY() + child.getHeight();
                    this.mDivider.setBounds(0, n, width, this.mDividerHeight + n);
                    this.mDivider.draw(canvas);
                }
            }
        }
        
        public void setAllowDividerAfterLastItem(final boolean mAllowDividerAfterLastItem) {
            this.mAllowDividerAfterLastItem = mAllowDividerAfterLastItem;
        }
        
        public void setDivider(final Drawable mDivider) {
            if (mDivider != null) {
                this.mDividerHeight = mDivider.getIntrinsicHeight();
            }
            else {
                this.mDividerHeight = 0;
            }
            this.mDivider = mDivider;
            this.this$0.mList.invalidateItemDecorations();
        }
        
        public void setDividerHeight(final int mDividerHeight) {
            this.mDividerHeight = mDividerHeight;
            this.this$0.mList.invalidateItemDecorations();
        }
    }
    
    public interface OnPreferenceDisplayDialogCallback
    {
        boolean onPreferenceDisplayDialog(@NonNull final PreferenceFragment p0, final Preference p1);
    }
    
    public interface OnPreferenceStartFragmentCallback
    {
        boolean onPreferenceStartFragment(final PreferenceFragment p0, final Preference p1);
    }
    
    public interface OnPreferenceStartScreenCallback
    {
        boolean onPreferenceStartScreen(final PreferenceFragment p0, final PreferenceScreen p1);
    }
    
    private static class ScrollToPreferenceObserver extends AdapterDataObserver
    {
        private final Adapter mAdapter;
        private final String mKey;
        private final RecyclerView mList;
        private final Preference mPreference;
        
        ScrollToPreferenceObserver(final Adapter mAdapter, final RecyclerView mList, final Preference mPreference, final String mKey) {
            this.mAdapter = mAdapter;
            this.mList = mList;
            this.mPreference = mPreference;
            this.mKey = mKey;
        }
        
        private void scrollToPreference() {
            this.mAdapter.unregisterAdapterDataObserver(this);
            int n;
            if (this.mPreference != null) {
                n = ((PreferenceGroup.PreferencePositionCallback)this.mAdapter).getPreferenceAdapterPosition(this.mPreference);
            }
            else {
                n = ((PreferenceGroup.PreferencePositionCallback)this.mAdapter).getPreferenceAdapterPosition(this.mKey);
            }
            if (n != -1) {
                this.mList.scrollToPosition(n);
            }
        }
        
        @Override
        public void onChanged() {
            this.scrollToPreference();
        }
        
        @Override
        public void onItemRangeChanged(final int n, final int n2) {
            this.scrollToPreference();
        }
        
        @Override
        public void onItemRangeChanged(final int n, final int n2, final Object o) {
            this.scrollToPreference();
        }
        
        @Override
        public void onItemRangeInserted(final int n, final int n2) {
            this.scrollToPreference();
        }
        
        @Override
        public void onItemRangeMoved(final int n, final int n2, final int n3) {
            this.scrollToPreference();
        }
        
        @Override
        public void onItemRangeRemoved(final int n, final int n2) {
            this.scrollToPreference();
        }
    }
}
