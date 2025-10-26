package br.com.coregate.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NocStatus {
    private String mode;
    private double tps;
    private double sla;
    private String updatedAt;
}