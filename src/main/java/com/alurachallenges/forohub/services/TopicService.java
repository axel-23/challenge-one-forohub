package com.alurachallenges.forohub.services;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.alurachallenges.forohub.dto.topic.TopicData;
import com.alurachallenges.forohub.infra.exceptions.ForbiddenException;
import com.alurachallenges.forohub.infra.exceptions.IntegrityValidation;
import com.alurachallenges.forohub.infra.exceptions.NotFoundException;
import com.alurachallenges.forohub.models.Topic;
import com.alurachallenges.forohub.repositories.TopicRepository;
import com.alurachallenges.forohub.repositories.UserRepository;

@Service
public class TopicService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TopicRepository topicRepository;

    public TopicData publish(
            @RequestBody @Valid TopicData topicData) {
        if (!userRepository.findById(topicData.usuario()).isPresent()) {
            throw new IntegrityValidation("Este usuario no existe");
        }

        var user = userRepository.findById(topicData.usuario()).get();
        Topic topic = new Topic();
        topic.setUsuario(user);
        topic.setCurso(topicData.curso());
        topic.setTitulo(topicData.titulo());
        topic.setMensaje(topicData.mensaje());
        topic.setFechaCreacion(LocalDateTime.now());
        topic.setStatus(true);

        topicRepository.save(topic);
        return new TopicData(topic);
    }

    public void deleteTopic(Long topicId, String authenticatedUsername) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Tema no encontrado"));
        if (!topic.getStatus()) {
            throw new NotFoundException("Tema no encontrado");
        }

        if (!topic.getUsuario().getUsername().equalsIgnoreCase(authenticatedUsername)) {
            throw new ForbiddenException("No tienes permiso para eliminar este tema");
        }

        topic.disableTopic();
        topicRepository.save(topic);
    }

}