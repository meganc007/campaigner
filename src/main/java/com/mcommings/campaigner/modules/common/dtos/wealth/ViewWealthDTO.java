package com.mcommings.campaigner.modules.common.dtos.wealth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewWealthDTO {
    private int id;
    private String name;
}
