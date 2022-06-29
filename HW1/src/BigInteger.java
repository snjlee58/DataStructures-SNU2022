import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");
    private int[] numArray;
    private char sign;
    private int len;

    public BigInteger(){

    }

    public BigInteger(int[] numArray){
        // insert digits into numArray right justified
        this.len = numArray.length;
        this.numArray = new int[this.len];

        for (int i=0; i<this.len; i++){
            this.numArray[i] = numArray[i];
        }
    }

    public BigInteger(int[] numArray, char sign) {
        this(numArray);
        this.sign = sign;
    }

    public void setSign(char sign){
        this.sign = sign;
    }

    public void setNumArray(int[] numArray){ this.numArray = numArray; }

    public String getAbsoluteValue(){
        String numString = "";
        int idx = 0;

        // skip over all zeroes
        for (int i=0; i<this.len; i++){
            if (this.numArray[i] != 0){
                idx = i;
                break;
            }
            idx++;
        }

        if (idx == this.len){
            numString = "0";
        } else{
            for (int i = idx; i < this.len; i++){
                numString = numString.concat(Integer.toString(this.numArray[i]));
            }
        }

        return numString;
    }

    public boolean compareAbsoluteValues(BigInteger num2){
        int num1length = this.getAbsoluteValue().length();
        int num2length = num2.getAbsoluteValue().length();

        if (num1length == num2length) // if number of digits is the same, compare digits from leftmost digit to rightmost digit
        {
            int compareResult = this.getAbsoluteValue().compareTo(num2.getAbsoluteValue());

            if (compareResult >= 0){
                return true;
            } else {
                return false;
            }
        }
        else if (num1length >= num2length) // if num1 has more digits, it is larger
        {
            return true;
        } else // if num2 has more digits, it is larger
        {
            return false;
        }
    }

    public int[] addLeadingZeroes(int[] numArray, int leadingZeroes){
        int[] newArray = new int[numArray.length + leadingZeroes];
        for (int i=0; i<leadingZeroes; i++){
            newArray[i] = 0;
        }
        for (int i=0; i< numArray.length; i++){
            newArray[leadingZeroes+i] = numArray[i];
        }

        return newArray;
    }

    public BigInteger add(BigInteger num2) {
        BigInteger bigInt1 = this;
        BigInteger bigInt2 = num2;

        // number of digits of result should be 1 greater than the longest operand
        int resultLength;
        if (bigInt1.len >= bigInt2.len){
            resultLength = bigInt1.len + 1;
            bigInt2.setNumArray(addLeadingZeroes(num2.numArray, bigInt1.len - bigInt2.len));
        } else{
            resultLength = bigInt2.len + 1;
            bigInt1.setNumArray(addLeadingZeroes(this.numArray, bigInt2.len - bigInt1.len));
        }

        int[] result = new int[resultLength];

        result[resultLength-1] = bigInt1.numArray[resultLength-2] + bigInt2.numArray[resultLength-2];
        int carry = result[resultLength-1] / 10;
        result[resultLength-1] = result[resultLength-1] % 10;

        for (int i = resultLength-2; i>0; i--){
            if (carry > 0) {
                result[i] = bigInt1.numArray[i-1] + bigInt2.numArray[i-1] + carry;
            }
            else{
                result[i] = bigInt1.numArray[i-1] + bigInt2.numArray[i-1];
            }
            carry = result[i] / 10;
            result[i] = result[i] % 10;
        }

        // leftmost digit
        if (carry > 0){
            result[0] = carry;
        } else{
            int[] resultNoEndCarry = new int[resultLength-1];
            for (int i=1; i<resultLength; i++){
                resultNoEndCarry[i-1] = result[i];
            }
            result = resultNoEndCarry;
        }

        BigInteger resultBigInt = new BigInteger(result);

        // determine sign of result
        if (this.sign == '+'){
            resultBigInt.setSign('+');
        } else {
            resultBigInt.setSign('-');
        }

        return resultBigInt;
    }

    public BigInteger subtract(BigInteger num2) {
        BigInteger biggerInt;
        BigInteger smallerInt;

        if (this.compareAbsoluteValues(num2)) {
            biggerInt = this;
            smallerInt = num2;
        } else{
            biggerInt = num2;
            smallerInt = this;
        }

        smallerInt.setNumArray(addLeadingZeroes(smallerInt.numArray, biggerInt.len - smallerInt.len));
        int resultLength = biggerInt.numArray.length;
        int[] result = new int[resultLength];

        for (int i = resultLength-1; i>=0; i--){
            if (biggerInt.numArray[i] >= smallerInt.numArray[i]){
                result[i] = biggerInt.numArray[i] - smallerInt.numArray[i];
            } else{
                // borrow from left digit
                biggerInt.numArray[i-1] -= 1;
                biggerInt.numArray[i] += 10;
                result[i] = biggerInt.numArray[i] - smallerInt.numArray[i];
            }
        }

        char resultSign = ' ';

        // compare absolute value of the two numbers
        if (this.getAbsoluteValue().equals(biggerInt.getAbsoluteValue())){
            resultSign = '+';
        }else if (num2.getAbsoluteValue().equals(biggerInt.getAbsoluteValue())){
            resultSign = '-';
        }
        BigInteger resultBigInt = new BigInteger(result, resultSign);
        return resultBigInt;
    }

    public BigInteger multiply(BigInteger num2) {
        BigInteger bigInt1 = this;
        BigInteger bigInt2 = num2;

        int[] result = new int[bigInt1.len + bigInt2.len];

        for (int n = bigInt2.len - 1; n >= 0; n--){
            for (int m = bigInt1.len - 1; m >= 0; m--){
                int multiply = bigInt1.numArray[m] * bigInt2.numArray[n];
                int sumWithRHS = multiply + result[m+n+1];

                result[m+n] += sumWithRHS / 10;
                result[m+n+1] = sumWithRHS % 10;
            }
        }

        BigInteger resultBigInt = new BigInteger(result);


        if (this.sign == num2.sign){
            resultBigInt.setSign('+');
        } else {
            resultBigInt.setSign('-');
        }

        return resultBigInt;

    }


    @Override
    public String toString() {
        String numString = getAbsoluteValue();

        if (numString.equals("0")){
            return numString;
        } else if (this.sign == '-'){
            return this.sign + numString;
        } else{
            return numString;
        }
    }

    static int[] stringToDigitArray(String stringNumber){
        // exclude negative sign
        if (stringNumber.charAt(0) == '-'){
            stringNumber = stringNumber.substring(1, stringNumber.length());
        }

        char[] charArray = stringNumber.toCharArray();

        int[] intArray = new int[charArray.length];
        for (int i=0; i< charArray.length; i++){
            intArray[i] = Character. getNumericValue(charArray[i]);
        }
        return intArray;
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException{
        char num1sign;
        String num1 = "";
        char num2sign;
        String num2 = "";
        char operator;

        input = input.trim(); // trim leading and trailing zeroes

        // extract num1 sign
        if (input.charAt(0) == '-' || input.charAt(0) == '+'){
            num1sign = input.charAt(0);
            input = input.substring(1, input.length());
        } else{
            num1sign = '+';
        }
        input = input.trim();

        // extract num1 absolute value
        int currIdx= 0;
        while (Character.isDigit(input.charAt(currIdx))){
            num1 += input.charAt(currIdx);
            currIdx++;
        }

        input = input.substring(currIdx, input.length());
        input = input.trim();

        // extract operator
        operator = input.charAt(0);

        input = input.substring(1, input.length());
        input = input.trim();

        if (input.charAt(0) == '-' || input.charAt(0) == '+'){
            num2sign = input.charAt(0);
            input = input.substring(1, input.length());
        } else{
            num2sign = '+';
        }
        input = input.trim();

        // extract num1 absolute value
        num2 = input;

        BigInteger bigInt1 = new BigInteger(stringToDigitArray(num1), num1sign);
        BigInteger bigInt2 = new BigInteger(stringToDigitArray(num2), num2sign);

        // perform operation
        BigInteger result = new BigInteger();
        if (operator == '+'){
            if ((num1sign == '+' && num2sign == '+') || (num1sign == '-' && num2sign == '-')) {
                result = bigInt1.add(bigInt2);
            } else if (num1sign == '+' && num2sign == '-'){
                result = bigInt1.subtract(bigInt2);
            } else if (num1sign == '-' && num2sign == '+'){
                result = bigInt2.subtract(bigInt1);
            }
        }

        else if (operator == '-'){
            if ((num1sign == '+' && num2sign == '-') || (num1sign == '-' && num2sign == '+')) {
                result = bigInt1.add(bigInt2);
            } else if (num1sign == '+' && num2sign == '+'){
                result = bigInt1.subtract(bigInt2);
            } else if (num1sign == '-' && num2sign == '-'){
                result = bigInt2.subtract(bigInt1);
            }
        }

        else if (operator == '*') {
            result = bigInt1.multiply(bigInt2);
        }

        return result;
    }
  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
