package com.example.futasbus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.fragment.app.Fragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;

import com.example.futasbus.helper.MyMarkerView;
import com.example.futasbus.respone.StatisticsResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class StatisticsManagementFragment extends Fragment {
    private ImageView iconBack;
    private TextView tvTotalCustomer, tvTotalXe, tvTotalChuyenXe, tvTotalRevenue,edtStartDate, edtEndDate;
    private BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statistic_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iconBack = view.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        initViews(view);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Ngày kết thúc = hôm nay
        String endDate = dateFormat.format(calendar.getTime());

        // Ngày bắt đầu = 01-01 của năm hiện tại
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startDate = dateFormat.format(calendar.getTime());

        edtStartDate.setText(startDate);
        edtEndDate.setText(endDate);

        // Gọi API
        fetchThongKe(startDate, endDate);

        edtStartDate.setOnClickListener(v -> showDatePicker(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePicker(edtEndDate));

    }

    private void initViews(View view) {
        edtStartDate = view.findViewById(R.id.edtStartDate);
        edtEndDate = view.findViewById(R.id.edtEndDate);
        tvTotalCustomer = view.findViewById(R.id.tvTotalCustomer);
        tvTotalXe = view.findViewById(R.id.tvTotalXe);
        tvTotalChuyenXe = view.findViewById(R.id.tvTotalChuyenXe);
        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue);
        barChart = view.findViewById(R.id.barChart);
    }

    private void showDatePicker(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            String selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
            textView.setText(selectedDate);
            fetchThongKe(edtStartDate.getText().toString(), edtEndDate.getText().toString());
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void fetchThongKe(String startDate, String endDate) {
        Log.d(TAG, "fetchThongKe called with startDate=" + startDate + ", endDate=" + endDate);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getThongKe(startDate, endDate).enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                Log.d(TAG, "API onResponse: success=" + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null) {
                    StatisticsResponse data = response.body();
                    Log.d(TAG, "Received data: totalCustomer=" + data.getTotalCustomer()
                            + ", totalXe=" + data.getTotalXe()
                            + ", totalChuyenXe=" + data.getTotalChuyenXe()
                            + ", tongDoanhThuThangHienTai=" + data.getTongDoanhThuThangHienTai()
                            + ", ngayList size=" + (data.getNgayList() != null ? data.getNgayList().size() : 0)
                            + ", tongTienList size=" + (data.getTongTienList() != null ? data.getTongTienList().size() : 0)
                    );

                    if (isAdded() && getContext() != null) {
                        tvTotalCustomer.setText(String.valueOf(data.getTotalCustomer()));
                        tvTotalXe.setText(String.valueOf(data.getTotalXe()));
                        tvTotalChuyenXe.setText(String.valueOf(data.getTotalChuyenXe()));
                        tvTotalRevenue.setText(NumberFormat.getCurrencyInstance(new Locale("vi","VN")).format(data.getTongDoanhThuThangHienTai()));
                        setupBarChart(data.getNgayList(), data.getTongTienList());
                    } else {
                        Log.w(TAG, "Fragment not attached to context, skip UI update.");
                    }
                } else {
                    Log.e(TAG, "API response unsuccessful or body null. Code: " + response.code());
                    if (isAdded() && getContext() != null) {
                        Toast.makeText(requireContext(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                Log.e(TAG, "API onFailure: " + t.getMessage(), t);
                if (isAdded() && getContext() != null) {
                    Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupBarChart(List<String> labels, List<BigDecimal> values) {
        Log.d(TAG, "setupBarChart called with labels size=" + (labels != null ? labels.size() : 0)
                + " and values size=" + (values != null ? values.size() : 0));

        if (labels == null || values == null || labels.size() != values.size()) {
            Log.e(TAG, "Labels and values list size mismatch or null");
            return;
        }

        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            barEntries.add(new BarEntry(i, values.get(i).floatValue()));
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "Doanh thu theo ngày");
        dataSet.setColor(getResources().getColor(R.color.futa_orange)); // hoặc màu bạn chọn
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));
        dataSet.setDrawValues(false); // hiển thị số trên cột

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.4f); // độ rộng cột

        barChart.setData(barData);

        // Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new ArrayList<String>() {{
            for (String label : labels) {
                // Chuyển từ "2025-04-15" → "04/2025"
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
                    Date date = inputFormat.parse(label);
                    add(outputFormat.format(date));
                } catch (ParseException e) {
                    add(label); // fallback nếu lỗi
                }
            }
        }}));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(11f);
        xAxis.setLabelRotationAngle(-45f); // xoay nhãn trục X
        xAxis.setDrawGridLines(false);
        xAxis.setYOffset(10f);
        // Cấu hình trục Y
        barChart.getAxisLeft().setTextSize(11f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(true);

        // Animation mượt
        barChart.animateY(1000);
        barChart.animateX(1000);

        // Giao diện tổng thể
        barChart.getDescription().setEnabled(false);
//        barChart.getLegend().setTextSize(12f);
        barChart.getLegend().setEnabled(false);

        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setFitBars(true);
        barChart.setScaleEnabled(false); // không zoom
        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(false);
        barChart.setExtraBottomOffset(30f); // hoặc cao hơn nếu cần

        barChart.invalidate(); // vẽ lại

        MyMarkerView markerView = new MyMarkerView(getContext(), R.layout.marker_view, labels, values);
        markerView.setChartView(barChart);
        barChart.setMarker(markerView);

    }



}

