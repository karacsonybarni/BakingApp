package com.udacity.bakingapp.ui.steplistview;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.ui.RecipeViewModel;
import com.udacity.bakingapp.ui.widget.IngredientsWidgetProvider;

import java.util.Objects;

public class StepListFragment extends Fragment {

    private RecipeViewModel viewModel;
    private StepListAdapter adapter;
    private OnSelectionListener selectionListener;
    private int savedStepPosition;

    @Nullable private Recipe recipe;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        viewModel = getViewModel();
        initAdapter();
        updateRecipe();
        return initRecyclerView(inflater, container);
    }

    private RecipeViewModel getViewModel() {
        return ViewModelProviders.of(getNonNullActivity()).get(RecipeViewModel.class);
    }

    @NonNull
    private FragmentActivity getNonNullActivity() {
        return Objects.requireNonNull(getActivity());
    }

    private void initAdapter() {
        adapter = new StepListAdapter(getContext(), selectionListener);
        adapter.updateStepPosition(savedStepPosition);
    }

    private void updateRecipe() {
        viewModel.getRecipe().observe(this, this::updateRecipe);
    }

    private void updateRecipe(Recipe recipe) {
        this.recipe = recipe;
        adapter.update(recipe);
    }

    private RecyclerView initRecyclerView(LayoutInflater inflater, ViewGroup container) {
        RecyclerView recyclerView = inflateRecyclerView(inflater, container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addDivider(recyclerView);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    private RecyclerView inflateRecyclerView(LayoutInflater inflater, ViewGroup container) {
        return (RecyclerView) inflater
                .inflate(R.layout.fragment_step_list, container, false);
    }

    private void addDivider(RecyclerView recyclerView) {
        DividerItemDecoration divider =
                new DividerItemDecoration(getNonNullContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
    }

    @NonNull
    private Context getNonNullContext() {
        return Objects.requireNonNull(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selectionListener = (OnSelectionListener) context;
        } catch (ClassCastException ignored) {
            throw new ClassCastException(context + "must implement OnSelectionListener");
        }
    }

    void updateStepPosition(int stepPosition) {
        savedStepPosition = stepPosition;
        if (adapter != null) {
            adapter.updateStepPosition(stepPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recipe != null) {
            viewModel.updateLastViewed(recipe);
            updateWidgets();
        }
    }

    private void updateWidgets() {
        Context context = getNonNullContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName provider = new ComponentName(context, IngredientsWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(provider);
        IngredientsWidgetProvider.updateWidgets(context, appWidgetManager, appWidgetIds, recipe);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients);
    }

    @Override
    public void onDestroy() {
        viewModel.getRecipe().removeObservers(this);
        super.onDestroy();
    }
}
