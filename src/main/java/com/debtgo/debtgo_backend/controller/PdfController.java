package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.simulation.SimulationResponseDto;
import com.debtgo.debtgo_backend.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PdfController {

    private final PdfGeneratorService pdfService;

    /**
     * Genera y devuelve un PDF profesional del reporte de simulación.
     * Recibe el objeto SimulationResponseDto con todos los datos de la simulación.
     */
    @PostMapping("/simulation")
    public ResponseEntity<byte[]> generarPDF(
            @RequestBody SimulationResponseDto dto,
            @RequestHeader(value = "X-User-Name", required = false) String userName // Viene del front o JWT
    ) {
        try {
            // Si no llega el nombre del usuario, asigna uno genérico
            if (userName != null && !userName.isEmpty()) {
                dto.setUserName(userName);
            } else if (dto.getUserName() == null) {
                dto.setUserName("Usuario DebtGo");
            }

            byte[] pdf = pdfService.generarPDF(dto);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_simulacion_debtgo.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error al generar PDF: " + e.getMessage()).getBytes());
        }
    }
}