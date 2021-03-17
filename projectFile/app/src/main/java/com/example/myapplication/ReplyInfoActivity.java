package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReplyInfoActivity extends AppCompatActivity {
    private Reply reply;
    private TextView description;
    private TextView publisher;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replyinfo);

        Intent intent = getIntent();
        reply = (Reply) intent.getSerializableExtra("Reply");

        description = findViewById(R.id.reply_content) ;
        publisher = findViewById(R.id.reply_publisher_content);
        time = findViewById(R.id.reply_time_content);

        description.setText(reply.getDescription());
        publisher.setText(reply.getPublisher());
        time.setText(reply.getTime().toString());

    }
}
