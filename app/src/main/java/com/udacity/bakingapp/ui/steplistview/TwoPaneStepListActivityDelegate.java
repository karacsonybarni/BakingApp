package com.udacity.bakingapp.ui.steplistview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.ui.stepview.StepFragment;

class TwoPaneStepListActivityDelegate implements OnSelectionListener {

    private static final String STEP_POSITION = "stepPosition";

    private StepListActivity activity;
    private StepFragment stepFragment;

    private int stepPosition;

    TwoPaneStepListActivityDelegate(StepListActivity activity) {
        this.activity = activity;
    }

    void setup(Bundle savedInstanceState) {
        stepPosition = getStepPosition(savedInstanceState);
        initStepListFragment();
        initStepFragment();
    }

    private int getStepPosition(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return savedInstanceState.getInt(STEP_POSITION, 0);
        }
        return 0;
    }

    private void initStepListFragment() {
        StepListFragment stepListFragment = (StepListFragment) activity
                .getSupportFragmentManager()
                .findFragmentById(R.id.stepListFragmentContainer);
        if (stepListFragment == null) {
            stepListFragment = new StepListFragment();
            addFragment(R.id.stepListFragmentContainer, stepListFragment);
        }
        stepListFragment.setStepPosition(stepPosition);
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment)
                .commit();
    }

    private void initStepFragment() {
        stepFragment =
                (StepFragment) activity
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.stepFragmentContainer);
        if (stepFragment == null) {
            stepFragment = new StepFragment();
            addFragment(R.id.stepFragmentContainer, stepFragment);
        }
        stepFragment.setStepPosition(stepPosition);
    }

    void onSaveInstanceState(Bundle outState) {
        outState.putInt(STEP_POSITION, stepPosition);
    }

    @Override
    public void onSelect(int position) {
        stepPosition = position;
        stepFragment.setStepPosition(stepPosition);
        stepFragment.updateViews();
    }
}
