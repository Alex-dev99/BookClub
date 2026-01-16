package controller;


import dto.CreatePrestamoRequest;
import dto.PrestamoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.PrestamoService;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoDto> crear(@RequestBody CreatePrestamoRequest request) {
        return ResponseEntity.ok(prestamoService.crearPrestamo(request));
    }

    @PutMapping("/prestamos/{id}/devolver")
    public ResponseEntity<Void> devolver(@PathVariable Long id) {
        prestamoService.devolverPrestamo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prestamos/activos")
    public ResponseEntity<Page<PrestamoDto>> listarActivos(Pageable pageable) {
        return ResponseEntity.ok(prestamoService.obtenerActivos(pageable));
    }

}
