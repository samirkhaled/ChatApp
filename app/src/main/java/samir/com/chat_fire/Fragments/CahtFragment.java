package samir.com.chat_fire.Fragments;

import android.app.Activity;
import android.graphics.ImageDecoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import samir.com.chat_fire.Adapters.Chat_msgs;
import samir.com.chat_fire.Adapters.UsersAdapter;
import samir.com.chat_fire.R;
import samir.com.chat_fire.model.Chatlist;
import samir.com.chat_fire.model.Users;


public class CahtFragment extends Fragment {

    private  RecyclerView recyclerView;
    private  FirebaseUser  fuser;
    private  List<Users> mList;
    private UsersAdapter usersAdapter;
     private DatabaseReference reference;
    private   List<Chatlist> chatlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_caht, container, false);
        recyclerView=view.findViewById(R.id.recview_users);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());


        recyclerView.setLayoutManager(linearLayoutManager);
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        chatlist=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chatlist  chatlist1=snapshot.getValue(Chatlist.class);
                    chatlist.add(chatlist1);

                }
               // chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return view;
    }

    private void chatList() {
        mList=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mList.clear();
               for(DataSnapshot snapshot:dataSnapshot.getChildren())
               {
                   Users user=snapshot.getValue(Users.class);
                   for(Chatlist chlist:chatlist){

                       if(user.getUserId().equals(chlist.getId())){
                           mList.add(user);

                       }
                   }


               }
               usersAdapter=new UsersAdapter(getContext(),mList);
               recyclerView.setAdapter(usersAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










    }


}
