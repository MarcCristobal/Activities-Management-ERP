/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.domain;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvConstraintViolationException;
/**
 *
 * @author wilso
 */


public class MyEnumConverter extends AbstractBeanField {
    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            return CustomerType.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new CsvDataTypeMismatchException(e.getMessage());
        }
    }
}
