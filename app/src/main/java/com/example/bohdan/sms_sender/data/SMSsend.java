package com.example.bohdan.sms_sender.data;

import com.orm.SugarRecord;

/**
 * Created by User on 08-Jun-16.
 * Класс для хранения  Шаблонов СМС для ОТПРАВКИ
 */
public class SMSsend extends SugarRecord {
    private String name;
    private String text;
    private boolean active;

    public SMSsend() {
    }

    public SMSsend(String name, String text, boolean active) {
        this.name = name;
        this.text = text;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
