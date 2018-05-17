package students.polsl.eatnear.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import students.polsl.eatnear.AddRestaurantActivity;
import students.polsl.eatnear.MapActivity;
import students.polsl.eatnear.R;
import students.polsl.eatnear.RestaurantActivity;
import students.polsl.eatnear.RestaurantsMainAdapter;
import students.polsl.eatnear.fake_data.FakeRestaurantDataCreator;

public class AllRestaurantsFragment extends Fragment implements RestaurantsMainAdapter.RestaurantTileListener{
    private Context appContext;
    private RecyclerView mRecyclerView;
    private RestaurantsMainAdapter mRestaurantsMainAdapter;

    private FloatingActionButton mMenuActionButton;
    private FloatingActionButton mMapActionButton;
    private FloatingActionButton mAddActionButton;

    private boolean mIsFabOpen;

    public AllRestaurantsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        appContext = getActivity().getApplicationContext();

        mMenuActionButton = rootView.findViewById(R.id.fabMenu);
        mAddActionButton = rootView.findViewById(R.id.fabAdd);
        mMapActionButton = rootView.findViewById(R.id.fabMap);

        mRecyclerView = rootView.findViewById(R.id.restaurant_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(appContext));
        mRestaurantsMainAdapter = new RestaurantsMainAdapter(this, appContext, FakeRestaurantDataCreator.createRestaurantFakeDataList(20));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRestaurantsMainAdapter);

        mMenuActionButton.setOnClickListener(view -> {
            if(!mIsFabOpen){
                showFabenu();
            }else{
                closeFabenu();
            }
        });

        mAddActionButton.setOnClickListener(view -> {
            Intent addRestaurant = new Intent(appContext, AddRestaurantActivity.class);
            startActivity(addRestaurant);
        });

        mMapActionButton.setOnClickListener(view -> {
            Intent startMapActivity = new Intent(appContext, MapActivity.class);
            startActivity(startMapActivity);
        });

        return rootView;
    }

    private void showFabenu(){
        mIsFabOpen = true;
        mAddActionButton.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        mMapActionButton.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFabenu(){
        mIsFabOpen = false;
        mAddActionButton.animate().translationY(0);
        mMapActionButton.animate().translationY(0);
    }

    @Override
    public void onClickAction(View view) {
        //Toast.makeText(appContext, "Click registered on tile: " + view.getTag(RestaurantsMainAdapter.ID_KEY), Toast.LENGTH_SHORT).show();
        Intent startRestaurantActivity = new Intent(appContext, RestaurantActivity.class);
        startRestaurantActivity.putExtra("location", "" + view.getTag(R.id.addressTextView));
        startRestaurantActivity.putExtra("name", "" + view.getTag(R.id.restaurantNameTextView));
        startActivity(startRestaurantActivity);
    }
}
