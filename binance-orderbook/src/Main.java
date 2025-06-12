import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java Main <symbol>, e.g., BTCUSDT");
            return;
        }

        String symbol = args[0].toLowerCase();
        OrderBookService service = new OrderBookService(symbol);
        service.start();
    }
}
