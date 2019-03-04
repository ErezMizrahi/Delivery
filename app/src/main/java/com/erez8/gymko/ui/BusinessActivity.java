package com.erez8.gymko.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.erez8.gymko.Models.Business;
import com.erez8.gymko.Models.Delivery;
import com.erez8.gymko.Models.User;
import com.erez8.gymko.Models.UserLocation;
import com.erez8.gymko.R;
import com.erez8.gymko.UserClient;
import com.erez8.gymko.util.AddDeliveryDialog;
import com.erez8.gymko.util.BottomSheetDelivery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BusinessActivity extends AppCompatActivity implements
        View.OnClickListener , AddDeliveryDialog.AddDeliveryDialogListener , BottomSheetDelivery.ListviewListener {
    private static final String TAG = "BusinessActivity";

    //widgets
    private Business mBusiness;
    private ConstraintLayout layout;
    //vars
    private ListenerRegistration  mUserListEventListener ;
    private ArrayList<Delivery> listOfShipments;
    private FirebaseFirestore mDb;

    ListView listView ;


    private ArrayList<User> mUserList = new ArrayList<>();
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        listView = findViewById(R.id.recyclerView_shipments);

        mDb = FirebaseFirestore.getInstance();
        listOfShipments = new ArrayList<>();
        getIncomingIntent();
        getBusinessUsers();
        initBtns();

    }

    private void initBtns() {
        layout = findViewById(R.id.layout);
        findViewById(R.id.btn_userList).setOnClickListener(this);
        findViewById(R.id.btn_delivery).setOnClickListener(this);
        findViewById(R.id.btn_startDeliver).setOnClickListener(this);
        findViewById(R.id.btn_leave).setOnClickListener(this);
        findViewById(R.id.btn_listOfShipments).setOnClickListener(this);
    }

    private void getUserLocation(com.erez8.gymko.Models.User user) {
        DocumentReference locationsRef = mDb
                .collection(getString(R.string.collection_user_locations))
                .document(user.getUser_id());

        locationsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    if (task.getResult().toObject(UserLocation.class) != null) {

                        mUserLocations.add(task.getResult().toObject(UserLocation.class));
                    }
                }
            }
        });

    }


    private void getBusinessUsers() {

        CollectionReference usersRef = mDb
                .collection(getString(R.string.collection_business))
                .document(mBusiness.getmBusiness_id())
                .collection(getString(R.string.collection_business_user_list));

        mUserListEventListener = usersRef
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "onEvent: Listen failed.", e);
                            return;
                        }

                        if (queryDocumentSnapshots != null) {

                            // Clear the list and add all the users again
                            mUserList.clear();
                            mUserList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                User user = doc.toObject(User.class);
                                mUserList.add(user);
                                getUserLocation(user);
                            }

                            Log.d(TAG, "onEvent: user list size: " + mUserList.size());
                        }
                    }
                });
    }


    private void inflateUserListFragment() {
        hideSoftKeyboard();
        layout.setVisibility(View.INVISIBLE);

        UserListFragment fragment = UserListFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.intent_user_list), mUserList);
        bundle.putParcelableArrayList(getString(R.string.intent_user_locations), mUserLocations);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        transaction.replace(R.id.user_list_container, fragment, getString(R.string.fragment_user_list));
        transaction.addToBackStack(getString(R.string.fragment_user_list));
        transaction.commit();
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void getIncomingIntent() {
        if (getIntent().hasExtra(getString(R.string.intent_business))) {
            mBusiness = getIntent().getParcelableExtra(getString(R.string.intent_business));
            joinChatroom();
        }
    }

    private void leaveBusiness() {

        DocumentReference joinChatroomRef = mDb
                .collection(getString(R.string.collection_business))
                .document(mBusiness.getmBusiness_id())
                .collection(getString(R.string.collection_business_user_list))
                .document(FirebaseAuth.getInstance().getUid());

        joinChatroomRef.delete();


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void joinChatroom() {

        DocumentReference joinChatroomRef = mDb
                .collection(getString(R.string.collection_business))
                .document(mBusiness.getmBusiness_id())
                .collection(getString(R.string.collection_business_user_list))
                .document(FirebaseAuth.getInstance().getUid());

        User user = ((UserClient) (getApplicationContext())).getUser();
        joinChatroomRef.set(user); // Don't care about listening for completion.
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUserListEventListener != null) {
            mUserListEventListener.remove();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        layout.setVisibility(View.VISIBLE);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_userList:
                inflateUserListFragment();
                break;

            case R.id.btn_delivery:
                openDeliveryDialog();
                break;

            case R.id.btn_leave:
                leaveBusiness();
                break;

            case R.id.btn_startDeliver:
                if (mLocation != "") {
                    inflateMapDirectiontFragment(this.mLocation);
                }
                break;
            case R.id.btn_listOfShipments:
                openList();
                break;

        }
    }

    private void openList() {

                BottomSheetDelivery dialogFragment = new BottomSheetDelivery();
                Bundle bundle = new Bundle();
                bundle.putString("b", mBusiness.getmBusiness_id());
                bundle.putParcelableArrayList("arr",listOfShipments);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(),"bottomSheet");
            }


    private void openDeliveryDialog() {
        AddDeliveryDialog deliveryDialog = new AddDeliveryDialog();
        deliveryDialog.show(getSupportFragmentManager(), "delivery dialog");
    }


    @Override
    public void pushToFireStore(String what_to_deliver, String location_delivey, String time_of_arrival) {
        DocumentReference addDeliveryRef = mDb
                .collection(getString(R.string.collection_business))
                .document(mBusiness.getmBusiness_id())
                .collection(getString(R.string.collection_business_delivery))
                .document(location_delivey);

        Delivery delivery = new Delivery(what_to_deliver,location_delivey,time_of_arrival);

        addDeliveryRef.set(delivery);

    }

    private String mLocation = "";

    @Override
    public void onItemClicked(String location) {
        this.mLocation = location;
    }

    private void inflateMapDirectiontFragment(String location) {
        getBusinessUsers();
        hideSoftKeyboard();
        layout.setVisibility(View.INVISIBLE);

        MapDireactionFragment fragment = MapDireactionFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("location", location);
        bundle.putParcelableArrayList(getString(R.string.intent_user_list), mUserList);
        bundle.putParcelableArrayList(getString(R.string.intent_user_locations), mUserLocations);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        transaction.replace(R.id.user_list_container, fragment);
        transaction.addToBackStack(getString(R.string.fragment_user_list));
        transaction.commit();
    }
}
