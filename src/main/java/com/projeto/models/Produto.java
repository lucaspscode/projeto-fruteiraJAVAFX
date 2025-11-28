package com.projeto.models;

public class Produto {
    private String nome;
    private double valor;
    private int quantidadeEmEstoque;
    private String imagePath; // caminho no resources (ex: "/imagens/item1.svg")

    public Produto(String nome, double valor, int quantidadeEmEstoque) {
        this.nome = nome;
        this.valor = valor;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public Produto(String nome, double valor, int quantidadeEmEstoque, String imagePath) {
        this(nome, valor, quantidadeEmEstoque);
        this.imagePath = imagePath;
    }

    public String getNome() {
        return nome;
    }

    public double getValor() {
        return valor;
    }

    public int getQuantidade() {
        return quantidadeEmEstoque;
    }

    public void setQuantidade(int quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}