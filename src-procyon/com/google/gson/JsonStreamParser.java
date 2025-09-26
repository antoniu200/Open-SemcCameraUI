// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson;

import java.io.EOFException;
import com.google.gson.internal.Streams;
import java.util.NoSuchElementException;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import com.google.gson.stream.JsonToken;
import java.io.StringReader;
import java.io.Reader;
import com.google.gson.stream.JsonReader;
import java.util.Iterator;

public final class JsonStreamParser implements Iterator<JsonElement>
{
    private final Object lock;
    private final JsonReader parser;
    
    public JsonStreamParser(final Reader reader) {
        (this.parser = new JsonReader(reader)).setLenient(true);
        this.lock = new Object();
    }
    
    public JsonStreamParser(final String s) {
        this(new StringReader(s));
    }
    
    @Override
    public boolean hasNext() {
        final Object lock = this.lock;
        monitorenter(lock);
        try {
            try {
                final boolean b = this.parser.peek() != JsonToken.END_DOCUMENT;
                monitorexit(lock);
                return b;
            }
            finally {
                monitorexit(lock);
            }
        }
        catch (final IOException ex) {}
        catch (final MalformedJsonException ex2) {}
    }
    
    @Override
    public JsonElement next() throws JsonParseException {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return Streams.parse(this.parser);
        }
        catch (final JsonParseException ex) {
            RuntimeException ex2 = ex;
            if (ex.getCause() instanceof EOFException) {
                ex2 = new NoSuchElementException();
            }
            throw ex2;
        }
        catch (final OutOfMemoryError outOfMemoryError) {
            throw new JsonParseException("Failed parsing JSON source to Json", outOfMemoryError);
        }
        catch (final StackOverflowError stackOverflowError) {
            throw new JsonParseException("Failed parsing JSON source to Json", stackOverflowError);
        }
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
