package br.com.coregate.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class Pan {

    @NotBlank
    String value; // armazenamento deve ser tokenizado/criptografado fora do domínio

    @JsonCreator
    public Pan(@JsonProperty("value") String value) {
        if (value == null || value.trim().length() < 12) {
            throw new IllegalArgumentException("invalid PAN");
        }
        this.value = value.trim();
    }

    public static Pan of(String v) {
        return new Pan(v);
    }

    public String masked() {
        return "****" + value.substring(value.length() - 4);
    }
}
