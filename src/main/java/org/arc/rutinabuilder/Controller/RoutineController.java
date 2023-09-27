package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Routine;
import org.arc.rutinabuilder.Services.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutineController {

    @Autowired
    RoutineService routineService;

    public ResponseEntity<Routine> CreateRoutine(@RequestBody Routine routine) {
        try {
            if (routine == null || routine.getExercices() == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.status(HttpStatus.OK).body(routineService.CreateRoutine(routine));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
