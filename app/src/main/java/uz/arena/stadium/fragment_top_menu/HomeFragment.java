package uz.arena.stadium.fragment_top_menu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import uz.arena.stadium.DetailsActivity;
import uz.arena.stadium.HomeActivity;
import uz.arena.stadium.OrderAdapter;
import uz.arena.stadium.OrderItem;
import uz.arena.stadium.R;
import uz.arena.stadium.more.BookingActivity;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    Button btn_tennis, btn_bowling, btn_add;
    private DatabaseReference RootRef;
    SupportMapFragment mapFragment;
    private GoogleMap map;
    RatingBar ratingBar;
    RecyclerView recyclerView;
    OrderAdapter stadiumAdapter;
    CardView filter;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    CardView card_home;
    FusedLocationProviderClient mFusedLocationClient;
    EditText filter_date, filter_time;
    String location, arena_name, rasim1, rasim2, rasim3, rasim4, check_1, check_2, choosing, dimensions, summa, radio, uid,
            latitude, longitude;
    int REQUEST_LOCATION = 88;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_football);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        RootRef = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.recycler_home);
        btn_tennis = view.findViewById(R.id.btn_tennis_home);
        filter_date = view.findViewById(R.id.filter_date);
        filter_time = view.findViewById(R.id.filter_time);
        filter = view.findViewById(R.id.filter_football);
        btn_bowling = view.findViewById(R.id.btn_bowling_home);
        btn_add = view.findViewById(R.id.btn_add_home);
        ratingBar = view.findViewById(R.id.rating_id);
        card_home = view.findViewById(R.id.card_home);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String Date = bundle.getString("date");
            String Time = bundle.getString("time");
            filter_date.setText(Date);
            filter_time.setText(Time);
            String DateE = filter_date.getText().toString();
            String TimeE = filter_time.getText().toString();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order_filter").child("Football").child(DateE);

            Query query = reference
                    .orderByChild(TimeE)
                    .equalTo(TimeE);
            FirebaseRecyclerOptions<OrderItem> options =
                    new FirebaseRecyclerOptions.Builder<OrderItem>()
                            .setQuery(query, OrderItem.class)
                            .build();
            stadiumAdapter = new OrderAdapter(options);
            recyclerView.setAdapter(stadiumAdapter);
            LocationsIf();
            card_home.setVisibility(View.GONE);

        } else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order_arena").child("Football");

            FirebaseRecyclerOptions<OrderItem> options =
                    new FirebaseRecyclerOptions.Builder<OrderItem>()
                            .setQuery(reference, OrderItem.class)
                            .build();
            stadiumAdapter = new OrderAdapter(options);
            recyclerView.setAdapter(stadiumAdapter);
            LocationsElse();

        }

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);

        filter.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), FilterActivity.class);
            startActivity(intent);
        });

        btn_tennis.setOnClickListener(v -> requireFragmentManager().beginTransaction()
                .replace(R.id.fragment_home, new TennisFragment())
                .commit());

        btn_bowling.setOnClickListener(v -> requireFragmentManager().beginTransaction()
                .replace(R.id.fragment_home, new BowlingFragment())
                .commit());

        btn_add.setOnClickListener(v -> requireFragmentManager().beginTransaction()
                .replace(R.id.fragment_home, new AddFragment())
                .commit());

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Joylashuv ruxsati zarur")
                        .setMessage("Bu ilovaga Joylashuv ruxsati kerak, joylashuv funksiyasidan foydalanishga rozilik bildiring")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(requireActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LocationMap();
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //move map camera
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(12).build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                Log.i("RealMadrid", latLng.toString());
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());
                    map.setMyLocationEnabled(true);

                }
            }
        }
    }

    private void PermissionLocation() {

        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Locations();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 23);
        }
    }

    private void Locations() {

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(50000);
        request.setFastestInterval(2000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(requireActivity().getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try {
                task.getResult(ApiException.class);
            } catch (ApiException e) {

                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(requireActivity(), REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException ignored) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }

                e.printStackTrace();
            }
        });
    }

    private void getLocations() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                LocationMap();
                PermissionLocation();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        
        TedPermission.with(requireActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

    }

    private void LocationMap() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                        Looper.myLooper());
                map.setMyLocationEnabled(true);

                map.setPadding(0, 210, 30, 0);

            } else {
                //Request Location Permission
                checkLocationPermission();
                getLocations();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                    Looper.myLooper());
            map.setMyLocationEnabled(true);
        }
    }

    private void LocationsElse() {

        RootRef.child("order_arena").child("Football")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Map<String, Object>> markers = new HashMap<>();
                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            double Lat = Double.parseDouble(Objects.requireNonNull(snapshot.child(Objects.requireNonNull(dsp.getKey())).child("latitude").getValue()).toString());
                            double Long = Double.parseDouble(Objects.requireNonNull(snapshot.child(dsp.getKey()).child("longitude").getValue()).toString());

                            Map<String, Object> dataModel = new HashMap<>();
                            location = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("location").getValue())).toString();
                            arena_name = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("arena_name").getValue())).toString();
                            rasim1 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("rasim1").getValue())).toString();
                            rasim2 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("rasim2").getValue())).toString();
                            rasim3 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("rasim3").getValue())).toString();
                            rasim4 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("rasim4").getValue())).toString();
                            check_1 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("check_1").getValue())).toString();
                            check_2 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("check_2").getValue())).toString();
                            choosing = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("choosing").getValue())).toString();
                            dimensions = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("dimensions").getValue())).toString();
                            summa = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("summa").getValue())).toString();
                            radio = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("radio").getValue())).toString();
                            uid = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("uid").getValue())).toString();
                            latitude = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("latitude").getValue())).toString();
                            longitude = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("longitude").getValue())).toString();

                            dataModel.put("location", location);
                            dataModel.put("arena_name", arena_name);
                            dataModel.put("rasim1", rasim1);
                            dataModel.put("rasim2", rasim2);
                            dataModel.put("rasim3", rasim3);
                            dataModel.put("rasim4", rasim4);
                            dataModel.put("check_1", check_1);
                            dataModel.put("check_2", check_2);
                            dataModel.put("choosing", choosing);
                            dataModel.put("dimensions", dimensions);
                            dataModel.put("summa", summa);
                            dataModel.put("radio", radio);
                            dataModel.put("uid", uid);
                            dataModel.put("latitude", latitude);
                            dataModel.put("longitude", longitude);

                            LatLng position = new LatLng(Lat, Long);

                            @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_new_home);
                            Bitmap b = bitmapdraw.getBitmap();
                            int height = 140;
                            int width = 130;
                            Log.i("bag", "no exist bag");
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            Log.i("bag", "exist bag");

                            float zoomLevel = 16.0f; //This goes up to 21
                            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(position)
                                    .title(arena_name)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                            assert marker != null;
                            markers.put(marker.getId(), dataModel);
                            Objects.requireNonNull(marker).setTag(position);
                        }

                        map.setOnMarkerClickListener(mark -> {
                            Map<String, Object> dataModelE = markers.get(mark.getId());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra("datas", (Serializable) dataModelE);
                            startActivity(intent);
                            return false;
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void LocationsIf() {
        String DateE = filter_date.getText().toString();
        String TimeE = filter_time.getText().toString();

        RootRef.child("order_filter").child("Football").child(DateE).orderByChild(TimeE).equalTo(TimeE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Map<String, Object>> markers = new HashMap<>();
                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            double Lat = Double.parseDouble(Objects.requireNonNull(snapshot.child(Objects.requireNonNull(dsp.getKey())).child("latitude").getValue()).toString());
                            double Long = Double.parseDouble(Objects.requireNonNull(snapshot.child(dsp.getKey()).child("longitude").getValue()).toString());

                            Map<String, Object> dataModel = new HashMap<>();
                            location = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("location").getValue())).toString();
                            arena_name = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("arena_name").getValue())).toString();
                            rasim1 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("rasim1").getValue())).toString();
                            rasim2 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("rasim2").getValue())).toString();
                            rasim3 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("rasim3").getValue())).toString();
                            rasim4 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("rasim4").getValue())).toString();
                            check_1 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("check_1").getValue())).toString();
                            check_2 = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("check_2").getValue())).toString();
                            choosing = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("choosing").getValue())).toString();
                            dimensions = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("dimensions").getValue())).toString();
                            summa = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("summa").getValue())).toString();
                            radio = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("radio").getValue())).toString();
                            uid = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("uid").getValue())).toString();
                            latitude = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("latitude").getValue())).toString();
                            longitude = (Objects.requireNonNull(snapshot.child(dsp.getKey()).child("longitude").getValue())).toString();

                            dataModel.put("location", location);
                            dataModel.put("arena_name", arena_name);
                            dataModel.put("rasim1", rasim1);
                            dataModel.put("rasim2", rasim2);
                            dataModel.put("rasim3", rasim3);
                            dataModel.put("rasim4", rasim4);
                            dataModel.put("check_1", check_1);
                            dataModel.put("check_2", check_2);
                            dataModel.put("choosing", choosing);
                            dataModel.put("dimensions", dimensions);
                            dataModel.put("summa", summa);
                            dataModel.put("radio", radio);
                            dataModel.put("uid", uid);
                            dataModel.put("latitude", latitude);
                            dataModel.put("longitude", longitude);

                            LatLng position = new LatLng(Lat, Long);

                            @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_new_home);
                            Bitmap b = bitmapdraw.getBitmap();
                            int height = 140;
                            int width = 130;
                            Log.i("bag", "No bag");
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(position)
                                    .title(arena_name)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                            assert marker != null;
                            markers.put(marker.getId(), dataModel);
                            Objects.requireNonNull(marker).setTag(position);
                        }

                        map.setOnMarkerClickListener(mark -> {
                            Map<String, Object> dataModelE = markers.get(mark.getId());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra("datas", (Serializable) dataModelE);
                            startActivity(intent);
                            return false;
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        stadiumAdapter.startListening();
        PermissionLocation();
    }

    @Override
    public void onStop() {
        super.onStop();
        stadiumAdapter.stopListening();
    }
}