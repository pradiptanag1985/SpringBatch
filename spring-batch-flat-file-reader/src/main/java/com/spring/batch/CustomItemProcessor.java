package com.spring.batch;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<DataHolder,DataHolder> {
    @Override
    public DataHolder process(DataHolder dataHolder) {
        if(dataHolder.getLeave() != 0) {
            throw new IllegalStateException("Leave can't be anything other than zero");
        }
        return dataHolder;
    }
}
