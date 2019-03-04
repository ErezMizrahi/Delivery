package com.erez8.gymko.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.erez8.gymko.Models.User;
import com.erez8.gymko.R;
import com.erez8.gymko.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity implements
        View.OnClickListener

{

    private static final String TAG = "ProfileActivity";
    private static final int REQ_CODE =101 ;


    //widgets
    private CircleImageView mAvatarImage;

    //vars
//    private ImageListFragment mImageListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAvatarImage = findViewById(R.id.image_choose_avatar);

        findViewById(R.id.image_choose_avatar).setOnClickListener(this);
        findViewById(R.id.text_choose_avatar).setOnClickListener(this);
        findViewById(R.id.finish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        retrieveProfileImage();
    }

    private void retrieveProfileImage() {
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.cwm_logo)
                .placeholder(R.drawable.cwm_logo);


        if (afterResultPhoto != null ) {
            Glide.with(ProfileActivity.this)
                    .load(afterResultPhoto)
                    .into(mAvatarImage);
        }
    }


    private Uri afterResultPhoto = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            afterResultPhoto= data.getData();


        Glide.with(this)
                .load(afterResultPhoto)
                .into(mAvatarImage);

        // update the client and database
        User user = ((UserClient)getApplicationContext()).getUser();
        user.setAvatar(afterResultPhoto.toString());

        FirebaseFirestore.getInstance()
                .collection(getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().getUid())
                .set(user);
    }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, REQ_CODE);
//        mImageListFragment = new ImageListFragment();
//        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
//                .replace(R.id.fragment_container, mImageListFragment, getString(R.string.fragment_image_list))
//                .commit();
    }


////
//    @Override
//    public void onImageSelected(int resource) {
//
//        // remove the image selector fragment
//        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
//                .remove(mImageListFragment)
//                .commit();
////
////        // display the image
//        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(R.drawable.cwm_logo)
//                .error(R.drawable.cwm_logo);
//
//        Glide.with(this)
//                .setDefaultRequestOptions(requestOptions)
//                .load(resource)
//                .into(mAvatarImage);
//
//        // update the client and database
//        User user = ((UserClient)getApplicationContext()).getUser();
//        user.setAvatar(String.valueOf(resource));
//
//        FirebaseFirestore.getInstance()
//                .collection(getString(R.string.collection_users))
//                .document(FirebaseAuth.getInstance().getUid())
//                .set(user);
//    }

}
