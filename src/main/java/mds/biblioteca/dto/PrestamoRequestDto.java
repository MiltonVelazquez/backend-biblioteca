package mds.biblioteca.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrestamoRequestDto {
    @NotNull private Long idLibro;
    @NotBlank private String nroSocio;
}
