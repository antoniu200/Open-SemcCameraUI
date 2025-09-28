package com.google.gson;

import com.google.gson.reflect.TypeToken;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface TypeAdapterFactory {
    <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken);
}
