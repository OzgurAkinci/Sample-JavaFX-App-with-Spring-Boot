package com.schoolz.schoolz.screen;


import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.constant.AppConstant;
import com.schoolz.schoolz.constant.IconConstant;
import com.schoolz.schoolz.constant.MenuConstant;
import com.schoolz.schoolz.custom.LimitedTextField;
import com.schoolz.schoolz.dao.SClassDao;
import com.schoolz.schoolz.dao.SStudentDao;
import com.schoolz.schoolz.dto.ComboBoxDTO;
import com.schoolz.schoolz.entity.SClass;
import com.schoolz.schoolz.entity.SStudent;
import com.schoolz.schoolz.entity.SStudentExam;
import com.schoolz.schoolz.service.SClassService;
import com.schoolz.schoolz.service.SStudentService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.prefs.BackingStoreException;

@Component
public class StudentScreen {
    private SStudent student;
    private TableView tableView;

    @Autowired
    SClassService sClassService;

    @Autowired
    SStudentService sStudentService;

    @Autowired
    MenuConstant menuConstant;

    private final ObservableList<ComboBoxDTO> genderListData =
            FXCollections.observableArrayList(
                    new ComboBoxDTO("", ""),
                    new ComboBoxDTO("MR", "Mr"),
                    new ComboBoxDTO("MRS", "Mrs"));

    public Stage studentStage(Stage stage) throws IOException, BackingStoreException {
        HashMap configs = AppConstant.initializeBaseConfig();

        ObservableList<SClass> classListData = FXCollections.observableArrayList();
        List<SClass> classList = sClassService.findAll();
        classList.forEach(c-> {
            classListData.add(c);
        });

        MenuBar menuBar = menuConstant.menuBar();
        menuBar.setCursor(Cursor.CLOSED_HAND);
        menuBar.setStyle("-fx-font-size: 14.0 pt");


        Label nameLabel = new Label();
        nameLabel.textProperty().bind(Bindings.concat(I18N.createStringBinding("student.name"), "  <Max: ", (configs.containsKey("maxTextFieldLength") ? configs.get("maxTextFieldLength").toString() : 0), ">"));
        nameLabel.setStyle("-fx-font-size: 14.0 pt");
        LimitedTextField nameTextField = new LimitedTextField();
        nameTextField.setStyle("-fx-font-size: 14.0 pt");
        nameTextField.setText(student.getName());
        nameTextField.setMaxLength((configs.containsKey("maxTextFieldLength") && !(configs.get("maxTextFieldLength").equals(""))) ? Integer.valueOf(configs.get("maxTextFieldLength").toString()) : 0);



        Label lastNameLabel = new Label();
        lastNameLabel.textProperty().bind(I18N.createStringBinding("student.surname"));
        lastNameLabel.setStyle("-fx-font-size: 14.0 pt");
        TextField lastNameTextField = new TextField();
        lastNameTextField.setStyle("-fx-font-size: 14.0 pt");
        lastNameTextField.setText(student.getSurname());

        Label studentNumberLabel = new Label();
        studentNumberLabel.textProperty().bind(I18N.createStringBinding("student.studentNumber"));
        studentNumberLabel.setStyle("-fx-font-size: 14.0 pt");
        TextField studentNumberTextField = new TextField();
        studentNumberTextField.setStyle("-fx-font-size: 14.0 pt");
        studentNumberTextField.setText(student.getStudentNumber());

        Label genderLabel = new Label();
        genderLabel.textProperty().bind(I18N.createStringBinding("student.gender"));
        genderLabel.setStyle("-fx-font-size: 14.0 pt");
        ComboBox genderComboBox = new ComboBox();
        genderComboBox.prefWidthProperty().bind(studentNumberTextField.widthProperty());
        genderComboBox.setItems(genderListData);
        genderComboBox.setStyle("-fx-font-size: 14.0 pt");
        if(student.getGender() != null)
            genderComboBox.getSelectionModel().select(genderListData.stream().filter(d->d.getKey().equals(student.getGender())).findFirst().get());

        Label classLabel = new Label();
        classLabel.textProperty().bind(I18N.createStringBinding("student.className"));
        classLabel.setStyle("-fx-font-size: 14.0 pt");
        ComboBox classComboBox = new ComboBox();
        classComboBox.prefWidthProperty().bind(studentNumberTextField.widthProperty());
        classComboBox.setItems(classListData);
        classComboBox.setStyle("-fx-font-size: 14.0 pt");
        if(student.getsClass() != null)
            classComboBox.getSelectionModel().select(classListData.stream().filter(d->d.getId()==student.getsClass().getId()).findFirst().get());


        Label birthDayLabel = new Label();
        birthDayLabel.textProperty().bind(Bindings.concat(I18N.createStringBinding("student.birthday"), " <Max: ", (configs.containsKey("maxDateFiledValue") ? configs.get("maxDateFiledValue").toString() : ""),">"));
        birthDayLabel.setStyle("-fx-font-size: 14.0 pt");
        DatePicker birthDayLabelDatePicker = new DatePicker();
        birthDayLabelDatePicker.prefWidthProperty().bind(studentNumberTextField.widthProperty());
        birthDayLabelDatePicker.setStyle("-fx-font-size: 14.0 pt");
        birthDayLabelDatePicker.setValue(student.getBirthday() == null ? null : AppConstant.convertToLocalDate(student.getBirthday()));
        if(configs.containsKey("maxDateFiledValue") && !configs.get("maxDateFiledValue").toString().equals("")) {
            LocalDate maxDate = LocalDate.parse(configs.get("maxDateFiledValue").toString());
            birthDayLabelDatePicker.setDayCellFactory(d ->
                    new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            setDisable(item.isAfter(maxDate));
                        }
                    });
        }

        Label addressLabel = new Label();
        addressLabel.textProperty().bind(I18N.createStringBinding("student.address"));
        addressLabel.setStyle("-fx-font-size: 14.0 pt");
        TextArea addresTextArea = new TextArea();
        addresTextArea.setStyle("-fx-font-size: 14.0 pt");

        Separator sep = new Separator();
        sep.prefWidthProperty().bind(stage.widthProperty());
        sep.setHalignment(HPos.CENTER);
        VBox SeparatorvBox = new VBox(sep);
        SeparatorvBox.setPadding(new Insets(5, 0, 5, 0));

        ImageView saveButtonImageView = new ImageView(new Image(IconConstant.faviconImage));
        Button saveButton = new Button("Save", saveButtonImageView);
        saveButton.textProperty().bind(I18N.createStringBinding("button.save"));
        saveButton.setStyle("-fx-font-size: 14.0 pt");
        saveButton.prefWidthProperty().bind(studentNumberTextField.widthProperty());

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String name = nameTextField.getText();
                String lastName = lastNameTextField.getText();
                String address = addresTextArea.getText();
                String studentNumber = studentNumberTextField.getText();
                String gender = "";
                if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null) {
                    ComboBoxDTO selectedGender = (ComboBoxDTO) genderComboBox.getSelectionModel().getSelectedItem();
                    gender = selectedGender.getKey();
                }
                Date birthDay = null;
                if(birthDayLabelDatePicker.getValue() != null) {
                    birthDay = AppConstant.convertToDate(birthDayLabelDatePicker.getValue());
                }

                SClass selectedClass = null;
                if(classComboBox.getSelectionModel() != null && classComboBox.getSelectionModel().getSelectedItem() != null) {
                    selectedClass = (SClass) classComboBox.getSelectionModel().getSelectedItem();
                }

                student.setId(student.getId() == null ? null : student.getId());
                student.setName((name == null || name.equals("")) ? null : name);
                student.setSurname((lastName == null || lastName.equals("")) ? null : lastName);
                student.setAddress((address == null || address.equals("")) ? null : address);
                student.setCreatedOn(new Date());
                student.setGender((gender == null || gender.equals("")) ? null : gender);
                student.setsClass(selectedClass);
                student.setStudentNumber((studentNumber == null || studentNumber.equals("")) ? null : studentNumber);
                student.setBirthday(birthDay);
                save(student, stage);
            }
        });


        // Exams..
        TableView<SStudentExam> studentExamsTable = new TableView<>();
        Set<SStudentExam> studentExams = student.getsStudentExams() != null ? student.getsStudentExams() : null;
        ObservableList<SStudentExam> studentExamsData = studentExams == null ? null : FXCollections.observableArrayList(studentExams);

        TableColumn examStudentName = new TableColumn();
        examStudentName.textProperty().bind(I18N.createStringBinding("student.name"));
        examStudentName.setMinWidth(100);
        examStudentName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SStudentExam, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SStudentExam, String> param) {
                ObservableValue<String> d = null;
                if(param.getValue().getsStudent() != null)
                    d = new SimpleStringProperty(param.getValue().getsStudent().getName());
                return d;
            }
        });

        TableColumn examStudentSurName = new TableColumn();
        examStudentSurName.textProperty().bind(I18N.createStringBinding("student.surname"));
        examStudentSurName.setMinWidth(100);
        examStudentSurName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SStudentExam, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SStudentExam, String> param) {
                ObservableValue<String> d = null;
                if(param.getValue().getsStudent() != null)
                    d = new SimpleStringProperty(param.getValue().getsStudent().getSurname());
                return d;
            }
        });

        TableColumn examCode = new TableColumn();
        examCode.textProperty().bind(I18N.createStringBinding("exam.code"));
        examCode.setMinWidth(100);
        examCode.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SStudentExam, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SStudentExam, String> param) {
                ObservableValue<String> d = null;
                if(param.getValue().getsExam() != null)
                    d = new SimpleStringProperty(param.getValue().getsExam().getExamCode());
                return d;
            }
        });


        Label studentExamsLabel = new Label();
        studentExamsLabel.textProperty().bind(I18N.createStringBinding("student.exams"));
        studentExamsLabel.setStyle("-fx-font-size: 14.0 pt");

        TableColumn examScore = new TableColumn();
        examScore.textProperty().bind(I18N.createStringBinding("exam.score"));
        examScore.setMinWidth(100);
        examScore.setCellValueFactory(new PropertyValueFactory<SStudentExam, String>("score"));
        studentExamsTable.setItems(studentExamsData);
        studentExamsTable.getColumns().addAll(examStudentName, examStudentSurName, examCode, examScore);




        VBox vbFormStudentDetail = new VBox(nameLabel, nameTextField, lastNameLabel, lastNameTextField, studentNumberLabel, studentNumberTextField, genderLabel, genderComboBox, classLabel, classComboBox, birthDayLabel, birthDayLabelDatePicker, addressLabel, addresTextArea, SeparatorvBox, saveButton);
        vbFormStudentDetail.setPadding(new Insets(10, 10, 10, 10));

        VBox vbFormStudentExams = new VBox(studentExamsLabel, studentExamsTable);
        vbFormStudentExams.setPadding(new Insets(10, 10, 10, 10));


        GridPane gridPane = new GridPane();
        ReadOnlyDoubleProperty fullWidth = gridPane.widthProperty();
        vbFormStudentDetail.prefWidthProperty().bind(fullWidth.multiply(0.5));
        vbFormStudentExams.prefWidthProperty().bind(fullWidth.multiply(0.5));
        gridPane.add(vbFormStudentDetail, 0, 0);
        gridPane.add(vbFormStudentExams, 1, 0);
        //HBox hbFormGeneral = new HBox(vbFormStudentDetail, vbFormStudentExams);


        VBox vbFormGeneral = new VBox(menuBar, gridPane);
        Scene scene = new Scene(vbFormGeneral, 900, 500);

        stage.titleProperty().bind(I18N.createStringBinding("application.title"));
        stage.getIcons().add(new Image(IconConstant.faviconImage));
        stage.setScene(scene);
        return stage;
    }

    private void save(SStudent student_, Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.titleProperty().bind(I18N.createStringBinding("message.title"));
        alert.headerTextProperty().bind(I18N.createStringBinding("message.areYouSure"));
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                try {
                    student = sStudentService.save(student_);
                    if(tableView != null)
                        tableView.refresh();
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.titleProperty().bind(I18N.createStringBinding("message.title"));
                    alert1.headerTextProperty().bind(I18N.createStringBinding("message.savedStudent"));
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

    public void setStudent(SStudent student) {
        this.student = student;
    }

    public void setTableView(TableView<SStudent> tableView) {
        this.tableView = tableView;
    }
}
