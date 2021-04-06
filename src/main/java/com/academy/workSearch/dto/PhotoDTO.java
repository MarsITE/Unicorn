package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter

public class PhotoDTO {
    private long fileLength;
    private ByteArrayResource photo;
}
