package com.rapidexpress.model.entity;

/**
 * Superclase comun para toda entidad de dominio identificada por un id
 * numerico autogenerado en base de datos.
 */
public abstract class EntidadBase implements Identificable {
    protected int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
