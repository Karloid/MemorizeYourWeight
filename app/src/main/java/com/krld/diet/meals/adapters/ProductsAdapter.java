package com.krld.diet.meals.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.krld.diet.R;
import com.krld.diet.base.fragments.BaseFragment;
import com.krld.diet.common.helpers.DataHelper;
import com.krld.diet.common.helpers.DrawableHelper;
import com.krld.diet.common.helpers.FLog;
import com.krld.diet.common.models.MealEnumeration;
import com.krld.diet.common.models.Product;
import com.krld.diet.meals.fragments.MealFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

import static rx.android.schedulers.AndroidSchedulers.*;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.AbstractViewHolder> {
    private final MealFragment fragment;
    private final ListItem addNewProductListItem;
    private final DataHelper dataHelper;
    private final CompositeSubscription compositeSubscription;
    private List<ListItem> items;
    private final MealEnumeration mealEnumeration;

    public ProductsAdapter(MealFragment mealFragment) {
        fragment = mealFragment;
        compositeSubscription = fragment.getCompositeSubscriptionCreated();
        items = new ArrayList<>();
        mealEnumeration = fragment.getMeal();
        dataHelper = DataHelper.getInstance();
        dataHelper.getMealObs(mealEnumeration);

        addNewProductListItem = new ListItem(Type.ADD_NEW_PRODUCT);

        items.add(new ListItem(Type.HEADER));
        items.add(new ListItem(Type.DIVIDER));
        items.add(addNewProductListItem);
        items.add(new ListItem(Type.DIVIDER));
        items.add(new ListItem(Type.FOOTER));

        compositeSubscription.add(dataHelper.getMealObs(mealEnumeration)
                .observeOn(mainThread())
                .flatMap(m -> Observable.from(m.products))
                .distinct()
                .subscribe(id -> {
                    int index = items.indexOf(addNewProductListItem);
                    items.add(index, new ListItem(Type.DIVIDER));
                    items.add(index, new ListItem(Type.PRODUCT, id));
                    notifyItemRangeInserted(index, 2);
                }));

        compositeSubscription.add(dataHelper.getDeletedProductsObs()
                .filter(product -> mealEnumeration.equals(product.mealEnumeration))
                .observeOn(mainThread())
                .subscribe(p -> {
                    int i = 0;
                    for (ListItem li : items) {
                        if (((Integer) p.id).equals(li.productId)) {
                            items.remove(i);
                            items.remove(i);

                            notifyItemRangeRemoved(i, 2);
                            break;
                        }
                        i++;
                    }
                }));
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
        dataHelper.addNewProduct(mealEnumeration);
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    private void deleteProduct(Product product) {
        dataHelper.deleteProduct(mealEnumeration, product);
    }

    private static class AddNewProductViewHolder extends AbstractViewHolder {
        public AddNewProductViewHolder(View itemView, ProductsAdapter adapter) {
            super(itemView, adapter);
            itemView.setOnClickListener(v -> adapter.createNewProduct(listItem));
        }

    }


    //=============================
    //=============================
    //=============================
    //=============================
    //=============================
    //=============================
    //=============================
    //=============================


    public static class AbstractViewHolder extends RecyclerView.ViewHolder {

        protected final ProductsAdapter adapter;
        protected ListItem listItem;

        public AbstractViewHolder(View itemView, ProductsAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            ButterKnife.bind(this, itemView);
        }

        public void onBind(ListItem listItem) {
            this.listItem = listItem;
        }

    }

    public static class ProductViewHolder extends AbstractViewHolder {

        @Bind(R.id.product)
        EditText productName;

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


        @Bind(R.id.delete_button)
        ImageButton deleteButton;

        private Subscription subscribe;

        private Product product;

        public ProductViewHolder(View itemView, ProductsAdapter adapter) {
            super(itemView, adapter);

            deleteButton.setImageDrawable(DrawableHelper.getTintedDrawable(R.drawable.ic_remove_circle_outline_black_24dp, R.color.grey_3));
        }

        @Override
        public void onBind(ListItem listItem) {
            super.onBind(listItem);

            if (subscribe != null) {
                subscribe.unsubscribe();
            }
            subscribe = adapter.dataHelper
                    .getProductObs(listItem.productId, adapter.mealEnumeration)
                    .observeOn(mainThread())
                    .subscribe(product -> {
                        this.product = product;
                        productName.setText(product.name);
                        //TODO
                    });
            adapter.compositeSubscription.add(subscribe);
        }

        @OnClick(R.id.delete_button)
        void onDeleteClick() {
            adapter.deleteProduct(product);
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
        private Integer productId;

        public ListItem(Type type) {

            this.type = type;
        }

        public ListItem(Type type, Integer productId) {

            this.type = type;
            this.productId = productId;
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
