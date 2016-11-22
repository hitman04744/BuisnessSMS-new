package com.example.bohdan.sms_sender.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bohdan.sms_sender.R;
import com.example.bohdan.sms_sender.activities.EditableSMSListActivity;
import com.example.bohdan.sms_sender.data.SMS;
import com.example.bohdan.sms_sender.dialogs.DialogFragment;
import com.example.bohdan.sms_sender.dialogs.PreviewDialogFragment;

import java.util.List;

/**
 * Created by User on 08-Jun-16.
 */
public class SmsListAdapter extends RecyclerView.Adapter<SmsListAdapter.SmsViewHolder> {
    private List<SMS> list;
    private Context context;


    public SmsListAdapter(List<SMS> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public SmsListAdapter.SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_editable_template, parent, false);
        SmsViewHolder smsVh = new SmsViewHolder(v);
        return smsVh;
    }

    @Override
    public void onBindViewHolder(SmsListAdapter.SmsViewHolder holder, int position) {
//        if (position <5) {
//            holder.deleteButton.setEnabled(false);
//        }
//        if (position == list.size()) {
        boolean state = list.get(position).isActive();
        System.out.println(position + " - " + state);
        holder.used.setChecked(state);
        holder.smsTitle.setText(list.get(position).getName());
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SmsViewHolder extends RecyclerView.ViewHolder {
        private TextView smsTitle;
        private SharedPreferences sPref;
        private ImageButton editButton;
        private ImageButton previewButton;
        private ImageButton deleteButton;
        private CheckBox used;
        private List<SMS> sms;

        public SmsViewHolder(View itemView) {
            super(itemView);
            sPref = context.getSharedPreferences("prefs", context.MODE_PRIVATE);
            smsTitle = (TextView) itemView.findViewById(R.id.SMS_Title);
            used = (CheckBox) itemView.findViewById(R.id.checkBoxUsed);
            editButton = (ImageButton) itemView.findViewById(R.id.button_Edit);
            previewButton = (ImageButton) itemView.findViewById(R.id.button_Preview);
            deleteButton = (ImageButton) itemView.findViewById(R.id.button_Delete);
            sms = SMS.listAll(SMS.class);
            used.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == true) {
                        list.get(getAdapterPosition()).setActive(true);
                        list.get(getAdapterPosition()).save();
                    } else {
                        list.get(getAdapterPosition()).setActive(false);
                        list.get(getAdapterPosition()).save();
                        buttonView.setChecked(false);
                    }
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditableSMSListActivity.editState = true;
                    String smsName = smsTitle.getText().toString();
                    EditableSMSListActivity.editedNAme = smsName;
                    EditableSMSListActivity.editPosition = getAdapterPosition();
                    List<SMS> sms = SMS.find(SMS.class, "name = ? ", smsName);
                    String TextSms = sms.get(0).getText();
                    DialogFragment mDialog = new DialogFragment();
                    mDialog.setSmsText(TextSms);
                    mDialog.setSmsTitle(smsName);
                    mDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "dialog");
                }
            });
            previewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String smsName = smsTitle.getText().toString();
                    List<SMS> sms = SMS.find(SMS.class, "name = ? ", smsName);
                    String TextSms = sms.get(0).getText();
                    PreviewDialogFragment mPreviewDialog = new PreviewDialogFragment();
                    mPreviewDialog.setText(TextSms);
                    mPreviewDialog.setTitle(smsName);
                    mPreviewDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "previewDialog");
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() > 1) {
//                    List<SMS> sms = SMS.find(SMS.class, "name = ? ", list.get(getAdapterPosition()).getName());
                        List<SMS> sms = SMS.find(SMS.class, "name = ? ", smsTitle.getText().toString());
//                    List<SMSsend> smsSend =  SMSsend.find(SMSsend.class, "name = ? ",list.get(getAdapterPosition()).getName());
                        list.remove(getAdapterPosition());
                        sms.get(0).delete();
                        List<SMS> logSMS = SMS.listAll(SMS.class);
                        for (int i = 0; i < logSMS.size(); i++) {
                            System.out.println(logSMS.get(i).getName());
                        }

                        notifyItemRemoved(getAdapterPosition());
                        System.out.println("Число: " + list.size());

                    } else {
                        notifyItemRemoved(getAdapterPosition());
                        SMS.deleteAll(SMS.class);
                        sms.clear();
                        list.clear();
                    }


                }
            });


        }
    }
}
