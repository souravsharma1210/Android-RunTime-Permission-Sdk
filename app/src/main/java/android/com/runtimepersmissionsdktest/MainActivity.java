package android.com.runtimepersmissionsdktest;

import android.Manifest;
import android.com.androidruntimepermission.OnPermissionGiven;
import android.com.androidruntimepermission.RuntimePermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RuntimePermission.requestCriticalPermission(this,1002, new OnPermissionGiven() {
            @Override
            public void onPermissionReceived(List<String> list, List<String> list1, List<String> list2) {
                Toast.makeText(MainActivity.this,"Nice Message",Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA);
    }
}
///////////////
