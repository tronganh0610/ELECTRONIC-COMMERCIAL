package com.shop.sport.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity // Đánh dấu đây là table trong db
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "quantity")
    private long productId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_product") // // thông qua khóa ngoại id
    private Product product;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_order") // // thông qua khóa ngoại id
    private Order1 order;


  
}
