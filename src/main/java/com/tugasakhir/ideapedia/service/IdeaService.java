package com.tugasakhir.ideapedia.service;

import com.cloudinary.utils.ObjectUtils;
import com.tugasakhir.ideapedia.core.IFile;
import com.tugasakhir.ideapedia.dto.response.RespFileLinkDTO;
import com.tugasakhir.ideapedia.dto.response.RespIdeaDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValIdeaDTO;
import com.tugasakhir.ideapedia.model.*;
import com.tugasakhir.ideapedia.repo.DetailIdeaRepo;
import com.tugasakhir.ideapedia.repo.HistoryRepo;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.security.JwtUtility;
import com.tugasakhir.ideapedia.util.GlobalFunction;
import com.tugasakhir.ideapedia.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.net.URL;


@Service
@Transactional
public class IdeaService implements IFile<Idea> {

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DetailIdeaRepo detailIdeaRepo;

    @Autowired
    private FileService fileService;

    @Autowired
    private PowerPointToPdfConverter powerPointToPdfConverter;

    @Autowired
    private JwtUtility jwtUtil;  // Injeksi dependensi jwtUtil

    private final ModelMapper modelMapper = new ModelMapper();
    private TransformPagination transformPagination = new TransformPagination();

    @Value("${file.storage.path}")
    private String fileStoragePath;

    @Value("${image.storage.path}")
    private String imageStoragePath;

    private static final List<String> ALLOWED_FILE_EXTENSIONS = List.of("ppt", "pptx", "pdf");
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png");

    @Override
    public ResponseEntity<Object> saveFile(Idea idea, HttpServletRequest request, MultipartFile file, MultipartFile image) {
        String fileUrl = null;

        try {
            // Convert PPT/PPTX file to PDF if necessary
            File pdfFile = null;

            if (file.getOriginalFilename().endsWith(".ppt") || file.getOriginalFilename().endsWith(".pptx")) {
                // Save the MultipartFile to a temporary file
                File tempFile = File.createTempFile("upload", file.getOriginalFilename());
                file.transferTo(tempFile);

                // Convert the PPT/PPTX file to PDF
                pdfFile = PowerPointToPdfConverter.convertPPTToPDF(tempFile);

                // Delete the temporary PPT file after conversion
                tempFile.delete();

                // Upload the PDF file to Cloudinary
                try {
                    if (pdfFile != null && pdfFile.exists() && pdfFile.length() > 0) {
                        fileUrl = fileService.uploadFile(pdfFile);  // Upload the converted PDF file
                    } else {
                        throw new IOException("File PDF tidak ditemukan atau kosong.");
                    }
                } finally {
                    if (pdfFile != null && pdfFile.exists()) {
                        pdfFile.delete(); // Delete the temporary PDF file after upload
                    }
                }
            } else {
                // Regular file upload for non-PPT files
                fileUrl = fileService.uploadFile(file);  // Upload the file directly
            }

            // Upload gambar ke Cloudinary (opsional jika file gambar ada)
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = fileService.uploadFile(image);
            }

            // Upload the image file
            // String imageFileName = saveFileToStorage(image, imageStoragePath);

            // Set file names and paths in the idea object
            idea.setFileName(file.getOriginalFilename());
            idea.setFilePath(fileUrl);
            idea.setFileImage(imageUrl); // Use Cloudinary URL for image
//            idea.setFileImage(imageStoragePath + imageFileName); //ini untuk lokal storage

            // Get the current user ID from the request
            Long currentUserId = getCurrentUserId(request);
            Optional<User> currentUser = userRepo.findById(currentUserId);

            if (!currentUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User tidak ditemukan.");
            }

            idea.setUser(currentUser.get());
            idea.setCreatedAt(LocalDateTime.now());

            // Save the idea to the database
            ideaRepo.save(idea);

            DetailIdea detailIdea = new DetailIdea();
            detailIdea.setStatus("New Entry");
            detailIdea.setIdea(idea);
            detailIdeaRepo.save(detailIdea);

            History history = new History();
            history.setUser(currentUser.get());
            history.setIdea(idea);
            history.setAction("Upload");
            history.setDetailAction("Upload file: " + idea.getJudul());
            history.setCreatedAt(LocalDateTime.now());
            historyRepo.save(history);

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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal menyimpan file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal menyimpan ide: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<Idea> page = ideaRepo.findAll(pageable);
        List<Idea> list = page.getContent();

        if (list.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespIdeaDTO(list),
                page,
                null, null
        );
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<Idea> page = null;
        List<Idea> list = null;
        try {
            switch (columnName) {
                case "judul":
                    page = ideaRepo.findByJudulContainingIgnoreCase(pageable, value);
                    break;
                case "deskripsi":
                    page = ideaRepo.findByDeskripsiContainingIgnoreCase(pageable, value);
                    break;
                case "userId": // TODO: Ingat ini disesuaikan
                    Long userId = Long.parseLong(value); // Convert String to Long
                    page = ideaRepo.findByUserIdOrderByIdDesc(pageable, userId);
                    break;
                default:
                    page = ideaRepo.findAll(pageable);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid userId format");
        }

        list = page.getContent();
        if (list.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespIdeaDTO(list),
                page,
                columnName, value
        );
    }

    public ResponseEntity<Resource> downloadFile(Long ideaId, String fileType, HttpServletRequest request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

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
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            //            // Membuat resource dari path file local
//            Path path = FileSystems.getDefault().getPath(filePath);
//            Resource resource = new UrlResource(path.toUri());
//            if (!resource.exists()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }

            // Menangani jika file adalah URL eksternal (Cloudinary)
            if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
                try {
                    // Membuat URL resource dari file URL
                    URL url = new URL(filePath);
                    Resource resource = new UrlResource(url.toURI());

                    if (!resource.exists()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }

                    // Mendapatkan pengguna yang sedang login
                    Long currentUserId = getCurrentUserId(request);
                    Optional<User> currentUser = userRepo.findById(currentUserId);
                    if (currentUser.isPresent()) {
                        idea.setUser(currentUser.get());
                    } else {
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
                    String contentDisposition = "attachment; filename=\"" + resource.getFilename() + "\"";

                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                            .body(resource);

                } catch (URISyntaxException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<Resource> getImage(Long ideaId) {
        try {
            // Mendapatkan objek Idea berdasarkan ID
            Optional<Idea> optionalIdea = ideaRepo.findById(ideaId);
            if (optionalIdea.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Idea idea = optionalIdea.get();

            // Mendapatkan path gambar (URL dari Cloudinary)
            String imagePath = idea.getFileImage();
            if (imagePath == null || imagePath.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Membuat resource dari URL gambar
            URL url = new URL(imagePath);  // Menggunakan URL langsung dari Cloudinary
            Resource resource = new UrlResource(url);
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

//            // Membuat resource dari path gambar untuk local
//            Path path = FileSystems.getDefault().getPath(imagePath);
//            Resource resource = new UrlResource(path.toUri());
//            if (!resource.exists()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }

            // Mengatur header respons untuk mendukung pengambilan gambar
            String contentDisposition = "inline; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // Sesuaikan dengan tipe media jika gambar dalam format lain
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<Resource> previewFile(Long id) {
        try {
            // Mendapatkan data Idea berdasarkan ID
            Optional<Idea> ideaOpt = ideaRepo.findById(id);
            if (ideaOpt.isEmpty()) {
                return ResponseEntity.notFound().build(); // File tidak ditemukan
            }

            Idea idea = ideaOpt.get();
            String fileUrl = idea.getFilePath(); // URL file di Cloudinary atau remote storage
            String fileName = idea.getFileName();

            // Memeriksa aksesibilitas file menggunakan URLConnection
            HttpURLConnection connection = (HttpURLConnection) new URL(fileUrl).openConnection();
            connection.setRequestMethod("HEAD"); // Menggunakan metode HEAD untuk memeriksa status file
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                // Menambahkan log jika file tidak dapat diakses
                System.out.println("File tidak dapat diakses, response code: " + responseCode);
                return ResponseEntity.notFound().build(); // File tidak dapat diakses
            }

            // Jika file dapat diakses, ambil Resource untuk file tersebut
            Resource resource = new UrlResource(fileUrl);
            if (!resource.exists() || !resource.isReadable()) {
                // Menambahkan log jika file tidak dapat dibaca
                System.out.println("File tidak ditemukan atau tidak dapat dibaca: " + fileUrl);
                return ResponseEntity.notFound().build(); // File tidak ditemukan
            }

            // Menyiapkan headers untuk inline preview
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");

            // Return file sebagai respons dengan header untuk preview
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            // Log error dan tangani pengecualian lainnya
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // Kesalahan internal server
        }
    }

    public ResponseEntity<?> previewFileLink(Long id) {
        try {
            // Mendapatkan data Idea berdasarkan ID
            Optional<Idea> ideaOpt = ideaRepo.findById(id);
            if (ideaOpt.isEmpty()) {
                return ResponseEntity.notFound().build(); // File tidak ditemukan
            }

            Idea idea = ideaOpt.get();
            String fileUrl = idea.getFilePath(); // URL file di Cloudinary atau remote storage

            // Periksa apakah URL valid (opsional)
            HttpURLConnection connection = (HttpURLConnection) new URL(fileUrl).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("File tidak dapat diakses, response code: " + responseCode);
                return ResponseEntity.notFound().build(); // File tidak dapat diakses
            }

            // Kembalikan URL file dalam format JSON
            return ResponseEntity.ok(new RespFileLinkDTO(fileUrl)); // Mengembalikan URL sebagai JSON

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // Kesalahan internal server
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> hideIdea(Long ideaId, HttpServletRequest request) {
        try {
            // Mencari DetailIdea berdasarkan ID Idea
            Optional<DetailIdea> optionalDetailIdea = detailIdeaRepo.findByIdeaId(ideaId);
            if (!optionalDetailIdea.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }

            // Mengubah status menjadi 'Hidden'
            DetailIdea detailIdea = optionalDetailIdea.get();
            detailIdea.setStatus("Hidden");
            detailIdeaRepo.save(detailIdea);

        } catch (Exception e) {
            return GlobalFunction.dataGagalDihapus("FEAUT004021", request); //021-030
        }

        return GlobalFunction.dataBerhasilDihapus(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> unhideIdea(Long ideaId, HttpServletRequest request) {
        try {
            // Mencari DetailIdea berdasarkan ID Idea
            Optional<DetailIdea> optionalDetailIdea = detailIdeaRepo.findByIdeaId(ideaId);
            if (!optionalDetailIdea.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }

            // Mengubah status menjadi 'Hidden'
            DetailIdea detailIdea = optionalDetailIdea.get();
            detailIdea.setStatus("Approved");
            detailIdeaRepo.save(detailIdea);

        } catch (Exception e) {
            return GlobalFunction.dataGagalDihapus("FEAUT004021", request); //021-030
        }

        return GlobalFunction.unhideIdea(request);
    }

    // Fungsi untuk menyimpan file ke penyimpanan local
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


    // Mengambil ekstensi file
//    private String getFileExtension(String fileName) {
//        if (fileName == null || fileName.isEmpty()) {
//            return null;  // Return null jika fileName kosong
//        }
//
//        int dotIndex = fileName.lastIndexOf('.');
//        if (dotIndex == -1) {
//            return null;  // Return null jika tidak ada titik (tidak ada ekstensi)
//        }
//
//        return fileName.substring(dotIndex + 1).toLowerCase();  // Mengembalikan ekstensi file setelah titik
//    }

    // Mendapatkan ID pengguna yang sedang login dari token JWT
    public Long getCurrentUserId(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request); // Mengambil token dari header request
        if (token != null) {
            return jwtUtil.getUserIdFromToken(token); // Mendapatkan user ID dari token
        }
        return null;
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
