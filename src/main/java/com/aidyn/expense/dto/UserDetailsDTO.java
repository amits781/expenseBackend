package com.aidyn.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

  private UserDTO user;
  private Float paid_share;
  private Float owed_share;
}
