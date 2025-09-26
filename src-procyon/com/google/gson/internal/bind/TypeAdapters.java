// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal.bind;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.StringTokenizer;
import java.util.GregorianCalendar;
import java.util.Date;
import java.sql.Timestamp;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.net.URISyntaxException;
import com.google.gson.JsonIOException;
import com.google.gson.internal.LazilyParsedNumber;
import java.lang.constant.Constable;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import java.util.UUID;
import java.net.URL;
import java.net.URI;
import java.util.Locale;
import com.google.gson.JsonElement;
import java.net.InetAddress;
import java.util.Calendar;
import com.google.gson.TypeAdapterFactory;
import java.util.BitSet;
import java.math.BigInteger;
import java.math.BigDecimal;
import com.google.gson.TypeAdapter;

public final class TypeAdapters
{
    public static final TypeAdapter<BigDecimal> BIG_DECIMAL;
    public static final TypeAdapter<BigInteger> BIG_INTEGER;
    public static final TypeAdapter<BitSet> BIT_SET;
    public static final TypeAdapterFactory BIT_SET_FACTORY;
    public static final TypeAdapter<Boolean> BOOLEAN;
    public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING;
    public static final TypeAdapterFactory BOOLEAN_FACTORY;
    public static final TypeAdapter<Number> BYTE;
    public static final TypeAdapterFactory BYTE_FACTORY;
    public static final TypeAdapter<Calendar> CALENDAR;
    public static final TypeAdapterFactory CALENDAR_FACTORY;
    public static final TypeAdapter<Character> CHARACTER;
    public static final TypeAdapterFactory CHARACTER_FACTORY;
    public static final TypeAdapter<Class> CLASS;
    public static final TypeAdapterFactory CLASS_FACTORY;
    public static final TypeAdapter<Number> DOUBLE;
    public static final TypeAdapterFactory ENUM_FACTORY;
    public static final TypeAdapter<Number> FLOAT;
    public static final TypeAdapter<InetAddress> INET_ADDRESS;
    public static final TypeAdapterFactory INET_ADDRESS_FACTORY;
    public static final TypeAdapter<Number> INTEGER;
    public static final TypeAdapterFactory INTEGER_FACTORY;
    public static final TypeAdapter<JsonElement> JSON_ELEMENT;
    public static final TypeAdapterFactory JSON_ELEMENT_FACTORY;
    public static final TypeAdapter<Locale> LOCALE;
    public static final TypeAdapterFactory LOCALE_FACTORY;
    public static final TypeAdapter<Number> LONG;
    public static final TypeAdapter<Number> NUMBER;
    public static final TypeAdapterFactory NUMBER_FACTORY;
    public static final TypeAdapter<Number> SHORT;
    public static final TypeAdapterFactory SHORT_FACTORY;
    public static final TypeAdapter<String> STRING;
    public static final TypeAdapter<StringBuffer> STRING_BUFFER;
    public static final TypeAdapterFactory STRING_BUFFER_FACTORY;
    public static final TypeAdapter<StringBuilder> STRING_BUILDER;
    public static final TypeAdapterFactory STRING_BUILDER_FACTORY;
    public static final TypeAdapterFactory STRING_FACTORY;
    public static final TypeAdapterFactory TIMESTAMP_FACTORY;
    public static final TypeAdapter<URI> URI;
    public static final TypeAdapterFactory URI_FACTORY;
    public static final TypeAdapter<URL> URL;
    public static final TypeAdapterFactory URL_FACTORY;
    public static final TypeAdapter<UUID> UUID;
    public static final TypeAdapterFactory UUID_FACTORY;
    
    static {
        CLASS = new TypeAdapter<Class>() {
            @Override
            public Class read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Class clazz) throws IOException {
                if (clazz == null) {
                    jsonWriter.nullValue();
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Attempted to serialize java.lang.Class: ");
                sb.append(clazz.getName());
                sb.append(". Forgot to register a type adapter?");
                throw new UnsupportedOperationException(sb.toString());
            }
        };
        CLASS_FACTORY = newFactory(Class.class, TypeAdapters.CLASS);
        BIT_SET = new TypeAdapter<BitSet>() {
            @Override
            public BitSet read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                final BitSet set = new BitSet();
                jsonReader.beginArray();
                Constable str = jsonReader.peek();
                int bitIndex = 0;
                while (str != JsonToken.END_ARRAY) {
                    final int n = TypeAdapters$32.$SwitchMap$com$google$gson$stream$JsonToken[((Enum)str).ordinal()];
                    boolean nextBoolean = true;
                    while (true) {
                        switch (n) {
                            default: {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Invalid bitset value type: ");
                                sb.append(str);
                                throw new JsonSyntaxException(sb.toString());
                            }
                            case 3: {
                                str = jsonReader.nextString();
                                try {
                                    if (Integer.parseInt((String)str) != 0) {
                                        break;
                                    }
                                    nextBoolean = false;
                                    break;
                                }
                                catch (final NumberFormatException ex) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Error: Expecting: bitset number value (1, 0), Found: ");
                                    sb2.append((String)str);
                                    throw new JsonSyntaxException(sb2.toString());
                                }
                            }
                            case 2: {
                                nextBoolean = jsonReader.nextBoolean();
                                break;
                            }
                            case 1: {
                                if (jsonReader.nextInt() != 0) {
                                    break;
                                }
                                continue;
                            }
                        }
                        break;
                    }
                    if (nextBoolean) {
                        set.set(bitIndex);
                    }
                    ++bitIndex;
                    str = jsonReader.peek();
                }
                jsonReader.endArray();
                return set;
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final BitSet set) throws IOException {
                if (set == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.beginArray();
                for (int i = 0; i < set.length(); ++i) {
                    jsonWriter.value(set.get(i) ? 1 : 0);
                }
                jsonWriter.endArray();
            }
        };
        BIT_SET_FACTORY = newFactory(BitSet.class, TypeAdapters.BIT_SET);
        BOOLEAN = new TypeAdapter<Boolean>() {
            @Override
            public Boolean read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                if (jsonReader.peek() == JsonToken.STRING) {
                    return Boolean.parseBoolean(jsonReader.nextString());
                }
                return jsonReader.nextBoolean();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Boolean b) throws IOException {
                if (b == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.value(b);
            }
        };
        BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
            @Override
            public Boolean read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return Boolean.valueOf(jsonReader.nextString());
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Boolean b) throws IOException {
                String string;
                if (b == null) {
                    string = "null";
                }
                else {
                    string = b.toString();
                }
                jsonWriter.value(string);
            }
        };
        BOOLEAN_FACTORY = newFactory(Boolean.TYPE, Boolean.class, TypeAdapters.BOOLEAN);
        BYTE = new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return (byte)jsonReader.nextInt();
                }
                catch (final NumberFormatException ex) {
                    throw new JsonSyntaxException(ex);
                }
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        BYTE_FACTORY = newFactory(Byte.TYPE, Byte.class, TypeAdapters.BYTE);
        SHORT = new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return (short)jsonReader.nextInt();
                }
                catch (final NumberFormatException ex) {
                    throw new JsonSyntaxException(ex);
                }
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        SHORT_FACTORY = newFactory(Short.TYPE, Short.class, TypeAdapters.SHORT);
        INTEGER = new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return jsonReader.nextInt();
                }
                catch (final NumberFormatException ex) {
                    throw new JsonSyntaxException(ex);
                }
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        INTEGER_FACTORY = newFactory(Integer.TYPE, Integer.class, TypeAdapters.INTEGER);
        LONG = new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return jsonReader.nextLong();
                }
                catch (final NumberFormatException ex) {
                    throw new JsonSyntaxException(ex);
                }
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        FLOAT = new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return (float)jsonReader.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        DOUBLE = new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        NUMBER = new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader jsonReader) throws IOException {
                final JsonToken peek = jsonReader.peek();
                final int n = TypeAdapters$32.$SwitchMap$com$google$gson$stream$JsonToken[peek.ordinal()];
                if (n == 1) {
                    return new LazilyParsedNumber(jsonReader.nextString());
                }
                if (n != 4) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Expecting number, got: ");
                    sb.append(peek);
                    throw new JsonSyntaxException(sb.toString());
                }
                jsonReader.nextNull();
                return null;
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        NUMBER_FACTORY = newFactory(Number.class, TypeAdapters.NUMBER);
        CHARACTER = new TypeAdapter<Character>() {
            @Override
            public Character read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                final String nextString = jsonReader.nextString();
                if (nextString.length() != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Expecting character, got: ");
                    sb.append(nextString);
                    throw new JsonSyntaxException(sb.toString());
                }
                return nextString.charAt(0);
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Character obj) throws IOException {
                String value;
                if (obj == null) {
                    value = null;
                }
                else {
                    value = String.valueOf(obj);
                }
                jsonWriter.value(value);
            }
        };
        CHARACTER_FACTORY = newFactory(Character.TYPE, Character.class, TypeAdapters.CHARACTER);
        STRING = new TypeAdapter<String>() {
            @Override
            public String read(final JsonReader jsonReader) throws IOException {
                final JsonToken peek = jsonReader.peek();
                if (peek == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                if (peek == JsonToken.BOOLEAN) {
                    return Boolean.toString(jsonReader.nextBoolean());
                }
                return jsonReader.nextString();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final String s) throws IOException {
                jsonWriter.value(s);
            }
        };
        BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
            @Override
            public BigDecimal read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return new BigDecimal(jsonReader.nextString());
                }
                catch (final NumberFormatException ex) {
                    throw new JsonSyntaxException(ex);
                }
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final BigDecimal bigDecimal) throws IOException {
                jsonWriter.value(bigDecimal);
            }
        };
        BIG_INTEGER = new TypeAdapter<BigInteger>() {
            @Override
            public BigInteger read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return new BigInteger(jsonReader.nextString());
                }
                catch (final NumberFormatException ex) {
                    throw new JsonSyntaxException(ex);
                }
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final BigInteger bigInteger) throws IOException {
                jsonWriter.value(bigInteger);
            }
        };
        STRING_FACTORY = newFactory(String.class, TypeAdapters.STRING);
        STRING_BUILDER = new TypeAdapter<StringBuilder>() {
            @Override
            public StringBuilder read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return new StringBuilder(jsonReader.nextString());
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final StringBuilder sb) throws IOException {
                String string;
                if (sb == null) {
                    string = null;
                }
                else {
                    string = sb.toString();
                }
                jsonWriter.value(string);
            }
        };
        STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, TypeAdapters.STRING_BUILDER);
        STRING_BUFFER = new TypeAdapter<StringBuffer>() {
            @Override
            public StringBuffer read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return new StringBuffer(jsonReader.nextString());
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final StringBuffer sb) throws IOException {
                String string;
                if (sb == null) {
                    string = null;
                }
                else {
                    string = sb.toString();
                }
                jsonWriter.value(string);
            }
        };
        STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, TypeAdapters.STRING_BUFFER);
        URL = new TypeAdapter<URL>() {
            @Override
            public URL read(final JsonReader jsonReader) throws IOException {
                final JsonToken peek = jsonReader.peek();
                final JsonToken null = JsonToken.NULL;
                final URL url = null;
                if (peek == null) {
                    jsonReader.nextNull();
                    return null;
                }
                final String nextString = jsonReader.nextString();
                URL url2;
                if ("null".equals(nextString)) {
                    url2 = url;
                }
                else {
                    url2 = new URL(nextString);
                }
                return url2;
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final URL url) throws IOException {
                String externalForm;
                if (url == null) {
                    externalForm = null;
                }
                else {
                    externalForm = url.toExternalForm();
                }
                jsonWriter.value(externalForm);
            }
        };
        URL_FACTORY = newFactory(URL.class, TypeAdapters.URL);
        URI = new TypeAdapter<URI>() {
            @Override
            public URI read(final JsonReader jsonReader) throws IOException {
                final JsonToken peek = jsonReader.peek();
                final JsonToken null = JsonToken.NULL;
                final URI uri = null;
                if (peek == null) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    final String nextString = jsonReader.nextString();
                    URI uri2;
                    if ("null".equals(nextString)) {
                        uri2 = uri;
                    }
                    else {
                        uri2 = new URI(nextString);
                    }
                    return uri2;
                }
                catch (final URISyntaxException ex) {
                    throw new JsonIOException(ex);
                }
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final URI uri) throws IOException {
                String asciiString;
                if (uri == null) {
                    asciiString = null;
                }
                else {
                    asciiString = uri.toASCIIString();
                }
                jsonWriter.value(asciiString);
            }
        };
        URI_FACTORY = newFactory(URI.class, TypeAdapters.URI);
        INET_ADDRESS = new TypeAdapter<InetAddress>() {
            @Override
            public InetAddress read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return InetAddress.getByName(jsonReader.nextString());
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final InetAddress inetAddress) throws IOException {
                String hostAddress;
                if (inetAddress == null) {
                    hostAddress = null;
                }
                else {
                    hostAddress = inetAddress.getHostAddress();
                }
                jsonWriter.value(hostAddress);
            }
        };
        INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, TypeAdapters.INET_ADDRESS);
        UUID = new TypeAdapter<UUID>() {
            @Override
            public UUID read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return java.util.UUID.fromString(jsonReader.nextString());
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final UUID uuid) throws IOException {
                String string;
                if (uuid == null) {
                    string = null;
                }
                else {
                    string = uuid.toString();
                }
                jsonWriter.value(string);
            }
        };
        UUID_FACTORY = newFactory(UUID.class, TypeAdapters.UUID);
        TIMESTAMP_FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.getRawType() != Timestamp.class) {
                    return null;
                }
                return (TypeAdapter<T>)new TypeAdapter<Timestamp>(this, gson.getAdapter(Date.class)) {
                    final TypeAdapters$22 this$0;
                    final TypeAdapter val$dateTypeAdapter;
                    
                    @Override
                    public Timestamp read(final JsonReader jsonReader) throws IOException {
                        final Date date = this.val$dateTypeAdapter.read(jsonReader);
                        Timestamp timestamp;
                        if (date != null) {
                            timestamp = new Timestamp(date.getTime());
                        }
                        else {
                            timestamp = null;
                        }
                        return timestamp;
                    }
                    
                    @Override
                    public void write(final JsonWriter jsonWriter, final Timestamp timestamp) throws IOException {
                        this.val$dateTypeAdapter.write(jsonWriter, timestamp);
                    }
                };
            }
        };
        CALENDAR = new TypeAdapter<Calendar>() {
            private static final String DAY_OF_MONTH = "dayOfMonth";
            private static final String HOUR_OF_DAY = "hourOfDay";
            private static final String MINUTE = "minute";
            private static final String MONTH = "month";
            private static final String SECOND = "second";
            private static final String YEAR = "year";
            
            @Override
            public Calendar read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                jsonReader.beginObject();
                int year = 0;
                int month = 0;
                int hourOfDay;
                final int n = hourOfDay = month;
                int second;
                int minute = second = hourOfDay;
                int dayOfMonth = n;
                while (jsonReader.peek() != JsonToken.END_OBJECT) {
                    final String nextName = jsonReader.nextName();
                    final int nextInt = jsonReader.nextInt();
                    if ("year".equals(nextName)) {
                        year = nextInt;
                    }
                    else if ("month".equals(nextName)) {
                        month = nextInt;
                    }
                    else if ("dayOfMonth".equals(nextName)) {
                        dayOfMonth = nextInt;
                    }
                    else if ("hourOfDay".equals(nextName)) {
                        hourOfDay = nextInt;
                    }
                    else if ("minute".equals(nextName)) {
                        minute = nextInt;
                    }
                    else {
                        if (!"second".equals(nextName)) {
                            continue;
                        }
                        second = nextInt;
                    }
                }
                jsonReader.endObject();
                return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Calendar calendar) throws IOException {
                if (calendar == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.beginObject();
                jsonWriter.name("year");
                jsonWriter.value(calendar.get(1));
                jsonWriter.name("month");
                jsonWriter.value(calendar.get(2));
                jsonWriter.name("dayOfMonth");
                jsonWriter.value(calendar.get(5));
                jsonWriter.name("hourOfDay");
                jsonWriter.value(calendar.get(11));
                jsonWriter.name("minute");
                jsonWriter.value(calendar.get(12));
                jsonWriter.name("second");
                jsonWriter.value(calendar.get(13));
                jsonWriter.endObject();
            }
        };
        CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, TypeAdapters.CALENDAR);
        LOCALE = new TypeAdapter<Locale>() {
            @Override
            public Locale read(final JsonReader jsonReader) throws IOException {
                final JsonToken peek = jsonReader.peek();
                final JsonToken null = JsonToken.NULL;
                String nextToken = null;
                if (peek == null) {
                    jsonReader.nextNull();
                    return null;
                }
                final StringTokenizer stringTokenizer = new StringTokenizer(jsonReader.nextString(), "_");
                String nextToken2;
                if (stringTokenizer.hasMoreElements()) {
                    nextToken2 = stringTokenizer.nextToken();
                }
                else {
                    nextToken2 = null;
                }
                String nextToken3;
                if (stringTokenizer.hasMoreElements()) {
                    nextToken3 = stringTokenizer.nextToken();
                }
                else {
                    nextToken3 = null;
                }
                if (stringTokenizer.hasMoreElements()) {
                    nextToken = stringTokenizer.nextToken();
                }
                if (nextToken3 == null && nextToken == null) {
                    return new Locale(nextToken2);
                }
                if (nextToken == null) {
                    return new Locale(nextToken2, nextToken3);
                }
                return new Locale(nextToken2, nextToken3, nextToken);
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Locale locale) throws IOException {
                String string;
                if (locale == null) {
                    string = null;
                }
                else {
                    string = locale.toString();
                }
                jsonWriter.value(string);
            }
        };
        LOCALE_FACTORY = newFactory(Locale.class, TypeAdapters.LOCALE);
        JSON_ELEMENT = new TypeAdapter<JsonElement>() {
            @Override
            public JsonElement read(final JsonReader jsonReader) throws IOException {
                switch (TypeAdapters$32.$SwitchMap$com$google$gson$stream$JsonToken[jsonReader.peek().ordinal()]) {
                    default: {
                        throw new IllegalArgumentException();
                    }
                    case 6: {
                        final JsonObject jsonObject = new JsonObject();
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            jsonObject.add(jsonReader.nextName(), this.read(jsonReader));
                        }
                        jsonReader.endObject();
                        return jsonObject;
                    }
                    case 5: {
                        final JsonArray jsonArray = new JsonArray();
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            jsonArray.add(this.read(jsonReader));
                        }
                        jsonReader.endArray();
                        return jsonArray;
                    }
                    case 4: {
                        jsonReader.nextNull();
                        return JsonNull.INSTANCE;
                    }
                    case 3: {
                        return new JsonPrimitive(jsonReader.nextString());
                    }
                    case 2: {
                        return new JsonPrimitive(jsonReader.nextBoolean());
                    }
                    case 1: {
                        return new JsonPrimitive(new LazilyParsedNumber(jsonReader.nextString()));
                    }
                }
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final JsonElement jsonElement) throws IOException {
                if (jsonElement != null && !jsonElement.isJsonNull()) {
                    if (jsonElement.isJsonPrimitive()) {
                        final JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
                        if (asJsonPrimitive.isNumber()) {
                            jsonWriter.value(asJsonPrimitive.getAsNumber());
                        }
                        else if (asJsonPrimitive.isBoolean()) {
                            jsonWriter.value(asJsonPrimitive.getAsBoolean());
                        }
                        else {
                            jsonWriter.value(asJsonPrimitive.getAsString());
                        }
                    }
                    else if (jsonElement.isJsonArray()) {
                        jsonWriter.beginArray();
                        final Iterator<JsonElement> iterator = jsonElement.getAsJsonArray().iterator();
                        while (iterator.hasNext()) {
                            this.write(jsonWriter, iterator.next());
                        }
                        jsonWriter.endArray();
                    }
                    else {
                        if (!jsonElement.isJsonObject()) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Couldn't write ");
                            sb.append(jsonElement.getClass());
                            throw new IllegalArgumentException(sb.toString());
                        }
                        jsonWriter.beginObject();
                        for (final Map.Entry<String, V> entry : jsonElement.getAsJsonObject().entrySet()) {
                            jsonWriter.name(entry.getKey());
                            this.write(jsonWriter, (JsonElement)entry.getValue());
                        }
                        jsonWriter.endObject();
                    }
                }
                else {
                    jsonWriter.nullValue();
                }
            }
        };
        JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, TypeAdapters.JSON_ELEMENT);
        ENUM_FACTORY = newEnumTypeHierarchyFactory();
    }
    
    private TypeAdapters() {
    }
    
    public static TypeAdapterFactory newEnumTypeHierarchyFactory() {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                final Class<? super T> rawType = typeToken.getRawType();
                if (Enum.class.isAssignableFrom(rawType) && rawType != Enum.class) {
                    Class superclass = rawType;
                    if (!rawType.isEnum()) {
                        superclass = rawType.getSuperclass();
                    }
                    return new EnumTypeAdapter<T>(superclass);
                }
                return null;
            }
        };
    }
    
    public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> typeToken, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory(typeToken, typeAdapter) {
            final TypeToken val$type;
            final TypeAdapter val$typeAdapter;
            
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                TypeAdapter val$typeAdapter;
                if (typeToken.equals(this.val$type)) {
                    val$typeAdapter = this.val$typeAdapter;
                }
                else {
                    val$typeAdapter = null;
                }
                return val$typeAdapter;
            }
        };
    }
    
    public static <TT> TypeAdapterFactory newFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory(clazz, typeAdapter) {
            final Class val$type;
            final TypeAdapter val$typeAdapter;
            
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                TypeAdapter val$typeAdapter;
                if (typeToken.getRawType() == this.val$type) {
                    val$typeAdapter = this.val$typeAdapter;
                }
                else {
                    val$typeAdapter = null;
                }
                return val$typeAdapter;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Factory[type=");
                sb.append(this.val$type.getName());
                sb.append(",adapter=");
                sb.append(this.val$typeAdapter);
                sb.append("]");
                return sb.toString();
            }
        };
    }
    
    public static <TT> TypeAdapterFactory newFactory(final Class<TT> clazz, final Class<TT> clazz2, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory(clazz, clazz2, typeAdapter) {
            final Class val$boxed;
            final TypeAdapter val$typeAdapter;
            final Class val$unboxed;
            
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                final Class<? super T> rawType = typeToken.getRawType();
                TypeAdapter<T> val$typeAdapter;
                if (rawType != this.val$unboxed && rawType != this.val$boxed) {
                    val$typeAdapter = null;
                }
                else {
                    val$typeAdapter = this.val$typeAdapter;
                }
                return val$typeAdapter;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Factory[type=");
                sb.append(this.val$boxed.getName());
                sb.append("+");
                sb.append(this.val$unboxed.getName());
                sb.append(",adapter=");
                sb.append(this.val$typeAdapter);
                sb.append("]");
                return sb.toString();
            }
        };
    }
    
    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> clazz, final Class<? extends TT> clazz2, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory(clazz, clazz2, typeAdapter) {
            final Class val$base;
            final Class val$sub;
            final TypeAdapter val$typeAdapter;
            
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                final Class<? super T> rawType = typeToken.getRawType();
                TypeAdapter<T> val$typeAdapter;
                if (rawType != this.val$base && rawType != this.val$sub) {
                    val$typeAdapter = null;
                }
                else {
                    val$typeAdapter = this.val$typeAdapter;
                }
                return val$typeAdapter;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Factory[type=");
                sb.append(this.val$base.getName());
                sb.append("+");
                sb.append(this.val$sub.getName());
                sb.append(",adapter=");
                sb.append(this.val$typeAdapter);
                sb.append("]");
                return sb.toString();
            }
        };
    }
    
    public static <TT> TypeAdapterFactory newTypeHierarchyFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory(clazz, typeAdapter) {
            final Class val$clazz;
            final TypeAdapter val$typeAdapter;
            
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                TypeAdapter val$typeAdapter;
                if (this.val$clazz.isAssignableFrom(typeToken.getRawType())) {
                    val$typeAdapter = this.val$typeAdapter;
                }
                else {
                    val$typeAdapter = null;
                }
                return val$typeAdapter;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Factory[typeHierarchy=");
                sb.append(this.val$clazz.getName());
                sb.append(",adapter=");
                sb.append(this.val$typeAdapter);
                sb.append("]");
                return sb.toString();
            }
        };
    }
    
    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T>
    {
        private final Map<T, String> constantToName;
        private final Map<String, T> nameToConstant;
        
        public EnumTypeAdapter(final Class<T> clazz) {
            this.nameToConstant = new HashMap<String, T>();
            this.constantToName = new HashMap<T, String>();
            try {
                for (final Enum<T> enum1 : clazz.getEnumConstants()) {
                    String name = enum1.name();
                    final SerializedName serializedName = clazz.getField(name).getAnnotation(SerializedName.class);
                    if (serializedName != null) {
                        name = serializedName.value();
                    }
                    this.nameToConstant.put(name, (T)enum1);
                    this.constantToName.put((T)enum1, name);
                }
            }
            catch (final NoSuchFieldException ex) {
                throw new AssertionError();
            }
        }
        
        @Override
        public T read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.nameToConstant.get(jsonReader.nextString());
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final T t) throws IOException {
            String s;
            if (t == null) {
                s = null;
            }
            else {
                s = this.constantToName.get(t);
            }
            jsonWriter.value(s);
        }
    }
}
