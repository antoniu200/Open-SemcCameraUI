// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview;

import com.sonyericsson.cameracommon.storage.StorageUtil;
import android.view.animation.Animation$AnimationListener;
import android.view.View$OnClickListener;
import java.util.ArrayList;
import java.util.Iterator;
import android.graphics.Bitmap;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.net.Uri;
import android.view.animation.Animation;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.View;
import android.content.Context;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.util.Collections;
import java.util.LinkedList;
import android.os.Looper;
import java.util.List;
import com.sonyericsson.cameracommon.utility.IncrementalId;
import android.os.Handler;
import android.app.Activity;
import com.sonyericsson.cameracommon.storage.Storage;

public class ContentsViewController implements StorageStateListener, ContentCreationCallback
{
    public static final int MAX_CONTENT_NUMBER = 1;
    public static final String TAG = "ContentsViewController";
    private Activity mActivity;
    private ClickListener mClickListener;
    private OnClickThumbnailProgressListener mClickThumbnailProgressListener;
    private boolean mClickable;
    private final ContentsContainer mContentContainer;
    private ContentLoader mContentLoader;
    private Handler mHandler;
    private boolean mIsCoreCamera;
    private int mOrientation;
    private final IncrementalId mRequestIdGenerator;
    private SecurityLevel mSecurityLevel;
    private Storage mStorage;
    private ContentPallet.ThumbnailStateListener mThumbnailStateListener;
    private final List<UpdateContentTask> mUpdateContentTaskList;
    
    public ContentsViewController(final Activity mActivity, final Storage mStorage, final SecurityLevel mSecurityLevel, final ContentPallet.ThumbnailStateListener mThumbnailStateListener) {
        this.mClickable = true;
        this.mClickListener = null;
        this.mThumbnailStateListener = null;
        this.mClickThumbnailProgressListener = null;
        this.mIsCoreCamera = false;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mUpdateContentTaskList = Collections.synchronizedList(new LinkedList<UpdateContentTask>());
        this.mActivity = mActivity;
        this.mContentLoader = new ContentLoader(mStorage, mSecurityLevel, (ContentLoader.ContentCreationCallback)this);
        this.mContentContainer = (ContentsContainer)mActivity.findViewById(2131296370);
        this.mRequestIdGenerator = new IncrementalId();
        this.mStorage = mStorage;
        this.mThumbnailStateListener = mThumbnailStateListener;
        this.mIsCoreCamera = CommonUtility.isCoreCameraApp((Context)mActivity);
        this.mSecurityLevel = mSecurityLevel;
    }
    
    private void removeExcessiveView(final int n) {
        for (int i = 0; i < this.mContentContainer.getChildCount(); ++i) {
            final ContentPallet contentPallet = (ContentPallet)this.mContentContainer.getChildAt(i);
            if (contentPallet.hasContent() && n != contentPallet.getRequestId()) {
                this.mContentContainer.removeView((View)contentPallet);
            }
        }
    }
    
    private ContentPallet searchPallet(final int i) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("searchPallet() has been called. requestId = ");
            sb.append(i);
            sb.append(", child = ");
            sb.append(this.mContentContainer.getChildCount());
            CamLog.d(sb.toString());
        }
        for (int j = 0; j < this.mContentContainer.getChildCount(); ++j) {
            final ContentPallet contentPallet = (ContentPallet)this.mContentContainer.getChildAt(j);
            if (i == contentPallet.getRequestId()) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("searchPallet() pallet find. index = ");
                    sb2.append(j);
                    CamLog.d(sb2.toString());
                }
                return contentPallet;
            }
        }
        return null;
    }
    
    private void startAnimationInner(final Animation animation) {
        if (animation != null) {
            animation.reset();
            this.mContentContainer.startAnimation(animation);
            if (CamLog.VERBOSE) {
                CamLog.d("Animation started.");
            }
        }
    }
    
    public void addContent(final int i, final Uri obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("addContent() has been called. requestId = ");
            sb.append(i);
            sb.append(", uri = ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (this.mContentLoader == null) {
            return;
        }
        if (this.searchPallet(i) == null && this.mSecurityLevel != SecurityLevel.NEWLY_ADDED_CONTENT_ONLY) {
            if (i != -1 && this.mContentContainer.getChildCount() == 0) {
                this.reload();
            }
        }
        else {
            this.mContentLoader.request(i, obj);
        }
    }
    
    public void clearContents() {
        this.mContentLoader.pause();
        this.mContentContainer.removeAllViews();
    }
    
    public int createContentFrame() {
        if (CamLog.VERBOSE) {
            CamLog.d("createContentFrame() has been called.");
        }
        final int emptyContentFrame = this.createEmptyContentFrame();
        this.showProgress(emptyContentFrame);
        return emptyContentFrame;
    }
    
    public int createEmptyContentFrame() {
        if (CamLog.VERBOSE) {
            CamLog.d("createEmptyContentFrame() has been called.");
        }
        if (this.mActivity == null) {
            CamLog.w("Activity has already been released at createEmptyContentFrame.");
            return -1;
        }
        final LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
        if (layoutInflater == null) {
            CamLog.w("could not get inflater.");
            return -1;
        }
        final int next = this.mRequestIdGenerator.getNext();
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("createEmptyContentFrame : create pallet. id = ");
            sb.append(next);
            CamLog.d(sb.toString());
        }
        ContentPallet contentPallet;
        if (this.mIsCoreCamera) {
            contentPallet = (ContentPallet)layoutInflater.inflate(2131492913, (ViewGroup)null);
        }
        else {
            contentPallet = (ContentPallet)layoutInflater.inflate(2131492912, (ViewGroup)null);
        }
        contentPallet.initialize(next, this.mThumbnailStateListener);
        if (!this.mClickable) {
            contentPallet.disableClick();
        }
        this.mContentContainer.addView((View)contentPallet);
        this.mContentContainer.setSensorOrientation(this.mOrientation);
        return next;
    }
    
    public int createProvisionalContentFrame() {
        final int emptyContentFrame = this.createEmptyContentFrame();
        final ContentPallet searchPallet = this.searchPallet(emptyContentFrame);
        if (searchPallet != null) {
            searchPallet.setProvisionalContent();
            this.removeExcessiveView(emptyContentFrame);
        }
        this.show();
        return emptyContentFrame;
    }
    
    public void disableClick() {
        if (CamLog.VERBOSE) {
            CamLog.d("disableClick() has been called.");
        }
        this.mContentContainer.disableClick();
        this.mClickable = false;
    }
    
    public void enableClick() {
        if (CamLog.VERBOSE) {
            CamLog.d("enableClick() has been called.");
        }
        this.mContentContainer.enableClick();
        this.mClickable = true;
    }
    
    public Content getCurrentContent() {
        if (CamLog.VERBOSE) {
            CamLog.d("getCurrentContent() has been called.");
        }
        if (this.mContentContainer == null || this.mContentContainer.getChildCount() == 0) {
            CamLog.w("getCurrentContent() mContentContainer has no content.");
            return null;
        }
        final ContentPallet contentPallet = (ContentPallet)this.mContentContainer.getChildAt(0);
        if (contentPallet == null) {
            CamLog.w("getCurrentContent() pallet(0) is null.");
            return null;
        }
        return contentPallet.getContent();
    }
    
    public int getCurrentRequestId() {
        if (CamLog.VERBOSE) {
            CamLog.d("getCurrentRequestId() has been called.");
        }
        if (this.mContentContainer == null || this.mContentContainer.getChildCount() == 0) {
            CamLog.w("getCurrentRequestId() mContentContainer has no content.");
            return -1;
        }
        final ContentPallet contentPallet = (ContentPallet)this.mContentContainer.getChildAt(0);
        if (contentPallet == null) {
            CamLog.w("getCurrentRequestId() pallet(0) is null.");
            return -1;
        }
        return contentPallet.getRequestId();
    }
    
    public List<Content.ContentInfo> getLocalContentInfo() {
        return this.mContentLoader.getLocalCache();
    }
    
    public void hide() {
        if (CamLog.VERBOSE) {
            CamLog.d("hide()");
        }
        this.mContentContainer.setVisibility(4);
    }
    
    public boolean isLoading() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("hasLoadingContent() has been called. child = ");
            sb.append(this.mContentContainer.getChildCount());
            CamLog.d(sb.toString());
        }
        for (int i = 0; i < this.mContentContainer.getChildCount(); ++i) {
            if (!((ContentPallet)this.mContentContainer.getChildAt(i)).hasContent()) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("isLoading() pallet is loading. index = ");
                    sb2.append(i);
                    CamLog.d(sb2.toString());
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean isLoadingInProvisionalContent() {
        for (int i = 0; i < this.mContentContainer.getChildCount(); ++i) {
            if (((ContentPallet)this.mContentContainer.getChildAt(i)).isProvisionalContent()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onContentCreated(final int i, final Content content, final Bitmap bitmap) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onContentCreated( ");
            sb.append(i);
            sb.append(" )");
            CamLog.d(sb.toString());
        }
        if (this.mActivity == null) {
            CamLog.w("Activity has already been released.");
            return;
        }
        int emptyContentFrame;
        if ((emptyContentFrame = i) == -1) {
            emptyContentFrame = i;
            if (!this.isLoading()) {
                emptyContentFrame = this.createEmptyContentFrame();
            }
        }
        final ContentPallet searchPallet = this.searchPallet(emptyContentFrame);
        if (searchPallet != null) {
            searchPallet.set(content, bitmap);
            this.removeExcessiveView(emptyContentFrame);
        }
        if (this.mThumbnailStateListener != null) {
            this.mThumbnailStateListener.onThumbnailCreated(content);
        }
    }
    
    @Override
    public void onNoContentLoaded() {
        this.remove();
    }
    
    @Override
    public void onStorageSizeChanged(final StorageType storageType, final long n) {
    }
    
    @Override
    public void onStorageStateChanged(final StorageType storageType, final StorageState storageState, final StorageReadyState storageReadyState) {
        if (CamLog.VERBOSE) {
            CamLog.d("onStorageStateChanged");
        }
        final UpdateContentTask updateContentTask = new UpdateContentTask(storageType, storageState);
        this.mUpdateContentTaskList.add(updateContentTask);
        this.mHandler.post((Runnable)updateContentTask);
    }
    
    public void pause() {
        if (CamLog.VERBOSE) {
            CamLog.d("pause() has been called.");
        }
        if (this.mContentLoader != null) {
            this.mContentLoader.pause();
        }
    }
    
    public void reconstructLocalCache() {
        this.mContentLoader.loadLocalCache();
        this.mContentLoader.clearLocalCacheBackup();
        this.mContentLoader.removeInvalidLocalCache((Context)this.mActivity);
        this.mContentLoader.reloadTopContent();
    }
    
    public void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("release() has been called.");
        }
        this.mContentLoader.clearLocalCacheBackup();
        this.mContentLoader.release();
        this.mContentLoader = null;
        this.mStorage.removeStorageStateListener((Storage.StorageStateListener)this);
        final Iterator<UpdateContentTask> iterator = this.mUpdateContentTaskList.iterator();
        while (iterator.hasNext()) {
            this.mHandler.removeCallbacks((Runnable)iterator.next());
        }
        this.mUpdateContentTaskList.clear();
        this.mActivity = null;
    }
    
    public void reload() {
        if (CamLog.VERBOSE) {
            CamLog.d("reload() has been called.");
        }
        if (this.mContentLoader != null) {
            this.mContentLoader.reload(1);
        }
        this.mStorage.addStorageStateListener((Storage.StorageStateListener)this);
    }
    
    public void remove() {
        this.mContentContainer.removeAllViews();
    }
    
    public void requestCreateContentInfoSync(final ArrayList<Uri> list) {
        this.mContentLoader.requestCreateContentInfoSync(list);
    }
    
    public void requestLastContentLoading(final int n) {
        if (this.mContentLoader != null) {
            this.mContentLoader.requestLastDataLoad(n);
        }
    }
    
    public void requestLayout() {
        this.mContentContainer.requestLayout();
    }
    
    public void saveLocalCache() {
        this.mContentLoader.saveLocalCache();
    }
    
    public void setClickThumbnailProgressListener(final OnClickThumbnailProgressListener mClickThumbnailProgressListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("setClickThumbnailProgressListener");
        }
        if ((this.mClickThumbnailProgressListener = mClickThumbnailProgressListener) == null) {
            this.mClickListener = null;
        }
        else {
            this.mClickListener = new ClickListener();
        }
    }
    
    public void setSensorOrientation(final int n) {
        this.mOrientation = n;
        if (this.mContentContainer != null) {
            this.mContentContainer.setSensorOrientation(n);
        }
    }
    
    public void show() {
        if (CamLog.VERBOSE) {
            CamLog.d("show()");
        }
        this.mContentContainer.setVisibility(0);
        this.mContentContainer.cancelRequestHide();
    }
    
    public void showProgress(final int n) {
        if (CamLog.VERBOSE) {
            CamLog.d("showProgress() has been called.");
        }
        final ContentPallet searchPallet = this.searchPallet(n);
        if (searchPallet != null) {
            final View viewById = searchPallet.findViewById(2131296367);
            if (viewById != null) {
                viewById.setVisibility(0);
                viewById.setOnClickListener((View$OnClickListener)this.mClickListener);
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("progress = ");
                sb.append(searchPallet.findViewById(2131296367));
                CamLog.d("ContentsViewController", sb.toString());
            }
        }
    }
    
    public void startHideAnimation(final Animation animation) {
        this.mContentContainer.cancelRequestHide();
        this.startAnimationInner(animation);
    }
    
    public void stopAnimation(final boolean b) {
        final Animation animation = this.mContentContainer.getAnimation();
        if (animation != null) {
            if (!b) {
                animation.setAnimationListener((Animation$AnimationListener)null);
            }
            animation.cancel();
            if (CamLog.VERBOSE) {
                CamLog.d("Animation canceled.");
            }
            this.mContentContainer.setAnimation((Animation)null);
        }
    }
    
    private class ClickListener implements View$OnClickListener
    {
        final ContentsViewController this$0;
        
        private ClickListener(final ContentsViewController this$0) {
            this.this$0 = this$0;
        }
        
        public void onClick(final View obj) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onClick: ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (this.this$0.mClickThumbnailProgressListener != null && obj != null && obj.getId() == 2131296367) {
                this.this$0.mClickThumbnailProgressListener.onClickThumbnailProgress();
            }
        }
    }
    
    public interface OnClickThumbnailProgressListener
    {
        void onClickThumbnailProgress();
    }
    
    private class UpdateContentTask implements Runnable
    {
        private final StorageState mChangedStorageState;
        private final StorageType mChangedStorageType;
        final ContentsViewController this$0;
        
        UpdateContentTask(final ContentsViewController this$0, final StorageType mChangedStorageType, final StorageState mChangedStorageState) {
            this.this$0 = this$0;
            this.mChangedStorageType = mChangedStorageType;
            this.mChangedStorageState = mChangedStorageState;
        }
        
        @Override
        public void run() {
            this.this$0.mUpdateContentTaskList.remove(this);
            if (this.this$0.mContentLoader != null && this.this$0.mContentLoader.getLocalCache() != null) {
                if (this.mChangedStorageState != StorageState.AVAILABLE && this.mChangedStorageState != StorageState.AVAILABLE_NEAR_FULL && this.mChangedStorageState != StorageState.FULL && this.mChangedStorageState != StorageState.READ_ONLY && this.this$0.mContentLoader.getLocalCache().size() > 0 && StorageUtil.getStorageTypeFromPath(this.this$0.mContentLoader.getLocalCache().getFirst().mOriginalPath, (Context)this.this$0.mActivity) == this.mChangedStorageType) {
                    this.this$0.clearContents();
                }
                if (!this.this$0.isLoading()) {
                    this.this$0.reload();
                }
            }
        }
    }
}
