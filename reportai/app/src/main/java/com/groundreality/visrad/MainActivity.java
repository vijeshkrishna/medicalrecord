package com.groundreality.visrad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class MainActivity extends AppCompatActivity {

    TextView tv,textblocks;
    Bitmap bitmap;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.badslide);

//        TextRecognizer recog = new TextRecognizer.Builder(getApplicationContext()).build();
//
//        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//
//        SparseArray<TextBlock> items = recog.detect(frame);
//
        tv = findViewById(R.id.textv);

        textblocks = findViewById(R.id.textblocks);
//        StringBuilder builder = new StringBuilder();
//
//        for(int i =0;i<items.size();i++){
//
//            TextBlock txt= items.valueAt(i);
//            builder.append(txt.getValue());
//            builder.append(" ");
//
//
//        }
//
//        tv.setText(builder+"");





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 786);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 786) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            runImageTextProcessor(image);

        }
    }
    FirebaseVisionImage image;
    FirebaseVisionTextRecognizer detector;
    void runImageTextProcessor(Bitmap fromcamera){
        image = FirebaseVisionImage.fromBitmap(fromcamera);

        detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText)
            {

                ProcessTextRecognition(firebaseVisionText);
            }


        });
    }


    void ProcessTextRecognition(FirebaseVisionText firebaseVisionText){
        tv.setText(firebaseVisionText.getText());

        textblocks.setText("Blocks" +firebaseVisionText.getTextBlocks().size());

        StringBuilder builder = new StringBuilder();


        for(int j=0;j<firebaseVisionText.getTextBlocks().size();j++){
            builder.append("Blocks"+j+" " +firebaseVisionText.getTextBlocks().get(j).getText());
        }
        textblocks.setText(builder+"");

    }

}
