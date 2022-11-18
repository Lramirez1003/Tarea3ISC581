package com.example.tarea3.datamodels;



public class ProductoModel {
    private Integer id;
    private String name;
    private Integer price;
    private String marca;
    private String image64;

    public ProductoModel(Integer id, String name, Integer price, String marca, String image64) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.marca = marca;
        this.image64 = image64;
    }

    public ProductoModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getImage64() {
        return image64;
    }

    public void setImage64(String image64) {
        this.image64 = image64;
    }
}
