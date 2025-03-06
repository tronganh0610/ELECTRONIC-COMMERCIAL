package com.shop.sport.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.sport.Utils.MethodPayMent;
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
@Builder
public class PaymentMethod {

    //ShipCode : tiền mặt
    // Zalopay

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "id_custommer") // // thông qua khóa ngoại id
//    private User user;

    @Enumerated(EnumType.STRING)
    private MethodPayMent payment_name;

    @Column(name = "payment_method_detail")
    private String paymentMethodDetail;

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<OrderPayment> orderPayments;
}
