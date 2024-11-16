package com.tugasakhir.ideapedia.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileService{

    private final Cloudinary cloudinary;

    // Autowiring the Cloudinary instance from configuration
    @Autowired
    public FileService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Upload file to Cloudinary (Only accepts image, pdf, ppt, pptx)
     * @param file MultipartFile to upload
     * @return String URL of the uploaded file
     * @throws IOException if file is empty, unsupported type, or upload fails
     */
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        try {
            // Mengonversi MultipartFile menjadi FileInputStream
            FileInputStream fileInputStream = (FileInputStream) file.getInputStream();

            // Siapkan parameter upload untuk Cloudinary
            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("resource_type", "auto");

            // Upload file ke Cloudinary
            Map uploadResult = cloudinary.uploader().upload(fileInputStream, uploadParams);

            // Kembalikan URL file yang diupload
            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            // Tangani error dengan memberikan pesan tambahan
            throw new IOException("Error uploading file to Cloudinary: " + e.getMessage(), e);
        }
    }

    public String uploadFile(File file) throws IOException {
        // Periksa apakah file kosong
        if (file == null || !file.exists() || file.length() == 0) {
            throw new IOException("File is empty or does not exist");
        }

        try {
            // Prepare uploadParams for raw file upload
            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("resource_type", "auto");

            // Upload file to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file, uploadParams);

            // Return the secure URL of the uploaded file
            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            // Log and rethrow with additional information
            throw new IOException("Error uploading file to Cloudinary: " + e.getMessage(), e);
        }
    }

    /**
     * Check if the file is a valid type (image, pdf, ppt, pptx)
     * @param file the file to check
     * @return true if the file is valid, false otherwise
     */
    private boolean isValidFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.startsWith("image") ||
                        contentType.equals("application/pdf") ||
                        contentType.equals("application/vnd.ms-powerpoint") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation"));
    }

    /**
     * Get the preview URL of a file from Cloudinary (Only for PDF and PPT/PPTX)
     * @param publicId The public ID of the file stored in Cloudinary
     * @return URL for previewing the file
     * @throws IOException if the file is not found or any other error occurs
     */
    public URL getFilePreviewUrl(String publicId) throws IOException {
        // Only generate preview for PDF and PPT/PPTX files
        if (isValidPreviewFile(publicId)) {
            try {
                // Generate the preview URL for PDF or PPT/PPTX file
                String urlString = cloudinary.url()
                        .format("jpg")  // Set the desired format, e.g., "jpg" or "png" for image preview
                        .publicId(publicId)
                        .secure(true)   // Use HTTPS for secure access
                        .generate();

                // Convert String URL to java.net.URL
                return new URL(urlString);
            } catch (IOException e) {
                // Catch IOException (or any exception) that might arise during the URL generation
                throw new IOException("Error generating preview for file: " + e.getMessage(), e);
            }
        } else {
            throw new IOException("Preview not available for this file type.");
        }
    }

    /**
     * Check if the file is a valid type for preview (PDF, PPT, PPTX)
     * @param publicId The public ID of the file
     * @return true if the file is PDF or PPT/PPTX, false otherwise
     */
    private boolean isValidPreviewFile(String publicId) {
        // In Cloudinary, the file type can be derived from the file extension or format
        return publicId != null && (
                publicId.endsWith(".pdf") ||
                        publicId.endsWith(".ppt") ||
                        publicId.endsWith(".pptx"));
    }
}
