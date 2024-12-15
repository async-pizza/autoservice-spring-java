document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;

    const csrfToken = getCookie('XSRF-TOKEN');

    fetch('/api/auth/register', {
        method: 'POST',    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest) {
        User user = new User(null, registrationRequest.name(), registrationRequest.email(), registrationRequest.password(), registrationRequest.role());
        userService.register(user);
        return ResponseEntity.ok("User registered successfully!");
    }
        headers: {
            'Content-Type': 'application/json',
            'X-XSRF-TOKEN': csrfToken
        },
        body: JSON.stringify({ name: name, email: email, password: password, role: role })
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/login.html'; // Redirect to login page
        } else {
            return response.json();
        }
    })
    .then(data => {
        if (data.error) {
            document.getElementById('error-message').innerText = data.error;
        }
    })
    .catch(error => console.error('Error:', error));

    function getCookie(name) {
        let cookieValue = null;
        if (document.cookie && document.cookie !== '') {
            const cookies = document.cookie.split(';');
            for (let i = 0; i < cookies.length; i++) {
                const cookie = cookies[i].trim();
                if (cookie.substring(0, name.length + 1) === (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
});