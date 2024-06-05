import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = "dd6c7a4524323fba596068c1";
    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private static final Map<String, String> CURRENCIES = new HashMap<>();

    static {
        CURRENCIES.put("ARS", "Peso argentino");
        CURRENCIES.put("BOB", "Boliviano boliviano");
        CURRENCIES.put("BRL", "Real brasileiro");
        CURRENCIES.put("CLP", "Peso chileno");
        CURRENCIES.put("COP", "Peso colombiano");
        CURRENCIES.put("USD", "Dólar americano");
    }

    public static void main(String[] args) {
        try {
            System.out.println("Bem-vindo ao Conversor de Moedas!");
            Scanner scanner = new Scanner(System.in);

            System.out.println("Escolha a moeda de origem:");
            printCurrencyList();
            String convertFrom = scanner.nextLine().toUpperCase();

            System.out.println("Escolha a moeda de destino:");
            printCurrencyList();
            String convertTo = scanner.nextLine().toUpperCase();

            System.out.println("Digite o valor a ser convertido:");
            BigDecimal amount = scanner.nextBigDecimal();

            String urlString = API_BASE_URL + API_KEY + "/latest/" + convertFrom;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
            JsonObject ratesObject = jsonResponse.getAsJsonObject("conversion_rates");

            BigDecimal exchangeRate = ratesObject.getAsJsonPrimitive(convertTo).getAsBigDecimal();
            BigDecimal result = amount.multiply(exchangeRate);

            System.out.println(amount + " " + convertFrom + " = " + result + " " + convertTo);
        } catch (IOException e) {
            System.err.println("Erro de entrada/saída: " + e.getMessage());
        }
    }

    private static void printCurrencyList() {
        for (Map.Entry<String, String> entry : CURRENCIES.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }
}