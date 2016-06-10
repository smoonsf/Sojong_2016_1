package embeddedlab.yonsei.cs.sojong_test;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alexmoon on 2016. 4. 25..
 */
public class RankRecyclerViewAdapter extends RecyclerView.Adapter<RankRecyclerViewAdapter.ViewHolder>{

    List<String> appNames;
    List<Double> ranks;
    List<Drawable> icons;
    List<Integer> modes;

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView_icon;
        public TextView textView_appName;
        public TextView textView_rank;
        public ImageView imageView_reset;
        public Switch switch_mode;
        public ViewHolder(View itemView) {
            super(itemView);
            this.switch_mode = (Switch) itemView.findViewById(R.id.switch_mode);
            this.imageView_icon = (ImageView) itemView.findViewById(R.id.imageview_icon);
            this.textView_rank = (TextView) itemView.findViewById(R.id.textview_rank);
            this.textView_appName = (TextView) itemView.findViewById(R.id.textview_appname);
            this.imageView_reset = (ImageView) itemView.findViewById(R.id.imageview_reset);
        }
    }

    RankRecyclerViewAdapter(List<String> _appNames, List<Double> _ranks, List<Drawable> _icons, List<Integer> _modes){
        appNames = _appNames;
        ranks = _ranks;
        icons = _icons;
        modes = _modes;

    }

    @Override
    public RankRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder =
                new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank_recycler, null));

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView_appName.setText(appNames.get(position));
        holder.textView_rank.setText(ranks.get(position) + "");
        holder.imageView_icon.setImageDrawable(icons.get(position));
        holder.imageView_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySQLiteOpenHelper openHelper = new MySQLiteOpenHelper(v.getContext(), 1);
                SQLiteDatabase database = openHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("pname", appNames.get(position));
                values.put("rankpoint", (Double) 2.0);
                String[] args = {appNames.get(position)};
                database.update("apprank", values, "pname=?", args);
                holder.textView_rank.setText("2.0");
            }
        });
        holder.switch_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySQLiteOpenHelper openHelper = new MySQLiteOpenHelper(buttonView.getContext(), 1);
                SQLiteDatabase database = openHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                Integer modebit = 0;
                if(isChecked)
                    modebit = 1;
                values.put("mode", modebit);
                String[] args = {appNames.get(position)};
                database.update("apprank", values, "pname=?", args);
            }
        });
        if(modes.get(position) == 0)
            holder.switch_mode.setChecked(false);
        else
            holder.switch_mode.setChecked(true);



    }


    @Override
    public int getItemCount() {


        return appNames.size();
    }
}
