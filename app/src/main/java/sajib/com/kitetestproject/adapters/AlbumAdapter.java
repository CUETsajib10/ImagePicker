package sajib.com.kitetestproject.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import sajib.com.kitetestproject.R;
import sajib.com.kitetestproject.interfaces.OnImageSelectedListener;
import sajib.com.kitetestproject.utils.Function;

/**
 * Created by Sajib on 3/3/2018.
 */



public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final OnImageSelectedListener mListener;
    private ArrayList<HashMap<String, String>> mAlbumList;

    //onSearchActivityListener sListener;
    String TAG = "AlbumAdapter";

    public  static View previousView;
    //onMainActivityListener mListener;
    JSONArray item = null;
    JSONObject obj;

    //String img_base_url= Variables.BASE_URL+"uploads/image/";
    private String pic;
    Context mContext;
    private String image;
    int count = 0;

    public AlbumAdapter(Context context, ArrayList<HashMap<String, String>> albumList, OnImageSelectedListener listener) {
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAlbumList = albumList;
        mContext = context;
        mListener = listener;
        Prefs.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_row, parent, false);
        // Log.d(TAG, "Favorite recycler view adapter");
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        HashMap < String, String > song = new HashMap < String, String > ();
        song = mAlbumList.get(position);
        holder.cardItem.setMinimumHeight(holder.cardItem.getWidth());

        try {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(10));
            Glide.with(mContext)
                    .load(new File(song.get(Function.KEY_PATH)))
                    .apply(requestOptions)// Uri of the picture
                    .into(holder.imageView);

        } catch (Exception e){
            e.printStackTrace();
        }

       if(Prefs.contains(song.get(Function.KEY_PATH))){
           holder.imageViewTick.setVisibility(View.VISIBLE);
           Log.d(TAG,"contains");
        }else{
           holder.imageViewTick.setVisibility(View.GONE);
        }

        final HashMap<String, String> finalSong = song;
        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.imageViewTick.getVisibility() == View.VISIBLE){
                    holder.imageViewTick.setVisibility(View.GONE);
                    count--;
                    mListener.onImageSelected(count);
                    Prefs.remove(finalSong.get(Function.KEY_PATH));

                }else{
                    count++;
                    holder.imageViewTick.setVisibility(View.VISIBLE);
                    //MyPref.save(mContext, Constant.PREF_KID,package_name,"");
                    mListener.onImageSelected(count);
                    Prefs.putBoolean(finalSong.get(Function.KEY_PATH),true);

                }
            }
        });
    }

    @Override
    public int getItemCount() {

       // return mItems.length();

        if(mAlbumList != null){
            return mAlbumList.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View cardItem;
        ImageView imageView, imageViewTick;

        public ViewHolder(View view) {

            super(view);
            imageView = view.findViewById(R.id.image);
           cardItem = view.findViewById(R.id.card_view);
           imageViewTick = view.findViewById(R.id.iv_tick);

        }

        @Override
        public String toString() {

            return super.toString();
        }
    }

    public void setData(ArrayList<HashMap<String, String>> nAlbumList){
        mAlbumList = new ArrayList<>(nAlbumList);
        notifyDataSetChanged();
    }
}

