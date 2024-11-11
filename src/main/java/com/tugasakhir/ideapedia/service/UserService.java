package com.tugasakhir.ideapedia.service;

import com.tugasakhir.ideapedia.core.IFile;
import com.tugasakhir.ideapedia.core.IService;
import com.tugasakhir.ideapedia.dto.response.RespUserDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValUserDTO;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.security.BcryptImpl;
import com.tugasakhir.ideapedia.util.GlobalFunction;
import com.tugasakhir.ideapedia.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserService implements IService<User> {

    @Autowired
    private UserRepo userRepo;

    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();
    private StringBuilder sBuild = new StringBuilder();

    public Long getCurrentUserId(HttpServletRequest request) {
        // Mengambil username dari security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Cari user berdasarkan username
        Optional<User> user = userRepo.findByUsername(username);
        return user.map(User::getId).orElse(null);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> save(User user, HttpServletRequest request) {
        try {
            // Hash password dengan tambahan username
            user.setPassword(BcryptImpl.hash(user.getPassword() + user.getUsername()));

            // Set waktu pembuatan sebagai timestamp saat ini
            user.setCreatedAt(LocalDateTime.now());

            // Set createdBy berdasarkan ID user yang melakukan request
            // Misalnya, Anda bisa mendapatkan ID user dari sesi atau token
            Long createdByUserId = getCurrentUserId(request); // Implementasikan metode ini sesuai kebutuhan
            user.setCreatedBy(createdByUserId);

            // Simpan data user
            userRepo.save(user);

            // Siapkan response dengan data user yang baru dibuat
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", user.getId());
            responseData.put("username", user.getUsername());
            responseData.put("email", user.getEmail());
            responseData.put("createdAt", user.getCreatedAt());
            responseData.put("createdBy", user.getCreatedBy());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    GlobalFunction.dataUserBerhasilDisimpan(request, responseData)
            );

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return GlobalFunction.dataGagalDisimpan("FEAUT004001", request);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> update(Long id, User user, HttpServletRequest request) {
        try{
            Optional<User> optionalUser = userRepo.findById(id);
            if(!optionalUser.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            User userNext = optionalUser.get();
            userNext.setUsername(user.getUsername());
            userNext.setNip(user.getNip());
            userNext.setEmail(user.getEmail());
            userNext.setPassword(user.getPassword());
            userNext.setNoHp(user.getNoHp());
            userNext.setUnitKerja(user.getUnitKerja());
        }catch (Exception e) {
            return GlobalFunction.dataGagalDisimpan("FEAUT004011",request);//011-020
        }
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try {
            Optional<User> optionalUser = userRepo.findById(id);
            if(!optionalUser.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            userRepo.deleteById(id);
        }catch (Exception e){
            return GlobalFunction.dataGagalDihapus("FEAUT004021",request);//021-030
        }
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<User> page = null;
        List<User> list = null;
        page=userRepo.findAll(pageable);
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespUserDTO(list),
                page,
                null,null
        );
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<User> optionalUser = userRepo.findById(id);
        if(!optionalUser.isPresent()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }
        User user = optionalUser.get();

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(user, RespUserDTO.class));
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<User> page = null;
        List<User> list = null;
        switch (columnName){
            case "email":page=userRepo.findByEmailContainingIgnoreCase(pageable,value);break;
            case "nip":page=userRepo.findByNipContainingIgnoreCase(pageable,value);break;
            case "noHp":page=userRepo.findByNoHpContainingIgnoreCase(pageable,value);break;
            case "userName":page=userRepo.findByUsernameContainingIgnoreCase(pageable,value);break;
            default:page=userRepo.findAll(pageable);
        }
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespUserDTO(list),
                page,
                columnName,value
        );
    }

    public User convertToEntity(ValUserDTO valUserDTO){
        return modelMapper.map(valUserDTO, User.class);
    }

    public List<RespUserDTO> convertToListRespUserDTO(List<User> users){
        return modelMapper.map(users,new TypeToken<List<RespUserDTO>>(){}.getType());
    }

}