package imbanamid.Peer2PeerBooks;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entity class representing a financial transaction.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransactionEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private Double exchangeRate;
    private Double profit;

//    @DateTimeFormat(fallbackPatterns = "MM")
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType = TransactionType.SEPA_INSTANT;

    private Double usdtAmount;

//    public TransactionEntity() {
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//        this.creationDate = LocalDateTime.parse(now.format(formatter), formatter);
//    }

    /**
     * Updates the USDT amount based on the transaction's amount and exchange rate.
     */


    @PrePersist
    @PreUpdate
    private void updateUsdtAmount() {
        if (amount != null && exchangeRate != null) {
            usdtAmount = amount * exchangeRate;
        }


    }
}
