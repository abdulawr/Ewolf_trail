package com.ss_technology.mushinprojectandroidapp.Phone_Auth_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ss_technology.mushinprojectandroidapp.Activities.Upload_User_Account_Detailts;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.Loading_Dai;
import com.ss_technology.mushinprojectandroidapp.R;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    TextInputEditText phone,otp;
    Button submitbtn,verify_btn;
    Loading_Dai alert;
    TextView resend_code,timerTextview;
    TextInputLayout phoneLayout,otp_layout;
    String authCode= "",ph="";
    CountDownTimer countDownTimer;
    Boolean resCheck=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_phone_auth);

        resend_code=findViewById(R.id.resend_code);
        otp_layout=findViewById(R.id.otp_layout);
        phoneLayout=findViewById(R.id.phoneLayout);
        timerTextview=findViewById(R.id.timerTextview);
        phone=findViewById(R.id.phone);
        submitbtn=findViewById(R.id.submitbtn);
        verify_btn=findViewById(R.id.verify_btn);
        otp=findViewById(R.id.otp);
        alert=new Loading_Dai(this);

        /*TO SEND THE VERIFICATION CODE*/
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ph=phone.getText().toString();
                if(HelperFunctions.verify(ph)){
                    if(ph.charAt(0) != '0'){
                        if(ph.trim().length() > 10 || ph.trim().length() < 10){
                            Toast.makeText(PhoneAuth.this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // on successfully enter the number
                            SendVerificationCode(ph);

                            countDownTimer=new CountDownTimer(100000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    timerTextview.setVisibility(View.VISIBLE);
                                    timerTextview.setText(String.valueOf(millisUntilFinished/1000));
                                    resCheck=false;
                                }

                                @Override
                                public void onFinish() {
                                    resCheck=true;
                                }
                            };
                        }
                    }
                    else{
                        Toast.makeText(PhoneAuth.this, "Mobile number should not be start with zero and country is added by default", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(PhoneAuth.this, "Enter mobile number!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*To resend the code*/
        resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resCheck)
                {
                    verify_btn.setVisibility(View.GONE);
                    submitbtn.setVisibility(View.VISIBLE);
                    authCode=" ";
                    SendVerificationCode(ph);
                }
                else {
                    Toast.makeText(PhoneAuth.this, "You will be able to make another request after 1 minute", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*TO VERIFY THE VERIFICATION CODE SENDED TO USER*/
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String code=otp.getText().toString();
              if (HelperFunctions.verify(code)){
                  alert.Show();
                  PhoneAuthCredential credential = PhoneAuthProvider.getCredential(authCode, code);
                  FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful())
                          {
                              Intent intent=new Intent(getApplicationContext(), Upload_User_Account_Detailts.class);
                              intent.putExtra("mobile",ph);
                              startActivity(intent);
                              finish();
                              alert.Hide();
                          }
                          else {
                              Toast.makeText(PhoneAuth.this, "Invalid code is enter try again", Toast.LENGTH_SHORT).show();
                              alert.Hide();
                          }
                      }
                  });
              }
              else {
                  Toast.makeText(PhoneAuth.this, "Please enter code", Toast.LENGTH_SHORT).show();
              }
            }
        });

    }

    void assignAhtoCode(String v)
    {
        authCode=v;
    }

    private void SendVerificationCode(String no) {
        alert.Show();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+92" + no,        // Phone number to verify
                    60L,                 // Timeout duration
                    TimeUnit.SECONDS,// Unit of timeout
                    PhoneAuth.this,               // Activity (for callback binding)
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            alert.Hide();
                            Intent intent=new Intent(getApplicationContext(), Upload_User_Account_Detailts.class);
                            intent.putExtra("mobile",phone.getText().toString());
                            startActivity(intent);
                            finish();
                            Toast.makeText(PhoneAuth.this, "This number is already verify by Google play service so need for OTP!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(PhoneAuth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage());
                            alert.Hide();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            assignAhtoCode(s);
                            authCode = s;
                            alert.Hide();
                            resend_code.setVisibility(View.VISIBLE);
                            countDownTimer.cancel();
                            otp_layout.setVisibility(View.VISIBLE);
                            phoneLayout.setVisibility(View.GONE);
                            submitbtn.setVisibility(View.GONE);
                            countDownTimer.start();
                            verify_btn.setVisibility(View.VISIBLE);
                        }
                    });        // OnVerificationStateChangedCallbacks
    }

}