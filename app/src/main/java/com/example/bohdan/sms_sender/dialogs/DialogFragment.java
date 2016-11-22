package com.example.bohdan.sms_sender.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bohdan.sms_sender.R;

/**
 * Created by User on 24-Jun-16.
 */
public class DialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    private EditText mEditText;
    private EditText mEditTextName;

    private String smsTitle;
    private String smsText;
    private String url_text;

    public DialogFragment() {
    }

    public String getUrl_text() {
        return url_text;
    }

    public void setUrl_text(String url_text) {
        this.url_text = url_text;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        mEditTextName = (EditText) view.findViewById(R.id.txt_your_URL_NAME);
        mEditText.setText(smsText);
        mEditTextName.setText(smsTitle);
        getDialog().setTitle("URL");
        final Button btnSave = (Button) view.findViewById(R.id.btnYes);
        btnSave.setOnClickListener(this);
        view.findViewById(R.id.btnNo).setOnClickListener(this);

        final TextWatcher tv = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSave.setText(String.valueOf(s.length()) + "/save");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mEditText.addTextChangedListener(tv);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btnYes):

                EditNameDialogListener activity = (EditNameDialogListener) getActivity();
                activity.onFinishEditDialog(mEditTextName.getText().toString(), mEditText.getText().toString());

                this.dismiss();
                break;
            case (R.id.btnNo):
                dismiss();
                break;


        }
    }

    public String getSmsTitle() {
        return smsTitle;
    }

    public void setSmsTitle(String smsTitle) {
        this.smsTitle = smsTitle;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String smsTitle, String smsText);
    }


}
