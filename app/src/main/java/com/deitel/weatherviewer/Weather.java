package com.deitel.weatherviewer;

public class Weather {
    private final String data;
    private final double tempMin;
    private final double tempMax;
    private final String descricao;
    private final double umidade;
    private final String icone;

    public Weather(String data, double tempMin, double tempMax, String descricao, double umidade, String icone) {
        this.data = data;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.descricao = descricao;
        this.umidade = umidade;
        this.icone = icone;
    }

    public String getData() { return data; }
    public double getTempMin() { return tempMin; }
    public double getTempMax() { return tempMax; }
    public String getDescricao() { return descricao; }
    public double getUmidade() { return umidade; }
    public String getIcone() { return icone; }

    public String getStringDetalhes() {
        return String.format("Min: %.1f°C | Max: %.1f°C | Umidade: %.0f%%", getTempMin(), getTempMax(), getUmidade() * 100);
    }
}