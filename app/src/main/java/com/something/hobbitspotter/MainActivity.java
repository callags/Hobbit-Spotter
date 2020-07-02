package com.something.hobbitspotter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    //Initialize camera button
    Button camButton;

    static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign camera button to id
        camButton = findViewById(R.id.camButton);


        camButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Check if permission has been granted
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) +
                        ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) +
                        ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED) {

                    //When permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.CAMERA) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        //Create an alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this
                        );
                        builder.setTitle("Grant these permissions!");
                        builder.setMessage("Camera, Read and Write.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE);
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE);
                    }
                }
                //Otherwise load the camera
                else {
                    startActivity(new Intent(MainActivity.this, camera.class));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            //Permissions are allowed
            if ((grantResults.length > 0 ) && (grantResults[0]
                    + grantResults[1] + grantResults[2]
                    == PackageManager.PERMISSION_GRANTED)){
                //Tell user to press the camera button again to continue
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Press the camera button again to continue");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            //Permissions are denied
            else {
                Toast.makeText(getApplicationContext(), "Please allow permissions for this app!", Toast.LENGTH_LONG).show();
            }
        }
    }
}