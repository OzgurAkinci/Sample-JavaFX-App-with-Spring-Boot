package com.schoolz.schoolz.screen;


import com.opencsv.CSVReader;
import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.constant.AppConstant;
import com.schoolz.schoolz.constant.IconConstant;
import com.schoolz.schoolz.constant.MenuConstant;
import com.schoolz.schoolz.custom.LimitedTextField;
import com.schoolz.schoolz.entity.SClass;
import com.schoolz.schoolz.entity.SExam;
import com.schoolz.schoolz.entity.SStudent;
import com.schoolz.schoolz.entity.SStudentExam;
import com.schoolz.schoolz.service.SClassService;
import com.schoolz.schoolz.service.SExamService;
import com.schoolz.schoolz.service.SStudentExamService;
import com.schoolz.schoolz.service.SStudentService;
import com.schoolz.schoolz.util.ReportsUtil;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.script.Bindings;
import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.prefs.BackingStoreException;

@Component
public class SFileOperationsScreen {

    @Autowired
    MenuConstant menuConstant;

    @Autowired
    SStudentService sStudentService;

    @Autowired
    SExamService sExamService;

    @Autowired
    SStudentExamService sStudentExamService;

    private ListView<String> resultListBox;

    private File selectedFile;

    private String fileOutputPath;


    public Stage fileOperationsStage(Stage stage) throws IOException, BackingStoreException {
        HashMap configs = AppConstant.initializeBaseConfig();
        fileOutputPath = configs.containsKey("reportPath") ? configs.get("reportPath").toString() : null;
        MenuBar menuBar = menuConstant.menuBar();
        menuBar.setCursor(Cursor.CLOSED_HAND);
        menuBar.setStyle("-fx-font-size: 14.0 pt");

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 14.0 pt");

        FileChooser fileChooser = new FileChooser();
        fileChooser.titleProperty().bind(I18N.createStringBinding("fileOperations.openResource"));
        ImageView chooserButtonImageView = new ImageView(new Image(IconConstant.faviconImage));
        Button fileChooserButton = new Button("Select File", chooserButtonImageView);
        fileChooserButton.prefWidthProperty().bind(stage.widthProperty());
        fileChooserButton.textProperty().bind(I18N.createStringBinding("fileOperations.selectFile"));
        fileChooserButton.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(stage);
            if(selectedFile != null && selectedFile.getName() != null) {
                String fileName = selectedFile.getName();
                resultLabel.setText(fileName);
            }else{
                resultLabel.setText(null);
            }
        });


        ImageView saveButtonImageView = new ImageView(new Image(IconConstant.faviconImage));
        Button saveButton = new Button("Save", saveButtonImageView);
        saveButton.textProperty().bind(I18N.createStringBinding("button.save"));
        saveButton.setStyle("-fx-font-size: 14.0 pt");
        saveButton.prefWidthProperty().bind(stage.widthProperty());
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    importFile();
                } catch (FileNotFoundException e1) {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.titleProperty().bind(I18N.createStringBinding("message.warning"));
                    alert2.headerTextProperty().bind(I18N.createStringBinding("message.fileNotFound"));
                    alert2.setContentText(e1.getMessage());
                    alert2.showAndWait().ifPresent(rs2 -> {

                    });
                }
            }
        });


        Separator sep = new Separator();
        sep.prefWidthProperty().bind(stage.widthProperty());
        sep.setHalignment(HPos.CENTER);
        VBox SeparatorvBox = new VBox(sep);
        SeparatorvBox.setPadding(new Insets(5, 0, 5, 0));

        ImageView exportButtonImageView = new ImageView(new Image(IconConstant.faviconImage));
        Button exportButton = new Button("Export", exportButtonImageView);
        exportButton.textProperty().bind(I18N.createStringBinding("button.exportReport").concat("<Async>"));
        exportButton.setStyle("-fx-font-size: 14.0 pt");
        exportButton.prefWidthProperty().bind(stage.widthProperty());
        exportButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                    List<CompletableFuture> tasks = new ArrayList<CompletableFuture>();

                    List<SStudent> students = sStudentService.findAll();

                    students.stream().forEach(d-> {
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    exportAsync(d);
                                } catch (SQLException e1) {
                                    showFileContentError(e1.getClass() + ": " + e1.getMessage());
                                    e1.printStackTrace();
                                } catch (IOException e1) {
                                    showFileContentError(e1.getClass() + ": " + e1.getMessage());
                                    e1.printStackTrace();
                                } catch (JRException e1) {
                                    showFileContentError(e1.getClass() + ": " + e1.getMessage());
                                    e1.printStackTrace();
                                }
                            }

                        });

                    });

            }
        });


        Label resultsLabel = new Label("Export Results");
        resultsLabel.setStyle("-fx-font-size: 14.0 pt");


        resultListBox = new ListView<>();
        resultListBox.setStyle("-fx-font-size: 14.0 pt");
        resultListBox.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        resultListBox.setPrefHeight(300);


        VBox vb1 = new VBox(fileChooserButton, resultLabel);
        vb1.setPadding(new Insets(10, 10, 10, 10));

        VBox vb2 = new VBox(saveButton);
        vb2.setPadding(new Insets(10, 10, 10, 10));

        GridPane gridPane = new GridPane();
        ReadOnlyDoubleProperty fullWidth = gridPane.widthProperty();
        vb1.prefWidthProperty().bind(fullWidth.multiply(0.5));
        vb2.prefWidthProperty().bind(fullWidth.multiply(0.5));
        gridPane.add(vb1, 0, 0);
        gridPane.add(vb2, 1, 0);


        VBox vbForm = new VBox(gridPane, SeparatorvBox, exportButton, resultsLabel, resultListBox);
        vbForm.setPadding(new Insets(10, 10, 10, 10));
        VBox vBox = new VBox(menuBar,vbForm);
        Scene scene = new Scene(vBox, 600, 500);
        stage.titleProperty().bind(I18N.createStringBinding("application.title"));
        stage.getIcons().add(new Image(IconConstant.faviconImage));
        stage.setScene(scene);
        return stage;
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture exportAsync(SStudent student) throws SQLException, IOException, JRException {
        String studentName = student.getName();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", student.getId());
        byte[] reportByteArr = ReportsUtil.exportPDF(parameters, "StudentReport");

        try{
            String path = fileOutputPath + "\\report-" + student.getStudentNumber() + ".pdf";
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos.write(reportByteArr);
            fos.flush();
        }catch (Exception e1){
            showFileContentError(e1.getClass() + ": " + e1.getMessage());
        }

        resultListBox.getItems().add("Created report : " + studentName + ". Time: " + java.util.Calendar.getInstance().getTime());

        return null;
    }

    /*public String exportReport2(SStudent student) throws IOException, SQLException, JRException {
        String studentName = student.getName();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", student.getId());
        byte[] reportByteArr = ReportsUtil.exportPDF(parameters, "StudentReport");
        resultListBox.getItems().add("Created report : " + studentName);

        try{
            File file = new File("D:\\reports\\export.pdf");
            FileOutputStream fos = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos.write(reportByteArr);
            fos.flush();
        }catch (Exception e){

        }


        return null;
    }
*/

    private void importFile() throws FileNotFoundException {
        if(selectedFile != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.titleProperty().bind(I18N.createStringBinding("message.title"));
            alert.headerTextProperty().bind(I18N.createStringBinding("message.areYouSure"));
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    try {
                        CSVReader reader = new CSVReader(new FileReader(selectedFile), ';');
                        String[] record = null;
                        List<Map> records = new ArrayList<>();
                        while ((record = reader.readNext()) != null) {
                            Map map = new HashMap();
                            String studentNumber = record[0];
                            String examCode = record[1];
                            String score = record[2];
                            map.put("studentNumber", studentNumber);
                            map.put("examCode", examCode);
                            map.put("score", score);
                            records.add(map);
                        }

                        saveRecords(records);
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.titleProperty().bind(I18N.createStringBinding("message.title"));
                        alert1.headerTextProperty().bind(I18N.createStringBinding("message.savedStudentExam"));
                        alert1.showAndWait().ifPresent(rs1 -> {
                            if (rs1 == ButtonType.OK) {
                                //primaryStage.close();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        showFileContentError(e.getMessage());
                    } catch (IOException e) {
                        showFileContentError(e.getMessage());
                    } catch (IndexOutOfBoundsException e){
                        showFileContentError(e.getMessage());
                    }
                }
            });
        }else{
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.titleProperty().bind(I18N.createStringBinding("message.warning"));
            alert2.headerTextProperty().bind(I18N.createStringBinding("message.fileNotFound"));
            alert2.showAndWait().ifPresent(rs2 -> {

            });
        }
    }

    private void saveRecords(List<Map> records) {
        records.forEach(d -> {
            SStudent student = null;
            SExam exam = null;

            String studentNumber_ = d.getOrDefault("studentNumber", "").toString();
            List<SStudent> students = sStudentService.findByStudentNumber(studentNumber_);

            if (students != null && students.size() > 0) {
                student = students.get(0);
            }

            String examCode_ = d.getOrDefault("examCode", "").toString();
            List<SExam> exams = sExamService.findByExamCode(examCode_);

            if (exams != null && exams.size() > 0) {
                exam = exams.get(0);
            }

            Integer score = Integer.valueOf(d.getOrDefault("score", 0).toString());

            if(student != null && exam != null && score != null){
                SStudentExam sStudentExam = new SStudentExam();
                sStudentExam.setScore(score);
                sStudentExam.setsExam(exam);
                sStudentExam.setsStudent(student);
                sStudentExamService.save(sStudentExam);
            }
        });
    }

    private void showFileContentError(String errMsg) {
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.titleProperty().bind(I18N.createStringBinding("message.warning"));
        alert2.headerTextProperty().bind(I18N.createStringBinding("message.fileContentError"));
        alert2.setContentText(errMsg);
        alert2.showAndWait().ifPresent(rs2 -> {

        });
    }

}
