package com.example.android11mediastorage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android11mediastorage.databinding.ItemMediaBinding;

import java.util.List;

public class MAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private List<MediaFile> listItem;

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setListItem(List<MediaFile> listItem) {
        this.listItem = listItem;
        this.notifyDataSetChanged();
    }

    public MAdapter(Context context) {
        this.context = context;
    }

    public interface Callback {

        void onItemClick(int position, MediaFile item);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        return new MediaViewHolder(ItemMediaBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaViewHolder) {
            ((MediaViewHolder) holder).bindItem(position);
        }
    }

    @Override
    public int getItemCount() {
        return this.listItem == null ? 0 : this.listItem.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {

        private final ItemMediaBinding binding;

        public MediaViewHolder(@NonNull ItemMediaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bindItem(int position) {
            MediaFile file = listItem.get(position);
            if (file == null) {
                return;
            }

            if (!TextUtils.isEmpty(file.mime)) {
                if (TextUtils.equals(file.mime, "audio/mpeg")) {
                    this.binding.ivIcon.setImageResource(R.drawable.ic_sharp_audiotrack_24);
                }else {
                    Glide.with(context)
                            .load(file.uri)
                            .error(R.drawable.ic_sharp_image_24)
                            .into(this.binding.ivIcon);
                }
            }else {
                this.binding.ivIcon.setImageResource(R.drawable.ic_sharp_image_24);
            }

            this.binding.tvInfo.setText(file.name + " | " + Formatter.formatFileSize(context, file.size) + " | " + file.duration);

            this.itemView.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onItemClick(position, file);
                }
            });
        }
    }
}
