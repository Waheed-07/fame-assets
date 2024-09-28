package com.simplerishta.cms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserVerificationAttributes.
 */
@Entity
@Table(name = "user_verification_attributes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserVerificationAttributes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "status")
    private String status;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "last_action_date")
    private Instant lastActionDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserVerificationAttributes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public UserVerificationAttributes documentType(String documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentUrl() {
        return this.documentUrl;
    }

    public UserVerificationAttributes documentUrl(String documentUrl) {
        this.setDocumentUrl(documentUrl);
        return this;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getStatus() {
        return this.status;
    }

    public UserVerificationAttributes status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerificationToken() {
        return this.verificationToken;
    }

    public UserVerificationAttributes verificationToken(String verificationToken) {
        this.setVerificationToken(verificationToken);
        return this;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Instant getLastActionDate() {
        return this.lastActionDate;
    }

    public UserVerificationAttributes lastActionDate(Instant lastActionDate) {
        this.setLastActionDate(lastActionDate);
        return this;
    }

    public void setLastActionDate(Instant lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UserVerificationAttributes createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public UserVerificationAttributes updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserVerificationAttributes)) {
            return false;
        }
        return getId() != null && getId().equals(((UserVerificationAttributes) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserVerificationAttributes{" +
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
