package org.example.weather.controller;

import lombok.AllArgsConstructor;
import org.example.weather.dto.UserDTO;
import org.example.weather.entity.UserEntity;
import org.example.weather.exceptions.UsernameAlreadyExistsException;
import org.example.weather.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ResponseEntity<List<UserEntity>> readAll() {
        return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<UserEntity> create(@RequestBody UserDTO dto) {
        return new ResponseEntity<>(service.addUser(dto), HttpStatus.OK);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.PUT)
    public ResponseEntity<UserEntity> update(@RequestBody UserEntity user) {
        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }

    @DeleteMapping("/registration/{id}")
    public HttpStatus delete(@PathVariable Long id) {
        service.delete(id);
        return HttpStatus.OK;
    }
}
