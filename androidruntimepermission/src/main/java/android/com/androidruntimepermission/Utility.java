package android.com.androidruntimepermission;

import android.content.Context;
import android.content.SharedPreferences;

class Utility {
    Utility() {
    }

    private static SharedPreferences getSharedPref(Context mContext) {
        return mContext.getSharedPreferences("Permission", 0);
    }

    static boolean isPermissionAskedPreviously(Context mContext, String permissionName) {
        SharedPreferences sp = getSharedPref(mContext);
        return sp.getBoolean(permissionName, false);
    }

    static void savePermissionisAsked(Context mContext, String[] permissionArray) {
        SharedPreferences sp = getSharedPref(mContext);
        SharedPreferences.Editor editor = sp.edit();
        String[] var4 = permissionArray;
        int i = permissionArray.length;

        for(int var6 = 0; var6 < i; ++var6) {
            String permission = var4[var6];
            editor.putBoolean(permission, true);
        }

        editor.apply();
    }
}
