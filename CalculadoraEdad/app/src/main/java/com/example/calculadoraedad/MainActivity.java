package com.example.calculadoraedad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void continuar(View view) {
        String nombre = ((EditText) findViewById(R.id.etNombre)).getText().toString();
        Double edad = Double.parseDouble(((EditText) findViewById(R.id.etEdad)).getText().toString());

        Intent intent = new Intent(this, Activity2.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("edad", edad.toString());
        startActivity(intent);
    }



}
