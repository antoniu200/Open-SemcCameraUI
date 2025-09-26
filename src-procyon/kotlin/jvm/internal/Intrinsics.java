// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.UninitializedPropertyAccessException;
import kotlin.KotlinNullPointerException;
import java.util.List;
import java.util.Arrays;
import kotlin.SinceKotlin;

public class Intrinsics
{
    private Intrinsics() {
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final double n, final Double n2) {
        return n2 != null && n == n2;
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final float n, final Float n2) {
        return n2 != null && n == n2;
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final Double n, final double n2) {
        return n != null && n == n2;
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final Double n, final Double n2) {
        final boolean b = false;
        if (n == null) {
            final boolean b2 = b;
            if (n2 != null) {
                return b2;
            }
        }
        else {
            boolean b2 = b;
            if (n2 == null) {
                return b2;
            }
            b2 = b;
            if (n != (double)n2) {
                return b2;
            }
        }
        return true;
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final Float n, final float n2) {
        return n != null && n == n2;
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final Float n, final Float n2) {
        final boolean b = false;
        if (n == null) {
            final boolean b2 = b;
            if (n2 != null) {
                return b2;
            }
        }
        else {
            boolean b2 = b;
            if (n2 == null) {
                return b2;
            }
            b2 = b;
            if (n != (float)n2) {
                return b2;
            }
        }
        return true;
    }
    
    public static boolean areEqual(final Object o, final Object obj) {
        boolean equals;
        if (o == null) {
            equals = (obj == null);
        }
        else {
            equals = o.equals(obj);
        }
        return equals;
    }
    
    public static void checkExpressionValueIsNotNull(final Object o, final String str) {
        if (o == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" must not be null");
            throw sanitizeStackTrace(new IllegalStateException(sb.toString()));
        }
    }
    
    public static void checkFieldIsNotNull(final Object o, final String s) {
        if (o == null) {
            throw sanitizeStackTrace(new IllegalStateException(s));
        }
    }
    
    public static void checkFieldIsNotNull(final Object o, final String str, final String str2) {
        if (o == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field specified as non-null is null: ");
            sb.append(str);
            sb.append(".");
            sb.append(str2);
            throw sanitizeStackTrace(new IllegalStateException(sb.toString()));
        }
    }
    
    public static void checkHasClass(String replace) throws ClassNotFoundException {
        replace = replace.replace('/', '.');
        try {
            Class.forName(replace);
        }
        catch (final ClassNotFoundException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Class ");
            sb.append(replace);
            sb.append(" is not found. Please update the Kotlin runtime to the latest version");
            throw sanitizeStackTrace(new ClassNotFoundException(sb.toString(), ex));
        }
    }
    
    public static void checkHasClass(String replace, final String str) throws ClassNotFoundException {
        replace = replace.replace('/', '.');
        try {
            Class.forName(replace);
        }
        catch (final ClassNotFoundException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Class ");
            sb.append(replace);
            sb.append(" is not found: this code requires the Kotlin runtime of version at least ");
            sb.append(str);
            throw sanitizeStackTrace(new ClassNotFoundException(sb.toString(), ex));
        }
    }
    
    public static void checkNotNull(final Object o) {
        if (o == null) {
            throwNpe();
        }
    }
    
    public static void checkNotNull(final Object o, final String s) {
        if (o == null) {
            throwNpe(s);
        }
    }
    
    public static void checkNotNullExpressionValue(final Object o, final String s) {
        if (o == null) {
            throw sanitizeStackTrace(new IllegalStateException(s));
        }
    }
    
    public static void checkNotNullParameter(final Object o, final String s) {
        if (o == null) {
            throw sanitizeStackTrace(new IllegalArgumentException(s));
        }
    }
    
    public static void checkParameterIsNotNull(final Object o, final String s) {
        if (o == null) {
            throwParameterIsNullException(s);
        }
    }
    
    public static void checkReturnedValueIsNotNull(final Object o, final String s) {
        if (o == null) {
            throw sanitizeStackTrace(new IllegalStateException(s));
        }
    }
    
    public static void checkReturnedValueIsNotNull(final Object o, final String str, final String str2) {
        if (o == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Method specified as non-null returned null: ");
            sb.append(str);
            sb.append(".");
            sb.append(str2);
            throw sanitizeStackTrace(new IllegalStateException(sb.toString()));
        }
    }
    
    public static int compare(int n, final int n2) {
        if (n < n2) {
            n = -1;
        }
        else if (n == n2) {
            n = 0;
        }
        else {
            n = 1;
        }
        return n;
    }
    
    public static int compare(final long n, final long n2) {
        final long n3 = lcmp(n, n2);
        int n4;
        if (n3 < 0) {
            n4 = -1;
        }
        else if (n3 == 0) {
            n4 = 0;
        }
        else {
            n4 = 1;
        }
        return n4;
    }
    
    public static void needClassReification() {
        throwUndefinedForReified();
    }
    
    public static void needClassReification(final String s) {
        throwUndefinedForReified(s);
    }
    
    public static void reifiedOperationMarker(final int n, final String s) {
        throwUndefinedForReified();
    }
    
    public static void reifiedOperationMarker(final int n, final String s, final String s2) {
        throwUndefinedForReified(s2);
    }
    
    private static <T extends Throwable> T sanitizeStackTrace(final T t) {
        return sanitizeStackTrace(t, Intrinsics.class.getName());
    }
    
    static <T extends Throwable> T sanitizeStackTrace(final T t, final String s) {
        final StackTraceElement[] stackTrace = t.getStackTrace();
        final int length = stackTrace.length;
        int n = -1;
        for (int i = 0; i < length; ++i) {
            if (s.equals(stackTrace[i].getClassName())) {
                n = i;
            }
        }
        final List<StackTraceElement> subList = Arrays.asList(stackTrace).subList(n + 1, length);
        t.setStackTrace(subList.toArray(new StackTraceElement[subList.size()]));
        return t;
    }
    
    public static String stringPlus(final String str, final Object obj) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(obj);
        return sb.toString();
    }
    
    public static void throwAssert() {
        throw sanitizeStackTrace(new AssertionError());
    }
    
    public static void throwAssert(final String detailMessage) {
        throw sanitizeStackTrace(new AssertionError((Object)detailMessage));
    }
    
    public static void throwIllegalArgument() {
        throw sanitizeStackTrace(new IllegalArgumentException());
    }
    
    public static void throwIllegalArgument(final String s) {
        throw sanitizeStackTrace(new IllegalArgumentException(s));
    }
    
    public static void throwIllegalState() {
        throw sanitizeStackTrace(new IllegalStateException());
    }
    
    public static void throwIllegalState(final String s) {
        throw sanitizeStackTrace(new IllegalStateException(s));
    }
    
    public static void throwNpe() {
        throw sanitizeStackTrace(new KotlinNullPointerException());
    }
    
    public static void throwNpe(final String s) {
        throw sanitizeStackTrace(new KotlinNullPointerException(s));
    }
    
    private static void throwParameterIsNullException(final String str) {
        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        final String className = stackTraceElement.getClassName();
        final String methodName = stackTraceElement.getMethodName();
        final StringBuilder sb = new StringBuilder();
        sb.append("Parameter specified as non-null is null: method ");
        sb.append(className);
        sb.append(".");
        sb.append(methodName);
        sb.append(", parameter ");
        sb.append(str);
        throw sanitizeStackTrace(new IllegalArgumentException(sb.toString()));
    }
    
    public static void throwUndefinedForReified() {
        throwUndefinedForReified("This function has a reified type parameter and thus can only be inlined at compilation time, not called directly.");
    }
    
    public static void throwUndefinedForReified(final String message) {
        throw new UnsupportedOperationException(message);
    }
    
    public static void throwUninitializedProperty(final String s) {
        throw sanitizeStackTrace(new UninitializedPropertyAccessException(s));
    }
    
    public static void throwUninitializedPropertyAccessException(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("lateinit property ");
        sb.append(str);
        sb.append(" has not been initialized");
        throwUninitializedProperty(sb.toString());
    }
}
