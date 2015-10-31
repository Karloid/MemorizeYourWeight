package com.krld.diet.base.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.krld.diet.R;
import com.krld.diet.base.helpers.NavigationItemsHelper;
import com.krld.diet.base.models.NavigationMenuItem;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.AbstractViewHolder> {

    private List<NavigationMenuItem> mItems;
    private HeaderViewHolder headerVh;

    public NavigationAdapter(NavigationItemsHelper.NavigationItemsCallback callback, Context context) {
        super();
        mItems = NavigationItemsHelper.createItems(callback, context);
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NavigationMenuItem.Type type = NavigationMenuItem.Type.getById(viewType);
        assert type != null;
        View itemView = inflater.inflate(type.layoutId, parent, false);
        AbstractViewHolder vh = type.createViewHolder.call(itemView);
        //hacky way
        if (vh instanceof HeaderViewHolder) {
            headerVh = (HeaderViewHolder) vh;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        NavigationMenuItem item = mItems.get(position);

        holder.bind(item);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type.id;
    }


    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    static public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
        protected NavigationMenuItem mItem;

        public AbstractViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(NavigationMenuItem item) {
            mItem = item;
        }
    }

    static public class HeaderViewHolder extends AbstractViewHolder {
        private final TextView mUserNameText;
        private final TextView mEmailText;
        private final ImageView mUserImage;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mUserNameText = (TextView) itemView.findViewById(R.id.navigation_username);
            mEmailText = (TextView) itemView.findViewById(R.id.navigation_email);
            mUserImage = (ImageView) itemView.findViewById(android.R.id.icon1);

            itemView.setOnClickListener(v -> mItem.onClick.call());
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void bind(NavigationMenuItem item) {
            super.bind(item);
   /*         LoginResponse loginResponse = FuzdAccount.getLoginResponse(); //TODO logout when account removed
            if (loginResponse != null) {
                UserInfoResponse.UserInfoFull userinfo = loginResponse.getUserInfo();
                updateValues(userinfo);
            }*/
        }

       /* public void updateValues(UserInfoResponse.UserInfoFull userinfo) {
            if (mUserNameText == null) return;
            mUserNameText.setText(userinfo.username);
            mEmailText.setText(userinfo.email);

            Application.getInstance().getMainImageLoader().get(userinfo.small_ava, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    mUserImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    mUserImage.setImageResource(R.drawable.avatar_unknown_small);
                    FLog.e(getClass().getSimpleName() + " update avatar image error " + error);
                }
            });
        }*/
    }

    static public class ItemViewHolder extends AbstractViewHolder {
        private final TextView mNotificationCount;
        private final TextView mLabel;
        private final ImageView mIcon;
        private Subscription notificationSubscribe;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
            mLabel = (TextView) itemView.findViewById(android.R.id.text1);
            mNotificationCount = (TextView) itemView.findViewById(R.id.notifications_count);
            itemView.setOnClickListener(v -> mItem.onClick.call());
        }

        public void bind(NavigationMenuItem item) {
            super.bind(item);
            mIcon.setImageResource(mItem.imageResourceId);
            mLabel.setText(mItem.title);

            if (notificationSubscribe != null && !notificationSubscribe.isUnsubscribed()) {
                notificationSubscribe.unsubscribe();
            }
            mNotificationCount.setVisibility(View.INVISIBLE);
            if (item.notificationsObs != null) {
                notificationSubscribe = item.notificationsObs.debounce(300, TimeUnit.MILLISECONDS, Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                count -> {
                                    mNotificationCount.setVisibility(count > 0 ? View.VISIBLE : View.INVISIBLE);
                                    mNotificationCount.setText("" + count);
                                });
            }
        }
    }

    static public class SeparatorViewHolder extends AbstractViewHolder {
        public SeparatorViewHolder(View itemView) {
            super(itemView);
        }
    }
}
