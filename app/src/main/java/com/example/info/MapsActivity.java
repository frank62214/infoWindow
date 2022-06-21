package com.example.info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.info.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton1, infoButton2;
    private OnInfoWindowElemTouchListener infoButtonListener;
    private MapWrapperLayout mapWrapperLayout;

    static final LatLng latlng1 = new LatLng(28.5355, 77.3910);
    static final LatLng latlng2 = new LatLng(28.6208768, 77.3726377);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //將layout新增至頁面中
        setContentView(R.layout.activity_main);
        //取得map_relative_layout
        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //同步mapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20));

        //取得自製infowindow的layout
        infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.custom_infowindow, null);

        //取得layout中的元件
        infoTitle = (TextView)infoWindow.findViewById(R.id.nameTxt);
        infoSnippet = (TextView)infoWindow.findViewById(R.id.addressTxt);
        infoButton1 = (Button)infoWindow.findViewById(R.id.btnOne);
        infoButton2 = (Button)infoWindow.findViewById(R.id.btnTwo);

        //新增Button1的事件
        infoButtonListener = new OnInfoWindowElemTouchListener(infoButton1){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                //todo
                Toast.makeText(MapsActivity.this, "click on button 1", Toast.LENGTH_SHORT).show();
            }

        };
        //將事件加入
        infoButton1.setOnTouchListener(infoButtonListener);


        //設定googlemap中的WindowsAdapter所取得的文字對應
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoSnippet.setText(marker.getTitle());
                infoTitle.setText(marker.getSnippet());
                infoButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        // Let's add a couple of markers
        //新增標記1
        mMap.addMarker(new MarkerOptions()
                .position(latlng1)
                .title("Source")
                .snippet("Comapny Name")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        //新增標記2
        mMap.addMarker(new MarkerOptions()
                .position(latlng2)
                .title("Destination")
                .snippet("AmisunXXXXXX")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        //移動相機
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng1, 10));

    }
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
}