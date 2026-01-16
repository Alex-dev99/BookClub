package dto;

import java.time.LocalDateTime;

public record PrestamoDto(Long id, LocalDateTime fechaInicio, String estado, String tituloLibro) {}

