package mds.biblioteca.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DevolucionRequestDto {
    Long idLibro;
    String nroSocio;
    Boolean buenasCondiciones;
}
