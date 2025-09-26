// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.widget;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.util.Log;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.widget.PopupWindow;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class PopupWindowCompat
{
    private static final String TAG = "PopupWindowCompatApi21";
    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Field sOverlapAnchorField;
    private static boolean sOverlapAnchorFieldAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;
    
    private PopupWindowCompat() {
    }
    
    public static boolean getOverlapAnchor(@NonNull final PopupWindow obj) {
        if (Build$VERSION.SDK_INT >= 23) {
            return obj.getOverlapAnchor();
        }
        if (Build$VERSION.SDK_INT >= 21) {
            if (!PopupWindowCompat.sOverlapAnchorFieldAttempted) {
                try {
                    (PopupWindowCompat.sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor")).setAccessible(true);
                }
                catch (final NoSuchFieldException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)ex);
                }
                PopupWindowCompat.sOverlapAnchorFieldAttempted = true;
            }
            if (PopupWindowCompat.sOverlapAnchorField != null) {
                try {
                    return (boolean)PopupWindowCompat.sOverlapAnchorField.get(obj);
                }
                catch (final IllegalAccessException ex2) {
                    Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", (Throwable)ex2);
                }
            }
        }
        return false;
    }
    
    public static int getWindowLayoutType(@NonNull final PopupWindow p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: bipush          23
        //     5: if_icmplt       13
        //     8: aload_0        
        //     9: invokevirtual   android/widget/PopupWindow.getWindowLayoutType:()I
        //    12: ireturn        
        //    13: getstatic       android/support/v4/widget/PopupWindowCompat.sGetWindowLayoutTypeMethodAttempted:Z
        //    16: ifne            44
        //    19: ldc             Landroid/widget/PopupWindow;.class
        //    21: ldc             "getWindowLayoutType"
        //    23: iconst_0       
        //    24: anewarray       Ljava/lang/Class;
        //    27: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    30: putstatic       android/support/v4/widget/PopupWindowCompat.sGetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
        //    33: getstatic       android/support/v4/widget/PopupWindowCompat.sGetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
        //    36: iconst_1       
        //    37: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
        //    40: iconst_1       
        //    41: putstatic       android/support/v4/widget/PopupWindowCompat.sGetWindowLayoutTypeMethodAttempted:Z
        //    44: getstatic       android/support/v4/widget/PopupWindowCompat.sGetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
        //    47: ifnull          70
        //    50: getstatic       android/support/v4/widget/PopupWindowCompat.sGetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
        //    53: aload_0        
        //    54: iconst_0       
        //    55: anewarray       Ljava/lang/Object;
        //    58: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    61: checkcast       Ljava/lang/Integer;
        //    64: invokevirtual   java/lang/Integer.intValue:()I
        //    67: istore_1       
        //    68: iload_1        
        //    69: ireturn        
        //    70: iconst_0       
        //    71: ireturn        
        //    72: astore_2       
        //    73: goto            40
        //    76: astore_0       
        //    77: goto            70
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  19     40     72     76     Ljava/lang/Exception;
        //  50     68     76     80     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0070:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2604)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
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
    
    public static void setOverlapAnchor(@NonNull final PopupWindow obj, final boolean b) {
        if (Build$VERSION.SDK_INT >= 23) {
            obj.setOverlapAnchor(b);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            if (!PopupWindowCompat.sOverlapAnchorFieldAttempted) {
                try {
                    (PopupWindowCompat.sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor")).setAccessible(true);
                }
                catch (final NoSuchFieldException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)ex);
                }
                PopupWindowCompat.sOverlapAnchorFieldAttempted = true;
            }
            if (PopupWindowCompat.sOverlapAnchorField != null) {
                try {
                    PopupWindowCompat.sOverlapAnchorField.set(obj, b);
                }
                catch (final IllegalAccessException ex2) {
                    Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", (Throwable)ex2);
                }
            }
        }
    }
    
    public static void setWindowLayoutType(@NonNull final PopupWindow p0, final int p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: bipush          23
        //     5: if_icmplt       14
        //     8: aload_0        
        //     9: iload_1        
        //    10: invokevirtual   android/widget/PopupWindow.setWindowLayoutType:(I)V
        //    13: return         
        //    14: getstatic       android/support/v4/widget/PopupWindowCompat.sSetWindowLayoutTypeMethodAttempted:Z
        //    17: ifne            51
        //    20: ldc             Landroid/widget/PopupWindow;.class
        //    22: ldc             "setWindowLayoutType"
        //    24: iconst_1       
        //    25: anewarray       Ljava/lang/Class;
        //    28: dup            
        //    29: iconst_0       
        //    30: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
        //    33: aastore        
        //    34: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    37: putstatic       android/support/v4/widget/PopupWindowCompat.sSetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
        //    40: getstatic       android/support/v4/widget/PopupWindowCompat.sSetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
        //    43: iconst_1       
        //    44: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
        //    47: iconst_1       
        //    48: putstatic       android/support/v4/widget/PopupWindowCompat.sSetWindowLayoutTypeMethodAttempted:Z
        //    51: getstatic       android/support/v4/widget/PopupWindowCompat.sSetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
        //    54: ifnull          76
        //    57: getstatic       android/support/v4/widget/PopupWindowCompat.sSetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
        //    60: aload_0        
        //    61: iconst_1       
        //    62: anewarray       Ljava/lang/Object;
        //    65: dup            
        //    66: iconst_0       
        //    67: iload_1        
        //    68: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    71: aastore        
        //    72: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    75: pop            
        //    76: return         
        //    77: astore_2       
        //    78: goto            47
        //    81: astore_0       
        //    82: goto            76
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  20     47     77     81     Ljava/lang/Exception;
        //  57     76     81     85     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0076:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2604)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
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
    
    public static void showAsDropDown(@NonNull final PopupWindow popupWindow, @NonNull final View view, final int n, final int n2, final int n3) {
        if (Build$VERSION.SDK_INT >= 19) {
            popupWindow.showAsDropDown(view, n, n2, n3);
        }
        else {
            int n4 = n;
            if ((GravityCompat.getAbsoluteGravity(n3, ViewCompat.getLayoutDirection(view)) & 0x7) == 0x5) {
                n4 = n - (popupWindow.getWidth() - view.getWidth());
            }
            popupWindow.showAsDropDown(view, n4, n2);
        }
    }
}
