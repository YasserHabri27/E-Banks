package com.ebank.dto.client;

public class ClientMinimalDTO {
    private Long id;
    private String nomComplet;
    private String numeroIdentite;
    private String email;

    public ClientMinimalDTO() {
    }

    public ClientMinimalDTO(Long id, String nomComplet, String numeroIdentite, String email) {
        this.id = id;
        this.nomComplet = nomComplet;
        this.numeroIdentite = numeroIdentite;
        this.email = email;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String nomComplet;
        private String numeroIdentite;
        private String email;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nomComplet(String nomComplet) {
            this.nomComplet = nomComplet;
            return this;
        }

        public Builder numeroIdentite(String numeroIdentite) {
            this.numeroIdentite = numeroIdentite;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public ClientMinimalDTO build() {
            return new ClientMinimalDTO(id, nomComplet, numeroIdentite, email);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getNumeroIdentite() {
        return numeroIdentite;
    }

    public void setNumeroIdentite(String numeroIdentite) {
        this.numeroIdentite = numeroIdentite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
