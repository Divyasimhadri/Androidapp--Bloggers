package com.example.blogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class blogupload extends AppCompatActivity {
    private RecyclerView mBlogList;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseRecyclerAdapter adapter;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(mDataBase,Blog.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {
            @Override
            public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blog_row, parent, false);

                return new BlogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(BlogViewHolder holder, int position, Blog model) {
                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDesc());
                holder.setImage(model.getImage());
            }


        };
        adapter.startListening();
        mBlogList.setAdapter(adapter);




    }
    public  class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;
        }

        public void setTitle(String title)
        {
            TextView post_title ;
            post_title=(TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);

        }

        public void setDesc(String desc)
        {
            TextView post_desc ;
            post_desc=(TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }

        public void setImage(String image)
        {
            ImageView post_image;
            post_image=(ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.action_menu,menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogupload);

        mDataBase= FirebaseDatabase.getInstance().getReference().child("blog");

        mBlogList=(RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(getApplicationContext(), addpostactivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                mAuth.signOut();
                Intent intent1 =new Intent(getApplicationContext(),MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
