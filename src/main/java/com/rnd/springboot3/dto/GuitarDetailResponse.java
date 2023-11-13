package com.rnd.springboot3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rnd.springboot3.entity.Guitar;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuitarDetailResponse {

    private long id;
    private String type;
    private BigDecimal price;
    private boolean isDeleted;
    private Date craetedDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;

}
