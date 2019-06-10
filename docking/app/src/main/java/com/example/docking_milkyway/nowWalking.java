package com.example.docking_milkyway;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firestore.v1.WriteResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.O)
public class nowWalking extends Fragment {

    // nowWalk : 산책중이 아닐 경우 false, 산책중인 경우 true
    boolean isNowWalking = false;

    // static var : save itself
    public static nowWalking nowW;

    View view;
    Context context;

    // variables : about weather value
    TextView cityField, detailsField, currentTemperatureField, weatherIcon, updatedField, dogAttribute;
    ProgressBar loader;
    double lat = 37.292047;
    double lon = 126.976890;
    String openWeatherMapApiKey = "0ee1e074bf0ef21fc295ffb1a78461d2";
    LocationManager locaManager;
    final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 225;

    // variables : gps helper
    GPSHelper gpsHelper;

    // variables : about record walking
    TextView startTime, endTime, elaspeTime, Distance;
    boolean isFirstRecording = true;
    boolean stopRecoding = false;
    WalkRecorder recorder = new WalkRecorder();
    Thread recorderThread = new Thread();
    Button finishWalking;

    @SuppressLint("HandlerLeak")
    final Handler timehandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void handleMessage(Message msg){
            // 현재 시간 - 시작 시간 하여 지속 시간 구하기
            LocalDateTime nowtime = LocalDateTime.now();
            Duration elaspe= Duration.between(recorder.starttime, nowtime);
            recorder.elaspetime = elaspe.getSeconds();
            Integer elaspeMin = (int) recorder.elaspetime / 60;
            Integer elaspeSec = (int) recorder.elaspetime % 60;
            elaspeTime.setText(elaspeMin + "분 " + elaspeSec+"초");
            Log.d("상아",elaspeMin + "분 " + elaspeSec+"초");
        }
    };

    @SuppressLint("HandlerLeak")
    final Handler distancehandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void handleMessage(Message msg) {
            // 이전 위치 설정
            recorder.prevGPS[0] = recorder.thisGPS[0];
            recorder.prevGPS[1] = recorder.thisGPS[1];
            // 현재 위치 갱신
            gpsHelper.getlocation();
            lat = gpsHelper.getLatitude();
            lon = gpsHelper.getLongitude();
            recorder.thisGPS[0] = lat;
            recorder.thisGPS[1] = lon;
            // 현재 위치 {lat,lon}을 walklog에 add한다
            recorder.walkLog.add(recorder.thisGPS);
            Log.d("상아","현재의 lat :"+lat+" / lon :"+lon);
            // calculate
            double addDistance = getDistance(recorder.prevGPS[0], recorder.prevGPS[1], recorder.thisGPS[0] , recorder.thisGPS[1]);
            recorder.distance += addDistance;
            Log.d("상아","distance : " + recorder.distance+ " / add : " + addDistance);

            Distance.setText(recorder.distance + "m");

        }
    };

    // variable : 견종
    // 테스트값입니다 이후에 userDB에서 가져오는 형식으로 수정하겠습니다 0531상아
    String dogSpecies = "Labrador Retriever";

    // --------------------------------------------------------------------------------------------------------------------------------------//

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // static var : save itself
        nowW = nowWalking.this;

        if (!isNowWalking){
            // 매칭할지 여부를 묻는 과정을 거침
            askforMatch();
        }

        // init locationManager and check permission
        locaManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (getActivity().checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            Log.d("상아", "nowWalk : fine location permission : granted" );
        }

        if (getActivity().checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            Log.d("상아", "nowWalk : coarse location permission : granted" );
        }

        // load location
        gpsHelper = new GPSHelper(context, locaManager);
        lat = gpsHelper.getLatitude();
        lon = gpsHelper.getLongitude();
        Log.d("상아", "nowWalk : lat : "+lat+" / lon : "+lon);

        // start to record walking
        Log.d("상아","시작시간 기록 (onCreate)");
        startRecording();



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        startTime = (TextView) view.findViewById(R.id.startTime);
        elaspeTime = (TextView) view.findViewById(R.id.elapseTime);
        Distance = (TextView) view.findViewById(R.id.distance);
        finishWalking = (Button) view.findViewById(R.id.finishWalking);

        // load weather
        loadWeather(lat, lon);

        // set startime
        startTime.setText(recorder.starttime.getHour() + "시" + recorder.starttime.getMinute() + "분");

        // set finish Walking button
        finishWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       endRecording();      }
        });


        // read dogattribute
        // String ArrayList로 받아서 일단 프린트만 했습니다
        String dogAttriArray = getDogAttribute(dogSpecies);
        String tmpStr=dogAttriArray;

        Log.d("상아","nowWalk : finished string : "+tmpStr);
        dogAttribute.setText(tmpStr);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        Log.d("상아","경과시간 기록(onResume)");
        keepRecording();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPause() {
        super.onPause();
        Log.d("상아","onPause");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStop() {
        super.onStop();
        Log.d("상아","onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("상아","onDestroy");
    }

    // --------------------------------------------------------------------------------------------------------------------------------------//

    // method : about matching
    // 산책 시작하기
    @RequiresApi(api = Build.VERSION_CODES.O)
    void askforMatch(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.start_walking, null);
        builder.setView(view);
        final Button alonePlz = (Button) view.findViewById(R.id.alonePlz);
        final Button matchPlz = (Button) view.findViewById(R.id.matchPlz);

        final AlertDialog dialog = builder.create();
        dialog.show();
        alonePlz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "바로 산책을 시작합니다.", Toast.LENGTH_SHORT).show();
                isNowWalking = true;
                dialog.dismiss();

            }
        });
        matchPlz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "산책메이트 매칭을 시작합니다.", Toast.LENGTH_SHORT).show();
                isNowWalking = true;
                dialog.dismiss();
                // start matching process
                Intent intent = new Intent(getActivity(), Matching.class);
                startActivity(intent);
            }
        });

    }

    // --------------------------------------------------------------------------------------------------------------------------------------//

    // method : about weather
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
            Log.d("상아","loadWeather : 네트워크 사용이 불가하여 날씨정보를 받아오지 못했습니다.");
        }
    }

    // --------------------------------------------------------------------------------------------------------------------------------------//

    // method : about dog attribute --> this inforamtion care about : warning toast message / matching process
    // 반려견의 견종으로 관련 정보들을 받아온다
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
            Log.d("상아","getDotAttri : get dog.json from asset");

            // make json object
            JSONObject obj = new JSONObject(json);
            // 해당하는 견종의 정보를 받아옴
            String dogArray = obj.getJSONObject("dog_breeds").getString(DogSpecies);
            Log.d("상아","getDotAttri : get info of test dogspecies");

            return dogArray;
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            Log.d("상아","getDotAttri : unsupported encoding");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("상아","getDotAttri : io exception");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("상아","getDotAttri : json  exception");
        }
        return "error";

    }

    // --------------------------------------------------------------------------------------------------------------------------------------//

    // 산책을 시작하고 시작시간과 처음 위치를 측정한다
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startRecording(){
        if (isFirstRecording){
            Log.d("상아","첫 기록입니다. 시작 시간을 기록합니다.");
            // 시작 시간 기록
            recorder.starttime = LocalDateTime.now();
            Log.d("상아", "start record : "+recorder.starttime.toString());

            // 처음 위치 기록
            gpsHelper.getlocation();
            lat = gpsHelper.getLatitude();
            lon = gpsHelper.getLongitude();
            double[] latlon = {lat, lon};
            recorder.walkLog.add(latlon);
            recorder.thisGPS[0]=lat;
            recorder.thisGPS[1]=lon;
            Log.d("상아","start record : lat : "+lat+" / lon"+lon);

            // setting
            stopRecoding = false;
        }
        else{
            Log.d("상아","첫 기록이 아닙니다. 기존 시작 시간을 사용합니다.");
        }
    }

    // 경과 시간과 거리를 지속적으로 측정한다
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void keepRecording(){
        recorderThread= new Thread(){
            public void run(){
                if (isFirstRecording){
                    isFirstRecording = false;
                    int tmpFlag = 0;
                    while(true){
                        tmpFlag = (tmpFlag+1)%5;
                        if(stopRecoding){       break;                  }
                        try{                    sleep(1000);      }
                        catch (Exception e) {   e.printStackTrace();    }
                        // time : 1초마다 갱신
                        timehandler.sendEmptyMessage(0);
                        // time : 5초마다 갱신
                        if (tmpFlag==0){
                            distancehandler.sendEmptyMessage(0);
                        }
                    }
                }
            }
        };
        recorderThread.start();
    }

    // 산책을 종료하고 시작시간, 경과시간, 거리, 종료시간을 firebase에 저장한다
    public void endRecording(){
        Toast.makeText(context.getApplicationContext(), "산책을 완료합니다.", Toast.LENGTH_SHORT).show();

        // 산책한 정보 정리
        recorder.endtime = LocalDateTime.now();
        Duration elaspe= Duration.between(recorder.starttime, recorder.endtime);
        recorder.elaspetime = elaspe.getSeconds();      // 초 단위로 저장

        // 저장했던 walklog의 distance 계산
        /*int walkLogLen = recorder.walkLog.size();
        for (int i=0 ; i<walkLogLen-1 ; i++){
            double[] nowP = recorder.walkLog.get(i);
            double[] nextP = recorder.walkLog.get(i+1);
            double smallDistance = getDistance(nowP[0], nowP[1], nextP[0], nextP[1]);
            recorder.distance += smallDistance;         // 미터 단위로 저장
        }*/

        // 저장했던 walklog들을 ArrayList<Double> lat0, lon0, lat1, lon1, lat2, lon2, ...}으로 변환
        ArrayList<Double> walkLogArr = new ArrayList<>();
        for (double[] i : recorder.walkLog){
            walkLogArr.add(i[0]);
            walkLogArr.add(i[1]);
        }

        // WalkingDB object로 저장
        Timestamp stTime    = LocalDateTimeToTimestamp(recorder.starttime);
        Timestamp enTime    = LocalDateTimeToTimestamp(recorder.endtime);
        int elTime          = (int)recorder.elaspetime;
        int disT            = (int)recorder.distance;
        int d_SSN = 1111;    // 이후 사용자 정보 가져와서 연결되면 기입
        final WalkingDB tmp_Walk = new WalkingDB(stTime, enTime, elTime, disT, d_SSN, walkLogArr);

        // 현재정보를 파이어베이스에 저장
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 문서이름을 산책 끝 시간으로 정하기
        int m = recorder.endtime.getMonthValue();
        int d = recorder.endtime.getDayOfMonth();
        int h = recorder.endtime.getHour();
        int n = recorder.endtime.getMinute();
        String userID = "shapizz@naver.com";      // 문서 이름 유저아이디로 수정하기
        db.collection("Walking").document(userID).set(tmp_Walk);
        // wNum <- db.colletion("Walking").document(userID).get(length)
        // wNum++1 -> 다시 set 하고
        // db.collection("Walking").document(userID).collection(userID), document("h"+wNum) 하도록 수정하겠습니다




        Toast.makeText(context.getApplicationContext(), "오늘의 산책을 저장하였습니다.", Toast.LENGTH_SHORT).show();
        Log.d("상아","산책 저장 완료");

        isNowWalking = false;
        isFirstRecording = true;
        stopRecoding = true;
        recorder = new WalkRecorder();
        recorderThread.interrupt();

        startTime.setText("00시00분");
        elaspeTime.setText("00분00초");
        Distance.setText("00.00km");

    }

    // locatdatetime -> timestamp
    public Timestamp LocalDateTimeToTimestamp (LocalDateTime ldt){
        char[] tmpChar = ldt.toString().toCharArray();
        String tmpStr = "";
        for (char i : tmpChar){
            String j = Character.toString(i);
            if (i == 'T'){  tmpStr=tmpStr+" ";    }
            else{           tmpStr=tmpStr+j;        }
            System.out.println(j);
        }
        System.out.println(tmpStr+" test1");
        Timestamp result = Timestamp.valueOf(tmpStr);
        return result;
    }

    // GPS lat, lon -> distance
    // reference : https://raw.githubusercontent.com/janantala/GPS-distance/master/java/Distance.java
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563;
        double L = Math.toRadians(lon2 - lon1);
        double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
        double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);
        double cosSqAlpha;
        double sinSigma;
        double cos2SigmaM;
        double cosSigma;
        double sigma;

        double lambda = L, lambdaP, iterLimit = 100;
        do
        {
            double sinLambda = Math.sin(lambda), cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt(	(cosU2 * sinLambda)
                    * (cosU2 * sinLambda)
                    + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
                    * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
            );
            if (sinSigma == 0)
            {
                return 0;
            }

            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;

            double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = 	L + (1 - C) * f * sinAlpha
                    * 	(sigma + C * sinSigma
                    * 	(cos2SigmaM + C * cosSigma
                    * 	(-1 + 2 * cos2SigmaM * cos2SigmaM)
            )
            );

        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        if (iterLimit == 0)
        {
            return 0;
        }

        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384
                * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma =
                B * sinSigma
                        * (cos2SigmaM + B / 4
                        * (cosSigma
                        * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
                        * (-3 + 4 * sinSigma * sinSigma)
                        * (-3 + 4 * cos2SigmaM * cos2SigmaM)));

        double s = b * A * (sigma - deltaSigma);

        return s;
    }

    // --------------------------------------------------------------------------------------------------------------------------------------//


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

            } catch (JSONException e1) {
                Toast.makeText(context.getApplicationContext(), "날씨 정보를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e2){
                Toast.makeText(context.getApplicationContext(), "날씨 정보를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
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
