package com.free.agent.document.impl;

import com.free.agent.db.domain.UploadFiles;
import com.free.agent.db.service.UploadFilesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class DbTests {

    @Autowired
    private UploadFilesService uploadFilesService;

    @Test
    public void test_insert() {
        UploadFiles uploadFiles = new UploadFiles();
        uploadFiles.setFileName("insert1");
        uploadFiles.setCreateTime(new Date());
        uploadFilesService.save(uploadFiles);

        UploadFiles byId = uploadFilesService.getById(4);
        System.out.println(byId.toString());

    }
}
