package samir.com.chat_fire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Rigster extends AppCompatActivity {
    private EditText username,password,Email;
    Button submit;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigster);
        submit=findViewById(R.id.reg_submit);
        username=findViewById(R.id.reg_username);
        password=findViewById(R.id.reg_password);
        Email=findViewById(R.id.reg_Email);
        auth=FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=username.getText().toString();
                String Password=password.getText().toString();
                String useremail=Email.getText().toString();
                if(TextUtils.isEmpty(userName)&&TextUtils.isEmpty(useremail)&&TextUtils.isEmpty(Password))
                {
                    Toast.makeText(Rigster.this,"Empty filed",Toast.LENGTH_LONG).show();


                }
                else if(Password.length()<6){
                    Toast.makeText(Rigster.this,"password must be more than 5 digit",Toast.LENGTH_LONG).show();
                }
                else
                {
                    doRigster(useremail,Password,userName);

                }




            }
        });




    }

   private void doRigster(final String email_user, String pass_user, final String user_user){
        auth.createUserWithEmailAndPassword(email_user,pass_user)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful())
                   {
                       FirebaseUser user1=auth.getCurrentUser();
                       String userId=user1.getUid();
                       reference= FirebaseDatabase.getInstance().getReference("users").child(userId);
                       HashMap<String ,String> map=new HashMap<>();
                       map.put("userId",userId);
                       map.put("Email",email_user);
                       map.put("userName",user_user);
                       map.put("image","defualt");
                       reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               Intent intent=new Intent(Rigster.this,MainChat.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                               finish();
                           }
                       });



                   }
                   else
                   {
                       Toast.makeText(Rigster.this,"cant created",Toast.LENGTH_LONG).show();
                   }
                    }
                });
   }
}
