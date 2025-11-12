package com.mentoria.back_end_mentoria.resumo;

import com.mentoria.back_end_mentoria.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResumoController.class)
class ResumoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResumoService resumoService;

    @MockBean
    private TokenService tokenService; // Mocking TokenService dependency from SecurityConfig

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 200 OK ao fazer upload de um PDF")
    void uploadPdf_deveRetornarOk() throws Exception {
        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "PDF content".getBytes()
        );

        doNothing().when(resumoService).insertPDF(any());

        // Act & Assert
        mockMvc.perform(multipart("/resumos/meus/cv").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string("Upload do PDF realizado com sucesso."));

        verify(resumoService).insertPDF(mockFile);
    }
}
