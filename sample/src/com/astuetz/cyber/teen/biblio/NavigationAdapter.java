package com.astuetz.cyber.teen.biblio;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.karnix.cyberteen.biblio.R;

import java.util.ArrayList;

public class NavigationAdapter extends BaseAdapter{

    ArrayList<NavigationItem> navigationItems;
    LayoutInflater inflater;

    public NavigationAdapter(Activity context, ArrayList<NavigationItem> navigationItems)
    {
        super();
        this.navigationItems = navigationItems;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return navigationItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View v = view;
        NavigationItem item = navigationItems.get(i);

        if(view == null)
        {
            v = inflater.inflate(R.layout.navigation_list_item,null);
        }

        ImageView icon = (ImageView) v.findViewById(R.id.row_icon);
        TextView itemName = (TextView)v.findViewById(R.id.row_text);

        icon.setImageResource(item.imageId);
        itemName.setText(item.item);

        return  v;
    }
}
