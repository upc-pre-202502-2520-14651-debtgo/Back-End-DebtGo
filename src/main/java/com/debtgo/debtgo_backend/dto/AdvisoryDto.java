package com.debtgo.debtgo_backend.dto;

import com.debtgo.debtgo_backend.domain.advisory.Advisory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvisoryDto {
    private String title;
    private String description;

    public Advisory toEntity() {
        Advisory advisory = new Advisory();
        advisory.setTitle(this.title);
        advisory.setDescription(this.description);
        return advisory;
    }
}