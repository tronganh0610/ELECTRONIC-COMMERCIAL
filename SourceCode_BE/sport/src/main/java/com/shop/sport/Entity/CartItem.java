package com.shop.sport.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;

@Entity // Đánh dấu đây là table trong db
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_Cart") // // thông qua khóa ngoại id
    private Cart cart;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_Product") // // thông qua khóa ngoại id
    private Product product;

    @Column(name = "quantity")
    private int quantity;




}
