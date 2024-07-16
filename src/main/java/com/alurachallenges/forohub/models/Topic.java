package com.alurachallenges.forohub.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.alurachallenges.forohub.dto.TopicDataUpdate;

@Table(name = "topicos")
@Entity(name = "topicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid_usuario")
    private User usuario;

    @Column(name = "nombre_curso")
    private String curso;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private Boolean status = Boolean.TRUE;

    public void updateTopicData(
            TopicDataUpdate topic) {
        if (topic.titulo() != null) {
            this.titulo = topic.titulo();
        }
        if (topic.mensaje() != null) {
            this.mensaje = topic.mensaje();
        }
    }

    public void disableTopic() {
        this.status = false;
    }
}
