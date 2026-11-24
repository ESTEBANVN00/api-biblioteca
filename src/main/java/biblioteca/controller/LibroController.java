package biblioteca.controller;

import biblioteca.model.Libro;
import biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService service;

    public LibroController(LibroService service) {
        this.service = service;
    }

    @GetMapping
    public List<Libro> listar(@RequestParam(required = false) String autor,
                              @RequestParam(required = false) Boolean disponible) {
        return service.listar(autor, disponible);
    }

    @GetMapping("/{id}")
    public Libro obtener(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Libro crear(@Valid @RequestBody Libro libro) {
        return service.crear(libro);
    }

    @PutMapping("/{id}")
    public Libro actualizar(@PathVariable Long id, @Valid @RequestBody Libro libro) {
        return service.actualizar(id, libro);
    }

    @PatchMapping("/{id}/prestar")
    public Libro prestar(@PathVariable Long id) {
        return service.prestar(id);
    }

    @PatchMapping("/{id}/devolver")
    public Libro devolver(@PathVariable Long id) {
        return service.devolver(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
