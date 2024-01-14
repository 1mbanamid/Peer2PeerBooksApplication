package imbanamid.Peer2PeerBooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final ProfitCalculatorService profitCalculatorService;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository, ProfitCalculatorService profitCalculatorService) {
        this.transactionRepository = transactionRepository;
        this.profitCalculatorService = profitCalculatorService;
    }

    // Контроллер для сохранения новой транзакции
    @PostMapping("/save")
    public ResponseEntity<String> saveTransaction(@RequestBody TransactionEntity transaction) {
        transactionRepository.save(transaction);
        return new ResponseEntity<>("Transaction saved successfully", HttpStatus.CREATED);
    }

    // Контроллер для удаления транзакции по идентификатору
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
        return new ResponseEntity<>("Transaction deleted successfully", HttpStatus.OK);
    }

    // Контроллер для вызова ProfitCalculatorService
    @GetMapping("/calculate-profits")
    public ResponseEntity<String> calculateProfits() {
        profitCalculatorService.calculateAndSaveProfits();
        return new ResponseEntity<>("Profits calculated and saved successfully", HttpStatus.OK);
    }

    // Получение списка всех транзакций
    @GetMapping("/all")
    public ResponseEntity<List<TransactionEntity>> getAllTransactions() {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
