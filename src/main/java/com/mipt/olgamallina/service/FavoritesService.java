package com.mipt.olgamallina.service;

import com.mipt.olgamallina.model.Task;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class FavoritesService {

    public static final String FAVORITE_TASK_IDS = "favoriteTaskIds";

    private final TaskService taskService;

    public FavoritesService(TaskService taskService) {
        this.taskService = taskService;
    }

    @SuppressWarnings("unchecked")
    private Set<Long> getFavoriteIds(HttpSession session) {
        Object attribute = session.getAttribute(FAVORITE_TASK_IDS);
        if (attribute == null) {
            Set<Long> ids = new LinkedHashSet<>();
            session.setAttribute(FAVORITE_TASK_IDS, ids);
            return ids;
        }
        return (Set<Long>) attribute;
    }

    public void addToFavorites(Long taskId, HttpSession session) {
        taskService.getById(taskId);
        getFavoriteIds(session).add(taskId);
    }

    public void removeFromFavorites(Long taskId, HttpSession session) {
        getFavoriteIds(session).remove(taskId);
    }

    public List<Task> getFavoriteTasks(HttpSession session) {
        Set<Long> ids = getFavoriteIds(session);
        List<Task> result = new ArrayList<>();
        for (Long id : ids) {
            result.add(taskService.getById(id));
        }
        return result;
    }
}