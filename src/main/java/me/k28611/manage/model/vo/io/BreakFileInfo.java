package me.k28611.manage.model.vo.io;

import lombok.Data;

import java.util.List;

/**
 * @author K28611
 * @date 2020/5/3 14:43
 */
@Data
public class BreakFileInfo {

    private boolean isExists;

    private boolean isBlock;

    private String message;

    private String uploadType;

    private Long chunkSize;

    private List<String> fileAddress;

}
