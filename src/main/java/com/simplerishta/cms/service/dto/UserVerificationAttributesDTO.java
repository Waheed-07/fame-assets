package com.simplerishta.cms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplerishta.cms.domain.UserVerificationAttributes} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserVerificationAttributesDTO implements Serializable {

    private Long id;

    private String documentType;

    private String documentUrl;

    private String status;

    private String verificationToken;

    private Instant lastActionDate;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Instant getLastActionDate() {
        return lastActionDate;
    }

    public void setLastActionDate(Instant lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserVerificationAttributesDTO)) {
            return false;
        }

        UserVerificationAttributesDTO userVerificationAttributesDTO = (UserVerificationAttributesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userVerificationAttributesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserVerificationAttributesDTO{" +
            "id=" + getId() +
            ", documentType='" + getDocumentType() + "'" +
            ", documentUrl='" + getDocumentUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", verificationToken='" + getVerificationToken() + "'" +
            ", lastActionDate='" + getLastActionDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
