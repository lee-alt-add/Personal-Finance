// A simple state management object for user ID and data
const urlParams = new URLSearchParams(window.location.search);
const id = parseInt(urlParams.get('id'), 10);


const appState = {
    userId: id, // Hardcoded user ID for this example
    transactions: [],
    expenses: [],
    income: [],
    balance: 0,
    categorySummary: {},
    trends: {
        months: [],
        income: [],
        expenses: []
    }
};

const BASE_URL = 'http://localhost:8080';

// DOM Elements

// Side Bar Specific 
// Add these new DOM element variables at the top of your app.js
const menuToggle = document.getElementById('menu-toggle');
const body = document.body;

// User Specific
const userProfile = document.getElementById('user-profile');
const dropdownMenu = document.getElementById('dropdown-menu');
const signOutBtn = document.getElementById('sign-out-btn');
const userAvatar = document.getElementById('user-avatar');

// Others
const navLinks = document.querySelectorAll('nav ul li a');
const views = document.querySelectorAll('.view');
const currentBalanceEl = document.getElementById('current-balance');
const transactionsListEl = document.getElementById('transactions-list');
const expensesListEl = document.getElementById('expenses-list');
const incomeListEl = document.getElementById('income-list');
const addExpenseBtn = document.getElementById('add-expense-btn');
const deleteExpenseBtn = document.getElementById("expense-delete-button");
const addIncomeBtn = document.getElementById('add-income-btn');
const incomeDeleteBtn = document.getElementById("income-delete-button");
const modal = document.getElementById('modal');
const modalTitle = document.getElementById('modal-title');
const closeBtn = document.querySelector('.close-btn');
const transactionForm = document.getElementById('transaction-form');
const transactionTypeInput = document.getElementById('transaction-type');
const categoryGroup = document.getElementById('category-group');

// deleteExpenseBtn.addEventListener("click", () => {
//     const selectedItems = expensesListEl.querySelectorAll('input[type="checkbox"]:checked');

//     for (const checkbox of selectedItems) {
//         const response = deleteItem("expense", checkbox);
//         if (!response) {
//             return;
//         }
//     }
// });

// const deleteItem = async (from, item) => {
//     if (from === "expense") {
//         try {
//             const response = await fetch(`users/${item.id}/expenses`);
//             return response.ok;
//         } catch (error) {
//             console.error("Error deleting expenses:", error);
//             // Display a user-friendly error message
//             alert("Failed to delete expense");
//             return false;
//         }
//     } else {
//         console.error("Could not send delete data.")
//     }
// };

deleteExpenseBtn.addEventListener("click", () => {
    const selectedItems = expensesListEl.querySelectorAll('input[type="checkbox"]:checked');
    
    // Check if there are items selected before proceeding
    if (selectedItems.length === 0) {
        alert("Please select at least one item to delete.");
        return;
    }
    
    // Deleting all selected items
    (async () => {
        for (const checkbox of selectedItems) {
            const response = await deleteItem("expense", checkbox.id);
            if (!response) {
                return;
            }
            fetchData();
        }
    })();
    showNotification("All selected expenses have been deleted!");
});

incomeDeleteBtn.addEventListener("click", () => {
    const selectedItems = incomeListEl.querySelectorAll('input[type="checkbox"]:checked');
    
    // Check if there are items selected before proceeding
    if (selectedItems.length === 0) {
        alert("Please select at least one item to delete.");
        return;
    }
    
    // Deleting all selected items
    (async () => {
        for (const checkbox of selectedItems) {
            const response = await deleteItem("income", checkbox.id);
            if (!response) {
                return;
            }
            fetchData();
        }
    })();
    showNotification("All selected expenses have been deleted!");
});

const deleteItem = async (from, itemId) => {
    const apiLink = from === "expense" ?
        `users/${appState.userId}/expenses` :
        `users/${appState.userId}/income`
        ;

    try {
        const response = await fetch(apiLink, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: itemId
            })
        });
        
        if (response.ok) {
            console.log("Expense deleted successfully.");
            return true;
        } else {
            console.error("Failed to delete expense. Server responded with:", response.status);
            // Optionally, parse the error message from the server
            const errorData = await response.json();
            alert(`Failed to delete expense: ${errorData.message || 'Unknown error'}`);
            return false;
        }
    } catch (error) {
        console.error("Error deleting expenses:", error);
        alert("Failed to delete expense. Check your network connection.");
        return false;
    }
};

const showNotification = (message) => {
    const notification = document.getElementById('notification');
    notification.innerHTML = message;
    notification.style.display = "block";

    setTimeout(() => {
        document.getElementById('notification').innerHTML = "";
        notification.style.display = "none";
    }, 5000);
};

// --- Sidebar Toggle Logic ---
menuToggle.addEventListener('click', () => {
    body.classList.toggle('sidebar-open');
});

// Close sidebar when a navigation link is clicked
navLinks.forEach(link => {
    link.addEventListener('click', () => {
        // Your existing navigation logic goes here
        body.classList.remove('sidebar-open');
    });
});

// Chart instances
let trendsChart, categoryChart;

// Helper function to format currency
const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'ZAR'
    }).format(amount);
};

// Function to fetch data from the API
const fetchData = async () => {
    try {
        
        const [
            transactionsRes,
            balanceRes,
            trendsRes,
            categoryRes,
            expensesRes,
            incomeRes
        ] = await Promise.all([
            fetch(`${BASE_URL}/users/${appState.userId}/transactions`),
            fetch(`${BASE_URL}/users/${appState.userId}/balance`),
            fetch(`${BASE_URL}/users/${appState.userId}/summary/trends`),
            fetch(`${BASE_URL}/users/${appState.userId}/summary/categories`),
            fetch(`${BASE_URL}/users/${appState.userId}/expenses`),
            fetch(`${BASE_URL}/users/${appState.userId}/income`)
        ]);

        appState.transactions = await transactionsRes.json();
        appState.balance = await balanceRes.json();
        appState.trends = await trendsRes.json();
        appState.categorySummary = await categoryRes.json();
        appState.expenses = await expensesRes.json();
        appState.income = await incomeRes.json();

        updateUI();
    } catch (error) {
        console.error("Error fetching data:", error);
        // Display a user-friendly error message
        alert("Failed to load data. Please check the server connection.");
    }
};

// Function to update all UI components
const updateUI = () => {
    updateDashboard();
    updateExpenses();
    updateIncome();
};

// Update Dashboard View
const updateDashboard = () => {
    currentBalanceEl.textContent = formatCurrency(appState.balance.amount);
    currentBalanceEl.style.color = appState.balance.amount >= 0 ? 'var(--accent-green)' : 'var(--accent-orange)';
    renderTransactionsList(appState.transactions.slice(0, 5), transactionsListEl, false); 
    renderTrendsChart();
};

// Update Expenses View
const updateExpenses = () => {
    renderTransactionsList(appState.expenses, expensesListEl, true);
    renderCategoryChart();
};

// Update Income View
const updateIncome = () => {
    renderTransactionsList(appState.income, incomeListEl, true);
};

// Render transaction list
const renderTransactionsList = (transactions, listElement, addDelete) => {
    listElement.innerHTML = ''; // Clear the list
    if (transactions.length === 0) {
        listElement.innerHTML = '<li class="transaction-item">No transactions found.</li>';
        return;
    }

    transactions.forEach(t => {
        const li = document.createElement('li');
        li.className = 'transaction-item';
        const isExpense = t.description != null;
        const amountClass = isExpense ? 'amount-expense' : 'amount-income';
        const sign = isExpense ? '-' : '+';
        const descriptor = isExpense ? t.description : t.source;

        if (addDelete) {
            li.innerHTML = `
                <div class="transaction-details">
                    <div class="transaction-description">${descriptor}</div>
                    <div class="transaction-date">
                        <input type="checkbox" id="${t.id}" name="${t.id}">
                        <label for="${t.id}">${new Date(t.date).toLocaleDateString()}</label>
                    </div>
                    </div>
                <div class="transaction-amount ${amountClass}">${sign}${formatCurrency(t.amount)}</div>
            `;
        } 
        else {
            li.innerHTML = `
                <div class="transaction-details">
                    <div class="transaction-description">${descriptor}</div>
                    <div class="transaction-date">${new Date(t.date).toLocaleDateString()}</div>
                </div>
                <div class="transaction-amount ${amountClass}">${sign}${formatCurrency(t.amount)}</div>
            `;
        }
        
        listElement.appendChild(li);
    });  
};

// Render Trends Chart
const renderTrendsChart = () => {
    const ctx = document.getElementById('trends-chart').getContext('2d');
    if (trendsChart) trendsChart.destroy();

    trendsChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: appState.trends.months,
            datasets: [{
                label: 'Income',
                data: appState.trends.income,
                borderColor: 'var(--accent-green)',
                backgroundColor: 'rgba(46, 204, 113, 0.2)',
                tension: 0.4,
                fill: true
            }, {
                label: 'Expenses',
                data: appState.trends.expenses,
                borderColor: 'var(--accent-orange)',
                backgroundColor: 'rgba(255, 107, 107, 0.2)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(255, 255, 255, 0.1)'
                    },
                    ticks: {
                        color: 'var(--text-color)'
                    }
                },
                x: {
                    grid: {
                        color: 'rgba(255, 255, 255, 0.1)'
                    },
                    ticks: {
                        color: 'var(--text-color)'
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        color: 'var(--text-color)'
                    }
                }
            }
        }
    });
};

// Render Category Chart
const renderCategoryChart = () => {
    const ctx = document.getElementById('category-chart').getContext('2d');
    if (categoryChart) categoryChart.destroy();

    const data = {
        labels: Object.keys(appState.categorySummary),
        datasets: [{
            data: Object.values(appState.categorySummary),
            backgroundColor: [
                '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'
            ],
        }]
    };

    categoryChart = new Chart(ctx, {
        type: 'doughnut',
        data: data,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'right',
                    labels: {
                        color: 'var(--text-color)'
                    }
                }
            }
        }
    });
};

// --- Event Listeners and Logic ---

// Navigation logic
navLinks.forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const targetViewId = e.target.getAttribute('href').substring(1) + '-view';

        // Update active class for navigation
        navLinks.forEach(navLink => navLink.parentElement.classList.remove('active'));
        e.target.parentElement.classList.add('active');

        // Show the correct view and hide others
        views.forEach(view => {
            if (view.id === targetViewId) {
                view.classList.add('active');
            } else {
                view.classList.remove('active');
            }
        });
    });
});

// Modal open/close logic
addExpenseBtn.addEventListener('click', () => {
    modalTitle.textContent = 'Add New Expense';
    transactionTypeInput.value = 'expense';
    categoryGroup.style.display = 'block';
    modal.style.display = 'flex';
});

addIncomeBtn.addEventListener('click', () => {
    modalTitle.textContent = 'Add New Income';
    transactionTypeInput.value = 'income';
    categoryGroup.style.display = 'none';
    modal.style.display = 'flex';
});

closeBtn.addEventListener('click', () => {
    modal.style.display = 'none';
    transactionForm.reset();
});

window.addEventListener('click', (e) => {
    if (e.target === modal) {
        modal.style.display = 'none';
        transactionForm.reset();
    }
});

// Handle form submission
transactionForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const type = transactionTypeInput.value;
    const amount = parseFloat(document.getElementById('amount').value);
    const description = document.getElementById('description').value;
    const category = type === 'expense' ? document.getElementById('category').value : null;

    const payload = category === null ? 
    {
        amount,
        source: description,
        date: new Date().toISOString().split('T')[0],
        ...(type === 'expense' && { category })
    } : 
    {
        amount,
        description,
        date: new Date().toISOString().split('T')[0],
        ...(type === 'expense' && { category })
    };

    const endpoint = type === 'expense' ? 'expenses' : 'income';

    try {
        const response = await fetch(`${BASE_URL}/users/${appState.userId}/${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert(`${type.charAt(0).toUpperCase() + type.slice(1)} added successfully!`);
            modal.style.display = 'none';
            transactionForm.reset();
            fetchData();
        } else {
            const errorData = await response.json();
            alert(`Error adding ${type}: ${errorData.message}`);
        }
    } catch (error) {
        console.error(`Error submitting ${type}:`, error);
        alert('An error occurred. Please try again.');
    }
});

// Initial data load
document.addEventListener('DOMContentLoaded', () => {
    fetchData();
});

// ------------------------------------------------------------------------------------

// --- User Profile & Dropdown Logic ---

// Toggle the dropdown menu on avatar click
userProfile.addEventListener('click', (e) => {
    dropdownMenu.classList.toggle('active');
});

// Close the dropdown if the user clicks outside of it
window.addEventListener('click', (e) => {
    if (!userProfile.contains(e.target)) {
        dropdownMenu.classList.remove('active');
    }
});

// Handle the Sign Out action
signOutBtn.addEventListener('click', (e) => {
    e.preventDefault(); // Prevent the default link behavior
    
    // Here you would implement your sign-out logic
    // For a real app, this would be an API call to clear the session/token on the server.
    console.log('User signed out.'); 

    alert('You have been signed out.');
    
    // Redirect to the login page or home page
    window.location.href = '/signin.html'; // Or whatever your login page URL is
});


// fetch(`${BASE_URL}/users/${appState.userId}`)
//     .then(res => res.json())
//     .then(user => {
//         if (user.avatarUrl) {
//             userAvatar.src = user.avatarUrl;
//         }
//     });