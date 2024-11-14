package com.tugasakhir.ideapedia.service;

import com.tugasakhir.ideapedia.core.IDetail;
import com.tugasakhir.ideapedia.core.IService;
import com.tugasakhir.ideapedia.dto.response.RespDetailIdeaDTO;
import com.tugasakhir.ideapedia.dto.response.RespUnitKerjaDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValDetailIdeaDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValIdeaDTO;
import com.tugasakhir.ideapedia.model.DetailIdea;
import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.repo.DetailIdeaRepo;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.repo.UnitKerjaRepo;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.security.Crypto;
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
    private JwtUtility jwtUtil;

    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    @Transactional
    public ResponseEntity<Object> approve(Long id, ValIdeaDTO valIdeaDTO, HttpServletRequest request) {
        try {
            // Cari entitas DetailIdea berdasarkan ID
            Optional<DetailIdea> optionalDetailIdea = detailIdeaRepo.findById(id);
            if (!optionalDetailIdea.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request); // Mengembalikan respons jika data tidak ditemukan
            }

            DetailIdea detailIdea = optionalDetailIdea.get();

            // Set tanggal persetujuan dan status
            Date approvalDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            detailIdea.setApprovalDate(approvalDate);
            detailIdea.setStatus("Approved");
            detailIdea.setComments("Approved");

            // Mendapatkan pengguna yang mengirim request
            Long currentUserId = getCurrentUserId(request);
            Optional<User> currentUser = userRepo.findById(currentUserId);
            if (currentUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User tidak ditemukan.");
            }
            detailIdea.setApprovedBy(currentUser.get());

            // Ambil feedback dan penguji dari request body (ValIdeaDTO)
            String feedback = valIdeaDTO.getFeedback();
            Long pengujiPertama = valIdeaDTO.getPengujiPertama();
            Long pengujiKedua = valIdeaDTO.getPengujiKedua();
            Long pengujiKetiga = valIdeaDTO.getPengujiKetiga();

            // Set feedback dan penguji pada entitas Idea yang terkait
            detailIdea.getIdea().setFeedback(feedback);
            detailIdea.getIdea().setPengujiPertama(pengujiPertama);
            detailIdea.getIdea().setPengujiKedua(pengujiKedua);
            detailIdea.getIdea().setPengujiKetiga(pengujiKetiga);

            // Simpan perubahan ke database
            detailIdeaRepo.save(detailIdea);

            // Siapkan response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", detailIdea.getId());
            responseData.put("approvalDate", detailIdea.getApprovalDate());
            responseData.put("comments", detailIdea.getComments());
            responseData.put("status", detailIdea.getStatus());
            responseData.put("idea", detailIdea.getIdea());
            responseData.put("user", detailIdea.getIdea().getUser());

            return GlobalFunction.dataDetailIDeaBerhasilDisimpan(request, responseData);

        } catch (Exception e) {
            e.printStackTrace();
            return GlobalFunction.dataGagalDisimpan("ADI004001", request);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> reject(Long id, String comment, HttpServletRequest request) {
        try {
            Optional<DetailIdea> optionalDetailIdea = detailIdeaRepo.findById(id);
            if (!optionalDetailIdea.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }

            DetailIdea detailIdea = optionalDetailIdea.get();

            // Set rejection data
            Date rejectedDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            detailIdea.setRejectedDate(rejectedDate);
            detailIdea.setStatus("Rejected");
            detailIdea.setComments(comment);

            // Get current user from token
            Long currentUserId = getCurrentUserId(request);
            Optional<User> currentUser = userRepo.findById(currentUserId);
            if (currentUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User tidak ditemukan.");
            }
            detailIdea.setApprovedBy(currentUser.get());

            // Save rejection changes
            detailIdeaRepo.save(detailIdea);

            // Prepare response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", detailIdea.getId());
            responseData.put("approvalDate", detailIdea.getApprovalDate());
            responseData.put("comments", detailIdea.getComments());
            responseData.put("status", detailIdea.getStatus());
            responseData.put("idea", detailIdea.getIdea());
            responseData.put("user", detailIdea.getIdea().getUser());

            return GlobalFunction.dataDetailIDeaBerhasilDisimpan(request, responseData);

        } catch (Exception e) {
            e.printStackTrace();
            return GlobalFunction.dataGagalDisimpan("ADI004001", request);
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

    // Convert DTO to entity
    public DetailIdea convertToEntity(ValDetailIdeaDTO valDetailIdeaDTO) {
        return modelMapper.map(valDetailIdeaDTO, DetailIdea.class);
    }

    // Convert list of Detail Idea entities to DTOs
    public List<RespDetailIdeaDTO> convertToListDetailIdeaDTO(List<DetailIdea> detailIdeas) {
        return modelMapper.map(detailIdeas, new TypeToken<List<RespDetailIdeaDTO>>(){}.getType());
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<DetailIdea> page;
        switch (columnName) {
            case "approvalDate":
                page = detailIdeaRepo.findByApprovalDate(pageable, value);
                break;
            case "status":
                page = detailIdeaRepo.findByStatusContainingIgnoreCase(pageable, value);
                break;
            default:
                page = detailIdeaRepo.findAll(pageable);
        }
        List<DetailIdea> list = page.getContent();
        if (list.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(new HashMap<>(), convertToListDetailIdeaDTO(list), page, columnName, value);
    }
}

