package erp.domain;

import java.util.Map;
import java.util.Queue;

import lombok.Data;

@Data
public class CsvProcessingResult {
    private Queue<Customer> customers;
    private Map<Integer, String> unprocessedLines;
}
