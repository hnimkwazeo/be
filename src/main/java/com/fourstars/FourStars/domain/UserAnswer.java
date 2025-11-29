package com.fourstars.FourStars.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_answers")
@Getter
@Setter
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_quiz_attempt_id", nullable = false)
    private UserQuizAttempt userQuizAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String userAnswerText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_choice_id")
    private QuestionChoice selectedChoice;

    private boolean isCorrect;

    private int pointsAwarded = 0;
}
