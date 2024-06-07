package org.example.catalogservice.dto;

import lombok.Data;
import org.springframework.core.serializer.support.SerializationDelegate;

import javax.sql.rowset.serial.SerialArray;
import java.io.Serializable;

@Data
public class CatalogDto implements Serializable {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}
