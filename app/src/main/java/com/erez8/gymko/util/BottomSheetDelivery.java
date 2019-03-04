package com.erez8.gymko.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.erez8.gymko.Models.Delivery;
import com.erez8.gymko.R;
import com.erez8.gymko.adapters.ListOfShipmentsAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class BottomSheetDelivery extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDelivery";
    private ListenerRegistration  mBusinessDeliveryListenerRegistretion;
    private List<Delivery> listOfShipments;
    private FirebaseFirestore mDb;
    private ListviewListener listviewListener;

    ListView listView ;
    public BottomSheetDelivery() {
    }

    public interface  ListviewListener{
        void onItemClicked(String location);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listviewListener = (ListviewListener)context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement ListviewListener") ;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_of_shipments_layout,container,false);
            listView = v.findViewById(R.id.recyclerView_shipments);
            listOfShipments = getArguments().getParcelableArrayList("arr");
        mDb = FirebaseFirestore.getInstance();

        openList();
        return v;
    }



    private void openList() {
        Log.d(TAG, "openList: " +listOfShipments.size());
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);

        CollectionReference deliveryCollection = mDb
                .collection(getString(R.string.collection_business))
                .document(getArguments().getString("b"))
                .collection(getString(R.string.collection_business_delivery));

        mBusinessDeliveryListenerRegistretion = deliveryCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent: called.");

                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }

                if(queryDocumentSnapshots != null){
                    listOfShipments.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        Delivery delivery = doc.toObject(Delivery.class);
                        listOfShipments.add(delivery);
                    }
                }
                Log.d(TAG, "amount: " + listOfShipments.size());
                listView.setAdapter(new ListOfShipmentsAdapter(listOfShipments,getActivity()));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listviewListener.onItemClicked(listOfShipments.get(position).getLocation_of_delivery());
                        dismiss();
                    }
                });
            }
        });
    }
}
