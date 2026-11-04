import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);

        for (;;) {
            System.out.print("Input a file name:");
            String name = in.nextLine();
            Scanner s = new Scanner(new File(name + ".txt"));
            String ticker = s.next();
            int n = s.nextInt();

            int[] years = new int[n];
            int[] months = new int[n];
            double[] prices = new double[n];
            
            loadData(s, years, months, prices);
            printData(years, months, prices);
            int low = indexOfLowest(prices);
            int high = indexOfHighest(prices);
            System.out.println(ticker + " lowest: $" + prices[low] + " in " + monthName(months[low]) + " " + years[low]);
            System.out.println(ticker + " highest: $" + prices[high] + " in " + monthName(months[high]) + " " + years[high]);
        }
    }

    public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            years[i] = in.nextInt();
            months[i] = in.nextInt();
            prices[i] = in.nextDouble();
        }
    }

    public static String monthName(int month) {
        String[] months = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month - 1];
    }

    public static void printData(int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            System.out.print(monthName(months[i]) + " " + years[i] + ": $");
            System.out.printf("%.2f", prices[i]);
            System.out.println();
        }
    }

    public static int indexOfHighest(double[] prices) {
		int arrayIndex = 0;
        for (int i = 0; i < prices.length; i++) {
			if(prices[i] > prices[arrayIndex]) {
				arrayIndex = i;
			}
        }
        return arrayIndex;
    }
    
    public static int indexOfLowest(double[] prices) {
		int arrayIndex = 0;
        for (int i = 0; i < prices.length; i++) {
			if(prices[i] < prices[arrayIndex]) {
				arrayIndex = i;
			}
        }
        return arrayIndex;
    }
}
