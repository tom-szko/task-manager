package com.szkopinski.todoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotBlank
  @Size(min = 3)
  private String userName;

  @NotBlank
  @Size(min = 3)
  @Column(length = 60)
  @NotBlank
  private String password;

  @NotBlank
  @Email
  private String email;

  @OneToOne
  private ImageFile avatar;

  public Account(String userName, String password, String email, ImageFile avatar) {
    this.userName = userName;
    this.password = password;
    this.email = email;
    this.avatar = avatar;
  }
}
