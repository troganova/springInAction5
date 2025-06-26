package tacos.controller;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="taco.orders")
public class OrderProps {
    private Integer pageSize;
}
