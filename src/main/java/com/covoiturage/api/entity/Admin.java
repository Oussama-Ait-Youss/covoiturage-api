package com.covoiturage.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin extends Utilisateur{

}
