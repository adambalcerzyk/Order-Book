import java.net.URI;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import javax.websocket.*;

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
            container.connectToServer(this, new URI("wss://fstream.binance.com/ws/" + symbol + "@depth@100ms"));
            new CountDownLatch(1).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        Map<String, Object> parsed = JsonUtils.parseJson(message);
        long updateId = ((Number) parsed.get("u")).longValue();

        List<List<String>> bids = (List<List<String>>) parsed.get("b");
        List<List<String>> asks = (List<List<String>>) parsed.get("a");

        orderBook.applyUpdate(updateId, bids, asks);
    }
}
