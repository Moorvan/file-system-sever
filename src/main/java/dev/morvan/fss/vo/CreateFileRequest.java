package dev.morvan.fss.vo;

import lombok.Data;

@Data
public class CreateFileRequest {
    String dirName;
    String fileName;
    String content;
}
