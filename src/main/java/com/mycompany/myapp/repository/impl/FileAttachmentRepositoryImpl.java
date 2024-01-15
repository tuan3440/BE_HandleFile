package com.mycompany.myapp.repository.impl;

import com.mycompany.myapp.domain.FileAttachment;
import com.mycompany.myapp.repository.FileAttachmentRepositoryCustom;
import com.mycompany.myapp.service.form.FileManagementDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FileAttachmentRepositoryImpl implements FileAttachmentRepositoryCustom {

    @Autowired
    public EntityManager entityManager;

    @Override
    public List<FileManagementDetail> getFileManagement(Integer fileType, Long objectId) {
        String sql = "SELECT f " +
            " from FileAttachment f where  1 = 1                  " +
            "and f.fileType = :fileType                                            " +
            "and f.objectId = :objectId                                       ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("fileType", fileType);
        query.setParameter("objectId", objectId);
//        List<FileAttachment> result = query.getResultList();
//        SQLQuery query = sessionFactory.getSessionFactory().getCurrentSession().createSQLQuery(sql);
//        query.setParameter("fileType", fileType);
//        query.setParameter("objectId", objectId);
        List<FileAttachment> result = query.getResultList();
        List<FileManagementDetail> list = new ArrayList<>();
        result.forEach(r -> {
            FileManagementDetail y = new FileManagementDetail();
            y.setId(r.getId());
            y.setSourceUrl(String.format("/file-managements/view?id=%d", r.getId()));
            y.setSourceFile(String.format("/file-managements/download?id=%d", r.getId()));
            y.setFileName(r.getFileName());
            list.add(y);
        });

        return list;
    }
}
