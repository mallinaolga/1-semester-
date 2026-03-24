package com.mipt.olgamallina.controller;

import com.mipt.olgamallina.dto.TaskResponseDto;
import com.mipt.olgamallina.mapper.TaskMapper;
import com.mipt.olgamallina.service.FavoritesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;
    private final TaskMapper taskMapper;

    public FavoritesController(FavoritesService favoritesService, TaskMapper taskMapper) {
        this.favoritesService = favoritesService;
        this.taskMapper = taskMapper;
    }

    @Operation(summary = "Add task to favorites")
    @PostMapping("/{taskId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long taskId, HttpSession session) {
        favoritesService.addToFavorites(taskId, session);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove task from favorites")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long taskId, HttpSession session) {
        favoritesService.removeFromFavorites(taskId, session);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get favorite tasks")
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getFavorites(HttpSession session) {
        List<TaskResponseDto> response = favoritesService.getFavoriteTasks(session).stream()
                .map(taskMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }
}