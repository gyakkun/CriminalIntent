package moe.nyamori.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.FloatRange;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.FileNameMap;
import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity
implements CrimeFragment.Callbacks{

    private static final String EXTRA_CRIME_ID = "moe.nyamori.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context pacakgeContext, UUID crimeId) {
        Intent intent = new Intent(pacakgeContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int ctr = 0; ctr < mCrimes.size(); ctr++) {
            if (mCrimes.get(ctr).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(ctr);
                break;
            }
        }


    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
