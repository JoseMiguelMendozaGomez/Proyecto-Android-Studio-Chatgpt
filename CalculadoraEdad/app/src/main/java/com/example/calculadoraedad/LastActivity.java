package com.example.calculadoraedad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);

        Intent intent = getIntent();
        Double edad = Double.parseDouble(intent.getStringExtra("edad"));



        ((TextView) findViewById(R.id.txEdad)).setText(edad.toString());
        Double edadCalculada = Double.parseDouble(intent.getStringExtra("edad"));
        String nombre = intent.getStringExtra("nombre");

        // Obtener la edad real (32) de alguna forma, supongamos que la tenemos en una variable.
        Double edadReal = 21.0;

        // Comparar la edad calculada con la edad real y construir el mensaje en consecuencia.
        String mensaje;
        if (edadCalculada > edadReal) {
            mensaje = "Mi nombre es " + nombre + ", dame consejos para mejorar mi estado físico ya que tengo " +
                    edadReal.toString() + " años pero aparento " + edadCalculada.toString();
        } else {
            mensaje = "Mi nombre es " + nombre + ", dame consejos para mantenerme en forma ya que mi edad real es " +
                    edadReal.toString() + " años pero aparento " + edadCalculada.toString();
        }

        ((TextView) findViewById(R.id.txEdad)).setText(edadCalculada.toString());
        ((TextView) findViewById(R.id.txtPregunta)).setText(mensaje);
    }

    public void btnRegresarOnClick(View view) {
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }

    public void btnProcesarOnClick(View view) {
        // Código del botón "Procesar" para enviar la pregunta a GPT-3

        String pregunta = ((TextView) findViewById(R.id.txtPregunta)).getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.openai.com/v1/chat/completions";
        JSONObject jsonBody = new JSONObject();
        try {
            JSONArray messagesArray = new JSONArray();

            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", pregunta);
            messagesArray.put(systemMessage);

            jsonBody.put("messages", messagesArray);
            jsonBody.put("max_tokens", 3000);
            jsonBody.put("model", "gpt-3.5-turbo");
            jsonBody.put("temperature", 0.7);
            jsonBody.put("top_p", 1);
            jsonBody.put("frequency_penalty", 0);
            jsonBody.put("presence_penalty", 0);

        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    // Maneja la respuesta de la API de ChatGPT
                    try {
                        String completions = response.getJSONArray("choices").getJSONObject(0).getString("message");
                        JSONObject assistantMessage = new JSONObject(completions);
                        String content = assistantMessage.getString("content");
                        ((TextView) findViewById(R.id.lblRespuesta)).setText(content);
                    } catch (JSONException e) {
                        Toast.makeText(this, "1 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Maneja los errores de la solicitud
                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown error occurred " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer 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");
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
}
