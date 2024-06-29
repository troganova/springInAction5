package tacos.data;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tacos.domain.Ingredient;
import tacos.domain.Order;
import tacos.domain.Taco;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private JdbcOperations jdbcOperations;

    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco_Order  (name, street, city, state, zip, ccNumber, ccExpiration, ccCVV, placed_at) "
                        + "values (?,?,?,?,?,?,?,?,?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );
        pscf.setReturnGeneratedKeys(true);
        order.setPlacedAt(new Date());
        PreparedStatementCreator psc =  pscf.newPreparedStatementCreator(Arrays.asList(
                        order.getName(), order.getStreet(), order.getCity(), order.getState(), order.getZip(), order.getCcNumber(), order.getCcExpiration(), order.getCcCVV(), order.getPlacedAt()));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();
        int i=0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }
        return order;
    }

    private long saveTaco(Long orderId, int orderKey, Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
        "insert into Taco (name, created_at) values (?, ?)",
            Types.VARCHAR, Types.TIMESTAMP
        );
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList( taco.getName(), taco.getCreatedAt()));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);
        saveTacoToOrder(tacoId, orderId);
        saveIngredientRefs(tacoId, taco.getIngredients());
        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<Ingredient> ingredientRefs) {
        for (Ingredient ingredientRef : ingredientRefs) {
            jdbcOperations.update("insert into Taco_Ingredients (ingredient, taco) values (?, ?)",
                    ingredientRef.getId(), tacoId);
        }
    }

    private void saveTacoToOrder(long tacoId, long orderId) {
        jdbcOperations.update("insert into Taco_Order_Tacos (taco_order, taco) values (?, ?)",
                    orderId, tacoId);
    }
}
