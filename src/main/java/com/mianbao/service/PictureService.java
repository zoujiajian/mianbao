package com.mianbao.service;

/**
 * Created by zoujiajian on 2017-4-11.
 */
public interface PictureService {

    String uploadPicture(byte[] fileBuffer, String fileName) throws Exception;

    String uploadFile(String local_filename, String file_ext_name) throws Exception;

}
