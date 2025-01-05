package com.example.TaskManagementSystem.Controllers;

import com.example.TaskManagementSystem.Dao.CompanyDao;
import com.example.TaskManagementSystem.Dao.TaskDao;
import com.example.TaskManagementSystem.Dao.UserDao;
import com.example.TaskManagementSystem.Models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession; // Import pentru sesiune
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import java.util.ArrayList;
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

        // Create a new user
        User user = new User(signUpForm.getFullname(), signUpForm.getEmail(), signUpForm.getPassword(), signUpForm.getRole());

        // Special logic for `boss` role
        if ("boss".equalsIgnoreCase(signUpForm.getRole())) {
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

            // Set the company_id in the User object
            user.setCompanyId(companyId);
        }

        // Add user to the database
        boolean isAdded = userDao.addUser(user);
        if (isAdded) {
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully."));
        } else {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Failed to register user."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginForm loginForm, HttpSession session) {
        // Find user in database by email
        User user = userDao.findUserByEmail(loginForm.getEmail());

        if (user == null || !user.getPassword().equals(loginForm.getPassword())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email or password incorrect."));
        }

        // Store the user in the session
        session.setAttribute("loggedInUser", user);

        // Logging successful login
        logger.info("User {} logged in successfully with role: {}", user.getEmail(), user.getUserType());
        return ResponseEntity.ok(new ApiResponse(true, "Login successful!", user.getUserType()));
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

    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser(@RequestParam String email) {
        User user = userDao.findUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body(null); // Returnează 404 dacă utilizatorul nu este găsit
        }

        return ResponseEntity.ok(user); // Returnează detaliile utilizatorului
    }

    @GetMapping("/managers")
    public ResponseEntity<List<User>> getAllManagers() {
        List<User> managers = userDao.findUsersByType("manager");
        return ResponseEntity.ok(managers);
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
    public static class ApiResponse {
        private boolean success;
        private String message;
        private String userType; // Added this field

        public ApiResponse() {}

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public ApiResponse(boolean success, String message, String userType) {
            this.success = success;
            this.message = message;
            this.userType = userType;
        }

        // Getters and setters
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

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }
}
