package com.example.tarea3.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class TabsViewModel extends ViewModel {
    private MutableLiveData<String> mTitle = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mTitle, new Function<String, String>() {
        @Override
        public String apply(String input) {
            return "null";
        }
    });

    public void setIndex(String index) {
        mTitle.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}
