package moe.nyamori.arenaofvalor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.HeroDbSchema.HeroBaseHelper;
import database.HeroDbSchema.HeroCursorWrapper;
import database.HeroDbSchema.HeroDbSchema.HeroTable;

public class HeroLab {
    private static HeroLab sHeroLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static HeroLab get(Context context) {
        if (sHeroLab == null) {
            sHeroLab = new HeroLab(context);
        }
        return sHeroLab;
    }

    private HeroLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new HeroBaseHelper(mContext)
                .getWritableDatabase();


        //For test only

        for (int ctr = 0; ctr < 2; ctr++) {
            Hero Hero = new Hero();
            Hero.setTitle("Test Hero #" + ctr);
            Hero.setSolved(ctr % 2 == 0);
        }

    }

    public List<Hero> getHeros() {
        List<Hero> Heros = new ArrayList<>();

        HeroCursorWrapper cursor = queryHeros(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Heros.add(cursor.getHero());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }


        //now it returns a snapshot of the whole database
        //rather than a reference of mHeros before
        return Heros;
    }

    public Hero getHero(UUID id) {
        HeroCursorWrapper cursor = queryHeros(
                HeroTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getHero();
        } finally {
            cursor.close();
        }

    }

    public File getPhotoFile(Hero Hero){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, Hero.getPhotoFileName());
    }

    public void addHero(Hero c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(HeroTable.NAME, null, values);
    }

    public void updateHero(Hero Hero) {
        String uuidString = Hero.getId().toString();
        ContentValues values = getContentValues(Hero);

        mDatabase.update(HeroTable.NAME, values,
                HeroTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private HeroCursorWrapper queryHeros(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                HeroTable.NAME,
                null, // Select al the columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new HeroCursorWrapper(cursor);

    }

    private static ContentValues getContentValues(Hero Hero) {
        ContentValues values = new ContentValues();
        values.put(HeroTable.Cols.UUID, Hero.getId().toString());
        values.put(HeroTable.Cols.TITLE, Hero.getTitle());
        values.put(HeroTable.Cols.DATE, Hero.getDate().getTime());
        values.put(HeroTable.Cols.SOLVED, Hero.isSolved() ? 1 : 0);
        values.put(HeroTable.Cols.SUSPECT, Hero.getSuspect());

        return values;
    }

    public void deleteHero(Hero Hero) {
        mDatabase.delete(HeroTable.NAME,
                HeroTable.Cols.UUID + " = ? ",
                new String[]{Hero.getId().toString()});
        ;
    }
}