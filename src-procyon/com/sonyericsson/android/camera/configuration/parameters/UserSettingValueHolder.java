// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import java.util.Objects;
import java.lang.reflect.InvocationTargetException;
import com.sonyericsson.android.camera.util.CamLog;

public class UserSettingValueHolder<T extends UserSettingValue>
{
    public static final String DELIMITER = "-";
    public static final String NO_VALUE = "NO_VALUE";
    public static final String REGULAR_EXPRESSION = "@";
    public static final String TAG = "UserSettingValueHolder";
    private boolean mChanged;
    private T mDefaultValue;
    private T[] mOptions;
    private ParameterState mState;
    
    public UserSettingValueHolder(final T mDefaultValue) {
        if (mDefaultValue == null) {
            CamLog.d("Create UserSettingValueHolder with null default value ");
        }
        this.mDefaultValue = mDefaultValue;
        this.mState = (ParameterState)new NormalState(mDefaultValue, null, null);
        this.onApplied();
    }
    
    private T deserialize(final String s) {
        if (s.equals("NO_VALUE")) {
            return null;
        }
        final String[] split = s.split("@");
        if (split.length < 2) {
            return null;
        }
        try {
            final UserSettingValue userSettingValue = Enum.valueOf(Class.forName(split[0]), split[1]);
            return (T)userSettingValue;
        }
        catch (final IllegalArgumentException ex) {
            if (CamLog.VERBOSE) {
                CamLog.w("deserialize failed.", ex);
            }
        }
        catch (final LinkageError linkageError) {
            if (CamLog.VERBOSE) {
                CamLog.w("deserialize failed.", linkageError);
            }
        }
        catch (final ClassNotFoundException ex2) {
            if (CamLog.VERBOSE) {
                CamLog.w("deserialize failed.", ex2);
            }
        }
        catch (final ClassCastException ex3) {
            if (CamLog.VERBOSE) {
                CamLog.w("deserialize failed.", ex3);
            }
        }
        final UserSettingValue userSettingValue = null;
        return (T)userSettingValue;
    }
    
    private ParameterState getParameterState(final String className) {
        try {
            return (ParameterState)Class.forName(className).getConstructor(this.getClass()).newInstance(this);
        }
        catch (final LinkageError linkageError) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", linkageError);
            }
        }
        catch (final NoSuchMethodException ex) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", ex);
            }
        }
        catch (final InvocationTargetException ex2) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", ex2);
            }
        }
        catch (final IllegalAccessException ex3) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", ex3);
            }
        }
        catch (final InstantiationException ex4) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", ex4);
            }
        }
        catch (final SecurityException ex5) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", ex5);
            }
        }
        catch (final IllegalArgumentException ex6) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", ex6);
            }
        }
        catch (final ClassNotFoundException ex7) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", ex7);
            }
        }
        catch (final ClassCastException ex8) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", ex8);
            }
        }
        return null;
    }
    
    private String serialize(final T t) {
        if (t == null) {
            return "NO_VALUE";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getName());
        sb.append("@");
        sb.append(t.toString());
        return sb.toString();
    }
    
    public void applyCurrentValue() {
        this.mState.applyCurrentValue();
    }
    
    public void applyRecommendedValue(final T t) {
        this.mState.applyRecommendedValue(t);
    }
    
    public void canChanged() {
        this.mChanged = true;
    }
    
    public String createValueString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(this.mState.getClass().getName());
        sb.append("-");
        sb.append(this.serialize(this.mState.getCurrentValue()));
        sb.append("-");
        sb.append(this.serialize(this.mState.getOriginalValue()));
        sb.append("-");
        sb.append(this.serialize(this.mState.getRecommendedValue()));
        if (this.mState.getCurrentValue() instanceof UserSettingIntValue) {
            sb.append("-");
            sb.append(String.valueOf(this.mState.getCurrentValue().getInt()));
        }
        return sb.toString();
    }
    
    public boolean forceChange(final T t) {
        return this.mState.forceChange(t);
    }
    
    public T get() {
        return this.mState.getCurrentValue();
    }
    
    public T getDefaultValue() {
        return this.mDefaultValue;
    }
    
    public T[] getOptions() {
        return this.mOptions.clone();
    }
    
    public T getOriginalValue() {
        return this.mState.getOriginalValue();
    }
    
    public T getRecommendedValue() {
        return this.mState.getRecommendedValue();
    }
    
    public boolean hasChanged() {
        return this.mChanged;
    }
    
    public void onApplied() {
        this.mChanged = false;
    }
    
    public void parseValueString(final String s) {
        if (s == null) {
            return;
        }
        if (this.mChanged) {
            return;
        }
        final String[] split = s.split("-");
        if (split.length < 4) {
            return;
        }
        final ParameterState parameterState = this.getParameterState(split[0]);
        if (parameterState == null) {
            return;
        }
        final UserSettingValue deserialize = this.deserialize(split[1]);
        if (deserialize == null) {
            return;
        }
        final UserSettingValue deserialize2 = this.deserialize(split[2]);
        final UserSettingValue deserialize3 = this.deserialize(split[3]);
        if (deserialize instanceof UserSettingIntValue) {
            ((UserSettingIntValue)deserialize).setInt(Integer.parseInt(split[4]));
        }
        parameterState.setCurrentValue((T)deserialize);
        parameterState.setOriginalValue((T)deserialize2);
        parameterState.setRecommendedValue((T)deserialize3);
        this.mState = parameterState;
    }
    
    public boolean reset() {
        return this.mState.reset();
    }
    
    public void set(final T currentValue) {
        this.mState.setCurrentValue(currentValue);
    }
    
    public T setDefaultValue() {
        this.mState.setCurrentValue(this.mDefaultValue);
        return this.mDefaultValue;
    }
    
    public void setOptions(final T[] array) {
        this.mOptions = array.clone();
    }
    
    @Override
    public String toString() {
        if (this.mState == null) {
            return super.toString();
        }
        return String.format("%s(%s|%s|%s)", this.mState.getClass().getSimpleName(), Objects.toString(this.mState.getCurrentValue(), "NO_VALUE"), Objects.toString(this.mState.getOriginalValue(), "NO_VALUE"), Objects.toString(this.mState.getRecommendedValue(), "NO_VALUE"));
    }
    
    public T updateDefaultValue(final T mDefaultValue) {
        this.mDefaultValue = mDefaultValue;
        if (!this.mChanged) {
            this.setDefaultValue();
            this.onApplied();
        }
        return this.mDefaultValue;
    }
    
    private class ForceChangedState extends ParameterState
    {
        final UserSettingValueHolder this$0;
        
        public ForceChangedState(final UserSettingValueHolder this$0) {
            this.this$0 = this$0.super();
        }
        
        public ForceChangedState(final UserSettingValueHolder this$0, final T t, final T t2, final T t3) {
            super(t, t2, t3);
        }
        
        @Override
        public void applyCurrentValue() {
            if (this.mRecommendedValue != null) {
                ((ParameterState)this).setCurrentValue(this.mRecommendedValue);
            }
        }
        
        @Override
        public void applyRecommendedValue(final T currentValue) {
            ((ParameterState)this).setCurrentValue(currentValue);
        }
        
        @Override
        public boolean forceChange(final T currentValue) {
            ((ParameterState)this).setCurrentValue(currentValue);
            return false;
        }
        
        @Override
        public boolean reset() {
            this.this$0.mState = (ParameterState)new NormalState(this.mCurrentValue, null, null);
            this.this$0.mChanged = true;
            return true;
        }
    }
    
    private class NormalState extends ParameterState
    {
        final UserSettingValueHolder this$0;
        
        public NormalState(final UserSettingValueHolder this$0) {
            this.this$0 = this$0.super();
        }
        
        public NormalState(final UserSettingValueHolder this$0, final T t, final T t2, final T t3) {
            super(t, t2, t3);
        }
        
        @Override
        public void applyCurrentValue() {
        }
        
        @Override
        public void applyRecommendedValue(final T t) {
            this.this$0.mState = (ParameterState)new ForceChangedState(t, this.this$0.get(), t);
            this.this$0.mChanged = true;
        }
        
        @Override
        public boolean forceChange(final T t) {
            this.this$0.mState = (ParameterState)new ForceChangedState(t, this.this$0.get(), null);
            this.this$0.mChanged = true;
            return true;
        }
        
        @Override
        public boolean reset() {
            return false;
        }
    }
    
    private abstract class ParameterState
    {
        protected T mCurrentValue;
        protected T mOriginalValue;
        protected T mRecommendedValue;
        final UserSettingValueHolder this$0;
        
        public ParameterState(final UserSettingValueHolder this$0) {
            this.this$0 = this$0;
        }
        
        public ParameterState(final UserSettingValueHolder this$0, final T currentValue, final T originalValue, final T recommendedValue) {
            this.this$0 = this$0;
            this.setCurrentValue(currentValue);
            this.setOriginalValue(originalValue);
            this.setRecommendedValue(recommendedValue);
        }
        
        public abstract void applyCurrentValue();
        
        public abstract void applyRecommendedValue(final T p0);
        
        public void dumpStackTrace() {
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            CamLog.d("## dump stack trace ...");
            for (int i = 1; i < stackTrace.length; ++i) {
                final StringBuilder sb = new StringBuilder();
                sb.append("trace:");
                sb.append(stackTrace[i].getClassName());
                sb.append("#");
                sb.append(stackTrace[i].getMethodName());
                CamLog.d(sb.toString());
            }
        }
        
        public abstract boolean forceChange(final T p0);
        
        public final T getCurrentValue() {
            return this.mCurrentValue;
        }
        
        public final T getOriginalValue() {
            return this.mOriginalValue;
        }
        
        public final T getRecommendedValue() {
            return this.mRecommendedValue;
        }
        
        public abstract boolean reset();
        
        public final void setCurrentValue(final T mCurrentValue) {
            if (this.mCurrentValue != mCurrentValue) {
                this.this$0.mChanged = true;
            }
            if (mCurrentValue == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("[");
                sb.append(this.getClass().getSimpleName());
                sb.append("] setCurrentValue() mCurrentValue: null");
                CamLog.d(sb.toString());
                this.dumpStackTrace();
            }
            this.mCurrentValue = mCurrentValue;
        }
        
        public final void setOriginalValue(final T mOriginalValue) {
            this.mOriginalValue = mOriginalValue;
        }
        
        public final void setRecommendedValue(final T mRecommendedValue) {
            this.mRecommendedValue = mRecommendedValue;
        }
    }
}
