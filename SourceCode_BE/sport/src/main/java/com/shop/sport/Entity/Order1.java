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
public class Order1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "total_amount")
    private Float totalAmount;

    @Column(name = "shipping_date")
    private Date shippingDate;

    @Column(name = "shipping_adress")
    private String shippingAdress;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "name_reciver")
    private String name_reciver;



    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_User") // // thông qua khóa ngoại id
    private User user;// đơn hàng của user nào ?
//
//
    @ManyToOne
//    @JsonIgnore
    @JoinColumn(name = "id_order_status") // // thông qua khóa ngoại id
    private OrderStatus orderStatus;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_shipping_method") // // thông qua khóa ngoại id
    private ShippingMethod shippingMethod;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<OrderItem> orderItems;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<OrderPayment> orderPayments;

}
