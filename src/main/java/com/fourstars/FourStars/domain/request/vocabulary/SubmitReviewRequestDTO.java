package com.fourstars.FourStars.domain.request.vocabulary;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmitReviewRequestDTO {

    @NotNull(message = "Vocabulary ID cannot be null")
    private Long vocabularyId;
    @NotNull(message = "Quality score cannot be null")
    @Min(value = 0, message = "Quality score must be at least 0")
    @Max(value = 5, message = "Quality score must be at most 5")
    private int quality;
}
