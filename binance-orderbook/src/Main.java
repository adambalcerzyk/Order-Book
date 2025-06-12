public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the market symbol (e.g., BTCUSDT)");
            return;
        }

        String symbol = args[0];
        System.out.println("Starting Order Book for: " + symbol);

        // We'll write the logic here in later steps
    }
}