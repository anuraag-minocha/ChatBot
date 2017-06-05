package com.hodlife.android.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by test01 on 09-May-17.
 */
@DatabaseTable(tableName = "chat_table")
public class Chat implements Serializable {


    @DatabaseField
    String userId="";
    @DatabaseField
    String message="";
    @DatabaseField
    String time="";

    public String getUserId(){
        return  userId;
    }
    public String getMessage(){
        return  message;
    }
    public String getTime(){
        return  time;
    }

    public Chat() {
        // ORMLite needs a no-arg constructor
    }
    public Chat(String id, String msg, String time) {
        this.userId = id;
        this.message = msg;
        this.time = time;
    }

}
