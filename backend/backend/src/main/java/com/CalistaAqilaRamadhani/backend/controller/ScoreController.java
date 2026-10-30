package com.CalistaAqilaRamadhani.backend.controller;

import com.CalistaAqilaRamadhani.backend.model.Score;
import com.CalistaAqilaRamadhani.backend.service.ScoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{scoreId}")
    public ResponseEntity<List<Score>> getAllScores() {
        List<Score> scores = scoreService.getAllScores();
        return
            ResponseEntity.ok(scores);

        Optional<Score> score = scoreService.getScoreByID(scoreId);

        if (score.isPresent()) {
            return ResponseEntity.ok(score.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Score not found");
        }
    }

    @GetMapping("/leaderboard")
    public  ResponseEntity<List<Score>>     getLeaderboardByPoint(
        @RequestParam(defultValue = "10") Integer limit){
        List<Score> scores = scoreService.getLeaderboard(limit);

        return
            ResponseEntity.ok(scores);
    }

    @GetMapping("/above/{minValue}")

    public ResponseEntity<List<Score>> getScoresAboveValue(
        @PathVariable Integer minValue){
        List<Score> scores = scoreService.getScpreAboveValue(minValue);
        return
            ResponseEntity.ok(scores);
    }

    @GetMapping("/recent")

    public ResponseEntity<List<Score>> getRecentScores(){
        List<Score> scores = scoreService.getRecentScores();
        return
            ResponseEntity.ok(scores);
    }

    @DeleteMapping("/{scoreId}")
    public  ResponseEntity<?> deleteScore(
        @PAthVariable Long scoreID){
        try {
            scoreService.deleteScore(scoreId);
            return

                ResponseEntity.status(HttpsStatus.NOT_FOUND)
                    .body("Eror : Score not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> createScore(@RequestBody Score score) {

        try {
            Score newScore = scoreService.createScore(score);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(newScore);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Failed to create score");
        }
    }
}
