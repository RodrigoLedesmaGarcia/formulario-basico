package com.spring.www.ms_usuarios.controller;

import com.spring.www.ms_usuarios.entity.Usuario;
import com.spring.www.ms_usuarios.service.UsuarioServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioServiceImpl service;

    public UsuarioController(UsuarioServiceImpl service) {
        this.service = service;
    }


    @GetMapping("/listar")
    public ResponseEntity<?> listar (Pageable pageable){
        Page<Usuario> lista = service.paginar(pageable);
        if (lista.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(lista);
        }
    }

    @GetMapping("/buscarId/{id}")
    public ResponseEntity<?> buscarPorId (@PathVariable Long id){
        Optional<Usuario> usuarioId = service.buscarUsuarioPorId(id);
        if (usuarioId.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(usuarioId);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear (@Valid @RequestBody Usuario usuario, BindingResult result){
        if (result.hasErrors()){
            return getObjectResponseEntityErrors(result);
        } else {
            service.crear(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar (@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){
        if (result.hasErrors()){
            return getObjectResponseEntityErrors(result);
        } else {
            Optional<Usuario> usuarioId = service.buscarUsuarioPorId(id);
            if (usuarioId.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            Usuario entity = usuarioId.get();

            entity.setNombre(usuario.getNombre());
            entity.setApp(usuario.getApp());
            entity.setApm(usuario.getApm());
            entity.setCorreo(usuario.getCorreo());

            Usuario edited = entity;

            try {
                service.editar(edited);
                return ResponseEntity.ok(edited);
            } catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id){
        Optional<Usuario> borrar = service.buscarUsuarioPorId(id);
        if (borrar.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            service.eliminar(id);
            return ResponseEntity.ok(id);
        }
    }


    // metodo para mapear errores
    private static ResponseEntity<Object> getObjectResponseEntityErrors(BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        result.getFieldErrors().forEach(e -> {response.put(e.getField(), e.getDefaultMessage());});
        return ResponseEntity.badRequest().build();
    }
}
