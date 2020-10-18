package com.schoolz.schoolz.screen;


import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.constant.IconConstant;
import com.schoolz.schoolz.constant.MenuConstant;
import com.schoolz.schoolz.custom.LimitedTextField;
import com.schoolz.schoolz.dao.SClassDao;
import com.schoolz.schoolz.entity.SClass;
import com.schoolz.schoolz.service.SClassService;
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
import java.util.List;
import java.util.prefs.BackingStoreException;

@Component
public class SClassScreen {
    private SClass sclass;
    private TableView tableView;

    @Autowired
    private SClassService sClassService;

    @Autowired
    MenuConstant menuConstant;

    private final ObservableList<SClass> classListData = FXCollections.observableArrayList();

    public Stage studentStage(Stage stage) throws IOException, BackingStoreException {
        List<SClass> classList = sClassService.findAll();
        classList.forEach(c-> {
            classListData.add(c);
        });

        MenuBar menuBar = menuConstant.menuBar();
        menuBar.setCursor(Cursor.CLOSED_HAND);
        menuBar.setStyle("-fx-font-size: 14.0 pt");


        Label nameLabel = new Label();
        nameLabel.textProperty().bind(I18N.createStringBinding("student.className"));
        nameLabel.setStyle("-fx-font-size: 14.0 pt");
        LimitedTextField nameTextField = new LimitedTextField();
        nameTextField.setStyle("-fx-font-size: 14.0 pt");
        nameTextField.setText(sclass.getName());

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

                sclass.setId(sclass.getId() == null ? null : sclass.getId());
                sclass.setName((name == null || name.equals("")) ? null : name);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.titleProperty().bind(I18N.createStringBinding("message.title"));
                alert.headerTextProperty().bind(I18N.createStringBinding("message.areYouSure"));
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        save(sclass, stage);
                    }
                });

            }
        });


        VBox vbForm = new VBox(nameLabel, nameTextField, SeparatorvBox, saveButton);
        vbForm.setPadding(new Insets(10, 10, 10, 10));


        VBox vBox = new VBox(menuBar,vbForm);
        Scene scene = new Scene(vBox);

        stage.titleProperty().bind(I18N.createStringBinding("application.title"));
        stage.getIcons().add(new Image(IconConstant.faviconImage));
        stage.setScene(scene);
        return stage;
    }

    private void save(SClass sclass_, Stage primaryStage) {
        try {
            sclass = sClassService.save(sclass_);
            if(tableView != null)
                tableView.refresh();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.titleProperty().bind(I18N.createStringBinding("message.title"));
            alert1.headerTextProperty().bind(I18N.createStringBinding("message.savedClass"));
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

    public SClass getSclass() {
        return sclass;
    }

    public void setSclass(SClass sclass) {
        this.sclass = sclass;
    }

    public void setTableView(TableView<SClass> tableView) {
        this.tableView = tableView;
    }
}
