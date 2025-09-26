// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

import android.os.Message;
import android.os.Handler;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import android.view.MotionEvent;
import android.graphics.Rect;
import java.util.Iterator;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewStub;
import android.support.annotation.Nullable;
import com.sonyericsson.android.camera.util.CamLog;
import android.support.annotation.NonNull;
import android.animation.Animator;
import android.animation.Animator$AnimatorListener;
import android.animation.PropertyValuesHolder;
import android.view.View;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.view.View$OnClickListener;
import android.animation.ObjectAnimator;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import android.content.Context;
import java.util.concurrent.BlockingQueue;

public class HintTextViewController
{
    private static final String TAG = "HintTextViewController";
    private HintTextContent.HintPriority mContentDisplayThreshold;
    private BlockingQueue<HintTextContent> mContentPrioritizedStack;
    private Context mContext;
    private TimeoutHandler mHandler;
    private ViewGroup mHintTextBackground;
    private FrameLayout mHintTextContainer;
    private ObjectAnimator mHintTextFadeOutAnimator;
    private HintTextView mHintTextView;
    private HintTextContentListener mListener;
    private View$OnClickListener mOnClickListener;
    private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
    
    public HintTextViewController(final ViewGroup viewGroup, final HintTextContentListener mListener, final LayoutDependencyResolver.ScreenAspect mScreenAspect) {
        this.mContentPrioritizedStack = new BlockingFiloContentQueue();
        this.mOnClickListener = (View$OnClickListener)new View$OnClickListener() {
            final HintTextViewController this$0;
            
            public void onClick(final View view) {
                final HintTextContent access$000 = this.this$0.referTop();
                if (access$000 != null) {
                    this.this$0.mListener.onContentButtonClick(this.this$0, access$000);
                }
            }
        };
        this.mListener = mListener;
        this.mScreenAspect = mScreenAspect;
        this.initHintTextBackground(viewGroup);
        this.initHintTextView(viewGroup);
        (this.mHintTextFadeOutAnimator = ObjectAnimator.ofPropertyValuesHolder((Object)this.mHintTextView, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { 1.0f, 0.0f }) })).addListener((Animator$AnimatorListener)new Animator$AnimatorListener(this) {
            final HintTextViewController this$0;
            
            public void onAnimationCancel(final Animator animator) {
                this.this$0.mHintTextView.setAlpha(1.0f);
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (this.this$0.mHandler != null && this.this$0.mHandler.attachedContent != null) {
                    this.this$0.cancelFromContentStack(this.this$0.mHandler.attachedContent);
                }
                this.this$0.mHintTextView.setAlpha(1.0f);
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
            }
        });
    }
    
    private boolean cancelFromContentStack(@NonNull final HintTextContent hintTextContent) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cancel: content = ");
            sb.append(hintTextContent.getTag());
            CamLog.d(sb.toString());
        }
        if (!this.mContentPrioritizedStack.contains(hintTextContent)) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("cancel: content is not queued, content = ");
                sb2.append(hintTextContent.getTag());
                CamLog.d(sb2.toString());
            }
            return false;
        }
        final HintTextContent referTop = this.referTop();
        this.mContentPrioritizedStack.remove(hintTextContent);
        if (this.mContentPrioritizedStack.isEmpty()) {
            this.hideInternal(hintTextContent);
        }
        final HintTextContent referTop2 = this.referTop();
        if (referTop2 != null && !this.isSameContent(referTop, referTop2)) {
            if (this.mContentDisplayThreshold != null && referTop2.getPriority().compareTo(this.mContentDisplayThreshold) <= 0) {
                this.showInternal(referTop2);
            }
            else {
                this.hideInternal(hintTextContent);
            }
        }
        return true;
    }
    
    private void cancelTimeoutCount() {
        if (this.mHandler != null) {
            this.mHandler.cancelCount();
            this.mHandler = null;
        }
    }
    
    private void hideInternal(@Nullable final HintTextContent hintTextContent) {
        if (hintTextContent == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("hideInternal: content is null");
            }
            return;
        }
        this.cancelTimeoutCount();
        hintTextContent.detach(this.mHintTextView);
        if (hintTextContent.isToast()) {
            if (CamLog.VERBOSE) {
                CamLog.d("hideInternal: remove onetime content");
            }
            this.mContentPrioritizedStack.remove(hintTextContent);
        }
    }
    
    private void initHintTextBackground(final ViewGroup viewGroup) {
        viewGroup.addView((View)(this.mHintTextBackground = (ViewGroup)new Background(viewGroup.getContext())));
        this.mHintTextBackground.getLayoutParams().width = -1;
        this.mHintTextBackground.getLayoutParams().height = -1;
        this.mHintTextBackground.setClickable(false);
        this.mHintTextBackground.setFocusable(false);
    }
    
    private void initHintTextView(final ViewGroup viewGroup) {
        final ViewStub viewStub = (ViewStub)viewGroup.findViewById(2131296414);
        this.mContext = viewStub.getContext();
        this.mHintTextContainer = (FrameLayout)viewStub.inflate();
        if (this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            ((FrameLayout$LayoutParams)this.mHintTextContainer.getLayoutParams()).setMargins(ResourceUtil.getDimensionPixelSize(this.mContext, this.mContext.getPackageName(), 2131165456), 0, 0, 0);
        }
        (this.mHintTextView = HintTextView.inflate(this.mContext)).setOnButtonClickListener(this.mOnClickListener);
        this.mHintTextContainer.addView((View)this.mHintTextView);
    }
    
    private boolean isSameContent(@Nullable final HintTextContent hintTextContent, @Nullable final HintTextContent hintTextContent2) {
        if (hintTextContent != null) {
            return hintTextContent.equals(hintTextContent2);
        }
        return hintTextContent2 == null;
    }
    
    @Nullable
    private HintTextContent referTop() {
        if (!this.mContentPrioritizedStack.isEmpty()) {
            return this.mContentPrioritizedStack.peek();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("referTop: queue is empty");
        }
        return null;
    }
    
    private void showInternal(@Nullable final HintTextContent hintTextContent) {
        if (hintTextContent == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("showInternal: content is null");
            }
            return;
        }
        this.cancelTimeoutCount();
        hintTextContent.attach(this.mHintTextView);
        final long timedOutDuration = hintTextContent.getTimedOutDuration();
        if (timedOutDuration != -1L) {
            final int fadeDuration = hintTextContent.getFadeDuration();
            if (fadeDuration != -1) {
                this.startFadeOut(timedOutDuration, fadeDuration, hintTextContent);
            }
            else {
                this.startTimeoutCount(timedOutDuration, hintTextContent);
            }
        }
    }
    
    private void startFadeOut(final long n, final int n2, final HintTextContent hintTextContent) {
        if (this.mHandler == null) {
            this.mHandler = new TimeoutHandler();
        }
        this.mHandler.startFadeOut(n, n2, hintTextContent);
    }
    
    private void startTimeoutCount(final long n, final HintTextContent hintTextContent) {
        if (this.mHandler == null) {
            this.mHandler = new TimeoutHandler();
        }
        this.mHandler.startTimeoutCount(n, hintTextContent);
    }
    
    public boolean cancel(@NonNull final String str) {
        if (this.mContentPrioritizedStack.isEmpty()) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("cancel: content is empty = ");
                sb.append(str);
                CamLog.d(sb.toString());
            }
            return false;
        }
        final HintTextContent hintTextContent = null;
        final Iterator<Object> iterator = this.mContentPrioritizedStack.iterator();
        HintTextContent hintTextContent2;
        do {
            hintTextContent2 = hintTextContent;
            if (!iterator.hasNext()) {
                break;
            }
            hintTextContent2 = iterator.next();
        } while (!hintTextContent2.getTag().equals(str));
        if (hintTextContent2 == null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("cancel: content is not queued, tag = ");
                sb2.append(str);
                CamLog.d(sb2.toString());
            }
            return false;
        }
        return this.cancelFromContentStack(hintTextContent2);
    }
    
    public void clearAll() {
        this.cancelTimeoutCount();
        this.hideInternal(this.referTop());
        while (!this.mContentPrioritizedStack.isEmpty()) {
            this.mContentPrioritizedStack.poll();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("clear: removed all entry");
        }
    }
    
    public void clearToastContent() {
        final HintTextContent referTop = this.referTop();
        if (referTop != null && referTop.isToast()) {
            this.cancelFromContentStack(referTop);
        }
    }
    
    public boolean hide() {
        if (this.mContentDisplayThreshold != null) {
            this.hideInternal(this.referTop());
            this.mContentDisplayThreshold = null;
            return true;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("hide: hint text was already hidden");
        }
        return false;
    }
    
    public boolean isHintTextDisplayed(@NonNull final String anObject) {
        final HintTextContent referTop = this.referTop();
        return this.mHintTextView != null && referTop != null && (this.mHintTextView.getVisibility() == 0 && referTop.getTag().equals(anObject));
    }
    
    public boolean isNoTimeOutHinTextDisplayed() {
        final HintTextContent referTop = this.referTop();
        return referTop != null && referTop.getTimedOutDuration() == -1L && this.mContentDisplayThreshold != null;
    }
    
    public boolean post(@NonNull final HintTextContent hintTextContent) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("post: content = ");
            sb.append(hintTextContent.getTag());
            CamLog.d(sb.toString());
        }
        if (this.mContentPrioritizedStack.contains(hintTextContent)) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("post: content has been queued, content = ");
                sb2.append(hintTextContent.getTag());
                CamLog.d(sb2.toString());
            }
            return false;
        }
        final HintTextContent referTop = this.referTop();
        this.mContentPrioritizedStack.add(hintTextContent);
        final HintTextContent referTop2 = this.referTop();
        if (!this.isSameContent(referTop, referTop2)) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("post: top is changed, old = ");
                sb3.append(referTop);
                sb3.append(", new = ");
                sb3.append(referTop2.getTag());
                CamLog.d(sb3.toString());
            }
            if (this.mContentDisplayThreshold != null && referTop2.getPriority().compareTo(this.mContentDisplayThreshold) <= 0) {
                if (referTop != null) {
                    this.hideInternal(referTop);
                }
                this.showInternal(referTop2);
            }
        }
        return true;
    }
    
    public void setUiOrientation(final Rect rect, final Context context, final LayoutDependencyResolver.ScreenAspect screenAspect, final int n) {
        if (this.mHintTextView == null) {
            return;
        }
        this.mHintTextView.setUiOrientation(rect, context, screenAspect, n);
    }
    
    public boolean show(@NonNull final HintTextContent.HintPriority hintPriority) {
        if (this.mContentDisplayThreshold != null && hintPriority.compareTo(this.mContentDisplayThreshold) == 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("show: hint text was already shown");
            }
            return false;
        }
        final HintTextContent referTop = this.referTop();
        if (referTop != null) {
            if (referTop.getPriority().compareTo(hintPriority) <= 0) {
                if (this.mContentDisplayThreshold == null || referTop.getPriority().compareTo(this.mContentDisplayThreshold) > 0) {
                    this.showInternal(referTop);
                }
            }
            else {
                this.hideInternal(referTop);
            }
        }
        this.mContentDisplayThreshold = hintPriority;
        return true;
    }
    
    public boolean showAll() {
        return this.show(HintTextContent.HintPriority.LOW);
    }
    
    public void updateHintTextContainer(final int n, final int n2) {
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mHintTextContainer.getLayoutParams();
        final int max = Math.max(n, n2);
        final int min = Math.min(n, n2);
        frameLayout$LayoutParams.width = max;
        frameLayout$LayoutParams.height = min;
        frameLayout$LayoutParams.gravity = 16;
        final LayoutDependencyResolver.ScreenAspect mScreenAspect = this.mScreenAspect;
        final LayoutDependencyResolver.ScreenAspect eighteen_NINE = LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE;
        int n3 = 0;
        if (mScreenAspect == eighteen_NINE) {
            n3 = 0 + ResourceUtil.getDimensionPixelSize(this.mContext, this.mContext.getPackageName(), 2131165456);
        }
        int leftMargin = n3;
        if (n == n2) {
            leftMargin = n3 + n2 / 3;
        }
        frameLayout$LayoutParams.leftMargin = leftMargin;
        this.mHintTextContainer.requestLayout();
    }
    
    private class Background extends FrameLayout
    {
        final HintTextViewController this$0;
        
        public Background(final HintTextViewController this$0, final Context context) {
            this.this$0 = this$0;
            super(context);
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(n, n2);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onTouchEvent: ");
                sb.append(motionEvent.getAction());
                CamLog.d(sb.toString());
            }
            if (!this.this$0.mContentPrioritizedStack.isEmpty()) {
                switch (motionEvent.getAction()) {
                    case 1: {
                        return false;
                    }
                    case 0: {
                        return this.this$0.isHintTextDisplayed(HintTextAutoPowerOff.class.getSimpleName());
                    }
                }
            }
            return false;
        }
    }
    
    private static class BlockingFiloContentQueue extends PriorityBlockingQueue<HintTextContent>
    {
        private static final Map<HintTextContent, Long> sSequentialIndexMap;
        private long mCount;
        
        static {
            sSequentialIndexMap = new LinkedHashMap<HintTextContent, Long>();
        }
        
        public BlockingFiloContentQueue() {
            super(2, new Comparator<HintTextContent>() {
                @Override
                public int compare(final HintTextContent key, final HintTextContent key2) {
                    int compareTo;
                    final int n = compareTo = key.getPriority().compareTo(key2.getPriority());
                    if (n == 0) {
                        compareTo = n;
                        if (!key.equals(key2)) {
                            if (BlockingFiloContentQueue.sSequentialIndexMap.getOrDefault(key, Long.MIN_VALUE) < BlockingFiloContentQueue.sSequentialIndexMap.getOrDefault(key2, Long.MIN_VALUE)) {
                                compareTo = 1;
                            }
                            else {
                                compareTo = -1;
                            }
                        }
                    }
                    return compareTo;
                }
            });
            BlockingFiloContentQueue.sSequentialIndexMap.clear();
        }
        
        @Override
        public void clear() {
            super.clear();
            BlockingFiloContentQueue.sSequentialIndexMap.clear();
            this.mCount = 0L;
        }
        
        @Override
        public boolean offer(final HintTextContent e) {
            final Map<HintTextContent, Long> sSequentialIndexMap = BlockingFiloContentQueue.sSequentialIndexMap;
            final long mCount = this.mCount;
            this.mCount = 1L + mCount;
            sSequentialIndexMap.put(e, mCount);
            return super.offer(e);
        }
        
        @Override
        public HintTextContent poll() {
            final HintTextContent hintTextContent = super.poll();
            BlockingFiloContentQueue.sSequentialIndexMap.remove(hintTextContent);
            return hintTextContent;
        }
    }
    
    public interface HintTextContentListener
    {
        void onContentButtonClick(final HintTextViewController p0, final HintTextContent p1);
        
        void onStateChanged();
    }
    
    private class TimeoutHandler extends Handler
    {
        private static final int MSG_START_FADE_OUT = 2;
        private static final int MSG_TIMEOUT = 1;
        public HintTextContent attachedContent;
        final HintTextViewController this$0;
        
        private TimeoutHandler(final HintTextViewController this$0) {
            this.this$0 = this$0;
        }
        
        public void cancelCount() {
            this.removeMessages(1);
            this.removeMessages(2);
            this.this$0.mHintTextFadeOutAnimator.cancel();
            this.this$0.mListener.onStateChanged();
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                case 2: {
                    this.this$0.mHintTextFadeOutAnimator.cancel();
                    this.this$0.mHintTextFadeOutAnimator.setDuration((long)message.arg1);
                    this.this$0.mHintTextFadeOutAnimator.start();
                    break;
                }
                case 1: {
                    if (this.attachedContent != null) {
                        this.this$0.cancelFromContentStack(this.attachedContent);
                        break;
                    }
                    break;
                }
            }
        }
        
        public void startFadeOut(final long n, final int arg1, final HintTextContent attachedContent) {
            this.cancelCount();
            final Message obtain = Message.obtain();
            obtain.what = 2;
            obtain.arg1 = arg1;
            this.sendMessageDelayed(obtain, n);
            this.attachedContent = attachedContent;
        }
        
        public void startTimeoutCount(final long n, final HintTextContent attachedContent) {
            this.sendEmptyMessageDelayed(1, n);
            this.attachedContent = attachedContent;
        }
    }
}
