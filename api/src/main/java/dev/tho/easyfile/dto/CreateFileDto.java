package dev.tho.easyfile.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class CreateFileDto {

    MultipartFile file;

    public CreateFileDto(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateFileDto createFileDto = (CreateFileDto) o;
        return file.equals(createFileDto.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public String toString() {
        return "CreateFileDto{" +
                "file=" + file +
                '}';
    }
}
