package com.puchd.puplanner;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;


public class AttendanceFragment extends Fragment
{
    AttendanceDatabase attendanceDatabase;
    SharedPreferences sharedPreferences;
    ArrayList<String> SubjectData = new ArrayList<>();
    ArrayList<String> Subjects = new ArrayList<>();
    RecyclerView recyclerView;
    AttendanceAdapter attendanceAdapter;
    ProgressWheel progressWheel;
    List<AttendanceCards> list;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_attendance, container, false);
    }

    private void prepareCards()
    {
        for(String name:Subjects)
        {
            AttendanceCards attendanceCards = new AttendanceCards(name);
            list.add(attendanceCards);
            attendanceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Attendance");
        progressWheel = (ProgressWheel)view.findViewById(R.id.progress_wheel);
        progressWheel.setSpinSpeed((float) 2);
        progressWheel.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                attendanceDatabase = new AttendanceDatabase(getActivity());
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SubjectData = attendanceDatabase.GetData(sharedPreferences.getString("DefaultSchedule",""));
                for(String SubjectD:SubjectData)
                {
                    String SubjectTemp[] = SubjectD.split("_");
                    Subjects.add(SubjectTemp[0]);
                }
                recyclerView = (RecyclerView)view.findViewById(R.id.AttendanceCardRecyclerView);
                Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                recyclerView.startAnimation(fadeInAnimation);
                list = new ArrayList<>();
                attendanceAdapter = new AttendanceAdapter(getActivity(),list);
                prepareCards();
                progressWheel.setVisibility(View.GONE);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new AttendanceFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(attendanceAdapter);
            }
        }, 380);
    }

    private int dpToPx(int dp)
    {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
