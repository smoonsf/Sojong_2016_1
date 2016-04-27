package embeddedlab.yonsei.cs.sojong_test;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alexmoon on 2016. 4. 25..
 */
public class RankRecyclerViewAdapter extends RecyclerView.Adapter<RankRecyclerViewAdapter.ViewHolder>{

    List<String> appNames;
    List<Double> ranks;
    List<Drawable> icons;

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView_icon;
        public TextView textView_appName;
        public TextView textView_rank;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView_icon = (ImageView) itemView.findViewById(R.id.imageview_icon);
            this.textView_rank = (TextView) itemView.findViewById(R.id.textview_rank);
            this.textView_appName = (TextView) itemView.findViewById(R.id.textview_appname);
        }
    }

    RankRecyclerViewAdapter(List<String> _appNames, List<Double> _ranks, List<Drawable> _icons){
        appNames = _appNames;
        ranks = _ranks;
        icons = _icons;

    }

    @Override
    public RankRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder =
                new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank_recycler, null));

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView_appName.setText(appNames.get(position));
        holder.textView_rank.setText(ranks.get(position) + "");
        holder.imageView_icon.setImageDrawable(icons.get(position));



    }


    @Override
    public int getItemCount() {


        return appNames.size();
    }
}
