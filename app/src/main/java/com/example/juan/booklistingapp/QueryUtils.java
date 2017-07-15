package com.example.juan.booklistingapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
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

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String KEY_VOLUMEINFO = "volumeInfo";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHORS = "authors";
    private static final String KEY_PUBLISHED_DATE = "publishedDate";
    private static final String KEY_SEARCH_INFO = "searchInfo";
    private static final String KEY_TEXTSNIPPET = "textSnippet";
    private static final String KEY_PREVIEWLINK = "previewLink";
    private static String imageLink;
    private static Bitmap bmp = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.no_image);


    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    public static ArrayList<Book> fetchBookData(String requestUrl) {
        Log.e("Request URL", requestUrl);
        URL url = createUrl(requestUrl);
        String response = null;
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        ArrayList<Book> books = extractFeatures(response);
        return books;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String response = "";
        if (url == null) {
            return response;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return response;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
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

    private static ArrayList<Book> extractFeatures(String json) {
        ArrayList<Book> bookItems = new ArrayList<>();
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONObject baseJson = new JSONObject(json);
            JSONArray items = baseJson.getJSONArray("items");

            String title;
            JSONArray authors;
            String author;
            String publishedDate;
            String textSnippet;
            String previewLink;


            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject volumeInfo = item.getJSONObject(KEY_VOLUMEINFO);
                title = volumeInfo.getString(KEY_TITLE);
                if (volumeInfo.has(KEY_AUTHORS)) {
                    authors = volumeInfo.getJSONArray(KEY_AUTHORS);
                    author = authors.getString(0);
                } else {
                    author = null;
                }
                if (volumeInfo.has(KEY_PUBLISHED_DATE)) {
                    publishedDate = volumeInfo.getString(KEY_PUBLISHED_DATE);
                } else {
                    publishedDate = "No published date available.";
                }

                JSONObject searchInfo = item.getJSONObject(KEY_SEARCH_INFO);
                if (searchInfo.has(KEY_TEXTSNIPPET)) {
                    textSnippet = searchInfo.getString(KEY_TEXTSNIPPET);

                } else {
                    textSnippet = "No description available.";
                }

                if (volumeInfo.has(KEY_PREVIEWLINK)) {
                    previewLink = volumeInfo.getString(KEY_PREVIEWLINK);
                } else {
                    previewLink = "No information link available.";
                }

                if (volumeInfo.has("imageLinks")) {
                    JSONObject image = volumeInfo.getJSONObject("imageLinks");
                    imageLink = image.getString("smallThumbnail");
                    try {
                        URL url = new URL(imageLink);
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (MalformedURLException e) {
                        Log.e(LOG_TAG, "Invalid Image URL", e);
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Cannnot load image", e);
                    }
                }
                Book bookItem = new Book(bmp, title, author, publishedDate, textSnippet, previewLink);
                bookItems.add(bookItem);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return bookItems;
    }
}