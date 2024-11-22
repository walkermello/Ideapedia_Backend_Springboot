package com.tugasakhir.ideapedia.service;

import com.tugasakhir.ideapedia.core.IService;
import com.tugasakhir.ideapedia.dto.response.RespUserDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValUserDTO;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.security.BcryptImpl;
import com.tugasakhir.ideapedia.security.JwtUtility;
import com.tugasakhir.ideapedia.util.GlobalFunction;
import com.tugasakhir.ideapedia.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserService implements IService<User> {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtility jwtUtil;

    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();
    private StringBuilder sBuild = new StringBuilder();

    @Override
    @Transactional
    public ResponseEntity<Object> save(User user, HttpServletRequest request) {
        try {
            // Hash password dengan tambahan username
            user.setPassword(BcryptImpl.hash(user.getPassword() + user.getUsername()));

            // Set waktu pembuatan sebagai timestamp saat ini
            user.setCreatedAt(LocalDateTime.now());

            Long currentUserId = getCurrentUserId(request);
            Optional<User> currentUser = userRepo.findById(currentUserId);
            if (currentUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User tidak ditemukan.");
            }
            user.setCreatedBy(currentUser.get().getId());
            user.setStatus("Activated");
            user.setImgProfile("D:/ideapedia/uploads/images/img.png");

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
            // Mengubah status menjadi 'Hidden'
            User user = optionalUser.get();
            user.setStatus("Deleted");
            userRepo.save(user);
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
            case "status":page=userRepo.findByStatusContainingIgnoreCase(pageable,value);break;
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

    public ResponseEntity<Resource> getImage(Long userId) {
        try {
            // Mendapatkan objek User berdasarkan ID
            Optional<User> optionalUser = userRepo.findById(userId);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            User user = optionalUser.get();

            // Mendapatkan path gambar (dari database, yang disimpan secara lokal)
            String imagePath = user.getImgProfile();
            if (imagePath == null || imagePath.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Membuat path dan resource dari file lokal
            Path path = FileSystems.getDefault().getPath(imagePath);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Mengatur header respons untuk mendukung pengambilan gambar
            String contentDisposition = "inline; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // Sesuaikan dengan tipe media jika gambar dalam format lain (misalnya PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // Utility method to get current user's ID from the token
    public Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.trim().isEmpty()) {
            System.out.println("Token not found in request headers.");
            return null;
        }

        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " part
            }

            return jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            System.out.println("Error processing token: " + e.getMessage());
            return null;
        }
    }

    public User convertToEntity(ValUserDTO valUserDTO){
        return modelMapper.map(valUserDTO, User.class);
    }

    public List<RespUserDTO> convertToListRespUserDTO(List<User> users){
        return modelMapper.map(users,new TypeToken<List<RespUserDTO>>(){}.getType());
    }

}
