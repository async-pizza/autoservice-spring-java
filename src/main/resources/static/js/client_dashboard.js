function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    if (evt) {
        evt.currentTarget.className += " active";
    } else {
        // Find the tablink button and add "active" class
        var btn = document.querySelector('.tablinks[onclick*="' + tabName + '"]');
        if (btn) {
            btn.className += " active";
        }
    }
}

// Fetch and display cars
async function fetchAndDisplayCars() {
    const response = await fetch('/api/cars', {
        method: 'GET',
        credentials: 'include' // Include cookies
    });
    if (response.ok) {
        const cars = await response.json();
        const carsContainer = document.getElementById('carsContainer');
        carsContainer.innerHTML = '';
        if (cars.length === 0) {
            carsContainer.innerHTML = '<p>No cars found.</p>';
        } else {
            cars.forEach(car => {
                const carCard = document.createElement('div');
                carCard.className = 'card';
                carCard.innerHTML = `
                    <h3>${car.brand} ${car.model}</h3>
                    <p>Year: ${car.year}</p>
                    <p>License Plate: ${car.licensePlate}</p>
                `;
                carsContainer.appendChild(carCard);
            });
        }
    } else {
        const message = await response.text();
        alert(message);
    }
}

// Fetch and display orders
async function fetchAndDisplayOrders() {
    const response = await fetch('/api/orders', {
        method: 'GET',
        credentials: 'include' // Include cookies
    });
    if (response.ok) {
        const orders = await response.json();
        const ordersContainer = document.getElementById('ordersContainer');
        ordersContainer.innerHTML = '';
        if (orders.length === 0) {
            ordersContainer.innerHTML = '<p>No orders found.</p>';
        } else {
            orders.forEach(order => {
                const orderCard = document.createElement('div');
                orderCard.className = 'card';
                orderCard.innerHTML = `
                    <h3>Order</h3>
                    <p>Status: ${order.status}</p>
                    <p>Created: ${order.creationDate}</p>
                    <p>Completed: ${order.completionDate || 'N/A'}</p>
                    <p>Car: ${order.car.brand} ${order.car.model}</p>
                    <h4>Services:</h4>
                        <ul>
                        ${order.services.map(service => `<li>${service.service.name} x${service.quantity}</li>`).join('')}
                        </ul>
                `;
                ordersContainer.appendChild(orderCard);
            });
        }
    } else {
        const message = await response.text();
        alert(message);
    }
}
// Function to add a new car
async function addCar() {
    const brand = document.getElementById('brand').value;
    const model = document.getElementById('model').value;
    const year = document.getElementById('year').value;
    const licensePlate = document.getElementById('licensePlate').value;

    const carData = {
        brand: brand,
        model: model,
        year: year,
        licensePlate: licensePlate
    };

    const response = await fetch('/api/cars', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(carData),
        credentials: 'include' // Include cookies
    });

    if (response.ok) {
        alert('Car added successfully!');
        // Clear form inputs
        document.getElementById('brand').value = '';
        document.getElementById('model').value = '';
        document.getElementById('year').value = '';
        document.getElementById('licensePlate').value = '';
        // Refresh car list
        fetchAndDisplayCars();
    } else {
        const message = await response.text();
        alert(message);
    }
}

// Attach form submission handler
document.getElementById('addCarForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    await addCar();
});


// Fetch and display mechanics
async function fetchMechanics() {
    const response = await fetch('/api/auth/users?role=MECHANIC', {
        method: 'GET',
        credentials: 'include'
    });
    if (response.ok) {
        const mechanics = await response.json();
        const mechanicSelect = document.getElementById('mechanicSelect');
        mechanics.forEach(mechanic => {
            const option = document.createElement('option');
            option.value = mechanic.id;
            option.textContent = mechanic.name;
            mechanicSelect.appendChild(option);
        });
    } else {
        alert('Failed to fetch mechanics.');
    }
}

// Fetch and display services
async function fetchServices() {
    const response = await fetch('/api/services', {
        method: 'GET',
        credentials: 'include'
    });
    if (response.ok) {
        const services = await response.json();
        const serviceSelect = document.getElementById('serviceSelect');
        services.forEach(service => {
            const option = document.createElement('option');
            option.value = service.id;
            option.textContent = service.name;
            serviceSelect.appendChild(option);
        });
    } else {
        alert('Failed to fetch services.');
    }
}

async function fetchClientCars() {
    const response = await fetch('/api/cars', {
        method: 'GET',
        credentials: 'include' // Include cookies
    });
    if (response.ok) {
        const cars = await response.json();
        const carSelect = document.getElementById('carSelect');
        cars.forEach(car => {
            const option = document.createElement('option');
            option.value = car.id;
            option.textContent = `${car.brand} ${car.model} (${car.licensePlate})`;
            carSelect.appendChild(option);
        });
    } else {
        const message = await response.text();
        alert(`Failed to fetch cars: ${message}`);
    }
}

// Add service to the list
function addService() {
    const serviceSelect = document.getElementById('serviceSelect');
    const quantityInput = document.getElementById('quantityInput');
    const serviceId = serviceSelect.value;
    const quantity = quantityInput.value;

    if (serviceId === "" || quantity === "") {
        alert("Please select a service and enter quantity.");
        return;
    }

    const listItem = document.createElement('li');
    listItem.textContent = `Service ID: ${serviceId}, Quantity: ${quantity}`;
    document.getElementById('servicesList').appendChild(listItem);

    // Store the service request
    if (!orderServices) {
        orderServices = [];
    }
    orderServices.push({ serviceId: serviceId, quantity: quantity });

    // Clear the input fields
    serviceSelect.selectedIndex = 0;
    quantityInput.value = "";
}

// Submit order creation
document.getElementById('createOrderForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const carSelect = document.getElementById('carSelect');
    const mechanicSelect = document.getElementById('mechanicSelect');
    const status = "IN_PROGRESS"; // or set as needed

    if (!orderServices || orderServices.length === 0) {
        alert("Please add at least one service.");
        return;
    }

    const orderData = {
        mechanicId: mechanicSelect.value,
        carId: carSelect.value,
        status: status,
        services: orderServices
    };

    const response = await fetch('/api/orders', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderData),
        credentials: 'include'
    });

    if (response.ok) {
        alert('Order created successfully!');
        // Clear form inputs and list
        carSelect.selectedIndex = 0;
        mechanicSelect.selectedIndex = 0;
        document.getElementById('servicesList').innerHTML = "";
        orderServices = [];
    } else {
        const message = await response.text();
        alert(message);
    }
});

let orderServices = [];

(async () => {
    // Fetch current user
    const userResponse = await fetch('/api/auth/user', {
        method: 'GET',
        credentials: 'include'
    });
    const user = await userResponse.json();
    if (!user || user.role !== 'CLIENT') {
        alert('You are not authorized to view this page.');
        window.location.href = 'login.html';
        return;
    }

    // Fetch and display cars
    fetchAndDisplayCars();

    fetchAndDisplayOrders();

    fetchClientCars();

    // Fetch and display mechanics
    fetchMechanics();

    // Fetch and display services
    fetchServices();

    // Default to Cars tab
    openTab(event, 'Cars');
})();