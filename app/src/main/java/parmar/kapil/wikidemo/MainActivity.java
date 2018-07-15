package parmar.kapil.wikidemo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String searchName = "";
    String strUrl;
    ProgressBar progressBar;

    EditText edtSearch;
    ArrayList dataModels;
    ListView listView;
    private static CustomAdapter adapter;
    ImageView wiKiImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtSearch = (EditText)findViewById(R.id.edt_search);
        listView = (ListView)findViewById(R.id.listview);
        progressBar =(ProgressBar)findViewById(R.id.progress);
        wiKiImage = (ImageView)findViewById(R.id.wiki_image);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (isNetworkConnected()) {
                    if (edtSearch.getText().toString().length() > 0) {
                        wiKiImage.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        searchName = edtSearch.getText().toString();
                        strUrl = "https://en.wikipedia.org//w/api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch=" + searchName + "&gpslimit=10";
                        new GetWikiData().execute();
                    } else {

                        listView.setAdapter(null);
                        listView.setVisibility(View.GONE);
                        wiKiImage.setVisibility(View.VISIBLE);

                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"No Inernet Connection",Toast.LENGTH_LONG).show();
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataModel dataModel = (DataModel) dataModels.get(i);
                String url = "https://en.wikipedia.org/wiki/"+dataModel.getName();
                Intent intent = new Intent(MainActivity.this,WebResult.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public class GetWikiData extends AsyncTask<String, String,String>{

        HttpURLConnection urlConnection;
        JSONObject jsonObjectImage;
        JSONObject jsonObjectTerms;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {


            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(strUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.GONE);

            try {
                JSONObject jsonMainObject = new JSONObject(s);
                JSONObject jsonObjectQuerry = new JSONObject(String.valueOf(jsonMainObject.getJSONObject("query")));
                JSONArray jsonArray =  jsonObjectQuerry.getJSONArray("pages");
                dataModels= new ArrayList<>();

                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String imageString = "string";
                    String DescrString = "Description not available";


                    if (jsonObject.has("thumbnail"))
                        imageString = jsonObject.getJSONObject("thumbnail").getString("source");

                    if (jsonObject.has("terms")){
                        jsonObjectTerms = jsonObject.getJSONObject("terms");
                        DescrString =  jsonObjectTerms.getString("description").replace("[","").replace("]","").toString();
                    }

                    dataModels.add(new DataModel(jsonObject.getString("title"), DescrString,imageString));


                }
                listView.setAdapter(null);
                adapter= new CustomAdapter(dataModels,getApplicationContext());

                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
