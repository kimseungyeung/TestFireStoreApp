package com.example.testfirestoreapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testfirestoreapp.R;
import com.google.gson.JsonObject;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationSource;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.style.sources.GeoJsonSource;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NaverMapActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback, NaverMap.OnLocationChangeListener {
    public final String CLIENT_ID = "b3zd0wlx07";// 애플리케이션 클라이언트 아이디 값
    public Button btn_nowlocation,btn_zoomin,btn_zoomout,btn_search_location;
    public EditText edt_search_location;
    private FusedLocationSource locationSource;
    private LocationOverlay locationoverlay;
    private  CircleOverlay co;
    private NaverMap nmap;
    private LatLng nowlocation;
    private boolean firstcheck=true;
    private boolean nowlocation_check=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navermapactivity);
        component();
    }

    public void component(){
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.nmaps);
        if(mapFragment==null){
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.nmaps,mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource =new FusedLocationSource(this,1000);
        edt_search_location= (EditText)findViewById(R.id.edt_search_location);
        btn_search_location =(Button)findViewById(R.id.btn_search_location);
        btn_search_location.setOnClickListener(this);
        btn_nowlocation= (Button)findViewById(R.id.btn_now_location);
        btn_zoomin =(Button)findViewById(R.id.btn_zoomin);
        btn_zoomout=(Button)findViewById(R.id.btn_zoomout);
        btn_nowlocation.setOnClickListener(this);
        btn_zoomin.setOnClickListener(this);
        btn_zoomout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_now_location:
                if(nowlocation_check){
                    nowlocation_check=false;
                }else{
                    nowlocation_check=true;
                }
                break;
            case R.id.btn_zoomin:
                CameraUpdate zin =CameraUpdate.zoomIn();
                nmap.moveCamera(zin);
                break;
            case R.id.btn_zoomout:
                CameraUpdate zout =CameraUpdate.zoomOut();
                nmap.moveCamera(zout);
                break;
            case R.id.btn_search_location:
               // t.start();
                String t= edt_search_location.getText().toString().trim();
                new SearchTask(t).execute();
                break;
        }
    }
Handler h = new Handler();
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        if(nmap==null){
            nmap=naverMap;
        }
         co=  new CircleOverlay();
        co.setRadius(250);
        co.setOutlineColor(Color.BLUE);
        co.setColor(Color.TRANSPARENT);
        co.setOutlineWidth(1);
            naverMap.setLocationSource(locationSource);
            naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
            naverMap.addOnLocationChangeListener(this);
    }

    @Override
    public void onLocationChange(@NonNull Location location) {
         nowlocation = new LatLng(location.getLatitude(),location.getLongitude());
        if(nowlocation_check) {
            CameraUpdate cup = CameraUpdate.scrollTo(nowlocation);
            cup.animate(CameraAnimation.Linear);
            if (firstcheck) {
                CameraUpdate zset = CameraUpdate.zoomTo(17);
                nmap.moveCamera(zset);
                firstcheck = false;
            }
            nmap.moveCamera(cup);
        }
        co.setCenter(nowlocation);
        co.setMap(nmap);



    }
        public void searchnear(String t){
            String clientId = "vEnaXmL4l6ZmkwcZS62K";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "Zzg8o4R5WU";//애플리케이션 클라이언트 시크릿값";
            try {
                String text = URLEncoder.encode(t, "UTF-8");

                String apiURL = "https://openapi.naver.com/v1/search/local?query="+ text+"&display=30"+"&start=1"; // json 결과
                //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
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
                JSONArray ja = result.getJSONArray("items");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jj = ja.getJSONObject(i);
                    String title=jj.getString("title");
                    String category=jj.getString("category");
                    String description =jj.getString("description");
                    String phone=jj.getString("telephone");
                    String adress = jj.getString("address");
                    String roadadress = jj.getString("roadAddress");

                    LatLng lg=geocoder(adress);
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            setmarker(lg.latitude,lg.longitude,title);
                        }
                    });

                                /*    try {
                                        List<Address> dd = geocoder.getFromLocation(lat, lng, 10);
                                        search(dd.get(0), name, false);
                                    } catch (IOException eee) {

                                    }*/
                    Log.d("이름", title);
                }

                System.out.println(response.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        public LatLng geocoder(String address){
        LatLng lg=null;
        String ncp_clientId = "b3zd0wlx07";//naver cloudplatform id
        String ncp_clientSecret = "Wr4itNIFasWDdOOsPyFRNrZxTg5WCP4WpQJ8EpXJ";//navercloudplatform 시크릿값";
        try {
            String text = URLEncoder.encode(address, "UTF-8");

            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+address; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", ncp_clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", ncp_clientSecret);
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
            try {
                JSONObject result = new JSONObject(response.toString());

                String status =result.getString("status");
                JSONObject xy=(JSONObject) result.getJSONArray("addresses").get(0);
                double x = Double.parseDouble(xy.getString("x"));
                double y = Double.parseDouble(xy.getString("y"));
                lg = new LatLng(y, x);
            }catch (JSONException e){

 /*               for (int i = 0; i < ja.length(); i++) {
                    JSONObject jj = ja.getJSONObject(i);
                    String title=jj.getString("title");
                    String category=jj.getString("category");
                    String description =jj.getString("description");
                    String phone=jj.getString("telephone");
                    String adress = jj.getString("address");
                    String roadadress = jj.getString("roadAddress");
                    Integer mapx = jj.getInt("mapx");
                    Integer mapy = jj.getInt("mapy");
                    GeoPoint gtp = new GeoPoint(mapx,mapy);
                    GeoPoint resultt=GeoTrans.convert(GeoTrans.TM,GeoTrans.GEO,gtp);
                    double lon =resultt.getX();
                    double lat =resultt.getY();*/
             /*       h.post(new Runnable() {
                        @Override
                        public void run() {
                            setmarker(lat,lon,title);
                        }
                    });
*/
                                /*    try {
                                        List<Address> dd = geocoder.getFromLocation(lat, lng, 10);
                                        search(dd.get(0), name, false);
                                    } catch (IOException eee) {

                                    }*/
                //   Log.d("이름", title);
            }

            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
        return lg;
    }
    public void nearsearch2(String address,LatLng latLng){
        String latlon=String.valueOf(latLng.longitude)+","+String.valueOf(latLng.latitude);
        String ncp_clientId = "b3zd0wlx07";//naver cloudplatform id
        String ncp_clientSecret = "Wr4itNIFasWDdOOsPyFRNrZxTg5WCP4WpQJ8EpXJ";//navercloudplatform 시크릿값";
        try {
            String text = URLEncoder.encode(address, "UTF-8");

            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-place/v1/search?query="+address+"&coordinate="+latlon+"&orderBy=weight"; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", ncp_clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", ncp_clientSecret);
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
            try {
                JSONObject result = new JSONObject(response.toString());

                String status =result.getString("status");
                JSONArray ja=result.getJSONArray("places");
                for(int i=0; i<ja.length(); i++) {
                    JSONObject jb=ja.getJSONObject(i);
                    String name =jb.getString("name");
                    double x = Double.parseDouble(jb.getString("x"));
                    double y = Double.parseDouble(jb.getString("y"));
                    double distance = Double.parseDouble(jb.getString("distance"));
                    //lg = new LatLng(y, x);
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            setmarker(y,x,name);
                        }
                    });
                }
            }catch (JSONException e){

 /*               for (int i = 0; i < ja.length(); i++) {
                    JSONObject jj = ja.getJSONObject(i);
                    String title=jj.getString("title");
                    String category=jj.getString("category");
                    String description =jj.getString("description");
                    String phone=jj.getString("telephone");
                    String adress = jj.getString("address");
                    String roadadress = jj.getString("roadAddress");
                    Integer mapx = jj.getInt("mapx");
                    Integer mapy = jj.getInt("mapy");
                    GeoPoint gtp = new GeoPoint(mapx,mapy);
                    GeoPoint resultt=GeoTrans.convert(GeoTrans.TM,GeoTrans.GEO,gtp);
                    double lon =resultt.getX();
                    double lat =resultt.getY();*/
             /*       h.post(new Runnable() {
                        @Override
                        public void run() {
                            setmarker(lat,lon,title);
                        }
                    });
*/
                                /*    try {
                                        List<Address> dd = geocoder.getFromLocation(lat, lng, 10);
                                        search(dd.get(0), name, false);
                                    } catch (IOException eee) {

                                    }*/
                //   Log.d("이름", title);
            }

            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public void setmarker(double lat,double lon,String title){
        LatLng loc=new LatLng(lat,lon);
        Marker m = new Marker();
        m.setPosition(loc);
        m.setCaptionText(title);
        m.setMap(nmap);

    }

    private void loadNearByPlaces(String text) {
        String clientId = "vEnaXmL4l6ZmkwcZS62K";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "Zzg8o4R5WU";//애플리케이션 클라이언트 시크릿값";
//YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works
        {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            Map<String, String> params = new HashMap<String, String>();
            JSONObject jsonObj = new JSONObject(params);

            Intent i = getIntent();
            String type = "hospital";

            StringBuilder naverPlacesUrl =
                    new StringBuilder("https://openapi.naver.com/v1/search/local.json?");
            naverPlacesUrl.append("query=").append(text);
            naverPlacesUrl.append("&display=").append(10);
            naverPlacesUrl.append("&start=").append(10);
            naverPlacesUrl.append("&sort=").append("random");
            naverPlacesUrl.append("&X-Naver-Client-Id=" + clientId);
            naverPlacesUrl.append("&X-Naver-Client-Secret=" + clientSecret);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, naverPlacesUrl.toString(), jsonObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject result) {
                            try {
                                Log.i("성공", "onResponse: Result= " + result.toString());
//                        parseLocationResult(result);
                                JSONArray ja = result.getJSONArray("results");

                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jj = ja.getJSONObject(i);
                                    JSONObject loc = jj.getJSONObject("geometry").getJSONObject("location");
                                    String title=jj.getString("title");
                                    String category=jj.getString("category");
                                    String description =jj.getString("description");
                                    String phone=jj.getString("telephone");
                                    String adress = jj.getString("address");
                                    String roadadress = jj.getString("rodadress");
                                    Integer lat = loc.getInt("mapx");
                                    Integer lng = loc.getInt("mapy");
                                /*    try {
                                        List<Address> dd = geocoder.getFromLocation(lat, lng, 10);
                                        search(dd.get(0), name, false);
                                    } catch (IOException eee) {

                                    }*/
                                    Log.d("이름", title);
                                }

                            } catch (JSONException e) {
                                Log.e("에러", e.getMessage().toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("실패1", "onErrorResponse: Error= " + error);
                            Log.e("실패2", "onErrorResponse: Error= " + error.getMessage());
                        }
                    });
            requestQueue.add(request);

        }
    }
    //Asynctask <doinbackgroud변수,progress변수,postexcute변수>
    public class SearchTask extends AsyncTask<Integer, Integer, Boolean> {
        ProgressDialog asyncDialog = new ProgressDialog(NaverMapActivity.this);
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

           // searchnear(text);
            nearsearch2(text,nowlocation);
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
}
