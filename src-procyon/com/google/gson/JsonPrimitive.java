// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson;

import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonPrimitive extends JsonElement
{
    private static final Class<?>[] PRIMITIVE_TYPES;
    private Object value;
    
    static {
        PRIMITIVE_TYPES = new Class[] { Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };
    }
    
    public JsonPrimitive(final Boolean value) {
        this.setValue(value);
    }
    
    public JsonPrimitive(final Character value) {
        this.setValue(value);
    }
    
    public JsonPrimitive(final Number value) {
        this.setValue(value);
    }
    
    JsonPrimitive(final Object value) {
        this.setValue(value);
    }
    
    public JsonPrimitive(final String value) {
        this.setValue(value);
    }
    
    private static boolean isIntegral(final JsonPrimitive jsonPrimitive) {
        final boolean b = jsonPrimitive.value instanceof Number;
        boolean b2 = false;
        if (b) {
            final Number n = (Number)jsonPrimitive.value;
            if (n instanceof BigInteger || n instanceof Long || n instanceof Integer || n instanceof Short || n instanceof Byte) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    private static boolean isPrimitiveOrString(final Object o) {
        if (o instanceof String) {
            return true;
        }
        final Class<?> class1 = o.getClass();
        final Class<?>[] primitive_TYPES = JsonPrimitive.PRIMITIVE_TYPES;
        for (int length = primitive_TYPES.length, i = 0; i < length; ++i) {
            if (primitive_TYPES[i].isAssignableFrom(class1)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    JsonPrimitive deepCopy() {
        return this;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        final boolean b2 = true;
        final boolean b3 = true;
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final JsonPrimitive jsonPrimitive = (JsonPrimitive)o;
        if (this.value == null) {
            return jsonPrimitive.value == null && b3;
        }
        if (isIntegral(this) && isIntegral(jsonPrimitive)) {
            if (this.getAsNumber().longValue() != jsonPrimitive.getAsNumber().longValue()) {
                b = false;
            }
            return b;
        }
        if (this.value instanceof Number && jsonPrimitive.value instanceof Number) {
            final double doubleValue = this.getAsNumber().doubleValue();
            final double doubleValue2 = jsonPrimitive.getAsNumber().doubleValue();
            boolean b4 = b2;
            if (doubleValue != doubleValue2) {
                b4 = (Double.isNaN(doubleValue) && Double.isNaN(doubleValue2) && b2);
            }
            return b4;
        }
        return this.value.equals(jsonPrimitive.value);
    }
    
    @Override
    public BigDecimal getAsBigDecimal() {
        BigDecimal bigDecimal;
        if (this.value instanceof BigDecimal) {
            bigDecimal = (BigDecimal)this.value;
        }
        else {
            bigDecimal = new BigDecimal(this.value.toString());
        }
        return bigDecimal;
    }
    
    @Override
    public BigInteger getAsBigInteger() {
        BigInteger bigInteger;
        if (this.value instanceof BigInteger) {
            bigInteger = (BigInteger)this.value;
        }
        else {
            bigInteger = new BigInteger(this.value.toString());
        }
        return bigInteger;
    }
    
    @Override
    public boolean getAsBoolean() {
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper();
        }
        return Boolean.parseBoolean(this.getAsString());
    }
    
    @Override
    Boolean getAsBooleanWrapper() {
        return (Boolean)this.value;
    }
    
    @Override
    public byte getAsByte() {
        byte b;
        if (this.isNumber()) {
            b = this.getAsNumber().byteValue();
        }
        else {
            b = Byte.parseByte(this.getAsString());
        }
        return b;
    }
    
    @Override
    public char getAsCharacter() {
        return this.getAsString().charAt(0);
    }
    
    @Override
    public double getAsDouble() {
        double n;
        if (this.isNumber()) {
            n = this.getAsNumber().doubleValue();
        }
        else {
            n = Double.parseDouble(this.getAsString());
        }
        return n;
    }
    
    @Override
    public float getAsFloat() {
        float n;
        if (this.isNumber()) {
            n = this.getAsNumber().floatValue();
        }
        else {
            n = Float.parseFloat(this.getAsString());
        }
        return n;
    }
    
    @Override
    public int getAsInt() {
        int n;
        if (this.isNumber()) {
            n = this.getAsNumber().intValue();
        }
        else {
            n = Integer.parseInt(this.getAsString());
        }
        return n;
    }
    
    @Override
    public long getAsLong() {
        long n;
        if (this.isNumber()) {
            n = this.getAsNumber().longValue();
        }
        else {
            n = Long.parseLong(this.getAsString());
        }
        return n;
    }
    
    @Override
    public Number getAsNumber() {
        Number n;
        if (this.value instanceof String) {
            n = new LazilyParsedNumber((String)this.value);
        }
        else {
            n = (Number)this.value;
        }
        return n;
    }
    
    @Override
    public short getAsShort() {
        short n;
        if (this.isNumber()) {
            n = this.getAsNumber().shortValue();
        }
        else {
            n = Short.parseShort(this.getAsString());
        }
        return n;
    }
    
    @Override
    public String getAsString() {
        if (this.isNumber()) {
            return this.getAsNumber().toString();
        }
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper().toString();
        }
        return (String)this.value;
    }
    
    @Override
    public int hashCode() {
        if (this.value == null) {
            return 31;
        }
        if (isIntegral(this)) {
            final long longValue = this.getAsNumber().longValue();
            return (int)(longValue >>> 32 ^ longValue);
        }
        if (this.value instanceof Number) {
            final long doubleToLongBits = Double.doubleToLongBits(this.getAsNumber().doubleValue());
            return (int)(doubleToLongBits >>> 32 ^ doubleToLongBits);
        }
        return this.value.hashCode();
    }
    
    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }
    
    public boolean isNumber() {
        return this.value instanceof Number;
    }
    
    public boolean isString() {
        return this.value instanceof String;
    }
    
    void setValue(final Object value) {
        if (value instanceof Character) {
            this.value = String.valueOf((char)value);
        }
        else {
            $Gson$Preconditions.checkArgument(value instanceof Number || isPrimitiveOrString(value));
            this.value = value;
        }
    }
}
