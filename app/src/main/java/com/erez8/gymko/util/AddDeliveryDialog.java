package com.erez8.gymko.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.erez8.gymko.R;
import com.erez8.gymko.adapters.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import static com.erez8.gymko.Constants.LAT_LNG_BOUNDS;

public class AddDeliveryDialog extends AppCompatDialogFragment implements GoogleApiClient.OnConnectionFailedListener {

    private EditText whatToDeliver, timeOfArrival;
    private AutoCompleteTextView deliveryLocation;
    private AddDeliveryDialogListener deliveryDialogListener;

    private PlaceAutocompleteAdapter autocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_delivery,null);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), this)
                    .build();
        }

        autocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(),mGoogleApiClient,LAT_LNG_BOUNDS,null);


        builder.setView(view).
                setTitle("add a delivery")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    String what_to_deliver = whatToDeliver.getText().toString();
                    String location_delivery = deliveryLocation.getText().toString();
                    String time_of_arrival = timeOfArrival.getText().toString();
                    deliveryDialogListener.pushToFireStore(what_to_deliver,location_delivery,time_of_arrival);
            }
        });
        whatToDeliver =  view.findViewById(R.id.what_to_deliver);
        deliveryLocation = view.findViewById(R.id.delivery_location);
        timeOfArrival = view.findViewById(R.id.time_of_arrival);
        deliveryLocation.setAdapter(autocompleteAdapter);

        return  builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            deliveryDialogListener =  (AddDeliveryDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implemt AddDeliveryDialogListener");
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public interface AddDeliveryDialogListener{
        void pushToFireStore(String what_to_deliver, String location_delivey, String time_of_arrival);
    }
}
