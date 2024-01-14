document.addEventListener("DOMContentLoaded", function () {
    fetchTransactions();
});

function fetchTransactions() {
    fetch('/transactions/all')
        .then(response => response.json())
        .then(transactions => {
            renderTransactions(transactions);
        });
}

function renderTransactions(transactions) {
    const table = document.getElementById('transactionTable');
    // Clear existing rows
    table.innerHTML = "<tr><th>Time</th><th>Amount</th><th>Exchange Rate</th><th>Transaction Type</th><th>Profit</th><th>Delete</th></tr>";

    transactions.forEach(transaction => {
        const row = table.insertRow(-1);
        const cell1 = row.insertCell(0);
        const cell2 = row.insertCell(1);
        const cell3 = row.insertCell(2);
        const cell4 = row.insertCell(3);
        const cell5 = row.insertCell(4);
        const cell6 = row.insertCell(5);

        cell1.innerHTML = transaction.creationDate;
        cell2.innerHTML = transaction.amount;
        cell3.innerHTML = transaction.exchangeRate;
        cell4.innerHTML = transaction.transactionType;
        cell5.innerHTML = transaction.profit;
        cell6.innerHTML = `<button onclick="deleteTransaction(${transaction.id})">Delete</button>`;
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
        .then(data => {
            alert(data);
            fetchTransactions();
        });
}

function deleteTransaction(id) {
    fetch(`/transactions/delete/${id}`, {
        method: 'DELETE',
    })

        .then(response => response.text())
        .then(data => {
            alert(data);
            fetchTransactions();
        });
}

function calculateProfits() {
    fetch('/transactions/calculate-profits', {
        method: 'GET',
    })
        .then(response => response.text())
        .then(data => {
            alert(data);
            fetchTransactions();
        });
}
