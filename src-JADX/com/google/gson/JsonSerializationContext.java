package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface JsonSerializationContext {
    JsonElement serialize(Object obj);

    JsonElement serialize(Object obj, Type type);
}
