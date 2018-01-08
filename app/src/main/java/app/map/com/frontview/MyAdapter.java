package app.map.com.frontview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by arul on 20/5/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<List_View> item;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView heading, desc,status;

        public MyViewHolder(View view) {
            super(view);
            heading = (TextView) view.findViewById(R.id.titles);
            desc = (TextView) view.findViewById(R.id.desc);
            status=(TextView) view.findViewById(R.id.status);
        }
    }


    public MyAdapter(List<List_View> item, SecondActivity secondActivity) {

        this.item = item;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        List_View v = item.get(position);
        holder.heading.setText(v.getHead());
        holder.desc.setText(v.getDesc());
        holder.status.setText(v.getStatus());
        String res=holder.status.getText().toString();
        if(res.equals("offline"))
        {
            holder.status.setText("online");
            holder.status.setTextColor(Color.parseColor("#e9ebee"));
        }

        if(res.equals("online"))
        {
            holder.status.setText("online");
             holder.status.setTextColor(Color.parseColor("#1a1a1b"));

        }

    }

        @Override
        public int getItemCount()
        {
            return item.size();
        }
}

