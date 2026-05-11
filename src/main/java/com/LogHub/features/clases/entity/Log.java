package com.LogHub.features.clases.entity;

import java.time.LocalDateTime;

import org.springframework.boot.logging.LogLevel;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    /*Registrar momento en el que se inicia el log */
    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}
