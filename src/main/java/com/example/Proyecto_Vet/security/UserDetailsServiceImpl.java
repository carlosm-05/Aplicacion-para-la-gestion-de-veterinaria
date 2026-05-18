package com.example.Proyecto_Vet.security;

import com.example.Proyecto_Vet.model.Empleado;
import com.example.Proyecto_Vet.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmpleadoRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Empleado emp = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                emp.getUsername(),
                emp.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + emp.getRol())));
    }
}