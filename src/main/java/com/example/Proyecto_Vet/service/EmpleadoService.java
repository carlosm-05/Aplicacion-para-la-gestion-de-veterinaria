package com.example.Proyecto_Vet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Proyecto_Vet.model.Empleado;
import com.example.Proyecto_Vet.repository.EmpleadoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository repo;
    private final PasswordEncoder passwordEncoder;

    public Empleado save(Empleado empleado) {
        if (empleado.getPassword() != null && !empleado.getPassword().isBlank()) {
            empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
        }
        if (empleado.getRol() == null) {
            empleado.setRol("EMPLEADO");
        }
        return repo.save(empleado);
    }

    public List<Empleado> findAll() {
        return repo.findAll();
    }

    public Empleado findById(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado: " + id));
    }

    public Empleado update(Integer id, Empleado datos) {
        Empleado e = findById(id);

        if (datos.getNombre() != null && !datos.getNombre().isBlank())
            e.setNombre(datos.getNombre());

        if (datos.getPosicion() != null)
            e.setPosicion(datos.getPosicion());

        if (datos.getRol() != null && !datos.getRol().isBlank())
            e.setRol(datos.getRol());

        if (datos.getUsername() != null && !datos.getUsername().isBlank())
            e.setUsername(datos.getUsername());

        // Solo re-encripta si viene una contraseña nueva (en texto plano)
        if (datos.getPassword() != null && !datos.getPassword().isBlank()
                && !datos.getPassword().startsWith("$2a$")) {
            e.setPassword(passwordEncoder.encode(datos.getPassword()));
        }

        return repo.save(e);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
