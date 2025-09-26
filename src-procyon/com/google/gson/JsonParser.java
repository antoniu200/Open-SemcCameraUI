// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson;

import java.io.StringReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

public final class JsonParser
{
    public JsonElement parse(final JsonReader jsonReader) throws JsonIOException, JsonSyntaxException {
        final boolean lenient = jsonReader.isLenient();
        jsonReader.setLenient(true);
        try {
            try {
                final JsonElement parse = Streams.parse(jsonReader);
                jsonReader.setLenient(lenient);
                return parse;
            }
            finally {}
        }
        catch (final OutOfMemoryError outOfMemoryError) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed parsing JSON source: ");
            sb.append(jsonReader);
            sb.append(" to Json");
            throw new JsonParseException(sb.toString(), outOfMemoryError);
        }
        catch (final StackOverflowError stackOverflowError) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed parsing JSON source: ");
            sb2.append(jsonReader);
            sb2.append(" to Json");
            throw new JsonParseException(sb2.toString(), stackOverflowError);
        }
        jsonReader.setLenient(lenient);
    }
    
    public JsonElement parse(final Reader reader) throws JsonIOException, JsonSyntaxException {
        try {
            final JsonReader jsonReader = new JsonReader(reader);
            final JsonElement parse = this.parse(jsonReader);
            if (!parse.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonSyntaxException("Did not consume the entire document.");
            }
            return parse;
        }
        catch (final NumberFormatException ex) {
            throw new JsonSyntaxException(ex);
        }
        catch (final IOException ex2) {
            throw new JsonIOException(ex2);
        }
        catch (final MalformedJsonException ex3) {
            throw new JsonSyntaxException(ex3);
        }
    }
    
    public JsonElement parse(final String s) throws JsonSyntaxException {
        return this.parse(new StringReader(s));
    }
}
