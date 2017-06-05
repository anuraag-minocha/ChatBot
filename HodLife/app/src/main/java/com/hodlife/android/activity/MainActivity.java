package com.hodlife.android.activity;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.hodlife.android.R;
import com.hodlife.android.database.DatabaseHelper;
import com.hodlife.android.model.Chat;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etMessage;
    ImageView ivSend;
    LinearLayout llContainer;
    private DatabaseHelper databaseHelper = null;
    RuntimeExceptionDao<Chat, Integer> simpleDao = null;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMessage = (EditText) findViewById(R.id.et_msg);
        ivSend = (ImageView) findViewById(R.id.iv_send);
        llContainer = (LinearLayout) findViewById(R.id.ll);
        scrollView = (ScrollView) findViewById(R.id.sv);




        simpleDao = getHelper().getSimpleDataDao();
        // query for all of the data objects in the database
        List<Chat> list = simpleDao.queryForAll();

        if(!list.isEmpty()) {
            for(int i=0;i<list.size(); i++){
                if(i%2==0) {
                    View childView = getLayoutInflater().inflate(R.layout.layout_bubble, null, false);
                    final TextView tvMsg = (TextView) childView.findViewById(R.id.tv_msg);
                    final TextView tvTime = (TextView) childView.findViewById(R.id.tv_time);
                    tvMsg.setText(list.get(i).getMessage());
                    tvTime.setText(list.get(i).getTime());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.END;
                    ((LinearLayout) tvMsg.getParent().getParent()).setLayoutParams(params);
                    llContainer.addView(childView);
                }
                else {

                    View childView2 = getLayoutInflater().inflate(R.layout.layout_bubble_white, null, false);
                    final TextView tvMsg2 = (TextView) childView2.findViewById(R.id.tv_msg);
                    final TextView tvTime2 = (TextView) childView2.findViewById(R.id.tv_time);
                    tvMsg2.setText(list.get(i).getMessage());
                    tvTime2.setText(list.get(i).getTime());
                    llContainer.addView(childView2);
                }

            }
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etMessage.getText().toString().trim().equalsIgnoreCase("")){
                    View childView = getLayoutInflater().inflate(R.layout.layout_bubble, null, false);
                    final TextView tvMsg = (TextView) childView.findViewById(R.id.tv_msg);
                    final TextView tvTime = (TextView) childView.findViewById(R.id.tv_time);
                    tvMsg.setText(Html.fromHtml(etMessage.getText().toString() + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
                    Calendar c = Calendar.getInstance();
                    DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    tvTime.setText(dateFormat.format(c.getTime()).toUpperCase());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.END;
                    ((LinearLayout)tvMsg.getParent().getParent()).setLayoutParams(params);
                    llContainer.addView(childView);
                    etMessage.setText("");

                    View childView2 = getLayoutInflater().inflate(R.layout.layout_bubble_white, null, false);
                    final TextView tvMsg2 = (TextView) childView2.findViewById(R.id.tv_msg);
                    final TextView tvTime2 = (TextView) childView2.findViewById(R.id.tv_time);
                    if(llContainer.getChildCount()%3==0)
                    tvMsg2.setText("I am good");
                    else if(llContainer.getChildCount()%3==1)
                        tvMsg2.setText("How are you?");
                    else
                        tvMsg2.setText("When is your birthday?");
                    Calendar c2 = Calendar.getInstance();
                    DateFormat dateFormat2 = new SimpleDateFormat("hh:mm a");
                    tvTime2.setText(dateFormat2.format(c2.getTime()).toUpperCase());
                    llContainer.addView(childView2);

                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                    simpleDao.create(new Chat("1",tvMsg.getText().toString(),tvTime.getText().toString()));
                    simpleDao.create(new Chat("2",tvMsg2.getText().toString(),tvTime2.getText().toString()));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                if(simpleDao!=null){
                    try {
                        TableUtils.clearTable(simpleDao.getConnectionSource(), Chat.class);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    llContainer.removeAllViews();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    }
