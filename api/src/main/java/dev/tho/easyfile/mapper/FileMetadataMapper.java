package dev.tho.easyfile.mapper;

import dev.tho.easyfile.dto.FileMetadataDto;
import dev.tho.easyfile.model.FileMetadata;
import org.springframework.stereotype.Component;

@Component
public class FileMetadataMapper {

    public FileMetadataDto asDto(FileMetadata fileMetadata) {
        FileMetadataDto fileMetadataDto = new FileMetadataDto();
        fileMetadataDto.setId(fileMetadata.getId());
        fileMetadataDto.setCreatedDate(fileMetadata.getCreatedDate());
        fileMetadataDto.setFileName(fileMetadata.getFileName());
        return fileMetadataDto;
    }
}
