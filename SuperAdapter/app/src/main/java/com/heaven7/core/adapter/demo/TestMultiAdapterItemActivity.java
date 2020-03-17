package com.heaven7.core.adapter.demo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heaven7.adapter.AdapterItemFactory;
import com.heaven7.adapter.BaseAdapterItem;
import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.QuickRecyclerViewAdapter3;
import com.heaven7.core.util.Toaster;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * test multi adapter item.
 */
public class TestMultiAdapterItemActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_adapter_item);
        ButterKnife.bind(this);

        setAdapter();
    }

    private void setAdapter() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        QuickRecyclerViewAdapter3<Item> adapter = new QuickRecyclerViewAdapter3<Item>(createItems());
        adapter.setAdapterItemFactory(new AdapterItemFactory<Item>() {
            @Override
            public BaseAdapterItem<Item> createAdapterItem(Item item) {
                //System.out.println("onCreateAdapterItem: " + item);
                if(item.text != null){
                    return new TextItem();
                }
                return new ImageItem();
            }
            @Override
            public String getItemTypeId(Item data) {
                return data.text != null ? "text" : "img";
            }
        });
        mRv.setAdapter(adapter);
    }

    private static List<Item> createItems() {
        List<Item> list = new ArrayList<>();
        for (int i = 0 ; i < 60 ; i ++){
            if(i % 3 == 0){
                list.add(new Item(null, R.mipmap.ic_launcher_round));
            }else {
                list.add(new Item("heaven7__" + i, 0));
            }
        }
        return list;
    }

    private static class Item extends BaseSelector {
        final String text;
        final int imageRes;
        public Item(String text, int imageRes) {
            this.text = text;
            this.imageRes = imageRes;
        }
    }
    public static class TextItem extends BaseAdapterItem<Item> {
        @BindView(R.id.tv)
        TextView mTv;
        @Override
        public int getLayoutId() {
            return R.layout.item_test_adapter_item1;
        }
        @Override
        public void bindView(View view) {
            ButterKnife.bind(this, view);
        }
        @Override
        public void onBindData(Context context, int position, Item data) {

            mTv.setText(data.text);
            getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toaster.show(context, "text: position = " + position);
                }
            });
        }
    }

    public static class ImageItem extends BaseAdapterItem<Item> {
        @BindView(android.R.id.icon)
        ImageView iv;
        @Override
        public int getLayoutId() {
            return android.R.layout.activity_list_item;
        }
        @Override
        public void bindView(View view) {
            ButterKnife.bind(this, view);
        }
        @Override
        public void onBindData(Context context, int position, Item data) {

            iv.setImageResource(data.imageRes);
            getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toaster.show(context, "image: " + data.text + "position = " + position);
                }
            });
        }
    }
}
