package com.sonyericsson.cameracommon.contentsview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.contentsview.ContentLoader;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.utility.IncrementalId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ContentsViewController implements Storage.StorageStateListener, ContentLoader.ContentCreationCallback {
    public static final int MAX_CONTENT_NUMBER = 1;
    public static final String TAG = "ContentsViewController";
    private Activity mActivity;
    private final ContentsContainer mContentContainer;
    private ContentLoader mContentLoader;
    private boolean mIsCoreCamera;
    private int mOrientation;
    private ContentLoader.SecurityLevel mSecurityLevel;
    private Storage mStorage;
    private ContentPallet.ThumbnailStateListener mThumbnailStateListener;
    private boolean mClickable = true;
    private ClickListener mClickListener = null;
    private OnClickThumbnailProgressListener mClickThumbnailProgressListener = null;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final List<UpdateContentTask> mUpdateContentTaskList = Collections.synchronizedList(new LinkedList());
    private final IncrementalId mRequestIdGenerator = new IncrementalId();

    public interface OnClickThumbnailProgressListener {
        void onClickThumbnailProgress();
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage.StorageStateListener
    public void onStorageSizeChanged(Storage.StorageType storageType, long j) {
    }

    public void requestLayout() {
        this.mContentContainer.requestLayout();
    }

    public ContentsViewController(Activity activity, Storage storage, ContentLoader.SecurityLevel securityLevel, ContentPallet.ThumbnailStateListener thumbnailStateListener) {
        this.mThumbnailStateListener = null;
        this.mIsCoreCamera = false;
        this.mActivity = activity;
        this.mContentLoader = new ContentLoader(storage, securityLevel, this);
        this.mContentContainer = (ContentsContainer) activity.findViewById(R.id.contents_container);
        this.mStorage = storage;
        this.mThumbnailStateListener = thumbnailStateListener;
        this.mIsCoreCamera = CommonUtility.isCoreCameraApp(activity);
        this.mSecurityLevel = securityLevel;
    }

    public void pause() {
        if (CamLog.VERBOSE) {
            CamLog.d("pause() has been called.");
        }
        if (this.mContentLoader != null) {
            this.mContentLoader.pause();
        }
    }

    public void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("release() has been called.");
        }
        this.mContentLoader.clearLocalCacheBackup();
        this.mContentLoader.release();
        this.mContentLoader = null;
        this.mStorage.removeStorageStateListener(this);
        Iterator<UpdateContentTask> it = this.mUpdateContentTaskList.iterator();
        while (it.hasNext()) {
            this.mHandler.removeCallbacks(it.next());
        }
        this.mUpdateContentTaskList.clear();
        this.mActivity = null;
    }

    public int createContentFrame() {
        if (CamLog.VERBOSE) {
            CamLog.d("createContentFrame() has been called.");
        }
        int iCreateEmptyContentFrame = createEmptyContentFrame();
        showProgress(iCreateEmptyContentFrame);
        return iCreateEmptyContentFrame;
    }

    public int createEmptyContentFrame() {
        ContentPallet contentPallet;
        if (CamLog.VERBOSE) {
            CamLog.d("createEmptyContentFrame() has been called.");
        }
        if (this.mActivity == null) {
            CamLog.w("Activity has already been released at createEmptyContentFrame.");
            return -1;
        }
        LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
        if (layoutInflater == null) {
            CamLog.w("could not get inflater.");
            return -1;
        }
        int next = this.mRequestIdGenerator.getNext();
        if (CamLog.VERBOSE) {
            CamLog.d("createEmptyContentFrame : create pallet. id = " + next);
        }
        if (this.mIsCoreCamera) {
            contentPallet = (ContentPallet) layoutInflater.inflate(R.layout.content_pallet_core_camera, (ViewGroup) null);
        } else {
            contentPallet = (ContentPallet) layoutInflater.inflate(R.layout.content_pallet, (ViewGroup) null);
        }
        contentPallet.initialize(next, this.mThumbnailStateListener);
        if (!this.mClickable) {
            contentPallet.disableClick();
        }
        this.mContentContainer.addView(contentPallet);
        this.mContentContainer.setSensorOrientation(this.mOrientation);
        return next;
    }

    private void removeExcessiveView(int i) {
        for (int i2 = 0; i2 < this.mContentContainer.getChildCount(); i2++) {
            ContentPallet contentPallet = (ContentPallet) this.mContentContainer.getChildAt(i2);
            if (contentPallet.hasContent() && i != contentPallet.getRequestId()) {
                this.mContentContainer.removeView(contentPallet);
            }
        }
    }

    public int createProvisionalContentFrame() {
        int iCreateEmptyContentFrame = createEmptyContentFrame();
        ContentPallet contentPalletSearchPallet = searchPallet(iCreateEmptyContentFrame);
        if (contentPalletSearchPallet != null) {
            contentPalletSearchPallet.setProvisionalContent();
            removeExcessiveView(iCreateEmptyContentFrame);
        }
        show();
        return iCreateEmptyContentFrame;
    }

    public void addContent(int i, Uri uri) {
        if (CamLog.VERBOSE) {
            CamLog.d("addContent() has been called. requestId = " + i + ", uri = " + uri);
        }
        if (this.mContentLoader == null) {
            return;
        }
        if (searchPallet(i) != null || this.mSecurityLevel == ContentLoader.SecurityLevel.NEWLY_ADDED_CONTENT_ONLY) {
            this.mContentLoader.request(i, uri);
        } else {
            if (i == -1 || this.mContentContainer.getChildCount() != 0) {
                return;
            }
            reload();
        }
    }

    public void requestLastContentLoading(int i) {
        if (this.mContentLoader != null) {
            this.mContentLoader.requestLastDataLoad(i);
        }
    }

    public void requestCreateContentInfoSync(ArrayList<Uri> arrayList) {
        this.mContentLoader.requestCreateContentInfoSync(arrayList);
    }

    public void reload() {
        if (CamLog.VERBOSE) {
            CamLog.d("reload() has been called.");
        }
        if (this.mContentLoader != null) {
            this.mContentLoader.reload(1);
        }
        this.mStorage.addStorageStateListener(this);
    }

    public void remove() {
        this.mContentContainer.removeAllViews();
    }

    public void show() {
        if (CamLog.VERBOSE) {
            CamLog.d("show()");
        }
        this.mContentContainer.setVisibility(0);
        this.mContentContainer.cancelRequestHide();
    }

    public void hide() {
        if (CamLog.VERBOSE) {
            CamLog.d("hide()");
        }
        this.mContentContainer.setVisibility(4);
    }

    public void setSensorOrientation(int i) {
        this.mOrientation = i;
        if (this.mContentContainer != null) {
            this.mContentContainer.setSensorOrientation(i);
        }
    }

    public void startHideAnimation(Animation animation) {
        this.mContentContainer.cancelRequestHide();
        startAnimationInner(animation);
    }

    private void startAnimationInner(Animation animation) {
        if (animation != null) {
            animation.reset();
            this.mContentContainer.startAnimation(animation);
            if (CamLog.VERBOSE) {
                CamLog.d("Animation started.");
            }
        }
    }

    public void stopAnimation(boolean z) {
        Animation animation = this.mContentContainer.getAnimation();
        if (animation != null) {
            if (!z) {
                animation.setAnimationListener(null);
            }
            animation.cancel();
            if (CamLog.VERBOSE) {
                CamLog.d("Animation canceled.");
            }
            this.mContentContainer.setAnimation(null);
        }
    }

    private ContentPallet searchPallet(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("searchPallet() has been called. requestId = " + i + ", child = " + this.mContentContainer.getChildCount());
        }
        for (int i2 = 0; i2 < this.mContentContainer.getChildCount(); i2++) {
            ContentPallet contentPallet = (ContentPallet) this.mContentContainer.getChildAt(i2);
            if (i == contentPallet.getRequestId()) {
                if (CamLog.VERBOSE) {
                    CamLog.d("searchPallet() pallet find. index = " + i2);
                }
                return contentPallet;
            }
        }
        return null;
    }

    public boolean isLoading() {
        if (CamLog.VERBOSE) {
            CamLog.d("hasLoadingContent() has been called. child = " + this.mContentContainer.getChildCount());
        }
        for (int i = 0; i < this.mContentContainer.getChildCount(); i++) {
            if (!((ContentPallet) this.mContentContainer.getChildAt(i)).hasContent()) {
                if (CamLog.VERBOSE) {
                    CamLog.d("isLoading() pallet is loading. index = " + i);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isLoadingInProvisionalContent() {
        for (int i = 0; i < this.mContentContainer.getChildCount(); i++) {
            if (((ContentPallet) this.mContentContainer.getChildAt(i)).isProvisionalContent()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sonyericsson.cameracommon.contentsview.ContentLoader.ContentCreationCallback
    public void onContentCreated(int i, Content content, Bitmap bitmap) {
        if (CamLog.VERBOSE) {
            CamLog.d("onContentCreated( " + i + " )");
        }
        if (this.mActivity == null) {
            CamLog.w("Activity has already been released.");
            return;
        }
        if (i == -1 && !isLoading()) {
            i = createEmptyContentFrame();
        }
        ContentPallet contentPalletSearchPallet = searchPallet(i);
        if (contentPalletSearchPallet != null) {
            contentPalletSearchPallet.set(content, bitmap);
            removeExcessiveView(i);
        }
        if (this.mThumbnailStateListener != null) {
            this.mThumbnailStateListener.onThumbnailCreated(content);
        }
    }

    @Override // com.sonyericsson.cameracommon.contentsview.ContentLoader.ContentCreationCallback
    public void onNoContentLoaded() {
        remove();
    }

    public void clearContents() {
        this.mContentLoader.pause();
        this.mContentContainer.removeAllViews();
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage.StorageStateListener
    public void onStorageStateChanged(Storage.StorageType storageType, Storage.StorageState storageState, Storage.StorageReadyState storageReadyState) {
        if (CamLog.VERBOSE) {
            CamLog.d("onStorageStateChanged");
        }
        UpdateContentTask updateContentTask = new UpdateContentTask(storageType, storageState);
        this.mUpdateContentTaskList.add(updateContentTask);
        this.mHandler.post(updateContentTask);
    }

    public void showProgress(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("showProgress() has been called.");
        }
        ContentPallet contentPalletSearchPallet = searchPallet(i);
        if (contentPalletSearchPallet != null) {
            View viewFindViewById = contentPalletSearchPallet.findViewById(R.id.content_progress_bar);
            if (viewFindViewById != null) {
                viewFindViewById.setVisibility(0);
                viewFindViewById.setOnClickListener(this.mClickListener);
            }
            if (CamLog.VERBOSE) {
                CamLog.d(TAG, "progress = " + contentPalletSearchPallet.findViewById(R.id.content_progress_bar));
            }
        }
    }

    public void enableClick() {
        if (CamLog.VERBOSE) {
            CamLog.d("enableClick() has been called.");
        }
        this.mContentContainer.enableClick();
        this.mClickable = true;
    }

    public void disableClick() {
        if (CamLog.VERBOSE) {
            CamLog.d("disableClick() has been called.");
        }
        this.mContentContainer.disableClick();
        this.mClickable = false;
    }

    public void setClickThumbnailProgressListener(OnClickThumbnailProgressListener onClickThumbnailProgressListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("setClickThumbnailProgressListener");
        }
        this.mClickThumbnailProgressListener = onClickThumbnailProgressListener;
        if (onClickThumbnailProgressListener == null) {
            this.mClickListener = null;
        } else {
            this.mClickListener = new ClickListener();
        }
    }

    private class ClickListener implements View.OnClickListener {
        private ClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (CamLog.VERBOSE) {
                CamLog.d("onClick: " + view);
            }
            if (ContentsViewController.this.mClickThumbnailProgressListener == null || view == null || view.getId() != 2131296367) {
                return;
            }
            ContentsViewController.this.mClickThumbnailProgressListener.onClickThumbnailProgress();
        }
    }

    public List<Content.ContentInfo> getLocalContentInfo() {
        return this.mContentLoader.getLocalCache();
    }

    public void reconstructLocalCache() {
        this.mContentLoader.loadLocalCache();
        this.mContentLoader.clearLocalCacheBackup();
        this.mContentLoader.removeInvalidLocalCache(this.mActivity);
        this.mContentLoader.reloadTopContent();
    }

    public void saveLocalCache() {
        this.mContentLoader.saveLocalCache();
    }

    public Content getCurrentContent() {
        if (CamLog.VERBOSE) {
            CamLog.d("getCurrentContent() has been called.");
        }
        if (this.mContentContainer == null || this.mContentContainer.getChildCount() == 0) {
            CamLog.w("getCurrentContent() mContentContainer has no content.");
            return null;
        }
        ContentPallet contentPallet = (ContentPallet) this.mContentContainer.getChildAt(0);
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
        ContentPallet contentPallet = (ContentPallet) this.mContentContainer.getChildAt(0);
        if (contentPallet == null) {
            CamLog.w("getCurrentRequestId() pallet(0) is null.");
            return -1;
        }
        return contentPallet.getRequestId();
    }

    private class UpdateContentTask implements Runnable {
        private final Storage.StorageState mChangedStorageState;
        private final Storage.StorageType mChangedStorageType;

        UpdateContentTask(Storage.StorageType storageType, Storage.StorageState storageState) {
            this.mChangedStorageType = storageType;
            this.mChangedStorageState = storageState;
        }

        @Override // java.lang.Runnable
        public void run() {
            ContentsViewController.this.mUpdateContentTaskList.remove(this);
            if (ContentsViewController.this.mContentLoader == null || ContentsViewController.this.mContentLoader.getLocalCache() == null) {
                return;
            }
            if (!(this.mChangedStorageState == Storage.StorageState.AVAILABLE || this.mChangedStorageState == Storage.StorageState.AVAILABLE_NEAR_FULL || this.mChangedStorageState == Storage.StorageState.FULL || this.mChangedStorageState == Storage.StorageState.READ_ONLY) && ContentsViewController.this.mContentLoader.getLocalCache().size() > 0 && StorageUtil.getStorageTypeFromPath(ContentsViewController.this.mContentLoader.getLocalCache().getFirst().mOriginalPath, ContentsViewController.this.mActivity) == this.mChangedStorageType) {
                ContentsViewController.this.clearContents();
            }
            if (ContentsViewController.this.isLoading()) {
                return;
            }
            ContentsViewController.this.reload();
        }
    }
}
