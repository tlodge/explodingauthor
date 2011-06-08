package uk.ac.nottingham.horizon.exploding.author.client.view;


import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import uk.ac.nottingham.horizon.exploding.author.client.datastore.Datastore;
import uk.ac.nottingham.horizon.exploding.author.client.model.GameListEntry;
import uk.ac.nottingham.horizon.exploding.author.client.presenter.FileManagerPresenter;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

public class FileManagerView extends Window implements FileManagerPresenter.Display{

	private Button exportButton;
	private Button importButton;
	private Button rawButton;
	private Button refreshButton;
	private TextBox gameName;
	private TextBox serverTextBox;
	private TextBox locationTextBox;
	private TextArea rawTextArea;
	
	private Grid<GameListEntry> grid;
	
	public FileManagerView() {
		setTitle("File import/export");
		setWidth("380px");
		setCollapsible(true);
		setClosable(false);
		
		
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
		FieldSet fldstImportContent = new FieldSet();
		add(fldstImportContent);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(10);
		fldstImportContent.add(verticalPanel);
		verticalPanel.setWidth("280");
		columns.add(new ColumnConfig("ID", "ID", 55));  
		columns.add(new ColumnConfig("name", "name", 85));  
		columns.add(new ColumnConfig("location", "location", 75));  
		columns.add(new ColumnConfig("startYear", "startYear", 55));  
		columns.add(new ColumnConfig("endYear", "endYear", 55));  
		
		/*
		 * should move this to presenter....
		 */
		grid = new Grid<GameListEntry>(Datastore.getListStore(), new ColumnModel(columns));  
		grid.setBorders(true);  
		grid.setHeight(200);
		verticalPanel.add(grid);
		grid.setSize("335px", "117px");
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(5);
		verticalPanel.add(horizontalPanel_1);
		verticalPanel.setCellVerticalAlignment(horizontalPanel_1, HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setCellHorizontalAlignment(horizontalPanel_1, HasHorizontalAlignment.ALIGN_CENTER);
		importButton = new Button("Import");
		horizontalPanel_1.add(importButton);
		horizontalPanel_1.setCellVerticalAlignment(importButton, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_1.setCellHorizontalAlignment(importButton, HasHorizontalAlignment.ALIGN_CENTER);
		
		refreshButton = new Button("Refresh");
		horizontalPanel_1.add(refreshButton);
		horizontalPanel_1.setCellVerticalAlignment(refreshButton, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_1.setCellHorizontalAlignment(refreshButton, HasHorizontalAlignment.ALIGN_CENTER);
		fldstImportContent.setHeading("Import Content");
		
		FieldSet fldstExportToServer = new FieldSet();
		add(fldstExportToServer);
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSpacing(5);
		fldstExportToServer.add(verticalPanel_1);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel);
		horizontalPanel.setWidth("288px");
		horizontalPanel.setSpacing(5);
		
		Label lblServerAddress = new Label("Server address");
		horizontalPanel.add(lblServerAddress);
		horizontalPanel.setCellHorizontalAlignment(lblServerAddress, HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setCellVerticalAlignment(lblServerAddress, HasVerticalAlignment.ALIGN_MIDDLE);
		lblServerAddress.setSize("90px", "19px");
		
		serverTextBox = new TextBox();
		horizontalPanel.add(serverTextBox);
		serverTextBox.setWidth("177px");
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		horizontalPanel_3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_3.setSpacing(5);
		verticalPanel_1.add(horizontalPanel_3);
		
		Label lblLocation = new Label("Game Location");
		horizontalPanel_3.add(lblLocation);
		lblLocation.setWidth("90");
		
		locationTextBox = new TextBox();
		horizontalPanel_3.add(locationTextBox);
		locationTextBox.setWidth("177");
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setSpacing(5);
		verticalPanel_1.add(horizontalPanel_2);
		horizontalPanel_2.setWidth("289px");
		
		Label lblGameName = new Label("Game Name");
		horizontalPanel_2.add(lblGameName);
		horizontalPanel_2.setCellVerticalAlignment(lblGameName, HasVerticalAlignment.ALIGN_MIDDLE);
		lblGameName.setWidth("90px");
		
		gameName = new TextBox();
		horizontalPanel_2.add(gameName);
		gameName.setWidth("177px");
		
		
		
		exportButton = new Button("Export");
		verticalPanel_1.add(exportButton);
		verticalPanel_1.setCellHorizontalAlignment(exportButton, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_1.setCellVerticalAlignment(exportButton, HasVerticalAlignment.ALIGN_MIDDLE);
		fldstExportToServer.setHeading("Export to Server");

		FieldSet fldstRawXML = new FieldSet();
		
		fldstRawXML.setHeading("Raw XML");
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		verticalPanel_3.setSpacing(5);
		fldstRawXML.add(verticalPanel_3);
		rawTextArea = new TextArea();
		rawTextArea.setWidth("320px");
		rawTextArea.setHeight("150px");
		verticalPanel_3.add(rawTextArea);
		
		HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
		rawButton = new Button("Refresh");
		
		horizontalPanel_4.add(rawButton);
		horizontalPanel_4.setCellVerticalAlignment(rawButton, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_4.setCellHorizontalAlignment(rawButton, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_3.add(horizontalPanel_4);
		
		add(fldstRawXML);
	}

	
	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return this;
	}


	@Override
	public HasClickHandlers getExportButton() {
		return exportButton;
	}


	@Override
	public HasClickHandlers getImportButton() {
		return importButton;
	}

	@Override
	public HasClickHandlers getRefreshButton() {
		return refreshButton;
	}

	@Override
	public HasClickHandlers getRawButton() {
		return rawButton;
	}
	
	@Override
	public Grid getGrid() {
		return grid;
	}
	
	@Override
	public String getGameName(){
		return gameName.getText();
		
	}


	@Override
	public String getServerAddress() {
		return serverTextBox.getText();
	}


	@Override
	public void setServerAddress(String address) {
		serverTextBox.setValue(address);
		
	}


	@Override
	public String getLocation() {
		return locationTextBox.getText();
	}


	@Override
	public void setLocation(String location) {
		locationTextBox.setText(location);
		
	}

	@Override
	public void setRawXML(String xml) {
		rawTextArea.setText(xml);
		
	}

}
