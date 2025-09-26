// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.support.v7.widget.TintContextWrapper;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatButton;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.support.v7.appcompat.R;
import android.view.InflateException;
import android.content.res.TypedArray;
import android.view.View$OnClickListener;
import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import android.content.ContextWrapper;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import java.lang.reflect.Constructor;
import java.util.Map;

public class AppCompatViewInflater
{
    private static final String LOG_TAG = "AppCompatViewInflater";
    private static final String[] sClassPrefixList;
    private static final Map<String, Constructor<? extends View>> sConstructorMap;
    private static final Class<?>[] sConstructorSignature;
    private static final int[] sOnClickAttrs;
    private final Object[] mConstructorArgs;
    
    static {
        sConstructorSignature = new Class[] { Context.class, AttributeSet.class };
        sOnClickAttrs = new int[] { 16843375 };
        sClassPrefixList = new String[] { "android.widget.", "android.view.", "android.webkit." };
        sConstructorMap = new ArrayMap<String, Constructor<? extends View>>();
    }
    
    public AppCompatViewInflater() {
        this.mConstructorArgs = new Object[2];
    }
    
    private void checkOnClickListener(final View view, final AttributeSet set) {
        final Context context = view.getContext();
        if (context instanceof ContextWrapper && (Build$VERSION.SDK_INT < 15 || ViewCompat.hasOnClickListeners(view))) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, AppCompatViewInflater.sOnClickAttrs);
            final String string = obtainStyledAttributes.getString(0);
            if (string != null) {
                view.setOnClickListener((View$OnClickListener)new DeclaredOnClickListener(view, string));
            }
            obtainStyledAttributes.recycle();
        }
    }
    
    private View createViewByPrefix(final Context context, final String str, final String str2) throws ClassNotFoundException, InflateException {
        Label_0094: {
            Constructor<? extends View> constructor;
            if ((constructor = AppCompatViewInflater.sConstructorMap.get(str)) != null) {
                break Label_0094;
            }
            try {
                final ClassLoader classLoader = context.getClassLoader();
                String string;
                if (str2 != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(str);
                    string = sb.toString();
                }
                else {
                    string = str;
                }
                constructor = classLoader.loadClass(string).asSubclass(View.class).getConstructor(AppCompatViewInflater.sConstructorSignature);
                AppCompatViewInflater.sConstructorMap.put(str, constructor);
                constructor.setAccessible(true);
                return (View)constructor.newInstance(this.mConstructorArgs);
            }
            catch (final Exception ex) {
                return null;
            }
        }
    }
    
    private View createViewFromTag(final Context context, final String s, final AttributeSet set) {
        String attributeValue = s;
        if (s.equals("view")) {
            attributeValue = set.getAttributeValue((String)null, "class");
        }
        try {
            this.mConstructorArgs[0] = context;
            this.mConstructorArgs[1] = set;
            if (-1 == attributeValue.indexOf(46)) {
                for (int i = 0; i < AppCompatViewInflater.sClassPrefixList.length; ++i) {
                    final View viewByPrefix = this.createViewByPrefix(context, attributeValue, AppCompatViewInflater.sClassPrefixList[i]);
                    if (viewByPrefix != null) {
                        return viewByPrefix;
                    }
                }
                return null;
            }
            return this.createViewByPrefix(context, attributeValue, null);
        }
        catch (final Exception ex) {
            return null;
        }
        finally {
            this.mConstructorArgs[0] = null;
            this.mConstructorArgs[1] = null;
        }
    }
    
    private static Context themifyContext(final Context context, final AttributeSet set, final boolean b, final boolean b2) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.View, 0, 0);
        int resourceId;
        if (b) {
            resourceId = obtainStyledAttributes.getResourceId(R.styleable.View_android_theme, 0);
        }
        else {
            resourceId = 0;
        }
        int n = resourceId;
        if (b2 && (n = resourceId) == 0) {
            final int resourceId2 = obtainStyledAttributes.getResourceId(R.styleable.View_theme, 0);
            if ((n = resourceId2) != 0) {
                Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
                n = resourceId2;
            }
        }
        obtainStyledAttributes.recycle();
        Object o = context;
        if (n != 0) {
            if (context instanceof ContextThemeWrapper) {
                o = context;
                if (((ContextThemeWrapper)context).getThemeResId() == n) {
                    return (Context)o;
                }
            }
            o = new ContextThemeWrapper(context, n);
        }
        return (Context)o;
    }
    
    private void verifyNotNull(final View view, final String str) {
        if (view == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getClass().getName());
            sb.append(" asked to inflate view for <");
            sb.append(str);
            sb.append(">, but returned null");
            throw new IllegalStateException(sb.toString());
        }
    }
    
    @NonNull
    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(final Context context, final AttributeSet set) {
        return new AppCompatAutoCompleteTextView(context, set);
    }
    
    @NonNull
    protected AppCompatButton createButton(final Context context, final AttributeSet set) {
        return new AppCompatButton(context, set);
    }
    
    @NonNull
    protected AppCompatCheckBox createCheckBox(final Context context, final AttributeSet set) {
        return new AppCompatCheckBox(context, set);
    }
    
    @NonNull
    protected AppCompatCheckedTextView createCheckedTextView(final Context context, final AttributeSet set) {
        return new AppCompatCheckedTextView(context, set);
    }
    
    @NonNull
    protected AppCompatEditText createEditText(final Context context, final AttributeSet set) {
        return new AppCompatEditText(context, set);
    }
    
    @NonNull
    protected AppCompatImageButton createImageButton(final Context context, final AttributeSet set) {
        return new AppCompatImageButton(context, set);
    }
    
    @NonNull
    protected AppCompatImageView createImageView(final Context context, final AttributeSet set) {
        return new AppCompatImageView(context, set);
    }
    
    @NonNull
    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(final Context context, final AttributeSet set) {
        return new AppCompatMultiAutoCompleteTextView(context, set);
    }
    
    @NonNull
    protected AppCompatRadioButton createRadioButton(final Context context, final AttributeSet set) {
        return new AppCompatRadioButton(context, set);
    }
    
    @NonNull
    protected AppCompatRatingBar createRatingBar(final Context context, final AttributeSet set) {
        return new AppCompatRatingBar(context, set);
    }
    
    @NonNull
    protected AppCompatSeekBar createSeekBar(final Context context, final AttributeSet set) {
        return new AppCompatSeekBar(context, set);
    }
    
    @NonNull
    protected AppCompatSpinner createSpinner(final Context context, final AttributeSet set) {
        return new AppCompatSpinner(context, set);
    }
    
    @NonNull
    protected AppCompatTextView createTextView(final Context context, final AttributeSet set) {
        return new AppCompatTextView(context, set);
    }
    
    @Nullable
    protected View createView(final Context context, final String s, final AttributeSet set) {
        return null;
    }
    
    final View createView(View o, final String s, @NonNull final Context context, @NonNull final AttributeSet set, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        Context context2;
        if (b && o != null) {
            context2 = ((View)o).getContext();
        }
        else {
            context2 = context;
        }
        Context themifyContext = null;
        Label_0046: {
            if (!b2) {
                themifyContext = context2;
                if (!b3) {
                    break Label_0046;
                }
            }
            themifyContext = themifyContext(context2, set, b2, b3);
        }
        Context wrap = themifyContext;
        if (b4) {
            wrap = TintContextWrapper.wrap(themifyContext);
        }
        switch (s) {
            default: {
                o = this.createView(wrap, s, set);
                break;
            }
            case "SeekBar": {
                o = this.createSeekBar(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "RatingBar": {
                o = this.createRatingBar(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "MultiAutoCompleteTextView": {
                o = this.createMultiAutoCompleteTextView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "AutoCompleteTextView": {
                o = this.createAutoCompleteTextView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "CheckedTextView": {
                o = this.createCheckedTextView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "RadioButton": {
                o = this.createRadioButton(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "CheckBox": {
                o = this.createCheckBox(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "ImageButton": {
                o = this.createImageButton(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "Spinner": {
                o = this.createSpinner(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "EditText": {
                o = this.createEditText(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "Button": {
                o = this.createButton(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "ImageView": {
                o = this.createImageView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "TextView": {
                o = this.createTextView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
        }
        Object viewFromTag = o;
        if (o == null) {
            viewFromTag = o;
            if (context != wrap) {
                viewFromTag = this.createViewFromTag(wrap, s, set);
            }
        }
        if (viewFromTag != null) {
            this.checkOnClickListener((View)viewFromTag, set);
        }
        return (View)viewFromTag;
    }
    
    private static class DeclaredOnClickListener implements View$OnClickListener
    {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;
        
        public DeclaredOnClickListener(@NonNull final View mHostView, @NonNull final String mMethodName) {
            this.mHostView = mHostView;
            this.mMethodName = mMethodName;
        }
        
        @NonNull
        private void resolveMethod(@Nullable Context context, @NonNull final String s) {
        Label_0047_Outer:
            while (true) {
                Label_0070: {
                    if (context == null) {
                        break Label_0070;
                    }
                    while (true) {
                        try {
                            if (!context.isRestricted()) {
                                final Method method = context.getClass().getMethod(this.mMethodName, View.class);
                                if (method != null) {
                                    this.mResolvedMethod = method;
                                    this.mResolvedContext = context;
                                    return;
                                }
                            }
                            if (context instanceof ContextWrapper) {
                                context = ((ContextWrapper)context).getBaseContext();
                                continue Label_0047_Outer;
                            }
                            context = null;
                            continue Label_0047_Outer;
                            while (true) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Could not find method ");
                                sb.append(this.mMethodName);
                                sb.append("(View) in a parent or ancestor Context for android:onClick ");
                                sb.append("attribute defined on view ");
                                sb.append(this.mHostView.getClass());
                                sb.append((String)context);
                                throw new IllegalStateException(sb.toString());
                                Label_0089: {
                                    context = (Context)new StringBuilder();
                                }
                                ((StringBuilder)context).append(" with id '");
                                final int id;
                                ((StringBuilder)context).append(this.mHostView.getContext().getResources().getResourceEntryName(id));
                                ((StringBuilder)context).append("'");
                                context = (Context)((StringBuilder)context).toString();
                                continue;
                                id = this.mHostView.getId();
                                iftrue(Label_0089:)(id != -1);
                                context = (Context)"";
                                continue;
                            }
                        }
                        catch (final NoSuchMethodException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        public void onClick(@NonNull final View view) {
            if (this.mResolvedMethod == null) {
                this.resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mResolvedMethod.invoke(this.mResolvedContext, view);
            }
            catch (final InvocationTargetException cause) {
                throw new IllegalStateException("Could not execute method for android:onClick", cause);
            }
            catch (final IllegalAccessException cause2) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", cause2);
            }
        }
    }
}
