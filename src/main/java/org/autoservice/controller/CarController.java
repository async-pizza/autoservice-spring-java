package org.autoservice.controller;

import org.autoservice.dto.CarRequest;
import org.autoservice.model.Car;
import org.autoservice.model.User;
import org.autoservice.security.SessionManager;
import org.autoservice.service.CarService;
import org.autoservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;
    private final UserService userService;

    public CarController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createCar(@RequestBody CarRequest carRequest, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserFromSession(request);
        System.out.println(user.toString());
        Car car = new Car(null, carRequest.brand(), carRequest.model(), carRequest.year(), carRequest.licensePlate(), user);
        carService.createCar(car);
        return ResponseEntity.ok("Car created successfully!");
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars(HttpServletRequest request) {
        User user = getUserFromSession(request);
        List<Car> cars = carService.getCarsByOwner(user);
        return ResponseEntity.ok(cars);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        carService.deleteCar(id);
        return ResponseEntity.ok("Car deleted successfully!");
    }

    private User getUserFromSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    return SessionManager.getUserFromSession(sessionId);
                }
            }
        }
        return null;
    }
}