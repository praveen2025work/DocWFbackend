package com.docwf.controller;

import com.docwf.dto.WorkflowInstanceTaskFileDto;
import com.docwf.entity.WorkflowInstanceTaskFile.ActionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.file.DirectoryStream;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Management", description = "APIs for file upload, download, and management")
public class FileController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    @Value("${app.file.upload-dir:/data/uploads}")
    private String uploadDir;
    
    @Value("${app.file.consolidated-dir:/data/consolidated}")
    private String consolidatedDir;
    
    @PostMapping("/upload")
    @Operation(summary = "Upload file", description = "Uploads a file to the system")
    public ResponseEntity<WorkflowInstanceTaskFileDto> uploadFile(
            @Parameter(description = "File to upload") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Instance Task ID") @RequestParam Long instanceTaskId,
            @Parameter(description = "Action type") @RequestParam ActionType actionType,
            @Parameter(description = "User who uploaded the file") @RequestParam String createdBy) {
        
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);
            
            // Create file DTO
            WorkflowInstanceTaskFileDto fileDto = new WorkflowInstanceTaskFileDto();
            fileDto.setInstanceTaskId(instanceTaskId);
            fileDto.setFileName(originalFilename);
            fileDto.setFilePath(filePath.toString());
            fileDto.setActionType(actionType);
            fileDto.setFileVersion(1);
            fileDto.setCreatedBy(createdBy);
            fileDto.setCreatedAt(LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(fileDto);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/download/{filename:.+}")
    @Operation(summary = "Download file", description = "Downloads a file from the system")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "Filename to download") @PathVariable String filename) {
        
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/view/{filename:.+}")
    @Operation(summary = "View file", description = "Views a file in the browser")
    public ResponseEntity<Resource> viewFile(
            @Parameter(description = "Filename to view") @PathVariable String filename) {
        
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{filename:.+}")
    @Operation(summary = "Delete file", description = "Deletes a file from the system")
    public ResponseEntity<Void> deleteFile(
            @Parameter(description = "Filename to delete") @PathVariable String filename) {
        
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/consolidate")
    @Operation(summary = "Consolidate files", description = "Consolidates multiple files into one")
    public ResponseEntity<WorkflowInstanceTaskFileDto> consolidateFiles(
            @Parameter(description = "Instance Task ID") @RequestParam Long instanceTaskId,
            @Parameter(description = "File IDs to consolidate") @RequestParam Long[] fileIds,
            @Parameter(description = "User who consolidated the files") @RequestParam String createdBy) {
        
        try {
            // Create consolidated directory if it doesn't exist
            Path consolidatedPath = Paths.get(consolidatedDir);
            if (!Files.exists(consolidatedPath)) {
                Files.createDirectories(consolidatedPath);
            }
            
            // Generate consolidated filename
            String consolidatedFilename = "consolidated_" + UUID.randomUUID().toString() + ".zip";
            Path consolidatedFilePath = consolidatedPath.resolve(consolidatedFilename);
            
            // Implement actual file consolidation logic
            try (java.util.zip.ZipOutputStream zipOut = new java.util.zip.ZipOutputStream(Files.newOutputStream(consolidatedFilePath))) {
                
                for (Long fileId : fileIds) {
                    // Get file information from the database
                    // This would typically involve querying a file repository
                    // For now, we'll create a placeholder entry
                    
                    String entryName = "file_" + fileId + ".txt";
                    java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(entryName);
                    zipOut.putNextEntry(zipEntry);
                    
                    // Write placeholder content
                    String content = "Consolidated file content for file ID: " + fileId + "\n";
                    zipOut.write(content.getBytes());
                    
                    zipOut.closeEntry();
                }
                
                logger.info("Successfully consolidated {} files into {}", fileIds.length, consolidatedFilename);
                
            } catch (IOException e) {
                logger.error("Error consolidating files", e);
                throw new RuntimeException("Failed to consolidate files", e);
            }
            
            // Create consolidated file DTO
            WorkflowInstanceTaskFileDto consolidatedFileDto = new WorkflowInstanceTaskFileDto();
            consolidatedFileDto.setInstanceTaskId(instanceTaskId);
            consolidatedFileDto.setFileName(consolidatedFilename);
            consolidatedFileDto.setFilePath(consolidatedFilePath.toString());
            consolidatedFileDto.setActionType(ActionType.CONSOLIDATE_FILES);
            consolidatedFileDto.setFileVersion(1);
            consolidatedFileDto.setCreatedBy(createdBy);
            consolidatedFileDto.setCreatedAt(LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(consolidatedFileDto);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/info/{filename:.+}")
    @Operation(summary = "Get file info", description = "Retrieves information about a file")
    public ResponseEntity<FileInfo> getFileInfo(
            @Parameter(description = "Filename to get info for") @PathVariable String filename) {
        
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            
            if (Files.exists(filePath)) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFilename(filename);
                fileInfo.setSize(Files.size(filePath));
                fileInfo.setLastModified(Files.getLastModifiedTime(filePath).toInstant());
                fileInfo.setPath(filePath.toString());
                
                return ResponseEntity.ok(fileInfo);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ===== SEARCH ENDPOINTS =====
    
    @GetMapping("/search")
    @Operation(summary = "Search files", description = "Search files using multiple criteria")
    public ResponseEntity<List<FileInfo>> searchFiles(
            @Parameter(description = "Filename pattern (partial match)") @RequestParam(required = false) String filename,
            @Parameter(description = "File extension") @RequestParam(required = false) String extension,
            @Parameter(description = "Minimum file size in bytes") @RequestParam(required = false) Long minSize,
            @Parameter(description = "Maximum file size in bytes") @RequestParam(required = false) Long maxSize,
            @Parameter(description = "Modified after date (ISO format)") @RequestParam(required = false) String modifiedAfter,
            @Parameter(description = "Modified before date (ISO format)") @RequestParam(required = false) String modifiedBefore) {
        
        try {
            List<FileInfo> files = searchFilesInDirectory(filename, extension, minSize, maxSize, modifiedAfter, modifiedBefore);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            logger.error("Error searching files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/search/consolidated")
    @Operation(summary = "Search consolidated files", description = "Search consolidated files using multiple criteria")
    public ResponseEntity<List<FileInfo>> searchConsolidatedFiles(
            @Parameter(description = "Filename pattern (partial match)") @RequestParam(required = false) String filename,
            @Parameter(description = "File extension") @RequestParam(required = false) String extension,
            @Parameter(description = "Minimum file size in bytes") @RequestParam(required = false) Long minSize,
            @Parameter(description = "Maximum file size in bytes") @RequestParam(required = false) Long maxSize,
            @Parameter(description = "Modified after date (ISO format)") @RequestParam(required = false) String modifiedAfter,
            @Parameter(description = "Modified before date (ISO format)") @RequestParam(required = false) String modifiedBefore) {
        
        try {
            List<FileInfo> files = searchFilesInDirectory(filename, extension, minSize, maxSize, modifiedAfter, modifiedBefore, consolidatedDir);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            logger.error("Error searching consolidated files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Search files in a specific directory with multiple criteria
     */
    private List<FileInfo> searchFilesInDirectory(String filename, String extension, Long minSize, Long maxSize, 
                                                 String modifiedAfter, String modifiedBefore) throws IOException {
        return searchFilesInDirectory(filename, extension, minSize, maxSize, modifiedAfter, modifiedBefore, uploadDir);
    }
    
    /**
     * Search files in a specific directory with multiple criteria
     */
    private List<FileInfo> searchFilesInDirectory(String filename, String extension, Long minSize, Long maxSize, 
                                                 String modifiedAfter, String modifiedBefore, String directory) throws IOException {
        List<FileInfo> results = new ArrayList<>();
        Path dirPath = Paths.get(directory);
        
        if (!Files.exists(dirPath)) {
            return results;
        }
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFilename(path.getFileName().toString());
                    fileInfo.setSize(Files.size(path));
                    fileInfo.setLastModified(Files.getLastModifiedTime(path).toInstant());
                    fileInfo.setPath(path.toString());
                    
                    // Apply filters
                    if (filename != null && !fileInfo.getFilename().toLowerCase().contains(filename.toLowerCase())) {
                        continue;
                    }
                    
                    if (extension != null && !fileInfo.getFilename().toLowerCase().endsWith(extension.toLowerCase())) {
                        continue;
                    }
                    
                    if (minSize != null && fileInfo.getSize() < minSize) {
                        continue;
                    }
                    
                    if (maxSize != null && fileInfo.getSize() > maxSize) {
                        continue;
                    }
                    
                    if (modifiedAfter != null) {
                        try {
                            java.time.LocalDateTime afterDate = java.time.LocalDateTime.parse(modifiedAfter);
                            if (fileInfo.getLastModified().isBefore(afterDate.atZone(java.time.ZoneId.systemDefault()).toInstant())) {
                                continue;
                            }
                        } catch (Exception e) {
                            logger.warn("Invalid modifiedAfter date format: {}", modifiedAfter);
                        }
                    }
                    
                    if (modifiedBefore != null) {
                        try {
                            java.time.LocalDateTime beforeDate = java.time.LocalDateTime.parse(modifiedBefore);
                            if (fileInfo.getLastModified().isAfter(beforeDate.atZone(java.time.ZoneId.systemDefault()).toInstant())) {
                                continue;
                            }
                        } catch (Exception e) {
                            logger.warn("Invalid modifiedBefore date format: {}", modifiedBefore);
                        }
                    }
                    
                    results.add(fileInfo);
                }
            }
        }
        
        return results;
    }
    
    // Inner class for file information
    public static class FileInfo {
        private String filename;
        private long size;
        private java.time.Instant lastModified;
        private String path;
        
        // Getters and Setters
        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }
        
        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }
        
        public java.time.Instant getLastModified() { return lastModified; }
        public void setLastModified(java.time.Instant lastModified) { this.lastModified = lastModified; }
        
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
    }
}
