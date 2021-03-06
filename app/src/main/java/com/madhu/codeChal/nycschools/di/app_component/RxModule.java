package com.madhu.codeChal.nycschools.di.app_component;

import com.madhu.codeChal.nycschools.rx_util.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class RxModule {

    @AppScope
    @Provides
    static SchedulerProvider schedulerProvider(){
        return new SchedulerProvider() {
            @Override
            public Scheduler io() {
                return Schedulers.io();
            }

            @Override
            public Scheduler computation() {
                return Schedulers.computation();
            }

            @Override
            public Scheduler main() {
                return AndroidSchedulers.mainThread();
            }

            @Override
            public Scheduler db() {
                /*
                    ensure that all db actions occur of same thread to avoid,
                    closed database issues.
                 */
                return Schedulers.single();
            }
        };
    }


}
