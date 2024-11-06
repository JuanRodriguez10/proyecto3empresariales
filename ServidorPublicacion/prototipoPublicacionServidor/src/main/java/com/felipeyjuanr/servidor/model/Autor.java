package com.felipeyjuanr.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Autor {
    //Atributos
    private String nombre;
    private  int edad;
    private String nacionalidad;
}
