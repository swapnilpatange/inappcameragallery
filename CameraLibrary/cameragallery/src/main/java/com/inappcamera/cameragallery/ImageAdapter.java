package com.inappcamera.cameragallery;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> list;
    private Activity activity;
    final int LIST_TYPE;
    private RequestOptions requestOptions;
    public final static int HORIZONTAL_LIST = 1;
    public final static int NORMAL_ITEM = 2;

    private LiveData<Float> offsetLiveData;

    public ImageAdapter(Activity activity, List<String> list, int listType) {
        this.list = list;
        this.activity = activity;
        this.LIST_TYPE = listType;
        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == HORIZONTAL_LIST) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_list, null);
            return new HorizontalListViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vertical_image_item, null);
            return new ImageViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HORIZONTAL_LIST;
        else
            return NORMAL_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ImageViewHolder) {
            setVerticalListData((ImageViewHolder) viewHolder, position);
        } else {
            final HorizontalListViewHolder horizontalListViewHolder = (HorizontalListViewHolder) viewHolder;
            HorizontalImageAdapter horizontalImageAdapter = new HorizontalImageAdapter(activity);
            horizontalListViewHolder.horizontalList.setAdapter(horizontalImageAdapter);
            horizontalListViewHolder.horizontalList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            horizontalImageAdapter.setListData(list);
            horizontalListViewHolder.horizontalList.setNestedScrollingEnabled(false);
            offsetLiveData.observe((Camera2Activity) activity, new Observer<Float>() {
                @Override
                public void onChanged(@Nullable Float aFloat) {
                    horizontalListViewHolder.horizontalList.setAlpha(1 - aFloat);
                    horizontalListViewHolder.txtPhotos.setAlpha(aFloat);
                }
            });
        }
        // Picasso.get().load(new File(list.get(position))).into(viewHolder.imageView);
    }

    private void setVerticalListData(final ImageViewHolder viewHolder, final int position) {
        Glide.with(activity).load(list.get(position - 1)).apply(requestOptions).into(viewHolder.imageView);

        offsetLiveData.observe((Camera2Activity) activity, new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float aFloat) {
                viewHolder.lnrMain.setAlpha(aFloat);
            }
        });

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(activity, PreviewPhotoActivity.class);
                intent.putExtra("previewImageURI", list.get(position - 1));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options =

                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                                    view,   // Starting view
                                    view.getTransitionName()    // The String
                            );
                    ActivityCompat.startActivityForResult(activity, intent, AppConstants.PHOTO_CLICKED_INTENT,options.toBundle());
                } else {
                    activity.startActivityForResult(intent,AppConstants.PHOTO_CLICKED_INTENT);
                }*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public void setListData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setLiveData(MutableLiveData<Float> offsetLiveData) {
        this.offsetLiveData = offsetLiveData;
    }


    class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private LinearLayout lnrMain;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            lnrMain = itemView.findViewById(R.id.lnrMain);
        }
    }

    class HorizontalListViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView horizontalList;
        public TextView txtPhotos;

        public HorizontalListViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalList = itemView.findViewById(R.id.horizontalList);
            txtPhotos = itemView.findViewById(R.id.txtPhotos);
        }
    }
}
