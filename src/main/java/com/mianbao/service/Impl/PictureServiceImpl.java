package com.mianbao.service.Impl;

import com.mianbao.service.PictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by zoujiajian on 2017-4-11.
 */
@Service
public class PictureServiceImpl extends PictureServiceConfig implements PictureService{

    @Override
    public String uploadPicture(byte[] fileBuffer, String fileName) throws Exception {
        return getClinet().upload_file1(fileBuffer, fileName, null);
    }


    public String uploadFile(String local_filename, String file_ext_name) throws Exception {
        return getClinet().upload_file1(local_filename, file_ext_name, null);
    }
}
