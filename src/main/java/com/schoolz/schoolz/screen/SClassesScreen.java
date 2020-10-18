package com.schoolz.schoolz.screen;

import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.constant.IconConstant;
import com.schoolz.schoolz.constant.MenuConstant;
import com.schoolz.schoolz.dao.SClassDao;
import com.schoolz.schoolz.entity.SClass;
import com.schoolz.schoolz.entity.SStudent;
import com.schoolz.schoolz.service.SClassService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.prefs.BackingStoreException;

@Component
public class SClassesScreen {

    @Autowired
    private SClassService sClassService;

    @Autowired
    MenuConstant menuConstant;

    @Autowired
    SClassScreen sClassScreen;

    public Stage classesStage(Stage stage){
        MenuBar menuBar = menuConstant.menuBar();
        menuBar.setCursor(Cursor.CLOSED_HAND);
        menuBar.setStyle("-fx-font-size: 14.0 pt");


        TableView<SClass> table = createClassesTable();
        table.setStyle("-fx-font-size: 14.0 pt");
        table.prefHeightProperty().bind(stage.heightProperty());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label subLabel = new Label();
        subLabel.textProperty().bind(I18N.createStringBinding("application.title"));
        subLabel.setAlignment(Pos.CENTER);
        subLabel.setStyle("-fx-font-size: 14.0 pt");
        subLabel.setPrefHeight(55);


        VBox vBox = new VBox(menuBar, table, subLabel);
        Scene scene = new Scene(vBox, 900, 450);

        stage.titleProperty().bind(I18N.createStringBinding("application.title"));
        stage.getIcons().add(new Image(IconConstant.faviconImage));
        stage.setScene(scene);

        return stage;
    }


    private TableView<SClass> createClassesTable() {
        TableView<SClass> table = new TableView<>();
        List<SClass> s = sClassService.findAll();
        ObservableList<SClass> data = FXCollections.observableArrayList(s);

        TableColumn className = new TableColumn();
        className.textProperty().bind(I18N.createStringBinding("student.className"));
        className.setMinWidth(100);
        className.setCellValueFactory(new PropertyValueFactory<SStudent, String>("name"));


        table.setRowFactory( tv -> {
            TableRow<SClass> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    SClass sClass = row.getItem();
                    sClassScreen.setSclass(sClass);
                    sClassScreen.setTableView(table);
                    Stage primaryStage = new Stage();
                    try {
                        sClassScreen.studentStage(primaryStage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BackingStoreException e) {
                        e.printStackTrace();
                    }
                    try {
                        primaryStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
        table.setItems(data);
        table.getColumns().addAll(className);
        return table;
    }
}
