package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.FileAttachment;
import com.mycompany.myapp.repository.FileAttachmentRepository;
import com.mycompany.myapp.service.FileAttachmentService;
import com.mycompany.myapp.service.dto.FileAttachmentDTO;
import com.mycompany.myapp.service.form.FileManagementDetail;
import com.mycompany.myapp.service.mapper.FileAttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FileAttachmentServiceImpl implements FileAttachmentService {

    @Autowired
    private FileAttachmentRepository fileAttachmentRepository;

    @Autowired
    private FileAttachmentMapper fileAttachmentMapper;

    public FileAttachmentDTO save(FileAttachmentDTO fileAttachmentDTO) {
        FileAttachment result = fileAttachmentMapper.toEntity(fileAttachmentDTO);
        result = fileAttachmentRepository.save(result);
        return fileAttachmentMapper.toDto(result);
    }

    @Override
    public List<FileManagementDetail> getFileManagement(Integer fileType, Long objectId) {
        return fileAttachmentRepository.getFileManagement(fileType, objectId);
    }

    @Override
    public Optional<FileAttachmentDTO> findOne(Long id) {
        return fileAttachmentRepository.findById(id).map(fileAttachmentMapper::toDto);
    }

    @Override
    public void deleteById(Long avatarId) {
        fileAttachmentRepository.deleteById(avatarId);
    }

    @Override
    public void deleteByObjectIdAndFileType(Long objectId, Integer fileType) {
        fileAttachmentRepository.deleteObjectIdAndFileType(objectId, fileType);
    }
}

