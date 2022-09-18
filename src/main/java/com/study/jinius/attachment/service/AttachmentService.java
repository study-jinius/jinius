package com.study.jinius.attachment.service;

import com.study.jinius.attachment.model.Attachment;
import com.study.jinius.attachment.model.AttachmentResponse;
import com.study.jinius.attachment.model.Result;
import com.study.jinius.attachment.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.springframework.http.MediaType.*;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final String PATH = "/Users/jduckling_1024/study/project/files";
    private final AttachmentRepository attachmentRepository;

    @Transactional
    public List<AttachmentResponse> upload(MultipartFile[] files) throws IOException {
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
            // TODO: 예외처리
        }

        for (MultipartFile file : files) {
            String storedFileName = storeFile(file);
            attachmentList.add(new Attachment(file.getOriginalFilename(), storedFileName));
        }

        List<Attachment> resultList = attachmentRepository.saveAll(attachmentList);
        return resultList.stream().map(AttachmentResponse::from).toList();
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
        } while (attachmentRepository.findByStoredName(storedName) != null);

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
        Path path = Paths.get(PATH + "/" + storedName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return storedName;
    }
}
