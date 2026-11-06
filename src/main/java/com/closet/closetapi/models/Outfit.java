package com.closet.closetapi.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "outfits")
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "sneaker_id")
    private Sneaker sneaker;

    @ManyToOne
    @JoinColumn(name = "tshirt_id")
    private TShirt tshirt;

    @ManyToOne
    @JoinColumn(name = "pants_id")
    private Pants pants;

    @ManyToOne
    @JoinColumn(name = "hoodie_id")
    private Hoodie hoodie;

    @ManyToOne
    @JoinColumn(name = "jacket_id")
    private Jacket jacket;

    // Eager because responses are serialized after the transaction closes (open-in-view is off)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "outfit_accessories",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "accessory_id"))
    private List<Accessory> accessories = new ArrayList<>();

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

    public Sneaker getSneaker() {
        return sneaker;
    }

    public void setSneaker(Sneaker sneaker) {
        this.sneaker = sneaker;
    }

    public TShirt getTshirt() {
        return tshirt;
    }

    public void setTshirt(TShirt tshirt) {
        this.tshirt = tshirt;
    }

    public Pants getPants() {
        return pants;
    }

    public void setPants(Pants pants) {
        this.pants = pants;
    }

    public Hoodie getHoodie() {
        return hoodie;
    }

    public void setHoodie(Hoodie hoodie) {
        this.hoodie = hoodie;
    }

    public Jacket getJacket() {
        return jacket;
    }

    public void setJacket(Jacket jacket) {
        this.jacket = jacket;
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessory> accessories) {
        this.accessories = accessories;
    }
}
