package com.aidyn.expense.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {

  private Long id;
  private String description;
  private Boolean payment;
  private Float cost;
  private Date date;
  private Date deleted_at;
  private UserDTO created_by;
  private List<UserDetailsDTO> users;
  private CategoryDTO category;
}
