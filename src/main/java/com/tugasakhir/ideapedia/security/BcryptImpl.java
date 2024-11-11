package com.tugasakhir.ideapedia.security;

import java.util.function.Function;

public class BcryptImpl {

    private static final BcryptCustom bcrypt = new BcryptCustom(11);

    public static String hash(String password) {
        return bcrypt.hash(password);
    }

    public static boolean verifyAndUpdateHash(String password,
                                              String hash,
                                              Function<String, Boolean> updateFunc) {
        return bcrypt.verifyAndUpdateHash(password, hash, updateFunc);
    }

    public static boolean verifyHash(String password , String hash)
    {
        return bcrypt.verifyHash(password,hash);
    }

    public static void main(String[] args) {
        String strUserName = "paulch123";
        String password = "123456";


        System.out.println(hash(password+strUserName));
        System.out.println(verifyHash(password + strUserName,"$2a$11$so5sOX98YPHhWETRO6W8Je94CFhTi9LuWtSwgTz1n0eBPlBJbkjgW"));
        //System.out.println(verifyHash(password + strUserName,"$11$f0rQ7iB8CuTckPsIO8uM3e4ri2LXlIilFaCj1AIvve.sNriUT6COS"));
    }
}
