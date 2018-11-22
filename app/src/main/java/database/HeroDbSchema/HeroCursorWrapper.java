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
        String name = getString(getColumnIndex(HeroTable.Cols.NAME));
        String nickname = getString(getColumnIndex(HeroTable.Cols.NICKNAME));
        String position = getString(getColumnIndex(HeroTable.Cols.POSITION));

        int liveness = getInt(getColumnIndex(HeroTable.Cols.LIVENESS));
        int attack = getInt(getColumnIndex(HeroTable.Cols.ATTACK));
        int affection = getInt(getColumnIndex(HeroTable.Cols.AFFECTION));
        int hardness = getInt(getColumnIndex(HeroTable.Cols.HARDNESS));

        int isStarred = getInt(getColumnIndex(HeroTable.Cols.STARRED));

        Hero hero = new Hero(UUID.fromString(uuidString));
        hero.setName(name);
        hero.setNickname(nickname);
        hero.setPosition(position);
        hero.setLiveness(liveness);
        hero.setAttack(attack);
        hero.setAffection(affection);
        hero.setHardness(hardness);
        hero.setStarred(isStarred != 0);

        return hero;
    }
}
