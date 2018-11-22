package database.HeroDbSchema;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import database.HeroDbSchema.HeroDbSchema.HeroTable;
import moe.nyamori.arenaofvalor.Hero;

public class HeroCursorWrapper extends CursorWrapper {
    public HeroCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Hero getHero() {
        String uuidString = getString(getColumnIndex(HeroTable.Cols.UUID));
        String title = getString(getColumnIndex(HeroTable.Cols.TITLE));
        long date = getLong(getColumnIndex(HeroTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(HeroTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(HeroTable.Cols.SUSPECT));

        Hero hero = new Hero(UUID.fromString(uuidString));
        hero.setTitle(title);
        hero.setDate(new Date(date));
        hero.setSolved(isSolved != 0);
        hero.setSuspect(suspect);

        return hero;
    }
}
