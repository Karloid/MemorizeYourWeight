package com.krld.diet.meals.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krld.diet.R;
import com.krld.diet.base.fragments.BaseFragment;
import com.krld.diet.common.helpers.DataHelper;
import com.krld.diet.common.helpers.IntentHelper;
import com.krld.diet.common.models.MealEnumeration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Func2;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.AbstractViewHolder> {

    private List<ListItem> items;
    private BaseFragment fragment;

    public MealsAdapter(BaseFragment fragment) {
        super();
        this.fragment = fragment;
        items = new ArrayList<>();
        items.add(new ListItem(Type.HEADER));
        items.add(new ListItem(Type.DIVIDER));
        items.add(new ListItem(Type.MEAL, MealEnumeration.BREAKFAST));
        items.add(new ListItem(Type.DIVIDER));
        items.add(new ListItem(Type.MEAL, MealEnumeration.TIFFIN));
        items.add(new ListItem(Type.DIVIDER));
        items.add(new ListItem(Type.MEAL, MealEnumeration.LUNCH));
        items.add(new ListItem(Type.DIVIDER));
        items.add(new ListItem(Type.MEAL, MealEnumeration.AFTERNOON_SNACK));
        items.add(new ListItem(Type.DIVIDER));
        items.add(new ListItem(Type.MEAL, MealEnumeration.DINNER));
        items.add(new ListItem(Type.DIVIDER));
        items.add(new ListItem(Type.FOOTER));
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Type type = Type.findById(viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(type.layoutId, parent, false);
        return type.createVH.call(view, this);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.onBind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type.id;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    protected static class MealViewHolder extends AbstractViewHolder {
        @Bind(R.id.meal)
        TextView mealView;

        @Bind(R.id.proteins)
        TextView proteinsView;

        @Bind(R.id.fats)
        TextView fatsView;

        @Bind(R.id.carbs)
        TextView carbsView;

        @Bind(R.id.calories)
        TextView caloriesView;

        public MealViewHolder(View itemView, MealsAdapter adapter) {
            super(itemView, adapter);
            itemView.setOnClickListener(v -> IntentHelper.showMeal(adapter.getFragment().getActivity(), listItem.mealEnumeration));
        }

        @Override
        public void onBind(ListItem listItem, int position) {
            super.onBind(listItem, position);
            mealView.setText(listItem.mealEnumeration.nameLocResId);
            proteinsView.setText("100/300");
            fatsView.setText("200/500");
            carbsView.setText("300/300");
            caloriesView.setText("400/300");
        }
    }

    protected static class FooterViewHolder extends AbstractViewHolder {

        @Bind(R.id.totals)
        TextView totalsView;

        @Bind(R.id.proteins)
        TextView proteinsView;

        @Bind(R.id.fats)
        TextView fatsView;

        @Bind(R.id.carbs)
        TextView carbsView;

        @Bind(R.id.calories)
        TextView caloriesView;

        public FooterViewHolder(View itemView, MealsAdapter adapter) {
            super(itemView, adapter);
        }

        @Override
        public void onBind(ListItem listItem, int position) {
            super.onBind(listItem, position);

            DataHelper.getInstance().getMealSummary();
        }
    }

    private static class HeaderViewHolder extends AbstractViewHolder {
        public HeaderViewHolder(View itemView, MealsAdapter adapter) {
            super(itemView, adapter);
        }
    }

    public static class AbstractViewHolder extends RecyclerView.ViewHolder {
        protected final MealsAdapter adapter;
        protected ListItem listItem;
        protected int position;

        public AbstractViewHolder(View itemView, MealsAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            ButterKnife.bind(this, itemView);
        }

        public void onBind(ListItem listItem, int position) {
            this.listItem = listItem;
            this.position = position;
        }
    }

    private class ListItem {
        private Type type;
        private MealEnumeration mealEnumeration;

        public ListItem(Type type) {

            this.type = type;
        }

        public ListItem(Type type, MealEnumeration mealEnumeration) {

            this.type = type;
            this.mealEnumeration = mealEnumeration;
        }
    }

    private enum Type {
        HEADER(R.layout.meals_li_header, HeaderViewHolder::new),
        MEAL(R.layout.meals_li_meal, MealViewHolder::new),
        FOOTER(R.layout.meals_li_footer, FooterViewHolder::new),
        DIVIDER(R.layout.meals_li_divider, AbstractViewHolder::new),;
        public Func2<View, MealsAdapter, AbstractViewHolder> createVH;
        public int id;
        public int layoutId;

        Type(int layoutId, Func2<View, MealsAdapter, AbstractViewHolder> createVH) {
            this.createVH = createVH;
            this.layoutId = layoutId;
            id = IdHolder.id++;
        }

        public static Type findById(int viewType) {
            for (Type e : values()) {
                if (e.id == viewType) {
                    return e;
                }
            }
            return null;
        }

        static private class IdHolder {
            public static int id;
        }
    }
}
