package com.musti.modal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    private double quantity ;
    private double buyPrice;

    @ManyToOne
    private Coin  coin;

    @ManyToOne
    private Users user;


}
