package com.tugasakhir.ideapedia.security;

import com.tugasakhir.ideapedia.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {
    private static final long serialVersionUID = 234234523523L;
    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60;//untuk t menit 5 * 60 * 1000
    /**
     * Function disini hanya menerima token JWT yang sudah di decrypt
     * Yang dapat di claims disini adalah key yang diinput dari api Login
     */
    public Map<String,Object> mappingBodyToken(String token,
                                               Map<String,Object> mapz){
        /** claims adalah data payload yang ada di token
         * PASTIKAN YANG DIISI SAAT PROSES LOGIN SAMA SAAT PROSES CLAIMS
         */
        Claims claims = getAllClaimsFromToken(token);
        mapz.put("userId",claims.get("uid"));
        mapz.put("userName",claims.get("un"));
        mapz.put("nip",claims.get("nip"));
        mapz.put("email",claims.get("ml"));//untuk email
        mapz.put("password",claims.get("pw"));
        mapz.put("noHp",claims.get("pn"));

        return mapz;
    }

    /**
     * KONFIGURASI CUSTOMISASI BERAKHIR DISINI
     * KONFIGURASI UNTUK JWT DIMULAI DARI SINI
     */
//    username dari token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //parameter token habis waktu nya
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //kita dapat mengambil informasi dari token dengan menggunakan secret key
    //disini juga validasi dari expired token dan lihat signature  dilakukan
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(JwtConfig.getJwtSecret()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token untuk user
    public String generateToken(UserDetails userDetails, Map<String,Object> claims) {
        claims = (claims==null)?new HashMap<String,Object>():claims;
        return doGenerateToken(claims, userDetails.getUsername());
    }
    /** proses yang dilakukan saat membuat token adalah :
     mendefinisikan claim token seperti penerbit (Issuer) , waktu expired , subject dan ID
     generate signature dengan menggunakan secret key dan algoritma HS512 (HMAC - SHA),
     */

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Long timeMilis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(timeMilis))
                .setExpiration(new Date(timeMilis + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, JwtConfig.getJwtSecret()).compact();
    }

    public Boolean validateToken(String token) {
        /** Sudah otomatis tervalidaasi jika expired date masih aktif */
        String username = getUsernameFromToken(token);
        return (username!=null && !isTokenExpired(token));
    }
    /**
     * KONFIGURASI UNTUK JWT BERAKHIR DI SINI
     */
}
