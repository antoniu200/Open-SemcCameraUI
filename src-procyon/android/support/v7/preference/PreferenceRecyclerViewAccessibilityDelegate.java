// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.preference;

import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class PreferenceRecyclerViewAccessibilityDelegate extends RecyclerViewAccessibilityDelegate
{
    final AccessibilityDelegateCompat mDefaultItemDelegate;
    final AccessibilityDelegateCompat mItemDelegate;
    final RecyclerView mRecyclerView;
    
    public PreferenceRecyclerViewAccessibilityDelegate(final RecyclerView mRecyclerView) {
        super(mRecyclerView);
        this.mDefaultItemDelegate = super.getItemDelegate();
        this.mItemDelegate = new AccessibilityDelegateCompat() {
            final PreferenceRecyclerViewAccessibilityDelegate this$0;
            
            @Override
            public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                this.this$0.mDefaultItemDelegate.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                final int childAdapterPosition = this.this$0.mRecyclerView.getChildAdapterPosition(view);
                final RecyclerView.Adapter adapter = this.this$0.mRecyclerView.getAdapter();
                if (!(adapter instanceof PreferenceGroupAdapter)) {
                    return;
                }
                final Preference item = ((PreferenceGroupAdapter)adapter).getItem(childAdapterPosition);
                if (item == null) {
                    return;
                }
                item.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat);
            }
            
            @Override
            public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
                return this.this$0.mDefaultItemDelegate.performAccessibilityAction(view, n, bundle);
            }
        };
        this.mRecyclerView = mRecyclerView;
    }
    
    @Override
    public AccessibilityDelegateCompat getItemDelegate() {
        return this.mItemDelegate;
    }
}
