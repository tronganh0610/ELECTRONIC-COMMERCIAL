package com.shop.sport.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity // Đánh dấu đây là table trong db
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShippingMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "shipping_method_name")
    private String shippingMethodName;

    @Column(name = "shipping_method_detail")
    private String shippingMethodDetail;

    @OneToMany(mappedBy = "shippingMethod", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Order1> orders;
}
