package com.academy.workSearch.dto;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

@Data
public class PhotoDTO {
    private long fileLength;
    private ByteArrayResource photo;
}
