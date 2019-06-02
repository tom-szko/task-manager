package com.szkopinski.todoo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
