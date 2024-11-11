package com.tugasakhir.ideapedia.controller;

import com.tugasakhir.ideapedia.model.UnitKerja;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.utils.DataGenerator;
import com.tugasakhir.ideapedia.utils.TokenGenerator;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepo userRepo;

    private Random rand;
    private DataGenerator dataGenerator;
    private JSONObject req;
    private User user;
    private String token  ;
    private String status;
    private Boolean success;
    private String message;
    private String data;

    @BeforeClass
    private void setUp(){
        RestAssured.baseURI = "http://localhost:8080";
        rand = new Random();
        dataGenerator = new DataGenerator();
        req = new JSONObject();
        token  = AuthControllerTest.AUTH_TOKEN;
        token = new TokenGenerator(token).getToken();
//         fungsional cek token ? null atau tidak -> kalau null request kalau tidak estafet
        Optional<User> optionalUser= userRepo.findTopByOrderByIdDesc();
        user = optionalUser.get();
    }

    /** INI PASTI ERROR KALAU DITARUH DISINI, URUTAN NYA HARUS DI AWAL SEKALI , KEARENA :
     * 1. kalau setelah save nanti terpilih adalah data yang terakhir berbeda dengan data yang telah dipilih
     * 2. kalau setelah update nanti terpilih datanya tidak sama dengan data yang sudah ditampung sebelumnya...
     * jadi harus di awal sekali
     */
    @Test(priority = 1)
    private void findByIdTest(){
        RequestSpecification httpRequest = given().
                header("Content-Type","application/json").
                header("Authorization",token).
                header("accept","*/*");
        String pathVariable = "/user/"+ user.getId();
        Response response = httpRequest.request(Method.GET, pathVariable);
        ResponseBody responseBody = response.getBody();// seluruh body dari response
        System.out.println("====================================START RESPONSE BODY =================================================");
        System.out.println(responseBody.asPrettyString());// untuk melihat isi dari response body dalam bentuk JSON
        int responseCode = response.statusCode();
        JsonPath jPath = response.jsonPath();
        Long id = Long.parseLong(jPath.getString("id"));
        String email = jPath.getString("email");
        String nip = jPath.getString("nip");
        String noHp = jPath.getString("noHp");
        String username = jPath.getString("username");

        Assert.assertEquals(responseCode,200);
        Assert.assertEquals(id, user.getId());
        Assert.assertEquals(email, user.getEmail());
        Assert.assertEquals(nip, user.getNip());
        Assert.assertEquals(noHp, user.getNoHp());
        Assert.assertEquals(username, user.getUsername());
    }

    @Test(priority = 3)
    private void save(){
        req.put("email",dataGenerator.dataEmail());
        req.put("nip","20240445");
        req.put("no-hp",dataGenerator.dataNoHp());
        req.put("password",dataGenerator.dataPassword());
        req.put("username",dataGenerator.dataUsername());
        UnitKerja unitKerja = new UnitKerja();
        unitKerja.setId(1L);
        req.put("unitKerja",unitKerja);

        RequestSpecification httpRequest = given().
                header("Content-Type","application/json").
                header("Accept","*/*").
                header("Authorization",token).
                body(req);
        String pathVariable = "/user";
        Response response = httpRequest.request(Method.POST, pathVariable);
        JsonPath jPath = response.jsonPath();
        ResponseBody responseBody = response.getBody();// seluruh body dari response
        int responseCode = response.statusCode();
        System.out.println("====================================START RESPONSE BODY =================================================");
        System.out.println(responseBody.asPrettyString());// untuk melihat isi dari response body dalam bentuk JSON
        data = jPath.getString("data");
        status = jPath.getString("status");
        success = Boolean.parseBoolean(jPath.getString("success"));
        message = jPath.getString("message");
        Assert.assertEquals(data,"");
        Assert.assertEquals(responseCode,201);
        Assert.assertEquals(message,"DATA BERHASIL DISIMPAN");
        Assert.assertEquals(status,"201");
        Assert.assertEquals(success,true);
    }

    @Test(priority = 7)
    private void update(){
        req.put("username",dataGenerator.dataUsername());
        req.put("email",dataGenerator.dataEmail());
        req.put("no-hp",dataGenerator.dataNoHp());
        req.put("password",dataGenerator.dataPassword());
        req.put("tanggal-lahir",dataGenerator.dataTanggalLahir());
        req.put("alamat",dataGenerator.dataAlamat());
        req.put("nama-lengkap",dataGenerator.dataNamaLengkap());
        UnitKerja unitKerja = new UnitKerja();
        unitKerja.setId(1L);
        req.put("unitKerja",unitKerja);

        RequestSpecification httpRequest = given().
                header("Content-Type","application/json").
                header("Accept","*/*").
                header("Authorization",token).
                body(req);

        String pathVariable = "/user/"+user.getId();
        Response response = httpRequest.request(Method.PUT, pathVariable);
        int responseCode = response.statusCode();
        JsonPath jPath = response.jsonPath();
        message = jPath.getString("message");
        status = jPath.getString("status");
        success = Boolean.parseBoolean(jPath.getString("success"));
        /** kalau keempat parameter response ini sudah sama , artinya testing berhasil */
        data = jPath.getString("data");
        Assert.assertEquals(data,"");
        Assert.assertEquals(responseCode,200);
        Assert.assertEquals(message,"DATA BERHASIL DIUBAH");
        Assert.assertEquals(status,"200");
        Assert.assertEquals(success,true);
    }




    @Test(priority = 12)
    private void findAll(){
        RequestSpecification httpRequest = given().
                header("Content-Type","application/json").
                header("accept","*/*").
                header("Authorization",token);

        String pathVariable = "/user";
        Response response = httpRequest.request(Method.GET, pathVariable);
        ResponseBody responseBody = response.getBody();// seluruh body dari response
        System.out.println("====================================START RESPONSE BODY =================================================");
        System.out.println(responseBody.asPrettyString());// untuk melihat isi dari response body dalam bentuk JSON
        int responseCode = response.statusCode();
        JsonPath jPath = response.jsonPath();
        /** untuk case ini pengambilan datanya menggunakan List */
        List<Map<String,Object>> lt = jPath.getList("content");
        Long id = Long.parseLong(lt.get(0).get("id").toString());
        String nama =lt.get(0).get("namaLengkap").toString();
        status = jPath.getString("status");
        success = Boolean.parseBoolean(jPath.getString("success"));
        message = jPath.getString("message");
        Integer pageNumber = Integer.parseInt(jPath.getString("page_number"));
        String columnName = jPath.getString("column_name");
        String sort = jPath.getString("sort");
        String componentFilter = jPath.getString("component-filter");
        Integer sizePerPage = Integer.parseInt(jPath.getString("size_per_page"));
        String value = jPath.getString("value");

        Assert.assertEquals(responseCode,200);
        Assert.assertEquals(pageNumber,0);//karena diset manual seperti itu, untuk automation ada cara nya agar dynamic
        Assert.assertEquals(columnName,null);
        Assert.assertEquals(sort,"asc");
        Assert.assertEquals(componentFilter,null);
        Assert.assertEquals(value,null);
        Assert.assertEquals(sizePerPage,lt.size());//lt pasti bernilai dari size yang dihardcode di api findAll
    }

    @Test(priority = 15)
    private void uploadSheet(){
        RequestSpecification httpRequest = given().
                header("Content-Type","multipart/form-data").
                header("accept","*/*").
                header("Authorization",token).
                multiPart("file",new File(System.getProperty("user.dir")+"/src/test/resources/data-test/user.xlsx"));
        String pathVariable = "/user/upload-sheet";
        Response response = httpRequest.request(Method.POST, pathVariable);
        ResponseBody responseBody = response.getBody();// seluruh body dari response
        System.out.println("====================================START RESPONSE BODY =================================================");
        System.out.println(responseBody.asPrettyString());// untuk melihat isi dari response body dalam bentuk JSON
        int responseCode = response.statusCode();
        JsonPath jPath = response.jsonPath();
        status = jPath.getString("status");
        success = Boolean.parseBoolean(jPath.getString("success"));
        message = jPath.getString("message");
        Assert.assertEquals(responseCode,201);
        Assert.assertEquals(status,"201");
        Assert.assertEquals(success,true);
        Assert.assertEquals(message,"DATA BERHASIL DISIMPAN");
    }

    @Test(priority = 18)
    private void downloadSheet(){
        RequestSpecification httpRequest = given().
                header("Content-Type","application/json").
                header("accept","*/*").
                param("col","nama").
                header("Authorization",token).
                param("val", user.getNip());

        String pathVariable = "/user/download-sheet";
        Response response = httpRequest.request(Method.GET, pathVariable);
        ResponseBody responseBody = response.getBody();// seluruh body dari response
//        System.out.println("====================================START RESPONSE BODY =================================================");
        String prettyString = responseBody.asPrettyString();// karena bentuk nya gak karuan maka harus diambil 1 string saja sebagai pedoman dengan menggunakan fungsi string contains
//        System.out.println(prettyString);// untuk melihat isi dari response body dalam bentuk JSON
        int responseCode = response.statusCode();
        Assert.assertEquals(responseCode,200);
    }

    @Test(priority = 21)
    private void generatePDF(){
        RequestSpecification httpRequest = given().
                header("Content-Type","application/json").
                header("accept","*/*").
                param("col","nama").
                header("Authorization",token).
                param("val", user.getNip());

        String pathVariable = "/user/download-pdf";
        Response response = httpRequest.request(Method.GET, pathVariable);
        ResponseBody responseBody = response.getBody();// seluruh body dari response
        System.out.println("====================================START RESPONSE BODY =================================================");
        String prettyString = responseBody.asPrettyString();// karena bentuk nya gak karuan maka harus diambil 1 string saja sebagai pedoman dengan menggunakan fungsi string contains
        System.out.println(prettyString);// untuk melihat isi dari response body dalam bentuk JSON
        int responseCode = response.statusCode();
        Assert.assertEquals(responseCode,200);
    }

    @Test(priority = 100)
    private void delete(){
        RequestSpecification httpRequest = given().
                header("Content-Type","application/json").
                header("Authorization",token).
                header("Accept","*/*");

        String pathVariable = "/user/"+user.getId();
        Response response = httpRequest.request(Method.DELETE, pathVariable);
        ResponseBody responseBody = response.getBody();// seluruh body dari response
        System.out.println("====================================START RESPONSE BODY =================================================");
        System.out.println(responseBody.asPrettyString());// untuk melihat isi dari response body dalam bentuk JSON
        int responseCode = response.statusCode();
        JsonPath jPath = response.jsonPath();
        status = jPath.getString("status");
        message = jPath.getString("message");
        success = Boolean.parseBoolean(jPath.getString("success"));
        data = jPath.getString("data");
        Assert.assertEquals(data,"");
        Assert.assertEquals(responseCode,200);
        Assert.assertEquals(status,"200");
        Assert.assertEquals(success,true);
        Assert.assertEquals(message,"DATA BERHASIL DIHAPUS");
    }
}
