package com.tugasakhir.ideapedia.service;

import com.tugasakhir.ideapedia.core.IBookmark;
import com.tugasakhir.ideapedia.core.IService;
import com.tugasakhir.ideapedia.dto.response.RespUserDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValUserDTO;
import com.tugasakhir.ideapedia.model.Bookmark;
import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.repo.BookmarkRepo;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.repo.UserRepo;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BookmarkService implements IBookmark<Bookmark> {

    @Autowired
    private BookmarkRepo bookmarkRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();
    private StringBuilder sBuild = new StringBuilder();

    public Long getCurrentUserId(HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Optional<User> user = userRepo.findByUsername(username);
        return user.map(User::getId).orElse(null);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> bookmark(Bookmark bookmark, HttpServletRequest request) {
        try {
            bookmark.setCreatedAt(LocalDateTime.now());
            Long createdByUserId = getCurrentUserId(request);

            Optional<User> userOptional = userRepo.findById(createdByUserId);
            if (userOptional.isPresent()) {
                bookmark.setUser(userOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User tidak ditemukan.");
            }

            bookmarkRepo.save(bookmark);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", bookmark.getId());
            responseData.put("createdAt", bookmark.getCreatedAt());
            responseData.put("idea", bookmark.getIdea());
            responseData.put("user", bookmark.getUser());

            return GlobalFunction.dataBookmarkBerhasilDisimpan(request, responseData);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return GlobalFunction.dataGagalDisimpan("FEAUT004001", request);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> unbookmark(Long id, HttpServletRequest request) {
        try {
            Optional<Bookmark> optionalBookmark = bookmarkRepo.findById(id);
            if(!optionalBookmark.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            bookmarkRepo.deleteById(id);
        } catch (Exception e) {
            return GlobalFunction.dataGagalDihapus("FEAUT004021", request);
        }
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    public User convertToEntity(ValUserDTO valUserDTO) {
        return modelMapper.map(valUserDTO, User.class);
    }

    public List<RespUserDTO> convertToListRespUserDTO(List<Bookmark> bookmarks) {
        return modelMapper.map(bookmarks, new TypeToken<List<RespUserDTO>>() {}.getType());
    }
}
