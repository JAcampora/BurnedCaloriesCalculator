package com.acampora.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityBurnedCaloriesCalculator extends AppCompatActivity {

    private EditText weightEditText;
    private TextView milesAmountLabel;
    private SeekBar milesRanSeekBar;
    private TextView caloriesBurnedTextView;
    private Spinner heightFeetSpinner;
    private Spinner heightInchesSpinner;
    private TextView bmiTextView;
    private EditText nameEditText;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        weightEditText = (EditText) findViewById(R.id.weightEditText);
        milesAmountLabel = (TextView) findViewById(R.id.milesAmountLabel);
        milesRanSeekBar = (SeekBar) findViewById(R.id.milesRanSeekBar);
        caloriesBurnedTextView = (TextView) findViewById(R.id.caloriesBurnedTextView);
        heightFeetSpinner = (Spinner) findViewById(R.id.heightFeetSpinner);
        heightInchesSpinner = (Spinner) findViewById(R.id.heightInchesSpinner);
        bmiTextView = (TextView) findViewById(R.id.bmiTextView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        ArrayAdapter<String> heightFeetAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[] {
                "3ft",
                "4ft",
                "5ft",
                "6ft"
        });
        heightFeetSpinner.setAdapter(heightFeetAdapter);

        final ArrayAdapter<String> heightInchesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[] {
                "0in",
                "1in",
                "2in",
                "3in",
                "4in",
                "5in",
                "6in",
                "7in",
                "8in",
                "9in",
                "10in",
                "11in"
        });
        heightInchesSpinner.setAdapter(heightInchesAdapter);

        weightEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                calculateAndDisplay ( );

                return false;
            }
        });

        milesRanSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                calculateAndDisplay ( );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        heightFeetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateAndDisplay ( );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        heightInchesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateAndDisplay ( );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onPause() {
        // save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("weightString", weightEditText.getText().toString());
        editor.putInt("milesRan", milesRanSeekBar.getProgress());
        editor.putInt("heightFeet", heightFeet);
        editor.putInt("heightInches", heightInches);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        weightEditText.setText (savedValues.getString("weightString", "000"));
        milesRanSeekBar.setProgress(savedValues.getInt("milesRan", 1));
        heightFeetSpinner.setSelection(savedValues.getInt("heightFeet", 3) - 3);
        heightInchesSpinner.setSelection(savedValues.getInt("heightInches", 0));
    }

    float weight;
    int heightFeet;
    int heightInches;

    private void calculateAndDisplay ( ) {

        weight = 0;

        String weightString = weightEditText.getText().toString();
        try {
            weight = Float.parseFloat(weightString);
        } catch (Exception e) {
        }

        int milesRan = milesRanSeekBar.getProgress();
        milesAmountLabel.setText(milesRan + "mi");

        float caloriesBurned = 0.75f * weight * milesRan;
        caloriesBurnedTextView.setText(caloriesBurned + "");


        heightFeet = heightFeetSpinner.getSelectedItemPosition() + 3;
        heightInches = heightInchesSpinner.getSelectedItemPosition();

        float bmi = (weight * 703) / ((12 * heightFeet + heightInches) * (12 * heightFeet + heightInches));

        bmiTextView.setText(bmi + "");
    }
}
