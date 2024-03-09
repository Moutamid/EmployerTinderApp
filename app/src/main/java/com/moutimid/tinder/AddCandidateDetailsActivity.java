package com.moutimid.tinder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moutamid.tinder.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddCandidateDetailsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_PDF_REQUEST = 2;

    private ImageView upload_pic, upload_resume;
    private Button submit;
    private Uri imageUri, pdfUri;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate_details);

        upload_pic = findViewById(R.id.upload_pic);
        upload_resume = findViewById(R.id.upload_resume);
        submit = findViewById(R.id.submit);

        databaseReference = FirebaseDatabase.getInstance().getReference("TinderEmployeeApp").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference("TinderEmployeeApp").child("Users");

        upload_pic.setOnClickListener(v -> openImageChooser());

        upload_resume.setOnClickListener(v -> openPdfChooser());

        submit.setOnClickListener(v -> uploadData());


    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void openPdfChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            upload_pic.setImageURI(imageUri);
        }

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            upload_resume.setImageResource(R.drawable.pdf);

        }
    }

    private void uploadData() {
        if (imageUri != null && pdfUri != null) {
            Dialog loadingDialog = new Dialog(AddCandidateDetailsActivity.this);
            loadingDialog.setContentView(R.layout.loading);
            Objects.requireNonNull(loadingDialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
            loadingDialog.show();

            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
            StorageReference pdfRef = storageReference.child("pdfs/" + System.currentTimeMillis() + ".pdf");

            Task<Uri> imageTask = imageRef.putFile(imageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return imageRef.getDownloadUrl();
            });

            Task<Uri> pdfTask = pdfRef.putFile(pdfUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return pdfRef.getDownloadUrl();
            });

            Tasks.whenAllComplete(imageTask, pdfTask).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String imageUrl = Objects.requireNonNull(imageTask.getResult()).toString();
                    String pdfUrl = Objects.requireNonNull(pdfTask.getResult()).toString();

                    Map<String, Object> map = new HashMap<>();
                    map.put("profile_img", imageUrl);
                    map.put("pdfUrl", pdfUrl);

                    Stash.put("cand_img",imageUrl );
                    Stash.put("pdfUrl",pdfUrl );

                    databaseReference.updateChildren(map).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            loadingDialog.dismiss();
                            startActivity(new Intent(AddCandidateDetailsActivity.this, HomePage.class));
                            finish();
                        } else {
                            // Handle update failure
                            Toast.makeText(AddCandidateDetailsActivity.this, "Failed to update database.", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });
                } else {
                    // Handle failure in uploading either image or PDF
                    Toast.makeText(AddCandidateDetailsActivity.this, "Failed to upload image or PDF.", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "Please select profile image and resume", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view) {
        onBackPressed();
    }
}
