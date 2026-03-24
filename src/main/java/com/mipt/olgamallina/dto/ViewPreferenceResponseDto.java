package com.mipt.olgamallina.dto;

public class ViewPreferenceResponseDto {

    private String mode;

    public ViewPreferenceResponseDto() {
    }

    public ViewPreferenceResponseDto(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}