package com.sonyericsson.android.camera.view.tutorial;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.android.camera.setting.StoredSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView;
import com.sonyericsson.android.camera.view.tutorial.TutorialContainerView;
import com.sonyericsson.android.camera.view.tutorial.TutorialContentView;
import com.sonymobile.cameracommon.research.parameters.Event;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TutorialController {
    private static final long FADE_OUT_ANIMATION_DURATION_MILLIS = 300;
    private static final long SLIDE_IN_ANIMATION_DURATION_LAND_MILLIS = 700;
    private static final long SLIDE_IN_ANIMATION_DURATION_PORT_MILLIS = 1000;
    private static final String TAG = "TutorialController";
    private static final boolean TRACE = true;
    private ObjectAnimator mAnimator;
    private OnClickSetupWizardButtonListener mButtonListener;
    private TutorialContainerView mContainer;
    private final Context mContext;
    private TutorialType mCurrentType;
    private Runnable mPostStartAnimationTask;
    private TutorialType mPreviousType;
    private final ViewGroup mRootView;
    private SystemUiAccessor mSystemUiAccessor;
    private ViewFlipper mViewFlipper;
    private Window mWindow;
    private boolean mIsOpened = false;
    private int mOrientation = 0;
    private final TutorialFactory mTutorialFactory = new TutorialFactory();
    private final Handler mHandler = new Handler();

    public enum DisplayTrigger {
        SETUP_WIZARD,
        CHANGE_MODE,
        CHANGE_TO_SUPER_SLOW_MOTION_SHOT,
        CHANGE_TO_STANDARD_SLOW_MOTION,
        CHANGE_MANUAL_FUSION_SETTING
    }

    public interface OnClickSetupWizardButtonListener {
        void onAccepted(TutorialType tutorialType);

        void onClose(List<TutorialType> list);

        void onDenied(TutorialType tutorialType);
    }

    public interface SystemUiAccessor {
        void onAddFlags(int i);

        void onClearFlags(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void trace(String str) {
        CamLog.d(str);
    }

    public enum TutorialType {
        SAVE_LOCATION(MessageType.NO_MESSAGE),
        PREDICTIVE_LAUNCH(MessageType.NO_MESSAGE),
        DUAL_CAMERA(MessageType.NO_MESSAGE),
        EYE_GUIDE(MessageType.TUTORIAL_EYE_GUIDE),
        HAND_SHUTTER(MessageType.TUTORIAL_HAND_SHUTTER),
        SUPER_SLOW_MOTION_MORE_OPTIONS(MessageType.TUTORIAL_SUPER_SLOW_MOTION),
        SUPER_SLOW_MOTION(MessageType.NO_MESSAGE),
        SUPER_SLOW_MOTION_SHOT(MessageType.TUTORIAL_SUPER_SLOW_MOTION_SHOT),
        STANDARD_SLOW_MOTION(MessageType.TUTORIAL_STANDARD_SLOW_MOTION),
        MANUAL_FUSION(MessageType.TUTORIAL_MANUAL_FUSION),
        VIDEO_FUSION(MessageType.TUTORIAL_VIDEO_FUSION),
        SIDE_SENSE(MessageType.NO_MESSAGE);

        public final List<MessageType> messageTypes;

        TutorialType(MessageType... messageTypeArr) {
            this.messageTypes = new ArrayList(Arrays.asList(messageTypeArr));
        }
    }

    public static class OpenType {
        public final boolean isReadMore;
        public final List<TutorialType> tutorialTypes;

        public OpenType(List<TutorialType> list, boolean z) {
            this.tutorialTypes = list;
            this.isReadMore = z;
        }

        public static OpenType create(DisplayTrigger displayTrigger) {
            ArrayList arrayList = new ArrayList();
            switch (displayTrigger) {
                case SETUP_WIZARD:
                    arrayList.add(TutorialType.SAVE_LOCATION);
                    arrayList.add(TutorialType.PREDICTIVE_LAUNCH);
                    arrayList.add(TutorialType.DUAL_CAMERA);
                    arrayList.add(TutorialType.SIDE_SENSE);
                    break;
                case CHANGE_MODE:
                    arrayList.add(TutorialType.EYE_GUIDE);
                    arrayList.add(TutorialType.HAND_SHUTTER);
                    arrayList.add(TutorialType.SUPER_SLOW_MOTION_MORE_OPTIONS);
                    arrayList.add(TutorialType.SUPER_SLOW_MOTION_SHOT);
                    arrayList.add(TutorialType.STANDARD_SLOW_MOTION);
                    arrayList.add(TutorialType.VIDEO_FUSION);
                    break;
                case CHANGE_TO_SUPER_SLOW_MOTION_SHOT:
                    arrayList.add(TutorialType.SUPER_SLOW_MOTION_SHOT);
                    break;
                case CHANGE_TO_STANDARD_SLOW_MOTION:
                    arrayList.add(TutorialType.STANDARD_SLOW_MOTION);
                    break;
                case CHANGE_MANUAL_FUSION_SETTING:
                    arrayList.add(TutorialType.MANUAL_FUSION);
                    break;
            }
            return new OpenType(arrayList, false);
        }

        public static OpenType createByReadMore(TutorialType tutorialType) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(tutorialType);
            return new OpenType(arrayList, true);
        }
    }

    public TutorialController(ViewGroup viewGroup, Window window) throws Resources.NotFoundException {
        this.mRootView = viewGroup;
        this.mContext = viewGroup.getContext();
        this.mWindow = window;
        prepareTutorial();
    }

    private void prepareTutorial() throws Resources.NotFoundException {
        this.mContainer = (TutorialContainerView) ((ViewStub) this.mRootView.findViewById(R.id.tutorial_container_stub)).inflate().findViewById(R.id.tutorial_container_view);
        this.mViewFlipper = this.mContainer.getViewFlipper();
        if (this.mViewFlipper == null) {
            this.mViewFlipper = (ViewFlipper) this.mContainer.findViewById(R.id.flipper_view);
        }
        updateFlipperAnimation();
    }

    public boolean open(OpenType openType, StoredSettings storedSettings, TutorialContentView.OnClickCloseButtonListener onClickCloseButtonListener) {
        if (isOpened()) {
            trace("open()  :  is already accepted.In the middle of starting to open tutorial...");
            return false;
        }
        if (!setContentToView(openType, storedSettings, onClickCloseButtonListener)) {
            return false;
        }
        if (getTutorialCount() != 0) {
            trace("open()  :  is requested.");
            this.mIsOpened = true;
            LocalResearchUtil.getInstance().initSetupwizard(openType.isReadMore);
            LocalResearchUtil.getInstance().startSetupWizard(this.mCurrentType, 0);
            this.mPostStartAnimationTask = new Runnable() { // from class: com.sonyericsson.android.camera.view.tutorial.TutorialController.1
                @Override // java.lang.Runnable
                public void run() {
                    TutorialController.this.startSlideInAnimation();
                }
            };
            this.mHandler.post(this.mPostStartAnimationTask);
        }
        return true;
    }

    private boolean setContentToView(OpenType openType, StoredSettings storedSettings, TutorialContentView.OnClickCloseButtonListener onClickCloseButtonListener) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        for (TutorialType tutorialType : openType.tutorialTypes) {
            TutorialContentView.TutorialContent tutorialContentCreate = this.mTutorialFactory.create(tutorialType, this.mOrientation);
            if (tutorialContentCreate.isSimpleTutorialContent()) {
                arrayList5.add(tutorialType);
                arrayList6.add(tutorialContentCreate);
            } else if (openType.isReadMore || tutorialContentCreate.canShowContent(storedSettings)) {
                if (((PagingTutorialContentView.PagingTutorialContent) tutorialContentCreate).getNavigatorType() == PagingTutorialContentView.TutorialNavigatorType.NORMAL) {
                    arrayList.add(tutorialType);
                    arrayList2.add(tutorialContentCreate);
                } else {
                    arrayList3.add(tutorialType);
                    arrayList4.add(tutorialContentCreate);
                }
            }
        }
        if (arrayList5.size() == 0 && arrayList.size() == 0 && arrayList3.size() == 0) {
            return false;
        }
        for (int i = 0; i < arrayList5.size(); i++) {
            addContent((TutorialContentView.TutorialContent) arrayList6.get(i), new OnClickCloseButtonListenerImpl((TutorialContentView.TutorialContent) arrayList6.get(i)));
        }
        if (arrayList.size() > 0) {
            TutorialContentView.TutorialContent tutorialContentCreate2 = this.mTutorialFactory.create(this.mOrientation, arrayList, arrayList2);
            addContent(tutorialContentCreate2, onClickCloseButtonListener == null ? new OnClickCloseButtonListenerImpl(tutorialContentCreate2) : onClickCloseButtonListener);
        }
        if (arrayList3.size() > 0) {
            TutorialContentView.TutorialContent tutorialContentCreate3 = this.mTutorialFactory.create(this.mOrientation, arrayList3, arrayList4);
            if (onClickCloseButtonListener == null) {
                onClickCloseButtonListener = new OnClickCloseButtonListenerImpl(tutorialContentCreate3);
            }
            addContent(tutorialContentCreate3, onClickCloseButtonListener);
        }
        if (arrayList5.size() > 0) {
            this.mCurrentType = (TutorialType) arrayList5.get(0);
            return true;
        }
        if (arrayList.size() > 0) {
            this.mCurrentType = (TutorialType) arrayList.get(0);
            return true;
        }
        if (arrayList3.size() <= 0) {
            return true;
        }
        this.mCurrentType = (TutorialType) arrayList3.get(0);
        return true;
    }

    private void addContent(TutorialContentView.TutorialContent tutorialContent, TutorialContentView.OnClickCloseButtonListener onClickCloseButtonListener) {
        TutorialContainerView.TutorialView tutorialView = new TutorialContainerView.TutorialView(this.mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 3;
        tutorialView.setLayoutParams(layoutParams);
        this.mViewFlipper.addView(tutorialView);
        tutorialView.setContent(tutorialContent);
        tutorialView.setOnClickCloseButtonListener(onClickCloseButtonListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSlideInAnimation() {
        show();
        this.mAnimator = getSlideInAnimator(isPortrait() ? 1000L : SLIDE_IN_ANIMATION_DURATION_LAND_MILLIS);
        this.mAnimator.start();
    }

    public void pause() {
        trace("pause()");
        if (getTutorialCount() != 0) {
            release();
            hide();
        }
    }

    public void close() {
        trace("close()");
        if (getTutorialCount() != 0) {
            getFadeOutAnimator(FADE_OUT_ANIMATION_DURATION_MILLIS).start();
        }
        this.mIsOpened = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void release() {
        trace("release()");
        for (int i = 0; i < getTutorialCount(); i++) {
            ((TutorialContainerView.TutorialView) this.mViewFlipper.getChildAt(i)).release();
        }
        setNavigationBarThemeLight(false);
        this.mViewFlipper.removeAllViews();
        this.mHandler.removeCallbacks(this.mPostStartAnimationTask);
        this.mPostStartAnimationTask = null;
        this.mPreviousType = null;
        this.mCurrentType = null;
        this.mIsOpened = false;
    }

    private boolean isPortrait() {
        return this.mOrientation == 1;
    }

    private void show() {
        trace("show()");
        this.mContainer.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hide() {
        trace("hide()");
        this.mContainer.setVisibility(4);
    }

    public boolean isOpened() {
        return this.mIsOpened;
    }

    public void setUiOrientation(int i) throws Resources.NotFoundException {
        trace("setUiOrientation() E orientation = " + i);
        if (this.mOrientation != i) {
            this.mOrientation = i;
            if (this.mAnimator != null && this.mAnimator.isRunning()) {
                trace("setUiOrientation() X : Tutorial open animation is running.");
                return;
            }
            updateUiOrientation();
        }
        trace("setUiOrientation() X");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUiOrientation() throws Resources.NotFoundException {
        trace("updateUiOrientation() E orientation = " + this.mOrientation);
        if (this.mViewFlipper != null) {
            for (int i = 0; i < getTutorialCount(); i++) {
                ((TutorialContainerView.TutorialView) this.mViewFlipper.getChildAt(i)).setUiOrientation(this.mOrientation);
            }
        }
        updateFlipperAnimation();
        trace("updateUiOrientation() X");
    }

    private int getTutorialCount() {
        if (this.mViewFlipper == null) {
            return 0;
        }
        return this.mViewFlipper.getChildCount();
    }

    public void doNextAction(TutorialType tutorialType) {
        trace("doNextAction()");
        if (canHandleNextAction(tutorialType)) {
            this.mPreviousType = tutorialType;
            this.mCurrentType = getNextTutorialType(this.mPreviousType);
            if (hasNext(tutorialType)) {
                LocalResearchUtil.getInstance().startSetupWizard(this.mCurrentType, 0);
                this.mViewFlipper.showNext();
            } else {
                close();
            }
        }
    }

    public TutorialType getCurrentType() {
        return this.mCurrentType;
    }

    public List<TutorialType> getTutorialTypes() {
        return ((TutorialContainerView.TutorialView) this.mViewFlipper.getCurrentView()).getContent().getTutorialTypes();
    }

    private TutorialType getNextTutorialType(TutorialType tutorialType) {
        if (this.mViewFlipper != null && getTutorialCount() > 0) {
            for (int i = 0; i < getTutorialCount(); i++) {
                List<TutorialType> tutorialTypes = ((TutorialContainerView.TutorialView) this.mViewFlipper.getChildAt(i)).getContent().getTutorialTypes();
                for (int i2 = 0; i2 < tutorialTypes.size(); i2++) {
                    if (tutorialType == tutorialTypes.get(i2)) {
                        int i3 = i2 + 1;
                        if (i3 < tutorialTypes.size()) {
                            return tutorialTypes.get(i3);
                        }
                        int i4 = i + 1;
                        if (i4 >= getTutorialCount()) {
                            return null;
                        }
                        tutorialTypes = ((TutorialContainerView.TutorialView) this.mViewFlipper.getChildAt(i4)).getContent().getTutorialTypes();
                        if (tutorialTypes.size() > 0) {
                            return tutorialTypes.get(0);
                        }
                    }
                }
            }
        }
        return null;
    }

    private TutorialContainerView.TutorialView getTutorialView(TutorialType tutorialType) {
        TutorialContainerView.TutorialView tutorialView = null;
        if (this.mViewFlipper != null) {
            for (int i = 0; i < getTutorialCount(); i++) {
                tutorialView = (TutorialContainerView.TutorialView) this.mViewFlipper.getChildAt(i);
                if (tutorialView.getTag() == tutorialType) {
                    return tutorialView;
                }
            }
        }
        return tutorialView;
    }

    private boolean canHandleNextAction(TutorialType tutorialType) {
        return this.mPreviousType != tutorialType;
    }

    public boolean hasNext(TutorialType tutorialType) {
        if (this.mViewFlipper != null && getTutorialCount() > 0) {
            List<TutorialType> tutorialTypes = ((TutorialContainerView.TutorialView) this.mViewFlipper.getChildAt(getTutorialCount() - 1)).getContent().getTutorialTypes();
            if (tutorialTypes.get(tutorialTypes.size() - 1) == tutorialType) {
                return false;
            }
        }
        return true;
    }

    private void updateFlipperAnimation() throws Resources.NotFoundException {
        int i;
        int i2;
        if (this.mViewFlipper == null) {
            return;
        }
        if (this.mOrientation != 1) {
            i = R.anim.setup_wizard_slide_out_landscape;
            i2 = R.anim.setup_wizard_slide_in_landscape;
        } else {
            i = R.anim.setup_wizard_slide_out_portrait;
            i2 = R.anim.setup_wizard_slide_in_portrait;
        }
        this.mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this.mContext, i));
        this.mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this.mContext, i2));
    }

    private ObjectAnimator getSlideInAnimator(long j) {
        PathInterpolator pathInterpolator = new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);
        int dimension = (int) this.mContainer.getResources().getDimension(R.dimen.tutorial_port_height);
        String str = isPortrait() ? "translationY" : "translationX";
        float[] fArr = new float[2];
        if (isPortrait()) {
            dimension = -dimension;
        }
        fArr[0] = dimension;
        fArr[1] = 0.0f;
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mContainer, PropertyValuesHolder.ofFloat(str, fArr));
        objectAnimatorOfPropertyValuesHolder.setDuration(j);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(pathInterpolator);
        objectAnimatorOfPropertyValuesHolder.addListener(new Animator.AnimatorListener() { // from class: com.sonyericsson.android.camera.view.tutorial.TutorialController.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                if (TutorialController.this.mContainer.getVisibility() != 0) {
                    TutorialController.this.mContainer.setVisibility(0);
                }
                TutorialController.this.setNavigationBarThemeLight(true);
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) throws Resources.NotFoundException {
                TutorialController.trace("open()  : Tutorial is opened.");
                TutorialController.this.updateUiOrientation();
            }
        });
        return objectAnimatorOfPropertyValuesHolder;
    }

    private ObjectAnimator getFadeOutAnimator(long j) {
        PathInterpolator pathInterpolator = new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mContainer, PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f));
        objectAnimatorOfPropertyValuesHolder.setDuration(j);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(pathInterpolator);
        objectAnimatorOfPropertyValuesHolder.addListener(new Animator.AnimatorListener() { // from class: com.sonyericsson.android.camera.view.tutorial.TutorialController.3
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                TutorialController.this.release();
                TutorialController.this.hide();
                TutorialController.this.mContainer.setAlpha(1.0f);
                TutorialController.trace("close() : Tutorial is closed.");
            }
        });
        return objectAnimatorOfPropertyValuesHolder;
    }

    private class OnClickCloseButtonListenerImpl implements TutorialContentView.OnClickCloseButtonListener {
        private final TutorialContentView.TutorialContent mTutorialContent;

        public OnClickCloseButtonListenerImpl(TutorialContentView.TutorialContent tutorialContent) {
            this.mTutorialContent = tutorialContent;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.OnClickCloseButtonListener
        public void onClickCloseButton(View view) {
            if (TutorialController.this.isOpened()) {
                TutorialType tutorialType = this.mTutorialContent.getCurrentTutorialPageInfo().type;
                int id = view.getId();
                if (id == 2131296487) {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.GOT_IT);
                    LocalResearchUtil.getInstance().closeSetupWizard();
                    if (AnonymousClass4.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()] == 2) {
                        if (TutorialController.this.mButtonListener != null) {
                            TutorialController.this.mButtonListener.onAccepted(tutorialType);
                            return;
                        }
                        return;
                    } else {
                        TutorialController.this.doNextAction(tutorialType);
                        if (TutorialController.this.mButtonListener != null) {
                            TutorialController.this.mButtonListener.onClose(this.mTutorialContent.getTutorialTypes());
                            return;
                        }
                        return;
                    }
                }
                if (id == 2131296491) {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.SKIP);
                    LocalResearchUtil.getInstance().closeSetupWizard();
                    TutorialController.this.close();
                    if (TutorialController.this.mButtonListener != null) {
                        TutorialController.this.mButtonListener.onClose(this.mTutorialContent.getTutorialTypes());
                        return;
                    }
                    return;
                }
                if (id == 2131296676) {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.NO);
                    LocalResearchUtil.getInstance().closeSetupWizard();
                    TutorialController.this.doNextAction(tutorialType);
                    if (TutorialController.this.mButtonListener != null) {
                        TutorialController.this.mButtonListener.onDenied(tutorialType);
                        if (TutorialController.this.hasNext(tutorialType)) {
                            return;
                        }
                        TutorialController.this.mButtonListener.onClose(this.mTutorialContent.getTutorialTypes());
                        return;
                    }
                    return;
                }
                if (id != 2131296684) {
                    return;
                }
                LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.YES);
                LocalResearchUtil.getInstance().closeSetupWizard();
                if (AnonymousClass4.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()] != 1) {
                    TutorialController.this.doNextAction(tutorialType);
                }
                if (TutorialController.this.mButtonListener != null) {
                    TutorialController.this.mButtonListener.onAccepted(tutorialType);
                    if (tutorialType == TutorialType.SAVE_LOCATION || TutorialController.this.hasNext(tutorialType)) {
                        return;
                    }
                    TutorialController.this.mButtonListener.onClose(this.mTutorialContent.getTutorialTypes());
                }
            }
        }
    }

    /* renamed from: com.sonyericsson.android.camera.view.tutorial.TutorialController$4, reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType = new int[TutorialType.values().length];

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialType.SAVE_LOCATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialType.SIDE_SENSE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$DisplayTrigger = new int[DisplayTrigger.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$DisplayTrigger[DisplayTrigger.SETUP_WIZARD.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$DisplayTrigger[DisplayTrigger.CHANGE_MODE.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$DisplayTrigger[DisplayTrigger.CHANGE_TO_SUPER_SLOW_MOTION_SHOT.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$DisplayTrigger[DisplayTrigger.CHANGE_TO_STANDARD_SLOW_MOTION.ordinal()] = 4;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$DisplayTrigger[DisplayTrigger.CHANGE_MANUAL_FUSION_SETTING.ordinal()] = 5;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public void setOnClickTutorialButtonListener(OnClickSetupWizardButtonListener onClickSetupWizardButtonListener) {
        this.mButtonListener = onClickSetupWizardButtonListener;
    }

    public void setSystemUiAccessor(SystemUiAccessor systemUiAccessor) {
        this.mSystemUiAccessor = systemUiAccessor;
    }

    private void addFlags(int i) {
        if (this.mSystemUiAccessor != null) {
            this.mSystemUiAccessor.onAddFlags(i);
        }
    }

    private void clearFlags(int i) {
        if (this.mSystemUiAccessor != null) {
            this.mSystemUiAccessor.onClearFlags(i);
        }
    }

    public boolean backToPreviousPage() {
        ImageView imageView;
        if (this.mViewFlipper == null || (imageView = (ImageView) this.mViewFlipper.findViewById(R.id.page_tutorial_prev_icon_button)) == null || !imageView.isShown()) {
            return false;
        }
        imageView.callOnClick();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setNavigationBarThemeLight(boolean z) {
        if (this.mWindow != null) {
            int systemUiVisibility = this.mWindow.getDecorView().getSystemUiVisibility();
            this.mWindow.getDecorView().setSystemUiVisibility(z ? systemUiVisibility | 16 : systemUiVisibility & (-17));
        }
    }
}
