package com.krld.diet.meals.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krld.diet.R;
import com.krld.diet.common.models.Meal;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.functions.Func2;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.AbstractViewHolder> {

    private List<ListItem> items;

    public MealsAdapter() {
        super();
        items = new ArrayList<>();
        items.add(new ListItem(Type.HEADER));
        items.add(new ListItem(Type.MEAL, Meal.BREAKFAST));
        items.add(new ListItem(Type.MEAL, Meal.TIFFIN));
        items.add(new ListItem(Type.MEAL, Meal.LUNCH));
        items.add(new ListItem(Type.MEAL, Meal.AFTERNOON_SNACK));
        items.add(new ListItem(Type.MEAL, Meal.DINNER));
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
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type.id;
    }

    private static class MealViewHolder extends AbstractViewHolder {
        public MealViewHolder(View itemView, MealsAdapter adapter) {
            super(itemView, adapter);
        }
    }

    private static class FooterViewHolder extends AbstractViewHolder {
        public FooterViewHolder(View itemView, MealsAdapter adapter) {
            super(itemView, adapter);
        }
    }

    private static class HeaderViewHolder extends AbstractViewHolder {
        public HeaderViewHolder(View itemView, MealsAdapter adapter) {
            super(itemView, adapter);
        }
    }

    public static class AbstractViewHolder extends RecyclerView.ViewHolder {
        private final MealsAdapter adapter;
        private ListItem listItem;
        private int position;

        public AbstractViewHolder(View itemView, MealsAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            ButterKnife.bind(this, itemView);
        }

        public void bind(ListItem listItem, int position) {

            this.listItem = listItem;
            this.position = position;
        }
    }

    private class ListItem {
        private Type type;
        private Meal meal;

        public ListItem(Type type) {

            this.type = type;
        }

        public ListItem(Type type, Meal meal) {

            this.type = type;
            this.meal = meal;
        }
    }

    private enum Type {
        MEAL(R.layout.meals_li_header, MealViewHolder::new),
        FOOTER(R.layout.meals_li_header, FooterViewHolder::new),
        HEADER(R.layout.meals_li_header, HeaderViewHolder::new);
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
