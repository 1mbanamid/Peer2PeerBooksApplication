package imbanamid.Peer2PeerBooks;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfitCalculatorService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Рассчитывает прибыль для всех транзакций и сохраняет результат в базе данных.
     */
    @Transactional
    public void calculateAndSaveProfits() {
        // Удаление предыдущих значений прибыли перед каждым расчетом
        clearPreviousProfits();

        List<TransactionEntity> transactions = transactionRepository.findAll();

        for (TransactionEntity transaction : transactions) {
            double currentAmount = transaction.getAmount();
            double currentRate = transaction.getExchangeRate();

            if (currentAmount < 0) {
                transaction.setProfit(0.0);
            } else {
                double profit = calculatePositiveProfit(currentAmount, currentRate, transactions);
                profit = roundToThousands(profit);
                transaction.setProfit(profit);
            }
        }
    }

    /**
     * Рассчитывает положительную прибыль для текущей транзакции.
     *
     * @param currentAmount Текущая сумма транзакции.
     * @param currentRate   Текущая ставка транзакции.
     * @param transactions  Список всех транзакций.
     * @return Рассчитанная прибыль.
     */
    private double calculatePositiveProfit(double currentAmount, double currentRate, List<TransactionEntity> transactions) {
        double profit = 0.0;

        for (TransactionEntity negativeTransaction : transactions) {
            if (negativeTransaction.getAmount() < 0) {
                double minAmount = Math.min(currentAmount, -negativeTransaction.getAmount());

                profit += minAmount * currentRate - minAmount * negativeTransaction.getExchangeRate();
            }
        }

        return roundToThousands(profit);
    }

    /**
     * Очищает предыдущие значения прибыли в базе данных.
     */
    private void clearPreviousProfits() {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        for (TransactionEntity transaction : transactions) {
            transaction.setProfit(0.0);
        }
    }

    /**
     * Округляет значение до трех десятичных знаков.
     *
     * @param value Значение для округления.
     * @return Округленное значение.
     */
    private double roundToThousands(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}
