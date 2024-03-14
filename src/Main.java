import java.util.Scanner;
public class Main {
    public enum TypeOfOperand {
        notANumber,
        Arabian,
        Roman
    }

    public static String[] numbersA = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public static String[] numbersR = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public static void main(String[] args) {
        String row;

        Scanner in = new Scanner(System.in);
        System.out.println("Input:");
        row = in.nextLine();

        System.out.println(Calc(row));
    }

    public static String Calc(String row) {
        row = row.replace(" ", "");
        //разделим строку
        String[] items = row.split("[+\\-*/]", 2);

        try {
            //В выражении точно два операнда и одна операция
            if (items.length != 2) {
                throw new Exception("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
            }

            String num1 = items[0].trim();
            String num2 = items[1].trim();

            //операнды являются числами, попадают в диапазон
            //-1 это не число, 1 - это арабское число, 2 - это римское число
            TypeOfOperand type_num1 = CorrectOperand(num1);
            TypeOfOperand type_num2 = CorrectOperand(num2);

            try {
                //оба операнда числа и одной системы счисления
                if (type_num1 != type_num2) {
                    throw new Exception("Используются одновременно разные системы счисления");
                }

                if (type_num1 == TypeOfOperand.notANumber) {
                    throw new Exception("Строка не является математической операцией.");
                }
                //Вычисляем. Передаем строку, тип числа type_num1 или type_num2, сами числа
                return Calculate(row, type_num1, num1, num2);

            } catch (Exception ex) {
                return ex.getMessage();
            }

        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public static TypeOfOperand CorrectOperand(String operand) {
        //определяем, что число арабское
        for (String i : numbersA) {
            if (i.equals(operand)) {
                return TypeOfOperand.Arabian;
            }
        }
        //иначе определяем, что число римское
        for (String i : numbersR) {
            if (i.equals(operand)) {
                return TypeOfOperand.Roman;
            }
        }
        //это не число
        return TypeOfOperand.notANumber;
    }

    public static String Calculate(String row, TypeOfOperand type, String a, String b) {
        int res = 0;
        //это римские числа, то преобразуем в арабские
        if (type == TypeOfOperand.Roman) {
            a = ConvertRToA(a);
            b = ConvertRToA(b);
        }
        //вычисляем
        char[] charRow = row.toCharArray();
        for (char c : charRow) {
            res = switch (c) {
                case '+' -> Integer.parseInt(a) + Integer.parseInt(b);
                case '-' -> Integer.parseInt(a) - Integer.parseInt(b);
                case '/' -> Integer.parseInt(a) / Integer.parseInt(b);
                case '*' -> Integer.parseInt(a) * Integer.parseInt(b);
                default -> res;
            };
        }
        //это римские числа, то результат преобразуем в римское
        if (type == TypeOfOperand.Roman) {
            try {
                if (res <= 0) {
                    throw new Exception("В римской системе нет отрицательных чисел");
                }
                return ConvertAToR(res);
            } catch (Exception ex) {
                return ex.getMessage();
            }
        }
        //это арабские числа, результат арабское число
        return Integer.toString(res);
    }

    public static String ConvertRToA(String r) {
        String res = "";
        //ищем соответсвие римского числа арабскому
        for (int i = 0; i < numbersR.length; ++i) {
            if (r.equals(numbersR[i])) {
                //нашли, прервем цикл
                res = Integer.toString(i + 1);
                break;
            }
        }
        return res;
    }

    public static String ConvertAToR(int a) {
        StringBuilder s = new StringBuilder();

        if (a == 100) { s = new StringBuilder("C"); }
        else if (a >= 90)
        {
            s = new StringBuilder("XC" + lessThanTen(a - 90));
        }
        else if (a >= 50)
        {
            s = new StringBuilder("L");
            a = a - 50;
            while (a > 10)
            {
                s.append("X");
                a -= 10;
            }
            s.append(lessThanTen(a));
        }
        else if (a >= 40)
        {
            s = new StringBuilder("XL" + lessThanTen(a - 40));
        }
        else if (a > 10)
        {
            while (a > 10)
            {
                s.append("X");
                a -= 10;
            }
            s.append(lessThanTen(a));
        }
        else
        {
            s = new StringBuilder(lessThanTen(a));
        }

        return s.toString();
    }

    public static String lessThanTen(int a) {
        String res = "";

        for (int i = 0; i < numbersR.length; ++i) {
            if ((i + 1) == a) {
                //нашли, прервем цикл
                res = numbersR[i];
                break;
            }
        }
        return res;
    }
}