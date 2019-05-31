package com.example.docking_milkyway;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class nowWalking extends Fragment {
    View view;
    Context context;

    // variables : about weather value
    TextView cityField, detailsField, currentTemperatureField;
    TextView weatherIcon, updatedField, dogAttribute;
    ProgressBar loader;
    double lat = 37.54;
    double lon = 126.98;
    String openWeatherMapApiKey = "0ee1e074bf0ef21fc295ffb1a78461d2";

    // variables : about GPS connection
    LocationManager locationManager;
    Location location;
    boolean isGpsEnabled = false;
    boolean isGetLocation = false;
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    public static final long MIN_TIME_UPDATES = 1000 * 60 * 1;

    // variable : 견종
    // 테스트값입니다 이후에 userDB에서 가져오는 형식으로 수정하겠습니다 0531상아
    String dogSpecies = "Labrador Retriever";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load location
        location = getLocation();
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.d("상아", "lat : "+String.valueOf(lat)+" / lon : "+String.valueOf(lon));
        }




    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.now_walking, container, false);
        context =  view.getContext();

        // connect with now_walking.xml
        cityField = (TextView) view.findViewById(R.id.city_field);
        updatedField = (TextView) view.findViewById(R.id.updated_field);
        detailsField = (TextView) view.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) view.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) view.findViewById(R.id.weather_icon);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        dogAttribute = (TextView) view.findViewById(R.id.dog_attribute);

        // load weather
        loadWeather(lat, lon);

        // read dogattribute
        // String ArrayList로 받아서 일단 프린트만 했습니다
        String dogAttriArray = getDogAttribute(dogSpecies);
        String tmpStr=dogAttriArray;
      /*  for (String s : dogAttriArray){
            tmpStr = tmpStr + s + "\t";
        }*/
        Log.d("상아","finished string : "+tmpStr);
        dogAttribute.setText(tmpStr);


        return view;
    }

    // 네트워크 연결상태 체크
    public static boolean checkNetwork(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
    // api키로 url을 구성하고 날씨 정보를 받아온다
    public void loadWeather(double lat, double lon) {
        if (checkNetwork(context.getApplicationContext())) {
            String URL = WeatherHelper.excuteGet("http://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                    "&lon=" + lon + "&units=metric&appid=" + openWeatherMapApiKey);
            downloadWeather task = new downloadWeather();
            task.execute(URL);
        } else {
            Log.d("상아","네트워크 사용이 불가하여 날씨정보를 받아오지 못함");
        }
    }

    // gps가 켜져있을경우 위치정보를 받아와서 기존 위치에 덧씌움
    // 현재 작동하지 않음0531 - 이후 수정하도록 하겠습니다!
    @SuppressLint("MissingPermission")
    public Location getLocation(){
        try{
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            // GPS 사용 가능한지 알아오기
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // gps 사용 불가한 경우
            if (!isGpsEnabled){
                Toast.makeText(context.getApplicationContext(), "GPS를 사용할 수 없어 현재 날씨 정보를 받아올 수 없습니다.", Toast.LENGTH_LONG).show();
            }
            // gps 사용 가능한 경우
            else {
                Log.d("상아","get weatherinfo from GPS");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener );
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.d("상아","get weatherinfo from GPS success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("상아", "exception occur : during get gps location");
        }
        return location;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {      loadWeather(location.getLatitude(), location.getLongitude());   }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    public String getDogAttribute(String DogSpecies){
        String json = null;
        ArrayList<String> dogAttribute = new ArrayList<String>();
        try {
            // read json file from asset
            InputStream is = getResources().getAssets().open("dog.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.d("상아","get dog.json from asset");

            // make json object
            JSONObject obj = new JSONObject(json);
            // 해당하는 견종의 정보를 받아옴
            String dogArray = obj.getJSONObject("dog_breeds").getString(DogSpecies);
            Log.d("상아","get info of test dogspecies");

            return dogArray;
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            Log.d("상아","unsupported encoding");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("상아","io exception");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("상아","json  exception");
        }
        return "error";

    }


    class downloadWeather extends AsyncTask< String, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loader.setVisibility(view.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {
            String URL = WeatherHelper.excuteGet("http://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                    "&lon=" + lon + "&units=metric&appid=" + openWeatherMapApiKey);
            return URL;
        }

        @Override
        protected void onPostExecute(String URL) {
            // get weather from open-weather-api & parse it
            try {
                JSONObject json = new JSONObject(URL);

                JSONObject weather= json.getJSONArray("weather").getJSONObject(0);
                JSONObject main = json.getJSONObject("main");
                DateFormat df = DateFormat.getDateTimeInstance();

                // 도시, 국가
                String cityName = json.getString("name").toUpperCase(Locale.US);
                String countryName = json.getJSONObject("sys").getString("country");
                cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));

                // 날씨
                int weatherID = weather.getInt("id");
                String weatherKR = weatherTranslate(weatherID);  //날씨id받아서 한글로 번역
                detailsField.setText(weatherKR);

                // 온도 섭씨
                currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + "ºC");
                // 불러온 시간
                updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));

                // 날씨 아이콘
                int actualId = weather.getInt("id");
                int id = actualId /100;
                long sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
                long sunset = json.getJSONObject("sys").getLong("sunset") * 1000;
                Drawable wthIcon;

                if(actualId == 800) {
                    long currentTime = new Date().getTime();
                    if (currentTime >= sunrise && currentTime < sunset) {
                        wthIcon = getResources().getDrawable(R.drawable.a01d);
                    } else {
                        wthIcon = getResources().getDrawable(R.drawable.a01n);
                    }
                } else {
                    switch (id) {
                        case 2:     wthIcon = getResources().getDrawable(R.drawable.a11d);
                            break;
                        case 3:     wthIcon = getResources().getDrawable(R.drawable.a09d);
                            break;
                        case 7:     wthIcon = getResources().getDrawable(R.drawable.a50d);
                            break;
                        case 8:     wthIcon = getResources().getDrawable(R.drawable.a03d);
                            break;
                        case 6:     wthIcon = getResources().getDrawable(R.drawable.a13d);
                            break;
                        case 5:     wthIcon = getResources().getDrawable(R.drawable.a09d);
                            break;
                        default :   wthIcon = getResources().getDrawable(R.drawable.a01d);
                    }
                }
                // set weather icon
                weatherIcon.setCompoundDrawablesWithIntrinsicBounds(wthIcon,null,null,null);
                // 로딩바
                loader.setVisibility(View.GONE);

            } catch (JSONException e) {
                Toast.makeText(context.getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }
        }

        // 날씨 부분을 한글로 변역
        public String weatherTranslate(int ID){
            String weatherKR="";

            // 2xx Thunderstorm
            // 3xx Drizzle
            // 5xx Rain
            // 6xx Snow
            // 80x clouds
            if (200<=ID && ID<300){         ID = 200;   }
            else if (300<=ID && ID<400){    ID = 300;   }
            else if (500<=ID && ID<600){    ID = 500;   }
            else if (600<=ID && ID<700){    ID = 600;   }
            else if (800<=ID && ID<900){    ID = 801;   }
            else {      }

            // transfer ENG weather to KOR
            switch (ID){
                case 200:  weatherKR="뇌우";      break;
                case 300:  weatherKR="부슬비";    break;
                case 500:  weatherKR="비";        break;
                case 600:  weatherKR="눈";        break;

                case 701:  weatherKR="엷은 안개";  break;   //mist
                case 711:  weatherKR="안개";       break;   //smoke
                case 721:  weatherKR="실안개";     break;   //haze
                case 731:  weatherKR="회오리바람";  break;   //dust
                case 741:  weatherKR="안개";       break;   //Fog
                case 751:  weatherKR="모래안개";    break;  //Sand
                case 761:  weatherKR="황사";       break;   //Dust
                case 762:  weatherKR="화산재";     break;   //Ash
                case 771:  weatherKR="돌풍";       break;   //Tornado
                case 781:  weatherKR="토네이도";   break;   //Tornado

                case 800:  weatherKR="맑음";      break;
                case 801:  weatherKR="구름";      break;
            }
            return weatherKR;
        }
    }
}
