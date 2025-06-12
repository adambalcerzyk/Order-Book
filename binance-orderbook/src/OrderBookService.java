import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class OrderBookService {
    private final String symbol;
    private final OrderBook orderBook;

    public OrderBookService(String symbol) {
        this.symbol = symbol;
        this.orderBook = new OrderBook();
    }

    public void start() throws IOException {
        fetchInitialSnapshot();
        BinanceWebSocketClient client = new BinanceWebSocketClient(symbol, orderBook);
        client.connect();
    }

    private void fetchInitialSnapshot() throws IOException {
        String url = "https://fapi.binance.com/fapi/v1/depth?symbol=" + symbol.toUpperCase() + "&limit=1000";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            orderBook.updateSnapshot(json.toString());
        }
    }
}