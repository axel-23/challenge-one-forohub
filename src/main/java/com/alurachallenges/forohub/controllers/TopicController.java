package com.alurachallenges.forohub.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.alurachallenges.forohub.dto.TopicDataUpdate;
import com.alurachallenges.forohub.dto.UserData;
import com.alurachallenges.forohub.dto.topic.TopicData;
import com.alurachallenges.forohub.dto.topic.TopicDataResponse;
import com.alurachallenges.forohub.dto.topic.TopicListData;
import com.alurachallenges.forohub.infra.exceptions.ForbiddenException;
import com.alurachallenges.forohub.infra.exceptions.NotFoundException;
import com.alurachallenges.forohub.models.Topic;
import com.alurachallenges.forohub.repositories.TopicRepository;
import com.alurachallenges.forohub.services.TopicService;

@RestController
@RequestMapping("topics")
public class TopicController {

    @Autowired
    private TopicService service;

    @Autowired
    TopicRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity setTopic(
            @RequestBody @Valid TopicData topic) {
        var response = service.publish(topic);

        Object responseBody = new Object() {
            public final int httpStatus = HttpStatus.OK.value();
            public final Object topic = response;
        };

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping
    public Page<TopicListData> getTopics(
            @PageableDefault(size = 5, sort = "fechaCreacion", direction = Sort.Direction.DESC) Pageable pageable) {
        return repository.findByStatusTrue(pageable)
                .map(TopicListData::new);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> getATopic(
            @PathVariable Long id) {
        try {
            Topic topic = repository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Tema no encontrado"));

            if (!topic.getStatus()) {
                throw new NotFoundException("Tema no encontrado");
            }

            var topicData = new TopicData(
                    topic.getId(),
                    topic.getUsuario().getId(),
                    topic.getCurso(),
                    topic.getTitulo(),
                    topic.getMensaje(),
                    topic.getFechaCreacion()
            );

            Object responseBody = new Object() {
                public final int httpStatus = HttpStatus.OK.value();
                public final Object topic = topicData;
            };

            return ResponseEntity.ok(responseBody);

        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping()
    @Transactional
    public ResponseEntity<Object> updateTopic(
            @RequestBody @Valid TopicDataUpdate topicData) {
        Topic topic = repository.getReferenceById(topicData.id());
        topic.updateTopicData(topicData);

        var response = new TopicDataResponse(
                topic.getId(),
                new UserData(
                        topic.getUsuario().getId(),
                        topic.getUsuario().getUsername()),
                topic.getCurso(),
                topic.getTitulo(),
                topic.getMensaje());

        Object responseBody = new Object() {
            public final int httpStatus = HttpStatus.OK.value();
            public final Object topic = response;
        };

        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicDataResponse> deleteTopic(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        try {
            service.deleteTopic(id, authenticatedUsername);
            return ResponseEntity.noContent().build();
        } catch (ForbiddenException e) {
            return ResponseEntity.status(403).build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
