package uk.ac.nottingham.horizon.exploding.author.client.view;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.SliderField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;

public class EventAttributes extends ContentPanel {

	public EventAttributes() {
		
		setLayout(new ColumnLayout());
		
		ContentPanel cntntpnlEventText = new ContentPanel();
		cntntpnlEventText.setHeight("300");
		cntntpnlEventText.setHeading("Event Text");
		cntntpnlEventText.setCollapsible(true);
		
		TextArea textArea = new TextArea();
		cntntpnlEventText.add(textArea);
		textArea.setSize("160", "270");
		textArea.setFieldLabel("New TextArea");
		add(cntntpnlEventText, new ColumnData(165.0));
		
		ContentPanel cntntpnlAttributes = new ContentPanel();
		cntntpnlAttributes.setSize("250", "300");
		cntntpnlAttributes.setHeading("Attributes");
		cntntpnlAttributes.setCollapsible(true);
		cntntpnlAttributes.setLayout(new FillLayout(Orientation.VERTICAL));
		
		FieldSet fldstHealth = new FieldSet();
		
		Slider slider_1 = new Slider();
		slider_1.setValue(30);
		SliderField sliderField_1 = new SliderField(slider_1);
		fldstHealth.add(sliderField_1);
		sliderField_1.setFieldLabel("New SliderField");
		cntntpnlAttributes.add(fldstHealth);
		fldstHealth.setHeading("Health");
		
		FieldSet fldstWealth = new FieldSet();
		
		Slider slider = new Slider();
		slider.setValue(30);
		SliderField sliderField = new SliderField(slider);
		fldstWealth.add(sliderField);
		sliderField.setFieldLabel("New SliderField");
		cntntpnlAttributes.add(fldstWealth);
		fldstWealth.setHeading("Wealth");
		
		FieldSet fldstBrains = new FieldSet();
		
		Slider slider_2 = new Slider();
		slider_2.setValue(30);
		SliderField sliderField_2 = new SliderField(slider_2);
		fldstBrains.add(sliderField_2);
		sliderField_2.setFieldLabel("New SliderField");
		cntntpnlAttributes.add(fldstBrains);
		fldstBrains.setHeading("Brains");
		
		FieldSet fldstAction = new FieldSet();
		
		Slider slider_3 = new Slider();
		fldstAction.add(slider_3);
		slider_3.setValue(30);
		cntntpnlAttributes.add(fldstAction);
		fldstAction.setHeading("Action");
		add(cntntpnlAttributes);
	}

}
