package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class barcodeView extends AppCompatActivity {
    private ImageView imageView;
    public String expNameAndCategory;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_view);

        Intent intent = getIntent();
        expNameAndCategory = intent.getStringExtra("exp");

        textView = findViewById(R.id.textViewTest);
        textView.setText(expNameAndCategory);

        imageView = findViewById(R.id.barcodeImage);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(expNameAndCategory, BarcodeFormat.CODE_128,352,52);
            Bitmap bitmap = Bitmap.createBitmap(352,52,Bitmap.Config.RGB_565);
            for (int i = 0; i < 352;i++){
                for (int j = 0; j < 52;j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}