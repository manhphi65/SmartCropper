package me.pqpo.smartcropper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.pqpo.smartcropperlib.view.CropImageView;

public class CropActivity extends AppCompatActivity {

    private static final String EXTRA_CROPPED_FILE = "extra_cropped_file";

    CropImageView ivCrop;
    Button btnCancel;
    Button btnOk;

    File mCroppedFile;

    public static Intent getJumpIntent(Context context, File croppedFile) {
        Intent intent = new Intent(context, CropActivity.class);
        intent.putExtra(EXTRA_CROPPED_FILE, croppedFile);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ivCrop = findViewById(R.id.iv_crop);
        btnCancel = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        btnOk.setOnClickListener(v -> {
            if (ivCrop.canRightCrop()) {
                Bitmap crop = ivCrop.crop();
                if (crop != null) {
                    saveImage(crop, mCroppedFile);
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            } else {
                Toast.makeText(CropActivity.this, "cannot crop correctly", Toast.LENGTH_SHORT).show();
            }
        });
        ivCrop.setImageToCrop(
                BitmapFactory.decodeResource(getResources(), R.drawable.book)
        );

        mCroppedFile = (File) getIntent().getSerializableExtra(EXTRA_CROPPED_FILE);
    }

    private void saveImage(Bitmap bitmap, File saveFile) {
        try {
            FileOutputStream fos = new FileOutputStream(saveFile, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
