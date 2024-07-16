package com.alurachallenges.forohub.dto.topic;

import java.time.LocalDateTime;

import com.alurachallenges.forohub.models.Topic;

public record TopicData(
        Long id,
        Long usuario,
        String curso,
        String titulo,
        String mensaje,
        LocalDateTime fecha) {

        public TopicData(Topic topic) {
                this(
                        topic.getId(),
                        topic.getUsuario().getId(),
                        topic.getCurso(),
                        topic.getTitulo(),
                        topic.getMensaje(),
                        topic.getFechaCreacion());
        }
}
