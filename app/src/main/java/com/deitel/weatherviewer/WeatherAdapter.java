package com.deitel.weatherviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter<Weather> {

    private static class ViewHolder {
        TextView iconeTextView;
        TextView dataTextView;
        TextView descricaoTextView;
        TextView detalhesTextView;
    }

    public WeatherAdapter(Context contexto, List<Weather> listaPrevisao) {
        super(contexto, R.layout.list_item_weather, listaPrevisao);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Weather previsao = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_weather, parent, false);

            viewHolder.iconeTextView = convertView.findViewById(R.id.iconTextView);
            viewHolder.dataTextView = convertView.findViewById(R.id.dateTextView);
            viewHolder.descricaoTextView = convertView.findViewById(R.id.descriptionTextView);
            viewHolder.detalhesTextView = convertView.findViewById(R.id.detailsTextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (previsao != null) {
            viewHolder.iconeTextView.setText(previsao.getIcone());
            viewHolder.dataTextView.setText(previsao.getData());
            viewHolder.descricaoTextView.setText(previsao.getDescricao());
            viewHolder.detalhesTextView.setText(previsao.getStringDetalhes());
        }

        return convertView;
    }
}