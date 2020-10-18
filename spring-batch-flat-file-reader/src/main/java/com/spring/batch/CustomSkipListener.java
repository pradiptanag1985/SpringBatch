package com.spring.batch;

public class CustomSkipListener implements org.springframework.batch.core.SkipListener<DataHolder, DataHolder> {

    @Override
    public void onSkipInRead(Throwable throwable) {

    }

    @Override
    public void onSkipInWrite(DataHolder dataHolder, Throwable throwable) {

    }

    @Override
    public void onSkipInProcess(DataHolder dataHolder, Throwable throwable) {
        System.out.println("Skipping processing with leave : " + dataHolder);
    }
}
