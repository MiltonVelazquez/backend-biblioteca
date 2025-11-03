
package mds.biblioteca.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DevolucionRequestDto {
    // ¡AÑADIR private Y @NotNull!
    @NotNull private Long idLibro;
    @NotNull private String nroSocio; // @NotBlank también es válido
    @NotNull private Boolean buenasCondiciones;
}
