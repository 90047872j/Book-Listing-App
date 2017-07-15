package com.example.juan.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;

        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }
        Book currentBook = getItem(position);

        ImageView imageView = (ImageView) listView.findViewById(R.id.imageView);
        imageView.setImageBitmap(currentBook.getImage());

        TextView tvTitle = (TextView) listView.findViewById(R.id.textView_title);
        tvTitle.setText(currentBook.getTitle());

        TextView tvAuthor = (TextView) listView.findViewById(R.id.textView_author);
        tvAuthor.setText(currentBook.getAuthor());

        TextView tvPDate = (TextView) listView.findViewById(R.id.textView_PDate);
        tvPDate.setText(currentBook.getPublishDate());

        TextView tvDescription = (TextView) listView.findViewById(R.id.textView_description);
        tvDescription.setText(currentBook.getDescription());

        return listView;
    }
}
