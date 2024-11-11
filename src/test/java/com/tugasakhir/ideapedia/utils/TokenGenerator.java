package com.tugasakhir.ideapedia.utils;

import com.tugasakhir.ideapedia.controller.AuthControllerTest;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class TokenGenerator {

    private JSONObject req;
    private String token;
    public TokenGenerator(String token) {
        /** kalau token nya null , artinya kita mau melakukan testing tanpa harus dari AuthController */
        if(token == null){
            req = new JSONObject();
            /** seluruh testing URI nya berasal dari sini
             * Tidak perlu dibuatkan environment variable karena tidak akan di deploy ke server production
             */
            RestAssured.baseURI = "http://localhost:8080";
            doLoginManual();
        }else {
            /** untuk integration ataupun suite test agar menjaga tidak perlu request token berulang-ulang
             * dilakukan estafet pemanggilan constructor ini, jadi otomatis akan di skip proses request token lagi
             * dan value token sebelumnya di oper ke proses testing selanjut nya
             */
            this.token = "Bearer "+token;//gunakan Bearer sebelum token
        }
    }


    public void doLoginManual(){
        /** masukkan credentials admin sebagai default untuk proses login */
        req.put("username", AuthControllerTest.USER_NAME);
        req.put("password", AuthControllerTest.PASSWORD);
        Response response;
        ResponseBody responseBody = null;

        try {
            HttpClientConfig httpClientConfig = HttpClientConfig.httpClientConfig().
                    setParam("http.socket.timeout",60000).
                    setParam("http.connection.timeout",60000);
            config = RestAssuredConfig.config().httpClient(httpClientConfig);
            response =  given().
                    header("Content-Type","application/json").
                    header("accept","*/*").body(req).request(Method.POST, "auth/login");
            int intResponse = response.getStatusCode();
            JsonPath jPath = response.jsonPath();
//            System.out.println("Response TOken : "+ response.jsonPath().prettyPrint());
            this.token = "Bearer "+jPath.get("token");//gunakan Bearer sebelum token
            /** EXIT OTENTIKASI BERMASALAH
             * KARENA PERCUMA SAJA MELAKUKAN TESTING KALAU TIDAK MENDAPATKAN TOKEN
             */
            if(intResponse != 200 || token==null){
                System.out.println("ADMIN USERNAME DAN PASSWORD TIDAK ADA, SISTEM AKAN DIHENTIKAN PROSES NYA");
                System.exit(0);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getToken() {
        return token;
    }
}
