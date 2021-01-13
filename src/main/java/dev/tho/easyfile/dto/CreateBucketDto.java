package dev.tho.easyfile.dto;


import javax.validation.constraints.NotBlank;

public class CreateBucketDto {

    @NotBlank
    private String password;

    public CreateBucketDto() {};

    public String getPassword() {
        return password;
    }
}
