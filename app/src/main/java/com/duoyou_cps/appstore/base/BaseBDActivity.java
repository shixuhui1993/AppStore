package com.duoyou_cps.appstore.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;

public abstract class BaseBDActivity<T extends ViewDataBinding,X extends ViewModel> extends AppCompatActivity {

    protected T binding;
    protected X viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindLayout();
    }

    protected void bindLayout(){
        bindLayout(getViewModelClass());
    }

    protected void bindLayout(Class<X> clzVM){
        bindLayout(new ViewModelProvider(this).get(clzVM));
    }

    protected void bindLayout(X vm){
        viewModel = vm;
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.setLifecycleOwner(this);
        binding.setVariable(BR._all,vm);
    }

    public abstract int getLayoutId();

    //protected abstract Class<? extends ViewModel> getViewModelClass();

    public Class<X> getViewModelClass() {
        Class<X> xClass = (Class<X>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return xClass;
    }

}