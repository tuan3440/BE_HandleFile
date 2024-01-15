package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.FileAttachment;
import com.mycompany.myapp.service.dto.FileAttachmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface FileAttachmentMapper extends EntityMapper<FileAttachmentDTO, FileAttachment> {}
