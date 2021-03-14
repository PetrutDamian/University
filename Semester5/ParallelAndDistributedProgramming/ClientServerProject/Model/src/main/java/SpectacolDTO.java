import java.io.Serializable;
import java.util.List;

public class SpectacolDTO  implements Serializable {
    public String name;
    public List<Integer> locuriDisponibile;

    public SpectacolDTO(String name, List<Integer> locuriDisponibile) {
        this.name = name;
        this.locuriDisponibile = locuriDisponibile;
    }
}
