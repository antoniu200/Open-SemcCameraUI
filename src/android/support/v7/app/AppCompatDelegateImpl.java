package android.support.v7.app;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.NavUtils;
import android.support.v4.view.KeyEventDispatcher;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.FitWindowsViewGroup;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.sonyericsson.android.camera.view.modeselector.ResourceUtil;
import java.lang.Thread;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.Callback, LayoutInflater.Factory2 {
    private static final boolean DEBUG = false;
    static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
    private static final boolean IS_PRE_LOLLIPOP;
    private static final String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";
    private static boolean sInstalledExceptionHandler;
    private static final int[] sWindowBackgroundStyleable;
    ActionBar mActionBar;
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    ActionMode mActionMode;
    PopupWindow mActionModePopup;
    ActionBarContextView mActionModeView;
    final AppCompatCallback mAppCompatCallback;
    private AppCompatViewInflater mAppCompatViewInflater;
    final Window.Callback mAppCompatWindowCallback;
    private boolean mApplyDayNightCalled;
    private AutoNightModeManager mAutoNightModeManager;
    private boolean mClosingActionMenu;
    final Context mContext;
    private DecorContentParent mDecorContentParent;
    private boolean mEnableDefaultActionBarUp;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    boolean mHasActionBar;
    int mInvalidatePanelMenuFeatures;
    boolean mInvalidatePanelMenuPosted;
    boolean mIsDestroyed;
    boolean mIsFloating;
    private boolean mLongPressBackDown;
    MenuInflater mMenuInflater;
    final Window.Callback mOriginalWindowCallback;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    Runnable mShowActionModePopup;
    private View mStatusGuard;
    private ViewGroup mSubDecor;
    private boolean mSubDecorInstalled;
    private Rect mTempRect1;
    private Rect mTempRect2;
    private CharSequence mTitle;
    private TextView mTitleView;
    final Window mWindow;
    boolean mWindowNoTitle;
    ViewPropertyAnimatorCompat mFadeAnim = null;
    private boolean mHandleNativeActionModes = true;
    private int mLocalNightMode = -100;
    private final Runnable mInvalidatePanelMenuRunnable = new Runnable() { // from class: android.support.v7.app.AppCompatDelegateImpl.2
        @Override // java.lang.Runnable
        public void run() {
            if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 1) != 0) {
                AppCompatDelegateImpl.this.doInvalidatePanelMenu(0);
            }
            if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 4096) != 0) {
                AppCompatDelegateImpl.this.doInvalidatePanelMenu(108);
            }
            AppCompatDelegateImpl.this.mInvalidatePanelMenuPosted = false;
            AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures = 0;
        }
    };

    void onSubDecorInstalled(ViewGroup viewGroup) {
    }

    static {
        IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
        sWindowBackgroundStyleable = new int[]{R.attr.windowBackground};
        if (!IS_PRE_LOLLIPOP || sInstalledExceptionHandler) {
            return;
        }
        final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: android.support.v7.app.AppCompatDelegateImpl.1
            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(Thread thread, Throwable th) {
                if (shouldWrapException(th)) {
                    Resources.NotFoundException notFoundException = new Resources.NotFoundException(th.getMessage() + AppCompatDelegateImpl.EXCEPTION_HANDLER_MESSAGE_SUFFIX);
                    notFoundException.initCause(th.getCause());
                    notFoundException.setStackTrace(th.getStackTrace());
                    defaultUncaughtExceptionHandler.uncaughtException(thread, notFoundException);
                    return;
                }
                defaultUncaughtExceptionHandler.uncaughtException(thread, th);
            }

            private boolean shouldWrapException(Throwable th) {
                String message;
                if (!(th instanceof Resources.NotFoundException) || (message = th.getMessage()) == null) {
                    return false;
                }
                return message.contains(ResourceUtil.RES_TYPE_NAME_DRAWABLE) || message.contains("Drawable");
            }
        });
        sInstalledExceptionHandler = true;
    }

    AppCompatDelegateImpl(Context context, Window window, AppCompatCallback appCompatCallback) {
        this.mContext = context;
        this.mWindow = window;
        this.mAppCompatCallback = appCompatCallback;
        this.mOriginalWindowCallback = this.mWindow.getCallback();
        if (this.mOriginalWindowCallback instanceof AppCompatWindowCallback) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        this.mAppCompatWindowCallback = new AppCompatWindowCallback(this.mOriginalWindowCallback);
        this.mWindow.setCallback(this.mAppCompatWindowCallback);
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, (AttributeSet) null, sWindowBackgroundStyleable);
        Drawable drawableIfKnown = tintTypedArrayObtainStyledAttributes.getDrawableIfKnown(0);
        if (drawableIfKnown != null) {
            this.mWindow.setBackgroundDrawable(drawableIfKnown);
        }
        tintTypedArrayObtainStyledAttributes.recycle();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onCreate(Bundle bundle) {
        if (this.mOriginalWindowCallback instanceof Activity) {
            String parentActivityName = null;
            try {
                parentActivityName = NavUtils.getParentActivityName((Activity) this.mOriginalWindowCallback);
            } catch (IllegalArgumentException unused) {
            }
            if (parentActivityName != null) {
                ActionBar actionBarPeekSupportActionBar = peekSupportActionBar();
                if (actionBarPeekSupportActionBar == null) {
                    this.mEnableDefaultActionBarUp = true;
                } else {
                    actionBarPeekSupportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
        }
        if (bundle == null || this.mLocalNightMode != -100) {
            return;
        }
        this.mLocalNightMode = bundle.getInt(KEY_LOCAL_NIGHT_MODE, -100);
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onPostCreate(Bundle bundle) {
        ensureSubDecor();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public ActionBar getSupportActionBar() {
        initWindowDecorActionBar();
        return this.mActionBar;
    }

    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }

    final Window.Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }

    private void initWindowDecorActionBar() {
        ensureSubDecor();
        if (this.mHasActionBar && this.mActionBar == null) {
            if (this.mOriginalWindowCallback instanceof Activity) {
                this.mActionBar = new WindowDecorActionBar((Activity) this.mOriginalWindowCallback, this.mOverlayActionBar);
            } else if (this.mOriginalWindowCallback instanceof Dialog) {
                this.mActionBar = new WindowDecorActionBar((Dialog) this.mOriginalWindowCallback);
            }
            if (this.mActionBar != null) {
                this.mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            }
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setSupportActionBar(Toolbar toolbar) {
        if (this.mOriginalWindowCallback instanceof Activity) {
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar instanceof WindowDecorActionBar) {
                throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
            }
            this.mMenuInflater = null;
            if (supportActionBar != null) {
                supportActionBar.onDestroy();
            }
            if (toolbar != null) {
                ToolbarActionBar toolbarActionBar = new ToolbarActionBar(toolbar, ((Activity) this.mOriginalWindowCallback).getTitle(), this.mAppCompatWindowCallback);
                this.mActionBar = toolbarActionBar;
                this.mWindow.setCallback(toolbarActionBar.getWrappedWindowCallback());
            } else {
                this.mActionBar = null;
                this.mWindow.setCallback(this.mAppCompatWindowCallback);
            }
            invalidateOptionsMenu();
        }
    }

    final Context getActionBarThemedContext() {
        ActionBar supportActionBar = getSupportActionBar();
        Context themedContext = supportActionBar != null ? supportActionBar.getThemedContext() : null;
        return themedContext == null ? this.mContext : themedContext;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            initWindowDecorActionBar();
            this.mMenuInflater = new SupportMenuInflater(this.mActionBar != null ? this.mActionBar.getThemedContext() : this.mContext);
        }
        return this.mMenuInflater;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    @Nullable
    public <T extends View> T findViewById(@IdRes int i) {
        ensureSubDecor();
        return (T) this.mWindow.findViewById(i);
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onConfigurationChanged(Configuration configuration) {
        ActionBar supportActionBar;
        if (this.mHasActionBar && this.mSubDecorInstalled && (supportActionBar = getSupportActionBar()) != null) {
            supportActionBar.onConfigurationChanged(configuration);
        }
        AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
        applyDayNight();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onStart() {
        applyDayNight();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onStop() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(false);
        }
        if (this.mAutoNightModeManager != null) {
            this.mAutoNightModeManager.cleanup();
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onPostResume() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(true);
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setContentView(View view) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setContentView(int i) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(i, viewGroup);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ensureSubDecor();
        ((ViewGroup) this.mSubDecor.findViewById(R.id.content)).addView(view, layoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onSaveInstanceState(Bundle bundle) {
        if (this.mLocalNightMode != -100) {
            bundle.putInt(KEY_LOCAL_NIGHT_MODE, this.mLocalNightMode);
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onDestroy() {
        if (this.mInvalidatePanelMenuPosted) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
        }
        this.mIsDestroyed = true;
        if (this.mActionBar != null) {
            this.mActionBar.onDestroy();
        }
        if (this.mAutoNightModeManager != null) {
            this.mAutoNightModeManager.cleanup();
        }
    }

    private void ensureSubDecor() {
        if (this.mSubDecorInstalled) {
            return;
        }
        this.mSubDecor = createSubDecor();
        CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            if (this.mDecorContentParent != null) {
                this.mDecorContentParent.setWindowTitle(title);
            } else if (peekSupportActionBar() != null) {
                peekSupportActionBar().setWindowTitle(title);
            } else if (this.mTitleView != null) {
                this.mTitleView.setText(title);
            }
        }
        applyFixedSizeWindow();
        onSubDecorInstalled(this.mSubDecor);
        this.mSubDecorInstalled = true;
        PanelFeatureState panelState = getPanelState(0, false);
        if (this.mIsDestroyed) {
            return;
        }
        if (panelState == null || panelState.menu == null) {
            invalidatePanelMenu(108);
        }
    }

    private ViewGroup createSubDecor() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ViewGroup viewGroup;
        Context contextThemeWrapper;
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(android.support.v7.appcompat.R.styleable.AppCompatTheme);
        if (!typedArrayObtainStyledAttributes.hasValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowActionBar)) {
            typedArrayObtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
        if (typedArrayObtainStyledAttributes.getBoolean(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowNoTitle, false)) {
            requestWindowFeature(1);
        } else if (typedArrayObtainStyledAttributes.getBoolean(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowActionBar, false)) {
            requestWindowFeature(108);
        }
        if (typedArrayObtainStyledAttributes.getBoolean(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            requestWindowFeature(109);
        }
        if (typedArrayObtainStyledAttributes.getBoolean(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            requestWindowFeature(10);
        }
        this.mIsFloating = typedArrayObtainStyledAttributes.getBoolean(android.support.v7.appcompat.R.styleable.AppCompatTheme_android_windowIsFloating, false);
        typedArrayObtainStyledAttributes.recycle();
        this.mWindow.getDecorView();
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.mContext);
        if (!this.mWindowNoTitle) {
            if (this.mIsFloating) {
                viewGroup = (ViewGroup) layoutInflaterFrom.inflate(android.support.v7.appcompat.R.layout.abc_dialog_title_material, (ViewGroup) null);
                this.mOverlayActionBar = false;
                this.mHasActionBar = false;
            } else if (this.mHasActionBar) {
                TypedValue typedValue = new TypedValue();
                this.mContext.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarTheme, typedValue, true);
                if (typedValue.resourceId != 0) {
                    contextThemeWrapper = new ContextThemeWrapper(this.mContext, typedValue.resourceId);
                } else {
                    contextThemeWrapper = this.mContext;
                }
                viewGroup = (ViewGroup) LayoutInflater.from(contextThemeWrapper).inflate(android.support.v7.appcompat.R.layout.abc_screen_toolbar, (ViewGroup) null);
                this.mDecorContentParent = (DecorContentParent) viewGroup.findViewById(android.support.v7.appcompat.R.id.decor_content_parent);
                this.mDecorContentParent.setWindowCallback(getWindowCallback());
                if (this.mOverlayActionBar) {
                    this.mDecorContentParent.initFeature(109);
                }
                if (this.mFeatureProgress) {
                    this.mDecorContentParent.initFeature(2);
                }
                if (this.mFeatureIndeterminateProgress) {
                    this.mDecorContentParent.initFeature(5);
                }
            } else {
                viewGroup = null;
            }
        } else {
            if (this.mOverlayActionMode) {
                viewGroup = (ViewGroup) layoutInflaterFrom.inflate(android.support.v7.appcompat.R.layout.abc_screen_simple_overlay_action_mode, (ViewGroup) null);
            } else {
                viewGroup = (ViewGroup) layoutInflaterFrom.inflate(android.support.v7.appcompat.R.layout.abc_screen_simple, (ViewGroup) null);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                ViewCompat.setOnApplyWindowInsetsListener(viewGroup, new OnApplyWindowInsetsListener() { // from class: android.support.v7.app.AppCompatDelegateImpl.3
                    @Override // android.support.v4.view.OnApplyWindowInsetsListener
                    public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
                        int iUpdateStatusGuard = AppCompatDelegateImpl.this.updateStatusGuard(systemWindowInsetTop);
                        if (systemWindowInsetTop != iUpdateStatusGuard) {
                            windowInsetsCompat = windowInsetsCompat.replaceSystemWindowInsets(windowInsetsCompat.getSystemWindowInsetLeft(), iUpdateStatusGuard, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                        }
                        return ViewCompat.onApplyWindowInsets(view, windowInsetsCompat);
                    }
                });
            } else {
                ((FitWindowsViewGroup) viewGroup).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() { // from class: android.support.v7.app.AppCompatDelegateImpl.4
                    @Override // android.support.v7.widget.FitWindowsViewGroup.OnFitSystemWindowsListener
                    public void onFitSystemWindows(Rect rect) {
                        rect.top = AppCompatDelegateImpl.this.updateStatusGuard(rect.top);
                    }
                });
            }
        }
        if (viewGroup == null) {
            throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.mHasActionBar + ", windowActionBarOverlay: " + this.mOverlayActionBar + ", android:windowIsFloating: " + this.mIsFloating + ", windowActionModeOverlay: " + this.mOverlayActionMode + ", windowNoTitle: " + this.mWindowNoTitle + " }");
        }
        if (this.mDecorContentParent == null) {
            this.mTitleView = (TextView) viewGroup.findViewById(android.support.v7.appcompat.R.id.title);
        }
        ViewUtils.makeOptionalFitsSystemWindows(viewGroup);
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) viewGroup.findViewById(android.support.v7.appcompat.R.id.action_bar_activity_content);
        ViewGroup viewGroup2 = (ViewGroup) this.mWindow.findViewById(R.id.content);
        if (viewGroup2 != null) {
            while (viewGroup2.getChildCount() > 0) {
                View childAt = viewGroup2.getChildAt(0);
                viewGroup2.removeViewAt(0);
                contentFrameLayout.addView(childAt);
            }
            viewGroup2.setId(-1);
            contentFrameLayout.setId(R.id.content);
            if (viewGroup2 instanceof FrameLayout) {
                ((FrameLayout) viewGroup2).setForeground(null);
            }
        }
        this.mWindow.setContentView(viewGroup);
        contentFrameLayout.setAttachListener(new ContentFrameLayout.OnAttachListener() { // from class: android.support.v7.app.AppCompatDelegateImpl.5
            @Override // android.support.v7.widget.ContentFrameLayout.OnAttachListener
            public void onAttachedFromWindow() {
            }

            @Override // android.support.v7.widget.ContentFrameLayout.OnAttachListener
            public void onDetachedFromWindow() {
                AppCompatDelegateImpl.this.dismissPopups();
            }
        });
        return viewGroup;
    }

    private void applyFixedSizeWindow() {
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) this.mSubDecor.findViewById(R.id.content);
        View decorView = this.mWindow.getDecorView();
        contentFrameLayout.setDecorPadding(decorView.getPaddingLeft(), decorView.getPaddingTop(), decorView.getPaddingRight(), decorView.getPaddingBottom());
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(android.support.v7.appcompat.R.styleable.AppCompatTheme);
        typedArrayObtainStyledAttributes.getValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
        typedArrayObtainStyledAttributes.getValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
        if (typedArrayObtainStyledAttributes.hasValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
            typedArrayObtainStyledAttributes.getValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor());
        }
        if (typedArrayObtainStyledAttributes.hasValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
            typedArrayObtainStyledAttributes.getValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor());
        }
        if (typedArrayObtainStyledAttributes.hasValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
            typedArrayObtainStyledAttributes.getValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor());
        }
        if (typedArrayObtainStyledAttributes.hasValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
            typedArrayObtainStyledAttributes.getValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor());
        }
        typedArrayObtainStyledAttributes.recycle();
        contentFrameLayout.requestLayout();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public boolean requestWindowFeature(int i) {
        int iSanitizeWindowFeatureId = sanitizeWindowFeatureId(i);
        if (this.mWindowNoTitle && iSanitizeWindowFeatureId == 108) {
            return false;
        }
        if (this.mHasActionBar && iSanitizeWindowFeatureId == 1) {
            this.mHasActionBar = false;
        }
        switch (iSanitizeWindowFeatureId) {
            case 1:
                throwFeatureRequestIfSubDecorInstalled();
                this.mWindowNoTitle = true;
                return true;
            case 2:
                throwFeatureRequestIfSubDecorInstalled();
                this.mFeatureProgress = true;
                return true;
            case 5:
                throwFeatureRequestIfSubDecorInstalled();
                this.mFeatureIndeterminateProgress = true;
                return true;
            case 10:
                throwFeatureRequestIfSubDecorInstalled();
                this.mOverlayActionMode = true;
                return true;
            case 108:
                throwFeatureRequestIfSubDecorInstalled();
                this.mHasActionBar = true;
                return true;
            case 109:
                throwFeatureRequestIfSubDecorInstalled();
                this.mOverlayActionBar = true;
                return true;
            default:
                return this.mWindow.requestFeature(iSanitizeWindowFeatureId);
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public boolean hasWindowFeature(int i) {
        boolean z;
        switch (sanitizeWindowFeatureId(i)) {
            case 1:
                z = this.mWindowNoTitle;
                break;
            case 2:
                z = this.mFeatureProgress;
                break;
            case 5:
                z = this.mFeatureIndeterminateProgress;
                break;
            case 10:
                z = this.mOverlayActionMode;
                break;
            case 108:
                z = this.mHasActionBar;
                break;
            case 109:
                z = this.mOverlayActionBar;
                break;
            default:
                z = false;
                break;
        }
        return z || this.mWindow.hasFeature(i);
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public final void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        if (this.mDecorContentParent != null) {
            this.mDecorContentParent.setWindowTitle(charSequence);
        } else if (peekSupportActionBar() != null) {
            peekSupportActionBar().setWindowTitle(charSequence);
        } else if (this.mTitleView != null) {
            this.mTitleView.setText(charSequence);
        }
    }

    final CharSequence getTitle() {
        if (this.mOriginalWindowCallback instanceof Activity) {
            return ((Activity) this.mOriginalWindowCallback).getTitle();
        }
        return this.mTitle;
    }

    void onPanelClosed(int i) {
        if (i == 108) {
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.dispatchMenuVisibilityChanged(false);
                return;
            }
            return;
        }
        if (i == 0) {
            PanelFeatureState panelState = getPanelState(i, true);
            if (panelState.isOpen) {
                closePanel(panelState, false);
            }
        }
    }

    void onMenuOpened(int i) {
        ActionBar supportActionBar;
        if (i != 108 || (supportActionBar = getSupportActionBar()) == null) {
            return;
        }
        supportActionBar.dispatchMenuVisibilityChanged(true);
    }

    @Override // android.support.v7.view.menu.MenuBuilder.Callback
    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        PanelFeatureState panelFeatureStateFindMenuPanel;
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback == null || this.mIsDestroyed || (panelFeatureStateFindMenuPanel = findMenuPanel(menuBuilder.getRootMenu())) == null) {
            return false;
        }
        return windowCallback.onMenuItemSelected(panelFeatureStateFindMenuPanel.featureId, menuItem);
    }

    @Override // android.support.v7.view.menu.MenuBuilder.Callback
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        reopenMenu(menuBuilder, true);
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public ActionMode startSupportActionMode(@NonNull ActionMode.Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        ActionModeCallbackWrapperV9 actionModeCallbackWrapperV9 = new ActionModeCallbackWrapperV9(callback);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            this.mActionMode = supportActionBar.startActionMode(actionModeCallbackWrapperV9);
            if (this.mActionMode != null && this.mAppCompatCallback != null) {
                this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
            }
        }
        if (this.mActionMode == null) {
            this.mActionMode = startSupportActionModeFromWindow(actionModeCallbackWrapperV9);
        }
        return this.mActionMode;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void invalidateOptionsMenu() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null || !supportActionBar.invalidateOptionsMenu()) {
            invalidatePanelMenu(0);
        }
    }

    ActionMode startSupportActionModeFromWindow(@NonNull ActionMode.Callback callback) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ActionMode actionModeOnWindowStartingSupportActionMode;
        Context contextThemeWrapper;
        endOnGoingFadeAnimation();
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        if (!(callback instanceof ActionModeCallbackWrapperV9)) {
            callback = new ActionModeCallbackWrapperV9(callback);
        }
        if (this.mAppCompatCallback == null || this.mIsDestroyed) {
            actionModeOnWindowStartingSupportActionMode = null;
        } else {
            try {
                actionModeOnWindowStartingSupportActionMode = this.mAppCompatCallback.onWindowStartingSupportActionMode(callback);
            } catch (AbstractMethodError unused) {
            }
        }
        if (actionModeOnWindowStartingSupportActionMode != null) {
            this.mActionMode = actionModeOnWindowStartingSupportActionMode;
        } else {
            if (this.mActionModeView == null) {
                if (this.mIsFloating) {
                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = this.mContext.getTheme();
                    theme.resolveAttribute(android.support.v7.appcompat.R.attr.actionBarTheme, typedValue, true);
                    if (typedValue.resourceId != 0) {
                        Resources.Theme themeNewTheme = this.mContext.getResources().newTheme();
                        themeNewTheme.setTo(theme);
                        themeNewTheme.applyStyle(typedValue.resourceId, true);
                        contextThemeWrapper = new ContextThemeWrapper(this.mContext, 0);
                        contextThemeWrapper.getTheme().setTo(themeNewTheme);
                    } else {
                        contextThemeWrapper = this.mContext;
                    }
                    this.mActionModeView = new ActionBarContextView(contextThemeWrapper);
                    this.mActionModePopup = new PopupWindow(contextThemeWrapper, (AttributeSet) null, android.support.v7.appcompat.R.attr.actionModePopupWindowStyle);
                    PopupWindowCompat.setWindowLayoutType(this.mActionModePopup, 2);
                    this.mActionModePopup.setContentView(this.mActionModeView);
                    this.mActionModePopup.setWidth(-1);
                    contextThemeWrapper.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typedValue, true);
                    this.mActionModeView.setContentHeight(TypedValue.complexToDimensionPixelSize(typedValue.data, contextThemeWrapper.getResources().getDisplayMetrics()));
                    this.mActionModePopup.setHeight(-2);
                    this.mShowActionModePopup = new Runnable() { // from class: android.support.v7.app.AppCompatDelegateImpl.6
                        @Override // java.lang.Runnable
                        public void run() {
                            AppCompatDelegateImpl.this.mActionModePopup.showAtLocation(AppCompatDelegateImpl.this.mActionModeView, 55, 0, 0);
                            AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                            if (AppCompatDelegateImpl.this.shouldAnimateActionModeView()) {
                                AppCompatDelegateImpl.this.mActionModeView.setAlpha(0.0f);
                                AppCompatDelegateImpl.this.mFadeAnim = ViewCompat.animate(AppCompatDelegateImpl.this.mActionModeView).alpha(1.0f);
                                AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: android.support.v7.app.AppCompatDelegateImpl.6.1
                                    @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                                    public void onAnimationStart(View view) {
                                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                                    }

                                    @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                                    public void onAnimationEnd(View view) {
                                        AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                        AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                                        AppCompatDelegateImpl.this.mFadeAnim = null;
                                    }
                                });
                                return;
                            }
                            AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                            AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                        }
                    };
                } else {
                    ViewStubCompat viewStubCompat = (ViewStubCompat) this.mSubDecor.findViewById(android.support.v7.appcompat.R.id.action_mode_bar_stub);
                    if (viewStubCompat != null) {
                        viewStubCompat.setLayoutInflater(LayoutInflater.from(getActionBarThemedContext()));
                        this.mActionModeView = (ActionBarContextView) viewStubCompat.inflate();
                    }
                }
            }
            if (this.mActionModeView != null) {
                endOnGoingFadeAnimation();
                this.mActionModeView.killMode();
                StandaloneActionMode standaloneActionMode = new StandaloneActionMode(this.mActionModeView.getContext(), this.mActionModeView, callback, this.mActionModePopup == null);
                if (callback.onCreateActionMode(standaloneActionMode, standaloneActionMode.getMenu())) {
                    standaloneActionMode.invalidate();
                    this.mActionModeView.initForMode(standaloneActionMode);
                    this.mActionMode = standaloneActionMode;
                    if (shouldAnimateActionModeView()) {
                        this.mActionModeView.setAlpha(0.0f);
                        this.mFadeAnim = ViewCompat.animate(this.mActionModeView).alpha(1.0f);
                        this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: android.support.v7.app.AppCompatDelegateImpl.7
                            @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                            public void onAnimationStart(View view) {
                                AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                                AppCompatDelegateImpl.this.mActionModeView.sendAccessibilityEvent(32);
                                if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                                    ViewCompat.requestApplyInsets((View) AppCompatDelegateImpl.this.mActionModeView.getParent());
                                }
                            }

                            @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                            public void onAnimationEnd(View view) {
                                AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                                AppCompatDelegateImpl.this.mFadeAnim = null;
                            }
                        });
                    } else {
                        this.mActionModeView.setAlpha(1.0f);
                        this.mActionModeView.setVisibility(0);
                        this.mActionModeView.sendAccessibilityEvent(32);
                        if (this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View) this.mActionModeView.getParent());
                        }
                    }
                    if (this.mActionModePopup != null) {
                        this.mWindow.getDecorView().post(this.mShowActionModePopup);
                    }
                } else {
                    this.mActionMode = null;
                }
            }
        }
        if (this.mActionMode != null && this.mAppCompatCallback != null) {
            this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
        }
        return this.mActionMode;
    }

    final boolean shouldAnimateActionModeView() {
        return this.mSubDecorInstalled && this.mSubDecor != null && ViewCompat.isLaidOut(this.mSubDecor);
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setHandleNativeActionModesEnabled(boolean z) {
        this.mHandleNativeActionModes = z;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public boolean isHandleNativeActionModesEnabled() {
        return this.mHandleNativeActionModes;
    }

    void endOnGoingFadeAnimation() {
        if (this.mFadeAnim != null) {
            this.mFadeAnim.cancel();
        }
    }

    boolean onBackPressed() {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
            return true;
        }
        ActionBar supportActionBar = getSupportActionBar();
        return supportActionBar != null && supportActionBar.collapseActionView();
    }

    boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && supportActionBar.onKeyShortcut(i, keyEvent)) {
            return true;
        }
        if (this.mPreparedPanel != null && performPanelShortcut(this.mPreparedPanel, keyEvent.getKeyCode(), keyEvent, 1)) {
            if (this.mPreparedPanel != null) {
                this.mPreparedPanel.isHandled = true;
            }
            return true;
        }
        if (this.mPreparedPanel == null) {
            PanelFeatureState panelState = getPanelState(0, true);
            preparePanel(panelState, keyEvent);
            boolean zPerformPanelShortcut = performPanelShortcut(panelState, keyEvent.getKeyCode(), keyEvent, 1);
            panelState.isPrepared = false;
            if (zPerformPanelShortcut) {
                return true;
            }
        }
        return false;
    }

    boolean dispatchKeyEvent(KeyEvent keyEvent) {
        View decorView;
        if (((this.mOriginalWindowCallback instanceof KeyEventDispatcher.Component) || (this.mOriginalWindowCallback instanceof AppCompatDialog)) && (decorView = this.mWindow.getDecorView()) != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 82 && this.mOriginalWindowCallback.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        int keyCode = keyEvent.getKeyCode();
        return keyEvent.getAction() == 0 ? onKeyDown(keyCode, keyEvent) : onKeyUp(keyCode, keyEvent);
    }

    boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 4) {
            boolean z = this.mLongPressBackDown;
            this.mLongPressBackDown = false;
            PanelFeatureState panelState = getPanelState(0, false);
            if (panelState != null && panelState.isOpen) {
                if (!z) {
                    closePanel(panelState, true);
                }
                return true;
            }
            if (onBackPressed()) {
                return true;
            }
        } else if (i == 82) {
            onKeyUpPanel(0, keyEvent);
            return true;
        }
        return false;
    }

    boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            this.mLongPressBackDown = (keyEvent.getFlags() & 128) != 0;
        } else if (i == 82) {
            onKeyDownPanel(0, keyEvent);
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.support.v7.app.AppCompatDelegate
    public View createView(View view, String str, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        boolean zShouldInheritContext = false;
        if (this.mAppCompatViewInflater == null) {
            String string = this.mContext.obtainStyledAttributes(android.support.v7.appcompat.R.styleable.AppCompatTheme).getString(android.support.v7.appcompat.R.styleable.AppCompatTheme_viewInflaterClass);
            if (string == null || AppCompatViewInflater.class.getName().equals(string)) {
                this.mAppCompatViewInflater = new AppCompatViewInflater();
            } else {
                try {
                    this.mAppCompatViewInflater = (AppCompatViewInflater) Class.forName(string).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (Throwable th) {
                    Log.i("AppCompatDelegate", "Failed to instantiate custom view inflater " + string + ". Falling back to default.", th);
                    this.mAppCompatViewInflater = new AppCompatViewInflater();
                }
            }
        }
        if (IS_PRE_LOLLIPOP) {
            if (attributeSet instanceof XmlPullParser) {
                if (((XmlPullParser) attributeSet).getDepth() > 1) {
                    zShouldInheritContext = true;
                }
            } else {
                zShouldInheritContext = shouldInheritContext((ViewParent) view);
            }
        }
        return this.mAppCompatViewInflater.createView(view, str, context, attributeSet, zShouldInheritContext, IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
    }

    private boolean shouldInheritContext(ViewParent viewParent) {
        if (viewParent == null) {
            return false;
        }
        View decorView = this.mWindow.getDecorView();
        while (viewParent != null) {
            if (viewParent == decorView || !(viewParent instanceof View) || ViewCompat.isAttachedToWindow((View) viewParent)) {
                return false;
            }
            viewParent = viewParent.getParent();
        }
        return true;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void installViewFactory() throws IllegalAccessException, IllegalArgumentException {
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.mContext);
        if (layoutInflaterFrom.getFactory() == null) {
            LayoutInflaterCompat.setFactory2(layoutInflaterFrom, this);
        } else {
            if (layoutInflaterFrom.getFactory2() instanceof AppCompatDelegateImpl) {
                return;
            }
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    @Override // android.view.LayoutInflater.Factory2
    public final View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return createView(view, str, context, attributeSet);
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    private void openPanel(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        int i;
        ViewGroup.LayoutParams layoutParams;
        if (panelFeatureState.isOpen || this.mIsDestroyed) {
            return;
        }
        if (panelFeatureState.featureId == 0) {
            if ((this.mContext.getResources().getConfiguration().screenLayout & 15) == 4) {
                return;
            }
        }
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null && !windowCallback.onMenuOpened(panelFeatureState.featureId, panelFeatureState.menu)) {
            closePanel(panelFeatureState, true);
            return;
        }
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        if (windowManager != null && preparePanel(panelFeatureState, keyEvent)) {
            if (panelFeatureState.decorView == null || panelFeatureState.refreshDecorView) {
                if (panelFeatureState.decorView == null) {
                    if (!initializePanelDecor(panelFeatureState) || panelFeatureState.decorView == null) {
                        return;
                    }
                } else if (panelFeatureState.refreshDecorView && panelFeatureState.decorView.getChildCount() > 0) {
                    panelFeatureState.decorView.removeAllViews();
                }
                if (!initializePanelContent(panelFeatureState) || !panelFeatureState.hasPanelItems()) {
                    return;
                }
                ViewGroup.LayoutParams layoutParams2 = panelFeatureState.shownPanelView.getLayoutParams();
                if (layoutParams2 == null) {
                    layoutParams2 = new ViewGroup.LayoutParams(-2, -2);
                }
                panelFeatureState.decorView.setBackgroundResource(panelFeatureState.background);
                ViewParent parent = panelFeatureState.shownPanelView.getParent();
                if (parent != null && (parent instanceof ViewGroup)) {
                    ((ViewGroup) parent).removeView(panelFeatureState.shownPanelView);
                }
                panelFeatureState.decorView.addView(panelFeatureState.shownPanelView, layoutParams2);
                if (!panelFeatureState.shownPanelView.hasFocus()) {
                    panelFeatureState.shownPanelView.requestFocus();
                }
            } else {
                if (panelFeatureState.createdPanelView != null && (layoutParams = panelFeatureState.createdPanelView.getLayoutParams()) != null && layoutParams.width == -1) {
                    i = -1;
                }
                panelFeatureState.isHandled = false;
                WindowManager.LayoutParams layoutParams3 = new WindowManager.LayoutParams(i, -2, panelFeatureState.x, panelFeatureState.y, 1002, 8519680, -3);
                layoutParams3.gravity = panelFeatureState.gravity;
                layoutParams3.windowAnimations = panelFeatureState.windowAnimations;
                windowManager.addView(panelFeatureState.decorView, layoutParams3);
                panelFeatureState.isOpen = true;
            }
            i = -2;
            panelFeatureState.isHandled = false;
            WindowManager.LayoutParams layoutParams32 = new WindowManager.LayoutParams(i, -2, panelFeatureState.x, panelFeatureState.y, 1002, 8519680, -3);
            layoutParams32.gravity = panelFeatureState.gravity;
            layoutParams32.windowAnimations = panelFeatureState.windowAnimations;
            windowManager.addView(panelFeatureState.decorView, layoutParams32);
            panelFeatureState.isOpen = true;
        }
    }

    private boolean initializePanelDecor(PanelFeatureState panelFeatureState) {
        panelFeatureState.setStyle(getActionBarThemedContext());
        panelFeatureState.decorView = new ListMenuDecorView(panelFeatureState.listPresenterContext);
        panelFeatureState.gravity = 81;
        return true;
    }

    private void reopenMenu(MenuBuilder menuBuilder, boolean z) {
        if (this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && (!ViewConfiguration.get(this.mContext).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
            Window.Callback windowCallback = getWindowCallback();
            if (!this.mDecorContentParent.isOverflowMenuShowing() || !z) {
                if (windowCallback == null || this.mIsDestroyed) {
                    return;
                }
                if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
                    this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
                    this.mInvalidatePanelMenuRunnable.run();
                }
                PanelFeatureState panelState = getPanelState(0, true);
                if (panelState.menu == null || panelState.refreshMenuContent || !windowCallback.onPreparePanel(0, panelState.createdPanelView, panelState.menu)) {
                    return;
                }
                windowCallback.onMenuOpened(108, panelState.menu);
                this.mDecorContentParent.showOverflowMenu();
                return;
            }
            this.mDecorContentParent.hideOverflowMenu();
            if (this.mIsDestroyed) {
                return;
            }
            windowCallback.onPanelClosed(108, getPanelState(0, true).menu);
            return;
        }
        PanelFeatureState panelState2 = getPanelState(0, true);
        panelState2.refreshDecorView = true;
        closePanel(panelState2, false);
        openPanel(panelState2, null);
    }

    private boolean initializePanelMenu(PanelFeatureState panelFeatureState) {
        Context context = this.mContext;
        if ((panelFeatureState.featureId == 0 || panelFeatureState.featureId == 108) && this.mDecorContentParent != null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(android.support.v7.appcompat.R.attr.actionBarTheme, typedValue, true);
            Resources.Theme themeNewTheme = null;
            if (typedValue.resourceId != 0) {
                themeNewTheme = context.getResources().newTheme();
                themeNewTheme.setTo(theme);
                themeNewTheme.applyStyle(typedValue.resourceId, true);
                themeNewTheme.resolveAttribute(android.support.v7.appcompat.R.attr.actionBarWidgetTheme, typedValue, true);
            } else {
                theme.resolveAttribute(android.support.v7.appcompat.R.attr.actionBarWidgetTheme, typedValue, true);
            }
            if (typedValue.resourceId != 0) {
                if (themeNewTheme == null) {
                    themeNewTheme = context.getResources().newTheme();
                    themeNewTheme.setTo(theme);
                }
                themeNewTheme.applyStyle(typedValue.resourceId, true);
            }
            if (themeNewTheme != null) {
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 0);
                contextThemeWrapper.getTheme().setTo(themeNewTheme);
                context = contextThemeWrapper;
            }
        }
        MenuBuilder menuBuilder = new MenuBuilder(context);
        menuBuilder.setCallback(this);
        panelFeatureState.setMenu(menuBuilder);
        return true;
    }

    private boolean initializePanelContent(PanelFeatureState panelFeatureState) {
        if (panelFeatureState.createdPanelView != null) {
            panelFeatureState.shownPanelView = panelFeatureState.createdPanelView;
            return true;
        }
        if (panelFeatureState.menu == null) {
            return false;
        }
        if (this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback();
        }
        panelFeatureState.shownPanelView = (View) panelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
        return panelFeatureState.shownPanelView != null;
    }

    private boolean preparePanel(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        if (this.mIsDestroyed) {
            return false;
        }
        if (panelFeatureState.isPrepared) {
            return true;
        }
        if (this.mPreparedPanel != null && this.mPreparedPanel != panelFeatureState) {
            closePanel(this.mPreparedPanel, false);
        }
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null) {
            panelFeatureState.createdPanelView = windowCallback.onCreatePanelView(panelFeatureState.featureId);
        }
        boolean z = panelFeatureState.featureId == 0 || panelFeatureState.featureId == 108;
        if (z && this.mDecorContentParent != null) {
            this.mDecorContentParent.setMenuPrepared();
        }
        if (panelFeatureState.createdPanelView == null && (!z || !(peekSupportActionBar() instanceof ToolbarActionBar))) {
            if (panelFeatureState.menu == null || panelFeatureState.refreshMenuContent) {
                if (panelFeatureState.menu == null && (!initializePanelMenu(panelFeatureState) || panelFeatureState.menu == null)) {
                    return false;
                }
                if (z && this.mDecorContentParent != null) {
                    if (this.mActionMenuPresenterCallback == null) {
                        this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
                    }
                    this.mDecorContentParent.setMenu(panelFeatureState.menu, this.mActionMenuPresenterCallback);
                }
                panelFeatureState.menu.stopDispatchingItemsChanged();
                if (!windowCallback.onCreatePanelMenu(panelFeatureState.featureId, panelFeatureState.menu)) {
                    panelFeatureState.setMenu(null);
                    if (z && this.mDecorContentParent != null) {
                        this.mDecorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                    }
                    return false;
                }
                panelFeatureState.refreshMenuContent = false;
            }
            panelFeatureState.menu.stopDispatchingItemsChanged();
            if (panelFeatureState.frozenActionViewState != null) {
                panelFeatureState.menu.restoreActionViewStates(panelFeatureState.frozenActionViewState);
                panelFeatureState.frozenActionViewState = null;
            }
            if (!windowCallback.onPreparePanel(0, panelFeatureState.createdPanelView, panelFeatureState.menu)) {
                if (z && this.mDecorContentParent != null) {
                    this.mDecorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                }
                panelFeatureState.menu.startDispatchingItemsChanged();
                return false;
            }
            panelFeatureState.qwertyMode = KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1;
            panelFeatureState.menu.setQwertyMode(panelFeatureState.qwertyMode);
            panelFeatureState.menu.startDispatchingItemsChanged();
        }
        panelFeatureState.isPrepared = true;
        panelFeatureState.isHandled = false;
        this.mPreparedPanel = panelFeatureState;
        return true;
    }

    void checkCloseActionMenu(MenuBuilder menuBuilder) {
        if (this.mClosingActionMenu) {
            return;
        }
        this.mClosingActionMenu = true;
        this.mDecorContentParent.dismissPopups();
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null && !this.mIsDestroyed) {
            windowCallback.onPanelClosed(108, menuBuilder);
        }
        this.mClosingActionMenu = false;
    }

    void closePanel(int i) {
        closePanel(getPanelState(i, true), true);
    }

    void closePanel(PanelFeatureState panelFeatureState, boolean z) {
        if (z && panelFeatureState.featureId == 0 && this.mDecorContentParent != null && this.mDecorContentParent.isOverflowMenuShowing()) {
            checkCloseActionMenu(panelFeatureState.menu);
            return;
        }
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        if (windowManager != null && panelFeatureState.isOpen && panelFeatureState.decorView != null) {
            windowManager.removeView(panelFeatureState.decorView);
            if (z) {
                callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, null);
            }
        }
        panelFeatureState.isPrepared = false;
        panelFeatureState.isHandled = false;
        panelFeatureState.isOpen = false;
        panelFeatureState.shownPanelView = null;
        panelFeatureState.refreshDecorView = true;
        if (this.mPreparedPanel == panelFeatureState) {
            this.mPreparedPanel = null;
        }
    }

    private boolean onKeyDownPanel(int i, KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() != 0) {
            return false;
        }
        PanelFeatureState panelState = getPanelState(i, true);
        if (panelState.isOpen) {
            return false;
        }
        return preparePanel(panelState, keyEvent);
    }

    private boolean onKeyUpPanel(int featureId, KeyEvent event) {
        if (mActionMode != null) {
            return false;
        }
        boolean handled = false;
        final PanelFeatureState st = getPanelState(featureId, true);
        if (featureId == FEATURE_OPTIONS_PANEL && mDecorContentParent != null &&
                mDecorContentParent.canShowOverflowMenu() &&
                !ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
            if (!mDecorContentParent.isOverflowMenuShowing()) {
                if (!mDestroyed && preparePanel(st, event)) {
                    handled = mDecorContentParent.showOverflowMenu();
                }
            } else {
                handled = mDecorContentParent.hideOverflowMenu();
            }
        } else {
            if (st.isOpen || st.isHandled) {
                // Play the sound effect if the user closed an open menu (and not if
                // they just released a menu shortcut)
                handled = st.isOpen;
                // Close menu
                closePanel(st, true);
            } else if (st.isPrepared) {
                boolean show = true;
                if (st.refreshMenuContent) {
                    // Something may have invalidated the menu since we prepared it.
                    // Re-prepare it to refresh.
                    st.isPrepared = false;
                    show = preparePanel(st, event);
                }
                if (show) {
                    // Show menu
                    openPanel(st, event);
                    handled = true;
                }
            }
        }
        if (handled) {
            AudioManager audioManager = (AudioManager) mContext.getApplicationContext()
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
            } else {
                Log.w(TAG, "Couldn't get audio manager");
            }
        }
        return handled;
    }

    void callOnPanelClosed(int i, PanelFeatureState panelFeatureState, Menu menu) {
        if (menu == null) {
            if (panelFeatureState == null && i >= 0 && i < this.mPanels.length) {
                panelFeatureState = this.mPanels[i];
            }
            if (panelFeatureState != null) {
                menu = panelFeatureState.menu;
            }
        }
        if ((panelFeatureState == null || panelFeatureState.isOpen) && !this.mIsDestroyed) {
            this.mOriginalWindowCallback.onPanelClosed(i, menu);
        }
    }

    PanelFeatureState findMenuPanel(Menu menu) {
        PanelFeatureState[] panelFeatureStateArr = this.mPanels;
        int length = panelFeatureStateArr != null ? panelFeatureStateArr.length : 0;
        for (int i = 0; i < length; i++) {
            PanelFeatureState panelFeatureState = panelFeatureStateArr[i];
            if (panelFeatureState != null && panelFeatureState.menu == menu) {
                return panelFeatureState;
            }
        }
        return null;
    }

    protected PanelFeatureState getPanelState(int i, boolean z) {
        PanelFeatureState[] panelFeatureStateArr = this.mPanels;
        if (panelFeatureStateArr == null || panelFeatureStateArr.length <= i) {
            PanelFeatureState[] panelFeatureStateArr2 = new PanelFeatureState[i + 1];
            if (panelFeatureStateArr != null) {
                System.arraycopy(panelFeatureStateArr, 0, panelFeatureStateArr2, 0, panelFeatureStateArr.length);
            }
            this.mPanels = panelFeatureStateArr2;
            panelFeatureStateArr = panelFeatureStateArr2;
        }
        PanelFeatureState panelFeatureState = panelFeatureStateArr[i];
        if (panelFeatureState != null) {
            return panelFeatureState;
        }
        PanelFeatureState panelFeatureState2 = new PanelFeatureState(i);
        panelFeatureStateArr[i] = panelFeatureState2;
        return panelFeatureState2;
    }

    private boolean performPanelShortcut(PanelFeatureState panelFeatureState, int i, KeyEvent keyEvent, int i2) {
        boolean zPerformShortcut = false;
        if (keyEvent.isSystem()) {
            return false;
        }
        if ((panelFeatureState.isPrepared || preparePanel(panelFeatureState, keyEvent)) && panelFeatureState.menu != null) {
            zPerformShortcut = panelFeatureState.menu.performShortcut(i, keyEvent, i2);
        }
        if (zPerformShortcut && (i2 & 1) == 0 && this.mDecorContentParent == null) {
            closePanel(panelFeatureState, true);
        }
        return zPerformShortcut;
    }

    private void invalidatePanelMenu(int i) {
        this.mInvalidatePanelMenuFeatures = (1 << i) | this.mInvalidatePanelMenuFeatures;
        if (this.mInvalidatePanelMenuPosted) {
            return;
        }
        ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
        this.mInvalidatePanelMenuPosted = true;
    }

    void doInvalidatePanelMenu(int i) {
        PanelFeatureState panelState;
        PanelFeatureState panelState2 = getPanelState(i, true);
        if (panelState2.menu != null) {
            Bundle bundle = new Bundle();
            panelState2.menu.saveActionViewStates(bundle);
            if (bundle.size() > 0) {
                panelState2.frozenActionViewState = bundle;
            }
            panelState2.menu.stopDispatchingItemsChanged();
            panelState2.menu.clear();
        }
        panelState2.refreshMenuContent = true;
        panelState2.refreshDecorView = true;
        if ((i != 108 && i != 0) || this.mDecorContentParent == null || (panelState = getPanelState(0, false)) == null) {
            return;
        }
        panelState.isPrepared = false;
        preparePanel(panelState, null);
    }

    int updateStatusGuard(int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean z;
        boolean z2;
        if (this.mActionModeView == null || !(this.mActionModeView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            z = false;
        } else {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mActionModeView.getLayoutParams();
            if (this.mActionModeView.isShown()) {
                if (this.mTempRect1 == null) {
                    this.mTempRect1 = new Rect();
                    this.mTempRect2 = new Rect();
                }
                Rect rect = this.mTempRect1;
                Rect rect2 = this.mTempRect2;
                rect.set(0, i, 0, 0);
                ViewUtils.computeFitSystemWindows(this.mSubDecor, rect, rect2);
                if (marginLayoutParams.topMargin != (rect2.top == 0 ? i : 0)) {
                    marginLayoutParams.topMargin = i;
                    if (this.mStatusGuard == null) {
                        this.mStatusGuard = new View(this.mContext);
                        this.mStatusGuard.setBackgroundColor(this.mContext.getResources().getColor(android.support.v7.appcompat.R.color.abc_input_method_navigation_guard));
                        this.mSubDecor.addView(this.mStatusGuard, -1, new ViewGroup.LayoutParams(-1, i));
                    } else {
                        ViewGroup.LayoutParams layoutParams = this.mStatusGuard.getLayoutParams();
                        if (layoutParams.height != i) {
                            layoutParams.height = i;
                            this.mStatusGuard.setLayoutParams(layoutParams);
                        }
                    }
                    z2 = true;
                } else {
                    z2 = false;
                }
                z = this.mStatusGuard != null;
                if (!this.mOverlayActionMode && z) {
                    i = 0;
                }
            } else if (marginLayoutParams.topMargin != 0) {
                marginLayoutParams.topMargin = 0;
                z2 = true;
                z = false;
            } else {
                z2 = false;
                z = false;
            }
            if (z2) {
                this.mActionModeView.setLayoutParams(marginLayoutParams);
            }
        }
        if (this.mStatusGuard != null) {
            this.mStatusGuard.setVisibility(z ? 0 : 8);
        }
        return i;
    }

    private void throwFeatureRequestIfSubDecorInstalled() {
        if (this.mSubDecorInstalled) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }

    private int sanitizeWindowFeatureId(int i) {
        if (i == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        }
        if (i != 9) {
            return i;
        }
        Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
        return 109;
    }

    ViewGroup getSubDecor() {
        return this.mSubDecor;
    }

    void dismissPopups() {
        if (this.mDecorContentParent != null) {
            this.mDecorContentParent.dismissPopups();
        }
        if (this.mActionModePopup != null) {
            this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
            if (this.mActionModePopup.isShowing()) {
                try {
                    this.mActionModePopup.dismiss();
                } catch (IllegalArgumentException unused) {
                }
            }
            this.mActionModePopup = null;
        }
        endOnGoingFadeAnimation();
        PanelFeatureState panelState = getPanelState(0, false);
        if (panelState == null || panelState.menu == null) {
            return;
        }
        panelState.menu.close();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public boolean applyDayNight() {
        int nightMode = getNightMode();
        int iMapNightMode = mapNightMode(nightMode);
        boolean zUpdateForNightMode = iMapNightMode != -1 ? updateForNightMode(iMapNightMode) : false;
        if (nightMode == 0) {
            ensureAutoNightModeManager();
            this.mAutoNightModeManager.setup();
        }
        this.mApplyDayNightCalled = true;
        return zUpdateForNightMode;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setLocalNightMode(int i) {
        switch (i) {
            case -1:
            case 0:
            case 1:
            case 2:
                if (this.mLocalNightMode != i) {
                    this.mLocalNightMode = i;
                    if (this.mApplyDayNightCalled) {
                        applyDayNight();
                        break;
                    }
                }
                break;
            default:
                Log.i("AppCompatDelegate", "setLocalNightMode() called with an unknown mode");
                break;
        }
    }

    int mapNightMode(int i) {
        if (i == -100) {
            return -1;
        }
        if (i != 0) {
            return i;
        }
        if (Build.VERSION.SDK_INT >= 23 && ((UiModeManager) this.mContext.getSystemService(UiModeManager.class)).getNightMode() == 0) {
            return -1;
        }
        ensureAutoNightModeManager();
        return this.mAutoNightModeManager.getApplyableNightMode();
    }

    private int getNightMode() {
        return this.mLocalNightMode != -100 ? this.mLocalNightMode : getDefaultNightMode();
    }

    private boolean updateForNightMode(int i) throws IllegalAccessException, IllegalArgumentException {
        Resources resources = this.mContext.getResources();
        Configuration configuration = resources.getConfiguration();
        int i2 = configuration.uiMode & 48;
        int i3 = i == 2 ? 32 : 16;
        if (i2 == i3) {
            return false;
        }
        if (shouldRecreateOnNightModeChange()) {
            ((Activity) this.mContext).recreate();
            return true;
        }
        Configuration configuration2 = new Configuration(configuration);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration2.uiMode = i3 | (configuration2.uiMode & (-49));
        resources.updateConfiguration(configuration2, displayMetrics);
        if (Build.VERSION.SDK_INT >= 26) {
            return true;
        }
        ResourcesFlusher.flush(resources);
        return true;
    }

    private void ensureAutoNightModeManager() {
        if (this.mAutoNightModeManager == null) {
            this.mAutoNightModeManager = new AutoNightModeManager(TwilightManager.getInstance(this.mContext));
        }
    }

    @VisibleForTesting
    final AutoNightModeManager getAutoNightModeManager() {
        ensureAutoNightModeManager();
        return this.mAutoNightModeManager;
    }

    private boolean shouldRecreateOnNightModeChange() {
        if (!this.mApplyDayNightCalled || !(this.mContext instanceof Activity)) {
            return false;
        }
        try {
            return (this.mContext.getPackageManager().getActivityInfo(new ComponentName(this.mContext, this.mContext.getClass()), 0).configChanges & 512) == 0;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", e);
            return true;
        }
    }

    class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
        private ActionMode.Callback mWrapped;

        public ActionModeCallbackWrapperV9(ActionMode.Callback callback) {
            this.mWrapped = callback;
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                AppCompatDelegateImpl.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup);
            }
            if (AppCompatDelegateImpl.this.mActionModeView != null) {
                AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                AppCompatDelegateImpl.this.mFadeAnim = ViewCompat.animate(AppCompatDelegateImpl.this.mActionModeView).alpha(0.0f);
                AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: android.support.v7.app.AppCompatDelegateImpl.ActionModeCallbackWrapperV9.1
                    @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                    public void onAnimationEnd(View view) {
                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                        if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                            AppCompatDelegateImpl.this.mActionModePopup.dismiss();
                        } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View) AppCompatDelegateImpl.this.mActionModeView.getParent());
                        }
                        AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
                        AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                        AppCompatDelegateImpl.this.mFadeAnim = null;
                    }
                });
            }
            if (AppCompatDelegateImpl.this.mAppCompatCallback != null) {
                AppCompatDelegateImpl.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImpl.this.mActionMode);
            }
            AppCompatDelegateImpl.this.mActionMode = null;
        }
    }

    private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
        PanelMenuPresenterCallback() {
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            MenuBuilder rootMenu = menuBuilder.getRootMenu();
            boolean z2 = rootMenu != menuBuilder;
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (z2) {
                menuBuilder = rootMenu;
            }
            PanelFeatureState panelFeatureStateFindMenuPanel = appCompatDelegateImpl.findMenuPanel(menuBuilder);
            if (panelFeatureStateFindMenuPanel != null) {
                if (z2) {
                    AppCompatDelegateImpl.this.callOnPanelClosed(panelFeatureStateFindMenuPanel.featureId, panelFeatureStateFindMenuPanel, rootMenu);
                    AppCompatDelegateImpl.this.closePanel(panelFeatureStateFindMenuPanel, true);
                } else {
                    AppCompatDelegateImpl.this.closePanel(panelFeatureStateFindMenuPanel, z);
                }
            }
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback windowCallback;
            if (menuBuilder != null || !AppCompatDelegateImpl.this.mHasActionBar || (windowCallback = AppCompatDelegateImpl.this.getWindowCallback()) == null || AppCompatDelegateImpl.this.mIsDestroyed) {
                return true;
            }
            windowCallback.onMenuOpened(108, menuBuilder);
            return true;
        }
    }

    private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
        ActionMenuPresenterCallback() {
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback windowCallback = AppCompatDelegateImpl.this.getWindowCallback();
            if (windowCallback == null) {
                return true;
            }
            windowCallback.onMenuOpened(108, menuBuilder);
            return true;
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            AppCompatDelegateImpl.this.checkCloseActionMenu(menuBuilder);
        }
    }

    protected static final class PanelFeatureState {
        int background;
        View createdPanelView;
        ViewGroup decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int gravity;
        boolean isHandled;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        Context listPresenterContext;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView = false;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastOpen;
        int windowAnimations;
        int x;
        int y;

        PanelFeatureState(int i) {
            this.featureId = i;
        }

        public boolean hasPanelItems() {
            if (this.shownPanelView == null) {
                return false;
            }
            return this.createdPanelView != null || this.listMenuPresenter.getAdapter().getCount() > 0;
        }

        public void clearMenuPresenters() {
            if (this.menu != null) {
                this.menu.removeMenuPresenter(this.listMenuPresenter);
            }
            this.listMenuPresenter = null;
        }

        void setStyle(Context context) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme themeNewTheme = context.getResources().newTheme();
            themeNewTheme.setTo(context.getTheme());
            themeNewTheme.resolveAttribute(android.support.v7.appcompat.R.attr.actionBarPopupTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                themeNewTheme.applyStyle(typedValue.resourceId, true);
            }
            themeNewTheme.resolveAttribute(android.support.v7.appcompat.R.attr.panelMenuListTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                themeNewTheme.applyStyle(typedValue.resourceId, true);
            } else {
                themeNewTheme.applyStyle(android.support.v7.appcompat.R.style.Theme_AppCompat_CompactMenu, true);
            }
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 0);
            contextThemeWrapper.getTheme().setTo(themeNewTheme);
            this.listPresenterContext = contextThemeWrapper;
            TypedArray typedArrayObtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(android.support.v7.appcompat.R.styleable.AppCompatTheme);
            this.background = typedArrayObtainStyledAttributes.getResourceId(android.support.v7.appcompat.R.styleable.AppCompatTheme_panelBackground, 0);
            this.windowAnimations = typedArrayObtainStyledAttributes.getResourceId(android.support.v7.appcompat.R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            typedArrayObtainStyledAttributes.recycle();
        }

        void setMenu(MenuBuilder menuBuilder) {
            if (menuBuilder == this.menu) {
                return;
            }
            if (this.menu != null) {
                this.menu.removeMenuPresenter(this.listMenuPresenter);
            }
            this.menu = menuBuilder;
            if (menuBuilder == null || this.listMenuPresenter == null) {
                return;
            }
            menuBuilder.addMenuPresenter(this.listMenuPresenter);
        }

        MenuView getListMenuView(MenuPresenter.Callback callback) {
            if (this.menu == null) {
                return null;
            }
            if (this.listMenuPresenter == null) {
                this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, android.support.v7.appcompat.R.layout.abc_list_menu_item_layout);
                this.listMenuPresenter.setCallback(callback);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }

        Parcelable onSaveInstanceState() {
            SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return savedState;
        }

        void onRestoreInstanceState(Parcelable parcelable) {
            SavedState savedState = (SavedState) parcelable;
            this.featureId = savedState.featureId;
            this.wasLastOpen = savedState.isOpen;
            this.frozenMenuState = savedState.menuState;
            this.shownPanelView = null;
            this.decorView = null;
        }

        void applyFrozenState() {
            if (this.menu == null || this.frozenMenuState == null) {
                return;
            }
            this.menu.restorePresenterStates(this.frozenMenuState);
            this.frozenMenuState = null;
        }

        private static class SavedState implements Parcelable {
            public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.v7.app.AppCompatDelegateImpl.PanelFeatureState.SavedState.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // android.os.Parcelable.ClassLoaderCreator
                public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return SavedState.readFromParcel(parcel, classLoader);
                }

                @Override // android.os.Parcelable.Creator
                public SavedState createFromParcel(Parcel parcel) {
                    return SavedState.readFromParcel(parcel, null);
                }

                @Override // android.os.Parcelable.Creator
                public SavedState[] newArray(int i) {
                    return new SavedState[i];
                }
            };
            int featureId;
            boolean isOpen;
            Bundle menuState;

            @Override // android.os.Parcelable
            public int describeContents() {
                return 0;
            }

            SavedState() {
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(this.featureId);
                parcel.writeInt(this.isOpen ? 1 : 0);
                if (this.isOpen) {
                    parcel.writeBundle(this.menuState);
                }
            }

            static SavedState readFromParcel(Parcel parcel, ClassLoader classLoader) {
                SavedState savedState = new SavedState();
                savedState.featureId = parcel.readInt();
                savedState.isOpen = parcel.readInt() == 1;
                if (savedState.isOpen) {
                    savedState.menuState = parcel.readBundle(classLoader);
                }
                return savedState;
            }
        }
    }

    private class ListMenuDecorView extends ContentFrameLayout {
        public ListMenuDecorView(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && isOutOfBounds((int) motionEvent.getX(), (int) motionEvent.getY())) {
                AppCompatDelegateImpl.this.closePanel(0);
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public void setBackgroundResource(int i) {
            setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), i));
        }

        private boolean isOutOfBounds(int i, int i2) {
            return i < -5 || i2 < -5 || i > getWidth() + 5 || i2 > getHeight() + 5;
        }
    }

    class AppCompatWindowCallback extends WindowCallbackWrapper {
        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onContentChanged() {
        }

        AppCompatWindowCallback(Window.Callback callback) {
            super(callback);
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return super.dispatchKeyShortcutEvent(keyEvent) || AppCompatDelegateImpl.this.onKeyShortcut(keyEvent.getKeyCode(), keyEvent);
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onCreatePanelMenu(int i, Menu menu) {
            if (i != 0 || (menu instanceof MenuBuilder)) {
                return super.onCreatePanelMenu(i, menu);
            }
            return false;
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onPreparePanel(int i, View view, Menu menu) {
            MenuBuilder menuBuilder = menu instanceof MenuBuilder ? (MenuBuilder) menu : null;
            if (i == 0 && menuBuilder == null) {
                return false;
            }
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(true);
            }
            boolean zOnPreparePanel = super.onPreparePanel(i, view, menu);
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(false);
            }
            return zOnPreparePanel;
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onMenuOpened(int i, Menu menu) {
            super.onMenuOpened(i, menu);
            AppCompatDelegateImpl.this.onMenuOpened(i);
            return true;
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onPanelClosed(int i, Menu menu) {
            super.onPanelClosed(i, menu);
            AppCompatDelegateImpl.this.onPanelClosed(i);
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            if (Build.VERSION.SDK_INT >= 23) {
                return null;
            }
            if (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled()) {
                return startAsSupportActionMode(callback);
            }
            return super.onWindowStartingActionMode(callback);
        }

        final android.view.ActionMode startAsSupportActionMode(ActionMode.Callback callback) {
            SupportActionModeWrapper.CallbackWrapper callbackWrapper = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImpl.this.mContext, callback);
            android.support.v7.view.ActionMode actionModeStartSupportActionMode = AppCompatDelegateImpl.this.startSupportActionMode(callbackWrapper);
            if (actionModeStartSupportActionMode != null) {
                return callbackWrapper.getActionModeWrapper(actionModeStartSupportActionMode);
            }
            return null;
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        @RequiresApi(23)
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
            if (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() && i == 0) {
                return startAsSupportActionMode(callback);
            }
            return super.onWindowStartingActionMode(callback, i);
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        @RequiresApi(24)
        public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
            PanelFeatureState panelState = AppCompatDelegateImpl.this.getPanelState(0, true);
            if (panelState != null && panelState.menu != null) {
                super.onProvideKeyboardShortcuts(list, panelState.menu, i);
            } else {
                super.onProvideKeyboardShortcuts(list, menu, i);
            }
        }
    }

    @VisibleForTesting
    final class AutoNightModeManager {
        private BroadcastReceiver mAutoTimeChangeReceiver;
        private IntentFilter mAutoTimeChangeReceiverFilter;
        private boolean mIsNight;
        private TwilightManager mTwilightManager;

        AutoNightModeManager(@NonNull TwilightManager twilightManager) {
            this.mTwilightManager = twilightManager;
            this.mIsNight = twilightManager.isNight();
        }

        int getApplyableNightMode() {
            this.mIsNight = this.mTwilightManager.isNight();
            return this.mIsNight ? 2 : 1;
        }

        void dispatchTimeChanged() {
            boolean zIsNight = this.mTwilightManager.isNight();
            if (zIsNight != this.mIsNight) {
                this.mIsNight = zIsNight;
                AppCompatDelegateImpl.this.applyDayNight();
            }
        }

        void setup() {
            cleanup();
            if (this.mAutoTimeChangeReceiver == null) {
                this.mAutoTimeChangeReceiver = new BroadcastReceiver() { // from class: android.support.v7.app.AppCompatDelegateImpl.AutoNightModeManager.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        AutoNightModeManager.this.dispatchTimeChanged();
                    }
                };
            }
            if (this.mAutoTimeChangeReceiverFilter == null) {
                this.mAutoTimeChangeReceiverFilter = new IntentFilter();
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_SET");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_TICK");
            }
            AppCompatDelegateImpl.this.mContext.registerReceiver(this.mAutoTimeChangeReceiver, this.mAutoTimeChangeReceiverFilter);
        }

        void cleanup() {
            if (this.mAutoTimeChangeReceiver != null) {
                AppCompatDelegateImpl.this.mContext.unregisterReceiver(this.mAutoTimeChangeReceiver);
                this.mAutoTimeChangeReceiver = null;
            }
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl();
    }

    private class ActionBarDrawableToggleImpl implements ActionBarDrawerToggle.Delegate {
        ActionBarDrawableToggleImpl() {
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public Drawable getThemeUpIndicator() {
            TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(getActionBarThemedContext(), (AttributeSet) null, new int[]{android.support.v7.appcompat.R.attr.homeAsUpIndicator});
            Drawable drawable = tintTypedArrayObtainStyledAttributes.getDrawable(0);
            tintTypedArrayObtainStyledAttributes.recycle();
            return drawable;
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public Context getActionBarThemedContext() {
            return AppCompatDelegateImpl.this.getActionBarThemedContext();
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public boolean isNavigationVisible() {
            ActionBar supportActionBar = AppCompatDelegateImpl.this.getSupportActionBar();
            return (supportActionBar == null || (supportActionBar.getDisplayOptions() & 4) == 0) ? false : true;
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public void setActionBarUpIndicator(Drawable drawable, int i) {
            ActionBar supportActionBar = AppCompatDelegateImpl.this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeAsUpIndicator(drawable);
                supportActionBar.setHomeActionContentDescription(i);
            }
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public void setActionBarDescription(int i) {
            ActionBar supportActionBar = AppCompatDelegateImpl.this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeActionContentDescription(i);
            }
        }
    }
}
