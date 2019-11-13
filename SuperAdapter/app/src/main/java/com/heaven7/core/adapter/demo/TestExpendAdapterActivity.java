package com.heaven7.core.adapter.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.heaven7.adapter.ExpendableAdapter;
import com.heaven7.adapter.SimpleExpendItem;
import com.heaven7.adapter.util.ViewHelper2;
import com.heaven7.core.util.Toaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TestExpendAdapterActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    private ExpendableAdapter<ParentItem> mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_expend);
        ButterKnife.bind(this);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExpendableAdapter<ParentItem>(new ItemTyper(), null);
        mAdapter.setItemBindDelegate(new ItemBinder());
        mRv.setAdapter(mAdapter);
    }

    @OnClick(R.id.bt_parent)
    public void onClickAddParentItems(View view){
        Random random = new Random();
        ParentItem pi = new ParentItem("Google__" + random.nextInt());
        for (int i = 0 ; i < 3 ; i ++){
            Item item = new Item("Heaven7___" + i);
            pi.addChildItem(item);
        }
        mAdapter.addItem(pi);
    }
    @OnClick(R.id.bt_child)
    public void onClickAddChildItems(View view){
        int itemCount = mAdapter.getItemCount();
        if(itemCount > 0){
            List<Item> list = new ArrayList<>();
            for (int i = 0 ; i < 3 ; i ++){
                Item item = new Item(UUID.randomUUID().toString());
                list.add(item);
            }
            mAdapter.addChildItems(0, 0,  list, true);
        }
    }

    private class ItemTyper implements ExpendableAdapter.ItemTypeDelegate<ParentItem>{

        @Override
        public int getItemLayoutId(ParentItem item, int realPosition, int position) {
            return R.layout.item_expend_parent;
        }
        @Override
        public int getChildItemLayoutId(ParentItem item, int realPosition, int parentPosition, int childPos) {
            return android.R.layout.simple_list_item_1;
        }
    }

    private class ItemBinder implements ExpendableAdapter.ItemBindDelegate<ParentItem>{
        @Override
        public void onBindParentData(ExpendableAdapter<ParentItem> adapter, Context context, int realPosition, int parentPos,
                                     ParentItem item, int itemLayoutId, ViewHelper2 helper) {
             helper.setText(android.R.id.text1, item.text)
                     .setRootOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             if(item.isExpended()){
                                 adapter.collapseByParent(parentPos);
                             }else {
                                 adapter.expandByParent(parentPos);
                             }
                         }
                     });
        }
        @Override
        public void onBindChildData(ExpendableAdapter<ParentItem> adapter, Context context, int realPosition, int parentPos, int childPos,
                                    ParentItem item, int itemLayoutId, ViewHelper2 helper) {
            final Item childItem = item.getChildItem(childPos);
            helper.setText(android.R.id.text1, childItem.text)
                    .setRootOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toaster.show(context, childItem.text);
                        }
                    });
        }
    }
    private static class Item{
        String text;
        public Item(String text) {
            this.text = text;
        }
    }
    private static class ParentItem extends SimpleExpendItem<Item>{
        String text;
        public ParentItem(String text) {
            super();
            this.text = text;
        }
    }
}
