package com.heaven7.core.adapter.demo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.heaven7.adapter.page.PageDataProvider;
import com.heaven7.adapter.page.ViewPagerDelegate;
import com.heaven7.adapter.page.PageViewProvider;
import com.heaven7.core.util.Logger;

import butterknife.ButterKnife;

public class TestViewPagerActivity extends AppCompatActivity {

    private static final String TAG = "TestViewPager2Activity";
    private static final String[] ARRAY = {
            "http://img5.imgtn.bdimg.com/it/u=3050590552,1445108891&fm=11&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3356601016,43598563&fm=26&gp=0.jpg",
            "http://hbimg.b0.upaiyun.com/5507f33cdf77233ec4816d8e57407517a0f2477925557-FGnaJI_fw658"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.bind(this);
        ViewPagerDelegate.get(getPagerView()).setAdapter(this, new DataProvider0(this),
                new ViewProvider0(this), true);
    }

    protected View getPagerView(){
        return findViewById(R.id.vp);
    }

    protected int getLayoutId(){
        return R.layout.ac_test_vp;
    }


    protected static class DataProvider0 extends PageDataProvider<String>{

        public DataProvider0(Context context) {
            super(context);
        }
        @Override
        public int getItemCount() {
            return ARRAY.length;
        }
        @Override
        public String getItem(int realPos) {
            Logger.d(TAG, "getItem", "realPos = " + realPos);
            return ARRAY[realPos];
        }
    }
    protected static class ViewProvider0 extends PageViewProvider<String>{

        public ViewProvider0(Context context) {
            super(context);
        }

        @Override
        public View createItemView(ViewGroup parent, int position, int realPosition, String data) {
            ImageView iv = new ImageView(parent.getContext());
            return iv;
        }
        @Override
        public void onBindItemView(View v, int position, int realPosition, String data) {
            ImageView iv = (ImageView) v;
            Glide.with(v.getContext())
                    .load(data)
                    .into(iv);
        }
    }
}
