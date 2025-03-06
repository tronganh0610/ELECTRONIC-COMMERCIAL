package com.example.appsportshop.utils;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.appsportshop.R;
import com.example.appsportshop.model.Order;
import com.example.appsportshop.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportPDF extends Activity {


    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    private static final int PERMISSION_REQUEST_CODE = 200;


    public void main(Context context, String month, String year, String shopName, ArrayList<Product> productList){
        // initializing our variables.
//        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        Resources res = context.getResources();
        int id = R.drawable.img;
        bmp = BitmapFactory.decodeResource(res, R.drawable.img);

        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        generatePDF(month,year,  shopName, productList);
    }

    public void hoaDon(Context context, String fileName, String shopName, ArrayList<Order> productList){
        // initializing our variables.
//        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        Resources res = context.getResources();
        int id = R.drawable.img;
        bmp = BitmapFactory.decodeResource(res, R.drawable.img);

        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        generateHoaDon(fileName, shopName, productList);
    }

    private void generateHoaDon(String fileName, String shopName, ArrayList<Order> productList) {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
//        title.setColor(ContextCompat.getColor(this, R.color.purple_200));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("Doanh Thu Của Shop "+ shopName, 209, 100, title);
        canvas.drawText("SHOP SPORT", 209, 80, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        title.setColor(ContextCompat.getColor(ContextCompat.getSystemService(null), R.color.purple_200));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("This is sample document which we have created.", 396, 560, title);





//ở đây

// Vẽ các dòng của bảng
//        int startY=300;
//        int tong=0;
//        for (int i = 0; i < productList.size(); i++) {
//            String productId = productList.get(i).getId();
//            String productName = productList.get(i).getName_ceciver();
//            Float productQuantity = productList.get(i).getTotalAmount();
//            tong+=productQuantity;
//
//            // Vẽ các cột của bảng
//            canvas.drawLine(200, startY+5, 800, startY+5, paint);
//            canvas.drawText(String.valueOf(productId), 200, startY, paint);
//            canvas.drawText(String.valueOf(productName), 400, startY, paint);
//            canvas.drawText(String.valueOf(productQuantity), 600, startY, paint);
//
//            startY+=30;
//        }

        // dừng










        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStorageDirectory(), fileName+".pdf");
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),  fileName+".pdf");
        System.out.println("thanh cong PDF"+file);
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
//            Toast.makeText(ExportPDF.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();

    }


    private void generatePDF(String month, String year, String shopName, ArrayList<Product> productList) {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(40);


        // below line is sued for setting color
        // of our text inside our PDF file.
//        title.setColor(ContextCompat.getColor(this, R.color.purple_200));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
//        canvas.drawText("Doanh Thu Của Shop "+ shopName, 209, 100, title);
        canvas.drawText("Báo cáo Doanh thu của Shop tháng "+month+" năm "+year, 209, 80, title);

//

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        title.setColor(ContextCompat.getColor(ContextCompat.getSystemService(null), R.color.purple_200));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
//        canvas.drawText("This is sample document which we have created.", 396, 560, title);



// Vẽ các dòng của bảng
//        canvas.drawText("ID", 400, 290, paint);
//        canvas.drawText("Tên Sản Phẩm", 530, 290, paint);
//        canvas.drawText("Số lượng", 710, 290, paint);
//        canvas.drawText("Thành tiền", 830, 290, paint);

//                productList = new ArrayList<>();
//        System.out.println(PDF.size());




        int startY = 400;
        int startX = 50;
        int rowHeight = 70;
        int columnWidth = 170;

//        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        List<String> headers = new ArrayList<>();
        headers.add("STT");
        headers.add("Product Name");
        headers.add("Quantity");
        headers.add("Price");
        // Draw header row

        canvas.drawLine(startX, startY , startX*70*4, startY, paint);
        canvas.drawLine(startX*70*4, startY , startX*70*4, startY*productList.size(), paint);

        for (int i = 0; i < headers.size(); i++) {
            canvas.drawText(headers.get(i) , startX+20 + i * columnWidth, startY-20, paint);
            canvas.drawLine(startX  + i * columnWidth, startY - rowHeight, startX + i * columnWidth, startY, paint);
        }
//        canvas.drawLine(startX  + 5 * columnWidth, startY - rowHeight, startX + 5 * columnWidth, startY, paint);


        // Draw data rows
        for (int i = 0; i < productList.size(); i++) {
            Product rowData = productList.get(i);
            startY += rowHeight;
            canvas.drawText(String.valueOf(i+1), startX +20 + 0 * columnWidth, startY-20, paint);

            for (int j = 0; j < 4; j++) {
//                canvas.drawText(String.valueOf(j+1), startX + 0 * columnWidth, startY, paint);
                canvas.drawText(rowData.getNameProduct(), startX +20 + 1 * columnWidth, startY-20, paint);
                canvas.drawText(String.valueOf(rowData.getStockQuantity()), startX+20 + 2 * columnWidth, startY-20, paint);
                canvas.drawText(String.valueOf(rowData.getPrice()), startX +20 + 3 * columnWidth, startY-20, paint);
                canvas.drawLine(startX + j * columnWidth, startY - rowHeight, startX + j * columnWidth, startY, paint);
            }


            canvas.drawLine(startX, startY, startX + headers.size() * columnWidth, startY, paint);


        }






//        int startY=300;
//        int tong=0;
//        for (int i = 0; i < productList.size(); i++) {
//            long stt = i+1;
//            String productName = productList.get(i).getNameProduct();
//            int productQuantity = productList.get(i).getStockQuantity();
//            float price = productList.get(i).getPrice();
//            tong+=productQuantity;
//
//            // Vẽ các cột của bảng
//            canvas.drawLine(200, startY+5, 800, startY+5, paint);
//            canvas.drawText(String.valueOf(stt), 200, startY, paint);
//            canvas.drawText(String.valueOf(productName), 400, startY, paint);
//            canvas.drawText(String.valueOf(productQuantity), 600, startY, paint);
//            canvas.drawText(String.valueOf(price), 800, startY, paint);
//
//
//            startY+=30;
//        }
//        canvas.drawText(String.valueOf("Tổng tiền: "+tong), 1000, startY, paint);
        // after adding all attributes to our
        // PDF file we will be finishing our page.















        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStorageDirectory(), "BaoCao"+month.substring(6)+"/"+year+".pdf");
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),  fileName+".pdf");
        System.out.println("thanh cong PDF"+file);
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
//            Toast.makeText(ExportPDF.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();

    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}

