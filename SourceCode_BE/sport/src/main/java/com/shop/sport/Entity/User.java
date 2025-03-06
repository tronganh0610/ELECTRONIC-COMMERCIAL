package com.shop.sport.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity // Đánh dấu đây là table trong db
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "username" , unique = true)
  private String username;
  private String fullname;
  @Email
  private String email;
  private String password;
  private String phone;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @Column(name = "adress")
  private String adress;

  @Column(name = "birthday")
  private Date birthday;

  @Column(name = "public_id")
  private String publicId;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private Cart cart;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
  @JsonIgnore
  private Collection<Order1> orders;

  @OneToMany(mappedBy = "user_seller", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
  @JsonIgnore
  private Collection<OrderPayment> orderPayments;

//  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//  @JsonIgnore
//  private Collection<PaymentMethod> paymentMethods;




  private Boolean status;
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
