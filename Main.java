import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean devam = true;

        System.out.println("Hesap makinesine hos geldiniz!");
        while (devam) {
            System.out.println("islemi girin (ornek: 2*2):");

            String veri = scanner.nextLine();
            
            while (!sthata(veri)) {
                System.out.println("Gecerli bir matematiksel ifade giriniz:");
                veri = scanner.nextLine();
            }

            double result = parantez(veri);
            System.out.println("Sonuc: " + result);

            System.out.println("Devam etmek istiyor musunuz? (evet/hayir):");
            String devamCevabi = scanner.nextLine();
            devam = devamCevabi.equalsIgnoreCase("evet");
        }

        System.out.println("Program kapatiliyor.");

        scanner.close();
    }
    
    public static boolean sthata(String input) {
        return input.matches("[0-9+\\-*/(). ]+");
    }

    public static double parantez(String input) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (Character.isDigit(ch)) {  
                StringBuilder numStr = new StringBuilder();
                while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                    numStr.append(input.charAt(i));
                    i++;
                }
                i--;
                double num = Double.parseDouble(numStr.toString());
                numbers.push(num);
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    double result = applyOperation(operators.pop(), numbers.pop(), numbers.pop());
                    numbers.push(result);
                }
                operators.pop(); 
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && hasHigherPrecedence(ch, operators.peek())) {
                    double result = applyOperation(operators.pop(), numbers.pop(), numbers.pop());
                    numbers.push(result);
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            double result = applyOperation(operators.pop(), numbers.pop(), numbers.pop());
            numbers.push(result);
        }

        return numbers.pop();
    }

    public static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%';
    }

    public static boolean hasHigherPrecedence(char op1, char op2) {
        if ((op2 == '*' || op2 == '/' || op2 == '%') && (op1 == '+' || op1 == '-')) {
            return true;
        }
        return false;
    }

    public static double applyOperation(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Bölme sıfıra bölünemez.");
                }
                return a / b;
            case '%':
                return a % b;
            default:
                throw new IllegalArgumentException("Geçersiz işlem: " + operator);
        }
    }
}
