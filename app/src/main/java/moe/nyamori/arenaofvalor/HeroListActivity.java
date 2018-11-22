package moe.nyamori.arenaofvalor;

import android.support.v4.app.Fragment;

public class HeroListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new HeroListFragment();
    }
}
