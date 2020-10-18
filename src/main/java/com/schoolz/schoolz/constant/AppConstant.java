package com.schoolz.schoolz.constant;


import com.schoolz.schoolz.SchoolZApplication;
import com.schoolz.schoolz.dto.MenuDTO;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class AppConstant {
    private AppConstant() {}

    public static Preferences readIniFile(String filePath) throws IOException {
        Ini ini = new Ini(new File(getWorkingDirectoryPath() + filePath));
        Preferences prefs = new IniPreferences(ini);
        return prefs;
    }

    public static String getApplicationPath(){
        CodeSource codeSource = SchoolZApplication.class.getProtectionDomain().getCodeSource();
        File rootPath = null;
        try {
            rootPath = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return rootPath.getParentFile().getPath();
    }

    public static String getWorkingDirectoryPath() {
        return System.getProperty("user.dir");
    }

    public static List<MenuDTO> initializeMenuItems() {
        List<MenuDTO> menuModels = new ArrayList<MenuDTO>();
        try {
            Preferences prefs = AppConstant.readIniFile("/config.ini");
            Preferences langPrefs = prefs.node("menu");
            String[] keys = langPrefs.keys();
            for(int i=0; i<keys.length; i++){
                String row = langPrefs.get(String.valueOf(i), "No value for this key");
                String[] rowPropertyArray = row.split("\\|");
                MenuDTO tempMenuModel = new MenuDTO();
                tempMenuModel.setId(rowPropertyArray[0].equals("") ? null : Integer.valueOf(rowPropertyArray[0]));
                tempMenuModel.setTitle(rowPropertyArray[1]);
                tempMenuModel.setIcon(rowPropertyArray[2]);
                tempMenuModel.setParentId(rowPropertyArray[3].equals("") ? null : Integer.valueOf(rowPropertyArray[3]));
                tempMenuModel.setOrder(rowPropertyArray[4].equals("") ? null : Integer.valueOf(rowPropertyArray[4]));
                tempMenuModel.setAction(rowPropertyArray[5]);
                menuModels.add(tempMenuModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        return menuModels;
    }

    public static HashMap initializeBaseConfig() throws IOException, BackingStoreException {
        HashMap map = new HashMap();
        Preferences prefs = readIniFile("/config.ini");
        Preferences baseConfigPrefs = prefs.node("baseConfig");
        String[] keys = baseConfigPrefs.keys();
        for(int i=0; i<keys.length; i++){
            String key = keys[i];
            String value = baseConfigPrefs.get(key, "No value for this key");
            map.put(key, value);
        }
        return map;
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertToDate(LocalDate dateToConvert) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(dateToConvert.atStartOfDay(defaultZoneId).toInstant());
    }
}
