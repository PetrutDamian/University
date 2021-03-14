import java.util.List;

public interface IServices {
  List<SpectacolDTO> connect(Observer client);
  void buyTickets(Observer client,String spectacol, List<Integer> locuri);
}
