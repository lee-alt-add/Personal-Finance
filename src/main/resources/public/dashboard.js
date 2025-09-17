document.addEventListener('DOMContentLoaded', function() {
    const primaryText = '#E0E0E0';
    const secondaryText = '#A0A0A0';
    const accentGreen = '#26A69A';
    const accentRed = '#EF5350';
    const accentBlue = '#42A5F5';
    const gridColor = '#2F2F2F';

    // 1. Trends Chart (Line)
    const trendsCtx = document.getElementById('trendsChart').getContext('2d');
    new Chart(trendsCtx, {
        type: 'line',
        data: {
            labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun', 'Year'],
            datasets: [{
                label: 'Income',
                data: [100, 150, 200, 180, 250, 220, 300, 280],
                borderColor: accentGreen,
                tension: 0.4,
                fill: false
            }, {
                label: 'Expenses',
                data: [80, 120, 150, 130, 190, 160, 240, 210],
                borderColor: accentRed,
                tension: 0.4,
                fill: false
            }]
        },

        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { labels: { color: primaryText } } },
            scales: {
                y: { ticks: { color: secondaryText }, grid: { color: gridColor } },
                x: { ticks: { color: secondaryText }, grid: { color: 'transparent' } }
            }
        }
    });

    // 2. Investments Chart (Line)
    const investmentsCtx = document.getElementById('investmentsChart').getContext('2d');
    new Chart(investmentsCtx, {
        type: 'line',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug'],
            datasets: [{
                label: 'Stock A',
                data: [200, 220, 210, 250, 280, 310, 300, 320],
                borderColor: accentBlue,
                tension: 0.4,
            }, {
                label: 'Stock B',
                data: [150, 180, 160, 190, 210, 240, 220, 250],
                borderColor: accentGreen,
                tension: 0.4,
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
                y: { ticks: { color: secondaryText }, grid: { color: gridColor } },
                x: { ticks: { color: secondaryText }, grid: { color: 'transparent' } }
            }
        }
    });

    // 3. Spending Chart (Doughnut)
    const spendingCtx = document.getElementById('spendingChart').getContext('2d');
    new Chart(spendingCtx, {
        type: 'doughnut',
        data: {
            labels: ['Housing', 'Transportation', 'Food', 'Entertainment'],
            datasets: [{
                data: [45, 25, 20, 10],
                backgroundColor: [accentGreen, accentBlue, accentRed, '#FFCA28'],
                borderWidth: 0,
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '70%',
            plugins: { legend: { display: false } }
        }
    });
});

