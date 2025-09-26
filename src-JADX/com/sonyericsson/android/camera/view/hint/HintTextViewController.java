package com.sonyericsson.android.camera.view.hint;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.hint.HintTextContent;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HintTextViewController {
    private static final String TAG = "HintTextViewController";
    private HintTextContent.HintPriority mContentDisplayThreshold;
    private Context mContext;
    private TimeoutHandler mHandler;
    private ViewGroup mHintTextBackground;
    private FrameLayout mHintTextContainer;
    private ObjectAnimator mHintTextFadeOutAnimator;
    private HintTextView mHintTextView;
    private HintTextContentListener mListener;
    private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private BlockingQueue<HintTextContent> mContentPrioritizedStack = new BlockingFiloContentQueue();
    private View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.hint.HintTextViewController.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            HintTextContent hintTextContentReferTop = HintTextViewController.this.referTop();
            if (hintTextContentReferTop != null) {
                HintTextViewController.this.mListener.onContentButtonClick(HintTextViewController.this, hintTextContentReferTop);
            }
        }
    };

    public interface HintTextContentListener {
        void onContentButtonClick(HintTextViewController hintTextViewController, HintTextContent hintTextContent);

        void onStateChanged();
    }

    private static class BlockingFiloContentQueue extends PriorityBlockingQueue<HintTextContent> {
        private static final Map<HintTextContent, Long> sSequentialIndexMap = new LinkedHashMap();
        private long mCount;

        public BlockingFiloContentQueue() {
            super(2, new Comparator<HintTextContent>() { // from class: com.sonyericsson.android.camera.view.hint.HintTextViewController.BlockingFiloContentQueue.1
                @Override // java.util.Comparator
                public int compare(HintTextContent hintTextContent, HintTextContent hintTextContent2) {
                    int iCompareTo = hintTextContent.getPriority().compareTo(hintTextContent2.getPriority());
                    return (iCompareTo != 0 || hintTextContent.equals(hintTextContent2)) ? iCompareTo : ((Long) BlockingFiloContentQueue.sSequentialIndexMap.getOrDefault(hintTextContent, Long.MIN_VALUE)).longValue() < ((Long) BlockingFiloContentQueue.sSequentialIndexMap.getOrDefault(hintTextContent2, Long.MIN_VALUE)).longValue() ? 1 : -1;
                }
            });
            sSequentialIndexMap.clear();
        }

        @Override // java.util.concurrent.PriorityBlockingQueue, java.util.Queue, java.util.concurrent.BlockingQueue
        public boolean offer(HintTextContent hintTextContent) {
            Map<HintTextContent, Long> map = sSequentialIndexMap;
            long j = this.mCount;
            this.mCount = 1 + j;
            map.put(hintTextContent, Long.valueOf(j));
            return super.offer((BlockingFiloContentQueue) hintTextContent);
        }

        @Override // java.util.concurrent.PriorityBlockingQueue, java.util.Queue
        public HintTextContent poll() {
            HintTextContent hintTextContent = (HintTextContent) super.poll();
            sSequentialIndexMap.remove(hintTextContent);
            return hintTextContent;
        }

        @Override // java.util.concurrent.PriorityBlockingQueue, java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            super.clear();
            sSequentialIndexMap.clear();
            this.mCount = 0L;
        }
    }

    public HintTextViewController(ViewGroup viewGroup, HintTextContentListener hintTextContentListener, LayoutDependencyResolver.ScreenAspect screenAspect) {
        this.mListener = hintTextContentListener;
        this.mScreenAspect = screenAspect;
        initHintTextBackground(viewGroup);
        initHintTextView(viewGroup);
        this.mHintTextFadeOutAnimator = ObjectAnimator.ofPropertyValuesHolder(this.mHintTextView, PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f));
        this.mHintTextFadeOutAnimator.addListener(new Animator.AnimatorListener() { // from class: com.sonyericsson.android.camera.view.hint.HintTextViewController.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) throws Resources.NotFoundException {
                if (HintTextViewController.this.mHandler != null && HintTextViewController.this.mHandler.attachedContent != null) {
                    HintTextViewController.this.cancelFromContentStack(HintTextViewController.this.mHandler.attachedContent);
                }
                HintTextViewController.this.mHintTextView.setAlpha(1.0f);
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                HintTextViewController.this.mHintTextView.setAlpha(1.0f);
            }
        });
    }

    public void clearAll() {
        cancelTimeoutCount();
        hideInternal(referTop());
        while (!this.mContentPrioritizedStack.isEmpty()) {
            this.mContentPrioritizedStack.poll();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("clear: removed all entry");
        }
    }

    public void clearToastContent() throws Resources.NotFoundException {
        HintTextContent hintTextContentReferTop = referTop();
        if (hintTextContentReferTop == null || !hintTextContentReferTop.isToast()) {
            return;
        }
        cancelFromContentStack(hintTextContentReferTop);
    }

    public boolean post(@NonNull HintTextContent hintTextContent) throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("post: content = " + hintTextContent.getTag());
        }
        if (this.mContentPrioritizedStack.contains(hintTextContent)) {
            if (CamLog.VERBOSE) {
                CamLog.d("post: content has been queued, content = " + hintTextContent.getTag());
            }
            return false;
        }
        HintTextContent hintTextContentReferTop = referTop();
        this.mContentPrioritizedStack.add(hintTextContent);
        HintTextContent hintTextContentReferTop2 = referTop();
        if (!isSameContent(hintTextContentReferTop, hintTextContentReferTop2)) {
            if (CamLog.VERBOSE) {
                CamLog.d("post: top is changed, old = " + hintTextContentReferTop + ", new = " + hintTextContentReferTop2.getTag());
            }
            if (this.mContentDisplayThreshold != null && hintTextContentReferTop2.getPriority().compareTo(this.mContentDisplayThreshold) <= 0) {
                if (hintTextContentReferTop != null) {
                    hideInternal(hintTextContentReferTop);
                }
                showInternal(hintTextContentReferTop2);
            }
        }
        return true;
    }

    public boolean cancel(@NonNull String str) {
        if (this.mContentPrioritizedStack.isEmpty()) {
            if (CamLog.VERBOSE) {
                CamLog.d("cancel: content is empty = " + str);
            }
            return false;
        }
        HintTextContent hintTextContent = null;
        Iterator it = this.mContentPrioritizedStack.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            HintTextContent hintTextContent2 = (HintTextContent) it.next();
            if (hintTextContent2.getTag().equals(str)) {
                hintTextContent = hintTextContent2;
                break;
            }
        }
        if (hintTextContent == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("cancel: content is not queued, tag = " + str);
            }
            return false;
        }
        return cancelFromContentStack(hintTextContent);
    }

    public boolean showAll() {
        return show(HintTextContent.HintPriority.LOW);
    }

    public boolean show(@NonNull HintTextContent.HintPriority hintPriority) throws Resources.NotFoundException {
        if (this.mContentDisplayThreshold != null && hintPriority.compareTo(this.mContentDisplayThreshold) == 0) {
            if (!CamLog.VERBOSE) {
                return false;
            }
            CamLog.d("show: hint text was already shown");
            return false;
        }
        HintTextContent hintTextContentReferTop = referTop();
        if (hintTextContentReferTop != null) {
            if (hintTextContentReferTop.getPriority().compareTo(hintPriority) <= 0) {
                if (this.mContentDisplayThreshold == null || hintTextContentReferTop.getPriority().compareTo(this.mContentDisplayThreshold) > 0) {
                    showInternal(hintTextContentReferTop);
                }
            } else {
                hideInternal(hintTextContentReferTop);
            }
        }
        this.mContentDisplayThreshold = hintPriority;
        return true;
    }

    public boolean hide() {
        if (this.mContentDisplayThreshold != null) {
            hideInternal(referTop());
            this.mContentDisplayThreshold = null;
            return true;
        }
        if (!CamLog.VERBOSE) {
            return false;
        }
        CamLog.d("hide: hint text was already hidden");
        return false;
    }

    public void setUiOrientation(Rect rect, Context context, LayoutDependencyResolver.ScreenAspect screenAspect, int i) {
        if (this.mHintTextView == null) {
            return;
        }
        this.mHintTextView.setUiOrientation(rect, context, screenAspect, i);
    }

    public boolean isNoTimeOutHinTextDisplayed() {
        HintTextContent hintTextContentReferTop = referTop();
        return (hintTextContentReferTop == null || hintTextContentReferTop.getTimedOutDuration() != -1 || this.mContentDisplayThreshold == null) ? false : true;
    }

    public boolean isHintTextDisplayed(@NonNull String str) {
        HintTextContent hintTextContentReferTop = referTop();
        return this.mHintTextView != null && hintTextContentReferTop != null && this.mHintTextView.getVisibility() == 0 && hintTextContentReferTop.getTag().equals(str);
    }

    private void showInternal(@Nullable HintTextContent hintTextContent) throws Resources.NotFoundException {
        if (hintTextContent == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("showInternal: content is null");
                return;
            }
            return;
        }
        cancelTimeoutCount();
        hintTextContent.attach(this.mHintTextView);
        long timedOutDuration = hintTextContent.getTimedOutDuration();
        if (timedOutDuration != -1) {
            int fadeDuration = hintTextContent.getFadeDuration();
            if (fadeDuration != -1) {
                startFadeOut(timedOutDuration, fadeDuration, hintTextContent);
            } else {
                startTimeoutCount(timedOutDuration, hintTextContent);
            }
        }
    }

    private void hideInternal(@Nullable HintTextContent hintTextContent) {
        if (hintTextContent == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("hideInternal: content is null");
                return;
            }
            return;
        }
        cancelTimeoutCount();
        hintTextContent.detach(this.mHintTextView);
        if (hintTextContent.isToast()) {
            if (CamLog.VERBOSE) {
                CamLog.d("hideInternal: remove onetime content");
            }
            this.mContentPrioritizedStack.remove(hintTextContent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean cancelFromContentStack(@NonNull HintTextContent hintTextContent) throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("cancel: content = " + hintTextContent.getTag());
        }
        if (!this.mContentPrioritizedStack.contains(hintTextContent)) {
            if (CamLog.VERBOSE) {
                CamLog.d("cancel: content is not queued, content = " + hintTextContent.getTag());
            }
            return false;
        }
        HintTextContent hintTextContentReferTop = referTop();
        this.mContentPrioritizedStack.remove(hintTextContent);
        if (this.mContentPrioritizedStack.isEmpty()) {
            hideInternal(hintTextContent);
        }
        HintTextContent hintTextContentReferTop2 = referTop();
        if (hintTextContentReferTop2 != null && !isSameContent(hintTextContentReferTop, hintTextContentReferTop2)) {
            if (this.mContentDisplayThreshold != null && hintTextContentReferTop2.getPriority().compareTo(this.mContentDisplayThreshold) <= 0) {
                showInternal(hintTextContentReferTop2);
            } else {
                hideInternal(hintTextContent);
            }
        }
        return true;
    }

    private void startTimeoutCount(long j, HintTextContent hintTextContent) {
        if (this.mHandler == null) {
            this.mHandler = new TimeoutHandler();
        }
        this.mHandler.startTimeoutCount(j, hintTextContent);
    }

    private void startFadeOut(long j, int i, HintTextContent hintTextContent) {
        if (this.mHandler == null) {
            this.mHandler = new TimeoutHandler();
        }
        this.mHandler.startFadeOut(j, i, hintTextContent);
    }

    private void cancelTimeoutCount() {
        if (this.mHandler != null) {
            this.mHandler.cancelCount();
            this.mHandler = null;
        }
    }

    private class TimeoutHandler extends Handler {
        private static final int MSG_START_FADE_OUT = 2;
        private static final int MSG_TIMEOUT = 1;
        public HintTextContent attachedContent;

        private TimeoutHandler() {
        }

        public void startTimeoutCount(long j, HintTextContent hintTextContent) {
            sendEmptyMessageDelayed(1, j);
            this.attachedContent = hintTextContent;
        }

        public void startFadeOut(long j, int i, HintTextContent hintTextContent) {
            cancelCount();
            Message messageObtain = Message.obtain();
            messageObtain.what = 2;
            messageObtain.arg1 = i;
            sendMessageDelayed(messageObtain, j);
            this.attachedContent = hintTextContent;
        }

        public void cancelCount() {
            removeMessages(1);
            removeMessages(2);
            HintTextViewController.this.mHintTextFadeOutAnimator.cancel();
            HintTextViewController.this.mListener.onStateChanged();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws Resources.NotFoundException {
            switch (message.what) {
                case 1:
                    if (this.attachedContent != null) {
                        HintTextViewController.this.cancelFromContentStack(this.attachedContent);
                        break;
                    }
                    break;
                case 2:
                    HintTextViewController.this.mHintTextFadeOutAnimator.cancel();
                    HintTextViewController.this.mHintTextFadeOutAnimator.setDuration(message.arg1);
                    HintTextViewController.this.mHintTextFadeOutAnimator.start();
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nullable
    public HintTextContent referTop() {
        if (!this.mContentPrioritizedStack.isEmpty()) {
            return this.mContentPrioritizedStack.peek();
        }
        if (!CamLog.VERBOSE) {
            return null;
        }
        CamLog.d("referTop: queue is empty");
        return null;
    }

    private boolean isSameContent(@Nullable HintTextContent hintTextContent, @Nullable HintTextContent hintTextContent2) {
        if (hintTextContent != null) {
            return hintTextContent.equals(hintTextContent2);
        }
        return hintTextContent2 == null;
    }

    private void initHintTextBackground(ViewGroup viewGroup) {
        this.mHintTextBackground = new Background(viewGroup.getContext());
        viewGroup.addView(this.mHintTextBackground);
        this.mHintTextBackground.getLayoutParams().width = -1;
        this.mHintTextBackground.getLayoutParams().height = -1;
        this.mHintTextBackground.setClickable(false);
        this.mHintTextBackground.setFocusable(false);
    }

    private void initHintTextView(ViewGroup viewGroup) {
        ViewStub viewStub = (ViewStub) viewGroup.findViewById(R.id.hint_text_container_sub);
        this.mContext = viewStub.getContext();
        this.mHintTextContainer = (FrameLayout) viewStub.inflate();
        if (this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            ((FrameLayout.LayoutParams) this.mHintTextContainer.getLayoutParams()).setMargins(ResourceUtil.getDimensionPixelSize(this.mContext, this.mContext.getPackageName(), R.dimen.navigator_container_width), 0, 0, 0);
        }
        this.mHintTextView = HintTextView.inflate(this.mContext);
        this.mHintTextView.setOnButtonClickListener(this.mOnClickListener);
        this.mHintTextContainer.addView(this.mHintTextView);
    }

    public void updateHintTextContainer(int i, int i2) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mHintTextContainer.getLayoutParams();
        int iMax = Math.max(i, i2);
        int iMin = Math.min(i, i2);
        layoutParams.width = iMax;
        layoutParams.height = iMin;
        layoutParams.gravity = 16;
        int dimensionPixelSize = this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE ? 0 + ResourceUtil.getDimensionPixelSize(this.mContext, this.mContext.getPackageName(), R.dimen.navigator_container_width) : 0;
        if (i == i2) {
            dimensionPixelSize += i2 / 3;
        }
        layoutParams.leftMargin = dimensionPixelSize;
        this.mHintTextContainer.requestLayout();
    }

    private class Background extends FrameLayout {
        public Background(Context context) {
            super(context);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (CamLog.VERBOSE) {
                CamLog.d("onTouchEvent: " + motionEvent.getAction());
            }
            if (!HintTextViewController.this.mContentPrioritizedStack.isEmpty()) {
                switch (motionEvent.getAction()) {
                    case 0:
                        return HintTextViewController.this.isHintTextDisplayed(HintTextAutoPowerOff.class.getSimpleName());
                    case 1:
                        return false;
                }
            }
            return false;
        }
    }
}
