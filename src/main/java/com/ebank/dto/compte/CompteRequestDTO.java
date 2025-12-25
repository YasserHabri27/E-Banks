package com.ebank.dto.compte;

import jakarta.validation.constraints.Pattern;

public class CompteRequestDTO {

    private Long clientId;

    private String numeroIdentiteClient;

    // RG_9: RIB validation
    @Pattern(regexp = "^FR\\d{2}\\d{5}\\d{5}\\w{11}\\d{2}$", message = "Invalid RIB format")
    private String rib;

    public CompteRequestDTO() {
    }

    public CompteRequestDTO(Long clientId, String numeroIdentiteClient, String rib) {
        this.clientId = clientId;
        this.numeroIdentiteClient = numeroIdentiteClient;
        this.rib = rib;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getNumeroIdentiteClient() {
        return numeroIdentiteClient;
    }

    public void setNumeroIdentiteClient(String numeroIdentiteClient) {
        this.numeroIdentiteClient = numeroIdentiteClient;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }
}
