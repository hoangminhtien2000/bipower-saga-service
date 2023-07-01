package com.biplus.saga.domain.dto.candidate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignCVReviewCandidate {
    private String link;
    private String name;
    private String applyPosition;
    private String level;
    private String technology;
}
