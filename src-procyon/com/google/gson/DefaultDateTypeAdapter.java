// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson;

import java.sql.Timestamp;
import java.lang.reflect.Type;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;

final class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>
{
    private final DateFormat enUsFormat;
    private final DateFormat iso8601Format;
    private final DateFormat localFormat;
    
    DefaultDateTypeAdapter() {
        this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
    }
    
    DefaultDateTypeAdapter(final int n) {
        this(DateFormat.getDateInstance(n, Locale.US), DateFormat.getDateInstance(n));
    }
    
    public DefaultDateTypeAdapter(final int n, final int n2) {
        this(DateFormat.getDateTimeInstance(n, n2, Locale.US), DateFormat.getDateTimeInstance(n, n2));
    }
    
    DefaultDateTypeAdapter(final String s) {
        this(new SimpleDateFormat(s, Locale.US), new SimpleDateFormat(s));
    }
    
    DefaultDateTypeAdapter(final DateFormat enUsFormat, final DateFormat localFormat) {
        this.enUsFormat = enUsFormat;
        this.localFormat = localFormat;
        (this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)).setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    private Date deserializeToDate(final JsonElement p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/google/gson/DefaultDateTypeAdapter.localFormat:Ljava/text/DateFormat;
        //     4: astore_2       
        //     5: aload_2        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: getfield        com/google/gson/DefaultDateTypeAdapter.localFormat:Ljava/text/DateFormat;
        //    11: aload_1        
        //    12: invokevirtual   com/google/gson/JsonElement.getAsString:()Ljava/lang/String;
        //    15: invokevirtual   java/text/DateFormat.parse:(Ljava/lang/String;)Ljava/util/Date;
        //    18: astore_3       
        //    19: aload_2        
        //    20: monitorexit    
        //    21: aload_3        
        //    22: areturn        
        //    23: astore_1       
        //    24: goto            80
        //    27: astore_3       
        //    28: aload_0        
        //    29: getfield        com/google/gson/DefaultDateTypeAdapter.enUsFormat:Ljava/text/DateFormat;
        //    32: aload_1        
        //    33: invokevirtual   com/google/gson/JsonElement.getAsString:()Ljava/lang/String;
        //    36: invokevirtual   java/text/DateFormat.parse:(Ljava/lang/String;)Ljava/util/Date;
        //    39: astore_3       
        //    40: aload_2        
        //    41: monitorexit    
        //    42: aload_3        
        //    43: areturn        
        //    44: astore_3       
        //    45: aload_0        
        //    46: getfield        com/google/gson/DefaultDateTypeAdapter.iso8601Format:Ljava/text/DateFormat;
        //    49: aload_1        
        //    50: invokevirtual   com/google/gson/JsonElement.getAsString:()Ljava/lang/String;
        //    53: invokevirtual   java/text/DateFormat.parse:(Ljava/lang/String;)Ljava/util/Date;
        //    56: astore_3       
        //    57: aload_2        
        //    58: monitorexit    
        //    59: aload_3        
        //    60: areturn        
        //    61: astore_3       
        //    62: new             Lcom/google/gson/JsonSyntaxException;
        //    65: astore          4
        //    67: aload           4
        //    69: aload_1        
        //    70: invokevirtual   com/google/gson/JsonElement.getAsString:()Ljava/lang/String;
        //    73: aload_3        
        //    74: invokespecial   com/google/gson/JsonSyntaxException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    77: aload           4
        //    79: athrow         
        //    80: aload_2        
        //    81: monitorexit    
        //    82: aload_1        
        //    83: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                      
        //  -----  -----  -----  -----  --------------------------
        //  7      19     27     80     Ljava/text/ParseException;
        //  7      19     23     84     Any
        //  19     21     23     84     Any
        //  28     40     44     80     Ljava/text/ParseException;
        //  28     40     23     84     Any
        //  40     42     23     84     Any
        //  45     57     61     80     Ljava/text/ParseException;
        //  45     57     23     84     Any
        //  57     59     23     84     Any
        //  62     80     23     84     Any
        //  80     82     23     84     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 53 out of bounds for length 53
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:385)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:427)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3362)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3611)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:112)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
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
    
    @Override
    public Date deserialize(final JsonElement jsonElement, final Type obj, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!(jsonElement instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        final Date deserializeToDate = this.deserializeToDate(jsonElement);
        if (obj == Date.class) {
            return deserializeToDate;
        }
        if (obj == Timestamp.class) {
            return new Timestamp(deserializeToDate.getTime());
        }
        if (obj == java.sql.Date.class) {
            return new java.sql.Date(deserializeToDate.getTime());
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass());
        sb.append(" cannot deserialize to ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public JsonElement serialize(final Date date, final Type type, final JsonSerializationContext jsonSerializationContext) {
        synchronized (this.localFormat) {
            return new JsonPrimitive(this.enUsFormat.format(date));
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(DefaultDateTypeAdapter.class.getSimpleName());
        sb.append('(');
        sb.append(this.localFormat.getClass().getSimpleName());
        sb.append(')');
        return sb.toString();
    }
}
