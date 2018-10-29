package com.backcountrydesigngroup.android.pandasleuth;
/**     Copyright (C) 2016 The Android Open Source Project

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **/
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class PandaQuery {

    static final String LOG_TAG = PandaQuery.class.getSimpleName();

    private PandaQuery() {
    }

    /**
     * This is a wrapper method that requests a creation of URL from a string url,
     * requests an HTTP request, requests the formation of a response into a string, passes the string
     * to a parser, then spits the requested information out to the EarthquakeActivity.java file
     * Returns an {@link ArrayList} object to represent a single earthquake.
     */

    public static ArrayList<article> getArticles(String queryURL) {
        // Create URL object
        URL url = createURL(queryURL);
        Log.e(LOG_TAG, "A URL Request has been pulled.");

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = httpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Parse the JSON response and suck out each article, returns an Arraylist of articles with section, title, date, and author(s)
        ArrayList<article> articles = parseArticles(jsonResponse);

        // Return the {@link ArrayList}
        return articles;
    }

    /**
     * This converts a string url to Java's absolute url
     *
     * @param urlString
     * @return the url object
     */
    private static URL createURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            return null;
        }
        return url;
    }

    // httpRequest submits the url request
    private static String httpRequest(URL url) throws IOException {
        String JSONResponse = "";
        if (url == null) {
            return JSONResponse;
        }

        HttpURLConnection connection = null;
        InputStream HTTPResponse = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                HTTPResponse = connection.getInputStream();
                JSONResponse = streamStringBuilder(HTTPResponse);
            } else {
                Log.e("ResponseCode", "The Response Code is: " + connection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e("IOException", "IOException has been thrown: " + e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (HTTPResponse != null) {
                HTTPResponse.close();
            }
        }
        return JSONResponse;
    }

    /**
     * Takes the JSONResponse from httpRequest {@link InputStream} and builds it into a string.
     */
    private static String streamStringBuilder(InputStream inputStream) throws IOException {
        // set the output
        StringBuilder output = new StringBuilder();

        // Check to ensure the input stream is actually reality; else simply return an empty string
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a ArrayList of {@link article} objects from the parsed JSON text
     */
    private static ArrayList<article> parseArticles(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<article> articleList = new ArrayList<>();

        // Parse jsonResponse; if there is a problem, an error is thrown.
        try {

            // Get the root
            JSONObject jsonObj = new JSONObject(jsonResponse);
            // Get the first (and only) object, "response"
            JSONObject response = jsonObj.getJSONObject("response");
            // Get the results array
            JSONArray results = response.getJSONArray("results");
            // Loop through the results array and each desired entry into a new array
            for (int i = 0; i < results.length(); i++) {
                JSONObject placeholder = results.getJSONObject(i);
                String sectionName = placeholder.getString("sectionName");
                String webPublicationDate = placeholder.getString("webPublicationDate");
                String webTitle = placeholder.getString("webTitle");
                String webUrl = placeholder.getString("webUrl");
                JSONObject fields = placeholder.getJSONObject("fields");
                String byline = fields.getString("byline");

                // assemble the list
                articleList.add(new article(webTitle, byline, webPublicationDate, sectionName, webUrl));
            }

        } catch (JSONException e) {
            // on mess up...
            Log.e("PandaQuery", "JSON parse error: ", e);
        }

        return articleList;
    }

}