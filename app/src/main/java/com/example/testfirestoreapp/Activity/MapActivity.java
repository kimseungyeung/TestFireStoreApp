package com.example.testfirestoreapp.Activity;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfirestoreapp.Data.PlaceData;
import com.example.testfirestoreapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));
    private GoogleMap mMap;
    LocationManager lm;
    Button btn_zoomin, btn_zoomout, btn_search_location, btn_now_location;
    EditText edt_search_location;
    List<Address> addresslist = null;
    MarkerOptions mylocation = null;
    Marker nowmarker = null;
    ListView ll_auto;
    PlacesClient placesClient;
    LatLng ll ;
    Geocoder geocoder;
    ArrayList<Marker> selectedmarker;
    SensorManager mySensorManager;
    boolean sersorrunning=false;
    Marker lotatem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapactivity);
        component();
    }

    public void component() {
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mMapFragment.getMapAsync(this);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE},
                        1);
            }
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, lmlistener);
//        lm.requestLocationUpdates(null,0,2,lmlistener);
        btn_zoomin = (Button) findViewById(R.id.btn_zoomin);
        btn_zoomout = (Button) findViewById(R.id.btn_zoomout);
        edt_search_location = (EditText) findViewById(R.id.edt_search_location);
        btn_search_location = (Button) findViewById(R.id.btn_search_location);
        btn_now_location = (Button) findViewById(R.id.btn_now_location);
        btn_zoomout.setOnClickListener(this);
        btn_zoomin.setOnClickListener(this);
        btn_search_location.setOnClickListener(this);
        btn_now_location.setOnClickListener(this);
        ll_auto = (ListView) findViewById(R.id.list_auto);
        geocoder = new Geocoder(getBaseContext());
        Places.initialize(this, getString(R.string.API_KEY));
        placesClient = Places.createClient(this);
        final PlaceAutoAdapter padapter = new PlaceAutoAdapter(this, BOUNDS_INDIA);
        ll_auto.setAdapter(padapter);
        mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

        if(mySensors.size() > 0){
            mySensorManager.registerListener(msensorlistener, mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            sersorrunning = true;
            Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG).show();
            sersorrunning = false;
            finish();
        }


        edt_search_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    padapter.getFilter().filter(s.toString());
                }else{
                    padapter.clearlist();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_search_location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    padapter.clearlist();

                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.

    }

    public boolean checkzoom = true;
    LocationListener lmlistener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng nowlocation = new LatLng(location.getLatitude(), location.getLongitude());
            if (mylocation == null) {
                BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker_location_normal));
                mylocation = setmarker(mMap, null, "", nowlocation);
                ll=new LatLng(nowlocation.latitude,nowlocation.longitude);
            } else {
                mylocation.position(nowlocation);
            }
            if (nowmarker != null) {
                nowmarker.remove();
            }
            nowmarker = mMap.addMarker(mylocation);
            if (checkzoom) {
                checkzoom = false;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nowlocation, 17));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                        Toast.makeText(this, "권한오류", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zoomin:
                ;
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case R.id.btn_zoomout:
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;
            case R.id.btn_search_location:

                String g = edt_search_location.getText().toString();




                try {
                    // Getting a maximum of 3 Address that matches the input
                    // text
                    addresslist = geocoder.getFromLocationName(g, 10);
                    if (addresslist != null && !addresslist.equals(""))
                        search(addresslist);

                } catch (Exception e) {

                }
                break;
            case R.id.btn_now_location:
                zoomnowlocation();
                break;
        }
    }

    protected void search(List<Address> addresses) {

        Address address = (Address) addresses.get(0);
        double home_long = address.getLongitude();
        double home_lat = address.getLatitude();
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());
        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker_location_normal));
        MarkerOptions markerOptions = setmarker(mMap, bd, addressText, latLng);

        if(selectedmarker!=null){
            for(int i=0; i<selectedmarker.size(); i++){
                selectedmarker.get(i).remove();
            }
            selectedmarker.clear();
        }
       selectedmarker = new ArrayList<>();
       Marker dd= mMap.addMarker(markerOptions);
        selectedmarker.add(dd);
       mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mMap.setOnMarkerClickListener(this);


    }

    public boolean clickcheck = false;

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle() != null) {
            if (clickcheck) {
                clickcheck = false;
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker_location_normal)));
            } else {
                clickcheck = true;
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker_location_clicked)));
            }
        }
        return true;
    }

    public MarkerOptions setmarker(GoogleMap map, BitmapDescriptor bd, String title, LatLng location) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(location);
        if (bd != null) {
            marker.icon(bd);
        }
        if (!title.equals("")) {
            marker.title(title);
        }
        return marker;
    }

    public void zoomnowlocation() {
        if (mylocation != null) {
            if (nowmarker != null) {
                nowmarker.remove();
            }
            nowmarker = mMap.addMarker(mylocation);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation.getPosition(), 17));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class PlaceAutoAdapter extends BaseAdapter implements Filterable {
        Context context;
        ArrayList<PlaceData> resultList;
        LatLngBounds bounds;
        LayoutInflater layoutInflater;

        public PlaceAutoAdapter(Context ctx, LatLngBounds bd) {
            this.context = ctx;
            this.bounds = bd;
            this.layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resultList=new ArrayList<>();
        }

        public void clearlist() {
            resultList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (resultList == null) {
                return 0;
            }
            return this.resultList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_place_auto, parent, false);
                holder = new ViewHolder();

                holder.tv_pName = (TextView) convertView.findViewById(R.id.tv_pname);
                holder.tv_padress = (TextView) convertView.findViewById(R.id.tv_padress);
                holder.ll_auto_item = (LinearLayout) convertView.findViewById(R.id.ll_auto_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
                String pname=resultList.get(position).getPlacename().toString();
                String padress =resultList.get(position).getDescription().toString();

                holder.tv_pName.setText(pname);
                holder.tv_padress.setText(padress);
                holder.ll_auto_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker_location_normal));
                        try {
                            if(addresslist!=null) {
                                addresslist.clear();
                            }
                            addresslist = geocoder.getFromLocationName(padress, 10);
                            if(addresslist.size()!=0) {
                                search(addresslist);
                            }else{
                                Toast.makeText(context,"검색결과가 없습니다",Toast.LENGTH_LONG).show();
                            }
                        }catch (IOException e){

                        }
                        clearlist();
                    }
                });

            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    resultList.clear();
                    resultList = new ArrayList<>();
                    if (constraint != null) {


                        //(제약) 검색 문자열에 대한 자동 완성 API를 쿼리하십시오.
                        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
// Create a RectangularBounds object.
                        RectangularBounds bounds=null;

                            bounds = RectangularBounds.newInstance(
                                    new LatLng(-0, 0),
                                    new LatLng(0, 0));
                        if(mylocation!=null) {
                            bounds = RectangularBounds.newInstance(
                                    new LatLng(ll.latitude-0.1, ll.longitude),
                                    new LatLng(ll.latitude,ll.longitude));
                        }

                        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                                .setLocationBias(bounds)
//                                .setLocationRestriction(bounds)
                                .setCountry("kr")
                                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                .setSessionToken(token)
                                .setQuery(constraint.toString().trim())
                                .build();

                        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                Log.i("성공", prediction.getPlaceId());
                                Log.i("성공", prediction.getPrimaryText(null).toString());
                                Log.i("성공", prediction.getFullText(null).toString());
                                PlaceData pp = new PlaceData(prediction.getPlaceId(),prediction.getFullText(null),prediction.getPrimaryText(null));
                                resultList.add(pp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("실패", e.getMessage().toString());
                            }
                        });


                        if (resultList != null) {


                            //API가 성공적으로 결과를 반환했습니다.

                            results.values = resultList;

                            results.count = resultList.size();


                        }


                    }
                    return results;
                }


                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {

                        //API가 하나 이상의 결과를 반환하고 데이터를 업데이트합니다.

                        notifyDataSetChanged();

                    } else {


                    }

                }
            };
            return filter;
        }


    }

    private class ViewHolder {
        public TextView tv_padress, tv_pName;
        public LinearLayout ll_auto_item;
    }
    public Bitmap setbit(Bitmap bitmapOrg,float met){
        Matrix matrix = new Matrix();



        matrix.postRotate(met);



        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, 50, 50, true);



        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;

    }
    SensorEventListener msensorlistener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
           if(ll!=null) {
               if (lotatem != null) {
                   lotatem.remove();
               }
               Bitmap bb = BitmapFactory.decodeResource(getResources(), R.drawable.location_arrow);
               Bitmap resultbit = setbit(bb, event.values[0]);
               MarkerOptions marker2 = new MarkerOptions();
               marker2.icon(BitmapDescriptorFactory.fromBitmap(resultbit));
               marker2.position(new LatLng(ll.latitude, ll.longitude));
               lotatem = mMap.addMarker(marker2);
           }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}