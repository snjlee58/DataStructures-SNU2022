public class StringForHash implements Comparable<StringForHash> {
    String string;
    static final int substringLength = 6;

    public StringForHash(String string) {
        this.string = string;
    }

    @Override
    public int hashCode() {
        int sumOfASCII = 0;
        for (int i = 0; i < substringLength; i++) {
            sumOfASCII += string.charAt(i);
        }
        return sumOfASCII;
    }

    @Override
    public int compareTo(StringForHash o) {
        return string.compareTo(o.string);
    }

    @Override
    public String toString(){
        return string;
    }
}
