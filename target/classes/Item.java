package com.exemplo.model;
import jakarta.persistence.*;

@Entity
public class Item {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigoItem;
    private Integer quantidade;
    private Double valorItem;

    @ManyToOne
    private Produto produto;

    public Item() {}

    public Long getCodigoItem() { return codigoItem; }
    public void setCodigoItem(Long codigoItem) { this.codigoItem = codigoItem; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Double getValorItem() { return valorItem; }
    public void setValorItem(Double valorItem) { this.valorItem = valorItem; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
}
