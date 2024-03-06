package com.moutimid.tinder;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moutamid.tinder.R;

public class PdfViewerActivity extends AppCompatActivity {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        String pdfUrl = "https://firebasestorage.googleapis.com/v0/b/childfr-35a43.appspot.com/o/TinderEmployeeApp%2FUsers%2Fpdfs%2F1709615266995.pdf?alt=media&token=cb75f9d0-5a42-420f-88e2-87b53dcbe0ee";

        openPdfWithExternalViewer(pdfUrl);
    }

    private void openPdfWithExternalViewer(String pdfUrl) {
        try {
            Uri uri = Uri.parse(pdfUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // If no PDF viewer app is installed, inform the user to install one
            Toast.makeText(this, "No PDF viewer app installed", Toast.LENGTH_SHORT).show();
        }
    }}
