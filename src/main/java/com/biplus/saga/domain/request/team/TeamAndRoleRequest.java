package com.biplus.saga.domain.request.team;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamAndRoleRequest {
    private Long teamId;
    private List<Integer> roleIds;
}
