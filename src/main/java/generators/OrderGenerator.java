package generators;
import models.Order;

public class OrderGenerator {

    public static Order getOrderDefault() {
        String ingredients = "61c0c5a71d1f82001bdaaa73";
        return new Order(ingredients);
    }

    public static Order getOrderWithoutIngredients() {
        return new Order(null);
    }

    public static Order getOrderWithWrongIngredientsHash() {
        String ingredients = "1incorrect1hash1test1";
        return new Order(ingredients);
    }
}
