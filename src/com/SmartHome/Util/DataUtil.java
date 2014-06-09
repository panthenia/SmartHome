package com.SmartHome.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.SmartHome.R;

/**
 * Created by p on 14-4-23.
该类为数据库操作类，继承了android提供的SQLiteOpenHelper类
 使用时创建该类的对象后，调用继承自SQLiteOpenHelper类的
 getReadableDatabase()方法获得一个可读写的SQLiteDataBase对象进行操作
当打开数据库失败时会调用重写的onCreate方法来创建一个数据库
 */

public class DataUtil extends SQLiteOpenHelper {

    private Context ctx;
    public DataUtil(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        ctx=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(ctx.getString(R.string.DB_CRT_SENSOR_TB));
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
