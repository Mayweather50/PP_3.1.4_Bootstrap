//Admin/User
fetch('http://localhost:8089/api/admin/showUser', {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
    },
})
    .then(response => response.json())
    .then(user => {
        const table = document.getElementById('tableAdmin');
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

function editUser(event) {
    console.log("editUser function called");
    event.preventDefault();
    const userId = document.getElementById('userIdEdit').value;
    const firstname = document.getElementById('firstNameEdit').value;
    const lastname = document.getElementById('lastNameEdit').value;
    const age = document.getElementById('ageEdit').value;
    const email = document.getElementById('emailEdit').value;
    const password = document.getElementById('passwordEdit').value;
    const role = document.getElementById('roleEdit').value;
    let selectElement = document.getElementById('roleEdit');
    let selectedOptions = Array.from(selectElement.selectedOptions).map(option => option.value);

    const user = {
        id: userId,
        firstname: firstname,
        lastname: lastname,
        age: age,
        email: email,
        password: password,
        roles: selectedOptions,
    };

    fetch(`http://localhost:8089/api/admin/users/${userId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    })
        .then(response => {
            console.log("Fetch response:", response);
            console.log("Fetch response status:", response.status);
            console.log("Fetch response headers:", response.headers);

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            $('#editModal').modal('hide');
            location.reload();
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}



//Обновление после редактировании
// Функция для обновления таблицы
function updateEditTable() {
    const table = document.getElementById('userTable');
    table.innerHTML = '';

    const editBtn = document.querySelector("#editus");
    editBtn.addEventListener("click", function(event) {

        const fakeEvent = {
            preventDefault: function() {

            }
        };
        editUser(fakeEvent);
    });
}




//DeleteUser
function deleteUser(event) {
    if (event) {
        event.preventDefault();
    }

    const userId = document.getElementById('userId').value;
    fetch(`http://localhost:8089/api/admin/delete/${userId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Network response was not ok, status: ${response.status}`);
            }

            $('#deleteModal').modal('hide');

            location.reload();
        })
        .catch((error) => {
            console.error('Error:', error);
            if (error.response) {
                console.error('Response status:', error.response.status);
                console.error('Response text:', error.response.statusText);
            }
        });

}


// Функция для обновления таблицы
function updateTable() {
    const table = document.getElementById('userTable');
    table.innerHTML = ''; // Очищаем текущее содержимое таблицы

    const deleteBtn = document.querySelector("#deleteus");
    deleteBtn.addEventListener("click", function(event) {
        const fakeEvent = {
            preventDefault: function() {
            }
        };
        deleteUser(fakeEvent);
    });
}

//getAllUsers
fetch('http://localhost:8089/api/admin', {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
    },
})
    .then(response => response.json())
    .then(users => {
        const table = document.getElementById('userTable');
        users.forEach(user => {
            const row = table.insertRow();

            // Добавляем ячейки с данными пользователя
            row.insertCell().innerText = user.id;
            row.insertCell().innerText = user.firstname;
            row.insertCell().innerText = user.lastname;
            row.insertCell().innerText = user.age;
            row.insertCell().innerText = user.email;
            let roles = user.roles.map(role => role.name.replace('ROLE_', '')).join(' ');
            row.insertCell().innerText = roles;

            // Выводим роли в консоль
            console.log('Роли пользователя:', roles);

            // Добавляем ячейку с кнопкой "Edit"
            const editCell = row.insertCell();
            const editButton = document.createElement('button');
            editButton.href = "#";
            editButton.setAttribute('data-toggle', 'modal');
            editButton.setAttribute('data-target', '#editModal');
            editButton.setAttribute('data-user-id', user.id);
            editButton.setAttribute('data-first-name', user.firstname);
            editButton.setAttribute('data-last-name', user.lastname);
            editButton.setAttribute('data-age', user.age);
            editButton.className = "btn btn-primary edit-btn";
            editButton.style.backgroundColor = "#17A2B8";
            editButton.style.border = "#17A2B8";
            editButton.innerHTML = '<span class="glyphicon glyphicon-pencil"></span> Edit';

            editButton.addEventListener('click', function() {
                document.getElementById('userIdEdit').value = user.id;
                document.getElementById('idEdit').value = user.id;
                document.getElementById('firstNameEdit').value = user.firstname;
                document.getElementById('lastNameEdit').value = user.lastname;
                document.getElementById('ageEdit').value = user.age;
                document.getElementById('emailEdit').value = user.email;
                document.getElementById('passwordEdit').value = user.password;
                // Получаем поле выбора роли
                var select = document.getElementById('roleEdit');
                // Проходим через каждый элемент в поле выбора
                for (var i = 0; i < select.options.length; i++) {
                    var option = select.options[i];
                    // Если значение элемента совпадает с одной из ролей пользователя, устанавливаем его как выбранный
                    if (user.roles.includes(option.value)) {
                        option.selected = true;
                    }
                }



            });

            editCell.appendChild(editButton);


            // Добавляем ячейку с кнопкой "Delete"
            const deleteCell = row.insertCell();
            const deleteButton = document.createElement('button');
            deleteButton.innerText = 'Delete';
            deleteButton.setAttribute('data-toggle', 'modal');
            deleteButton.setAttribute('data-target', '#deleteModal');
            deleteButton.className = 'btn btn-danger';
            console.log(user)
            deleteButton.onclick = function() {
                document.getElementById('userId').value = user.id;
                document.getElementById('id').value = user.id;
                document.getElementById('firstName').value = user.firstname;
                document.getElementById('lastName').value = user.lastname;
                document.getElementById('age').value = user.age;
                document.getElementById('email').value = user.email;
                document.getElementById('password').value = user.password;

                document.getElementById('role').value = user.roles.join(', ');
            };

            deleteCell.appendChild(deleteButton);
        });

        const deleteBtn = document.querySelector("#deleteus");
        deleteBtn.addEventListener("click", function(event) {

            const fakeEvent = {
                preventDefault: function() {

                }
            };
            deleteUser(fakeEvent);
        });
        document.addEventListener('DOMContentLoaded', function () {

            document.getElementById('editus').addEventListener('click', function(event) {
                event.preventDefault();
                editUser();
            });


        });


    })
    .catch((error) => {
        console.error('Error:', error);
    });





// Функция для добавления пользователя
// add.js

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('formNew');

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        // Собираем данные из формы
        let selectElement = document.getElementById('create-roles');
        let selectedOptions = Array.from(selectElement.selectedOptions).map(option => option.value);

        const userData = {
            firstname: document.getElementById('create-firstname').value,
            lastname: document.getElementById('create-lastname').value,
            age: parseInt(document.getElementById('create-age').value, 10),
            email: document.getElementById('create-email').value,
            password: document.getElementById('create-password').value,
            roles: selectedOptions,
        };

        console.log('Данные пользователя:', userData)

        addUser(userData);
    });
});


function addUser(userData) {
    fetch('http://localhost:8089/api/admin/user', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return userData;
        })
        .then(data => {
            console.log('User added successfully', data);
            $('#new-user').modal('hide');
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error occurred. Check the console for details.');
            console.log('Error details:', error);
        });

}

//Переключения вкладок

function showNewUserTab() {
    // Скрыть вкладку "allUsers"
    document.getElementById('users').classList.remove('show', 'active');

    // Показать вкладку "new-user"
    document.getElementById('new-user').classList.add('show', 'active');
}

function showAllUsersTab() {
    document.getElementById('users').classList.add('show', 'active');
    document.getElementById('new-user').classList.remove('show', 'active');
}

