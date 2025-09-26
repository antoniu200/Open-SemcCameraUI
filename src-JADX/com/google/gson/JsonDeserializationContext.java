package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface JsonDeserializationContext {
    <T> T deserialize(JsonElement jsonElement, Type type) throws JsonParseException;
}
