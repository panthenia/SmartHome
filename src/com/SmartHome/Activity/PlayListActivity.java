package com.SmartHome.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.SmartHome.Adaptor.PlayListAdaptor;
import com.SmartHome.R;

/**
 * Created by p on 14-5-9.
 */
public class PlayListActivity extends Activity {

    TextView textViews[] = new TextView[4] ;//四个媒体类型
    ImageView imageView[] = new ImageView[4];//四个括号

    String play_list[]={"smb://DLINK/Volume_1/3.mp4",
            "smb://DLINK/Volume_1/a.mp4","smb://DLINK/Volume_1/4.mp4"};
    String author_list[] = {"老虎不在家","小松鼠","大老虎"};
    ListView listView;
    PlayListAdaptor list_adapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.play_list);

        textViews[0]=(TextView)findViewById(R.id.textView1);
        textViews[1]=(TextView)findViewById(R.id.textView2);
        textViews[2]=(TextView)findViewById(R.id.textView3);
        textViews[3]=(TextView)findViewById(R.id.textView4);

        textViews[0].setSelected(true);//默认选择所有

        imageView[0]=(ImageView)findViewById(R.id.pic1);
        imageView[1]=(ImageView)findViewById(R.id.pic2);
        imageView[2]=(ImageView)findViewById(R.id.pic3);
        imageView[3]=(ImageView)findViewById(R.id.pic4);

        imageView[0].setVisibility(View.VISIBLE);
        imageView[1].setVisibility(View.INVISIBLE);
        imageView[2].setVisibility(View.INVISIBLE);
        imageView[3].setVisibility(View.INVISIBLE);

        listView = (ListView)findViewById(R.id.list);
        list_adapter = new PlayListAdaptor(this,play_list,0,this);
        listView.setAdapter(list_adapter);
        TextView textView = (TextView)findViewById(R.id.acitivity_name);
        textView.setText("      媒体库");
    }
    public void onAllClicked(View v){// 在 textviews 中对应的位置 0
        list_adapter.setContent(play_list);
        list_adapter.setShowLevel(0);
        list_adapter.notifyDataSetChanged();
       for(int i=0;i<textViews.length;++i){
           textViews[i].setSelected(false);
           imageView[i].setVisibility(View.INVISIBLE);
       }
        textViews[0].setSelected(true);
        imageView[0].setVisibility(View.VISIBLE);
    }
    public void onAuthorClicked(View v){//1
        list_adapter.setContent(author_list);
        list_adapter.setShowLevel(1);
        list_adapter.setSubContent(play_list);
        list_adapter.notifyDataSetChanged();
        for(int i=0;i<textViews.length;++i){
            textViews[i].setSelected(false);
            imageView[i].setVisibility(View.INVISIBLE);
        }
        textViews[1].setSelected(true);
        imageView[1].setVisibility(View.VISIBLE);
    }
    public void onIssueClicked(View v){//2
        for(int i=0;i<textViews.length;++i){
            textViews[i].setSelected(false);
            imageView[i].setVisibility(View.INVISIBLE);
        }
        textViews[2].setSelected(true);
        imageView[2].setVisibility(View.VISIBLE);
    }
    public void onStyleClicked(View v){//3
        for(int i=0;i<textViews.length;++i){
            textViews[i].setSelected(false);
            imageView[i].setVisibility(View.INVISIBLE);
        }
        textViews[3].setSelected(true);
        imageView[3].setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed(){
        Intent intent = getIntent();
        intent.putExtra("file_name","");
        intent.putExtra("file_path","");
        setResult(0,intent);
        finish();
    }


}