package fi.aalto.ekanban;

public class AcceptanceTestUtil {

    public static Integer getIndexInt(String stringIndex) {
        Integer intIndex;
        switch (stringIndex) {
            case "first": intIndex = 0;
                break;
            case "second": intIndex = 1;
                break;
            default: intIndex = 2;
                break;
        }
        return intIndex;
    }

}
