package com.example.bohdan.sms_sender.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bohdan.sms_sender.R;

/**
 * Created by User on 24-Jun-16.
 */
public class PreviewDialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    private TextView smsTextPreview;
    private String text;
    private String title;

    public PreviewDialogFragment() {
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_dialog_fragment, container);
        smsTextPreview = (TextView) view.findViewById(R.id.smsTextDialog);
        getDialog().setTitle(title);
        smsTextPreview.setText(text);

        return view;
    }

    @Override
    public void onClick(View v) {


    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
