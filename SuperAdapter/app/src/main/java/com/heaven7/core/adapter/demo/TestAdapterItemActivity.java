package com.heaven7.core.adapter.demo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heaven7.adapter.BaseAdapterItem;
import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.QuickRecyclerViewAdapter3;
import com.heaven7.core.util.Toaster;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * test simple adapter item.
 */
public class TestAdapterItemActivity extends AppCompatActivity {

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
        QuickRecyclerViewAdapter3<Item> adapter = new QuickRecyclerViewAdapter3<>(new PersonItem(), createItems());
        mRv.setAdapter(adapter);
    }

    private static List<Item> createItems() {
        List<Item> list = new ArrayList<>();
        for (int i = 0 ; i < 60 ; i ++){
            list.add(new Item("heaven7__" + i));
        }
        return list;
    }

    private static class Item extends BaseSelector {
        final String text;
        public Item(String text) {
            this.text = text;
        }
    }
    public static class PersonItem extends BaseAdapterItem<Item> {
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
            System.out.println("position = " + position + " ,data = " + data.text);

            mTv.setText(data.text);
            getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toaster.show(context, "" + data.text + "position = " + position);
                }
            });
        }
    }
}
