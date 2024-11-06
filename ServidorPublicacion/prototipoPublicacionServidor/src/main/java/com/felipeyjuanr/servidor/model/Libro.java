package com.felipeyjuanr.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * @author Juan y Felipe R
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Libro
{
    //Atributos
    private String titulo;
    private ArrayList<Autor> autores;
    private int cantidadPaginas;
    private LocalDateTime fechaCreacion;
    private double precio;
    private boolean tapaDura;
}

