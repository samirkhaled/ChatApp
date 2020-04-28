package samir.com.chat_fire.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.security.PrivateKey;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import samir.com.chat_fire.MasagesActivity;
import samir.com.chat_fire.R;
import samir.com.chat_fire.model.Users;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    private Context mContext;
    List<Users> users;

    public UsersAdapter(Context mContext, List<Users> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.users_items,parent,false);

        return new  UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users1=users.get(position);
        holder.username.setText(users1.getUserName());
        if(users1.getImage().equals("defualt"))
        {
           holder.profile_img.setImageResource(R.drawable.pp);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MasagesActivity.class);
                intent.putExtra("userId",users1.getUserId());
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public CircleImageView profile_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.useritem_username);
            profile_img = itemView.findViewById(R.id.userItems_profimg);

        }
    }

}
