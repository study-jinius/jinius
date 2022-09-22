package com.study.jinius.attachment.service;

import com.study.jinius.attachment.exception.UnsupportedFileException;
import com.study.jinius.attachment.model.Attachment;
import com.study.jinius.attachment.model.AttachmentDownloadResponse;
import com.study.jinius.attachment.model.AttachmentUploadResponse;
import com.study.jinius.attachment.model.Result;
import com.study.jinius.attachment.repository.AttachmentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.springframework.http.MediaType.*;

@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final Path path;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
        // TODO: yaml로 빼서 관리하도록 하기
        this.path = Path.of("/Users/jduckling_1024/study/project/files");
    }

    @Transactional
    public List<AttachmentUploadResponse> upload(MultipartFile[] files) throws IOException {
        Map<Result, List<String>> wrongFileMap = new HashMap<>();
        List<Attachment> attachmentList = new ArrayList<>();

        for (MultipartFile file : files) {
            Result result = checkMultiFile(file);
            if (Result.SUCCESS.equals(result)) continue;

            List<String> fileNameList = wrongFileMap.getOrDefault(result, new ArrayList<>());
            fileNameList.add(file.getOriginalFilename());
            wrongFileMap.put(result, fileNameList);
        }

        if (!CollectionUtils.isEmpty(wrongFileMap)) {
            throw new UnsupportedFileException("파일 업로드에 실패했습니다 " + wrongFileMap);
        }

        for (MultipartFile file : files) {
            String storedFileName = storeFile(file);
            attachmentList.add(new Attachment(file.getOriginalFilename(), storedFileName, file.getContentType()));
        }

        List<Attachment> resultList = attachmentRepository.saveAll(attachmentList);
        return resultList.stream().map(AttachmentUploadResponse::from).toList();
    }

    @Transactional
    public AttachmentDownloadResponse download(String storedName) throws MalformedURLException {
        Attachment attachment = attachmentRepository.findByStoredName(storedName).orElseThrow();

        if (StringUtils.isBlank(attachment.getMediaType())) {
            throw new UnsupportedFileException("알 수 없는 파일 타입입니다.");
        }

        Path filePath = path.resolve(attachment.getStoredName());
        URI uri = filePath.toUri();

        UrlResource urlResource = new UrlResource(uri);
        return AttachmentDownloadResponse.from(attachment, urlResource);
    }

    private Result checkMultiFile(MultipartFile file) {
        if (file == null || StringUtils.isBlank(file.getOriginalFilename())) {
            return Result.EMPTY;
        } else if (!isValidContentType(file.getContentType())) {
            return Result.UNSUPPORTED;
        }

        return Result.SUCCESS;
    }

    private String createStoredName(String extension) {
        String storedName;
        do {
            storedName = UUID.randomUUID() + "." + extension;
        } while (attachmentRepository.findByStoredName(storedName).isPresent());

        return storedName;
    }

    private boolean isValidContentType(String contentType) {
        Set<String> whiteList = Set.of(IMAGE_JPEG_VALUE, IMAGE_GIF_VALUE, IMAGE_PNG_VALUE);
        return whiteList.contains(contentType);
    }

    private String storeFile(MultipartFile file) throws IOException {
        String[] fileNameToken = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String extension = fileNameToken[fileNameToken.length - 1];

        String storedName = createStoredName(extension);
        Path filePath = path.resolve(storedName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return storedName;
    }
}
