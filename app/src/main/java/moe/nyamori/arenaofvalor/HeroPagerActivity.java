package moe.nyamori.arenaofvalor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class HeroPagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID = "moe.nyamori.arenaofvalor.crime_id";

    private ViewPager mViewPager;
    private List<Hero> mHeros;

    public static Intent newIntent(Context pacakgeContext, UUID crimeId) {
        Intent intent = new Intent(pacakgeContext, HeroPagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        mHeros = HeroLab.get(this).getHeros();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Hero crime = mHeros.get(position);
                return HeroFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mHeros.size();
            }
        });

        for (int ctr = 0; ctr < mHeros.size(); ctr++) {
            if (mHeros.get(ctr).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(ctr);
                break;
            }
        }


    }
}
