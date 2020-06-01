public class Polindrome {
    public static void main(String[] args) {
        for(int i = 0; i < args.length; i++) { /*перебор аргумента для функции из строчки, вводимой с терминала*/
            String s = args[i];
            if (isPalindrome(s)) {
                System.out.println(s + " ololo"); /*вывод аргумента и текста ololo через пробел, если это полиндром*/
            } else{
                System.out.println(s + " no ololo"); /*если не полиндром*/
            }
        }
    }

    public static String reverseString(String s) {
        String out = "";
        for (int i = s.length() - 1; i >= 0; i--) { /*метод *.length считает общее кол-во символов*/
            out += s.charAt(i); /* *.charAt выводит символ в строке с номером i */
        }
        return out;
    }

    public static boolean isPalindrome(String s) {
        return (s.equals(reverseString(s))); /*этот метод сравнивает переменную s и тем, что вышло на выходе reverseString(s)*/
    }
}