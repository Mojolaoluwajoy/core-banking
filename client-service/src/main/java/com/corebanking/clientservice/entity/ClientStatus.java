package com.corebanking.clientservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


public enum ClientStatus {
    PENDING,
    ACTIVE,
    INACTIVE


}