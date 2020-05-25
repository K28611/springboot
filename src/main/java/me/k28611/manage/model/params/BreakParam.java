package me.k28611.manage.model.params;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author K28611
 * @date 2020/5/3 15:19
 */
@Data
public class BreakParam {

    private String uid;

    private String id;


    private int chunks;


    private int chunk;


    private long chunkSize;


    private long size;


    private String name;


    private MultipartFile file;


    private String md5;


    private String groupName;





}
