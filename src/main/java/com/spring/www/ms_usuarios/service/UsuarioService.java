package com.spring.www.ms_usuarios.service;

import com.spring.www.ms_usuarios.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsuarioService {

    Page<Usuario> paginar (Pageable pageable);

    Optional<Usuario> buscarUsuarioPorId (Long id);

    Usuario crear (Usuario usuario);

    Usuario editar (Usuario usuario);

    void eliminar (Long id);
}
