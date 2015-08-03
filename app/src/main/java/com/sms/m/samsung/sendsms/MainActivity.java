package com.sms.m.samsung.sendsms;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class MainActivity extends Activity {

    private static final int REQUEST_SEND_SMS_PERMISSION = 0;
    private String TAG="MainActivity";

    private Button btnSendSMS;
    private EditText txtPhoneNo;
    private EditText txtMessage;

    private String phoneNo;
    private String message;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);

        btnSendSMS.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                phoneNo = txtPhoneNo.getText().toString();
                message = txtMessage.getText().toString();
                if (phoneNo.length()>0 && message.length()>0)
                    sendSMS(phoneNo, message);
                else
                    Toast.makeText(getBaseContext(),"Please enter both phone number and message.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendSMS(String phoneNumber, String message)
    {
        try {
            //checking if that permission is already there!!!
            if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "SEND_SMS permission has already been granted.");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(getApplicationContext(), "SMS Sent!",Toast.LENGTH_LONG).show();
            }
            else
            {
                Log.i(TAG, "SEND_SMS permission has not been granted.");
                // Provide an additional rationale to the user if the permission was not granted
                if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                    Log.i(TAG, "Displaying SEND_SMS permission rationale to provide additional context.");
//                    Toast.makeText(this,"Displaying SEND_SMS permission" , Toast.LENGTH_SHORT).show();
                }

                // Request SEND_SMS permission
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},REQUEST_SEND_SMS_PERMISSION);

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later!",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == REQUEST_SEND_SMS_PERMISSION) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i(TAG, "Received response for SEND_SMS permission request.");

            // Check if the only required permission has been granted
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // SEND_SMS permission has been granted
                Log.i(TAG, "SEND_SMS permission has now been granted.");
                Toast.makeText(this, "SEND_SMS permission has now been granted.", Toast.LENGTH_SHORT).show();
                sendSMS(phoneNo, message);
            }
            else {
                Log.i(TAG, "SEND_SMS permission was NOT granted.");
                Toast.makeText(this, "SEND_SMS permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
