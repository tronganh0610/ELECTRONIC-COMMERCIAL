package com.shop.sport.Repositories;

import com.shop.sport.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
@EnableJpaRepositories
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

//    @Query(value = "SELECT u FROM User u WHERE u.role = 'EMPLOYEE' and u.status= true and u.username =:username")
//    Optional<User> findByUsername(String username);

    @Query(value = "CALL check_delete_user(:idUser);", nativeQuery = true)
    Long check_delete_user(@Param("idUser") long id);

    @Query(value = "CALL getAllUserByRole(:chucvu);", nativeQuery = true)
    List<User> getAllByRole(@Param("chucvu") String chucvu);

    @Query(value = "CALL search_user(:role, :text);", nativeQuery = true)
    List<User> searchUser(@Param("role") String role,@Param("text") String text);

}
