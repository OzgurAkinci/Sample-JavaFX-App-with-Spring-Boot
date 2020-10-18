package com.schoolz.schoolz.screen;

import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.constant.*;
import com.schoolz.schoolz.custom.LimitedTextField;
import com.schoolz.schoolz.entity.SClass;
import com.schoolz.schoolz.entity.SExam;
import com.schoolz.schoolz.service.SClassService;
import com.schoolz.schoolz.service.SExamService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.stream.Collectors;

@Component
public class SExamScreen {
    private SExam sExam;
    private TableView tableView;

    @Autowired
    SExamService sExamService;

    @Autowired
    SClassService sClassService;

    @Autowired
    MenuConstant menuConstant;

    private final ObservableList<SClass> classListData = FXCollections.observableArrayList();

    public Stage examStage(Stage stage) throws IOException, BackingStoreException {
        List<SClass> classList = sClassService.findAll();
        classList.forEach(c-> {
            classListData.add(c);
        });

        MenuBar menuBar = menuConstant.menuBar();
        menuBar.setCursor(Cursor.CLOSED_HAND);
        menuBar.setStyle("-fx-font-size: 14.0 pt");


        Label nameLabel = new Label();
        nameLabel.textProperty().bind(I18N.createStringBinding("exam.code"));
        nameLabel.setStyle("-fx-font-size: 14.0 pt");
        LimitedTextField nameTextField = new LimitedTextField();
        nameTextField.setStyle("-fx-font-size: 14.0 pt");
        nameTextField.setText(sExam.getExamCode());

        Label examClassLabel = new Label();
        examClassLabel.textProperty().bind(I18N.createStringBinding("exam.class"));
        examClassLabel.setStyle("-fx-font-size: 14.0 pt");

        ObservableList<SClass> items = FXCollections.observableArrayList();
        classList.forEach(sClass -> {
            items.add(sClass);
        });
        ListView<SClass> classListBox = new ListView<>(items);
        ListView<SClass> selected = new ListView<>();
        classListBox.prefWidthProperty().bind(nameTextField.widthProperty());
        classListBox.setStyle("-fx-font-size: 14.0 pt");
        classListBox.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        classListBox.setPrefHeight(100);
        classListBox.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
            selected.setItems(classListBox.getSelectionModel().getSelectedItems());
        });

        if(sExam.getsClasses() != null)
        sExam.getsClasses().forEach(selectedExamClass->{
            classListBox.getSelectionModel().select(items.stream().filter(d->d.getId().equals(selectedExamClass.getId())).findFirst().get());
        });


        Label examLocationLabel = new Label();
        examLocationLabel.textProperty().bind(I18N.createStringBinding("exam.location"));
        examLocationLabel.setStyle("-fx-font-size: 14.0 pt");
        LimitedTextField examLocationField = new LimitedTextField();
        examLocationField.setStyle("-fx-font-size: 14.0 pt");
        examLocationField.setText(sExam.getExamLocation());

        Label examTimeLabel = new Label();
        examTimeLabel.textProperty().bind(I18N.createStringBinding("exam.time"));
        examTimeLabel.setStyle("-fx-font-size: 14.0 pt");
        NumberFieldFX examTimeField = new NumberFieldFX();
        examTimeField.setStyle("-fx-font-size: 14.0 pt");
        examTimeField.setText(sExam.getExamTime() != null ? sExam.getExamTime().toString() : "0");

        Label examDateLabel = new Label();
        examDateLabel.textProperty().bind(I18N.createStringBinding("exam.date"));
        examDateLabel.setStyle("-fx-font-size: 14.0 pt");
        DateTimePicker examDatePicker = new DateTimePicker();
        examDatePicker.prefWidthProperty().bind(nameTextField.widthProperty());
        examDatePicker.setStyle("-fx-font-size: 14.0 pt");
        examDatePicker.setValue(sExam.getExamDate() == null ? null : AppConstant.convertToLocalDate(sExam.getExamDate()));


        Separator sep = new Separator();
        sep.prefWidthProperty().bind(stage.widthProperty());
        sep.setHalignment(HPos.CENTER);
        VBox SeparatorvBox = new VBox(sep);
        SeparatorvBox.setPadding(new Insets(5, 0, 5, 0));

        ImageView saveButtonImageView = new ImageView(new Image(IconConstant.faviconImage));
        Button saveButton = new Button("Save", saveButtonImageView);
        saveButton.textProperty().bind(I18N.createStringBinding("button.save"));
        saveButton.setStyle("-fx-font-size: 14.0 pt");
        saveButton.prefWidthProperty().bind(nameTextField.widthProperty());

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String name = nameTextField.getText();
                sExam.setId(sExam.getId() == null ? null : sExam.getId());
                sExam.setExamCode((name == null || name.equals("")) ? null : name);
                Date examDate_ = null;
                if(examDatePicker.getValue() != null) {
                    examDate_ = AppConstant.convertToDate(examDatePicker.getValue());
                    sExam.setExamDate(examDate_);
                }
                sExam.setExamLocation((examLocationField.getText() == null || examLocationField.getText().equals("")) ? null : examLocationField.getText());
                sExam.setExamTime((examTimeField.getText() == null || examTimeField.getText().equals("")) ? null : (Integer.valueOf(examTimeField.getText())));
                sExam.setsStudentExams(sExam.getsStudentExams());

                Set<SClass> eClass = new HashSet<>();
                classListBox.getSelectionModel().getSelectedItems().stream().forEach(sClass -> {
                    //eClass.add(sClassService.findById(sClass.getId()));
                });

                sExam.setsClasses(classListBox.getSelectionModel().getSelectedItems().stream().collect(Collectors.toSet()));

                save(sExam, stage);
            }
        });


        VBox vbForm = new VBox(nameLabel, nameTextField, examLocationLabel, examLocationField, examTimeLabel, examTimeField, examDateLabel, examDatePicker, examClassLabel, classListBox, SeparatorvBox, saveButton);
        vbForm.setPadding(new Insets(10, 10, 10, 10));


        VBox vBox = new VBox(menuBar,vbForm);
        Scene scene = new Scene(vBox);

        stage.titleProperty().bind(I18N.createStringBinding("application.title"));
        stage.getIcons().add(new Image(IconConstant.faviconImage));
        stage.setScene(scene);
        return stage;
    }

    private void save(SExam sExam, Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.titleProperty().bind(I18N.createStringBinding("message.title"));
        alert.headerTextProperty().bind(I18N.createStringBinding("message.areYouSure"));
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                try {
                    //sExamClassDao.deleteB(sExam);
                    sExamService.save(sExam);
                    if(tableView != null)
                        tableView.refresh();
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.titleProperty().bind(I18N.createStringBinding("message.title"));
                    alert1.headerTextProperty().bind(I18N.createStringBinding("message.savedExam"));
                    alert1.showAndWait().ifPresent(rs1 -> {
                        if (rs1 == ButtonType.OK) {
                            //primaryStage.close();
                        }
                    });
                }catch (Exception err){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.titleProperty().bind(I18N.createStringBinding("message.warning"));
                    alert2.headerTextProperty().bind(I18N.createStringBinding("message.anyError"));
                    alert2.setContentText(err.getMessage());
                    alert2.showAndWait().ifPresent(rs2 -> {

                    });
                }
            }
        });
    }

    public SExam getsExam() {
        return sExam;
    }

    public void setsExam(SExam sExam) {
        this.sExam = sExam;
    }

    public void setTableView(TableView<SExam> tableView) {
        this.tableView = tableView;
    }
}
