package samir.com.chat_fire.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import samir.com.chat_fire.MasagesActivity;
import samir.com.chat_fire.R;
import samir.com.chat_fire.model.Users;
public class MassageAdapter extends RecyclerView.Adapter<MassageAdapter.ViewHolder>{
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

        private Context mContext;
        List<Chat_msgs> chat_msgs;
        private String magUrl;
        FirebaseUser firebaseUser;

        public MassageAdapter(Context mContext, List<Chat_msgs> chat_msgs,String magUrl) {
            this.mContext = mContext;
            this.chat_msgs = chat_msgs;
            this.magUrl=magUrl;
        }

        @NonNull
        @Override
        public MassageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if(viewType==MSG_TYPE_RIGHT){
                View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);

                return new MassageAdapter.ViewHolder(view);

            }
            else{
                View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);

                return new MassageAdapter.ViewHolder(view);
            }


        }

        @Override
        public void onBindViewHolder(@NonNull MassageAdapter.ViewHolder holder, int position) {

            Chat_msgs chat=chat_msgs.get(position);
            holder.usermsg.setText(chat.getMassage());
            if(magUrl.equals("defualt"))
            {
                holder.profile_img.setImageResource(R.drawable.pp);
            }else {
                Glide.with(mContext).load(magUrl).into(holder.profile_img);
            }


        }

        @Override
        public int getItemCount() {
            return chat_msgs.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView usermsg;
            public CircleImageView profile_img;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                usermsg= itemView.findViewById(R.id.chat_item_text);
                profile_img = itemView.findViewById(R.id.chat_img_item);

            }
        }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chat_msgs.get(position).getSender().equals(firebaseUser.getUid())){

           return MSG_TYPE_RIGHT;
        }
        else {
           return MSG_TYPE_LEFT;
        }

    }
}


