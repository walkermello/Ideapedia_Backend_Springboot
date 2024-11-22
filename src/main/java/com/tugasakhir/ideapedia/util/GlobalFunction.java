package com.tugasakhir.ideapedia.util;

import com.tugasakhir.ideapedia.config.OtherConfig;
import com.tugasakhir.ideapedia.handler.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class GlobalFunction {

    public static void println(Object obj){
        if(OtherConfig.getEnablePrint().equals("y")){
            System.out.println(obj);
        }
    }

    public static ResponseEntity<Object> dataUserBerhasilDisimpan(HttpServletRequest request, Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data); // pastikan data dimasukkan dalam response
        response.put("success", true);
        response.put("message", "DATA BERHASIL DISIMPAN");
        response.put("status", HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static ResponseEntity<Object> dataGagalDisimpan(String errorCode,HttpServletRequest request){
        return new ResponseHandler().
                generateResponse("DATA GAGAL DISIMPAN",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,errorCode,request);
    }

    public static ResponseEntity<Object> dataGagalDihapus(String errorCode,HttpServletRequest request){
        return new ResponseHandler().
                generateResponse("DATA GAGAL DIHAPUS",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,errorCode,request);
    }

    public static ResponseEntity<Object> dataBerhasilDiubah(HttpServletRequest request){
        return new ResponseHandler().
                generateResponse("DATA BERHASIL DIUBAH",
                        HttpStatus.OK,
                        null,null,request);
    }
    public static ResponseEntity<Object> dataBerhasilDihapus(HttpServletRequest request){
        return new ResponseHandler().
                generateResponse("DATA BERHASIL DIHAPUS",
                        HttpStatus.OK,
                        null,null,request);
    }

    public static ResponseEntity<Object> dataTidakDitemukan(HttpServletRequest request){
        return new ResponseHandler().
                generateResponse("DATA TIDAK DITEMUKAN",
                        HttpStatus.NOT_FOUND,
                        null,"G-01-002",request);
    }

    public static ResponseEntity<Object> unhideIdea(HttpServletRequest request){
        return new ResponseHandler().
                generateResponse("DATA BERHASIL DIUNHIDE",
                        HttpStatus.OK,
                        null,"G-01-002",request);
    }

    //Global FUnction For Unit Kerja
    public static ResponseEntity<Object> dataUnitKerjaBerhasilDisimpan(HttpServletRequest request, Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data); // pastikan data dimasukkan dalam response
        response.put("success", true);
        response.put("message", "DATA BERHASIL DISIMPAN");
        response.put("status", HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //Global FUnction For Idea
    public static ResponseEntity<Object> dataIDeaBerhasilDisimpan(HttpServletRequest request, Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("success", true);
        response.put("message", "DATA BERHASIL DISIMPAN");
        response.put("status", HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    //Global FUnction For Detail Idea
    public static ResponseEntity<Object> dataDetailIDeaBerhasilDisimpan(HttpServletRequest request, Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("success", true);
        response.put("message", "APPROVED");
        response.put("status", HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //Global Function For Bookmark
    public static ResponseEntity<Object> dataBookmarkBerhasilDisimpan(HttpServletRequest request, Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("success", true);
        response.put("message", "BOOKMARKED");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static String formatingDateDDMMMMYYYY(String strDate,String patternBefore, String patternAfter) {
        String s = "";
        try{
            s = new SimpleDateFormat(patternAfter).
                    format(new SimpleDateFormat(patternBefore, Locale.ENGLISH).parse(strDate));
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

    public static Map<String,Object> convertClassToObject(Object object){
        Map<String, Object> map = new LinkedHashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
//                LoggingFile.exceptionStringz("GlobalFunction","convertClassToObject",e, OtherConfig.getFlagLogging());
            }
        }
        return map;
    }

    public static Map<String,Object> convertClassToObject(Object object,String strNull){
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object)==null?"-":field.get(object));
            } catch (IllegalAccessException e) {
//                LoggingFile.exceptionStringz("GlobalFunction","convertClassToObject",e, OtherConfig.getFlagLogging());
            }
        }
        return map;
    }

    /** saya hardcode pattern nya karena kebutuhan report aja , jadi tidak perlu memasukkan pattern saat menggunakan functional ini */
    public static String formatingDateDDMMMMYYYY(){
        /** mengambil current time saat ini **/
        return new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(new Date());
    }

    public static String camelToStandar(String str)
    {
        StringBuilder sb = new StringBuilder();
        char c = str.charAt(0);
        sb.append(Character.toLowerCase(c));
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                sb.append(' ').append(Character.toLowerCase(ch));
            }
            else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }
}