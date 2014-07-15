package com.SmartHome.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.SmartHome.Activity.PlayListActivity;
import com.SmartHome.R;

import java.util.ArrayList;

/**
 * Created by p on 14-5-9.
 */
public class PlayListAdaptor extends BaseAdapter {

    ArrayList<String> list_content = null;
    ArrayList<String> file_names = new ArrayList<String>();
    ArrayList<String> sub_content = null;
    Context context =null;
    View.OnClickListener listener = null;
    LayoutInflater inflater = null;
    PlayListActivity playListActivity;

    /*由于于同一个listview 需要显示 所有的歌曲，或者作者然后点了作者在进入该作者的
    歌曲列表 所以用show_level变量记录当前listview显示的层次，0是歌曲层点击后直接返回播放，1是歌曲的
    上一层（作者，专辑之类）点击进入相应的歌曲列表
    */
    int show_level;

    public PlayListAdaptor(Context ctx,ArrayList<String> lst,int level,PlayListActivity act){
        list_content = lst;
        context = ctx;
        inflater = LayoutInflater.from(ctx);
        show_level = level;
        playListActivity = act;
    }
    public PlayListAdaptor(Context ctx,String[] lst,int level,PlayListActivity act){
        list_content = new ArrayList<String>();
        for(int i=0;i<lst.length;++i)
            list_content.add(lst[i]);
        file_names.add("control4驱动设计教学");
        file_names.add("2nix激战系列宣传片");
        file_names.add("红尘客栈");
        show_level = level;
        context = ctx;
        playListActivity = act;
        inflater = LayoutInflater.from(ctx);
    }
    public void setContent(ArrayList<String> arr){
        list_content = arr;
    }
    public void setContent(String[] arr){
        list_content = new ArrayList<String>();
        for(int i=0;i<arr.length;++i)
            list_content.add(arr[i]);
    }
    public void setShowLevel(int level){
        show_level = level;
    }
    public void setSubContent(ArrayList<String> ar){
        sub_content = ar;
    }
    public void setSubContent(String[] ar){
        sub_content = new ArrayList<String>();
        for(int i=0;i<ar.length;++i)
            sub_content.add(ar[i]);
    }
    @Override
    public int getCount() {
        return list_content.size();

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.play_list_item,null);
        TextView textView = (TextView)v.findViewById(R.id.play_list_item);
        textView.setText(file_names.get(i));

        if(show_level == 0)
            v.setOnClickListener(new ZeroLevelClicked(i));
        else if(show_level == 1)
                v.setOnClickListener(new OneLevelCliced());
        return v;
    }
    public class ZeroLevelClicked implements View.OnClickListener{
        int ci;
        public ZeroLevelClicked(int i){
            ci =i;
        }
        @Override
        public void onClick(View view) {
            Intent intent = playListActivity.getIntent();
            TextView textView = (TextView)view.findViewById(R.id.play_list_item);
            intent.putExtra("file_name",textView.getText());
            intent.putExtra("file_path",list_content.get(ci));
            playListActivity.setResult(0,intent);
            playListActivity.finish();
        }
    }
    public class OneLevelCliced implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            list_content = sub_content;
            setShowLevel(0);
            notifyDataSetChanged();
        }
    }
}
