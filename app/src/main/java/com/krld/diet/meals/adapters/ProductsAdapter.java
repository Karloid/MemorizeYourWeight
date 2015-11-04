package com.krld.diet.meals.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.krld.diet.R;
import com.krld.diet.base.fragments.BaseFragment;
import com.krld.diet.common.helpers.DataHelper;
import com.krld.diet.common.models.Product;
import com.krld.diet.meals.fragments.MealFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Func2;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.AbstractViewHolder> {
    private final MealFragment fragment;
    private List<ListItem> items;

    public ProductsAdapter(MealFragment mealFragment) {
        fragment = mealFragment;
        items = new ArrayList<>();
        items.add(new ListItem(Type.HEADER));
        items.add(new ListItem(Type.DIVIDER));
        items.add(new ListItem(Type.ADD_NEW_PRODUCT));
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
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type.id;
    }

    private void createNewProduct(ListItem listItem) {
        int newProductIndex = items.indexOf(listItem);
        items.add(newProductIndex, new ListItem(Type.DIVIDER));
        items.add(newProductIndex, new ListItem(Type.PRODUCT, DataHelper.getInstance().createNewProduct()));
        notifyItemRangeInserted(newProductIndex, 2);
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public static class AbstractViewHolder extends RecyclerView.ViewHolder {

        protected ListItem listItem;

        public AbstractViewHolder(View itemView, ProductsAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(ListItem listItem) {
            this.listItem = listItem;
        }

    }

    public static class ProductViewHolder extends AbstractViewHolder {

        @Bind(R.id.product)
        EditText productView;

        @Bind(R.id.proteins)
        EditText proteinsView;

        @Bind(R.id.fats)
        EditText fatsView;

        @Bind(R.id.carbs)
        EditText carbsView;

        @Bind(R.id.weight)
        EditText weightView;

        @Bind(R.id.calories)
        TextView caloriesView;

        public ProductViewHolder(View itemView, ProductsAdapter adapter) {
            super(itemView, adapter);
        }

        @Override
        public void onBind(ListItem listItem) {
            super.onBind(listItem);

            productView.setText(listItem.product.name);
        }
    }

    private static class AddNewProductViewHolder extends AbstractViewHolder {
        public AddNewProductViewHolder(View itemView, ProductsAdapter adapter) {
            super(itemView, adapter);
            itemView.setOnClickListener(v -> adapter.createNewProduct(listItem));
        }

    }

    private static class FooterViewHolder extends AbstractViewHolder {
        public FooterViewHolder(View itemView, ProductsAdapter adapter) {
            super(itemView, adapter);
        }
        //TODO
    }


    private class ListItem {
        private Type type;
        private Product product;

        public ListItem(Type type) {

            this.type = type;
        }

        public ListItem(Type type, Product product) {

            this.type = type;
            this.product = product;
        }
    }

    private enum Type {
        HEADER(R.layout.meal_li_header, AbstractViewHolder::new),
        PRODUCT(R.layout.meal_li_product, ProductViewHolder::new),
        ADD_NEW_PRODUCT(R.layout.meal_li_new_product, AddNewProductViewHolder::new),
        FOOTER(R.layout.meal_li_footer, FooterViewHolder::new),
        DIVIDER(R.layout.meals_li_divider, AbstractViewHolder::new),;

        public Func2<View, ProductsAdapter, AbstractViewHolder> createVH;
        public int id;
        public int layoutId;

        Type(int layoutId, Func2<View, ProductsAdapter, AbstractViewHolder> createVH) {
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
