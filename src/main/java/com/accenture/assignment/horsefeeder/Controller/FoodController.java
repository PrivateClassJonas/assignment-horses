package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.FoodDto;
import com.accenture.assignment.horsefeeder.Service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Die Klasse FoodController ist ein REST-Controller der FoodDto Objekte durch CRUD-Operationen bereitstellt/manipuliert/löscht
 */
@RestController
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private FoodService foodService;

    /**
     * HTTP GET Request, der alle Food Objekte in der Datenbank 'horsefeeder' ausgibt
     * @return ResponseEntity<List<FoodDto>>
     *     Ein ResponseEntity, dass eine Liste von allen Food Objekten zurückgibt
     */
    @GetMapping("/")
    public ResponseEntity<List<FoodDto>> showFoods() {
        Optional<List<FoodDto>> response = foodService.showFoods();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP GET Request, der ein bestimmtes Food Objekt in der Datenbank 'horsefeeder' ausgibt
     * @param foodId
     *     Die ID des gewünschten Food Objektes
     * @return ResponseEntity<FoodDto>
     *     Ein ResponseEntity, dass das gesuchte Food Objekt zurückgibt
     */
    @GetMapping("/{foodId}")
    public ResponseEntity<FoodDto> showFoodById(
            @PathVariable("foodId") Long foodId) {
        Optional<FoodDto> response = foodService.showFoodById(foodId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP POST Request, der ein bestimmtes Food Objekt in der Datenbank 'horsefeeder' abspeichert
     * @param foodDto
     *     Das Dto der Food Entität, dass in der Datenbank abgelegt werden soll
     * @return ResponseEntity<FoodDto>
     *     Ein ResponseEntity, dass das hinzugefügte Food Objekt zurückgibt
     */
    @PostMapping(path = "/")
    public @ResponseBody ResponseEntity<FoodDto> addNewFood(
            @RequestBody FoodDto foodDto) {
        Optional<FoodDto> response = foodService.addNewFood(foodDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP DELETE Request, der ein bestimmtes Food Objekt aus der Datenbank 'horsefeeder' löscht
     * @param foodId
     *     Die ID des gewünschten Food Objektes
     * @return ResponseEntity<FoodDto>
     *     Ein ResponseEntity, dass das gelöschte Food Objekt zurückgibt
     */
    @DeleteMapping("/{foodId}")
    public @ResponseBody ResponseEntity<FoodDto> deleteFoodById(
            @PathVariable("foodId") Long foodId) {
        Optional<FoodDto> response = foodService.deleteFoodById(foodId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP PUT Request, der ein bestimmtes Food Objekt in der Datenbank 'horsefeeder' ändern soll
     * @param foodId
     *     Die ID des gewünschten Food Objektes
     * @param foodDto
     *     Das neue Food Objekt, zum überschreiben in der Datenbank
     * @return ResponseEntity<FoodDto>
     *     Ein ResponseEntity, dass das neue Food Objekt zurückgibt
     */

    @RequestMapping(value = "/{foodID}", method = PUT)
    public @ResponseBody ResponseEntity<FoodDto> updateFoodByID(
            @PathVariable("foodID") Long foodId,
            @RequestBody FoodDto foodDto) {
        Optional<FoodDto> response = foodService.updateFoodByID(foodId, foodDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
}
