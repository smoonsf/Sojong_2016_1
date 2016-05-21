package embeddedlab.yonsei.cs.sojong_test;

import android.app.Notification;
import android.graphics.drawable.Drawable;
import android.service.notification.StatusBarNotification;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alexmoon on 2016. 4. 25..
 */
public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder>{

    List<StatusBarNotification> notificationList;

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView_icon;
        public TextView textView_title;
        public TextView textView_subtitle;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView_icon = (ImageView) itemView.findViewById(R.id.imageview_icon);
            this.textView_title = (TextView) itemView.findViewById(R.id.textview_title);
            this.textView_subtitle = (TextView) itemView.findViewById(R.id.textview_subtitle);
        }
    }

    NotificationRecyclerViewAdapter(List<StatusBarNotification> _notificationList){
        notificationList = _notificationList;
    }

    @Override
    public NotificationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder =
                new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_recycler, null));

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView_title.setText(notificationList.get(position).getPackageName());
        holder.textView_subtitle.setText(new Date(notificationList.get(position).getPostTime()).toGMTString());
        holder.imageView_icon.setImageBitmap(notificationList.get(position).getNotification().largeIcon);




    }


    @Override
    public int getItemCount() {


        return notificationList.size();
    }
}
