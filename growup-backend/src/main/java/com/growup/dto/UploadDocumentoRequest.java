package com.growup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadDocumentoRequest {
    private String nomeProjeto;
    private String conteudoDocumento;
    private String contextoAdicional;
}
