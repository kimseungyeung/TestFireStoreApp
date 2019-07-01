package com.example.testfirestoreapp.Activity;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.testfirestoreapp.R;
import com.naver.maps.geometry.LatLng;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class DaumMapActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, View.OnClickListener, MapView.CurrentLocationEventListener {
    Button btn_nowlocation;
    EditText edt_search_location;
    MapView mapView;
    LocationManager lm;
    LatLng now_location=null;
    MapCircle circle1=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daummapactivity);
        /*getAppKeyHash();*/
        component();
    }
    public void component(){
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapView = new MapView(this);
        ViewGroup mapViewContatiner = (ViewGroup)findViewById(R.id.map_view);
        mapViewContatiner.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
        btn_nowlocation=(Button)findViewById(R.id.btn_now_location);
        edt_search_location= (EditText)findViewById(R.id.edt_search_location);
        btn_nowlocation.setOnClickListener(this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, lmlistener);

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

    }
    private void getAppKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    //네이버 디벨로퍼(구버전) 지역검색
    public void searchnear(String t,LatLng loc){
        String app_key = "668b4aaca051b0dbf2df145b894fb42a";//애플리케이션 클라이언트 아이디값";
        String lon =Double.toString(loc.longitude);
        String lat =Double.toString(loc.latitude);
        int radius=250;
        try {
            String text = URLEncoder.encode(t, "UTF-8");

            String apiURL = "https://dapi.kakao.com//v2/local/search/keyword?query="+text+"&category_group_code=HP8&x="+lon+"&y="+lat+"&radius="+radius+"&size=10"; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK "+app_key);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            JSONObject result =new JSONObject(response.toString());
            JSONArray ja = result.getJSONArray("documents");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jj = ja.getJSONObject(i);
                String address_name = jj.getString("address_name");
                String road_address_name = jj.getString("road_address_name");
                String place_name = jj.getString("place_name");
                String category_group_name = jj.getString("category_name");
                String x = jj.getString("x");
                String y = jj.getString("y");
                String distance = jj.getString("distance");
                String phone = jj.getString("phone");
                setmarker(Double.parseDouble(y),Double.parseDouble(x),place_name);
            }
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_now_location:
                String text=edt_search_location.getText().toString().trim();
                new SearchTask(text).execute();

                break;
        }
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
      now_location= new LatLng(mapPoint.getMapPointGeoCoord().latitude,mapPoint.getMapPointGeoCoord().longitude);
      if(mapView.getCircles().length!=0) {
          mapView.removeCircle(circle1);
      }
      circle1 = new MapCircle(
                mapPoint, // center
                250, // radius
                Color.argb(128, 255, 0, 0), // strokeColor
                Color.argb(0, 0, 255, 0) // fillColor
        );
        circle1.setTag(1234);
        mapView.addCircle(circle1);
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    //Asynctask <doinbackgroud변수,progress변수,postexcute변수>
    public class SearchTask extends AsyncTask<Integer, Integer, Boolean> {
        ProgressDialog asyncDialog = new ProgressDialog(DaumMapActivity.this);
        String text=null;
        boolean result = false;

        public SearchTask(String t) {
            this.text = t;
        }

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("검색 중 입니다...");
            asyncDialog.setCancelable(false);
            asyncDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
           // LatLng loc= new LatLng(37.53737528, 127.00557633);
            // searchnear(text);
           searchnear(text,now_location);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//           super.onPostExecute(aBoolean);

            asyncDialog.dismiss();

        }

    }
    public void setmarker(double lat,double lon,String title){

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(title);
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lat,lon));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
    }
    LocationListener lmlistener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
           double lat= location.getLatitude();
           double lon =location.getLongitude();
           now_location =new LatLng(lat,lon);
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
}

