package samir.com.chat_fire;

import androidx.annotation.NonNull;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import samir.com.chat_fire.Fragments.CahtFragment;
import samir.com.chat_fire.Fragments.UsersFragment;
import samir.com.chat_fire.model.Users;

public class MainChat extends AppCompatActivity {

    CircleImageView pro_img;
    TextView user_name_text;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        pro_img=findViewById(R.id.profileImage);
        user_name_text=findViewById(R.id.username);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Toolbar toolbar=findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
         reference.addValueEventListener(new ValueEventListener() {
                @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users users=dataSnapshot.getValue(Users.class);
                    user_name_text.setText(users.getUserName());
                    if(users.getImage().equals("defualt"))
                    {
                        pro_img.setImageResource(R.drawable.pp);
                    }

               }

          @Override
       public void onCancelled(@NonNull DatabaseError databaseError) {

         }
            });

         ViewpagerAdapter viewpagerAdapter=new ViewpagerAdapter(getSupportFragmentManager());
         viewpagerAdapter.addFragment(new CahtFragment(),"Chat");
         viewpagerAdapter.addFragment(new UsersFragment(),"Users");
         viewPager.setAdapter(viewpagerAdapter);
         tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu,menu);
       return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         switch (item.getItemId())
         {

             case R.id.logout:
                 FirebaseAuth.getInstance().signOut();
                 startActivity(new Intent(MainChat.this,Login.class));
                 finish();
                 return true;

         }
         return false;
    }

}
class  ViewpagerAdapter extends FragmentPagerAdapter{

      ArrayList<Fragment> fragments;
      ArrayList<String> titles;

    ViewpagerAdapter(FragmentManager fm){
        super(fm);
        this.fragments=new ArrayList<>();
        this.titles=new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }



    public void addFragment(Fragment fragment, String tit){
        fragments.add(fragment);
        titles.add(tit);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }




}