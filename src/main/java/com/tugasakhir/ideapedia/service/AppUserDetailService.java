package com.tugasakhir.ideapedia.service;

import com.tugasakhir.ideapedia.dto.validasi.ValLoginDTO;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.security.BcryptImpl;
import com.tugasakhir.ideapedia.security.Crypto;
import com.tugasakhir.ideapedia.security.JwtUtility;
import com.tugasakhir.ideapedia.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AppUserDetailService implements UserDetailsService {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtility jwtUtility;

    private ModelMapper modelMapper = new ModelMapper();
    public ResponseEntity<Object> doLogin(User user , HttpServletRequest request){

        Optional<User> optionalUser = userRepo.findByUsername(user.getUsername());
        if(!optionalUser.isPresent()){
            return GlobalFunction.dataTidakDitemukan(request);
        }

        User nextUser = optionalUser.get();
        if(!BcryptImpl.verifyHash((user.getPassword()+user.getUsername()),nextUser.getPassword())){
            return GlobalFunction.dataTidakDitemukan(request);
        }

        UserDetails userDetails = loadUserByUsername(user.getUsername());
        /** start jwt config */
        Map<String,Object> mapForClaims = new HashMap<>();
        mapForClaims.put("uid",nextUser.getId());//payload
        mapForClaims.put("un",nextUser.getUsername());//payload
        mapForClaims.put("nip",nextUser.getNip());//payload
        mapForClaims.put("ml",nextUser.getEmail());//payload
        mapForClaims.put("pw",nextUser.getPassword());//payload
        mapForClaims.put("pn",nextUser.getNoHp());//payload
        mapForClaims.put("pp",nextUser.getImgProfile());
        String token = jwtUtility.generateToken(userDetails,mapForClaims);
        // Create response map
        Map<String, Object> m = new HashMap<>();
        m.put("token", Crypto.performEncrypt(token));

        // Add user details to the response
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", nextUser.getId());
        userData.put("username", nextUser.getUsername());
        userData.put("nip", nextUser.getNip());
        userData.put("email", nextUser.getEmail());
        userData.put("noHp", nextUser.getNoHp());
        userData.put("unit_kerja", nextUser.getUnitKerja());
        userData.put("img_profile", nextUser.getImgProfile());

        m.put("user", userData); // Add user details to response map

        return ResponseEntity.status(HttpStatus.OK).body(m);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username");
        }

        User userNext = optionalUser.get();

        // Return UserDetails with only username and password, omitting authorities
        return new org.springframework.security.core.userdetails.User(
                userNext.getUsername(),
                userNext.getPassword(),
                new ArrayList<>()
        );
    }

    public User convertToEntiy(ValLoginDTO valLoginDTO){
        return modelMapper.map(valLoginDTO, User.class);
    }

}
