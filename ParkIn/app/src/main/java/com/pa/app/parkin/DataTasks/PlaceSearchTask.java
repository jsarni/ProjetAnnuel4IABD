package com.pa.app.parkin.DataTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pa.app.parkin.Horodateur;
import com.pa.app.parkin.SearchContext;
import com.pa.app.parkin.Utils.DevUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PlaceSearchTask extends AsyncTask<String, Void, ArrayList<Horodateur>> {

    DevUtils devUtils = DevUtils.getInstance();
    private String searchUrl = "http://ec2-54-174-245-36.compute-1.amazonaws.com/script_search_place.php";

    @Override
    protected ArrayList<Horodateur> doInBackground(String... strings) {
        ArrayList<Horodateur> horodateurs = null;

        if (strings.length != 5) {
            return null;
        } else {

            try {
                String lat = strings[0];
                String lng = strings[1];
                String perimeter = strings[2];
                String date = strings[3];
                String hour = strings[4];
                horodateurs = new ArrayList<Horodateur>();
                Log.e("DEBUUUUG", "1");

                Log.e("DEBUUUUUG", date);
                String data = URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
                data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(lng, "UTF-8");
                data += "&" + URLEncoder.encode("rayon", "UTF-8") + "=" + URLEncoder.encode(perimeter, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                data += "&" + URLEncoder.encode("heure", "UTF-8") + "=" + URLEncoder.encode(hour, "UTF-8");

                Log.e("DEBUUUUG",data);
                Log.e("DEBUUUUG", "2");
                BufferedReader reader;

                URL url = new URL(searchUrl);
                Log.e("DEBUUUUG", "3");

                URLConnection conn = url.openConnection();
                Log.e("DEBUUUUG", "4");
                conn.setDoOutput(true);
                Log.e("DEBUUUUG", "5");

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                Log.e("DEBUUUUG", conn.getOutputStream().toString());
                wr.write(data);
                Log.e("DEBUUUUG", "7");

                wr.flush();
                Log.e("DEBUUUUG", "8");

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                Log.e("DEBUUUUG", "9");
                StringBuilder sb = new StringBuilder("");
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + ",\n");
                    Log.e("DEBUUUUG", line);
                }
                Log.e("DEBUUUUG", "10");

                String result = sb.toString();
                Log.e("DEBUUUUG", "11");
                Log.e("DEBUUUUG", "11");

//                result = result.substring(1, result.length() - 2);

                JSONArray horodateurs_jArray = new JSONArray(result);
                Log.e("DEBUUUUG", "12");

                for(int i=0; i < horodateurs_jArray.length(); i++) {
                    JSONObject horodateur_data = horodateurs_jArray.getJSONObject(i);

                    horodateurs.add(
                            new Horodateur(
                                    horodateur_data.getDouble("horodateur_latitude"),
                                    horodateur_data.getDouble("horodateur_longitude"),
                                    horodateur_data.getInt("horodateur_nb_places_reel")
                            ));
                }

            } catch (Exception ex) {
                Log.e("PlaceSearchError", ex.toString());
            } finally {
                return horodateurs;
            }
        }
    }

    public ArrayList<Horodateur> searchPlaces(SearchContext searchContext){
        ArrayList<Horodateur> searchResult = null;

        String lat = String.valueOf(searchContext.getPosition().latitude);
        String lng = String.valueOf(searchContext.getPosition().longitude);
        String perimeter = String.valueOf(searchContext.getPerimeter());
        String date = devUtils.formattedDateToString2(searchContext.getDateTimeContext());
        String hour = devUtils.formattedHourToString(searchContext.getDateTimeContext());

        try{
            searchResult = this.execute(lat, lng, perimeter, date, hour).get();
        } catch (Exception e) {
            Log.e("PlaceSearchError", "An error occured when searching for places \n"+e.getMessage());
        }
        return searchResult;
    }
}
