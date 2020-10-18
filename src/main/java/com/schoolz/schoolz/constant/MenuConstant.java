package com.schoolz.schoolz.constant;

import com.schoolz.schoolz.app.I18N;
import com.schoolz.schoolz.dto.MenuDTO;
import com.schoolz.schoolz.entity.SClass;
import com.schoolz.schoolz.entity.SExam;
import com.schoolz.schoolz.entity.SStudent;
import com.schoolz.schoolz.entity.SStudentExam;
import com.schoolz.schoolz.screen.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class MenuConstant {

    @Autowired
    private SClassesScreen sClassesScreen;

    @Autowired
    private SClassScreen sClassScreen;

    @Autowired
    private StudentsScreen studentsScreen;

    @Autowired
    private StudentScreen studentScreen;

    @Autowired
    private SExamsScreen sExamsScreen;

    @Autowired
    private SExamScreen sExamScreen;

    @Autowired
    private SStudentScoreScreen sStudentScoreScreen;

    @Autowired
    private SFileOperationsScreen fileOperationsScreen;



    // Recursive, MenuFormat: #id|title|icon|parentId|order
    private MenuBar buildTree(List<MenuDTO> menuModels, MenuBar menuBar, Menu parentMenu) {
        List<MenuDTO> willBeAddedMenus = menuModels.stream().filter(menu_ -> menu_.getParentId() == (parentMenu.getId() == null ? null : Integer.valueOf(parentMenu.getId()))).collect(Collectors.toList());
        willBeAddedMenus.forEach(menu -> {
            if(menuModels.size() > 0){
                if(menu.getParentId() == null){
                    Menu m = createMenu(menu.getId().toString(), menu.getTitle());
                    menuBar.getMenus().add(m);
                    menuModels.removeIf(v-> v.getId().toString().equals(m.getId()));
                    buildTree(menuModels, menuBar, m);
                }else{
                    List<MenuDTO> haveChildMenuItem = new ArrayList<>();
                    if(parentMenu != null && parentMenu.getId() != null)
                        haveChildMenuItem = menuModels.stream().filter(menu_ -> menu_.getParentId() == (Integer.valueOf(menu.getId()))).collect(Collectors.toList());
                    if(haveChildMenuItem.size() > 0) {
                        Menu m = createMenu(menu.getId().toString(), menu.getTitle());
                        parentMenu.getItems().add(m);
                        menuModels.removeIf(v -> v.getId().toString().equals(m.getId()));
                        buildTree(menuModels, menuBar, m);
                    }else{
                        MenuItem m = createMenuItem(menu.getId().toString(), menu.getTitle());
                        m.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent e) {
                                actionMenuItem(((MenuItem)e.getSource()).getId());
                            }
                        });

                        parentMenu.getItems().add(m);
                        menuModels.removeIf(v -> v.getId().toString().equals(m.getId()));
                        buildTree(menuModels, menuBar, new Menu());
                    }
                }
            }
        });
        return menuBar;
    }

    private void actionMenuItem(String id) {
        try {
            List<MenuDTO> menus = AppConstant.initializeMenuItems();
            MenuDTO m = menus.stream().filter(d -> d.getId().toString().equals(id)).findFirst().get();
            if (m.getAction().equals("students")) {
                Stage primaryStage = new Stage();
                studentsScreen.studentsStage(primaryStage);
                primaryStage.show();
            }else if(m.getAction().equals("newStudent")){
                studentScreen.setStudent(new SStudent());
                Stage primaryStage = new Stage();
                studentScreen.studentStage(primaryStage);
                primaryStage.show();
            }else if(m.getAction().equals("changeLang")){
                String lang = m.getTitle();
                String[] arr;
                if(lang.contains("-"))
                    arr = lang.split("-");
                else
                    arr = lang.split("_");
                I18N.setLocale(new Locale(arr[0], arr[1]));
            }else if(m.getAction().equals("classes")){
                Stage primaryStage = new Stage();
                sClassesScreen.classesStage(primaryStage);
                primaryStage.show();
            }else if(m.getAction().equals("newClass")){
                sClassScreen.setSclass(new SClass());
                Stage primaryStage = new Stage();
                sClassScreen.studentStage(primaryStage);
                primaryStage.show();
            }else if(m.getAction().equals("exams")){
                Stage primaryStage = new Stage();
                sExamsScreen.examsStage(primaryStage);
                primaryStage.show();
            }else if(m.getAction().equals("newExam")){
                sExamScreen.setsExam(new SExam());
                Stage primaryStage = new Stage();
                sExamScreen.examStage(primaryStage);
                primaryStage.show();
            }else if(m.getAction().equals("newScore")){
                sStudentScoreScreen.setsStudentScore(new SStudentExam());
                Stage primaryStage = new Stage();
                sStudentScoreScreen.studentExamStage(primaryStage);
                primaryStage.show();
            }else if(m.getAction().equals("fileOperations")){
                Stage primaryStage = new Stage();
                fileOperationsScreen.fileOperationsStage(primaryStage);
                primaryStage.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add( new Image(IconConstant.menuImage));
                alert.setTitle("Warning!");
                alert.setHeaderText("Something is not going well.");
                alert.setContentText("Action value not found.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private Menu createMenu(String id, String title){
        Menu menu = new Menu();
        menu.setId(id);
        menu.textProperty().bind(I18N.createStringBinding("menu." + title));
        menu.setGraphic(new ImageView( new Image(IconConstant.menuImage)));
        return menu;
    }

    private MenuItem createMenuItem(String id, String title){
        MenuItem menuItem = new MenuItem();
        menuItem.setId(id);
        menuItem.textProperty().bind(I18N.createStringBinding("menu." + title));
        menuItem.setGraphic(new ImageView( new Image(IconConstant.menuImage)));
        return menuItem;
    }

    public MenuBar menuBar() {
        return buildTree(AppConstant.initializeMenuItems(), new MenuBar(), new Menu());
    }




}
