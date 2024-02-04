document.addEventListener("DOMContentLoaded", function () {
    fetchTransactions();
    const app = document.getElementById('app');
    const toggleDarkModeButton = document.getElementById('toggleDarkMode');

    // Check the user's preference for dark mode
    const isDarkMode = localStorage.getItem('darkMode') === 'enabled';

    // Set initial mode based on user preference
    if (isDarkMode) {
        app.classList.add('dark-mode');
    }
});


function fetchTransactions() {
    fetch('/transactions/all')
        .then(response => response.json())
        .then(transactions => {
            renderTransactions(transactions);
        updateTransactionSums(transactions);
        });
}

function updateTransactionSums(transactions) {
    let totalUsdtAmount = 0;
    let totalAmount = 0;
    let sepaInstantRemaining = 5000;
    const today = new Date().setHours(0, 0, 0, 0);

    // Расчет сумм и остатка SEPA INSTANT
    transactions.forEach(transaction => {
        const transactionDate = new Date(transaction.creationDate).setHours(0, 0, 0, 0);

        totalUsdtAmount += parseFloat(transaction.usdtAmount);
        totalAmount += parseFloat(transaction.amount);

        // Расчет остатка для SEPA INSTANT
        if (transaction.transactionType === "SEPA_INSTANT" && transactionDate === today) {
            if (parseFloat(transaction.amount) < 0) {
                sepaInstantRemaining += parseFloat(transaction.amount); // Отнимаем, если транзакция отрицательная
            }
        }
    });

    // Обновление HTML
    document.getElementById('sepaInstantRemaining').innerText = `SEPA INSTANT: ${sepaInstantRemaining.toFixed(2)}`;
    document.getElementById('totalUsdtAmount').innerText = `USDT: ${totalUsdtAmount.toFixed(2)}`;
    document.getElementById('totalAmount').innerText = `EURO: ${totalAmount.toFixed(2)}`;
}

function renderTransactions(transactions) {
    const table = document.getElementById('transactionTable');
    // Clear existing rows
    table.innerHTML = "<tr><th>Time</th><th>Amount</th><th>Exchange Rate</th><th>Transaction Type</th><th>Profit</th><th>USDT Amount</th><th>Percent</th><th>Delete</th></tr>"; // Добавлен новый заголовок столбца для процента

    // Reverse the order of transactions to display the newest first
    transactions.reverse();

    transactions.forEach(transaction => {
        const row = table.insertRow(-1);
        const cell1 = row.insertCell(0);
        const cell2 = row.insertCell(1);
        const cell3 = row.insertCell(2);
        const cell4 = row.insertCell(3);
        const cell5 = row.insertCell(4);
        const cell6 = row.insertCell(5);
        const cell7 = row.insertCell(6);
        const cell8 = row.insertCell(7);

        const profitPercent = (transaction.amount && transaction.profit) ? (transaction.profit / transaction.amount * 100) : 0; // Проверка на нулевой Amount
        if (transaction.amount < 0) {
            cell2.classList.add('negative');
        }

        cell1.innerHTML = transaction.creationDate;
        cell2.innerHTML = transaction.amount;
        cell3.innerHTML = transaction.exchangeRate;
        cell4.innerHTML = transaction.transactionType;
        cell5.innerHTML = transaction.profit;
        cell6.innerHTML = transaction.usdtAmount; // Display usdtAmount
        cell7.innerHTML = `${profitPercent.toFixed(2)}%`;
        cell8.innerHTML = `<button onclick="deleteTransaction(${transaction.id})">Delete</button>`;

    });
}

function saveTransaction() {
    const amount = document.getElementById('amount').value;
    const exchangeRate = document.getElementById('exchangeRate').value;
    const transactionType = document.getElementById('transactionType').value;

    fetch('/transactions/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            amount: amount,
            exchangeRate: exchangeRate,
            transactionType: transactionType,
        }),
    })
        .then(response => response.text())
        .then(() => {
            fetchTransactions();
            calculateProfits();
        });
}

function deleteTransaction(id) {
    fetch(`/transactions/delete/${id}`, {
        method: 'DELETE',
    })
        .then(response => response.text())
        .then(() => {
            calculateProfits();
            fetchTransactions();
        });
}

function calculateProfits() {
    fetch('/transactions/calculate-profits', {
        method: 'GET',
    })
        .then(response => response.text())
        .then(() => {
            fetchTransactions();
        });
}
