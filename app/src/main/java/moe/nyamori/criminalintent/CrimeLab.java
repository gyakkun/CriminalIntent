package moe.nyamori.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private  static  CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab==null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();

        //For test only

        for(int ctr=0; ctr <2 ; ctr++){
            Crime crime = new Crime();
            crime.setTitle("Test Crime #" + ctr);
            crime.setSolved(ctr%2==0);
            mCrimes.add(crime);
        }

    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime crime:mCrimes){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public void deleteCrime(Crime crime){
        mCrimes.remove(crime);
    }
}
