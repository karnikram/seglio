package com.astuetz.cyber.teen.biblio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.karnix.cyberteen.biblio.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BooksAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String,String>> items = null;

    public BooksAdapter(Context context, ArrayList<HashMap<String, String>> items)
    {
        super();
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        MyHolder holder = null;
        if(v == null)
        {
            v = inflater.inflate(R.layout.recent_list_item,parent,false);
            holder = new MyHolder(v);
            v.setTag(holder);
        }

        else
        {
            holder = (MyHolder) v.getTag();
        }

        holder.title.setText(items.get(position).get("title"));
        holder.author.setText(items.get(position).get("author"));
        holder.dept.setText(items.get(position).get("dept"));
        holder.price.setText(items.get(position).get("price"));

        return v;
    }

    class MyHolder
    {
        TextView dept,title,author,price;

        public MyHolder(View view)
        {
            title = (TextView) view.findViewById(R.id.book_title);
            dept = (TextView)view.findViewById(R.id.dept_name);
            author = (TextView) view.findViewById(R.id.author_name);
            price = (TextView)view.findViewById(R.id.price_list);
        }
    }
}
