package tacos.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix="taco.discount")
@Data
@NoArgsConstructor
public class DiscountCodeProps {

  private Map<String, Integer> codes = new HashMap<>();
  
}
