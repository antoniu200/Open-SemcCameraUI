// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import java.util.Collection;
import java.util.Arrays;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Iterator;
import java.util.ArrayList;
import com.sonyericsson.android.camera.setting.StoredSettings;
import android.view.ViewStub;
import java.util.List;
import android.animation.Animator;
import android.animation.Animator$AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.PropertyValuesHolder;
import android.view.animation.PathInterpolator;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.view.Window;
import android.widget.ViewFlipper;
import android.view.ViewGroup;
import android.os.Handler;
import android.content.Context;
import android.animation.ObjectAnimator;

public class TutorialController
{
    private static final long FADE_OUT_ANIMATION_DURATION_MILLIS = 300L;
    private static final long SLIDE_IN_ANIMATION_DURATION_LAND_MILLIS = 700L;
    private static final long SLIDE_IN_ANIMATION_DURATION_PORT_MILLIS = 1000L;
    private static final String TAG = "TutorialController";
    private static final boolean TRACE = true;
    private ObjectAnimator mAnimator;
    private OnClickSetupWizardButtonListener mButtonListener;
    private TutorialContainerView mContainer;
    private final Context mContext;
    private TutorialType mCurrentType;
    private final Handler mHandler;
    private boolean mIsOpened;
    private int mOrientation;
    private Runnable mPostStartAnimationTask;
    private TutorialType mPreviousType;
    private final ViewGroup mRootView;
    private SystemUiAccessor mSystemUiAccessor;
    private final TutorialFactory mTutorialFactory;
    private ViewFlipper mViewFlipper;
    private Window mWindow;
    
    public TutorialController(final ViewGroup mRootView, final Window mWindow) {
        this.mIsOpened = false;
        this.mOrientation = 0;
        this.mRootView = mRootView;
        this.mContext = mRootView.getContext();
        this.mTutorialFactory = new TutorialFactory();
        this.mHandler = new Handler();
        this.mWindow = mWindow;
        this.prepareTutorial();
    }
    
    private void addContent(final TutorialContentView.TutorialContent content, final TutorialContentView.OnClickCloseButtonListener onClickCloseButtonListener) {
        final TutorialContainerView.TutorialView tutorialView = new TutorialContainerView.TutorialView(this.mContext);
        final FrameLayout$LayoutParams layoutParams = new FrameLayout$LayoutParams(-1, -1);
        layoutParams.gravity = 3;
        tutorialView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.mViewFlipper.addView((View)tutorialView);
        tutorialView.setContent(content);
        tutorialView.setOnClickCloseButtonListener(onClickCloseButtonListener);
    }
    
    private void addFlags(final int n) {
        if (this.mSystemUiAccessor != null) {
            this.mSystemUiAccessor.onAddFlags(n);
        }
    }
    
    private boolean canHandleNextAction(final TutorialType tutorialType) {
        return this.mPreviousType != tutorialType;
    }
    
    private void clearFlags(final int n) {
        if (this.mSystemUiAccessor != null) {
            this.mSystemUiAccessor.onClearFlags(n);
        }
    }
    
    private ObjectAnimator getFadeOutAnimator(final long duration) {
        final PathInterpolator interpolator = new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)this.mContainer, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { 1.0f, 0.0f }) });
        ofPropertyValuesHolder.setDuration(duration);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)interpolator);
        ofPropertyValuesHolder.addListener((Animator$AnimatorListener)new Animator$AnimatorListener(this) {
            final TutorialController this$0;
            
            public void onAnimationCancel(final Animator animator) {
            }
            
            public void onAnimationEnd(final Animator animator) {
                this.this$0.release();
                this.this$0.hide();
                this.this$0.mContainer.setAlpha(1.0f);
                trace("close() : Tutorial is closed.");
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
            }
        });
        return ofPropertyValuesHolder;
    }
    
    private TutorialType getNextTutorialType(final TutorialType tutorialType) {
        if (this.mViewFlipper != null && this.getTutorialCount() > 0) {
            for (int i = 0; i < this.getTutorialCount(); ++i) {
                List<TutorialType> tutorialTypes = ((TutorialContainerView.TutorialView)this.mViewFlipper.getChildAt(i)).getContent().getTutorialTypes();
                List<TutorialType> tutorialTypes2;
                for (int j = 0; j < tutorialTypes.size(); ++j, tutorialTypes = tutorialTypes2) {
                    tutorialTypes2 = tutorialTypes;
                    if (tutorialType == tutorialTypes.get(j)) {
                        final int n = j + 1;
                        if (n < tutorialTypes.size()) {
                            return (TutorialType)tutorialTypes.get(n);
                        }
                        final int n2 = i + 1;
                        if (n2 >= this.getTutorialCount()) {
                            return null;
                        }
                        final List<TutorialType> list = tutorialTypes2 = ((TutorialContainerView.TutorialView)this.mViewFlipper.getChildAt(n2)).getContent().getTutorialTypes();
                        if (list.size() > 0) {
                            return (TutorialType)list.get(0);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private ObjectAnimator getSlideInAnimator(final long duration) {
        final PathInterpolator interpolator = new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);
        final int n = (int)this.mContainer.getResources().getDimension(2131165701);
        String s;
        if (this.isPortrait()) {
            s = "translationY";
        }
        else {
            s = "translationX";
        }
        int n2 = n;
        if (this.isPortrait()) {
            n2 = -n;
        }
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)this.mContainer, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat(s, new float[] { (float)n2, 0.0f }) });
        ofPropertyValuesHolder.setDuration(duration);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)interpolator);
        ofPropertyValuesHolder.addListener((Animator$AnimatorListener)new Animator$AnimatorListener(this) {
            final TutorialController this$0;
            
            public void onAnimationCancel(final Animator animator) {
            }
            
            public void onAnimationEnd(final Animator animator) {
                trace("open()  : Tutorial is opened.");
                this.this$0.updateUiOrientation();
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
                if (this.this$0.mContainer.getVisibility() != 0) {
                    this.this$0.mContainer.setVisibility(0);
                }
                this.this$0.setNavigationBarThemeLight(true);
            }
        });
        return ofPropertyValuesHolder;
    }
    
    private int getTutorialCount() {
        int childCount;
        if (this.mViewFlipper == null) {
            childCount = 0;
        }
        else {
            childCount = this.mViewFlipper.getChildCount();
        }
        return childCount;
    }
    
    private TutorialContainerView.TutorialView getTutorialView(final TutorialType tutorialType) {
        final ViewFlipper mViewFlipper = this.mViewFlipper;
        TutorialContainerView.TutorialView tutorialView = null;
        final TutorialContainerView.TutorialView tutorialView2 = null;
        if (mViewFlipper != null) {
            int i = 0;
            tutorialView = tutorialView2;
            while (i < this.getTutorialCount()) {
                tutorialView = (TutorialContainerView.TutorialView)this.mViewFlipper.getChildAt(i);
                if (tutorialView.getTag() == tutorialType) {
                    return tutorialView;
                }
                ++i;
            }
        }
        return tutorialView;
    }
    
    private void hide() {
        trace("hide()");
        this.mContainer.setVisibility(4);
    }
    
    private boolean isPortrait() {
        final int mOrientation = this.mOrientation;
        boolean b = true;
        if (mOrientation != 1) {
            b = false;
        }
        return b;
    }
    
    private void prepareTutorial() {
        this.mContainer = (TutorialContainerView)((ViewStub)this.mRootView.findViewById(2131296674)).inflate().findViewById(2131296675);
        this.mViewFlipper = this.mContainer.getViewFlipper();
        if (this.mViewFlipper == null) {
            this.mViewFlipper = (ViewFlipper)this.mContainer.findViewById(2131296403);
        }
        this.updateFlipperAnimation();
    }
    
    private void release() {
        trace("release()");
        for (int i = 0; i < this.getTutorialCount(); ++i) {
            ((TutorialContainerView.TutorialView)this.mViewFlipper.getChildAt(i)).release();
        }
        this.setNavigationBarThemeLight(false);
        this.mViewFlipper.removeAllViews();
        this.mHandler.removeCallbacks(this.mPostStartAnimationTask);
        this.mPostStartAnimationTask = null;
        this.mPreviousType = null;
        this.mCurrentType = null;
        this.mIsOpened = false;
    }
    
    private boolean setContentToView(final OpenType openType, final StoredSettings storedSettings, final TutorialContentView.OnClickCloseButtonListener onClickCloseButtonListener) {
        final ArrayList list = new ArrayList();
        final ArrayList list2 = new ArrayList();
        final ArrayList list3 = new ArrayList();
        final ArrayList list4 = new ArrayList();
        final ArrayList list5 = new ArrayList();
        final ArrayList list6 = new ArrayList();
        for (final TutorialType tutorialType : openType.tutorialTypes) {
            final TutorialContentView.TutorialContent create = this.mTutorialFactory.create(tutorialType, this.mOrientation);
            if (create.isSimpleTutorialContent()) {
                list5.add(tutorialType);
                list6.add(create);
            }
            else {
                if (!openType.isReadMore && !create.canShowContent(storedSettings)) {
                    continue;
                }
                if (((PagingTutorialContentView.PagingTutorialContent)create).getNavigatorType() == PagingTutorialContentView.TutorialNavigatorType.NORMAL) {
                    list.add(tutorialType);
                    list2.add(create);
                }
                else {
                    list3.add(tutorialType);
                    list4.add(create);
                }
            }
        }
        if (list5.size() == 0 && list.size() == 0 && list3.size() == 0) {
            return false;
        }
        for (int i = 0; i < list5.size(); ++i) {
            this.addContent((TutorialContentView.TutorialContent)list6.get(i), new OnClickCloseButtonListenerImpl((TutorialContentView.TutorialContent)list6.get(i)));
        }
        if (list.size() > 0) {
            final TutorialContentView.TutorialContent create2 = this.mTutorialFactory.create(this.mOrientation, list, list2);
            TutorialContentView.OnClickCloseButtonListener onClickCloseButtonListener2;
            if (onClickCloseButtonListener == null) {
                onClickCloseButtonListener2 = new OnClickCloseButtonListenerImpl(create2);
            }
            else {
                onClickCloseButtonListener2 = onClickCloseButtonListener;
            }
            this.addContent(create2, onClickCloseButtonListener2);
        }
        if (list3.size() > 0) {
            final TutorialContentView.TutorialContent create3 = this.mTutorialFactory.create(this.mOrientation, list3, list4);
            TutorialContentView.OnClickCloseButtonListener onClickCloseButtonListener3;
            if ((onClickCloseButtonListener3 = onClickCloseButtonListener) == null) {
                onClickCloseButtonListener3 = new OnClickCloseButtonListenerImpl(create3);
            }
            this.addContent(create3, onClickCloseButtonListener3);
        }
        if (list5.size() > 0) {
            this.mCurrentType = (TutorialType)list5.get(0);
        }
        else if (list.size() > 0) {
            this.mCurrentType = (TutorialType)list.get(0);
        }
        else if (list3.size() > 0) {
            this.mCurrentType = (TutorialType)list3.get(0);
        }
        return true;
    }
    
    private void setNavigationBarThemeLight(final boolean b) {
        if (this.mWindow != null) {
            final int systemUiVisibility = this.mWindow.getDecorView().getSystemUiVisibility();
            int systemUiVisibility2;
            if (b) {
                systemUiVisibility2 = (systemUiVisibility | 0x10);
            }
            else {
                systemUiVisibility2 = (systemUiVisibility & 0xFFFFFFEF);
            }
            this.mWindow.getDecorView().setSystemUiVisibility(systemUiVisibility2);
        }
    }
    
    private void show() {
        trace("show()");
        this.mContainer.setVisibility(0);
    }
    
    private void startSlideInAnimation() {
        this.show();
        long n;
        if (this.isPortrait()) {
            n = 1000L;
        }
        else {
            n = 700L;
        }
        (this.mAnimator = this.getSlideInAnimator(n)).start();
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    private void updateFlipperAnimation() {
        if (this.mViewFlipper == null) {
            return;
        }
        int n;
        int n2;
        if (this.mOrientation != 1) {
            n = 2130771997;
            n2 = 2130771995;
        }
        else {
            n = 2130771998;
            n2 = 2130771996;
        }
        this.mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this.mContext, n));
        this.mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this.mContext, n2));
    }
    
    private void updateUiOrientation() {
        final StringBuilder sb = new StringBuilder();
        sb.append("updateUiOrientation() E orientation = ");
        sb.append(this.mOrientation);
        trace(sb.toString());
        if (this.mViewFlipper != null) {
            for (int i = 0; i < this.getTutorialCount(); ++i) {
                ((TutorialContainerView.TutorialView)this.mViewFlipper.getChildAt(i)).setUiOrientation(this.mOrientation);
            }
        }
        this.updateFlipperAnimation();
        trace("updateUiOrientation() X");
    }
    
    public boolean backToPreviousPage() {
        if (this.mViewFlipper != null) {
            final ImageView imageView = (ImageView)this.mViewFlipper.findViewById(2131296490);
            if (imageView != null && imageView.isShown()) {
                imageView.callOnClick();
                return true;
            }
        }
        return false;
    }
    
    public void close() {
        trace("close()");
        if (this.getTutorialCount() != 0) {
            this.getFadeOutAnimator(300L).start();
        }
        this.mIsOpened = false;
    }
    
    public void doNextAction(final TutorialType mPreviousType) {
        trace("doNextAction()");
        if (this.canHandleNextAction(mPreviousType)) {
            this.mPreviousType = mPreviousType;
            this.mCurrentType = this.getNextTutorialType(this.mPreviousType);
            if (this.hasNext(mPreviousType)) {
                LocalResearchUtil.getInstance().startSetupWizard(this.mCurrentType, 0);
                this.mViewFlipper.showNext();
            }
            else {
                this.close();
            }
        }
    }
    
    public TutorialType getCurrentType() {
        return this.mCurrentType;
    }
    
    public List<TutorialType> getTutorialTypes() {
        return ((TutorialContainerView.TutorialView)this.mViewFlipper.getCurrentView()).getContent().getTutorialTypes();
    }
    
    public boolean hasNext(final TutorialType tutorialType) {
        if (this.mViewFlipper != null && this.getTutorialCount() > 0) {
            final List<TutorialType> tutorialTypes = ((TutorialContainerView.TutorialView)this.mViewFlipper.getChildAt(this.getTutorialCount() - 1)).getContent().getTutorialTypes();
            if (tutorialTypes.get(tutorialTypes.size() - 1) == tutorialType) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isOpened() {
        return this.mIsOpened;
    }
    
    public boolean open(final OpenType openType, final StoredSettings storedSettings, final TutorialContentView.OnClickCloseButtonListener onClickCloseButtonListener) {
        if (this.isOpened()) {
            trace("open()  :  is already accepted.In the middle of starting to open tutorial...");
            return false;
        }
        if (!this.setContentToView(openType, storedSettings, onClickCloseButtonListener)) {
            return false;
        }
        if (this.getTutorialCount() != 0) {
            trace("open()  :  is requested.");
            this.mIsOpened = true;
            LocalResearchUtil.getInstance().initSetupwizard(openType.isReadMore);
            LocalResearchUtil.getInstance().startSetupWizard(this.mCurrentType, 0);
            this.mPostStartAnimationTask = new Runnable(this) {
                final TutorialController this$0;
                
                @Override
                public void run() {
                    this.this$0.startSlideInAnimation();
                }
            };
            this.mHandler.post(this.mPostStartAnimationTask);
        }
        return true;
    }
    
    public void pause() {
        trace("pause()");
        if (this.getTutorialCount() != 0) {
            this.release();
            this.hide();
        }
    }
    
    public void setOnClickTutorialButtonListener(final OnClickSetupWizardButtonListener mButtonListener) {
        this.mButtonListener = mButtonListener;
    }
    
    public void setSystemUiAccessor(final SystemUiAccessor mSystemUiAccessor) {
        this.mSystemUiAccessor = mSystemUiAccessor;
    }
    
    public void setUiOrientation(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("setUiOrientation() E orientation = ");
        sb.append(n);
        trace(sb.toString());
        if (this.mOrientation != n) {
            this.mOrientation = n;
            if (this.mAnimator != null && this.mAnimator.isRunning()) {
                trace("setUiOrientation() X : Tutorial open animation is running.");
                return;
            }
            this.updateUiOrientation();
        }
        trace("setUiOrientation() X");
    }
    
    public enum DisplayTrigger
    {
        private static final DisplayTrigger[] $VALUES;
        
        CHANGE_MANUAL_FUSION_SETTING, 
        CHANGE_MODE, 
        CHANGE_TO_STANDARD_SLOW_MOTION, 
        CHANGE_TO_SUPER_SLOW_MOTION_SHOT, 
        SETUP_WIZARD;
        
        static {
            $VALUES = new DisplayTrigger[] { DisplayTrigger.SETUP_WIZARD, DisplayTrigger.CHANGE_MODE, DisplayTrigger.CHANGE_TO_SUPER_SLOW_MOTION_SHOT, DisplayTrigger.CHANGE_TO_STANDARD_SLOW_MOTION, DisplayTrigger.CHANGE_MANUAL_FUSION_SETTING };
        }
    }
    
    private class OnClickCloseButtonListenerImpl implements OnClickCloseButtonListener
    {
        private final TutorialContent mTutorialContent;
        final TutorialController this$0;
        
        public OnClickCloseButtonListenerImpl(final TutorialController this$0, final TutorialContent mTutorialContent) {
            this.this$0 = this$0;
            this.mTutorialContent = mTutorialContent;
        }
        
        @Override
        public void onClickCloseButton(final View view) {
            if (!this.this$0.isOpened()) {
                return;
            }
            final TutorialType type = this.mTutorialContent.getCurrentTutorialPageInfo().type;
            final int id = view.getId();
            if (id != 2131296487) {
                if (id != 2131296491) {
                    if (id != 2131296676) {
                        if (id == 2131296684) {
                            LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.YES);
                            LocalResearchUtil.getInstance().closeSetupWizard();
                            if (TutorialController$4.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[type.ordinal()] != 1) {
                                this.this$0.doNextAction(type);
                            }
                            if (this.this$0.mButtonListener != null) {
                                this.this$0.mButtonListener.onAccepted(type);
                                if (type != TutorialType.SAVE_LOCATION && !this.this$0.hasNext(type)) {
                                    this.this$0.mButtonListener.onClose(this.mTutorialContent.getTutorialTypes());
                                }
                            }
                        }
                    }
                    else {
                        LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.NO);
                        LocalResearchUtil.getInstance().closeSetupWizard();
                        this.this$0.doNextAction(type);
                        if (this.this$0.mButtonListener != null) {
                            this.this$0.mButtonListener.onDenied(type);
                            if (!this.this$0.hasNext(type)) {
                                this.this$0.mButtonListener.onClose(this.mTutorialContent.getTutorialTypes());
                            }
                        }
                    }
                }
                else {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.SKIP);
                    LocalResearchUtil.getInstance().closeSetupWizard();
                    this.this$0.close();
                    if (this.this$0.mButtonListener != null) {
                        this.this$0.mButtonListener.onClose(this.mTutorialContent.getTutorialTypes());
                    }
                }
            }
            else {
                LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.GOT_IT);
                LocalResearchUtil.getInstance().closeSetupWizard();
                if (TutorialController$4.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[type.ordinal()] != 2) {
                    this.this$0.doNextAction(type);
                    if (this.this$0.mButtonListener != null) {
                        this.this$0.mButtonListener.onClose(this.mTutorialContent.getTutorialTypes());
                    }
                }
                else if (this.this$0.mButtonListener != null) {
                    this.this$0.mButtonListener.onAccepted(type);
                }
            }
        }
    }
    
    public interface OnClickSetupWizardButtonListener
    {
        void onAccepted(final TutorialType p0);
        
        void onClose(final List<TutorialType> p0);
        
        void onDenied(final TutorialType p0);
    }
    
    public static class OpenType
    {
        public final boolean isReadMore;
        public final List<TutorialType> tutorialTypes;
        
        public OpenType(final List<TutorialType> tutorialTypes, final boolean isReadMore) {
            this.tutorialTypes = tutorialTypes;
            this.isReadMore = isReadMore;
        }
        
        public static OpenType create(final DisplayTrigger displayTrigger) {
            final ArrayList list = new ArrayList();
            switch (TutorialController$4.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$DisplayTrigger[displayTrigger.ordinal()]) {
                case 5: {
                    list.add(TutorialType.MANUAL_FUSION);
                    break;
                }
                case 4: {
                    list.add(TutorialType.STANDARD_SLOW_MOTION);
                    break;
                }
                case 3: {
                    list.add(TutorialType.SUPER_SLOW_MOTION_SHOT);
                    break;
                }
                case 2: {
                    list.add(TutorialType.EYE_GUIDE);
                    list.add(TutorialType.HAND_SHUTTER);
                    list.add(TutorialType.SUPER_SLOW_MOTION_MORE_OPTIONS);
                    list.add(TutorialType.SUPER_SLOW_MOTION_SHOT);
                    list.add(TutorialType.STANDARD_SLOW_MOTION);
                    list.add(TutorialType.VIDEO_FUSION);
                    break;
                }
                case 1: {
                    list.add(TutorialType.SAVE_LOCATION);
                    list.add(TutorialType.PREDICTIVE_LAUNCH);
                    list.add(TutorialType.DUAL_CAMERA);
                    list.add(TutorialType.SIDE_SENSE);
                    break;
                }
            }
            return new OpenType(list, false);
        }
        
        public static OpenType createByReadMore(final TutorialType tutorialType) {
            final ArrayList list = new ArrayList();
            list.add(tutorialType);
            return new OpenType(list, true);
        }
    }
    
    public interface SystemUiAccessor
    {
        void onAddFlags(final int p0);
        
        void onClearFlags(final int p0);
    }
    
    public enum TutorialType
    {
        private static final TutorialType[] $VALUES;
        
        DUAL_CAMERA(new MessageType[] { MessageType.NO_MESSAGE }), 
        EYE_GUIDE(new MessageType[] { MessageType.TUTORIAL_EYE_GUIDE }), 
        HAND_SHUTTER(new MessageType[] { MessageType.TUTORIAL_HAND_SHUTTER }), 
        MANUAL_FUSION(new MessageType[] { MessageType.TUTORIAL_MANUAL_FUSION }), 
        PREDICTIVE_LAUNCH(new MessageType[] { MessageType.NO_MESSAGE }), 
        SAVE_LOCATION(new MessageType[] { MessageType.NO_MESSAGE }), 
        SIDE_SENSE(new MessageType[] { MessageType.NO_MESSAGE }), 
        STANDARD_SLOW_MOTION(new MessageType[] { MessageType.TUTORIAL_STANDARD_SLOW_MOTION }), 
        SUPER_SLOW_MOTION(new MessageType[] { MessageType.NO_MESSAGE }), 
        SUPER_SLOW_MOTION_MORE_OPTIONS(new MessageType[] { MessageType.TUTORIAL_SUPER_SLOW_MOTION }), 
        SUPER_SLOW_MOTION_SHOT(new MessageType[] { MessageType.TUTORIAL_SUPER_SLOW_MOTION_SHOT }), 
        VIDEO_FUSION(new MessageType[] { MessageType.TUTORIAL_VIDEO_FUSION });
        
        public final List<MessageType> messageTypes;
        
        static {
            $VALUES = new TutorialType[] { TutorialType.SAVE_LOCATION, TutorialType.PREDICTIVE_LAUNCH, TutorialType.DUAL_CAMERA, TutorialType.EYE_GUIDE, TutorialType.HAND_SHUTTER, TutorialType.SUPER_SLOW_MOTION_MORE_OPTIONS, TutorialType.SUPER_SLOW_MOTION, TutorialType.SUPER_SLOW_MOTION_SHOT, TutorialType.STANDARD_SLOW_MOTION, TutorialType.MANUAL_FUSION, TutorialType.VIDEO_FUSION, TutorialType.SIDE_SENSE };
        }
        
        private TutorialType(final MessageType[] a) {
            this.messageTypes = new ArrayList<MessageType>(Arrays.asList(a));
        }
    }
}
