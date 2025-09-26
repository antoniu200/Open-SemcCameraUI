// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.TypeAdapter;

public final class ObjectTypeAdapter extends TypeAdapter<Object>
{
    public static final TypeAdapterFactory FACTORY;
    private final Gson gson;
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.getRawType() == Object.class) {
                    return (TypeAdapter<T>)new ObjectTypeAdapter(gson, null);
                }
                return null;
            }
        };
    }
    
    private ObjectTypeAdapter(final Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public Object read(final JsonReader jsonReader) throws IOException {
        switch (ObjectTypeAdapter$2.$SwitchMap$com$google$gson$stream$JsonToken[jsonReader.peek().ordinal()]) {
            default: {
                throw new IllegalStateException();
            }
            case 6: {
                jsonReader.nextNull();
                return null;
            }
            case 5: {
                return jsonReader.nextBoolean();
            }
            case 4: {
                return jsonReader.nextDouble();
            }
            case 3: {
                return jsonReader.nextString();
            }
            case 2: {
                final LinkedTreeMap linkedTreeMap = new LinkedTreeMap();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    linkedTreeMap.put(jsonReader.nextName(), this.read(jsonReader));
                }
                jsonReader.endObject();
                return linkedTreeMap;
            }
            case 1: {
                final ArrayList list = new ArrayList();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    list.add(this.read(jsonReader));
                }
                jsonReader.endArray();
                return list;
            }
        }
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final Object o) throws IOException {
        if (o == null) {
            jsonWriter.nullValue();
            return;
        }
        final TypeAdapter<?> adapter = this.gson.getAdapter(o.getClass());
        if (adapter instanceof ObjectTypeAdapter) {
            jsonWriter.beginObject();
            jsonWriter.endObject();
            return;
        }
        adapter.write(jsonWriter, o);
    }
}
