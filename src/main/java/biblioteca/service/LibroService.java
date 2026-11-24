package biblioteca.service;

import biblioteca.exception.ConflictoException;
import biblioteca.exception.RecursoNoEncontradoException;
import biblioteca.model.Libro;
import biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    private final LibroRepository repository;

    public LibroService(LibroRepository repository) {
        this.repository = repository;
    }

    public List<Libro> listar(String autor, Boolean disponible) {
        List<Libro> libros = (autor != null && !autor.isBlank())
                ? repository.findByAutorContainingIgnoreCase(autor)
                : repository.findAll();

        if (disponible != null) {
            libros = libros.stream()
                    .filter(l -> l.isDisponible() == disponible)
                    .toList();
        }
        return libros;
    }

    public Libro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro con id " + id));
    }

    public Libro crear(Libro libro) {
        if (repository.existsByIsbn(libro.getIsbn())) {
            throw new ConflictoException("Ya existe un libro con el ISBN " + libro.getIsbn());
        }
        libro.setId(null);
        libro.setDisponible(true);
        return repository.save(libro);
    }

    public Libro actualizar(Long id, Libro datos) {
        Libro existente = buscarPorId(id);
        if (repository.existsByIsbnAndIdNot(datos.getIsbn(), id)) {
            throw new ConflictoException("Ya existe otro libro con el ISBN " + datos.getIsbn());
        }
        existente.setTitulo(datos.getTitulo());
        existente.setAutor(datos.getAutor());
        existente.setIsbn(datos.getIsbn());
        existente.setAnioPublicacion(datos.getAnioPublicacion());
        return repository.save(existente);
    }

    public Libro prestar(Long id) {
        Libro libro = buscarPorId(id);
        if (!libro.isDisponible()) {
            throw new ConflictoException("El libro ya está prestado");
        }
        libro.setDisponible(false);
        return repository.save(libro);
    }

    public Libro devolver(Long id) {
        Libro libro = buscarPorId(id);
        if (libro.isDisponible()) {
            throw new ConflictoException("El libro no estaba prestado");
        }
        libro.setDisponible(true);
        return repository.save(libro);
    }

    public void eliminar(Long id) {
        repository.delete(buscarPorId(id));
    }
}
