package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner categorySpinner;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText inputValue;
    private Button convertBtn;
    private TextView resultText;

    private final String[] categories = {"Length", "Weight", "Temperature"};

    private final String[] lengthUnits = {"mm", "cm", "m", "km", "inch", "feet", "mile", "yard"};
    private final String[] weightUnits = {"mg", "g", "kg", "lb", "oz"};
    private final String[] tempUnits   = {"Celsius", "Fahrenheit", "Kelvin"};

    // Length: factor to convert 1 unit → meters
    private final Map<String, Double> lengthToMeters = new HashMap<String, Double>() {{
        put("mm",   0.001);
        put("cm",   0.01);
        put("m",    1.0);
        put("km",   1000.0);
        put("inch", 0.0254);
        put("feet", 0.3048);
        put("mile", 1609.344);
        put("yard", 0.9144);
    }};

    // Weight: factor to convert 1 unit → grams
    private final Map<String, Double> weightToGrams = new HashMap<String, Double>() {{
        put("mg", 0.001);
        put("g",  1.0);
        put("kg", 1000.0);
        put("lb", 453.59237);
        put("oz", 28.349523125);
    }};

    private boolean isUserAction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categorySpinner = findViewById(R.id.categorySpinner);
        fromSpinner     = findViewById(R.id.fromSpinner);
        toSpinner       = findViewById(R.id.toSpinner);
        inputValue      = findViewById(R.id.inputValue);
        convertBtn      = findViewById(R.id.convertBtn);
        resultText      = findViewById(R.id.resultText);

        setupCategorySpinner();

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConvertClick();
            }
        });
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(this);

        // Populate unit spinners for the default (first) category
        onCategoryChange(0);
    }

    private void populateUnitSpinner(Spinner spinner, String[] units) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void onCategoryChange(int pos) {
        resultText.setText("");
        inputValue.setText("");
        switch (pos) {
            case 0: // Length
                populateUnitSpinner(fromSpinner, lengthUnits);
                populateUnitSpinner(toSpinner,   lengthUnits);
                break;
            case 1: // Weight
                populateUnitSpinner(fromSpinner, weightUnits);
                populateUnitSpinner(toSpinner,   weightUnits);
                break;
            case 2: // Temperature
                populateUnitSpinner(fromSpinner, tempUnits);
                populateUnitSpinner(toSpinner,   tempUnits);
                break;
            default:
                break;
        }
    }

    private void onConvertClick() {
        String raw = inputValue.getText().toString().trim();

        if (raw.isEmpty()) {
            showToast("Please enter a value to convert.");
            return;
        }

        double value;
        try {
            value = Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            showToast("Invalid number. Please enter a numeric value.");
            return;
        }

        String fromUnit    = fromSpinner.getSelectedItem().toString();
        String toUnit      = toSpinner.getSelectedItem().toString();
        String category    = categorySpinner.getSelectedItem().toString();

        double result = convert(value, fromUnit, toUnit, category);

        // Format result: show up to 6 significant decimal places, strip trailing zeros
        String formatted = formatResult(result);
        resultText.setText(formatted + " " + toUnit);
    }

    private String formatResult(double result) {
        if (result == Math.floor(result) && !Double.isInfinite(result)) {
            return String.valueOf((long) result);
        }
        // 6 decimal places, strip trailing zeros
        String s = String.format("%.6f", result);
        s = s.replaceAll("0+$", "");
        s = s.replaceAll("\\.$", "");
        return s;
    }

    private double convert(double val, String from, String to, String category) {
        switch (category) {
            case "Length":
                return convertLength(val, from, to);
            case "Weight":
                return convertWeight(val, from, to);
            case "Temperature":
                return convertTemperature(val, from, to);
            default:
                return val;
        }
    }

    private double convertLength(double val, String from, String to) {
        if (from.equals(to)) return val;
        Double fromFactor = lengthToMeters.get(from);
        Double toFactor   = lengthToMeters.get(to);
        if (fromFactor == null || toFactor == null) return val;
        double meters = val * fromFactor;
        return meters / toFactor;
    }

    private double convertWeight(double val, String from, String to) {
        if (from.equals(to)) return val;
        Double fromFactor = weightToGrams.get(from);
        Double toFactor   = weightToGrams.get(to);
        if (fromFactor == null || toFactor == null) return val;
        double grams = val * fromFactor;
        return grams / toFactor;
    }

    private double convertTemperature(double val, String from, String to) {
        if (from.equals(to)) return val;

        // Convert from → Celsius first
        double celsius;
        switch (from) {
            case "Celsius":
                celsius = val;
                break;
            case "Fahrenheit":
                celsius = (val - 32.0) * 5.0 / 9.0;
                break;
            case "Kelvin":
                celsius = val - 273.15;
                break;
            default:
                celsius = val;
                break;
        }

        // Convert Celsius → target
        switch (to) {
            case "Celsius":
                return celsius;
            case "Fahrenheit":
                return celsius * 9.0 / 5.0 + 32.0;
            case "Kelvin":
                return celsius + 273.15;
            default:
                return celsius;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // ── AdapterView.OnItemSelectedListener ──────────────────────────────────

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.categorySpinner) {
            onCategoryChange(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No-op
    }
}
