package android.com.runtimepersmissionsdktest;

import android.Manifest;
import android.com.androidruntimepermission.OnPermissionGiven;
import android.com.androidruntimepermission.RuntimePermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE=1002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RuntimePermission.requestCriticalPermission(this,REQUEST_CODE, new OnPermissionGiven() {

            @Override
            public void onPermissionReceived(List<String> list, List<String> list1, List<String> list2) {
                Toast.makeText(MainActivity.this,"Nice Message",Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        RuntimePermission.onRequestPermissionsResult(this,REQUEST_CODE, permissions,grantResults);
    }
}

