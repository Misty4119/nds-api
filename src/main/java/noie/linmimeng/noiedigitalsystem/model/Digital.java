package noie.linmimeng.noiedigitalsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Digital 模型類
 */
@Getter
@Setter
@AllArgsConstructor
public class Digital {
    
    private String name;
    private double amount;
    private double limit;
    
    public Digital(String name, double amount) {
        this.name = name;
        this.amount = amount;
        this.limit = -1; // 無限制
    }
}

