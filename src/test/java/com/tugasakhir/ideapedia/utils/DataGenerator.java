package com.tugasakhir.ideapedia.utils;

import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataGenerator {
    private Faker faker = new Faker(new Locale("in_ID","ID"));
    private boolean isValid = false;
    private Matcher matcher = null;
    private int intLoop = 0;

    public String dataEmail() {
        isValid = false;
        intLoop = 0;
        String email = "";
        while(!isValid){
            try{
                email = faker.internet().emailAddress();
                matcher = Pattern.compile("^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$").matcher(email);
                isValid = matcher.find();
                if(intLoop==250){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA Email SEBANYAK 250 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }

        return email;
    }

    public String dataNoHp() {
        isValid = false;
        intLoop = 0;
        String noHp = "";
        while(!isValid){
            try{
                noHp = faker.phoneNumber().phoneNumber();
//                matcher = Pattern.compile("^(08|62|\\+62)(21|12|13|14|15|52|53|96|95|78|17|18|19|56|57|55)\\d{7,13}$").matcher(noHp);
                matcher = Pattern.compile("^(62|\\+62|0)8[0-9]{9,11}$").matcher(noHp);
                isValid = matcher.find();
                if(intLoop==250){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA No HP SEBANYAK 250 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }
        return noHp;
    }

    public String dataUsername() {
        isValid = false;
        intLoop = 0;
        String usrName = "";
        while(!isValid){
            try{
                usrName = faker.name().username();
                matcher = Pattern.compile("^([a-z\\.]{7,15})$").matcher(usrName);
                isValid = matcher.find();
                if(intLoop==250){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA Username SEBANYAK 250 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }
        return usrName;
    }

    public String dataNamaDepan() {
        isValid = false;
        intLoop = 0;
        String namaDepan = "";
        while(!isValid){
            try{
                namaDepan = faker.name().firstName();
                matcher = Pattern.compile("^[a-zA-Z\\s?]{7,15}$").matcher(namaDepan);
                isValid = matcher.find();
                if(intLoop==250){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA Nama Depan SEBANYAK 250 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }
        return namaDepan;
    }

    public String dataNamaTengah() {
        isValid = false;
        intLoop = 0;
        String namaTengah = "";
        while(!isValid){
            try{
                namaTengah = faker.name().nameWithMiddle().split(" ")[1];
                matcher = Pattern.compile("^[a-zA-Z\\s?]{7,15}$").matcher(namaTengah);
                isValid = matcher.find();
                if(intLoop==250){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA Nama Tengah SEBANYAK 250 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }
        return namaTengah;
    }

    public String dataNamaBelakang() {
        isValid = false;
        intLoop = 0;
        String namaBelakang = "";
        while(!isValid){
            try{
                namaBelakang = faker.name().lastName();
                matcher = Pattern.compile("^[a-zA-Z]{7,15}$").matcher(namaBelakang);
                isValid = matcher.find();
                if(intLoop==250){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA Nama Belakang SEBANYAK 250 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }
        return namaBelakang;
    }

    public String dataNamaLengkap() {
        isValid = false;
        intLoop = 0;
        String namaLengkap = "";
        while(!isValid){
            try{
                namaLengkap = faker.name().fullName();
                matcher = Pattern.compile("^[a-zA-Z\\s\\.,]{3,50}$$").matcher(namaLengkap);
                isValid = matcher.find();
                if(intLoop==250){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA Nama Lengkap SEBANYAK 250 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }
        return namaLengkap;
    }

    public String dataKota() {
        isValid = false;
        intLoop = 0;
        String namaKota   = "";
        namaKota = faker.address().cityName();
        return namaKota;
    }

    public String dataNamaTim() {
        isValid = false;
        intLoop = 0;
        String namaKota   = "";
        namaKota = faker.team().name();
        return namaKota;
    }

    public String dataJudulBuku() {
        isValid = false;
        intLoop = 0;
        String judulBuku   = "";
        judulBuku = faker.book().title();
        return judulBuku;
    }

    public String dataIPAddress() {
        isValid = false;
        intLoop = 0;
        String ipAddress   = "";
        ipAddress = faker.internet().ipV4Address();
        return ipAddress;
    }

    public String dataKodePos() {
        isValid = false;
        intLoop = 0;
        String kodepos   = "";
        kodepos = faker.address().zipCode();
        return kodepos;
    }

    public String dataTanggalLahir() {
        isValid = false;
        intLoop = 0;
        String tglLahir = "";
        while(!isValid){
            try{
                tglLahir = new SimpleDateFormat("yyyy-MM-dd").format(faker.date().birthday());
                matcher = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$").matcher(tglLahir);
                isValid = matcher.find();
                if(intLoop==250){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA Tanggal Lahir SEBANYAK 250 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }
        return tglLahir;
    }

    public String dataAlamat() {
        isValid = false;
        intLoop = 0;
        String alamat = "";
        while(!isValid){
            try{
                alamat = faker.address().fullAddress();
                matcher = Pattern.compile("^[\\w\\s\\.\\,]{20,255}$").matcher(alamat);
                isValid = matcher.find();
                if(intLoop==250){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA Alamat SEBANYAK 250 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }
        return alamat;
    }

    public String dataPassword() {
        isValid = false;
        intLoop = 0;
        String password = "";
        while(!isValid){
            try{
                password = faker.internet().password(8,13,true,true,true);
//                matcher = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#\\-$])(?!.*?[^A-Za-z0-9_#\\-$]).{8,}$").matcher(password);
                matcher = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[_#\\-$])[\\w].{8,16}$").matcher(password);
//                System.out.println(password);
                isValid = matcher.find();
//                System.out.println(isValid);
                if(intLoop==200){
                    System.out.println("SUDAH MENCOBA MEMBUAT DATA PASSWORD SEBANYAK 200 KALI DAN GAGAL !!");
                    System.exit(1);
                }
                intLoop++;
            }catch (Exception e){
                isValid = false;
            }
        }
        return password;
    }
}
