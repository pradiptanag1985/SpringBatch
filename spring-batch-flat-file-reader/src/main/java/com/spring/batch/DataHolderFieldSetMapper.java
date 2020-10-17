package com.spring.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class DataHolderFieldSetMapper implements FieldSetMapper<DataHolder> {

    @Override
    public DataHolder mapFieldSet(FieldSet fieldSet) throws BindException {
        DataHolder dataHolder = new DataHolder();
        dataHolder.setSerialNumber(fieldSet.readLong("Serial Number"));
        dataHolder.setCompanyName(fieldSet.readString("Company Name"));
        dataHolder.setEmployee(fieldSet.readString("Employee Markme"));
        dataHolder.setDescription(fieldSet.readString("Description"));
        dataHolder.setLeave(fieldSet.readInt("Leave"));
        return dataHolder;
    }
}
