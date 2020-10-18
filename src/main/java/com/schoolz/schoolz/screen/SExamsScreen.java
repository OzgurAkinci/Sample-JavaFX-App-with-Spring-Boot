package com.schoolz.schoolz.screen;

import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.constant.IconConstant;
import com.schoolz.schoolz.constant.MenuConstant;
import com.schoolz.schoolz.dao.SExamDao;
import com.schoolz.schoolz.entity.SExam;
import com.schoolz.schoolz.service.SExamService;
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
public class SExamsScreen {

    @Autowired
    SExamService sExamService;

    @Autowired
    MenuConstant menuConstant;

    @Autowired
    SExamScreen sExamScreen;

    public Stage examsStage(Stage stage){
        MenuBar menuBar = menuConstant.menuBar();
        menuBar.setCursor(Cursor.CLOSED_HAND);
        menuBar.setStyle("-fx-font-size: 14.0 pt");


        TableView<SExam> table = createClassesTable();
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


    private TableView<SExam> createClassesTable() {
        TableView<SExam> table = new TableView<>();
        List<SExam> s = sExamService.findAll();
        ObservableList<SExam> data = FXCollections.observableArrayList(s);

        TableColumn className = new TableColumn();
        className.textProperty().bind(I18N.createStringBinding("exam.code"));
        className.setMinWidth(100);
        className.setCellValueFactory(new PropertyValueFactory<SExam, String>("examCode"));

        TableColumn examDate = new TableColumn();
        examDate.textProperty().bind(I18N.createStringBinding("exam.date"));
        examDate.setMinWidth(100);
        examDate.setCellValueFactory(new PropertyValueFactory<SExam, String>("examDate"));

        TableColumn examTime = new TableColumn();
        examTime.textProperty().bind(I18N.createStringBinding("exam.time"));
        examTime.setMinWidth(100);
        examTime.setCellValueFactory(new PropertyValueFactory<SExam, String>("examTime"));

        TableColumn examLocation = new TableColumn();
        examLocation.textProperty().bind(I18N.createStringBinding("exam.location"));
        examLocation.setMinWidth(100);
        examLocation.setCellValueFactory(new PropertyValueFactory<SExam, String>("examLocation"));

        TableColumn avgExamScore = new TableColumn();
        avgExamScore.textProperty().bind(I18N.createStringBinding("exam.avg"));
        avgExamScore.setMinWidth(100);
        avgExamScore.setCellValueFactory(new PropertyValueFactory<SExam, String>("avgScore"));

        TableColumn studentCountExamScore = new TableColumn();
        studentCountExamScore.textProperty().bind(I18N.createStringBinding("exam.studentCount"));
        studentCountExamScore.setMinWidth(100);
        studentCountExamScore.setCellValueFactory(new PropertyValueFactory<SExam, String>("studentCount"));




        table.setRowFactory( tv -> {

            TableRow<SExam> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    SExam sExam = row.getItem();
                    sExamScreen.setsExam(sExam);
                    sExamScreen.setTableView(table);
                    Stage primaryStage = new Stage();
                    try {
                        sExamScreen.examStage(primaryStage);
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
        table.getColumns().addAll(className, examDate, examTime, examLocation, studentCountExamScore, avgExamScore);
        return table;
    }
}
