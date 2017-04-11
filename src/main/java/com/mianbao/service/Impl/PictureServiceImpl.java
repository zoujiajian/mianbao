package com.mianbao.service.Impl;

import com.mianbao.service.FileLoadService;
import com.mianbao.service.PictureService;
import org.csource.fastdfs.StorageClient1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by zoujiajian on 2017-4-11.
 */
@Service
public class PictureServiceImpl extends PictureServiceConfig implements PictureService{

    private static final Logger logger = LoggerFactory.getLogger(PictureServiceImpl.class);

    @Override
    public String uploadPicture(byte[] fileBuffer, String fileName) throws Exception {
        StorageClient1 client = getClinet();
        String pictureAddress = null;
        try{
            pictureAddress = client.upload_file1(fileBuffer, fileName, null);
        }catch (Exception e){
            logger.error("uploadFile error",e);
        }finally {
            returnPool(client);
        }
        return pictureAddress;
    }


    public String uploadFile(String local_filename, String file_ext_name) throws Exception {
        StorageClient1 client = getClinet();
        String pictureAddress = null;
        try{
            pictureAddress = client.upload_file1(local_filename, file_ext_name, null);
        }catch (Exception e){
            logger.error("uploadFile error",e);
        }finally {
            returnPool(client);
        }
        return pictureAddress;
    }
}
