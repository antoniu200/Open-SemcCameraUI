package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.util.CamLog;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class UserSettingValueHolder<T extends UserSettingValue> {
    public static final String DELIMITER = "-";
    public static final String NO_VALUE = "NO_VALUE";
    public static final String REGULAR_EXPRESSION = "@";
    public static final String TAG = "UserSettingValueHolder";
    private boolean mChanged;
    private T mDefaultValue;
    private T[] mOptions;
    private UserSettingValueHolder<T>.ParameterState mState;

    public UserSettingValueHolder(T t) {
        if (t == null) {
            CamLog.d("Create UserSettingValueHolder with null default value ");
        }
        this.mDefaultValue = t;
        this.mState = new NormalState(t, null, null);
        onApplied();
    }

    public void setOptions(T[] tArr) {
        this.mOptions = (T[]) ((UserSettingValue[]) tArr.clone());
    }

    public T[] getOptions() {
        return (T[]) ((UserSettingValue[]) this.mOptions.clone());
    }

    public void set(T t) {
        this.mState.setCurrentValue(t);
    }

    public T get() {
        return (T) this.mState.getCurrentValue();
    }

    public T getOriginalValue() {
        return (T) this.mState.getOriginalValue();
    }

    public T getRecommendedValue() {
        return (T) this.mState.getRecommendedValue();
    }

    public boolean forceChange(T t) {
        return this.mState.forceChange(t);
    }

    public boolean reset() {
        return this.mState.reset();
    }

    public void applyRecommendedValue(T t) {
        this.mState.applyRecommendedValue(t);
    }

    public void applyCurrentValue() {
        this.mState.applyCurrentValue();
    }

    public T setDefaultValue() {
        this.mState.setCurrentValue(this.mDefaultValue);
        return this.mDefaultValue;
    }

    public T updateDefaultValue(T t) {
        this.mDefaultValue = t;
        if (!this.mChanged) {
            setDefaultValue();
            onApplied();
        }
        return this.mDefaultValue;
    }

    public T getDefaultValue() {
        return this.mDefaultValue;
    }

    public boolean hasChanged() {
        return this.mChanged;
    }

    public void onApplied() {
        this.mChanged = false;
    }

    public void canChanged() {
        this.mChanged = true;
    }

    private abstract class ParameterState {
        protected T mCurrentValue;
        protected T mOriginalValue;
        protected T mRecommendedValue;

        public abstract void applyCurrentValue();

        public abstract void applyRecommendedValue(T t);

        public abstract boolean forceChange(T t);

        public abstract boolean reset();

        public ParameterState() {
        }

        public ParameterState(T t, T t2, T t3) {
            setCurrentValue(t);
            setOriginalValue(t2);
            setRecommendedValue(t3);
        }

        public void dumpStackTrace() {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            CamLog.d("## dump stack trace ...");
            for (int i = 1; i < stackTrace.length; i++) {
                CamLog.d("trace:" + stackTrace[i].getClassName() + "#" + stackTrace[i].getMethodName());
            }
        }

        public final void setCurrentValue(T t) {
            if (this.mCurrentValue != t) {
                UserSettingValueHolder.this.mChanged = true;
            }
            if (t == null) {
                CamLog.d("[" + getClass().getSimpleName() + "] setCurrentValue() mCurrentValue: null");
                dumpStackTrace();
            }
            this.mCurrentValue = t;
        }

        public final T getCurrentValue() {
            return this.mCurrentValue;
        }

        public final T getOriginalValue() {
            return this.mOriginalValue;
        }

        public final void setOriginalValue(T t) {
            this.mOriginalValue = t;
        }

        public final T getRecommendedValue() {
            return this.mRecommendedValue;
        }

        public final void setRecommendedValue(T t) {
            this.mRecommendedValue = t;
        }
    }

    private class NormalState extends UserSettingValueHolder<T>.ParameterState {
        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder.ParameterState
        public void applyCurrentValue() {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder.ParameterState
        public boolean reset() {
            return false;
        }

        public NormalState() {
            super();
        }

        public NormalState(T t, T t2, T t3) {
            super(t, t2, t3);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder.ParameterState
        public boolean forceChange(T t) {
            UserSettingValueHolder.this.mState = new ForceChangedState(t, UserSettingValueHolder.this.get(), null);
            UserSettingValueHolder.this.mChanged = true;
            return true;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder.ParameterState
        public void applyRecommendedValue(T t) {
            UserSettingValueHolder.this.mState = new ForceChangedState(t, UserSettingValueHolder.this.get(), t);
            UserSettingValueHolder.this.mChanged = true;
        }
    }

    private class ForceChangedState extends UserSettingValueHolder<T>.ParameterState {
        public ForceChangedState() {
            super();
        }

        public ForceChangedState(T t, T t2, T t3) {
            super(t, t2, t3);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder.ParameterState
        public boolean forceChange(T t) {
            setCurrentValue(t);
            return false;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder.ParameterState
        public boolean reset() {
            UserSettingValueHolder.this.mState = new NormalState(this.mCurrentValue, null, null);
            UserSettingValueHolder.this.mChanged = true;
            return true;
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder.ParameterState
        public void applyRecommendedValue(T t) {
            setCurrentValue(t);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder.ParameterState
        public void applyCurrentValue() {
            if (this.mRecommendedValue != null) {
                setCurrentValue(this.mRecommendedValue);
            }
        }
    }

    public String createValueString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.mState.getClass().getName());
        stringBuffer.append(DELIMITER);
        stringBuffer.append(serialize(this.mState.getCurrentValue()));
        stringBuffer.append(DELIMITER);
        stringBuffer.append(serialize(this.mState.getOriginalValue()));
        stringBuffer.append(DELIMITER);
        stringBuffer.append(serialize(this.mState.getRecommendedValue()));
        if (this.mState.getCurrentValue() instanceof UserSettingIntValue) {
            stringBuffer.append(DELIMITER);
            stringBuffer.append(String.valueOf(((UserSettingIntValue) this.mState.getCurrentValue()).getInt()));
        }
        return stringBuffer.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void parseValueString(String str) {
        UserSettingValueHolder<T>.ParameterState parameterState;
        UserSettingValue userSettingValueDeserialize;
        if (str == null || this.mChanged) {
            return;
        }
        String[] strArrSplit = str.split(DELIMITER);
        if (strArrSplit.length < 4 || (parameterState = getParameterState(strArrSplit[0])) == 0 || (userSettingValueDeserialize = deserialize(strArrSplit[1])) == null) {
            return;
        }
        UserSettingValue userSettingValueDeserialize2 = deserialize(strArrSplit[2]);
        UserSettingValue userSettingValueDeserialize3 = deserialize(strArrSplit[3]);
        if (userSettingValueDeserialize instanceof UserSettingIntValue) {
            ((UserSettingIntValue) userSettingValueDeserialize).setInt(Integer.parseInt(strArrSplit[4]));
        }
        parameterState.setCurrentValue(userSettingValueDeserialize);
        parameterState.setOriginalValue(userSettingValueDeserialize2);
        parameterState.setRecommendedValue(userSettingValueDeserialize3);
        this.mState = parameterState;
    }

    private String serialize(T t) {
        if (t == null) {
            return NO_VALUE;
        }
        return t.getClass().getName() + "@" + t.toString();
    }

    private T deserialize(String str) {
        if (str.equals(NO_VALUE)) {
            return null;
        }
        String[] strArrSplit = str.split("@");
        if (strArrSplit.length < 2) {
            return null;
        }
        try {
            return (T) Enum.valueOf(Class.forName(strArrSplit[0]), strArrSplit[1]);
        } catch (ClassCastException e) {
            if (CamLog.VERBOSE) {
                CamLog.w("deserialize failed.", e);
            }
            return null;
        } catch (ClassNotFoundException e2) {
            if (CamLog.VERBOSE) {
                CamLog.w("deserialize failed.", e2);
            }
            return null;
        } catch (IllegalArgumentException e3) {
            if (CamLog.VERBOSE) {
                CamLog.w("deserialize failed.", e3);
            }
            return null;
        } catch (LinkageError e4) {
            if (CamLog.VERBOSE) {
                CamLog.w("deserialize failed.", e4);
            }
            return null;
        }
    }

    private UserSettingValueHolder<T>.ParameterState getParameterState(String str) {
        try {
            return (ParameterState) Class.forName(str).getConstructor(getClass()).newInstance(this);
        } catch (ClassCastException e) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", e);
            }
            return null;
        } catch (ClassNotFoundException e2) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", e2);
            }
            return null;
        } catch (IllegalAccessException e3) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", e3);
            }
            return null;
        } catch (IllegalArgumentException e4) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", e4);
            }
            return null;
        } catch (InstantiationException e5) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", e5);
            }
            return null;
        } catch (LinkageError e6) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", e6);
            }
            return null;
        } catch (NoSuchMethodException e7) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", e7);
            }
            return null;
        } catch (SecurityException e8) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", e8);
            }
            return null;
        } catch (InvocationTargetException e9) {
            if (CamLog.VERBOSE) {
                CamLog.d("getParameterState failed", e9);
            }
            return null;
        }
    }

    public String toString() {
        return this.mState == null ? super.toString() : String.format("%s(%s|%s|%s)", this.mState.getClass().getSimpleName(), Objects.toString(this.mState.getCurrentValue(), NO_VALUE), Objects.toString(this.mState.getOriginalValue(), NO_VALUE), Objects.toString(this.mState.getRecommendedValue(), NO_VALUE));
    }
}
