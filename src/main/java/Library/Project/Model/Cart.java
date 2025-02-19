package Library.Project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends AbstractEntity{

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private Set<CartItem> cartItems = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "cart")
    private User user;

    public void addCartItem(CartItem item) {
        this.cartItems.add(item);
        item.setCart(this);
    }

    public void removeCartItem(CartItem item) {
        this.cartItems.remove(item);
        item.setCart(null);
    }
}
