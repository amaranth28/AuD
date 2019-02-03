import java.util.function.Predicate;

public class EqualsPredicate implements Predicate<String> {

    private final String compareString;

    public EqualsPredicate(String compareString) {
        this.compareString = compareString;
    }

    @Override
    public boolean test(String t) {
        return t.equals(compareString);
    }
}
