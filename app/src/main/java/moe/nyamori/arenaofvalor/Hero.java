package moe.nyamori.arenaofvalor;

import java.util.Date;
import java.util.UUID;

public class Hero {

    private UUID mId;
    private String mName;
    private String mNickname;
    private String mPosition;
    private int mLiveness = 0;
    private int mAttack = 0;
    private int mAffection = 0;
    private int mHardness = 0;
    private boolean mStarred;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        mNickname = nickname;
    }

    public String getPosition() {
        return mPosition;
    }

    public void setPosition(String position) {
        mPosition = position;
    }

    public int getLiveness() {
        return mLiveness;
    }

    public void setLiveness(int liveness) {
        mLiveness = liveness;
    }

    public int getAttack() {
        return mAttack;
    }

    public void setAttack(int attack) {
        mAttack = attack;
    }

    public int getAffection() {
        return mAffection;
    }

    public void setAffection(int affection) {
        mAffection = affection;
    }

    public int getHardness() {
        return mHardness;
    }

    public void setHardness(int hardness) {
        mHardness = hardness;
    }




    public Hero() {
        mId = UUID.randomUUID();
    }

    public Hero(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public boolean isStarred() {
        return mStarred;
    }

    public void setStarred(boolean solved) {
        mStarred = solved;
    }

    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
