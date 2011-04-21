package uk.ac.nottingham.horizon.exploding.author.client.view;


import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import uk.ac.nottingham.horizon.exploding.author.client.presenter.AttributesPresenter;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.SliderField;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;


public class AttributesView extends Window implements AttributesPresenter.Display  {
	
	private ContentPanel cp_1;
	//private Button save,delete;
	private ContentPanel root; 
	private TextArea textArea;
	private TextBox textBox;
	
	private Slider slider1,slider2,slider3, slider4;
	private Button sequence1,sequence2,sequence3,sequence4;
	
	private Button[] buttons;
	
	private final int  MAXSLIDERVALUE = 5;
	private final int  MINSLIDERVALUE = -5;
	private final int DEFAULTSLIDERVALUE = 0;
	
	public AttributesView() {
		setActive(true);
		buttons = new Button[4];
		setHeading("Event Attributes Editor");
		setWidth(248);
		setHeight(382);
		ToolBar toolBar = new ToolBar();  
		toolBar.setAlignment(HorizontalAlignment.CENTER);
		toolBar.setSpacing(2);
		
		ButtonBar buttonBar = new ButtonBar();
		toolBar.add(buttonBar);
		
		sequence1  = new Button("1");
		buttonBar.add(sequence1);
		buttons[0] = sequence1;
		
		sequence2 = new Button("2");
		toolBar.add(sequence2);
		buttons[1] = sequence2;
		
		sequence3 = new Button("3");
		toolBar.add(sequence3);
		buttons[2] = sequence3;
		
		sequence4 = new Button("4");
		toolBar.add(sequence4);
		buttons[3] = sequence4;
		
		
		setTopComponent(toolBar);
		
		setLayout(new AccordionLayout());
		
		cp_1 = new ContentPanel();  
		cp_1.setAnimCollapse(false);  
		cp_1.setHeading("Text");  
		cp_1.setHeight(400);
		
		com.google.gwt.user.client.ui.VerticalPanel verticalPanel_1 = new com.google.gwt.user.client.ui.VerticalPanel();
		verticalPanel_1.setSpacing(10);
		cp_1.add(verticalPanel_1);
		
		Label lblTitle = new Label("Title");
		verticalPanel_1.add(lblTitle);
		
		textBox = new TextBox();
		verticalPanel_1.add(textBox);
		textBox.setWidth("205px");
		
		Label lblDescription = new Label("Description");
		verticalPanel_1.add(lblDescription);
		
		 textArea = new TextArea();
		 verticalPanel_1.add(textArea);
		 textArea.setSize("205px", "142px");
		 add(cp_1);
		root = new ContentPanel();  
		root.setFrame(true);
		root.setAnimCollapse(false);  
		root.setHeading("Attributes");  
		root.setLayout(new FitLayout());
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSize("231px", "280px");
		
		FieldSet fldstHealth = new FieldSet();
		
		slider1 = new Slider();
		slider1.setMinValue(MINSLIDERVALUE);
		slider1.setMaxValue(MAXSLIDERVALUE);
		slider1.setIncrement(1);
		slider1.setValue(DEFAULTSLIDERVALUE);
		SliderField sliderField = new SliderField(slider1);
		sliderField.setName("health");
		fldstHealth.add(sliderField);
		//sliderField.setFieldLabel("New SliderField");
		verticalPanel.add(fldstHealth);
		fldstHealth.setWidth("205px");
		fldstHealth.setHeading("Health");
		
		FieldSet fldstWealth = new FieldSet();
		
		slider2 = new Slider();
		slider2.setIncrement(1);
		slider2.setMinValue(MINSLIDERVALUE);
		slider2.setMaxValue(MAXSLIDERVALUE);
		slider2.setValue(DEFAULTSLIDERVALUE);
		SliderField sliderField_1 = new SliderField(slider2);
		fldstWealth.add(sliderField_1);
		//sliderField_1.setFieldLabel("New SliderField");
		verticalPanel.add(fldstWealth);
		fldstWealth.setWidth("205px");
		fldstWealth.setHeading("Wealth");
		
		FieldSet fldstBrains = new FieldSet();
		
		slider3 = new Slider();
		slider3.setIncrement(1);
		slider3.setMinValue(MINSLIDERVALUE);
		slider3.setMaxValue(MAXSLIDERVALUE);
		slider3.setValue(DEFAULTSLIDERVALUE);
		SliderField sliderField_2 = new SliderField(slider3);
		fldstBrains.add(sliderField_2);
		//sliderField_2.setFieldLabel("New SliderField");
		verticalPanel.add(fldstBrains);
		fldstBrains.setWidth("205px");
		fldstBrains.setHeading("Brains");
		
		FieldSet fldstAction = new FieldSet();
		
		slider4 = new Slider();
		slider4.setIncrement(1);
		slider4.setMinValue(MINSLIDERVALUE);
		slider4.setMaxValue(MAXSLIDERVALUE);
		slider4.setValue(DEFAULTSLIDERVALUE);
		SliderField sliderField_3 = new SliderField(slider4);
		fldstAction.add(sliderField_3);
		//sliderField_3.setFieldLabel("New SliderField");
		verticalPanel.add(fldstAction);
		fldstAction.setWidth("205px");
		fldstAction.setHeading("Action");
		root.add(verticalPanel);
		add(root);
	    
	  
	}


	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public String getTitle() {
		return textBox.getText();
	}


	@Override
	public void setTitle(String text) {
		textBox.setText(text);
	}
	
	@Override
	public String getDescription() {
		return textArea.getText();
	}


	@Override
	public void setDescription(String text) {
		textArea.setText(text);
	}

	@Override
	public Slider getSlider1() {
		return slider1;
	}


	@Override
	public Slider getSlider2() {
		return slider2;
	}


	@Override
	public Slider getSlider3() {
		return slider3;
	}


	@Override
	public Slider getSlider4() {
		return slider4;
	}
	@Override
	public void moveToFront(){
		/*
		 * Must be better way of doing this??
		 */
		this.hide();
		this.show();
	}
	
	@Override
	public TextArea getTextAreaDescription(){
		return textArea;
	}
	
	@Override
	public TextBox getTextBoxTitle(){
		return textBox;
	}


	@Override
	public Button getSequence1() {
		return sequence1;
	}


	@Override
	public Button getSequence2() {
		return sequence2;
	}


	@Override
	public Button getSequence3() {
		return sequence3;
	}


	@Override
	public Button getSequence4() {
		return sequence4;
	}

	@Override
	public void setSelected(int x){
		for (int i = 0; i < buttons.length; i++){
			if (i==x-1){
				if (buttons[i]!=null)
					buttons[i].setText("<" + (i+1) + ">");
			}else{
				if (buttons[i]!=null)
				buttons[i].setText(String.valueOf(i+1));
			}
				
		}
	}
	
}
