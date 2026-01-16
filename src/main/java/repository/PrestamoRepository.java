package repository;

import model.EstadoPrestamo;
import model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioIdAndEstado(Long usuarioId, EstadoPrestamo estado);

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.libro JOIN FETCH p.usuario WHERE p.estado = :estado")
    Page<Prestamo> findByEstadoWithDetails(@Param("estado") EstadoPrestamo estado, Pageable pageable);
}
