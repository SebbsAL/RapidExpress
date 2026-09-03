package com.rapidexpress.model.entity;

/**
 * Contrato comun para toda entidad de dominio identificada por un id
 * numerico autogenerado en base de datos.
 */
public interface Identificable {
    int getId();
    void setId(int id);
}
