package com.fourstars.FourStars.domain;

import com.fourstars.FourStars.util.constant.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions", indexes = {
        @Index(name = "idx_question_quizid", columnList = "quiz_id")
})
@Getter
@Setter
@SQLDelete(sql = "UPDATE questions SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private QuestionType questionType;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String textToFill;


    @Column(columnDefinition = "TEXT")
    private String correctSentence;


    @Column(length = 2048)
    private String audioUrl;


    @Column(length = 2048)
    private String imageUrl;


    private int points = 10;

    private int questionOrder;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<QuestionChoice> choices = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id")
    private Vocabulary relatedVocabulary;

    @Column(nullable = false)
    private boolean deleted = false;
}
