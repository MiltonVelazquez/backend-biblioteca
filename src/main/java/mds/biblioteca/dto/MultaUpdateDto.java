// src/main/java/mds/biblioteca/dto/MultaUpdateDto.java
package mds.biblioteca.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MultaUpdateDto {

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser un número positivo")
    private Double monto;
}
