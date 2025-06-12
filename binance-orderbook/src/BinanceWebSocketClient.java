import java.net.URI;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import javax.websocket.*;

// If using an IDE or javac, ensure the following JARs are in your classpath:
//   lib/tyrus-client-2.1.1.jar
//   lib/tyrus-core-2.1.1.jar
//   lib/javax.websocket-api-1.1.jar
@SuppressWarnings("unchecked")
@ClientEndpoint
public class BinanceWebSocketClient {
    private final String symbol;
    private final OrderBook orderBook;
    private Session session;

    public BinanceWebSocketClient(String symbol, OrderBook orderBook) {
        this.symbol = symbol;
        this.orderBook = orderBook;
    }

    public void connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("wss://fstream.binance.com/ws/" + symbol.toLowerCase() + "@depth@100ms"));
            new CountDownLatch(1).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        Map<String, Object> parsed = JsonUtils.parseJson(message);
        long updateId = ((Number) parsed.get("u")).longValue();

        List<List<String>> bids = null;
        List<List<String>> asks = null;
        try {
            bids = (List<List<String>>) parsed.get("b");
            asks = (List<List<String>>) parsed.get("a");
        } catch (ClassCastException e) {
            // Handle or log error if needed
        }
        if (bids != null && asks != null) {
            orderBook.applyUpdate(updateId, bids, asks);
        }
    }
}
