package com.ebank.dto.auth;

public class LoginResponseDTO {

    private String token;
    private String type = "Bearer";
    private String email;
    private String role;
    private Long expiresIn;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, String type, String email, String role, Long expiresIn) {
        this.token = token;
        this.type = type != null ? type : "Bearer";
        this.email = email;
        this.role = role;
        this.expiresIn = expiresIn;
    }

    // Builder pattern manually implemented since used in AuthenticationServiceImpl
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private String type = "Bearer";
        private String email;
        private String role;
        private Long expiresIn;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public LoginResponseDTO build() {
            return new LoginResponseDTO(token, type, email, role, expiresIn);
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
