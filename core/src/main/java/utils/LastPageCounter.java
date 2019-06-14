package utils;

public class LastPageCounter {
    public static boolean isLastPage(int pageNumber, int pageSize, int recordsCount){
        if (recordsCount%pageSize == 0){
            System.out.println("recordsCount/pageSize = " + recordsCount/pageSize);
            return (recordsCount/pageSize == pageNumber);
        }
        System.out.println("(recordsCount/pageSize + 1" + (recordsCount/pageSize + 1));
        return ((recordsCount/pageSize + 1) == pageNumber);
    }
}
