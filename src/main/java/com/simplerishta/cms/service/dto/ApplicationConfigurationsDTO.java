package com.simplerishta.cms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplerishta.cms.domain.ApplicationConfigurations} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationConfigurationsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 300)
    private String configKey;

    @NotNull
    @Size(max = 500)
    private String configValue;

    @Size(max = 500)
    private String description;

    @Size(max = 3)
    private String countryCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationConfigurationsDTO)) {
            return false;
        }

        ApplicationConfigurationsDTO applicationConfigurationsDTO = (ApplicationConfigurationsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicationConfigurationsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationConfigurationsDTO{" +
            "id=" + getId() +
            ", configKey='" + getConfigKey() + "'" +
            ", configValue='" + getConfigValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            "}";
    }
}
