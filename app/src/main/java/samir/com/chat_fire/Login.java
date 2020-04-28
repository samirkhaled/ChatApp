package samir.com.chat_fire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    TextView register;
    EditText Email,password;
    Button login;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register=findViewById(R.id.log_register);
        login=findViewById(R.id.log_login);
        Email=findViewById(R.id.log_Email);
        password=findViewById(R.id.log_password);
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email.getText().toString();
                String Password=password.getText().toString();

                if( TextUtils.isEmpty(email)&&TextUtils.isEmpty(Password))
                {
                    Toast.makeText(Login.this,"Empty filed",Toast.LENGTH_LONG).show();


                }
                else if(Password.length()<6){
                    Toast.makeText(Login.this,"password must be more than 5 digit",Toast.LENGTH_LONG).show();
                }
                else
                {
                     doLogin(email,Password);

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Rigster.class));
            }
        });


    }
   private  void doLogin(String user_email,String user_pass)
   {
      auth.signInWithEmailAndPassword(user_email,user_pass)
      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  Intent intent=new Intent(Login.this,MainChat.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(intent);
                  finish();
          }
              else{
                  Toast.makeText(Login.this,"invalid Email or password",Toast.LENGTH_LONG).show();

              }
          }
      }) ;




   }

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseUser!=null)
        {
         startActivity(new Intent(Login.this,MainChat.class));
         finish();
        }
    }
}
