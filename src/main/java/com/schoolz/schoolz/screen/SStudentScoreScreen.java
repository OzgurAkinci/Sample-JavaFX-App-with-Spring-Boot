package com.schoolz.schoolz.screen;

import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.constant.IconConstant;
import com.schoolz.schoolz.constant.MenuConstant;
import com.schoolz.schoolz.constant.NumberFieldFX;
import com.schoolz.schoolz.dao.SExamDao;
import com.schoolz.schoolz.dao.SStudentDao;
import com.schoolz.schoolz.dao.SStudentExamDao;
import com.schoolz.schoolz.dto.ComboBoxDTO;
import com.schoolz.schoolz.entity.SClass;
import com.schoolz.schoolz.entity.SExam;
import com.schoolz.schoolz.entity.SStudent;
import com.schoolz.schoolz.entity.SStudentExam;
import com.schoolz.schoolz.service.SExamService;
import com.schoolz.schoolz.service.SStudentExamService;
import com.schoolz.schoolz.service.SStudentService;
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

import java.util.List;

@Component
public class SStudentScoreScreen {
    private SStudentExam sStudentScore;
    private TableView tableView;

    @Autowired
    SExamService sExamService;

    @Autowired
    SStudentExamService sStudentExamService;

    @Autowired
    SStudentService sStudentService;

    @Autowired
    MenuConstant menuConstant;

    private final ObservableList<ComboBoxDTO> examsListData = FXCollections.observableArrayList();
    private final ObservableList<ComboBoxDTO> studentListData = FXCollections.observableArrayList();

    public Stage studentExamStage(Stage stage) {
        List<SExam> examList = sExamService.findAll();
        examList.forEach(s-> {
            examsListData.add(new ComboBoxDTO(s.getId().toString(), s.getExamCode()));
        });

        List<SStudent> studentList = sStudentService.findAll();
        studentList.forEach(s-> {
            String studentFullName = s.getName()+" " + s.getSurname();
            studentListData.add(new ComboBoxDTO(s.getId().toString(), studentFullName));
        });


        MenuBar menuBar = menuConstant.menuBar();
        menuBar.setCursor(Cursor.CLOSED_HAND);
        menuBar.setStyle("-fx-font-size: 14.0 pt");


        Label studentScoreLabel = new Label();
        studentScoreLabel.textProperty().bind(I18N.createStringBinding("exam.score"));
        studentScoreLabel.setStyle("-fx-font-size: 14.0 pt");
        NumberFieldFX studentScoreField = new NumberFieldFX();
        studentScoreField.setStyle("-fx-font-size: 14.0 pt");
        studentScoreField.setText(sStudentScore.getScore() != null ? sStudentScore.getScore().toString() : "0");

        Label examStudentLabel = new Label();
        examStudentLabel.textProperty().bind(I18N.createStringBinding("exam.student"));
        examStudentLabel.setStyle("-fx-font-size: 14.0 pt");
        ComboBox examStudentComboBox = new ComboBox();
        examStudentComboBox.prefWidthProperty().bind(studentScoreField.widthProperty());
        examStudentComboBox.setItems(studentListData);
        examStudentComboBox.setStyle("-fx-font-size: 14.0 pt");

        Label examCodeLabel = new Label();
        examCodeLabel.textProperty().bind(I18N.createStringBinding("exam.code"));
        examCodeLabel.setStyle("-fx-font-size: 14.0 pt");
        ComboBox examCodeComboBox = new ComboBox();
        examCodeComboBox.prefWidthProperty().bind(studentScoreField.widthProperty());
        examCodeComboBox.setItems(examsListData);
        examCodeComboBox.setStyle("-fx-font-size: 14.0 pt");


        Separator sep = new Separator();
        sep.prefWidthProperty().bind(stage.widthProperty());
        sep.setHalignment(HPos.CENTER);
        VBox SeparatorvBox = new VBox(sep);
        SeparatorvBox.setPadding(new Insets(5, 0, 5, 0));

        ImageView saveButtonImageView = new ImageView(new Image(IconConstant.faviconImage));
        Button saveButton = new Button("Save", saveButtonImageView);
        saveButton.textProperty().bind(I18N.createStringBinding("button.save"));
        saveButton.setStyle("-fx-font-size: 14.0 pt");
        saveButton.prefWidthProperty().bind(studentScoreField.widthProperty());

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                sStudentScore.setId(sStudentScore.getId() == null ? null : sStudentScore.getId());
                sStudentScore.setScore((studentScoreField.getText() == null || studentScoreField.getText().equals("")) ? null : Integer.valueOf(studentScoreField.getText()));
                SExam exam_ = null;
                ComboBoxDTO selectedExam = null;
                if((ComboBoxDTO) examCodeComboBox.getSelectionModel().getSelectedItem() != null){
                    selectedExam = (ComboBoxDTO) examCodeComboBox.getSelectionModel().getSelectedItem();
                    exam_ = sExamService.findById(Integer.valueOf(selectedExam.getKey()));
                }
                sStudentScore.setsExam(exam_);

                SStudent student_ = null;
                ComboBoxDTO selectedExamStudent = null;
                if((ComboBoxDTO) examStudentComboBox.getSelectionModel().getSelectedItem() != null){
                    selectedExamStudent = (ComboBoxDTO) examStudentComboBox.getSelectionModel().getSelectedItem();
                    student_ = sStudentService.findById(Integer.valueOf(selectedExamStudent.getKey()));
                }
                sStudentScore.setsStudent(student_);
                sStudentScore.setScore((studentScoreField.getText() == null || studentScoreField.getText().equals("")) ? 0 : Integer.valueOf(studentScoreField.getText()));
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.titleProperty().bind(I18N.createStringBinding("message.title"));
                alert.headerTextProperty().bind(I18N.createStringBinding("message.areYouSure"));
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        save(sStudentScore, stage);
                    }
                });

            }
        });


        VBox vbForm = new VBox(examCodeLabel, examCodeComboBox, examStudentLabel, examStudentComboBox, studentScoreLabel, studentScoreField, SeparatorvBox, saveButton);
        vbForm.setPadding(new Insets(10, 10, 10, 10));


        VBox vBox = new VBox(menuBar,vbForm);
        Scene scene = new Scene(vBox);

        stage.titleProperty().bind(I18N.createStringBinding("application.title"));
        stage.getIcons().add(new Image(IconConstant.faviconImage));
        stage.setScene(scene);
        return stage;
    }

    private void save(SStudentExam sStudentScore_, Stage primaryStage) {
        try {
            sStudentScore = sStudentExamService.save(sStudentScore_);
            if(tableView != null)
                tableView.refresh();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.titleProperty().bind(I18N.createStringBinding("message.title"));
            alert1.headerTextProperty().bind(I18N.createStringBinding("message.savedStudentExam"));
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

    public SStudentExam getsStudentScore() {
        return sStudentScore;
    }

    public void setsStudentScore(SStudentExam sStudentScore) {
        this.sStudentScore = sStudentScore;
    }

    public void setTableView(TableView<SClass> tableView) {
        this.tableView = tableView;
    }
}
