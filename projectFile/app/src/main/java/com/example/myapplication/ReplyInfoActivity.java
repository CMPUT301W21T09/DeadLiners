package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReplyInfoActivity extends AppCompatActivity {
    private QuestionOrReply reply;
    private TextView description;
    private TextView publisher;
    private TextView time;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replyinfo);

        Intent intent = getIntent();
        reply = (QuestionOrReply) intent.getSerializableExtra("Reply");

        description = findViewById(R.id.reply_content) ;
        publisher = findViewById(R.id.reply_publisher_content);
        time = findViewById(R.id.reply_time_content);

        description.setText(reply.getDescription());
        publisher.setText(reply.getPublisher_uid());
        time.setText(reply.getTime().toString());

        FloatingActionButton back=findViewById(R.id.replyinfo_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ finish(); }
        });
    }
}
