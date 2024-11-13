package com.tugasakhir.ideapedia.service;

import com.tugasakhir.ideapedia.core.IDetail;
import com.tugasakhir.ideapedia.core.IService;
import com.tugasakhir.ideapedia.dto.response.RespDetailIdeaDTO;
import com.tugasakhir.ideapedia.dto.response.RespUnitKerjaDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValDetailIdeaDTO;
import com.tugasakhir.ideapedia.model.DetailIdea;
import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.repo.DetailIdeaRepo;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.repo.UnitKerjaRepo;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.security.JwtUtility;
import com.tugasakhir.ideapedia.util.GlobalFunction;
import com.tugasakhir.ideapedia.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
public class DetailIdeaService implements IDetail<DetailIdea> {

    @Autowired
    private DetailIdeaRepo detailIdeaRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private JwtUtility jwtUtil;  // Injeksi dependensi jwtUtil

    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    @Transactional
    public ResponseEntity<Object> approve(DetailIdea detailIdea, HttpServletRequest request) {
        try {
            // Konversi LocalDate ke java.util.Date
            Date approvalDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            detailIdea.setApprovalDate(approvalDate);

            // Set ideaId sesuai dengan ID Idea yang dipilih
            Long ideaId = detailIdea.getIdea().getId();  // Pastikan 'idea' bukan null

            // Mengambil objek Idea berdasarkan ID
            Optional<Idea> ideaOptional = ideaRepo.findById(ideaId);
            if (ideaOptional.isPresent()) {
                detailIdea.setIdea(ideaOptional.get());  // Set objek Idea yang ditemukan
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Idea tidak ditemukan.");
            }

            detailIdea.setStatus("Approved");
            detailIdea.setComments("Approved");

            // Mendapatkan user berdasarkan ID pengguna yang sedang login
            Long currentUserId = getCurrentUserId(request);
            Optional<User> currentUser = userRepo.findById(currentUserId);
            if (currentUser.isPresent()) {
                detailIdea.setApprovedBy(currentUser.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User tidak ditemukan.");
            }

            detailIdeaRepo.save(detailIdea);

            // Membuat response data yang hanya berisi informasi yang diperlukan
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", detailIdea.getId());
            responseData.put("approvalDate", detailIdea.getApprovalDate());
            responseData.put("comments", detailIdea.getComments());
            responseData.put("status", detailIdea.getStatus());

            // Menyaring data terkait Idea
            Map<String, Object> ideaData = new HashMap<>();
            ideaData.put("id", detailIdea.getIdea().getId());
            ideaData.put("judul", detailIdea.getIdea().getJudul());
            ideaData.put("deskripsi", detailIdea.getIdea().getDeskripsi());
            ideaData.put("fileName", detailIdea.getIdea().getFileName());
            ideaData.put("createdAt", detailIdea.getIdea().getCreatedAt());

            // Menyaring data terkait User yang mengajukan
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", detailIdea.getIdea().getUser().getId());
            userData.put("username", detailIdea.getIdea().getUser().getUsername());
            userData.put("nip", detailIdea.getIdea().getUser().getNip());
            userData.put("email", detailIdea.getIdea().getUser().getEmail());
            userData.put("noHp", detailIdea.getIdea().getUser().getNoHp());

            responseData.put("idea", ideaData);  // Menyertakan detail Idea
            responseData.put("user", userData);  // Menyertakan detail User
            responseData.put("approvedBy", detailIdea.getApprovedBy().getUsername());  // User yang menyetujui

            // Mengembalikan response
            return GlobalFunction.dataDetailIDeaBerhasilDisimpan(request, responseData);

        } catch (Exception e) {
            e.printStackTrace();
            return GlobalFunction.dataGagalDisimpan("ADI004001", request);
        }
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<DetailIdea> page = null;
        List<DetailIdea> list = null;
        switch (columnName){
            case "approvalDate":page=detailIdeaRepo.findByApprovalDate(pageable,value);break;
            case "status":page=detailIdeaRepo.findByStatusContainingIgnoreCase(pageable,value);break;
            default:page=detailIdeaRepo.findAll(pageable);
        }
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListDetailIdeaDTO(list),
                page,
                columnName,value
        );
    }

    // Mendapatkan ID pengguna yang sedang login dari token JWT
    public Long getCurrentUserId(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request); // Mengambil token dari header request
        if (token != null) {
            return jwtUtil.getUserIdFromToken(token); // Mendapatkan user ID dari token
        }
        return null;
    }

    // Convert DTO to entity
    public DetailIdea convertToEntity(ValDetailIdeaDTO valDetailIdeaDTO) {
        return modelMapper.map(valDetailIdeaDTO, DetailIdea.class);
    }

    // Convert list of Detail Idea entities to DTOs
    public List<RespDetailIdeaDTO> convertToListDetailIdeaDTO(List<DetailIdea> detailIdeas) {
        return modelMapper.map(detailIdeas, new TypeToken<List<RespDetailIdeaDTO>>(){}.getType());
    }

}
