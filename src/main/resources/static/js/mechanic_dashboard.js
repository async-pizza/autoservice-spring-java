async function getCurrentUser() {
    const response = await fetch('/api/auth/user', {
        method: 'GET',
        credentials: 'include'
    });
    const user = await response.json();
    return user;
}

async function fetchAndDisplayOrders() {
    const user = await getCurrentUser();
    if (!user || user.role !== 'MECHANIC') return;

    const response = await fetch(`/api/orders`, {
        method: 'GET',
        credentials: 'include'
    });
    if (response.ok) {
        const orders = await response.json();
        const ordersContainer = document.getElementById('ordersContainer');
        ordersContainer.innerHTML = '';
        if (orders.length === 0) {
            ordersContainer.innerHTML = '<p>No orders assigned.</p>';
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
                        ${order.services.map(service => `<li>${service.name} x${service.quantity}</li>`).join('')}
                        </ul>
                    <button onclick="openUpdateModal(${order.id})">Update Order</button>
                `;
                ordersContainer.appendChild(orderCard);
            });
        }
    } else {
        alert('Failed to fetch orders.');
    }
}

async function fetchAndDisplayServices() {
    const response = await fetch('/api/services', {
        method: 'GET',
        credentials: 'include'
    });
    if (response.ok) {
        const services = await response.json();
        const servicesContainer = document.getElementById('servicesContainer');
        servicesContainer.innerHTML = '';
        if (services.length === 0) {
            servicesContainer.innerHTML = '<p>No services available.</p>';
        } else {
            services.forEach(service => {
                const serviceCard = document.createElement('div');
                serviceCard.className = 'card';
                serviceCard.innerHTML = `
                    <h3>${service.name}</h3>
                    <p>Description: ${service.description}</p>
                    <p>Cost: $${service.cost}</p>
                `;
                servicesContainer.appendChild(serviceCard);
            });
        }
    } else {
        alert('Failed to fetch services.');
    }
}

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
        var btn = document.querySelector('.tablinks[onclick*="' + tabName + '"]');
        if (btn) {
            btn.className += " active";
        }
    }
    closeModal('updateModal');
}

async function openUpdateModal(orderId) {
    try {
        const response = await fetch(`/api/orders/${orderId}`, {
            method: 'GET',
            credentials: 'include'
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const order = await response.json();
        document.getElementById('orderId').value = orderId;
        document.getElementById('status').value = order.status;
        document.getElementById('completionDate').value = order.completionDate || '';
        openModal('updateModal');
    } catch (error) {
        console.error('Error fetching order details:', error);
        alert('Failed to fetch order details.');
    }
}

document.getElementById('updateOrderForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const orderId = document.getElementById('orderId').value;
    const status = document.getElementById('status').value;
    let completionDate = document.getElementById('completionDate').value;
    if (completionDate) {
             completionDate += 'T00:00:00'; // Adds a time of 00:00:00
         } else {
             completionDate = null; // Set to null if no date is selected
         }
    const response = await fetch(`/api/orders/${orderId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ status, completionDate }),
        credentials: 'include'
    });
    if (response.ok) {
        alert('Order updated successfully.');
        closeModal('updateModal');
        fetchAndDisplayOrders();
    } else {
        alert('Failed to update order.');
    }
});

document.addEventListener('click', function(event) {
    var modal = document.getElementById('updateModal');
    if (event.target == modal) {
        closeModal('updateModal');
    }
});

document.getElementById('updateOrderForm').addEventListener('click', function(event) {
    event.stopPropagation();
});

function openModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

document.getElementById('addServiceForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('serviceName').value;
    const description = document.getElementById('serviceDescription').value;
    const cost = parseFloat(document.getElementById('serviceCost').value);

    const serviceData = {
        name: name,
        description: description,
        cost: cost
    };

    const response = await fetch('/api/services', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(serviceData),
        credentials: 'include'
    });

    if (response.ok) {
        alert('Service added successfully.');
        fetchAndDisplayServices();
        // Clear form inputs
        document.getElementById('serviceName').value = '';
        document.getElementById('serviceDescription').value = '';
        document.getElementById('serviceCost').value = '';
    } else {
        alert('Failed to add service.');
    }
});

(async () => {
    const user = await getCurrentUser();
    if (!user || user.role !== 'MECHANIC') {
        alert('You are not authorized to view this page.');
        window.location.href = 'login.html';
        return;
    }

    fetchAndDisplayOrders();
    fetchAndDisplayServices();

    // Default to Orders tab
    openTab(event, 'Orders');
})();