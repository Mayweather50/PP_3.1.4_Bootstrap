fetch('http://localhost:8089/api/user', {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
    },
})
    .then(response => response.json())
    .then(user => {
        const table = document.getElementById('tableUser');
        const row = table.insertRow();

        row.insertCell().innerText = user.id;
        row.insertCell().innerText = user.firstname;
        row.insertCell().innerText = user.lastname;
        row.insertCell().innerText = user.age;
        row.insertCell().innerText = user.email;
        row.insertCell().innerText = user.roles.map(role => role.name.replace('ROLE_', '')).join(' ');
    })
    .catch((error) => {
        console.error('Error:', error);
    });
