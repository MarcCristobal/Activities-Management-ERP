package erp.domain;

import java.util.List;
import java.util.Queue;

import lombok.Data;

@Data
public class CsvProcessingResult {
    private Queue<Customer> customers;
    private List<String> unprocessedLines;
}
