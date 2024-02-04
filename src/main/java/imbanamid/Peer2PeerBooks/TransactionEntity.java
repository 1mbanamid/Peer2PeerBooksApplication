package imbanamid.Peer2PeerBooks;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;



import javax.persistence.*;
import java.time.LocalDateTime;


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

    @JsonFormat(pattern = "yyy.MM.dd HH:mm")
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType = TransactionType.SEPA_INSTANT;

    private Double usdtAmount;


    /**
     * Updates the USDT amount based on the transaction's amount and exchange rate.
     */


    @PrePersist
    @PreUpdate
    private void updateUsdtAmount() {
        if (amount != null && exchangeRate != null) {
            usdtAmount = amount / exchangeRate * -1;
        }
    }
}
