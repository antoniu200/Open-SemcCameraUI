// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector.view;

import java.util.ArrayList;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.View;
import com.sonyericsson.android.camera.view.modeselector.ImageLoader;
import android.content.Context;
import java.util.List;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

public abstract class AbsAppsUiSelectorAdapter extends ArrayAdapter<AbsPanelView.PanelAttributes>
{
    public static final int DRAWABLE_RESOURCE_LOADING = 2131230944;
    private LayoutInflater mInflater;
    private boolean mIsItemClickEnabled;
    private List<AbsPanelView.PanelAttributes> mList;
    private int mOrientation;
    
    public AbsAppsUiSelectorAdapter(final Context context, final int n, final List<AbsPanelView.PanelAttributes> mList) {
        super(context, n, (List)mList);
        this.mOrientation = 0;
        this.mIsItemClickEnabled = true;
        this.mList = mList;
        this.mInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        IconCacheManager.registerClient(this, context.getApplicationContext());
    }
    
    public int getCount() {
        int size;
        if (this.mList == null) {
            size = 0;
        }
        else {
            size = this.mList.size();
        }
        return size;
    }
    
    public ImageLoader getImageLoader() {
        return IconCacheManager.getImageLoader();
    }
    
    public AbsPanelView.PanelAttributes getItem(final int n) {
        AbsPanelView.PanelAttributes panelAttributes;
        if (this.mList != null && !this.mList.isEmpty()) {
            panelAttributes = this.mList.get(n);
        }
        else {
            panelAttributes = null;
        }
        return panelAttributes;
    }
    
    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }
    
    public int getUiOrientation() {
        return this.mOrientation;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        View onCreateItemView = view;
        if (view == null) {
            onCreateItemView = this.onCreateItemView(n, viewGroup);
        }
        return this.onPrepareItemView(n, (AbsPanelView)onCreateItemView, viewGroup);
    }
    
    public boolean isEnabled(final int n) {
        return this.mIsItemClickEnabled;
    }
    
    protected abstract View onCreateItemView(final int p0, final ViewGroup p1);
    
    protected View onPrepareItemView(final int n, final AbsPanelView absPanelView, final ViewGroup viewGroup) {
        final AbsPanelView.PanelAttributes item = this.getItem(n);
        absPanelView.setItem(item);
        absPanelView.setUiOrientation(this.getUiOrientation());
        absPanelView.setContentDescription((CharSequence)"");
        final String iconUri = item.getIconUri();
        if (!TextUtils.isEmpty((CharSequence)iconUri)) {
            final ImageLoader imageLoader = this.getImageLoader();
            if (imageLoader != null) {
                imageLoader.requestLoad(iconUri, absPanelView);
            }
        }
        return (View)absPanelView;
    }
    
    public void release() {
        IconCacheManager.unregisterClient(this, this.mList);
        this.mList = null;
    }
    
    public void releaseToUntil() {
        IconCacheManager.unregisterToUntilClient(this, this.mList);
        this.mList = null;
    }
    
    public void setItemClickEnabled(final boolean mIsItemClickEnabled) {
        this.mIsItemClickEnabled = mIsItemClickEnabled;
    }
    
    public void setUiOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
        this.notifyDataSetChanged();
    }
    
    public void updateItems(final List<AbsPanelView.PanelAttributes> mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }
    
    private static class IconCacheManager
    {
        private static ArrayList<AbsAppsUiSelectorAdapter> sCacheClientStack;
        private static ImageLoader sImageLoader;
        
        public static ImageLoader getImageLoader() {
            return IconCacheManager.sImageLoader;
        }
        
        public static void registerClient(final AbsAppsUiSelectorAdapter absAppsUiSelectorAdapter, final Context context) {
            if (IconCacheManager.sCacheClientStack == null) {
                IconCacheManager.sCacheClientStack = new ArrayList<AbsAppsUiSelectorAdapter>();
            }
            if (!IconCacheManager.sCacheClientStack.contains(absAppsUiSelectorAdapter)) {
                IconCacheManager.sCacheClientStack.add(absAppsUiSelectorAdapter);
            }
            if (IconCacheManager.sImageLoader == null) {
                (IconCacheManager.sImageLoader = ImageLoader.getInstance(context)).setImageFadeIn(true);
                IconCacheManager.sImageLoader.setLoadingImage(2131230944);
            }
        }
        
        private static void releaseCache(final List<AbsPanelView.PanelAttributes> list) {
            if (IconCacheManager.sImageLoader != null) {
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); ++i) {
                        final String iconUri = ((AbsPanelView.PanelAttributes)list.get(i)).getIconUri();
                        if (iconUri != null) {
                            IconCacheManager.sImageLoader.removeCache(iconUri);
                        }
                    }
                }
                IconCacheManager.sImageLoader.release();
                IconCacheManager.sImageLoader = null;
            }
        }
        
        public static void unregisterClient(final AbsAppsUiSelectorAdapter o, final List<AbsPanelView.PanelAttributes> list) {
            if (IconCacheManager.sCacheClientStack == null) {
                return;
            }
            IconCacheManager.sCacheClientStack.remove(o);
            if (IconCacheManager.sCacheClientStack.isEmpty()) {
                IconCacheManager.sCacheClientStack = null;
                releaseCache(list);
            }
        }
        
        public static void unregisterToUntilClient(final AbsAppsUiSelectorAdapter o, final List<AbsPanelView.PanelAttributes> list) {
            if (IconCacheManager.sCacheClientStack == null) {
                return;
            }
            for (int index = IconCacheManager.sCacheClientStack.indexOf(o), i = 0; i <= index; ++i) {
                IconCacheManager.sCacheClientStack.remove(0);
            }
            if (IconCacheManager.sCacheClientStack.isEmpty()) {
                IconCacheManager.sCacheClientStack = null;
                releaseCache(list);
            }
        }
    }
}
