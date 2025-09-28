package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface JsonSerializer<T> {
    JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext);
}
