package plugin.example;

import java.lang.Object;

public final class JavaLibrary {

    private JavaLibrary() {
        throw new UnsupportedOperationException();
    }

    public static String process(Object ignored) {
        return "a result";
    }

}
