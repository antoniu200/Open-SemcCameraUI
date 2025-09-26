// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import java.text.ParseException;
import com.google.gson.JsonSyntaxException;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.text.DateFormat;
import com.google.gson.TypeAdapterFactory;
import java.util.Date;
import com.google.gson.TypeAdapter;

public final class DateTypeAdapter extends TypeAdapter<Date>
{
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat enUsFormat;
    private final DateFormat iso8601Format;
    private final DateFormat localFormat;
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                Object o;
                if (typeToken.getRawType() == Date.class) {
                    o = new DateTypeAdapter();
                }
                else {
                    o = null;
                }
                return (TypeAdapter<T>)o;
            }
        };
    }
    
    public DateTypeAdapter() {
        this.enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
        this.localFormat = DateFormat.getDateTimeInstance(2, 2);
        this.iso8601Format = buildIso8601Format();
    }
    
    private static DateFormat buildIso8601Format() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }
    
    private Date deserializeToDate(final String source) {
        monitorenter(this);
        try {
            try {
                final Date parse = this.localFormat.parse(source);
                monitorexit(this);
                return parse;
            }
            finally {}
        }
        catch (final ParseException ex) {
            try {
                final Date parse2 = this.enUsFormat.parse(source);
                monitorexit(this);
                return parse2;
            }
            catch (final ParseException ex2) {
                try {
                    final Date parse3 = this.iso8601Format.parse(source);
                    monitorexit(this);
                    return parse3;
                }
                catch (final ParseException ex3) {
                    throw new JsonSyntaxException(source, ex3);
                }
            }
        }
        monitorexit(this);
    }
    
    @Override
    public Date read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return this.deserializeToDate(jsonReader.nextString());
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final Date date) throws IOException {
        monitorenter(this);
        Label_0018: {
            if (date == null) {
                Label_0034: {
                    try {
                        jsonWriter.nullValue();
                        monitorexit(this);
                        return;
                    }
                    finally {
                        break Label_0034;
                    }
                    break Label_0018;
                }
                monitorexit(this);
            }
        }
        jsonWriter.value(this.enUsFormat.format(date));
        monitorexit(this);
    }
}
