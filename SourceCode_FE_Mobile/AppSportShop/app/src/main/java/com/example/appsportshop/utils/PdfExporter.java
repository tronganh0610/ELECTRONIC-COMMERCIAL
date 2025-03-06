package com.example.appsportshop.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import com.example.appsportshop.R;
import com.example.appsportshop.model.HoaDon;
import com.example.appsportshop.model.Product;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class PdfExporter {

    private static byte[] drawableToByteArray(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            return outputStream.toByteArray();
        }

        return new byte[0]; // Return an empty byte array if the Drawable is not a BitmapDrawable
    }

    public static boolean exportProductsToPdf(Context context, List<Product> productList, String thang, String nam) {
        File pdfFile = new File(Environment.getExternalStorageDirectory(), thang+nam+".pdf");
        try {
//            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN, PdfEncodings.UTF8);
            String title = "BÁO CÁO DOANH THU THÁNG "+thang +" / "+nam+"SHOP ";

            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pdfFile));
            Document document = new Document(pdfDoc);

            document.add(new Paragraph(title).setFontSize(30).setTextAlignment(TextAlignment.CENTER));


            //logo shop
            // Load the image
            // Get the drawable image from resources
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.logoshop);

            // Convert the drawable to an iText Image
            Image image = new Image(ImageDataFactory.create(drawableToByteArray(drawable)));

            // Set the image size as needed
            image.setWidth(100);
            image.setHeight(100);

            // Add the image to the PDF document
            document.add(image);

            Table table = new Table(4);
            table.setWidth(100);
            table.setFontSize(20);



            table.addCell("STT");
            table.addCell("TENSANPHAM");
            table.addCell("SOLUONG");
            table.addCell("THANHTIEN");


            float sum=0;
            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);


                table.addCell(String.valueOf(i + 1));
                table.addCell(product.getNameProduct());
                table.addCell(String.valueOf(product.getStockQuantity()));
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formattedValue = formatter.format( Double.valueOf(String.format("%.0f", product.getPrice())));
                sum+= product.getPrice();
                table.addCell(formattedValue);
            }

            document.add(table);

            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedValue = formatter.format( Double.valueOf(String.format("%.0f",sum)));

            // Add author and create date
            document.add(new Paragraph("Nguoi lap báo cáo                              Tổng Tiền: "+formattedValue +"VND" ).setFontSize(20));

            document.add(new Paragraph("Nguoi lap báo cáo                                  Ngày ... Tháng ... Nam .... \n (Ký, họ tên)" ).setFontSize(20));
//            document.add(new Paragraph("").setFontSize(12));

            document.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean exportBillOrder(Context context, List<HoaDon> productList, String fileName) {
        File pdfFile = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
//            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN, PdfEncodings.UTF8);
//


            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pdfFile));
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Hóa Don Bán Hàng").setFontSize(30).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(" Sport Shop").setFontSize(30).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("DC: HCM").setFontSize(20).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Chuyên kinh doanh các mặt hàng dụng cụ thể thao").setFontSize(20));


            //logo shop
            // Load the image
            // Get the drawable image from resources
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.logoshop);

            // Convert the drawable to an iText Image
            Image image = new Image(ImageDataFactory.create(drawableToByteArray(drawable)));

            // Set the image size as needed
            image.setWidth(100);
            image.setHeight(100);

            // Add the image to the PDF document
            document.add(image);

            Table table = new Table(4);
            table.setWidth(100);
            table.setFontSize(20);



            table.addCell("STT");
            table.addCell("TENSANPHAM");
            table.addCell("SOLUONG");
            table.addCell("THANHTIEN");


            float sum=0;
            for (int i = 0; i < productList.size(); i++) {
                HoaDon product = productList.get(i);


                table.addCell(String.valueOf(i + 1));
                table.addCell(product.getProductName());
                table.addCell(String.valueOf(product.getQuantity()));
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formattedValue = formatter.format( Double.valueOf(String.format("%.0f", product.getPrice())));
                sum+= product.getPrice();
                table.addCell(formattedValue);
            }

            document.add(table);

            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedValue = formatter.format( Double.valueOf(String.format("%.0f",sum)));

            // Add author and create date
            document.add(new Paragraph("NGƯỜI MUA HÀNG     Tổng Tiền: "+formattedValue +"VND" ).setFontSize(20));

            document.add(new Paragraph("NGƯỜI VIẾT HÓA ĐƠN            Ngày ... Tháng ... Nam .... \n (Ký, họ tên)" ).setFontSize(20));
//            document.add(new Paragraph("").setFontSize(12));

            document.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}

