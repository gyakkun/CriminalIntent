package moe.nyamori.arenaofvalor;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class HeroListFragment extends Fragment {
    private RecyclerView mHeroRecyclerView;
    private HeroAdapter mAdapter;
    private boolean mSubtitleVisible;
    private EditText mEditText;

    private String LOG_TAG = "HeroListFragment";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,
                container,
                false);


        mHeroRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);

        mHeroRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mEditText = (EditText) view.findViewById(R.id.crime_edit_text);

        if(savedInstanceState!=null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Hero crime = new Hero();
                HeroLab.get(getActivity()).addHero(crime);
                Intent intent = HeroPagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        HeroLab crimeLab = HeroLab.get(getActivity());
        int crimeCount = crimeLab.getHeros().size();
        @SuppressLint("StringFormatMatches")
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        HeroLab crimeLab = HeroLab.get(getActivity());
        List<Hero> crimes = crimeLab.getHeros();

        if (mAdapter == null) {
            mAdapter = new HeroAdapter(crimes);
            mHeroRecyclerView.setAdapter(mAdapter);
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mAdapter.getFilter().filter(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            mAdapter.setHeros(crimes);

            mAdapter.notifyDataSetChanged();

            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mAdapter.getFilter().filter(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            mAdapter.notifyDataSetChanged();


        }

        updateSubtitle();
    }

    private class HeroHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mImageView;
        private Hero mHero;

        public HeroHolder(LayoutInflater inflater, ViewGroup parent) {

            //inflater.inflate() returns a view instance
            super(inflater.inflate(R.layout.list_item_crime,
                    parent,
                    false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Hero crime) {
            mHero = crime;
            DateFormat dateFormat = DateFormat.getDateInstance();
            //Log.i(LOG_TAG, "Hero " + mHero.getId().toString() + " Bound.");
            mTitleTextView.setText(mHero.getTitle());
            mDateTextView.setText(dateFormat.format(crime.getDate()));
            mImageView.setVisibility(mHero.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
//            Intent intent = HeroActivity.newIntent(getActivity(), mHero.getId());
            Intent intent = HeroPagerActivity.newIntent(getActivity(), mHero.getId());
            startActivity(intent);
        }
    }

    private class HeroAdapter extends RecyclerView.Adapter<HeroHolder>
    implements Filterable{

        private List<Hero> mHeros;
        private List<Hero> mFilteredHeros;

        public HeroAdapter(List<Hero> crimes) {
            mHeros = crimes;
            mFilteredHeros = crimes;
        }


        @Override
        public HeroHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Log.i(LOG_TAG, "onCreateViewHolder() called.");
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new HeroHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(HeroHolder holder, int position) {
            Hero crime = mFilteredHeros.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mFilteredHeros.size();
        }

        //Set the mHeros received from database query (null, null)
        //it's a snapshot of the whole database
        public void setHeros(List<Hero> crimes){
            mFilteredHeros = crimes;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if(charString.isEmpty()){
                        mFilteredHeros = mHeros;
                    } else {
                        List<Hero> filteringHeros = new ArrayList<>();
                        for(Hero tmpHero : mHeros){
                            if (tmpHero.getTitle().contains(charString)) {
                                filteringHeros.add(tmpHero);
                            }
                        }

                        mFilteredHeros = filteringHeros;
                    }

                    FilterResults results = new FilterResults();
                    results.values = mFilteredHeros;
                    return results;

                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilteredHeros = (ArrayList<Hero>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
}
