package com.inappcamera.cameragallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HorizontalImageAdapter extends RecyclerView.Adapter<HorizontalImageAdapter.ImageViewHolder> {

    private Activity activity;

    public HorizontalImageAdapter(Activity activity) {
        this.activity = activity;
        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    private List<String> list;

    private RequestOptions requestOptions;

    public void setListData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, null);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder viewHolder, final int position) {
        Glide.with(activity).load(list.get(position)).apply(requestOptions).into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(activity, PreviewPhotoActivity.class);
                intent.putExtra("previewImageURI", list.get(position));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options =

                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                                    view,   // Starting view
                                    view.getTransitionName()    // The String
                            );
                    ActivityCompat.startActivityForResult(activity, intent, AppConstants.PHOTO_CLICKED_INTENT, options.toBundle());
                } else {
                    activity.startActivityForResult(intent, AppConstants.PHOTO_CLICKED_INTENT);
                }*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
