package com.example.fooddeliverymarketplace.document.ordertracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order_tracking")
public class OrderTrackingDocument {

    @Id
    private String id;

    private Long orderId;

    @Builder.Default
    private List<OrderTrackingEvent> events = new ArrayList<>();

    public void addEvent(OrderTrackingEvent event) {
        if (events == null) {
            events = new ArrayList<>();
        }

        events.add(event);
    }
}