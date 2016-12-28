package com.example.lefun.lefun;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private ListView lv;
    private List<Map<String, Object>> data;
    private Bag mBag;
    MyAdapter adapter;
    final static String BUY = "买";
    final static String SELL = "卖";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.lv);
        //获取将要绑定的数据设置到data中
        adapter = new MyAdapter(this);
        lv.setAdapter(adapter);
        new SearchTwitterTask().execute("002200");
    }

    private List<Map<String, Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for(int i=0;i<10;i++)
        {
            map = new HashMap<String, Object>();
            map.put("title1", "title1");
            map.put("price1", "price1");
            map.put("volumn1", "volumn1");
            map.put("title2", "title1");
            map.put("price2", "price1");
            map.put("volumn2", "volumn1");
            list.add(map);
        }
        return list;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            super.handleMessage(msg);
            new SearchTwitterTask().execute("002200");
        }
    };
    class SearchTwitterTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg0) {
//			String url = "http://search.twitter.com/search.json?q=" + URLEncoder.encode(arg0[0]);
            String url = "https://app.leverfun.com/timelyInfo/timelyOrderForm?stockCode=" + arg0[0];
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);
                HttpResponse response = client.execute(request);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                String json = "";
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                showData(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessageDelayed(0,1000);
        }

    }

    private void showData(String json) throws JSONException {
        mBag= new Gson().fromJson(json, Bag.class);
        adapter.notifyDataSetChanged();
    }
    //ViewHolder静态类
    static class ViewHolder {
        public TextView title1;
        public TextView price1;
        public TextView volumn1;
        public TextView title2;
        public TextView price2;
        public TextView volumn2;
    }
    public class MyAdapter extends BaseAdapter {


        private LayoutInflater mInflater = null;

        public MyAdapter(Context context) {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            //How many items are in the data set represented by this Adapter.
            //在此适配器中所代表的数据集中的条目数
            return mBag == null ? 0 : 10;
        }

        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return position;
        }

        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }

        //Get a View that displays the data at the specified position in the data set.
        //获取一个在数据集中指定索引的视图来显示数据
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if (convertView == null) {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder.title1 = (TextView) convertView.findViewById(R.id.title1);
                holder.price1 = (TextView) convertView.findViewById(R.id.price1);
                holder.volumn1 = (TextView) convertView.findViewById(R.id.volumn1);
                holder.title2 = (TextView) convertView.findViewById(R.id.title2);
                holder.price2 = (TextView) convertView.findViewById(R.id.price2);
                holder.volumn2 = (TextView) convertView.findViewById(R.id.volumn2);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            if(position <= 4) {
                int first = 5-position;
                holder.title1.setText(SELL+String.valueOf(first));
                if(mBag.data.sellPankou!=null && mBag.data.sellPankou.length > first) {
                    holder.price1.setText(String.valueOf(mBag.data.sellPankou[first].price));
                    holder.volumn1.setText(String.valueOf(mBag.data.sellPankou[first].volume));
                }
                int second = first + 5;
                holder.title2.setText(SELL+String.valueOf(second));
                if(mBag.data.sellPankou!=null && mBag.data.sellPankou.length > second) {
                    holder.price2.setText(String.valueOf(mBag.data.sellPankou[second].price));
                    holder.volumn2.setText(String.valueOf(mBag.data.sellPankou[second].volume));
                }
            } else {
                int first = position-4;
                holder.title1.setText(BUY+String.valueOf(first));
                if(mBag.data.buyPankou!=null && mBag.data.buyPankou.length > first) {
                    holder.price1.setText(String.valueOf(mBag.data.buyPankou[first].price));
                    holder.volumn1.setText(String.valueOf(mBag.data.buyPankou[first].volume));
                }
                int second = first + 5;
                holder.title2.setText(BUY+String.valueOf(second));
                if(mBag.data.buyPankou!=null && mBag.data.buyPankou.length > second) {
                    holder.price2.setText(String.valueOf(mBag.data.buyPankou[second].price));
                    holder.volumn2.setText(String.valueOf(mBag.data.buyPankou[second].volume));
                }
            }

            return convertView;
        }

    }
}
