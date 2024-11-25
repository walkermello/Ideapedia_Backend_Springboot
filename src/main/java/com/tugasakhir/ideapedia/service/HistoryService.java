package com.tugasakhir.ideapedia.service;

import com.tugasakhir.ideapedia.core.IHistory;
import com.tugasakhir.ideapedia.dto.response.RespHistoryDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValHistoryDTO;
import com.tugasakhir.ideapedia.model.History;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.repo.HistoryRepo;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.security.JwtUtility;
import com.tugasakhir.ideapedia.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistoryService implements IHistory<History> {

    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private JwtUtility jwtUtil;  // Injeksi dependensi jwtUtil

    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();
    private StringBuilder sBuild = new StringBuilder();

    @Override
    public ResponseEntity<Object> findByUserId(Long userId, HttpServletRequest request) {
        Long currentUserId = getCurrentUserId(request);
        Optional<User> currentUser = userRepo.findById(currentUserId);
        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User tidak ditemukan.");
        }

        List<History> histories = historyRepo.findByUserId(currentUserId);

        if (histories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No bookmarks found for user with id " + userId);
        }

        return ResponseEntity.ok(histories);
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

    public History convertToEntity(ValHistoryDTO valHistoryDTO) {
        return modelMapper.map(valHistoryDTO, History.class);
    }

    public List<RespHistoryDTO> convertToListRespHistoryDTO(List<History> histories) {
        return modelMapper.map(histories, new TypeToken<List<RespHistoryDTO>>() {}.getType());
    }
}
