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
    private String searchUrl = "http://projetannuel4iabd.yj.fr/script_search_place.php";

    @Override
    protected ArrayList<Horodateur> doInBackground(String... strings) {
        ArrayList<Horodateur> horodateurs = null;

        if (strings.length != 5) {
            return null;
        } else {

            Log.i("Connexion", "Started");
            try {
                Log.w("==========> ", "1");
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

                Log.w("==========> ", "2");
                URL url = new URL(searchUrl);
                Log.w("==========> ", "3");

                URLConnection conn = url.openConnection();
                Log.w("==========> ", "4");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                Log.w("==========> ", "5");
                wr.write(data);
                Log.w("==========> ", "6");
                wr.flush();
                Log.w("==========> ", "7");


                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                Log.w("==========> ", "8");
                StringBuilder sb = new StringBuilder("[");
                String line;

                Log.w("==========> ", "9");
                while ((line = reader.readLine()) != null) {
                    sb.append(line + ",\n");
                }

                Log.w("==========> ", "10");

                String result = sb.toString();
                result = result.substring(0, result.length() - 2) + "]";

                Log.w("==========> ", result);
                Log.w("==========> ", "11");
                JSONArray horodateurs_jArray = new JSONArray(result);

                Log.w("==========> ", "12");
                for(int i=0; i < horodateurs_jArray.length(); i++) {
                    JSONObject horodateur_data = horodateurs_jArray.getJSONObject(i);
                    Log.w("==========> ", "13");

                    horodateurs.add(
                            new Horodateur(
                                    horodateur_data.getString("horodateur_id"),
                                    horodateur_data.getDouble("horodateur_latitude"),
                                    horodateur_data.getDouble("horodateur_longitude"),
                                    horodateur_data.getInt("horodateur_nb_places_reel")
                            ));
                }
                Log.w("==========> ", "14");

            } catch (Exception ex) {
                Log.i("log_tag", "Error " + ex.getMessage() + ex.toString());
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
