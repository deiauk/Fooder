package fooder.deividas.com.fooder.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import fooder.deividas.com.fooder.MyApplication;
import fooder.deividas.com.fooder.MySampleFabFragment;
import fooder.deividas.com.fooder.R;
import fooder.deividas.com.fooder.activities.DetailAdditiveActivity;
import fooder.deividas.com.fooder.adapters.FoodAdditivesAdapter;
import fooder.deividas.com.fooder.database.models.FoodAdditive;
import fooder.deividas.com.fooder.events.AdditiveClickEvent;
import io.realm.Case;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodAdditivesListFragment extends Fragment {
    private Realm realm;
    private RecyclerView recyclerView;
    private FoodAdditivesAdapter adapter;

    public FoodAdditivesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_additives_list, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Maisto priedų sąrašas");

        realm = ((MyApplication) getActivity().getApplicationContext()).getRealm();

        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySampleFabFragment dialogFrag = MySampleFabFragment.newInstance();
                dialogFrag.setParentFab(fab);
                dialogFrag.show(getFragmentManager(), dialogFrag.getTag());
            }
        });

        List<FoodAdditive> foodAdditives = ((MyApplication) getActivity().getApplicationContext()).getDataList();
        adapter = new FoodAdditivesAdapter(foodAdditives);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //HorizontalDividerItemDecoration.Builder(context).build();
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onAdditiveClick(AdditiveClickEvent obj) {
        Intent intent = new Intent(getContext(), DetailAdditiveActivity.class);
        intent.putExtra(DetailAdditiveActivity.ID, obj.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                List<FoodAdditive> foodAdditives = realm.where(FoodAdditive.class)
                        .contains("name", text, Case.INSENSITIVE)
                        .or()
                        .contains("number", text, Case.INSENSITIVE)
                        .findAll();
                adapter.setList(foodAdditives);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }
}