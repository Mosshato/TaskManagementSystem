// Access the main content area
const content = document.getElementById('main-content');

// Define the pages object with consistent keys
const pages = {
    managers: `
            <section id="managers-list" style="padding: 20px;">
                <h2>Managers</h2>
                <div id="managers-container" class="managers-grid">
                    <!-- Manager cards will be dynamically inserted here -->
                </div>
            </section>
            
        <!-- Div pentru Taskurile Managerului -->
        <section id="tasks-section" style="display: none; padding: 20px;">
            <h2>Taskuri asignate</h2>
            <div id="tasks-container" class="tasks-grid">
                <!-- Taskurile vor fi generate dinamic -->
            </div>
            <div class="button-group">
                <button id="back-to-managers" onclick="goBackToManagers()">Înapoi la Manageri</button>
                <button id="delete-task-btn" onclick="deleteTask()">Complete/Delete Task</button>
                <button id="add-task-btn" onclick="showAddTaskPopup()">Add Task</button>
            </div>
    </section>
    
    
<!--            <button id="back-button" style="display: none; margin: 20px;" onclick="goBackToManagers()">Înapoi la Manageri</button>-->
        `,

    employee: `
                    <section id="employees-list" style="padding: 20px;">
                            <h2>Employees</h2>
                            <div id="employees-container" class="employees-grid">
                                <!-- Employee cards will be dynamically inserted here -->
                            </div>
                    </section>

            
        <!-- Div pentru Taskurile Managerului -->
                        <section id="tasks-section-employees" style="display: none; padding: 20px;">
                            <h2>Taskuri asignate</h2>
                            <div id="tasks-container-employees" class="tasks-grid">
                                <!-- Taskurile vor fi generate dinamic -->
                            </div>
                            <div class="button-group">
                                <button id="back-to-employees" onclick="goBackToEmployees()">Înapoi la Employees</button>
                                <button id="delete-task-btn-employees" style="display: none;" onclick="deleteEmployeeTask()">Complete/Delete Task</button>
                                <button id="add-task-btn-employees" onclick="showAddTaskPopupEmployees()">Add Task</button>
                            </div>
                        </section>

        `,

    create_users: `
            <section id="create-user-section" style="padding: 20px;">
    <h2 style="text-align: center; margin-bottom: 20px;">Create User</h2>
    <form id="create-user-form" style="max-width: 400px; margin: 0 auto;">
        <!-- Full Name -->
        <div class="form-group">
            <label for="user-full-name" class="form-label">Full Name:</label>
            <input type="text" id="user-full-name" name="fullName" class="form-input" placeholder="Enter full name" required />
        </div>

        <!-- Email -->
        <div class="form-group">
            <label for="user-email" class="form-label">Email:</label>
            <input type="email" id="user-email" name="email" class="form-input" placeholder="Enter email address" required />
        </div>

        <!-- Password -->
        <div class="form-group">
            <label for="user-password" class="form-label">Password:</label>
            <input type="password" id="user-password" name="password" class="form-input" placeholder="Enter password" required />
        </div>

        <!-- Confirm Password -->
        <div class="form-group">
            <label for="user-confirm-password" class="form-label">Confirm Password:</label>
            <input type="password" id="user-confirm-password" name="confirmPassword" class="form-input" placeholder="Confirm password" required />
        </div>

        <!-- User Type -->
        <div class="form-group">
            <label class="form-label">User Type:</label>
            <div class="radio-group">
                <label for="user-type-employee" class="radio-label">
                    <input type="radio" id="user-type-employee" name="userType" value="employee" required /> Employee
                </label>
                <label for="user-type-manager" class="radio-label">
                    <input type="radio" id="user-type-manager" name="userType" value="manager" /> Manager
                </label>
            </div>
        </div>

        <!-- Submit Button -->
        <div class="form-group">
            <button type="submit" class="form-button">Create User</button>
        </div>
    </form>
</section>
        `,

    boss_tasks: `
            <section id="tasks-section-boss" style="padding: 20px;">
    <h2>Your tasks</h2>
    <div id="tasks-container-boss" class="tasks-grid">
        <!-- Taskurile vor fi generate dinamic -->
    </div>
    <div class="button-group">
        <button id="delete-task-btn-boss" style="display: none;" onclick="deleteBossTask()">Complete/Delete Task</button>
        <button id="add-task-btn-boss" onclick="showAddTaskPopupBoss()">Add Task</button>
    </div>
</section>
        `,
    dashboard: `
            <h2>Dashboard</h2>
            <p>Welcome to the Dashboard.</p>
        `,

    my_account: `
    <section id="my-account-section" style="padding: 20px; max-width: 600px; margin: 0 auto;">
        <h2 style="text-align: center;">My Account</h2>
        <div id="account-info" class="account-info" style="margin-top: 20px; background-color: #f9f9f9; border: 1px solid #ddd; padding: 20px; border-radius: 8px;">
            <div style="margin-bottom: 15px;">
                <strong>Full Name:</strong> <span id="user-fullname">Loading...</span>
            </div>
            <div style="margin-bottom: 15px;">
                <strong>Email:</strong> <span id="user-email">Loading...</span>
            </div>
            <div style="margin-bottom: 15px;">
                <strong>Role:</strong> <span id="user-role">Loading...</span>
            </div>
            <div style="margin-bottom: 15px;">
                <strong>Company:</strong> <span id="user-company">Loading...</span>
            </div>
            <div>
                <strong>Company Code:</strong> <span id="company-code">Loading...</span>
            </div>
        </div>
    </section>
     `,
    employee_tasks: `
            <section id="tasks-section-boss" style="padding: 20px;">
    <h2>Your tasks</h2>
    <div id="tasks-container-boss" class="tasks-grid">
        <!-- Taskurile vor fi generate dinamic -->
    </div>
    <div class="button-group">
        <button id="delete-task-btn-boss" style="display: none;" onclick="deleteEmployeeTask()">Complete/Delete Task</button>
    </div>
</section>
`,
    employees_page: `
        <section id="employees-section" style="padding: 20px;">
            <h2>Employees</h2>
            <div id="employees-container" class="employees-grid">
                <!-- Employee cards will be dynamically inserted here -->
            </div>
        </section>

        <section id="tasks-section-employees" style="display: none; padding: 20px;">
            <h2>Taskuri asignate</h2>
            <div id="tasks-container-employees" class="tasks-grid">
                <!-- Taskurile vor fi generate dinamic -->
            </div>
            <div class="button-group">
                <button id="delete-task-btn-employees" style="display: none;" onclick="deleteEmployeeTask()">Complete/Delete Task</button>
                <button id="add-task-btn-employees" onclick="showAddTaskPopupEmployees()">Add Task</button>
            </div>
        </section> 
    `
};
function deleteEmployeeTask() {

}

async function fetchEmployeeTask() {
    try {
        const userEmail = localStorage.getItem("email");
        if (!userEmail) throw new Error("Email not found in localStorage.");

        // Fetch tasks from backend
        const response = await fetch(`/api/employeeTasks?email=${encodeURIComponent(userEmail)}`);
        if (!response.ok) throw new Error(`Failed to fetch tasks: ${response.status} ${response.statusText}`);

        const tasks = await response.json();
        console.log("Employee Tasks:", tasks); // Log pentru debugging

        // ✅ Afișează taskurile în UI
        const taskContainer = document.getElementById("tasks-container-employees");

        if (!taskContainer) {
            console.error("Element with ID 'tasks-container-employees' not found in the DOM.");
            return;
        }

        taskContainer.innerHTML = ""; // Curăță lista anterioară

        if (tasks.length === 0) {
            taskContainer.innerHTML = "<p>No tasks assigned.</p>";
        } else {
            tasks.forEach((task) => {
                const taskElement = document.createElement("div");
                taskElement.className = "task-item";
                taskElement.innerHTML = `<strong>${task.description}</strong>`;
                taskContainer.appendChild(taskElement);
            });
        }

    } catch (error) {
        console.error("Error fetching employee tasks:", error);
        const taskContainer = document.getElementById("tasks-container-employees");
        if (taskContainer) {
            taskContainer.innerHTML = "<p>Error loading tasks. Please try again later.</p>";
        }
    }
}




// Utility function to safely get an element by ID
function safeGetElement(id) {
    return document.getElementById(id) || null;
}

// Toggle sidebar visibility
function toggleSidebar() {
    const sidebar = safeGetElement("sidebar");
    const mainContent = document.querySelector(".main-content");
    if (sidebar && mainContent) {
        sidebar.classList.toggle("collapsed");
        mainContent.classList.toggle("collapsed");
    }
}

// Load page into the main content area
function loadPage(page) {
    if (pages.hasOwnProperty(page)) {
        content.innerHTML = pages[page];
        attachEventListeners(page);
    } else {
        content.innerHTML = `
                <h2>Page Not Found</h2>
                <p>The page you're looking for does not exist.</p>`;
        console.warn(`Page "${page}" not found in pages object.`);
    }
}

// Function to attach event listeners based on the loaded page
function attachEventListeners(page) {
    switch(page) {
        case 'managers':
            fetchManagers();
            break;

        case 'create_users':
            break;

        case 'employee':
            fetchEmployees();
            break;

        case 'boss_tasks':
            fetchEmployeeTask();
            break;
        case 'dashboard':
            break;
        case 'my_account':
            displayMyAccount();
            break;
        case 'employees_page':
            displayEmployeeTasks();
            break;
        default:
            // No action needed
            break;
    }
}

async function deleteBossTask() {
    if (!selectedTask) {
        alert('Please select a task to delete!');
        return;
    }

    // Confirmă ștergerea task-ului
    const confirmation = confirm('Are you sure you want to delete this task?');
    if (!confirmation) return;
    console.log(`Deleting task with ID: ${selectedTask}`);
    console.log(`Request URL: /tasks/${selectedTask}`);
    try {
        // Trimite cererea către backend pentru a șterge task-ul selectat
        const response = await fetch(`/api/tasks/${selectedTask}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`Failed to delete task: ${response.statusText}`);
        }

        // Elimină task-ul din front-end
        const tasksContainer = document.getElementById('tasks-container-boss');
        const selectedCard = tasksContainer.querySelector('.task-card.selected');
        if (selectedCard) {
            selectedCard.remove(); // Elimină task-ul selectat din DOM
        }

        // Resetează starea
        selectedTask = null;
        document.getElementById('delete-task-btn-boss').style.display = 'none';

        alert('Task deleted successfully!');
    } catch (error) {
        console.error('Error deleting task:', error);
        alert('Failed to delete the task. Please try again.');
    }
}

// Function to display messages to the user
function displayMessage(message, type = 'success') {
    // Remove existing message if any
    let existingMessage = document.querySelector('.message');
    if (existingMessage) {
        existingMessage.remove();
    }

    // Create a new message element
    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message');
    messageDiv.classList.add(type); // 'success' or 'error'
    messageDiv.textContent = message;

    // Insert the message at the top of the main content
    content.prepend(messageDiv);
}

// ----------------- Server Request Functions -----------------
function showAddTaskPopupBoss() {
    const popup = document.getElementById('add-task-popup-boss');
    if (popup) {
        popup.style.display = 'flex';
    }
}

function closeAddTaskPopupBoss() {
    const popup = document.getElementById('add-task-popup-boss');
    if (popup) {
        popup.style.display = 'none';
    }
}

// 1. Fetch Managers (GET Request)
async function fetchManagers() {
    try {

        const bossEmail = localStorage.getItem('email');
        if (!bossEmail) {
            displayMessage('Boss email not found in localStorage.', 'error');
            return;
        }
        console.log(bossEmail);
        // Construim URL-ul corect pentru request
        const url = `/api/managers?email=${bossEmail}`;
        // Fetch managers
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error(`Error: ${response.status} ${response.statusText}`);
        }

        const managers = await response.json();
        console.log("Manageri primiți de la API:", managers);
        displayManagers(managers);
    } catch (error) {
        console.error('Error fetching managers:', error);
        displayMessage('Failed to load managers.', 'error');
    }
}

// Function to display managers in the DOM
function displayManagers(managers) {
    const container = document.getElementById('managers-container');
    if (!container) return;

    container.innerHTML = ''; // Clear existing content

    if (managers.length === 0) {
        return;
    }

    managers.forEach(manager => {
        const card = document.createElement('div');
        card.className = 'manager-card';
        card.addEventListener('click', () => {
            selectedTask = manager.id; // Setează ID-ul managerului selectat
            displayManagerTasks(manager.id);
        });
        card.innerHTML = `
                <strong>Name:</strong> ${manager.fullname}<br>
                <strong>Email:</strong> ${manager.email}<br>
                <strong>Tasks Assigned:</strong> ${manager.tasksNum}
            `;
        container.appendChild(card);
    });
}

async function displayManagerTasks(managerId) {
    const tasksSection = document.getElementById('tasks-section');
    const tasksContainer = document.getElementById('tasks-container');

    if (!tasksSection || !tasksContainer) {
        console.error('tasks-section sau tasks-container nu au fost găsite în DOM.');
        return;
    }

    // Afișează un mesaj de încărcare
    tasksContainer.innerHTML = '<p>Loading tasks...</p>';

    try {
        // Fetch tasks de la server
        const response = await fetch(`/api/managers/tasks?managerId=${managerId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to fetch tasks: ${response.statusText}`);
        }

        // Parsează răspunsul JSON
        const tasks = await response.json();



        // Curăță conținutul anterior
        tasksContainer.innerHTML = '';
        // Afișează un mesaj dacă lista este goală
        if (tasks.length === 0) {
            tasksContainer.innerHTML = '<p>No tasks assigned for this manager.</p>';
        }
        else {
            tasks.forEach(task => {
                const taskCard = document.createElement('div');
                taskCard.className = 'task-card';
                taskCard.innerHTML = `
                <strong>Description:</strong> ${task.description}<br>
                <strong>Created At:</strong> ${new Date(task.createdAt).toLocaleDateString()}<br>
                <strong>Due Date:</strong> ${new Date(task.dueDate).toLocaleDateString()}
            `;
                // Adaugă event listener pentru click
                taskCard.addEventListener('click', () => {
                    toggleTaskSelection(taskCard, task.id);
                });

                tasksContainer.appendChild(taskCard);
            });
        }

    } catch (error) {
        console.error('Error fetching tasks:', error);
        tasksContainer.innerHTML = '<p>Error loading tasks. Please try again later.</p>';
    }

    // Afișează secțiunea cu taskuri și ascunde lista managerilor
    tasksSection.style.display = 'block';
    document.getElementById('managers-list').style.display = 'none';
}

async function fetchEmployees() {
    try {
        const bossEmail = localStorage.getItem('email');
        if (!bossEmail) {
            displayMessage('Boss email not found in localStorage.', 'error');
            return;
        }
        console.log(bossEmail);
        // Construim URL-ul corect pentru request
        const url = `/api/employees?email=${bossEmail}`;
        // Fetch employees
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        // Verificăm statusul răspunsului
        if (response.status === 204) {
            return;
        }

        if (!response.ok) {
            throw new Error(`Error: ${response.status} ${response.statusText}`);
        }

        const employees = await response.json();
        console.log(employees.length);
        if (employees.length === 0) {
            displayMessage('No employees found.', 'info');
            return;
        }
        console.log("Angajați primiți de la API:", employees);
        displayEmployees(employees);
    } catch (error) {
        console.error('Error fetching employees:', error);
        displayMessage('Failed to load employees.', 'error');
    }
}

let selectedEmployeeId = null; // Variabilă globală pentru ID-ul employee-ului selectat

function displayEmployees(employees) {
    const container = document.getElementById('employees-container');
    if (!container) return;

    container.innerHTML = ''; // Clear existing content

    if (employees.length === 0) {
        container.innerHTML = '<p>No employees found.</p>';
        return;
    }

    employees.forEach(employee => {
        const card = document.createElement('div');
        card.className = 'employee-card';
        card.addEventListener('click', () => {
            selectedEmployeeId = employee.id; // Actualizează ID-ul employee-ului selectat
            displayEmployeeTasks(employee.id); // Afișează task-urile pentru employee-ul selectat
        });
        card.innerHTML = `
                <strong>Name:</strong> ${employee.fullname}<br>
                <strong>Email:</strong> ${employee.email}<br>
                <strong>Tasks Assigned:</strong> ${employee.tasksNum}
            `;
        container.appendChild(card);
    });
}

function ensureEmployeeTasksContainerExists(callback) {
    setTimeout(() => {
        const taskContainer = document.getElementById("tasks-container-employees");
        if (taskContainer) {
            callback(); // Apelează funcția doar dacă elementul există
        } else {
            console.error("❌ Element 'tasks-container-employees' NU a fost încă adăugat în DOM!");
        }
    }, 200); // Așteaptă puțin ca să fie inserat în DOM
}


async function displayEmployeeTasks(employeeEmail) {
    const tasksSection = document.getElementById('tasks-section-employees');
    const tasksContainer = document.getElementById('tasks-container-employees');

    if (!tasksSection || !tasksContainer) {
        console.error('❌ tasks-section-employees sau tasks-container-employees nu au fost găsite în DOM.');
        return;
    }

    tasksContainer.innerHTML = '<p>Loading tasks...</p>';

    try {
        const response = await fetch(`/api/employees/tasks?email=${encodeURIComponent(employeeEmail)}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        // Handle 204 No Content
        if (response.status === 204) {
            tasksContainer.innerHTML = '<p>No tasks assigned for this employee.</p>';
            tasksSection.style.display = 'block';
            document.getElementById('employees-list').style.display = 'none';
            return;
        }

        if (!response.ok) {
            throw new Error(`Failed to fetch tasks: ${response.statusText}`);
        }

        const tasks = await response.json();

        if (tasks.length === 0) {
            tasksContainer.innerHTML = '<p>No tasks assigned for this employee.</p>';
            tasksSection.style.display = 'block';
            document.getElementById('employees-list').style.display = 'none';
            return;
        }

        // Clear existing tasks and populate the container
        tasksContainer.innerHTML = '';
        tasks.forEach(task => {
            const taskCard = document.createElement('div');
            taskCard.className = 'task-card';
            taskCard.innerHTML = `
                <strong>Description:</strong> ${task.description}<br>
                <strong>Created At:</strong> ${new Date(task.createdAt).toLocaleDateString()}<br>
                <strong>Due Date:</strong> ${new Date(task.dueDate).toLocaleDateString()}
            `;
            // Add event listener for selection
            taskCard.addEventListener('click', () => {
                toggleEmployeeTaskSelection(taskCard, task.id);
            });

            tasksContainer.appendChild(taskCard);
        });

    } catch (error) {
        console.error('❌ Error fetching tasks:', error);
        tasksContainer.innerHTML = '<p>Error loading tasks. Please try again later.</p>';
    }

    tasksSection.style.display = 'block';
    document.getElementById('employees-list').style.display = 'none';
}


function goBackToManagers() {
    // Ascunde secțiunea taskurilor
    const tasksSection = document.getElementById('tasks-section');
    if (tasksSection) {
        tasksSection.style.display = 'none';
    }

    // Afișează lista managerilor
    const managersList = document.getElementById('managers-list');
    if (managersList) {
        managersList.style.display = 'block';
    }
}

let selectedTask = null; // Variabilă pentru a stoca ID-ul task-ului selectat

function toggleTaskSelection(taskCard, taskId) {
    const tasksContainer = document.getElementById('tasks-container-boss');

    // Verifică dacă tasksContainer există
    if (!tasksContainer) {
        console.error('tasks-container-boss nu a fost găsit în DOM.');
        return;
    }

    // Verifică dacă taskCard este valid
    if (!taskCard) {
        console.error('taskCard este null.');
        return;
    }

    // Deselectează task-ul anterior
    const previousSelectedCard = tasksContainer.querySelector('.task-card.selected');
    if (previousSelectedCard) {
        previousSelectedCard.classList.remove('selected');
    }

    // Selectează sau deselectează task-ul curent
    if (selectedTask === taskId) {
        selectedTask = null;
        taskCard.classList.remove('selected');
    } else {
        selectedTask = taskId;
        taskCard.classList.add('selected');
    }

    // Verifică și setează stilul pentru butonul de ștergere
    const deleteTaskBtn = document.getElementById('delete-task-btn-boss');
    if (deleteTaskBtn) {
        deleteTaskBtn.style.display = selectedTask ? 'block' : 'none';
    } else {
        console.warn('delete-task-btn-boss nu a fost găsit în DOM.');
    }

    console.log('Selected task:', selectedTask);
}

let selectedEmployeeTask = null; // Variabilă pentru a stoca ID-ul task-ului selectat

function goBackToEmployees() {
    const tasksSection = document.getElementById('tasks-section-employees');
    if (tasksSection) {
        tasksSection.style.display = 'none';
    }

    const employeesList = document.getElementById('employees-list');
    if (employeesList) {
        employeesList.style.display = 'block';
    }
}

function toggleEmployeeTaskSelection(taskCard, taskId) {
    const tasksContainer = document.getElementById('tasks-container-employees');
    const deleteTaskBtn = document.getElementById('delete-task-btn-employees');

    // Deselectează task-ul anterior (dacă există)
    if (selectedEmployeeTask !== null) {
        const previousSelectedCard = tasksContainer.querySelector('.task-card.selected');
        if (previousSelectedCard) {
            previousSelectedCard.classList.remove('selected');
        }
    }

    // Selectează task-ul curent sau îl deselectează
    if (selectedEmployeeTask === taskId) {
        // Dacă utilizatorul face clic pe același task, îl deselectăm
        selectedEmployeeTask = null;
        taskCard.classList.remove('selected');
        deleteTaskBtn.style.display = 'none'; // Ascunde butonul
    } else {
        // Selectăm noul task
        selectedEmployeeTask = taskId;
        taskCard.classList.add('selected');
        deleteTaskBtn.style.display = 'block'; // Afișează butonul
    }

    console.log('Selected employee task:', selectedEmployeeTask);
}

async function deleteTask() {
    if (!selectedTask) {
        alert('Please select a task to delete!');
        return;
    }

    // Confirmă ștergerea task-ului
    const confirmation = confirm('Are you sure you want to delete this task?');
    if (!confirmation) return;

    try {
        // Trimite cererea către server pentru a șterge task-ul
        const response = await fetch(`/api/delete/task${selectedTask}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`Failed to delete task: ${response.statusText}`);
        }

        // Elimină task-ul din front-end
        const tasksContainer = document.getElementById('tasks-container');
        const selectedCard = tasksContainer.querySelector('.task-card.selected');
        if (selectedCard) {
            selectedCard.remove(); // Elimină task-ul selectat din DOM
        }

        // Resetează starea
        selectedTask = null;
        document.getElementById('delete-task-btn').style.display = 'none';

        alert('Task deleted successfully!');
    } catch (error) {
        console.error('Error deleting task:', error);
        alert('Failed to delete the task. Please try again.');
    }
}

function showAddTaskPopup() {
    const popup = document.getElementById('add-task-popup');
    popup.style.display = 'flex'; // Afișează pop-up-ul
}

function closeAddTaskPopup() {
    const popup = document.getElementById('add-task-popup');
    popup.style.display = 'none'; // Ascunde pop-up-ul
}

async function addTask(event) {
    event.preventDefault();

    const description = document.getElementById('task-description').value;
    const createdAt = document.getElementById('task-created-at').value;
    const dueDate = document.getElementById('task-due-date').value;

    if (!selectedTask) {
        alert('Please select a manager first!');
        return;
    }
    // Construim obiectul JSON
    const taskData = {
        description,
        createdAt,
        dueDate,
        assignedUserId: selectedTask
    };

    // Printăm obiectul înainte de a-l trimite
    console.log("Task object being sent to server:", taskData);

    try {
        const response = await fetch('/api/add/tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                description,
                createdAt,
                dueDate,
                assignedUserId: selectedTask
            })
        });

        if (!response.ok) {
            throw new Error('Failed to add task');
        }

        alert('Task added successfully!');
        closeAddTaskPopup();
        document.getElementById('add-task-form').reset();
    } catch (error) {
        console.error('Error adding task:', error);
        alert('Failed to add task. Please try again.');
    }
}

function showAddTaskPopupEmployees() {
    if (!selectedEmployeeId) {
        alert('Please select an employee first!');
        return;
    }

    const popup = document.getElementById('add-task-popup-employees');
    if (popup) {
        popup.style.display = 'block'; // Afișează pop-up-ul
    }
}

function closeAddTaskPopupEmployees() {
    const popup = document.getElementById('add-task-popup-employees');
    popup.style.display = 'none'; // Ascunde pop-up-ul
}

async function addEmployeeTask(event) {
    event.preventDefault();

    const description = document.getElementById('task-description-employees').value;
    const createdAt = document.getElementById('task-created-at-employees').value;
    const dueDate = document.getElementById('task-due-date-employees').value;

    if (!selectedEmployeeId) {
        alert('Please select an employee first!');
        return;
    }

    const taskData = {
        description,
        createdAt,
        dueDate,
        assignedUserId: selectedEmployeeId
    };
    try {
        const response = await fetch('api/add/tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(taskData)
        });

        if (!response.ok) {
            throw new Error('Failed to add task');
        }

        alert('Task added successfully!');

        // Închide popup-ul și resetează formularul
        closeAddTaskPopupEmployees();
        document.getElementById('add-task-form-employees').reset();

        // Actualizează lista de task-uri pentru employee-ul selectat
        displayEmployeeTasks(selectedEmployeeId);
    } catch (error) {
        console.error('Error adding task:', error);
        alert('Failed to add task. Please try again.');
    }
}

async function deleteEmployeeTask() {
    if (!selectedEmployeeTask) {
        alert('Please select a task to delete!');
        return;
    }

    const confirmation = confirm('Are you sure you want to delete this task?');
    if (!confirmation) return;

    try {
        const response = await fetch(`/api/tasks/${selectedEmployeeTask}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to delete task: ${response.statusText}`);
        }

        const tasksContainer = document.getElementById('tasks-container-employees');
        const selectedCard = tasksContainer.querySelector('.task-card.selected');
        if (selectedCard) {
            selectedCard.remove(); // Elimină task-ul selectat din DOM
        }

        selectedEmployeeTask = null;
        document.getElementById('delete-task-btn-employees').style.display = 'none';

        alert('Task deleted successfully!');
    } catch (error) {
        console.error('Error deleting task:', error);
        alert('Failed to delete the task. Please try again.');
    }
}

async function displayBossTasks() {
    const tasksSection = document.getElementById('tasks-section-boss');
    const tasksContainer = document.getElementById('tasks-container-boss');

    // Verifică dacă elementele există în DOM
    if (!tasksSection || !tasksContainer) {
        console.error('tasks-section-boss sau tasks-container-boss nu au fost găsite în DOM.');
        return;
    }

    // Afișează un mesaj de încărcare
    tasksContainer.innerHTML = '<p>Loading tasks...</p>';

    try {
        // Obține email-ul bossului din localStorage
        const bossEmail = localStorage.getItem('email');

        if (!bossEmail) {
            console.error('Email-ul bossului nu este disponibil în localStorage.');
            tasksContainer.innerHTML = '<p>Boss email not found. Please try logging in again.</p>';
            return;
        }

        console.log('Fetching tasks for boss with email:', bossEmail);

        // Fetch tasks de la server
        const response = await fetch(`/api/boss/tasks?email=${encodeURIComponent(bossEmail)}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        console.log('Response status:', response.status);

        if (!response.ok) {
            throw new Error(`Failed to fetch tasks: ${response.statusText}`);
        }

        // Parsează răspunsul JSON
        const textResponse = await response.text();
        console.log('Response text:', textResponse);

        if (!textResponse) {
            console.warn('Empty response received from server.');
            tasksContainer.innerHTML = '<p>No tasks found for this boss.</p>';
            return;
        }

        const tasks = JSON.parse(textResponse);

        console.log('Tasks received:', tasks);

        // Curăță conținutul anterior
        tasksContainer.innerHTML = '';

        // Afișează un mesaj dacă lista este goală
        if (tasks.length === 0) {
            tasksContainer.innerHTML = '<p>No tasks assigned for this boss.</p>';
        } else {
            tasks.forEach(task => {
                const taskCard = document.createElement('div');
                taskCard.className = 'task-card';
                taskCard.innerHTML = `
                <strong>Description:</strong> ${task.description}<br>
                <strong>Created At:</strong> ${new Date(task.createdAt).toLocaleDateString()}<br>
                <strong>Due Date:</strong> ${new Date(task.dueDate).toLocaleDateString()}
            `;
                // Adaugă event listener pentru click
                taskCard.addEventListener('click', () => {
                    toggleTaskSelection(taskCard, task.id);
                });

                tasksContainer.appendChild(taskCard);
            });
        }

    } catch (error) {
        console.error('Error fetching tasks:', error.message || error);
        tasksContainer.innerHTML = '<p>Error loading tasks. Please try again later.</p>';
    }

    // Afișează secțiunea cu taskuri și ascunde alte secțiuni, dacă este necesar
    tasksSection.style.display = 'block';
    const employeesList = document.getElementById('employees-list');
    if (employeesList) {
        employeesList.style.display = 'none';
    }
}

async function displayMyAccount() {
    try {
        // Obține email-ul utilizatorului din localStorage
        const userEmail = localStorage.getItem('email');
        console.log(userEmail);
        if (!userEmail) {
            throw new Error('User email not found in localStorage.');
        }

        // Trimite cererea către backend pentru a obține informațiile
        const response = await fetch(`/api/currentUser?email=${encodeURIComponent(userEmail)}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to fetch user details.');
        }

        const userDetails = await response.json();

        // Populează câmpurile din secțiunea My Account
        console.log(userDetails.companyName);

        document.getElementById('user-fullname').textContent = userDetails.fullName || 'N/A';
        document.getElementById('user-email').textContent = userDetails.email || 'N/A';
        document.getElementById('user-role').textContent = userDetails.role || 'N/A';
        document.getElementById('user-company').textContent = userDetails.companyName || 'N/A';
        document.getElementById('company-code').textContent = userDetails.companyCode || 'N/A'; // Adăugarea companiei

    } catch (error) {
        console.error('Error loading My Account details:', error);
        document.getElementById('account-info').innerHTML = '<p>Error loading account information. Please try again later.</p>';
    }
}


// ----------------- Initial User Data Fetch -----------------

// Fetch and display user data upon loading the dashboard
document.addEventListener('DOMContentLoaded', () => {
    // Obține email-ul utilizatorului din localStorage
    console.log("Verificăm localStorage...");
    console.log("Email din localStorage:", localStorage.getItem("email"));
    console.log("Role din localStorage:", localStorage.getItem("role"));

    const userEmail = localStorage.getItem('email');
    console.log(userEmail);
    if (userEmail) {
        fetch(`/api/currentUser?email=${encodeURIComponent(userEmail)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error: ${response.status} ${response.statusText}`);
                }
                return response.json();
            })
            .then(user => {
                if (user.companyName) {
                    document.querySelector('.company-name').textContent = `Compania: ${user.companyName}`;
                } else {
                    document.querySelector('.company-name').textContent = `Compania: Nume necunoscut`;
                }
            })
            .catch(error => {
                console.error('Eroare la obținerea datelor utilizatorului logat:', error);
                document.querySelector('.company-name').textContent = `Compania: Eroare la afișare`;
            });
    } else {
        console.error('Email-ul utilizatorului nu a fost găsit în localStorage.');
        document.querySelector('.company-name').textContent = `Compania: Eroare la afișare`;
    }

    // Adaugă event listener-ul pentru formularul Add Task (Manageri)
    const addTaskForm = document.getElementById('add-task-form');
    if (addTaskForm) {
        addTaskForm.addEventListener('submit', addTask);
    }

    // Adaugă event listener-ul pentru formularul Add Task (Employees)
    const addEmployeeTaskForm = document.getElementById('add-task-form-employees');
    if (addEmployeeTaskForm) {
        addEmployeeTaskForm.addEventListener('submit', addEmployeeTask);
    }

    const addBossTaskForm = document.getElementById('add-task-form-boss');
    if (addBossTaskForm) {
        addBossTaskForm.addEventListener('submit', addBossTask);
    }
});

async function addBossTask(event) {
    event.preventDefault();

    const description = document.getElementById('task-description-boss').value;
    const createdAt = document.getElementById('task-created-at-boss').value;
    const dueDate = document.getElementById('task-due-date-boss').value;

    // Obține email-ul boss-ului din localStorage
    const email = localStorage.getItem('email');

    if (!email) {
        alert('Boss email not found. Please log in again.');
        return;
    }

    const taskData = {
        description,
        createdAt,
        dueDate,
        email, // Include email-ul boss-ului în payload
    };
    console.log(taskData);
    console.log('aici');
    try {
        const response = await fetch('/api/add/tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(taskData),
        });

        if (!response.ok) {
            throw new Error('Failed to add task for the boss');
        }

        alert('Task added successfully!');

        // Închide popup-ul și resetează formularul
        closeAddTaskPopupBoss();
        document.getElementById('add-task-form-boss').reset();

        // Actualizează lista de task-uri pentru boss
        displayBossTasks();
    } catch (error) {
        console.error('Error adding task for boss:', error);
        alert('Failed to add task. Please try again.');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const createUserForm = document.getElementById('create-user-form');

    if (createUserForm) {
        createUserForm.addEventListener('submit', async (event) => {
            event.preventDefault(); // Prevenim trimiterea implicită a formularului

            // Obținem valorile din formular
            const fullName = document.getElementById('full-name').value.trim();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirm-password').value;
            const userType = document.querySelector('input[name="user-type"]:checked')?.value;

            // Validări
            if (!fullName || !email || !password || !confirmPassword || !userType) {
                alert('Toate câmpurile sunt obligatorii!');
                return;
            }

            if (password !== confirmPassword) {
                alert('Parolele nu coincid!');
                return;
            }

            if (!validateEmail(email)) {
                alert('Adresa de email nu este validă!');
                return;
            }

            // După validare, continuăm cu pasul următor
            console.log('Formular valid. Datele pregătite pentru trimitere:', {
                fullName,
                email,
                password,
                userType,
            });

            // Aici oprim pentru moment și așteptăm confirmarea ta să continuăm.
        });
    }
});

document.addEventListener("DOMContentLoaded", async () => {
    const userRole = localStorage.getItem('role');
    updateSidebar(userRole);

    // Call fetchEmployeeTask if the user is an employee
    if (userRole === "employee") {
        await fetchEmployeeTask();
    }
});

document.addEventListener('submit', async (event) => {
    event.preventDefault();

    const fullname = document.getElementById('user-full-name').value;
    const email = document.getElementById('user-email').value;
    const password = document.getElementById('user-password').value;
    const confirmPassword = document.getElementById('user-confirm-password').value;
    const role = document.querySelector('input[name="userType"]:checked').value;

    if (password !== confirmPassword) {
        alert("Passwords do not match!");
        return;
    }

    const bossEmail = localStorage.getItem('email'); // Get boss email from localStorage
    if (!bossEmail) {
        alert("Boss email not found in localStorage!");
        return;
    }

    const userData = {
        fullName: fullname,
        email: email,
        password: password,
        role: role
    };
    try {
        const response = await fetch('/api/manual/create/user', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Boss-Email': bossEmail
            },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            const result = await response.text();
            alert(result);
        } else {
            const error = await response.text();
            alert("Error: " + error);
        }
    } catch (error) {
        console.error("Error creating user:", error);
        alert("Failed to create user.");
    }
});

function keepRoleUp(){
    const userRole = localStorage.getItem('role');
    window.location.href = `/dashboard.html?role=${encodeURIComponent(userRole)}`;
}

function updateSidebar(role) {
    const sidebar = document.getElementById("sidebar");
    const menuList = sidebar.querySelector("ul");

    // Clear existing menu items
    menuList.innerHTML = "";

    // Add menu items dynamically based on role
    if (role === "employee") {
        menuList.innerHTML += `
            <li><a href="#" onclick="loadPage('employee_tasks')">Your Tasks</a></li>
            <li><a href="#" onclick="loadPage('company_info')">Company Info</a></li>
        `;
    } else if (role === "manager") {
        menuList.innerHTML += `
            <li><a href="#" onclick="loadPage('boss_tasks')">Your Tasks</a></li>
            <li><a href="#" onclick="loadPage('employee')">Employees</a></li>
            <li><a href="#" onclick="loadPage('company_info')">Company Info</a></li>
        `;
    } else if (role === "boss") {
        menuList.innerHTML += `
            <li><a href="#" onclick="loadPage('managers')">Managers</a></li>
            <li><a href="#" onclick="loadPage('employee')">Employees</a></li>
            <li><a href="#" onclick="loadPage('create_users')">Create Users</a></li>
            <li><a href="#" onclick="loadPage('boss_tasks')">Your Tasks</a></li>
            <li><a href="#" onclick="loadPage('company_info')">Company Info</a></li>
        `;
    } else {
        menuList.innerHTML = `<li><a href="#">Unauthorized</a></li>`;
    }
}
// Funcție pentru validarea email-ului
function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

