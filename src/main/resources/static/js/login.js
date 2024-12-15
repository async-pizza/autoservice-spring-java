document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const loginResponse = await fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password }),
        credentials: 'include' // Include cookies
    });

    if (loginResponse.ok) {
        // Fetch current user to get role
        const userResponse = await fetch('/api/auth/user', {
            method: 'GET',
            credentials: 'include' // Include cookies
        });
        const user = await userResponse.json();
        if (user && user.role) {
            if (user.role === 'CLIENT') {
                window.location.href = 'client_dashboard.html';
            } else if (user.role === 'MECHANIC') {
                window.location.href = 'mechanic_dashboard.html';
            }
        } else {
            alert('User not found.');
            window.location.href = 'login.html';
        }
    } else {
        const message = await loginResponse.text();
        document.getElementById('message').innerText = message;
    }
});