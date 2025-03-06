package com.shop.sport.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity // Đánh dấu đây là table trong db
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "payment_amout")
    private float paymentAmout;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_order") // // thông qua khóa ngoại id
    private Order1 order;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_seller") // // thông qua khóa ngoại id
    private User user_seller;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_payment_method") // // thông qua khóa ngoại id
    private PaymentMethod paymentMethod;


}
