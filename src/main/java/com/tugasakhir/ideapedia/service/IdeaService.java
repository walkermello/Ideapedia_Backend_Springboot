package com.tugasakhir.ideapedia.service;

import com.tugasakhir.ideapedia.core.IFile;
import com.tugasakhir.ideapedia.dto.response.RespIdeaDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValIdeaDTO;
import com.tugasakhir.ideapedia.model.History;
import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.repo.HistoryRepo;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class IdeaService implements IFile<Idea> {

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private UserRepo userRepo;

    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${file.storage.path}")
    private String fileStoragePath;

    @Value("${image.storage.path}")
    private String imageStoragePath;

    private static final List<String> ALLOWED_FILE_EXTENSIONS = List.of("ppt", "pptx", "pdf");
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png");

    @Override
    public ResponseEntity<Object> saveFile(Idea idea, HttpServletRequest request, MultipartFile file, MultipartFile image) {
        try {
            if (idea == null || idea.getJudul() == null || idea.getDeskripsi() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Judul dan deskripsi ide tidak boleh kosong.");
            }

            if (file == null || file.isEmpty() || !isValidFile(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File ide harus berupa .ppt, .pptx, atau .pdf.");
            }

            if (image == null || image.isEmpty() || !isValidImage(image)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File gambar harus berupa .jpg, .jpeg, atau .png.");
            }

            // Menyimpan file dan gambar
            String fileName = saveFileToStorage(file, fileStoragePath);
            String imageFileName = saveFileToStorage(image, imageStoragePath);

            // Menyimpan informasi file dan gambar ke dalam objek idea
            idea.setFileName(fileName);
            idea.setFilePath(fileStoragePath + fileName);
            idea.setFileImage(imageStoragePath + imageFileName);

            // Mendapatkan user berdasarkan ID pengguna yang sedang login
            Long currentUserId = getCurrentUserId(request);
            Optional<User> currentUser = userRepo.findById(currentUserId);
            if (currentUser.isPresent()) {
                idea.setUser(currentUser.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User tidak ditemukan.");
            }

            // Set timestamp
            idea.setCreatedAt(LocalDateTime.now());

            // Menyimpan data ide ke database
            ideaRepo.save(idea);

            // Menyimpan log download ke tabel history
            History history = new History();
            history.setUser(currentUser.get());
            history.setIdea(idea);
            history.setAction("Upload");
            history.setDetailAction("Upload file: " + idea.getJudul());
            history.setCreatedAt(LocalDateTime.now());
            historyRepo.save(history);

            // Menyiapkan data respons
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", idea.getId());
            responseData.put("judul", idea.getJudul());
            responseData.put("deskripsi", idea.getDeskripsi());
            responseData.put("fileName", idea.getFileName());
            responseData.put("fileImage", idea.getFileImage());
            responseData.put("filePath", idea.getFilePath());
            responseData.put("createdAt", idea.getCreatedAt());
            responseData.put("userName", idea.getUser().getUsername());
            responseData.put("nip", idea.getUser().getNip());
            responseData.put("email", idea.getUser().getEmail());
            responseData.put("noHp", idea.getUser().getNoHp());
            responseData.put("unitKerja", idea.getUser().getUnitKerja().getUnitName());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    GlobalFunction.dataIDeaBerhasilDisimpan(request, responseData)
            );

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal menyimpan file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal menyimpan ide: " + e.getMessage());
        }
    }

    public ResponseEntity<Resource> downloadFile(Long ideaId, String fileType, HttpServletRequest request) {
        try {
            // Mendapatkan objek Idea berdasarkan ID
            Optional<Idea> optionalIdea = ideaRepo.findById(ideaId);
            if (optionalIdea.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Idea idea = optionalIdea.get();

            // Menentukan path file berdasarkan tipe file yang diminta (file atau gambar)
            String filePath;
            if ("file".equalsIgnoreCase(fileType)) {
                filePath = idea.getFilePath();
            } else if ("image".equalsIgnoreCase(fileType)) {
                filePath = idea.getFileImage();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Membuat resource dari path file
            Path path = FileSystems.getDefault().getPath(filePath);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Mendapatkan pengguna yang sedang login
            Long currentUserId = getCurrentUserId(request);
            Optional<User> currentUser = userRepo.findById(currentUserId);
            if (currentUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Menyimpan log download ke tabel history
            History history = new History();
            history.setUser(currentUser.get());
            history.setIdea(idea);
            history.setAction("Download");
            history.setDetailAction("Download file: " + idea.getJudul());
            history.setCreatedAt(LocalDateTime.now());
            historyRepo.save(history);

            // Mengatur header respons untuk mendukung download
            String contentDisposition = "attachment; filename=\"" + path.getFileName().toString() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Fungsi untuk menyimpan file ke penyimpanan
    private String saveFileToStorage(MultipartFile file, String storagePath) throws IOException {
        // Menyusun path lengkap dan memastikan direktori ada
        Path uploadPath = Paths.get(storagePath);
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath); // Membuat direktori jika belum ada
        }

        String fileName = file.getOriginalFilename();
        Path targetPath = uploadPath.resolve(fileName); // Menggabungkan path folder dan nama file
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING); // Menyalin file ke direktori
        return fileName;
    }

    // Mengecek apakah ekstensi file valid
    private boolean isValidFile(MultipartFile file) {
        String extension = getFileExtension(file.getOriginalFilename());
        return ALLOWED_FILE_EXTENSIONS.contains(extension.toLowerCase());
    }

    // Mengecek apakah ekstensi gambar valid
    private boolean isValidImage(MultipartFile image) {
        String extension = getFileExtension(image.getOriginalFilename());
        return ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase());
    }

    // Mengambil ekstensi file
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }

    // Mendapatkan ID pengguna yang sedang login
    private Long getCurrentUserId(HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("User is not authenticated");
        }
        Optional<User> user = userRepo.findByUsername(username);
        return user.map(User::getId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Convert DTO to entity
    public Idea convertToEntity(ValIdeaDTO valIdeaDTO) {
        return modelMapper.map(valIdeaDTO, Idea.class);
    }

    // Convert list of Idea entities to DTOs
    public List<RespIdeaDTO> convertToListRespIdeaDTO(List<Idea> ideas) {
        return modelMapper.map(ideas, new TypeToken<List<RespIdeaDTO>>() {}.getType());
    }
}
