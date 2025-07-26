package tacos.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import tacos.TacoRepository;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TacoCountInfoContributor implements InfoContributor {

    private final TacoRepository tacoRepository;

    @Override
    public void contribute(Info.Builder builder) {
        long tacoCount = tacoRepository.count();
        Map<String, Object> tacoMap = new HashMap<String, Object>();
        tacoMap.put("count", tacoCount);
        builder.withDetail("taco-stats", tacoMap);
    }
}
