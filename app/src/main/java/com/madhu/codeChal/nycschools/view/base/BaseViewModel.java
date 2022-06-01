package com.madhu.codeChal.nycschools.view.base;

import androidx.lifecycle.ViewModel;

import com.madhu.codeChal.nycschools.di.ComponentProvider;
import com.madhu.codeChal.nycschools.di.ComponentProviderImpl;

import io.reactivex.disposables.CompositeDisposable;


public class BaseViewModel extends ViewModel {

    protected final CompositeDisposable onPauseDisposable = new CompositeDisposable();
    protected final CompositeDisposable onDestroyDisposable = new CompositeDisposable();
    protected final ComponentProvider componentProvider = ComponentProviderImpl.getInstance();

}
