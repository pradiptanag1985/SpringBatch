package com.spring.chunk;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleItemReader implements ItemReader<String> {
    private List<String> list = new ArrayList<>();
    private Iterator<String> iterator;

    public SimpleItemReader() {
        this.list.add("1");
        this.list.add("2");
        this.list.add("3");
        this.list.add("4");
        this.list.add("5");
        this.list.add("6");
        this.iterator = this.list.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return (iterator.hasNext()) ? iterator.next() : null;
    }
}
