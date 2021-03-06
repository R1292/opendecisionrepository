package nl.rug.search.odr.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Stefan
 */
@FacesComponent(value = "nl.rug.search.odr.component.DatetimePicker")
public class DatetimePicker extends UIInput implements NamingContainer {

    @Override
    public String getFamily() {
        return "javax.faces.NamingContainer";
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        Date date = (Date) getValue();
        Calendar cal = new GregorianCalendar();
        if (date != null) {
            cal.setTime(date);
        } else {
            cal.setTime(new Date());
        }
        UIInput dayComponent = (UIInput) findComponent("Day");
        UIInput monthComponent = (UIInput) findComponent("Month");
        UIInput yearComponent = (UIInput) findComponent("Year");
        UIInput hourComponent = (UIInput) findComponent("Hour");
        UIInput minuteComponent = (UIInput) findComponent("Minute");

        dayComponent.setValue(cal.get(Calendar.DATE));
        monthComponent.setValue(cal.get(Calendar.MONTH) + 1);
        yearComponent.setValue(cal.get(Calendar.YEAR));
        hourComponent.setValue(cal.get(Calendar.HOUR_OF_DAY));
        minuteComponent.setValue(cal.get(Calendar.MINUTE));

        Application application = context.getApplication();

        dayComponent.getChildren().clear();
//        if (monthComponent.getValue().equals(2)) {
//            if (isLeapYear((Integer) yearComponent.getValue())) {
//                UISelectItems dayItems = createSelectItems(application, 29, 1);
//                dayComponent.getChildren().add(dayItems);
//            } else {
//                UISelectItems dayItems = createSelectItems(application, 28, 1);
//                dayComponent.getChildren().add(dayItems);
//            }
//        } else if (monthComponent.getValue().equals(4) || monthComponent.getValue().equals(6)
//                || monthComponent.getValue().equals(9) || monthComponent.getValue().equals(11)) {
//            UISelectItems dayItems = createSelectItems(application, 30, 1);
//            dayComponent.getChildren().add(dayItems);
//        } else {
        UISelectItems dayItems = createSelectItems(application, 31, 1);
        dayComponent.getChildren().add(dayItems);
//        
        //}


        super.encodeBegin(context);

    }

    @Override
    protected Object getConvertedValue(FacesContext facesContext, Object submittedValue)
            throws ConverterException {

        UIInput dayComponent = (UIInput) findComponent("Day");
        UIInput monthComponent = (UIInput) findComponent("Month");
        UIInput yearComponent = (UIInput) findComponent("Year");
        UIInput hourComponent = (UIInput) findComponent("Hour");
        UIInput minuteComponent = (UIInput) findComponent("Minute");


        int day = (Integer) dayComponent.getValue();
        int month = (Integer) monthComponent.getValue();
        int year = (Integer) yearComponent.getValue();
        int hour = (Integer) hourComponent.getValue();
        int minute = (Integer) minuteComponent.getValue();

        //int amount = isValidDate(day, month, year);
//        if (amount != -1 && amount != 0) {
//            return new GregorianCalendar(year, month, amount, hour, minute).getTime();
//        } else {
        return new GregorianCalendar(year, month - 1, day, hour, minute).getTime();
//        }
    }

    @Override
    public Object getSubmittedValue() {
        return this;
    }

    private UISelectItems createSelectItems(Application application, int number, int step) throws FacesException {
        UISelectItems selectItems = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (int i = 1; i <= number; i += step) {
            items.add(new SelectItem(StringUtils.leftPad(String.valueOf(i), 2, "0")));
        }
        selectItems.setValue(items);
        return selectItems;
    }

    @Override
    public void updateModel(FacesContext context) {
        super.updateModel(context);
        UIInput dayComponent = (UIInput) findComponent("Day");

        dayComponent.validate(context);

        ValueExpression valueExpression = getValueExpression("value");
        Date value = (Date) valueExpression.getValue(context.getELContext());
        if (value != null && dayComponent.getValue() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(value);
            valueExpression.setValue(context.getELContext(), calendar.getTime());
        }
    }
}
