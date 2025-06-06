package com.example.futasbus.helper;

import android.content.Context;
import android.widget.TextView;

import com.example.futasbus.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MyMarkerView extends MarkerView {
    private final TextView tvContent;
    private final List<String> labels;
    private final List<BigDecimal> values;

    public MyMarkerView(Context context, int layoutResource, List<String> labels, List<BigDecimal> values) {
        super(context, layoutResource);
        this.labels = labels;
        this.values = values;
        tvContent = findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = (int) e.getX();
        if (index >= 0 && index < labels.size()) {
            String date = labels.get(index);
            String amount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(values.get(index));
            tvContent.setText("Ngày: " + date + "\nTổng: " + amount);
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}
