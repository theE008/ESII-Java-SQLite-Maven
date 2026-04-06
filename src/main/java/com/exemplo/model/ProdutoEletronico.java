package com.exemplo.model;
import jakarta.persistence.Entity;

@Entity
public class ProdutoEletronico extends Produto {
    private String voltagem;

    public ProdutoEletronico() {}

    public String getVoltagem() { return voltagem; }
    public void setVoltagem(String voltagem) { this.voltagem = voltagem; }
}
