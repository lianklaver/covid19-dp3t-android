package org.dpppt.android.app.selectlanguage.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.dpppt.android.app.R;

import java.util.List;


public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.ViewHolder> {
    private List<LanguageUiModel> languageUiModels;
    private Listener listener;

    public LanguagesAdapter(List<LanguageUiModel> languageUiModels, Listener listener) {
        this.languageUiModels = languageUiModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LanguagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LanguageUiModel languageUiModel = languageUiModels.get(position);

        holder.textView.setOnClickListener(v -> {
            listener.onLanguageSelected(languageUiModel);
        });
        holder.textView.setText(languageUiModel.getLabel());
        holder.textView.setBackgroundResource(languageUiModel.isSelected() ? R.drawable.bg_selected_language : 0);
    }

    @Override
    public int getItemCount() {
        return languageUiModels.size();
    }

    public interface Listener {
        void onLanguageSelected(LanguageUiModel languageUiModel);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }
}