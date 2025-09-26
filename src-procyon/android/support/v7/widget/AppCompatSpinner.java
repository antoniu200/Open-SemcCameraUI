// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.view.ViewTreeObserver;
import android.widget.PopupWindow$OnDismissListener;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.support.v4.view.ViewCompat;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.database.DataSetObserver;
import android.widget.ThemedSpinnerAdapter;
import android.support.v7.content.res.AppCompatResources;
import android.support.annotation.DrawableRes;
import android.widget.ListAdapter;
import android.widget.Adapter;
import android.view.MotionEvent;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.RestrictTo;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.os.Build$VERSION;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.view.ViewGroup;
import android.view.View$MeasureSpec;
import android.graphics.drawable.Drawable;
import android.content.res.Resources$Theme;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.graphics.Rect;
import android.widget.SpinnerAdapter;
import android.content.Context;
import android.support.v4.view.TintableBackgroundView;
import android.widget.Spinner;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView
{
    private static final int[] ATTRS_ANDROID_SPINNERMODE;
    private static final int MAX_ITEMS_MEASURED = 15;
    private static final int MODE_DIALOG = 0;
    private static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "AppCompatSpinner";
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    int mDropDownWidth;
    private ForwardingListener mForwardingListener;
    DropdownPopup mPopup;
    private final Context mPopupContext;
    private final boolean mPopupSet;
    private SpinnerAdapter mTempAdapter;
    final Rect mTempRect;
    
    static {
        ATTRS_ANDROID_SPINNERMODE = new int[] { 16843505 };
    }
    
    public AppCompatSpinner(final Context context) {
        this(context, null);
    }
    
    public AppCompatSpinner(final Context context, final int n) {
        this(context, null, R.attr.spinnerStyle, n);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set) {
        this(context, set, R.attr.spinnerStyle);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set, final int n) {
        this(context, set, n, -1);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set, final int n, final int n2) {
        this(context, set, n, n2, null);
    }
    
    public AppCompatSpinner(final Context p0, final AttributeSet p1, final int p2, final int p3, final Resources$Theme p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: aload_2        
        //     3: iload_3        
        //     4: invokespecial   android/widget/Spinner.<init>:(Landroid/content/Context;Landroid/util/AttributeSet;I)V
        //     7: aload_0        
        //     8: new             Landroid/graphics/Rect;
        //    11: dup            
        //    12: invokespecial   android/graphics/Rect.<init>:()V
        //    15: putfield        android/support/v7/widget/AppCompatSpinner.mTempRect:Landroid/graphics/Rect;
        //    18: aload_1        
        //    19: aload_2        
        //    20: getstatic       android/support/v7/appcompat/R$styleable.Spinner:[I
        //    23: iload_3        
        //    24: iconst_0       
        //    25: invokestatic    android/support/v7/widget/TintTypedArray.obtainStyledAttributes:(Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroid/support/v7/widget/TintTypedArray;
        //    28: astore          10
        //    30: aload_0        
        //    31: new             Landroid/support/v7/widget/AppCompatBackgroundHelper;
        //    34: dup            
        //    35: aload_0        
        //    36: invokespecial   android/support/v7/widget/AppCompatBackgroundHelper.<init>:(Landroid/view/View;)V
        //    39: putfield        android/support/v7/widget/AppCompatSpinner.mBackgroundTintHelper:Landroid/support/v7/widget/AppCompatBackgroundHelper;
        //    42: aload           5
        //    44: ifnull          64
        //    47: aload_0        
        //    48: new             Landroid/support/v7/view/ContextThemeWrapper;
        //    51: dup            
        //    52: aload_1        
        //    53: aload           5
        //    55: invokespecial   android/support/v7/view/ContextThemeWrapper.<init>:(Landroid/content/Context;Landroid/content/res/Resources$Theme;)V
        //    58: putfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //    61: goto            120
        //    64: aload           10
        //    66: getstatic       android/support/v7/appcompat/R$styleable.Spinner_popupTheme:I
        //    69: iconst_0       
        //    70: invokevirtual   android/support/v7/widget/TintTypedArray.getResourceId:(II)I
        //    73: istore          6
        //    75: iload           6
        //    77: ifeq            97
        //    80: aload_0        
        //    81: new             Landroid/support/v7/view/ContextThemeWrapper;
        //    84: dup            
        //    85: aload_1        
        //    86: iload           6
        //    88: invokespecial   android/support/v7/view/ContextThemeWrapper.<init>:(Landroid/content/Context;I)V
        //    91: putfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //    94: goto            120
        //    97: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   100: bipush          23
        //   102: if_icmpge       111
        //   105: aload_1        
        //   106: astore          5
        //   108: goto            114
        //   111: aconst_null    
        //   112: astore          5
        //   114: aload_0        
        //   115: aload           5
        //   117: putfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //   120: aload_0        
        //   121: getfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //   124: ifnull          363
        //   127: iload           4
        //   129: istore          7
        //   131: iload           4
        //   133: iconst_m1      
        //   134: if_icmpne       260
        //   137: aload_1        
        //   138: aload_2        
        //   139: getstatic       android/support/v7/widget/AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE:[I
        //   142: iload_3        
        //   143: iconst_0       
        //   144: invokevirtual   android/content/Context.obtainStyledAttributes:(Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;
        //   147: astore          5
        //   149: iload           4
        //   151: istore          6
        //   153: aload           5
        //   155: astore          8
        //   157: aload           5
        //   159: iconst_0       
        //   160: invokevirtual   android/content/res/TypedArray.hasValue:(I)Z
        //   163: ifeq            179
        //   166: aload           5
        //   168: astore          8
        //   170: aload           5
        //   172: iconst_0       
        //   173: iconst_0       
        //   174: invokevirtual   android/content/res/TypedArray.getInt:(II)I
        //   177: istore          6
        //   179: iload           6
        //   181: istore          7
        //   183: aload           5
        //   185: ifnull          260
        //   188: iload           6
        //   190: istore          4
        //   192: aload           5
        //   194: invokevirtual   android/content/res/TypedArray.recycle:()V
        //   197: iload           4
        //   199: istore          7
        //   201: goto            260
        //   204: astore          9
        //   206: goto            221
        //   209: astore_1       
        //   210: aconst_null    
        //   211: astore          8
        //   213: goto            248
        //   216: astore          9
        //   218: aconst_null    
        //   219: astore          5
        //   221: aload           5
        //   223: astore          8
        //   225: ldc             "AppCompatSpinner"
        //   227: ldc             "Could not read android:spinnerMode"
        //   229: aload           9
        //   231: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   234: pop            
        //   235: iload           4
        //   237: istore          7
        //   239: aload           5
        //   241: ifnull          260
        //   244: goto            192
        //   247: astore_1       
        //   248: aload           8
        //   250: ifnull          258
        //   253: aload           8
        //   255: invokevirtual   android/content/res/TypedArray.recycle:()V
        //   258: aload_1        
        //   259: athrow         
        //   260: iload           7
        //   262: iconst_1       
        //   263: if_icmpne       363
        //   266: new             Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;
        //   269: dup            
        //   270: aload_0        
        //   271: aload_0        
        //   272: getfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //   275: aload_2        
        //   276: iload_3        
        //   277: invokespecial   android/support/v7/widget/AppCompatSpinner$DropdownPopup.<init>:(Landroid/support/v7/widget/AppCompatSpinner;Landroid/content/Context;Landroid/util/AttributeSet;I)V
        //   280: astore          8
        //   282: aload_0        
        //   283: getfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //   286: aload_2        
        //   287: getstatic       android/support/v7/appcompat/R$styleable.Spinner:[I
        //   290: iload_3        
        //   291: iconst_0       
        //   292: invokestatic    android/support/v7/widget/TintTypedArray.obtainStyledAttributes:(Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroid/support/v7/widget/TintTypedArray;
        //   295: astore          5
        //   297: aload_0        
        //   298: aload           5
        //   300: getstatic       android/support/v7/appcompat/R$styleable.Spinner_android_dropDownWidth:I
        //   303: bipush          -2
        //   305: invokevirtual   android/support/v7/widget/TintTypedArray.getLayoutDimension:(II)I
        //   308: putfield        android/support/v7/widget/AppCompatSpinner.mDropDownWidth:I
        //   311: aload           8
        //   313: aload           5
        //   315: getstatic       android/support/v7/appcompat/R$styleable.Spinner_android_popupBackground:I
        //   318: invokevirtual   android/support/v7/widget/TintTypedArray.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   321: invokevirtual   android/support/v7/widget/AppCompatSpinner$DropdownPopup.setBackgroundDrawable:(Landroid/graphics/drawable/Drawable;)V
        //   324: aload           8
        //   326: aload           10
        //   328: getstatic       android/support/v7/appcompat/R$styleable.Spinner_android_prompt:I
        //   331: invokevirtual   android/support/v7/widget/TintTypedArray.getString:(I)Ljava/lang/String;
        //   334: invokevirtual   android/support/v7/widget/AppCompatSpinner$DropdownPopup.setPromptText:(Ljava/lang/CharSequence;)V
        //   337: aload           5
        //   339: invokevirtual   android/support/v7/widget/TintTypedArray.recycle:()V
        //   342: aload_0        
        //   343: aload           8
        //   345: putfield        android/support/v7/widget/AppCompatSpinner.mPopup:Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;
        //   348: aload_0        
        //   349: new             Landroid/support/v7/widget/AppCompatSpinner$1;
        //   352: dup            
        //   353: aload_0        
        //   354: aload_0        
        //   355: aload           8
        //   357: invokespecial   android/support/v7/widget/AppCompatSpinner$1.<init>:(Landroid/support/v7/widget/AppCompatSpinner;Landroid/view/View;Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;)V
        //   360: putfield        android/support/v7/widget/AppCompatSpinner.mForwardingListener:Landroid/support/v7/widget/ForwardingListener;
        //   363: aload           10
        //   365: getstatic       android/support/v7/appcompat/R$styleable.Spinner_android_entries:I
        //   368: invokevirtual   android/support/v7/widget/TintTypedArray.getTextArray:(I)[Ljava/lang/CharSequence;
        //   371: astore          5
        //   373: aload           5
        //   375: ifnull          403
        //   378: new             Landroid/widget/ArrayAdapter;
        //   381: dup            
        //   382: aload_1        
        //   383: ldc             17367048
        //   385: aload           5
        //   387: invokespecial   android/widget/ArrayAdapter.<init>:(Landroid/content/Context;I[Ljava/lang/Object;)V
        //   390: astore_1       
        //   391: aload_1        
        //   392: getstatic       android/support/v7/appcompat/R$layout.support_simple_spinner_dropdown_item:I
        //   395: invokevirtual   android/widget/ArrayAdapter.setDropDownViewResource:(I)V
        //   398: aload_0        
        //   399: aload_1        
        //   400: invokevirtual   android/support/v7/widget/AppCompatSpinner.setAdapter:(Landroid/widget/SpinnerAdapter;)V
        //   403: aload           10
        //   405: invokevirtual   android/support/v7/widget/TintTypedArray.recycle:()V
        //   408: aload_0        
        //   409: iconst_1       
        //   410: putfield        android/support/v7/widget/AppCompatSpinner.mPopupSet:Z
        //   413: aload_0        
        //   414: getfield        android/support/v7/widget/AppCompatSpinner.mTempAdapter:Landroid/widget/SpinnerAdapter;
        //   417: ifnull          433
        //   420: aload_0        
        //   421: aload_0        
        //   422: getfield        android/support/v7/widget/AppCompatSpinner.mTempAdapter:Landroid/widget/SpinnerAdapter;
        //   425: invokevirtual   android/support/v7/widget/AppCompatSpinner.setAdapter:(Landroid/widget/SpinnerAdapter;)V
        //   428: aload_0        
        //   429: aconst_null    
        //   430: putfield        android/support/v7/widget/AppCompatSpinner.mTempAdapter:Landroid/widget/SpinnerAdapter;
        //   433: aload_0        
        //   434: getfield        android/support/v7/widget/AppCompatSpinner.mBackgroundTintHelper:Landroid/support/v7/widget/AppCompatBackgroundHelper;
        //   437: aload_2        
        //   438: iload_3        
        //   439: invokevirtual   android/support/v7/widget/AppCompatBackgroundHelper.loadFromAttributes:(Landroid/util/AttributeSet;I)V
        //   442: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  137    149    216    221    Ljava/lang/Exception;
        //  137    149    209    216    Any
        //  157    166    204    209    Ljava/lang/Exception;
        //  157    166    247    248    Any
        //  170    179    204    209    Ljava/lang/Exception;
        //  170    179    247    248    Any
        //  225    235    247    248    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0179:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2604)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:799)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:635)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    int compatMeasureContentWidth(final SpinnerAdapter spinnerAdapter, final Drawable drawable) {
        int n = 0;
        if (spinnerAdapter == null) {
            return 0;
        }
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 0);
        final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 0);
        final int max = Math.max(0, this.getSelectedItemPosition());
        final int min = Math.min(spinnerAdapter.getCount(), max + 15);
        int i = Math.max(0, max - (15 - (min - max)));
        int max2 = 0;
        View view = null;
        while (i < min) {
            final int itemViewType = spinnerAdapter.getItemViewType(i);
            int n2;
            if (itemViewType != (n2 = n)) {
                view = null;
                n2 = itemViewType;
            }
            view = spinnerAdapter.getView(i, view, (ViewGroup)this);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
            }
            view.measure(measureSpec, measureSpec2);
            max2 = Math.max(max2, view.getMeasuredWidth());
            ++i;
            n = n2;
        }
        int n3 = max2;
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            n3 = max2 + (this.mTempRect.left + this.mTempRect.right);
        }
        return n3;
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.applySupportBackgroundTint();
        }
    }
    
    public int getDropDownHorizontalOffset() {
        if (this.mPopup != null) {
            return this.mPopup.getHorizontalOffset();
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getDropDownHorizontalOffset();
        }
        return 0;
    }
    
    public int getDropDownVerticalOffset() {
        if (this.mPopup != null) {
            return this.mPopup.getVerticalOffset();
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getDropDownVerticalOffset();
        }
        return 0;
    }
    
    public int getDropDownWidth() {
        if (this.mPopup != null) {
            return this.mDropDownWidth;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getDropDownWidth();
        }
        return 0;
    }
    
    public Drawable getPopupBackground() {
        if (this.mPopup != null) {
            return this.mPopup.getBackground();
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getPopupBackground();
        }
        return null;
    }
    
    public Context getPopupContext() {
        if (this.mPopup != null) {
            return this.mPopupContext;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            return super.getPopupContext();
        }
        return null;
    }
    
    public CharSequence getPrompt() {
        CharSequence charSequence;
        if (this.mPopup != null) {
            charSequence = this.mPopup.getHintText();
        }
        else {
            charSequence = super.getPrompt();
        }
        return charSequence;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ColorStateList getSupportBackgroundTintList() {
        ColorStateList supportBackgroundTintList;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintList = this.mBackgroundTintHelper.getSupportBackgroundTintList();
        }
        else {
            supportBackgroundTintList = null;
        }
        return supportBackgroundTintList;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        PorterDuff$Mode supportBackgroundTintMode;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintMode = this.mBackgroundTintHelper.getSupportBackgroundTintMode();
        }
        else {
            supportBackgroundTintMode = null;
        }
        return supportBackgroundTintMode;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mPopup != null && this.mPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        if (this.mPopup != null && View$MeasureSpec.getMode(n) == Integer.MIN_VALUE) {
            this.setMeasuredDimension(Math.min(Math.max(this.getMeasuredWidth(), this.compatMeasureContentWidth(this.getAdapter(), this.getBackground())), View$MeasureSpec.getSize(n)), this.getMeasuredHeight());
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return (this.mForwardingListener != null && this.mForwardingListener.onTouch((View)this, motionEvent)) || super.onTouchEvent(motionEvent);
    }
    
    public boolean performClick() {
        if (this.mPopup != null) {
            if (!this.mPopup.isShowing()) {
                this.mPopup.show();
            }
            return true;
        }
        return super.performClick();
    }
    
    public void setAdapter(final SpinnerAdapter spinnerAdapter) {
        if (!this.mPopupSet) {
            this.mTempAdapter = spinnerAdapter;
            return;
        }
        super.setAdapter(spinnerAdapter);
        if (this.mPopup != null) {
            Context context;
            if (this.mPopupContext == null) {
                context = this.getContext();
            }
            else {
                context = this.mPopupContext;
            }
            this.mPopup.setAdapter((ListAdapter)new DropDownAdapter(spinnerAdapter, context.getTheme()));
        }
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundDrawable(backgroundDrawable);
        }
    }
    
    public void setBackgroundResource(@DrawableRes final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundResource(backgroundResource);
        }
    }
    
    public void setDropDownHorizontalOffset(final int n) {
        if (this.mPopup != null) {
            this.mPopup.setHorizontalOffset(n);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setDropDownHorizontalOffset(n);
        }
    }
    
    public void setDropDownVerticalOffset(final int n) {
        if (this.mPopup != null) {
            this.mPopup.setVerticalOffset(n);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setDropDownVerticalOffset(n);
        }
    }
    
    public void setDropDownWidth(final int n) {
        if (this.mPopup != null) {
            this.mDropDownWidth = n;
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setDropDownWidth(n);
        }
    }
    
    public void setPopupBackgroundDrawable(final Drawable drawable) {
        if (this.mPopup != null) {
            this.mPopup.setBackgroundDrawable(drawable);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setPopupBackgroundDrawable(drawable);
        }
    }
    
    public void setPopupBackgroundResource(@DrawableRes final int n) {
        this.setPopupBackgroundDrawable(AppCompatResources.getDrawable(this.getPopupContext(), n));
    }
    
    public void setPrompt(final CharSequence charSequence) {
        if (this.mPopup != null) {
            this.mPopup.setPromptText(charSequence);
        }
        else {
            super.setPrompt(charSequence);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintList(@Nullable final ColorStateList supportBackgroundTintList) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintMode(@Nullable final PorterDuff$Mode supportBackgroundTintMode) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
    
    private static class DropDownAdapter implements ListAdapter, SpinnerAdapter
    {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;
        
        public DropDownAdapter(@Nullable final SpinnerAdapter mAdapter, @Nullable final Resources$Theme resources$Theme) {
            this.mAdapter = mAdapter;
            if (mAdapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter)mAdapter;
            }
            if (resources$Theme != null) {
                if (Build$VERSION.SDK_INT >= 23 && mAdapter instanceof ThemedSpinnerAdapter) {
                    final ThemedSpinnerAdapter themedSpinnerAdapter = (ThemedSpinnerAdapter)mAdapter;
                    if (themedSpinnerAdapter.getDropDownViewTheme() != resources$Theme) {
                        themedSpinnerAdapter.setDropDownViewTheme(resources$Theme);
                    }
                }
                else if (mAdapter instanceof android.support.v7.widget.ThemedSpinnerAdapter) {
                    final android.support.v7.widget.ThemedSpinnerAdapter themedSpinnerAdapter2 = (android.support.v7.widget.ThemedSpinnerAdapter)mAdapter;
                    if (themedSpinnerAdapter2.getDropDownViewTheme() == null) {
                        themedSpinnerAdapter2.setDropDownViewTheme(resources$Theme);
                    }
                }
            }
        }
        
        public boolean areAllItemsEnabled() {
            final ListAdapter mListAdapter = this.mListAdapter;
            return mListAdapter == null || mListAdapter.areAllItemsEnabled();
        }
        
        public int getCount() {
            int count;
            if (this.mAdapter == null) {
                count = 0;
            }
            else {
                count = this.mAdapter.getCount();
            }
            return count;
        }
        
        public View getDropDownView(final int n, View dropDownView, final ViewGroup viewGroup) {
            if (this.mAdapter == null) {
                dropDownView = null;
            }
            else {
                dropDownView = this.mAdapter.getDropDownView(n, dropDownView, viewGroup);
            }
            return dropDownView;
        }
        
        public Object getItem(final int n) {
            Object item;
            if (this.mAdapter == null) {
                item = null;
            }
            else {
                item = this.mAdapter.getItem(n);
            }
            return item;
        }
        
        public long getItemId(final int n) {
            long itemId;
            if (this.mAdapter == null) {
                itemId = -1L;
            }
            else {
                itemId = this.mAdapter.getItemId(n);
            }
            return itemId;
        }
        
        public int getItemViewType(final int n) {
            return 0;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            return this.getDropDownView(n, view, viewGroup);
        }
        
        public int getViewTypeCount() {
            return 1;
        }
        
        public boolean hasStableIds() {
            return this.mAdapter != null && this.mAdapter.hasStableIds();
        }
        
        public boolean isEmpty() {
            return this.getCount() == 0;
        }
        
        public boolean isEnabled(final int n) {
            final ListAdapter mListAdapter = this.mListAdapter;
            return mListAdapter == null || mListAdapter.isEnabled(n);
        }
        
        public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
            if (this.mAdapter != null) {
                this.mAdapter.registerDataSetObserver(dataSetObserver);
            }
        }
        
        public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
            if (this.mAdapter != null) {
                this.mAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }
    
    private class DropdownPopup extends ListPopupWindow
    {
        ListAdapter mAdapter;
        private CharSequence mHintText;
        private final Rect mVisibleRect;
        final AppCompatSpinner this$0;
        
        public DropdownPopup(final AppCompatSpinner appCompatSpinner, final Context context, final AttributeSet set, final int n) {
            this.this$0 = appCompatSpinner;
            super(context, set, n);
            this.mVisibleRect = new Rect();
            this.setAnchorView((View)appCompatSpinner);
            this.setModal(true);
            this.setPromptPosition(0);
            this.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener(this, appCompatSpinner) {
                final DropdownPopup this$1;
                final AppCompatSpinner val$this$0;
                
                public void onItemClick(final AdapterView<?> adapterView, final View view, final int selection, final long n) {
                    this.this$1.this$0.setSelection(selection);
                    if (this.this$1.this$0.getOnItemClickListener() != null) {
                        this.this$1.this$0.performItemClick(view, selection, this.this$1.mAdapter.getItemId(selection));
                    }
                    this.this$1.dismiss();
                }
            });
        }
        
        void computeContentWidth() {
            final Drawable background = this.getBackground();
            int right = 0;
            if (background != null) {
                background.getPadding(this.this$0.mTempRect);
                if (ViewUtils.isLayoutRtl((View)this.this$0)) {
                    right = this.this$0.mTempRect.right;
                }
                else {
                    right = -this.this$0.mTempRect.left;
                }
            }
            else {
                final Rect mTempRect = this.this$0.mTempRect;
                this.this$0.mTempRect.right = 0;
                mTempRect.left = 0;
            }
            final int paddingLeft = this.this$0.getPaddingLeft();
            final int paddingRight = this.this$0.getPaddingRight();
            final int width = this.this$0.getWidth();
            if (this.this$0.mDropDownWidth == -2) {
                final int compatMeasureContentWidth = this.this$0.compatMeasureContentWidth((SpinnerAdapter)this.mAdapter, this.getBackground());
                final int n = this.this$0.getContext().getResources().getDisplayMetrics().widthPixels - this.this$0.mTempRect.left - this.this$0.mTempRect.right;
                int a;
                if ((a = compatMeasureContentWidth) > n) {
                    a = n;
                }
                this.setContentWidth(Math.max(a, width - paddingLeft - paddingRight));
            }
            else if (this.this$0.mDropDownWidth == -1) {
                this.setContentWidth(width - paddingLeft - paddingRight);
            }
            else {
                this.setContentWidth(this.this$0.mDropDownWidth);
            }
            int horizontalOffset;
            if (ViewUtils.isLayoutRtl((View)this.this$0)) {
                horizontalOffset = right + (width - paddingRight - this.getWidth());
            }
            else {
                horizontalOffset = right + paddingLeft;
            }
            this.setHorizontalOffset(horizontalOffset);
        }
        
        public CharSequence getHintText() {
            return this.mHintText;
        }
        
        boolean isVisibleToUser(final View view) {
            return ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(this.mVisibleRect);
        }
        
        @Override
        public void setAdapter(final ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }
        
        public void setPromptText(final CharSequence mHintText) {
            this.mHintText = mHintText;
        }
        
        @Override
        public void show() {
            final boolean showing = this.isShowing();
            this.computeContentWidth();
            this.setInputMethodMode(2);
            super.show();
            this.getListView().setChoiceMode(1);
            this.setSelection(this.this$0.getSelectedItemPosition());
            if (showing) {
                return;
            }
            final ViewTreeObserver viewTreeObserver = this.this$0.getViewTreeObserver();
            if (viewTreeObserver != null) {
                final ViewTreeObserver$OnGlobalLayoutListener viewTreeObserver$OnGlobalLayoutListener = (ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener(this) {
                    final DropdownPopup this$1;
                    
                    public void onGlobalLayout() {
                        if (!this.this$1.isVisibleToUser((View)this.this$1.this$0)) {
                            this.this$1.dismiss();
                        }
                        else {
                            this.this$1.computeContentWidth();
                            this.this$1.show();
                        }
                    }
                };
                viewTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)viewTreeObserver$OnGlobalLayoutListener);
                this.setOnDismissListener((PopupWindow$OnDismissListener)new PopupWindow$OnDismissListener(this, viewTreeObserver$OnGlobalLayoutListener) {
                    final DropdownPopup this$1;
                    final ViewTreeObserver$OnGlobalLayoutListener val$layoutListener;
                    
                    public void onDismiss() {
                        final ViewTreeObserver viewTreeObserver = this.this$1.this$0.getViewTreeObserver();
                        if (viewTreeObserver != null) {
                            viewTreeObserver.removeGlobalOnLayoutListener(this.val$layoutListener);
                        }
                    }
                });
            }
        }
    }
}
