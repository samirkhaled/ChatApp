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

    private RecyclerView recyclerView;
    private UsersAdapter mAdapter;
    private List<Users> users;
    FirebaseUser  fuser;
    List<String> idlist;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_caht, container, false);
        recyclerView=view.findViewById(R.id.recview_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        users=new ArrayList<>();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        idlist=new ArrayList<>();
        //
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        //get user massages id list
        idlist=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idlist.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat_msgs msgs=snapshot.getValue(Chat_msgs.class);

                    if(msgs.getSender().equals(fuser.getUid())){
                        idlist.add(msgs.getReceiver());
                    }
                    else if(msgs.getReceiver().equals(fuser.getUid()))
                    {
                        idlist.add(msgs.getSender());
                    }




                }
                readUsers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void readUsers() {
        reference=FirebaseDatabase.getInstance().getReference("users");
        users=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Users user=snapshot.getValue(Users.class);
                    for(String id:idlist)
                    {
                        if(user.getUserId().equals(id))
                        {
                            if(users.size()!=0)
                            {
                                for(int i=0;i<users.size();i++)
                                {
                                    Users users1=users.get(i);
                                    if(!user.getUserId().equals(users1.getUserId()))
                                    {
                                        users.add(user);
                                    }
                                }

                            }else
                            {
                                //size =0;
                                users.add(user);
                            }
                        }


                    }

                }
                mAdapter=new UsersAdapter(getContext(),users);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }



}
