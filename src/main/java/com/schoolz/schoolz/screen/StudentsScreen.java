package com.schoolz.schoolz.screen;

import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.constant.IconConstant;
import com.schoolz.schoolz.constant.MenuConstant;
import com.schoolz.schoolz.dao.SStudentDao;
import com.schoolz.schoolz.entity.SStudent;
import com.schoolz.schoolz.service.SStudentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

@Component
public class StudentsScreen{
    @Autowired
    SStudentService sStudentService;

    @Autowired
    MenuConstant menuConstant;

    @Autowired
    StudentScreen studentScreen;

    private ObservableList<SStudent> data = FXCollections.observableArrayList();
    private ObservableList<SStudent> filteredData = FXCollections.observableArrayList();
    TableView<SStudent> table;
    TextField searchTextField;

    public Stage studentsStage(Stage stage){
        MenuBar menuBar = menuConstant.menuBar();
        menuBar.setCursor(Cursor.CLOSED_HAND);
        menuBar.setStyle("-fx-font-size: 14.0 pt");

        List<SStudent> s = sStudentService.findAll();
        data.addAll(s);


        Label searchTextLabel = new Label();
        searchTextLabel.textProperty().bind(I18N.createStringBinding("other.searchText").concat(" <").concat(I18N.createStringBinding("student.name").concat(",").concat(I18N.createStringBinding("student.surname")).concat(">")));
        searchTextLabel.setStyle("-fx-font-size: 14.0 pt");
        searchTextField = new TextField();
        searchTextField.setStyle("-fx-font-size: 14.0 pt");

        FilteredList<SStudent> filteredData = new FilteredList<>(data, p -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (person.getSurname().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        SortedList<SStudent> sortedData = new SortedList<>(filteredData);
        table = new TableView<>();
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);



        table = createStudentsTable();
        table.setStyle("-fx-font-size: 14.0 pt");
        table.prefHeightProperty().bind(stage.heightProperty());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label subLabel = new Label();
        subLabel.textProperty().bind(I18N.createStringBinding("application.title"));
        subLabel.setAlignment(Pos.CENTER);
        subLabel.setStyle("-fx-font-size: 14.0 pt");
        subLabel.setPrefHeight(55);

        VBox vBoxGeneral = new VBox(searchTextLabel, searchTextField);
        vBoxGeneral.setPadding(new Insets(10, 10, 10, 10));

        VBox vBox = new VBox(menuBar,vBoxGeneral, table, subLabel);

        Scene scene = new Scene(vBox, 900, 450);

        stage.titleProperty().bind(I18N.createStringBinding("application.title"));
        stage.getIcons().add(new Image(IconConstant.faviconImage));
        stage.setScene(scene);

        return stage;
    }


    private TableView<SStudent> createStudentsTable() {
        TableColumn firstName = new TableColumn();
        firstName.textProperty().bind(I18N.createStringBinding("student.name"));
        firstName.setMinWidth(100);
        firstName.setCellValueFactory(new PropertyValueFactory<SStudent, String>("name"));

        TableColumn lastName = new TableColumn();
        lastName.textProperty().bind(I18N.createStringBinding("student.surname"));
        lastName.setMinWidth(100);
        lastName.setCellValueFactory(new PropertyValueFactory<SStudent, String>("surname"));

        TableColumn studentNumber = new TableColumn();
        studentNumber.textProperty().bind(I18N.createStringBinding("student.studentNumber"));
        studentNumber.setMinWidth(100);
        studentNumber.setCellValueFactory(new PropertyValueFactory<SStudent, String>("studentNumber"));

        TableColumn gender = new TableColumn();
        gender.textProperty().bind(I18N.createStringBinding("student.gender"));
        gender.setMinWidth(100);
        gender.setCellValueFactory(new PropertyValueFactory<SStudent, String>("gender"));

        TableColumn birthday = new TableColumn();
        birthday.textProperty().bind(I18N.createStringBinding("student.birthday"));
        birthday.setMinWidth(100);
        birthday.setCellValueFactory(new PropertyValueFactory<SStudent, String>("birthday"));

        TableColumn className = new TableColumn();
        className.textProperty().bind(I18N.createStringBinding("student.className"));
        className.setMinWidth(100);
        className.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SStudent, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SStudent, String> param) {
                ObservableValue<String> d = null;
                if(param.getValue().getsClass() != null)
                    d = new SimpleStringProperty(param.getValue().getsClass().getName());
                return d;
            }
        });

        TableColumn avgExamScore = new TableColumn();
        avgExamScore.textProperty().bind(I18N.createStringBinding("exam.avg"));
        avgExamScore.setMinWidth(100);
        avgExamScore.setCellValueFactory(new PropertyValueFactory<SStudent, String>("avgScore"));

        table.setRowFactory( tv -> {
            TableRow<SStudent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    SStudent student = row.getItem();
                    studentScreen.setStudent(student);
                    studentScreen.setTableView(table);
                    Stage primaryStage = new Stage();
                    try {
                        studentScreen.studentStage(primaryStage);
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
        //table.setItems(data);
        table.getColumns().addAll(firstName, lastName, studentNumber, gender, birthday, className, avgExamScore);
        return table;
    }

}
