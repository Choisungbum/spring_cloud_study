package org.example.orderservice.entitiy;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "orders")
// 직렬화 사용이유 : 객체를 전송하거나 다른 네트워크로 전송하거나 데이터베이스에 보관하기 위해서
//                 마셜링, 언마셜링 하귀 위해 사용
// 마셜링 : 객체나 특정 형태의 데이터를 저장 및 전송 가능한 데이터 형태로 변환
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private Integer qty;
    @Column(nullable = false)
    private Integer unitPrice;
    private Integer totalPrice;

    @Column(nullable = false)
    private String userId;
    @Column(nullable = false, updatable = true)
    private String orderId;

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDate createAt;

}
