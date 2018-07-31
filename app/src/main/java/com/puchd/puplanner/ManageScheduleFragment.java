package com.puchd.puplanner;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageScheduleFragment extends Fragment
{
    RecyclerView recyclerView;
    ScheduleCardsAdapter scheduleCardsAdapter;
    List<ScheduleCards> list;
    String SnackbarString = "";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_manage_schedule, container, false);
        recyclerView = v.findViewById(R.id.ScheduleCardRecyclerView);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        recyclerView.startAnimation(fadeInAnimation);
        list = new ArrayList<>();
        scheduleCardsAdapter = new ScheduleCardsAdapter(getActivity(),list);
        prepareCards();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(scheduleCardsAdapter);
        String OldName;
        if(!Objects.requireNonNull(Objects.requireNonNull(getArguments()).getString("OldName")).equals(""))
        {
            OldName = getArguments().getString("OldName");
            String ActionString = getArguments().getString("Action");

            if(Objects.equals(ActionString, "DefaultDeleted"))SnackbarString = OldName+" deleted";
            if(Objects.equals(ActionString, "DefaultChanged"))SnackbarString = OldName+" set as default schedule";
            if(Objects.equals(ActionString, "DefaultRenamed")) SnackbarString = OldName+" renamed to "+getArguments().getString("NewScheduleName");
        }
        return v;
    }

    private void prepareCards()
    {
        NewScheduleDatabase newScheduleDatabase = new NewScheduleDatabase(getActivity());
        for(String name:newScheduleDatabase.GetTableNames())
        {
            ScheduleCards scheduleCards = new ScheduleCards(name);
            list.add(scheduleCards);
            scheduleCardsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Manage schedule");
        if(!SnackbarString.equals(""))
        {
            Snackbar snackbar = Snackbar.make(view,SnackbarString,Snackbar.LENGTH_LONG);
            View snack = snackbar.getView();
            snack.setBackgroundColor(Color.parseColor("#4052b5"));
            snackbar.show();
        }
    }

    private int dpToPx()
    {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
