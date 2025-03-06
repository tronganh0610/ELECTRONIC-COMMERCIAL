package com.example.appsportshop.fragment.Admin;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.UserAPI;
import com.example.appsportshop.model.Product;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.ExportPDF;
import com.example.appsportshop.utils.PdfExporter;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragRevenue extends Fragment {
    ArrayList<Product> ProductList  ;

    ArrayList<String> ProductBestSell;

    SingletonUser singletonUser = SingletonUser.getInstance();
    Spinner thang, nam;
    ImageView searchRevenue;
    TextView nam_thang, slSanPham;
    PieChart pieChartOne, pieChartTwo, pieChartThree, pieChartFour;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    RelativeLayout exportPDF;
    String nameShop = "Nho Nam";
    Double doanhthu = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_chart, container, false);
        mapping(view);
        //lấy dữ liệu đc gửi từ trang ManagerShop

        setAdapter();

        try {
            LoadData();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }




    private void setAdapter() {
        List<String> thangList = new ArrayList<String>();
        thangList.add("Tháng 1");
        thangList.add("Tháng 2");
        thangList.add("Tháng 3");
        thangList.add("Tháng 4");
        thangList.add("Tháng 5");
        thangList.add("Tháng 6");
        thangList.add("Tháng 7");
        thangList.add("Tháng 8");
        thangList.add("Tháng 9");
        thangList.add("Tháng 10");
        thangList.add("Tháng 11");
        thangList.add("Tháng 12");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, thangList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        thang.setAdapter(dataAdapter);

        List<String> namList = new ArrayList<String>();
        namList.add("2024");
        namList.add("2023");
        namList.add("2019");
        namList.add("2020");
        namList.add("2021");
        namList.add("2022");


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, namList);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        nam.setAdapter(dataAdapter1);

    }

    //piechart
    private void setUpPieChart() {

        Description description1 = new Description();
        description1.setText(ProductBestSell.get(0));
        description1.setTextSize(16);

        pieChartOne.setRotationEnabled(true);
        pieChartOne.setDescription(description1);
        pieChartOne.setHoleRadius(35f);
        pieChartOne.setTransparentCircleAlpha(0);
        pieChartOne.setCenterTextSize(10);

        Description description2= new Description();
        description2.setText(ProductBestSell.get(1));
        description2.setTextSize(16);
        pieChartTwo.setRotationEnabled(true);
        pieChartTwo.setDescription(description2);
        pieChartTwo.setHoleRadius(35f);
        pieChartTwo.setTransparentCircleAlpha(0);
        pieChartTwo.setCenterTextSize(10);

        Description description3 = new Description();
        description3.setText(ProductBestSell.get(2));
        description3.setTextSize(16);
        pieChartThree.setRotationEnabled(true);
        pieChartThree.setDescription(description3);
        pieChartThree.setHoleRadius(35f);
        pieChartThree.setTransparentCircleAlpha(0);
        pieChartThree.setCenterTextSize(10);

        Description description4 = new Description();
        description4.setText(ProductBestSell.get(3));
        description4.setTextSize(16);
        pieChartFour.setRotationEnabled(true);
        pieChartFour.setDescription(description4);
        pieChartFour.setHoleRadius(35f);
        pieChartFour.setTransparentCircleAlpha(0);
        pieChartFour.setCenterTextSize(10);

        addDataSetOne(pieChartOne);
        addDataSetTwo(pieChartTwo);
        addDataSetThree(pieChartThree);
        addDataSetFour(pieChartFour);
    }

    private static void addDataSetOne(PieChart pieChart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        float[] yData = {30, 70};
        String[] xData = {"Áo", "2"};

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }
        for (int i = 0; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);


        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private static void addDataSetTwo(PieChart pieChart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        float[] yData = {25, 80};
        String[] xData = {"Áo", "2"};

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }
        for (int i = 0; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);


        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private static void addDataSetThree(PieChart pieChart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        float[] yData = {20, 80};
        String[] xData = {"Áo", "2"};

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }
        for (int i = 0; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);


        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private static void addDataSetFour(PieChart pieChart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        float[] yData = {15, 75};
        String[] xData = {"Áo", "2"};

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }
        for (int i = 0; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);


        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    //end piechart


    private void setUpLineChart() throws JSONException {

        UserAPI.ApiGet(getContext(), Utils.BASE_URL + "order/data-chart/"+nam.getSelectedItem(), new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                System.out.println(response.getString("data"));
                List<Integer> integerList = new ArrayList<>();
                String input = response.getString("data");
                String[] parts = input.split(",");

                for (String part : parts) {
                    try {
                        int number = Integer.parseInt(part);
                        integerList.add(number);
                    } catch (NumberFormatException e) {
                        // Handle the case where parsing to an integer fails
                        // For example, if the part is not a valid number
                    }
                }

                getEntriesLineChart(integerList);
                lineDataSet = new LineDataSet(lineEntries, "");
                lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);
                lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                lineDataSet.setValueTextColor(Color.BLACK);
                lineDataSet.setValueTextSize(10f);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });


    }

    private void getEntriesLineChart(List<Integer> integerList) {

//        List<Integer> list = new ArrayList<Integer>();
//        for (int i = 1; i <= 12; i++) {
//            list.add(i);
//        }
//        Collections.shuffle(list);
//        System.out.println(list);

        lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(1, integerList.get(0)));
        lineEntries.add(new Entry(2f, integerList.get(1)));
        lineEntries.add(new Entry(3f, integerList.get(2)));
        lineEntries.add(new Entry(4f, integerList.get(3)));
        lineEntries.add(new Entry(5f, integerList.get(4)));
        lineEntries.add(new Entry(6f, integerList.get(5)));
        lineEntries.add(new Entry(7f, integerList.get(6)));
        lineEntries.add(new Entry(8, integerList.get(7)));
        lineEntries.add(new Entry(9f, integerList.get(8)));
        lineEntries.add(new Entry(10f, integerList.get(9)));
        lineEntries.add(new Entry(11f, integerList.get(10)));
        lineEntries.add(new Entry(12f, integerList.get(11)));
    }


    private void setEvent() {
        exportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PdfExporter.exportProductsToPdf(getContext(),ProductList ,thang.getSelectedItem().toString(),nam.getSelectedItem().toString());
//                ExportPDF exportPDF = new ExportPDF();
//                exportPDF.main(getContext(), thang.getSelectedItem().toString(),  nam.getSelectedItem().toString(), " SHOP",ProductList );
                CustomToast.makeText(getContext(), "Xuất báo cáo thành công, vui lòng vào Tệp để xem báo cáo !", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();

            }
        });

        slSanPham.setText(String.valueOf(doanhthu));

        nam_thang.setText(thang.getSelectedItem() + " " + "Năm " + nam.getSelectedItem());
        searchRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nam_thang.setText(thang.getSelectedItem() + " " + "Năm " + nam.getSelectedItem());
                try {
                    LoadData();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void mapping(View view) {
        lineChart = view.findViewById(R.id.lineChart);
        pieChartOne = view.findViewById(R.id.piechartOne);
        pieChartTwo = view.findViewById(R.id.piechartTwo);
        pieChartThree = view.findViewById(R.id.piechartThree);
        pieChartFour = view.findViewById(R.id.piechartFour);
        exportPDF = view.findViewById(R.id.exportPDF);
        thang = view.findViewById(R.id.thang);
        nam = view.findViewById(R.id.nam);
        nam_thang = view.findViewById(R.id.nam_thang);
        slSanPham = view.findViewById(R.id.spCount);
//        top1 = view.findViewById(R.id.topone);
//        top2 = view.findViewById(R.id.toptwo);
//        top3 = view.findViewById(R.id.topthree);
//        top4 = view.findViewById(R.id.topfour);
        searchRevenue = view.findViewById(R.id.search_revenue);


    }

    private  void LoadData() throws JSONException {
        //get số lượng sản phẩm đã bán được
        UserAPI.ApiGet(getContext(), Utils.BASE_URL + "order/best-sell/"+thang.getSelectedItem().toString().substring(6)+"/"+nam.getSelectedItem(), new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                JSONArray jsonArray = response.getJSONArray("data");
                JSONObject jsonObject ;
                int sum =0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);
                    sum+=jsonObject.getInt("quantity");
                }

                slSanPham.setText(String.valueOf(sum));
            }

            @Override
            public void onError(VolleyError error) {

            }
        });




        //get top 4 sản phẩm bán chạy nhất
        ProductBestSell = new ArrayList<>();
        UserAPI.ApiGet(getContext(), Utils.BASE_URL + "order/best-sell/"+thang.getSelectedItem().toString().substring(6)+"/"+nam.getSelectedItem(), new APICallBack() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                JSONArray jsonArray = response.getJSONArray("data");
                JSONObject jsonObject = new JSONObject();
                ProductList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject = (JSONObject) jsonArray.get(i);
                    if (ProductBestSell.size() < 5 )
                    {
                        ProductBestSell.add(jsonObject.getString("product_name"));
                    }
                    Product product = new Product();
                    product.setNameProduct(jsonObject.getString("product_name"));
                    product.setStockQuantity(jsonObject.getInt("quantity"));
                    product.setPrice((float) jsonObject.getDouble("price"));
                    ProductList.add(product);


                }

                if (ProductBestSell.size()<4)
                for (int i = 0; i < 4; i++) {
                        ProductBestSell.add("");
                }


                setUpLineChart();
                setUpPieChart();
                setEvent();

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }


}

