package com.sonyericsson.android.camera.view.modeselector.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.sonyericsson.android.camera.view.modeselector.ImageLoader;
import com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class AbsAppsUiSelectorAdapter extends ArrayAdapter<AbsPanelView.PanelAttributes> {
    public static final int DRAWABLE_RESOURCE_LOADING = 2131230944;
    private LayoutInflater mInflater;
    private boolean mIsItemClickEnabled;
    private List<AbsPanelView.PanelAttributes> mList;
    private int mOrientation;

    protected abstract View onCreateItemView(int i, ViewGroup viewGroup);

    public AbsAppsUiSelectorAdapter(Context context, int i, List<AbsPanelView.PanelAttributes> list) {
        super(context, i, list);
        this.mOrientation = 0;
        this.mIsItemClickEnabled = true;
        this.mList = list;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        IconCacheManager.registerClient(this, context.getApplicationContext());
    }

    public void updateItems(List<AbsPanelView.PanelAttributes> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void setUiOrientation(int i) {
        this.mOrientation = i;
        notifyDataSetChanged();
    }

    public void setItemClickEnabled(boolean z) {
        this.mIsItemClickEnabled = z;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public AbsPanelView.PanelAttributes getItem(int i) {
        if (this.mList == null || this.mList.isEmpty()) {
            return null;
        }
        return this.mList.get(i);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public int getCount() {
        if (this.mList == null) {
            return 0;
        }
        return this.mList.size();
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int i) {
        return this.mIsItemClickEnabled;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = onCreateItemView(i, viewGroup);
        }
        return onPrepareItemView(i, (AbsPanelView) view, viewGroup);
    }

    protected View onPrepareItemView(int i, AbsPanelView absPanelView, ViewGroup viewGroup) {
        ImageLoader imageLoader;
        AbsPanelView.PanelAttributes item = getItem(i);
        absPanelView.setItem(item);
        absPanelView.setUiOrientation(getUiOrientation());
        absPanelView.setContentDescription("");
        String iconUri = item.getIconUri();
        if (!TextUtils.isEmpty(iconUri) && (imageLoader = getImageLoader()) != null) {
            imageLoader.requestLoad(iconUri, absPanelView);
        }
        return absPanelView;
    }

    public int getUiOrientation() {
        return this.mOrientation;
    }

    public void release() {
        IconCacheManager.unregisterClient(this, this.mList);
        this.mList = null;
    }

    public void releaseToUntil() {
        IconCacheManager.unregisterToUntilClient(this, this.mList);
        this.mList = null;
    }

    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }

    public ImageLoader getImageLoader() {
        return IconCacheManager.getImageLoader();
    }

    private static class IconCacheManager {
        private static ArrayList<AbsAppsUiSelectorAdapter> sCacheClientStack;
        private static ImageLoader sImageLoader;

        private IconCacheManager() {
        }

        public static void registerClient(AbsAppsUiSelectorAdapter absAppsUiSelectorAdapter, Context context) {
            if (sCacheClientStack == null) {
                sCacheClientStack = new ArrayList<>();
            }
            if (!sCacheClientStack.contains(absAppsUiSelectorAdapter)) {
                sCacheClientStack.add(absAppsUiSelectorAdapter);
            }
            if (sImageLoader == null) {
                sImageLoader = ImageLoader.getInstance(context);
                sImageLoader.setImageFadeIn(true);
                sImageLoader.setLoadingImage(2131230944);
            }
        }

        public static ImageLoader getImageLoader() {
            return sImageLoader;
        }

        public static void unregisterClient(AbsAppsUiSelectorAdapter absAppsUiSelectorAdapter, List<AbsPanelView.PanelAttributes> list) {
            if (sCacheClientStack == null) {
                return;
            }
            sCacheClientStack.remove(absAppsUiSelectorAdapter);
            if (sCacheClientStack.isEmpty()) {
                sCacheClientStack = null;
                releaseCache(list);
            }
        }

        public static void unregisterToUntilClient(AbsAppsUiSelectorAdapter absAppsUiSelectorAdapter, List<AbsPanelView.PanelAttributes> list) {
            if (sCacheClientStack == null) {
                return;
            }
            int iIndexOf = sCacheClientStack.indexOf(absAppsUiSelectorAdapter);
            for (int i = 0; i <= iIndexOf; i++) {
                sCacheClientStack.remove(0);
            }
            if (sCacheClientStack.isEmpty()) {
                sCacheClientStack = null;
                releaseCache(list);
            }
        }

        private static void releaseCache(List<AbsPanelView.PanelAttributes> list) {
            if (sImageLoader != null) {
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        String iconUri = list.get(i).getIconUri();
                        if (iconUri != null) {
                            sImageLoader.removeCache(iconUri);
                        }
                    }
                }
                sImageLoader.release();
                sImageLoader = null;
            }
        }
    }
}
