import java.util.*;

public class OrderBook {
    private final TreeMap<Double, Double> bids = new TreeMap<>(Collections.reverseOrder());
    private final TreeMap<Double, Double> asks = new TreeMap<>();
    private long lastUpdateId;

    public synchronized void updateSnapshot(String json) {
        Map<String, Object> parsed = JsonUtils.parseJson(json);
        this.lastUpdateId = ((Number) parsed.get("lastUpdateId")).longValue();

        List<List<String>> bidsList = (List<List<String>>) parsed.get("bids");
        List<List<String>> asksList = (List<List<String>>) parsed.get("asks");

        for (List<String> bid : bidsList) {
            bids.put(Double.parseDouble(bid.get(0)), Double.parseDouble(bid.get(1)));
        }
        for (List<String> ask : asksList) {
            asks.put(Double.parseDouble(ask.get(0)), Double.parseDouble(ask.get(1)));
        }
        printTopLevels();
    }

    public synchronized void applyUpdate(long updateId, List<List<String>> newBids, List<List<String>> newAsks) {
        if (updateId <= lastUpdateId) return; // Skip old updates

        for (List<String> bid : newBids) {
            double price = Double.parseDouble(bid.get(0));
            double qty = Double.parseDouble(bid.get(1));
            if (qty == 0.0) bids.remove(price);
            else bids.put(price, qty);
        }

        for (List<String> ask : newAsks) {
            double price = Double.parseDouble(ask.get(0));
            double qty = Double.parseDouble(ask.get(1));
            if (qty == 0.0) asks.remove(price);
            else asks.put(price, qty);
        }

        this.lastUpdateId = updateId;
        printTopLevels();
    }

    private void printTopLevels() {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("lastUpdateId", lastUpdateId);
        json.put("bids", getTop(bids));
        json.put("asks", getTop(asks));

        System.out.println(JsonUtils.toJson(json));
    }

    private List<List<String>> getTop(TreeMap<Double, Double> book) {
        List<List<String>> top = new ArrayList<>();
        int count = 0;
        for (Map.Entry<Double, Double> entry : book.entrySet()) {
            if (count++ == 3) break;
            top.add(Arrays.asList(String.format("%.8f", entry.getKey()), String.format("%.8f", entry.getValue())));
        }
        return top;
    }
}
