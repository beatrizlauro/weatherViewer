package com.deitel.weatherviewer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText campoCidade;
    private ListView listaPrevisao;
    private WeatherAdapter adaptadorPrevisao;
    private List<Weather> listaDadosPrevisao;

    private static final String URL_BASE = "http://agent-weathermap-env-env.eba-6pzgqekp.us-east-2.elasticbeanstalk.com/api/weather";
    private static final String APP_ID = "AgentWeather2024_a8f3b9c1d7e2f5g6h4i9j0k1l2m3n4o5p6";
    private static final int DIAS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campoCidade = findViewById(R.id.cityEditText);
        listaPrevisao = findViewById(R.id.weatherListView);
        FloatingActionButton botaoFlutuante = findViewById(R.id.fab);

        listaDadosPrevisao = new ArrayList<>();
        adaptadorPrevisao = new WeatherAdapter(this, listaDadosPrevisao);
        listaPrevisao.setAdapter(adaptadorPrevisao);

        botaoFlutuante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cidade = campoCidade.getText().toString().trim();
                if (cidade.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, digite a cidade (ex: Passos,MG,BR).", Toast.LENGTH_SHORT).show();
                    return;
                }
                new BuscarPrevisaoTask().execute(cidade);
            }
        });
    }

    private class BuscarPrevisaoTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "Buscando previsão...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String cidade = params[0];

            String urlString = String.format("%s?city=%s&days=%d&APPID=%s",
                    URL_BASE, cidade, DIAS, APP_ID);

            HttpURLConnection conexaoUrl = null;
            BufferedReader leitor = null;
            String respostaJson = null;

            try {
                URL url = new URL(urlString);
                conexaoUrl = (HttpURLConnection) url.openConnection();
                conexaoUrl.setRequestMethod("GET");
                conexaoUrl.connect();

                int codigoResposta = conexaoUrl.getResponseCode();
                if (codigoResposta != HttpURLConnection.HTTP_OK) {
                    return "ERRO_HTTP:" + codigoResposta;
                }

                leitor = new BufferedReader(new InputStreamReader(conexaoUrl.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String linha;
                while ((linha = leitor.readLine()) != null) {
                    buffer.append(linha).append("\n");
                }
                respostaJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return "ERRO_REDE";
            } finally {
                if (conexaoUrl != null) {
                    conexaoUrl.disconnect();
                }
                if (leitor != null) {
                    try {
                        leitor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return respostaJson;
        }

        @Override
        protected void onPostExecute(String resultado) {
            if (resultado == null || resultado.startsWith("ERRO_")) {
                String mensagemErro = "Erro desconhecido.";
                if (resultado != null) {
                    if (resultado.startsWith("ERRO_HTTP")) {
                        String codigo = resultado.split(":")[1];
                        mensagemErro = "Erro na requisição HTTP: " + codigo + ". Verifique se a cidade está no formato correto (Cidade,Estado,País).";
                    } else if (resultado.equals("ERRO_REDE")) {
                        mensagemErro = "Erro de conexão. Verifique sua permissão de Internet e a URL da API.";
                    }
                }
                Toast.makeText(MainActivity.this, mensagemErro, Toast.LENGTH_LONG).show();
                listaDadosPrevisao.clear();
                adaptadorPrevisao.notifyDataSetChanged();
                return;
            }

            try {
                listaDadosPrevisao.clear();

                JSONObject objetoJson = new JSONObject(resultado);

                if (!objetoJson.has("days")) {
                    Toast.makeText(MainActivity.this, "Resposta da API inválida. Verifique o formato JSON.", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONArray arrayDias = objetoJson.getJSONArray("days");

                for (int i = 0; i < arrayDias.length(); i++) {
                    JSONObject objetoDia = arrayDias.getJSONObject(i);

                    String data = objetoDia.getString("date");
                    double tempMin = objetoDia.getDouble("minTempC");
                    double tempMax = objetoDia.getDouble("maxTempC");
                    String descricao = objetoDia.getString("description");
                    double umidade = objetoDia.getDouble("humidity");
                    String icone = objetoDia.getString("icon");

                    Weather previsao = new Weather(data, tempMin, tempMax, descricao, umidade, icone);
                    listaDadosPrevisao.add(previsao);

                    Log.d("PREVISAO_DEBUG", "Dia: " + data + ", TempMax: " + tempMax + ", Desc: " + descricao);
                }

                adaptadorPrevisao.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Previsão atualizada para " + objetoJson.getString("city"), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Erro ao processar os dados da previsão (JSON inválido).", Toast.LENGTH_LONG).show();
                listaDadosPrevisao.clear();
                adaptadorPrevisao.notifyDataSetChanged();
            }
        }
    }
}