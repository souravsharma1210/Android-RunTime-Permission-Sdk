package android.com.androidruntimepermission;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by sourav sharma on 29-11-2017.
 */


public final class RuntimePermission {
    private static final SparseArray<PermissionDetails> permissionRequestMap = new SparseArray();
    private static final String DEBUG_TAG =RuntimePermission.class.getSimpleName() ;

    private RuntimePermission() {
    }

    public static void requestCriticalPermission(Activity activity, int requestCode, @NonNull OnPermissionGiven onPermissionGiven, @NonNull String permission, @Nullable String... permissions) {
        RuntimePermission.PermissionDetails permissionDetails = new RuntimePermission.PermissionDetails(requestCode, onPermissionGiven);
        permissionRequestMap.put(requestCode, permissionDetails);
        List<String> permissionRequest = new ArrayList();
        permissionDetails.allPermission.add(permission);
        Log.d(DEBUG_TAG,"onCreate library");
        if(checkPermission(activity, permission) != RuntimePermission.PermissionStatus.PERMISSION_GRANTED) {
            permissionRequest.add(permission);
        } else {
            permissionDetails.permissionGranted.add(permission);
        }

        String[] permissionArray;
        if(permissions != null) {
            permissionArray = permissions;
            int noOfPermission = permissions.length;

            for(int i = 0; i < noOfPermission; ++i) {
                String permissionInList = permissionArray[i];
                permissionDetails.allPermission.add(permissionInList);
                if(checkPermission(activity, permissionInList) != RuntimePermission.PermissionStatus.PERMISSION_GRANTED) {
                    permissionRequest.add(permissionInList);
                } else {
                    permissionDetails.permissionGranted.add(permissionInList);
                }
            }
        }

        permissionArray = new String[permissionRequest.size()];
        permissionRequest.toArray(permissionArray);
        if(Build.VERSION.SDK_INT >= 23 && !permissionRequest.isEmpty()) {
            activity.requestPermissions(permissionArray, requestCode);
            Utility.savePermissionisAsked(activity, permissionArray);
        } else {
            onRequestPermissionsResult(requestCode);
        }
        Log.d(TAG,"last");

    }

    private static void onRequestPermissionsResult(int requestCode) {
        RuntimePermission.PermissionDetails p = (RuntimePermission.PermissionDetails)permissionRequestMap.get(requestCode);
        permissionRequestMap.remove(requestCode);
        p.onPermissionGiven.onPermissionReceived(p.allPermission, p.permissionsDenied, p.permissionsForeverDenied);
    }

    public static void onRequestPermissionsResult(Activity mActivity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RuntimePermission.PermissionDetails p = (RuntimePermission.PermissionDetails)permissionRequestMap.get(requestCode);
        permissionRequestMap.remove(requestCode);

        for(int index = 0; index < permissions.length; ++index) {
            if(grantResults[index] == 0) {
                p.permissionGranted.add(permissions[index]);
            } else {
                RuntimePermission.PermissionStatus permissionStatus = checkPermission(mActivity, permissions[index]);
                if(permissionStatus != RuntimePermission.PermissionStatus.PERMISSION_REJECTED && permissionStatus != RuntimePermission.PermissionStatus.PERMISSION_REJECTED_BEFORE) {
                    p.permissionsForeverDenied.add(permissions[index]);
                } else {
                    p.permissionsDenied.add(permissions[index]);
                }
            }
        }

        p.onPermissionGiven.onPermissionReceived(p.permissionGranted, p.permissionsDenied, p.permissionsForeverDenied);
    }

    private static RuntimePermission.PermissionStatus checkPermission(Activity activity, String permission) {
        int result = ContextCompat.checkSelfPermission(activity, permission);
        return result == 0?RuntimePermission.PermissionStatus.PERMISSION_GRANTED:
                (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)?
                        RuntimePermission.PermissionStatus.PERMISSION_REJECTED_BEFORE:
                        (Utility.isPermissionAskedPreviously(activity, permission)?
                                RuntimePermission.PermissionStatus.PERMISSION_FOREVER_REJECTED:
                                RuntimePermission.PermissionStatus.PERMISSION_REJECTED));
    }

    private enum PermissionStatus {
        PERMISSION_GRANTED,
        PERMISSION_REJECTED,
        PERMISSION_REJECTED_BEFORE,
        PERMISSION_FOREVER_REJECTED;

        private PermissionStatus() {
        }
    }

    private static class PermissionDetails {
        final int requestCode;
        final OnPermissionGiven onPermissionGiven;
        final List<String> allPermission;
        final List<String> permissionGranted;
        final List<String> permissionsDenied;
        final List<String> permissionsForeverDenied;

        private PermissionDetails(int requestCode, OnPermissionGiven onPermissionGiven) {
            this.requestCode = requestCode;
            this.onPermissionGiven = onPermissionGiven;
            this.permissionGranted = new ArrayList();
            this.permissionsDenied = new ArrayList();
            this.permissionsForeverDenied = new ArrayList();
            this.allPermission = new ArrayList();
        }
    }
}
