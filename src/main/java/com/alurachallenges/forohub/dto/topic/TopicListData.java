package com.alurachallenges.forohub.dto.topic;

import java.time.LocalDateTime;

import com.alurachallenges.forohub.models.Topic;

public record TopicListData(
        Long id,
        String curso,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        Boolean status
)
{
    public TopicListData(Topic topic)
    {
        this(
                topic.getId(),
                topic.getCurso(),
                topic.getTitulo(),
                topic.getMensaje(),
                topic.getFechaCreacion(),
                topic.getStatus()
        );
    }
}
