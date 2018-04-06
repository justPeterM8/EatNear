package students.polsl.eatnear.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import students.polsl.eatnear.R;
import students.polsl.eatnear.RestaurantsMainAdapter;
import students.polsl.eatnear.databinding.FragmentTabBinding;
import students.polsl.eatnear.fake_data.FakeDataCreator;

public class AllRestaurantsFragment extends Fragment implements RestaurantsMainAdapter.RestaurantTileListener{
    private Context appContext;
    private RecyclerView mRecyclerView;
    private RestaurantsMainAdapter mRestaurantsMainAdapter;
    private FragmentTabBinding fragmentTabBinding;

    public AllRestaurantsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        appContext = getActivity().getApplicationContext();
        mRecyclerView = rootView.findViewById(R.id.restaurant_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(appContext));
        mRestaurantsMainAdapter = new RestaurantsMainAdapter(this, appContext, FakeDataCreator.createRestaurantDataList(20));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRestaurantsMainAdapter);
        return rootView;
    }

    @Override
    public void onClickAction(View view) {
        Toast.makeText(appContext, "Click registered on tile: " + view.getTag(), Toast.LENGTH_SHORT).show();
    }
}
