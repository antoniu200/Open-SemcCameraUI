// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import com.google.gson.JsonElement;
import java.io.Writer;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

public final class JsonTreeWriter extends JsonWriter
{
    private static final JsonPrimitive SENTINEL_CLOSED;
    private static final Writer UNWRITABLE_WRITER;
    private String pendingName;
    private JsonElement product;
    private final List<JsonElement> stack;
    
    static {
        UNWRITABLE_WRITER = new Writer() {
            @Override
            public void close() throws IOException {
                throw new AssertionError();
            }
            
            @Override
            public void flush() throws IOException {
                throw new AssertionError();
            }
            
            @Override
            public void write(final char[] array, final int n, final int n2) {
                throw new AssertionError();
            }
        };
        SENTINEL_CLOSED = new JsonPrimitive("closed");
    }
    
    public JsonTreeWriter() {
        super(JsonTreeWriter.UNWRITABLE_WRITER);
        this.stack = new ArrayList<JsonElement>();
        this.product = JsonNull.INSTANCE;
    }
    
    private JsonElement peek() {
        return this.stack.get(this.stack.size() - 1);
    }
    
    private void put(final JsonElement product) {
        if (this.pendingName != null) {
            if (!product.isJsonNull() || this.getSerializeNulls()) {
                ((JsonObject)this.peek()).add(this.pendingName, product);
            }
            this.pendingName = null;
        }
        else if (this.stack.isEmpty()) {
            this.product = product;
        }
        else {
            final JsonElement peek = this.peek();
            if (!(peek instanceof JsonArray)) {
                throw new IllegalStateException();
            }
            ((JsonArray)peek).add(product);
        }
    }
    
    @Override
    public JsonWriter beginArray() throws IOException {
        final JsonArray jsonArray = new JsonArray();
        this.put(jsonArray);
        this.stack.add(jsonArray);
        return this;
    }
    
    @Override
    public JsonWriter beginObject() throws IOException {
        final JsonObject jsonObject = new JsonObject();
        this.put(jsonObject);
        this.stack.add(jsonObject);
        return this;
    }
    
    @Override
    public void close() throws IOException {
        if (!this.stack.isEmpty()) {
            throw new IOException("Incomplete document");
        }
        this.stack.add(JsonTreeWriter.SENTINEL_CLOSED);
    }
    
    @Override
    public JsonWriter endArray() throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        if (this.peek() instanceof JsonArray) {
            this.stack.remove(this.stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public JsonWriter endObject() throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        if (this.peek() instanceof JsonObject) {
            this.stack.remove(this.stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public void flush() throws IOException {
    }
    
    public JsonElement get() {
        if (!this.stack.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected one JSON element but was ");
            sb.append(this.stack);
            throw new IllegalStateException(sb.toString());
        }
        return this.product;
    }
    
    @Override
    public JsonWriter name(final String pendingName) throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        if (this.peek() instanceof JsonObject) {
            this.pendingName = pendingName;
            return this;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public JsonWriter nullValue() throws IOException {
        this.put(JsonNull.INSTANCE);
        return this;
    }
    
    @Override
    public JsonWriter value(final double n) throws IOException {
        if (!this.isLenient() && (Double.isNaN(n) || Double.isInfinite(n))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("JSON forbids NaN and infinities: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        this.put(new JsonPrimitive(n));
        return this;
    }
    
    @Override
    public JsonWriter value(final long l) throws IOException {
        this.put(new JsonPrimitive(l));
        return this;
    }
    
    @Override
    public JsonWriter value(final Number obj) throws IOException {
        if (obj == null) {
            return this.nullValue();
        }
        if (!this.isLenient()) {
            final double doubleValue = obj.doubleValue();
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("JSON forbids NaN and infinities: ");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        this.put(new JsonPrimitive(obj));
        return this;
    }
    
    @Override
    public JsonWriter value(final String s) throws IOException {
        if (s == null) {
            return this.nullValue();
        }
        this.put(new JsonPrimitive(s));
        return this;
    }
    
    @Override
    public JsonWriter value(final boolean b) throws IOException {
        this.put(new JsonPrimitive(b));
        return this;
    }
}
