package android.support.v4.os;

import android.support.annotation.RestrictTo;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class LocaleHelper {
    static Locale forLanguageTag(String str) {
        if (str.contains(UserSettingValueHolder.DELIMITER)) {
            String[] strArrSplit = str.split(UserSettingValueHolder.DELIMITER, -1);
            if (strArrSplit.length > 2) {
                return new Locale(strArrSplit[0], strArrSplit[1], strArrSplit[2]);
            }
            if (strArrSplit.length > 1) {
                return new Locale(strArrSplit[0], strArrSplit[1]);
            }
            if (strArrSplit.length == 1) {
                return new Locale(strArrSplit[0]);
            }
        } else if (str.contains("_")) {
            String[] strArrSplit2 = str.split("_", -1);
            if (strArrSplit2.length > 2) {
                return new Locale(strArrSplit2[0], strArrSplit2[1], strArrSplit2[2]);
            }
            if (strArrSplit2.length > 1) {
                return new Locale(strArrSplit2[0], strArrSplit2[1]);
            }
            if (strArrSplit2.length == 1) {
                return new Locale(strArrSplit2[0]);
            }
        } else {
            return new Locale(str);
        }
        throw new IllegalArgumentException("Can not parse language tag: [" + str + "]");
    }

    static String toLanguageTag(Locale locale) {
        StringBuilder sb = new StringBuilder();
        sb.append(locale.getLanguage());
        String country = locale.getCountry();
        if (country != null && !country.isEmpty()) {
            sb.append(UserSettingValueHolder.DELIMITER);
            sb.append(locale.getCountry());
        }
        return sb.toString();
    }

    private LocaleHelper() {
    }
}
