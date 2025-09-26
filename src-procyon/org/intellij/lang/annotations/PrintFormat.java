// 
// Decompiled by Procyon v0.6.0
// 

package org.intellij.lang.annotations;

@Pattern("(?:[^%]|%%|(?:%(?:\\d+\\$)?(?:[-#+ 0,(<]*)?(?:\\d+)?(?:\\.\\d+)?(?:[tT])?(?:[a-zA-Z%])))*")
public @interface PrintFormat {
}
