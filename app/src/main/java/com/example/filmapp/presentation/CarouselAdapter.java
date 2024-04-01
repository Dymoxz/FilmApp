package com.example.filmapp.presentation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;
import com.example.filmapp.model.MediaItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_IMAGE = 0;
    private static final int VIEW_TYPE_VIDEO = 1;

    private List<MediaItem> mediaItems;

    public CarouselAdapter(List<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    @Override
    public int getItemViewType(int position) {
        return mediaItems.get(position).getVideoUrl() != null ? VIEW_TYPE_VIDEO : VIEW_TYPE_IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_VIDEO) {
            View itemView = inflater.inflate(R.layout.item_video, parent, false);
            return new VideoViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MediaItem item = mediaItems.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_VIDEO) {
            VideoViewHolder videoHolder = (VideoViewHolder) holder;
            videoHolder.webView.loadData(item.getVideoUrl(), "text/html", "utf-8");
        } else {
            ImageViewHolder imageHolder = (ImageViewHolder) holder;
            Log.d("CarouselAdapter", "ImagePath:" + item.getImagePath());
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + item.getImagePath()).into(imageHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    // ViewHolder classes for video and image items
    private static class VideoViewHolder extends RecyclerView.ViewHolder {
        WebView webView;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.videoWebView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
        }
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.posterImageView);
        }
    }
}
