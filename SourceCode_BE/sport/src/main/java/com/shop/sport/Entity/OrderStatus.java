package com.shop.sport.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Collection;

@Entity // Đánh dấu đây là table trong db
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "order_status_name")
    private String orderStatusName;

    @Column(name = "order_status_detail")
    private String orderStatusDetail;

    @OneToMany(mappedBy = "orderStatus", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Order1> orders;
}
