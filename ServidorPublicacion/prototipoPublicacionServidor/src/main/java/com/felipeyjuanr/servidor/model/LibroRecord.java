package com.felipeyjuanr.servidor.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public record LibroRecord(String titulo, ArrayList<String>autores, int cantidadPaginas,
                          LocalDateTime fechaCreacion, double precio, boolean tapaDura)
{
}
