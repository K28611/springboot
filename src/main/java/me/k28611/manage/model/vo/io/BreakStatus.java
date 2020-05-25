package me.k28611.manage.model.vo.io;

import lombok.Data;

import java.util.List;

@Data
public class BreakStatus {

    private  String fileStatus;

    private Long chunkSize;

    private List<Integer> notUploaded;

    public BreakStatus(String fileStatus){
        this.fileStatus = fileStatus;
    }

    public BreakStatus(String fileStatus,Long chunkSize,List<Integer> data){
        this.fileStatus = fileStatus;
        this.chunkSize = chunkSize;
        this.notUploaded = data;
    }
}
