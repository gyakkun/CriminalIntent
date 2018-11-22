package database.HeroDbSchema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.HeroDbSchema.HeroDbSchema.HeroTable;

public class HeroBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "heroBase.db";

    public HeroBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + HeroTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                HeroTable.Cols.UUID + ", " +
                HeroTable.Cols.NAME + ", " +
                HeroTable.Cols.NICKNAME + ", " +
                HeroTable.Cols.STARRED + ", " +
                HeroTable.Cols.POSITION + ", " +
                HeroTable.Cols.LIVENESS + ", " +
                HeroTable.Cols.ATTACK + ", " +
                HeroTable.Cols.AFFECTION + ", " +
                HeroTable.Cols.HARDNESS +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
