package com.backcountrydesigngroup.android.pandasleuth;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class articleAdapter extends ArrayAdapter<article> {
    public articleAdapter(@NonNull Activity context, @NonNull ArrayList<article> objects) {
        super(context, 0, objects);
    }

    static final String LOG_TAG = articleAdapter.class.getSimpleName();

/**
public class articleAdapter extends ArrayAdapter<articleAdapter> {
    public articleAdapter(Activity context, int something, ArrayList<article> arrayListArticles) {
        super(context, 0, arrayListArticles);
    }
**/
    //The below comes from:  https://github.com/udacity/ud839_CustomAdapter_Example/blob/master/app/src/main/java/com/example/android/flavor/AndroidFlavorAdapter.java
    //With modifications.  Per the license agreement, the original copyright and license notice is posted below.

    /*
     * Copyright (C) 2016 The Android Open Source Project
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        article currentArticle = getItem(position);

        // Get and set the article number to place into the square
        TextView numberTextView = (TextView) listItemView.findViewById(R.id.number_text_view);
        int articleNumber = position + 1;
        numberTextView.setText(String.valueOf(articleNumber));
        Log.e(LOG_TAG, "The adapter position is: "+position);

        // Get and set the article section name for the current articleLong
        String section = currentArticle.getSection();
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_text_view);
        sectionTextView.setText(String.valueOf(section));
        Log.e(LOG_TAG,"The article section name is: "+String.valueOf(section));

        // Get and set the article name for the current article
        String title = currentArticle.getName();
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        nameTextView.setText(String.valueOf(title));
        Log.e(LOG_TAG,"The article name is: "+String.valueOf(title));

        // Get and set the article date, if it exists
        // The replacement of Z with +0000 thanks to:  https://www.mkyong.com/java/how-to-convert-string-to-date-java/
        String inputDate = currentArticle.getDate();
        SimpleDateFormat stringFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = null;
        try {
            date = stringFormatter.parse(inputDate.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            Log.e(LOG_TAG,"We caught a date error: ",e);
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String outputDate = dateFormatter.format(date);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(outputDate);
        Log.e(LOG_TAG,"The article date is: "+outputDate);


        // Get and set the article author, if s/he/they exists
        String author = currentArticle.getByline();
        if (author==null) {
            author = "";
            }
        TextView bylineTextView = (TextView) listItemView.findViewById(R.id.byline_text_view);
        bylineTextView.setText(author);
        Log.e(LOG_TAG,"The author is: "+author);

        return listItemView;
    }
}
