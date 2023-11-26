package com.example.calculadoraedad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class Activity11 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_11);
    }

    public void btnSiguienteOnClick(View view) {
        RadioButton opc1 = (RadioButton) findViewById(R.id.rbOpcion1);
        RadioButton opc2 = (RadioButton) findViewById(R.id.rbOpcion2);
        RadioButton opc3 = (RadioButton) findViewById(R.id.rbOpcion3);
        RadioButton opc4 = (RadioButton) findViewById(R.id.rbOpcion4);
        Intent intent = getIntent();
        Double edad = Double.valueOf(intent.getStringExtra("edad"));

        if (opc1.isChecked()) {
            edad += 1.5;
        } else if (opc2.isChecked()) {
            edad += 1;
        } else if (opc3.isChecked()) {
            edad -= 0.5;
        } else if (opc4.isChecked()) {
            edad -= 1;
        }

        Intent intent2 = new Intent(this, Activity12.class);
        intent2.putExtra("nombre", intent.getStringExtra("nombre"));
        intent2.putExtra("edad", edad.toString());
        startActivity(intent2);
    }
}