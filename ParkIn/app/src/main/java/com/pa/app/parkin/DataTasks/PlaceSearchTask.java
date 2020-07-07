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


                String data = URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
                data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(lng, "UTF-8");
                data += "&" + URLEncoder.encode("rayon", "UTF-8") + "=" + URLEncoder.encode(perimeter, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                data += "&" + URLEncoder.encode("heure", "UTF-8") + "=" + URLEncoder.encode(hour, "UTF-8");

                BufferedReader reader;

                URL url = new URL(searchUrl);

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder("[");
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + ",\n");
                }

                String result = sb.toString();
                result = result.substring(0, result.length() - 2) + "]";

                JSONArray horodateurs_jArray = new JSONArray(result);

                for(int i=0; i < horodateurs_jArray.length(); i++) {
                    JSONObject horodateur_data = horodateurs_jArray.getJSONObject(i);

                    horodateurs.add(
                            new Horodateur(
                                    horodateur_data.getString("horodateur_id"),
                                    horodateur_data.getDouble("horodateur_latitude"),
                                    horodateur_data.getDouble("horodateur_longitude"),
                                    horodateur_data.getInt("horodateur_nb_places_reel")
                            ));
                }

            } catch (Exception ex) {
                Log.e("PlaceSearchError", ex.getMessage());
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
        String date = devUtils.formattedDateToString(searchContext.getDateTimeContext());
        String hour = devUtils.formattedHourToString(searchContext.getDateTimeContext());

        try{
            searchResult = this.execute(lat, lng, perimeter, date, hour).get();
        } catch (Exception e) {
            Log.e("PlaceSearchError", "An error occured when searching for places \n"+e.getMessage());
        }
        return searchResult;
    }
}
