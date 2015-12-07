package org.lostfan.ktv.view.components;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.lostfan.ktv.utils.DateLabelFormatter;

import java.time.LocalDate;
import java.util.Date;

public class DatePickerField extends JDatePickerImpl {

    public DatePickerField() {
        super(new JDatePanelImpl(new UtilDateModel()), new DateLabelFormatter());
    }

    public DatePickerField(LocalDate initialDate) {
        this();
        setValue(initialDate);
    }

    public void setValue(LocalDate value) {
        if (value == null) {
            getModel().setValue(null);
            getModel().setSelected(false);
        } else {
            getModel().setDate(value.getYear(), value.getMonthValue() - 1, value.getDayOfMonth());
            getModel().setSelected(true);
        }
    }

    public LocalDate getValue() {
        if(getModel().getValue() == null) {
            return null;
        }

        return new java.sql.Date(((Date)getModel().getValue()).getTime()).toLocalDate();
    }
}
