package com.example.TaskManagementSystem.Controllers;

import com.example.TaskManagementSystem.DTOs.UserDTO;
import com.example.TaskManagementSystem.Dao.CompanyDao;
import com.example.TaskManagementSystem.Dao.TaskDao;
import com.example.TaskManagementSystem.Dao.UserDao;
import com.example.TaskManagementSystem.Models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession; // Import pentru sesiune
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api") // Base path for this controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserDao userDao;

    // Constructor injection for UserDao
    public UserController() {
        this.userDao = new UserDao();
    }
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksByManager(@RequestParam Long managerId) {
        TaskDao taskDao = new TaskDao();
        List<Task> tasks = taskDao.getTasksByManagerId(managerId);

        if (tasks.isEmpty()) {
            return ResponseEntity.status(404).body(new ArrayList<>());
        }
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpForm signUpForm, BindingResult bindingResult) {
        // Log received data
        System.out.println("Received SignUpForm: " + signUpForm);

        // Validate input
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append(" "));
            return ResponseEntity.badRequest().body(new ApiResponse(false, errors.toString().trim()));
        }

        // Check if passwords match
        if (!signUpForm.getPassword().equals(signUpForm.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Passwords do not match."));
        }

        // Check if user already exists
        if (userDao.userExists(signUpForm.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "A user with this email already exists."));
        }

        // Handle roles
        if ("boss".equalsIgnoreCase(signUpForm.getRole())) {
            // Ensure `companyName` is provided
            if (signUpForm.getCompanyName() == null || signUpForm.getCompanyName().isBlank()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Company name is required for boss users."));
            }

            // Generate company code and create company
            String companyCode = generateRandomCompanyCode();
            Company company = new Company();
            company.setCompanyName(signUpForm.getCompanyName());
            company.setCompanyCode(companyCode);

            // Insert company into database and retrieve company_id
            CompanyDao companyDao = new CompanyDao();
            long companyId = companyDao.addCompanyAndReturnId(company);

            if (companyId == -1) {
                return ResponseEntity.status(500).body(new ApiResponse(false, "Failed to create company."));
            }

            // Create the user
            User user = new User(signUpForm.getFullname(), signUpForm.getEmail(), signUpForm.getPassword(), signUpForm.getRole());
            user.setCompanyId(companyId);

            // Add user to the database
            if (userDao.addUser(user)) {
                return ResponseEntity.ok(new ApiResponse(true, "Boss registered successfully with company."));
            } else {
                return ResponseEntity.status(500).body(new ApiResponse(false, "Failed to register boss."));
            }
        } else {
            // Ensure `companyCode` is provided for non-boss users
            if (signUpForm.getCompanyCode() == null || signUpForm.getCompanyCode().isBlank()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Company code is required for manager or employee users."));
            }

            // Retrieve company ID using `companyCode`
            CompanyDao companyDao = new CompanyDao();
            Long companyId = companyDao.getCompanyIdByCode(signUpForm.getCompanyCode());

            if (companyId == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid company code."));
            }

            // Create the user
            User user = new User(signUpForm.getFullname(), signUpForm.getEmail(), signUpForm.getPassword(), signUpForm.getRole());
            user.setCompanyId(companyId);

            // Add user to the database
            if (userDao.addUser(user)) {
                return ResponseEntity.ok(new ApiResponse(true, "User registered successfully."));
            } else {
                return ResponseEntity.status(500).body(new ApiResponse(false, "Failed to register user."));
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> loginUser(@RequestBody LoginForm loginForm, HttpSession session) {
        // Căutăm utilizatorul după email
        User user = userDao.findUserByEmail(loginForm.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email sau parola incorectă."));
        }

        // Hash-uim parola introdusă folosind salt-ul stocat
        String hashedInputPassword = userDao.hashPassword(loginForm.getPassword());

        // Verificăm dacă hash-ul coincide
        if (!user.getPassword().equals(hashedInputPassword)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email sau parola incorectă."));
        }

        // Stocăm utilizatorul în sesiune
        session.setAttribute("loggedInUser", user);
        logger.info("User {} logged in successfully with role: {}", user.getEmail(), user.getUserType());
        UserDTO userDto = new UserDTO(user.getFullName(), user.getEmail(), user.getCompanyId(), user.getUserType());
        return ResponseEntity.ok(new ApiResponse<UserDTO>(true, "Autentificare reușită!", userDto));
    }

    @PostMapping("/manual/create/user")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO, @RequestHeader("Boss-Email") String bossEmail)   {
        try {
            if (userDao.userExists(userDTO.getEmail())) {
            return ResponseEntity.status(400).body("Email-ul există deja în baza de date.");
        }
            // Retrieve the company ID using the boss's email
            Long companyId = userDao.getCompanyIdByEmail(bossEmail);
            // Create a User object
            User user = new User();
            user.setFullname(userDTO.getFullName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword()); // Hash the password
            user.setUserType(userDTO.getRole());
            user.setCompanyId(companyId);
            // Save the user
            boolean isAdded = userDao.addUser(user);

            if (isAdded) {
                return ResponseEntity.ok("User created successfully.");
            } else {
                return ResponseEntity.status(500).body("Failed to create user.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/loggedInUser")
    public ResponseEntity<User> getLoggedInUser(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized dacă utilizatorul nu este logat
        }

        logger.info("User: {}, Company Name: {}", loggedInUser.getEmail(), loggedInUser.getCompanyName());
        return ResponseEntity.ok(loggedInUser); // Returnăm utilizatorul logat cu toate informațiile, inclusiv numele companiei
    }

    @GetMapping("/boss/tasks")
    public ResponseEntity<List<Task>> getBossTasks(@RequestParam("email") String email) {
        try {
            TaskDao taskDao = new TaskDao();
            // Retrieve the tasks assigned to the boss based on email
            List<Task> tasks = taskDao.getTasksByBossEmail(email);

            if (tasks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(tasks);
            }

            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/currentUser")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestParam String email) {
        // Obține companyId utilizând metoda din UserDao
        Long companyId = userDao.getCompanyIdByEmail(email);
        System.out.println(email);
        // Obține companyCode utilizând metoda din UserDao
        String companyCode = userDao.getCompanyCodeByEmail(email);

        // Găsește utilizatorul în baza de date
        User user = userDao.findUserByEmail(email);

        if (user == null) {
            System.out.println("user not found");
            return ResponseEntity.status(404).body(null); // Returnează 404 dacă utilizatorul nu este găsit
        }
        String companyName = userDao.getCompanyNameByEmail(email);
        // Mapare User -> UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCompanyId(companyId); // Folosește ID-ul obținut din funcție
        userDTO.setRole(user.getUserType());
        userDTO.setCompanyCode(companyCode); // Folosește codul companiei obținut din funcție
        userDTO.setCompanyName(companyName); // Adaugă companyName în DTO

        return ResponseEntity.ok(userDTO); // Returnează detaliile utilizatorului mapate în DTO
    }

    @GetMapping("/managers")
    public ResponseEntity<List<User>> getManagersByBoss(@RequestParam String email) {
        System.out.println(email);
        List<User> managers = userDao.findManagersByBoss(email);
        return ResponseEntity.ok(managers);
    }

    @GetMapping("/managers/tasks")
    public ResponseEntity<List<Task>> getTasksByManagerId(@RequestParam Long managerId) {
        // Creează instanța DAO pentru acces la baza de date
        TaskDao taskDao = new TaskDao();

        // Obține lista de task-uri pentru manager
        List<Task> tasks = taskDao.getTasksByManagerId(managerId);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<User>> getEmployeesByBoss(@RequestParam String email) {
        List<User> employees = userDao.findEmployeesByBoss(email);
        if (employees.isEmpty()) {
            logger.warn("No employees found for boss with email: {}", email);
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/tasks")
    public ResponseEntity<List<Task>> getTasksByEmployeeId(@RequestParam Long employeeId) {
        TaskDao taskDao = new TaskDao();
        List<Task> tasks = taskDao.getTasksByEmployeeId(employeeId);

        if (tasks.isEmpty()) {
            logger.warn("No tasks found for employee ID: {}", employeeId);
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/employeeTasks")
    public ResponseEntity<List<Task>> getEmployeeTasks(@RequestParam String email) {

        TaskDao taskDao = new TaskDao();

        List<Task> tasks = TaskDao.getTasksByEmployeeEmail(email);

        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(tasks); // 204 No Content dacă nu sunt taskuri
        }

        return ResponseEntity.ok(tasks); // Returnează lista de taskuri
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> removeTask(@PathVariable Long taskId) {
        TaskDao taskDao = new TaskDao();
        System.out.println(taskId);
        // Șterge task-ul
        boolean isRemoved = taskDao.removeTask(taskId);

        if (isRemoved) {
            return ResponseEntity.noContent().build(); // 204 No Content dacă ștergerea a avut succes
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PostMapping("/add/tasks")
    public ResponseEntity<Void> addTask(@RequestBody Task task) {
        TaskDao taskDao = new TaskDao();
        // Obține ID-ul utilizatorului pe baza email-ului
        System.out.println(task.getEmail());
        Long assignedUserId = taskDao.getUserIdByEmail(task.getEmail());
        System.out.println(assignedUserId);
        if (assignedUserId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }

        // Setează AssignedUserId în task
        task.setAssignedUserId(assignedUserId);

        // Adaugă task-ul
        boolean isAdded = taskDao.addTask(task);

        System.out.println("Task processing result: " + (isAdded ? "Success" : "Failure"));

        if (isAdded) {
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/delete/task{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        TaskDao taskDao = new TaskDao();
        boolean isDeleted = taskDao.deleteTask(taskId);

        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(500).build(); // 500 Internal Server Error
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutUser(HttpSession session) {
        // Invalidate the session
        session.invalidate();
        return ResponseEntity.ok(new ApiResponse(true, "Logout successful."));
    }

    // Helper method to generate a random company code
    private String generateRandomCompanyCode() {
        return "CMP-" + (int) (Math.random() * 100000);
    }

    // Inner class for API responses
    public class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        // Constructors
        public ApiResponse() {}

        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        public ApiResponse(boolean success, T data) {
            this.success = success;
            this.data = data;
        }

        // Getters and Setters

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
