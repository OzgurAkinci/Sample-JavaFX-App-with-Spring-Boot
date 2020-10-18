package com.schoolz.schoolz.screen;

import com.schoolz.schoolz.app.AbstractJavaFxApplicationSupport;
import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.constant.IconConstant;
import com.schoolz.schoolz.constant.MenuConstant;
import com.schoolz.schoolz.service.SClassService;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MainScreen extends AbstractJavaFxApplicationSupport {

    @Autowired
    MenuConstant menuConstant;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MenuBar menuBar = menuConstant.menuBar();
        menuBar.setCursor(Cursor.CLOSED_HAND);
        menuBar.setStyle("-fx-font-size: 14.0 pt");
        ImageView image = new ImageView(IconConstant.faviconImage);

        Label subLabel = new Label();
        subLabel.textProperty().bind(Bindings.concat(I18N.createStringBinding("application.title").concat(""),", ",I18N.createStringBinding("application.createdBy")));
        subLabel.setStyle("-fx-font-size: 14.0 pt");
        subLabel.setPrefHeight(55);


        VBox vBoxContainer = new VBox(image, subLabel);
        vBoxContainer.setAlignment(Pos.CENTER);
        vBoxContainer.setPadding(new Insets(30, 10, 10, 10));
        VBox vBoxMain = new VBox(menuBar,vBoxContainer);
        Scene scene = new Scene(vBoxMain);

        primaryStage.titleProperty().bind(I18N.createStringBinding("application.title"));
        primaryStage.getIcons().add(new Image(IconConstant.faviconImage));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
