package samir.com.chat_fire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import samir.com.chat_fire.Adapters.Chat_msgs;
import samir.com.chat_fire.Adapters.MassageAdapter;
import samir.com.chat_fire.model.Users;

public class MasagesActivity extends AppCompatActivity {
    MassageAdapter massageAdapter;
    RecyclerView recyclerView;
    List<Chat_msgs> chat_msgs;
    private CircleImageView profile_iamge;
    private TextView username;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    ImageButton button_msg;
    EditText send_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masages);
        Toolbar toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.massage_rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
          });
        profile_iamge=findViewById(R.id.profileImage_massage);
        username=findViewById(R.id.username_massage);
        button_msg=findViewById(R.id.bt_snd);
        send_msg=findViewById(R.id.text_send);
        Intent intent=getIntent();
        final String userId=intent.getStringExtra("userId");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                username.setText(users.getUserName());

                if(users.getImage().equals("defualt")){
                   profile_iamge.setImageResource(R.drawable.pp);
                }
                readMassages(firebaseUser.getUid(),userId,users.getImage());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        send_msg.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    button_msg.setVisibility(View.GONE);
                } else {
                    button_msg.setVisibility(View.VISIBLE);
                    button_msg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String msg=s.toString();
                            sendMassage(firebaseUser.getUid(),userId,msg);
                            send_msg.setText("");
                        }
                    });
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


    }

   private  void sendMassage(String sender, final String receiver, String msg) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
       HashMap<String ,Object> msghMap=new HashMap<>();
       msghMap.put("sender",sender);
       msghMap.put("receiver",receiver);
       msghMap.put("massage",msg.trim());
       reference.child("Chats").push().setValue(msghMap);
       final DatabaseReference chref=FirebaseDatabase.getInstance().getReference("ChatList")
               .child(firebaseUser.getUid()).child(receiver);

       chref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if(!dataSnapshot.exists()){
                   chref.child("id").setValue(receiver);
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });








    }
  private  void readMassages(final String senderid, final String recvid, final String uri)
  {
      chat_msgs=new ArrayList<>();
     DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              chat_msgs.clear();
              for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                  Chat_msgs chats=snapshot.getValue(Chat_msgs.class);
                  if(chats.getReceiver().equals(senderid)&&chats.getSender().equals(recvid)||
                  chats.getReceiver().equals(recvid)&&chats.getSender().equals(senderid)
                  ){
                     chat_msgs.add(chats);

                  }
                  massageAdapter=new MassageAdapter(MasagesActivity.this,chat_msgs,uri);
                  recyclerView.setAdapter(massageAdapter);

              }

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });



  }

}
