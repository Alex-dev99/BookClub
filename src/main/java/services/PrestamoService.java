package services;

import dto.CreatePrestamoRequest;
import dto.PrestamoDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import model.EstadoPrestamo;
import model.Libro;
import model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.LibroRepository;
import repository.PrestamoRepository;
import repository.UsuarioRepository;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PrestamoService {
    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PrestamoDto crearPrestamo(CreatePrestamoRequest request) {
        Libro libro = libroRepository.findById(request.libroId())
                .orElseThrow(() -> new EntityNotFoundException("libro no encontrado"));

        if (libro.getEjemplaresDisponibles() <= 0) {
            throw new IllegalStateException("no hay ejemplares disponibles");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);

        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuarioRepository.getReferenceById(request.usuarioId()));
        prestamo.setFechaInicio(LocalDateTime.now());
        prestamo.setEstado(EstadoPrestamo.ACTIVO);

        return mapToDto(prestamoRepository.save(prestamo));
    }

    @Transactional
    public void devolverPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("prestamo no encontrado"));

        if (prestamo.getEstado() == EstadoPrestamo.ACTIVO) {
            prestamo.setEstado(EstadoPrestamo.DEVUELTO);
            Libro libro = prestamo.getLibro();
            libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        }
    }

    public Page<PrestamoDto> obtenerActivos(Pageable pageable) {
        return prestamoRepository.findByEstadoWithDetails(EstadoPrestamo.ACTIVO, pageable)
                .map(this::mapToDto);
    }

    private PrestamoDto mapToDto(Prestamo p) {
        return new PrestamoDto(p.getId(), p.getFechaInicio(), p.getEstado().name(), p.getLibro().getTitulo());
    }
}
