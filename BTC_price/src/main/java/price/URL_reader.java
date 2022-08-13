package price;

import com.opencsv.CSVWriter;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class URL_reader extends Thread {

    @Override
    public void run() {
        int i = 0;


        while (true) {
            URL url = null;
            try {
                url = new URL("https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {

                String line;

                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {

                    sb.append(line);
                    sb.append(System.lineSeparator());
                }

                File file = new File("BTC_price.csv");
                try {

                    FileWriter outputfile = new FileWriter(file, true);

                    CSVWriter writer = new CSVWriter(outputfile,',', '"');
                    if(i == 0) {
                        String[] header = { "Currency", "Value" };
                        writer.writeNext(header);
                        i++;
                    }


                    String s = sb.substring(sb.lastIndexOf(":") + 1, sb.lastIndexOf("}"));

                    String[] data = { "USD", s};
                    writer.writeNext(data);

                    writer.close();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        URL_reader r = new URL_reader();
        r.run();
    }
}

