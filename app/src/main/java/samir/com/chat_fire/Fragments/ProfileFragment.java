package samir.com.chat_fire.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import samir.com.chat_fire.Login;
import samir.com.chat_fire.R;
import samir.com.chat_fire.model.Users;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    CircleImageView circleImageView;
    TextView username;
    FirebaseUser fuser;
    DatabaseReference reference;
    StorageReference storageReference;
    public static final  int IMAGE_REQUST=1;
    private Uri URi;
    StorageTask uploadtask;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        circleImageView=view.findViewById(R.id.profile_img);
        username=view.findViewById(R.id.username);
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        storageReference=FirebaseStorage.getInstance().getReference("uploads");
        reference= FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                username.setText(users.getUserName());
                if(users.getImage().equals("defualt")){
                    circleImageView.setImageResource(R.drawable.pp);

                }
                else {
                    Glide.with(getContext()).load(users.getImage()).into(circleImageView);
                   // Toast.makeText(getContext(),users.getImage(),Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




     circleImageView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             readImage();
         }

     });




        return view;

    }

    private void readImage() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUST);
    }
    private String getFileExtenation(Uri uri){

        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));


    }

private void uploadIamge(){
final ProgressDialog  progressDialog=new ProgressDialog(getContext());
 progressDialog.setMessage("Uploading");
 progressDialog.show();

    if(URi!=null){

        final StorageReference filerefrences=storageReference.child(System.currentTimeMillis() +"."+getFileExtenation(URi) );

        uploadtask=filerefrences.putFile(URi);
        uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>(){
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
               if(!task.isSuccessful()){
                   throw task.getException();
               }
               return filerefrences.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if(task.isSuccessful()){
                    Uri uridownload=task.getResult();
                    String uridownloaded=uridownload.toString();
                    reference=FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                    HashMap<String ,Object> hashMap=new HashMap<>();
                    hashMap.put("image",uridownloaded);

                    reference.updateChildren(hashMap);
                    progressDialog.dismiss();

                }else{
                    Toast.makeText(getContext(),"filed",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });


        }else {

        Toast.makeText(getContext(),"No image selected",Toast.LENGTH_LONG).show();
    }




}

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUST&&resultCode== RESULT_OK&&
        data!=null && data.getData()!=null)
        {
            URi=data.getData();
            if(uploadtask!=null&&uploadtask.isInProgress()){
                Toast.makeText(getContext(),"In progress",Toast.LENGTH_LONG).show();

            }else{
                uploadIamge();
            }
        }
    }
}
