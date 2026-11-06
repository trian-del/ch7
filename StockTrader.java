
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {
    public static final double START_CASH = 10000.0;
    public static final double FEE = 10.0;

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
            int dropIndex = indexOfBiggestDrop(prices);
            double dropPercent = percentageOfBiggestDrop(prices);

            System.out.println(ticker + " lowest: $" + prices[low] + " in " + monthName(months[low])
                + " " + years[low]);
            System.out.println(ticker + " highest: $" + prices[high] + " in "
                + monthName(months[high]) + " " + years[high]);
            System.out.print("Largest drop is ");
            System.out.printf("%.2f", dropPercent);
            System.out.println("% between " + monthName(months[dropIndex]) + " " + years[dropIndex]
                + " and " + monthName(months[dropIndex + 1]) + " " + years[dropIndex + 1]);
            double result = simulate(years, months, prices);
            System.out.printf("Final value: $%,.2f%n", result);
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
            if (prices[i] > prices[arrayIndex]) {
                arrayIndex = i;
            }
        }
        return arrayIndex;
    }

    public static int indexOfLowest(double[] prices) {
        int arrayIndex = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < prices[arrayIndex]) {
                arrayIndex = i;
            }
        }
        return arrayIndex;
    }

    public static int indexOfBiggestDrop(double[] prices) {
        int arrayIndex = 0;
        double prevDrop = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            if ((prices[i] - prices[i + 1]) / prices[i] > prevDrop) {
                prevDrop = ((prices[i] - prices[i + 1]) / prices[i]);
                arrayIndex = i;
            }
        }
        return arrayIndex;
    }

    public static double percentageOfBiggestDrop(double[] prices) {
        double prevDrop = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            if ((prices[i] - prices[i + 1]) / prices[i] > prevDrop) {
                prevDrop = ((prices[i] - prices[i + 1]) / prices[i]);
            }
        }
        return prevDrop * 100;
    }

    public static String buyAndHold(double[] history, boolean holding, double boughtAt) {
        return "BUY";
    }

    public static String dipBuyer(double[] history, boolean holding, double boughtAt) {

        if (holding) {
            if (history[history.length - 1] > boughtAt * 1.1) {
                return "SELL";
            }
        } else {
            if (history.length >= 3) {
                if (history[history.length - 3] > history[history.length - 2]
                    && history[history.length - 2] > history[history.length - 1]) {
                    return "BUY";
                }
            }
        }
        return "";
    }
    
    public static String movingAverage(double[] history, boolean holding, double boughtAt) {
		double tot = 0;
		double avg = 0;
		if(history.length>11) {
			for(int i = history.length-12; i < history.length; i++) {
				avg+=history[i];
			}
			avg/=12;
			//System.out.println(avg+ " " + history[history.length-1]);
			if(!holding && history[history.length-1] > avg) return "BUY";
			if (holding && history[history.length-1] < avg) return "SELL";
		}
		return "";
	}

    public static String decide(double[] history, boolean holding, double boughtAt) {
        return movingAverage(history, holding, boughtAt); // change this line to switch strategies
    }

    public static double simulate(int[] years, int[] months, double[] prices) {
        double cash = START_CASH;
        int shares = 0;
        double boughtAt = 0.0;

        for (int i = 0; i < prices.length; i++) {
            double[] history = Arrays.copyOf(prices, i + 1);
            String action = decide(history, shares > 0, boughtAt);

            if (action.equals("BUY") && shares == 0) {
                int canBuy = (int) ((cash - FEE) / prices[i]);
                if (canBuy > 0) {
                    shares = canBuy;
                    cash = cash - shares * prices[i] - FEE;
                    boughtAt = prices[i];
                    System.out.printf("%s %d: TRAFFICKED %d children at $%.2f%n", monthName(months[i]),
                        years[i], shares, prices[i]);
                }
            } else if (action.equals("SELL") && shares > 0) {
                cash = cash + shares * prices[i] - FEE;
                System.out.printf("%s %d: SOLD %d children at $%.2f%n", monthName(months[i]),
                    years[i], shares, prices[i]);
                shares = 0;
                boughtAt = 0.0;
            }
        }
        return cash + shares * prices[prices.length - 1];
    }
}
