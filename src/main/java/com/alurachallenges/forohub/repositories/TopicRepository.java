package com.alurachallenges.forohub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alurachallenges.forohub.models.Topic;


@Repository
public interface TopicRepository extends JpaRepository <Topic, Long>
{
    Page<Topic> findByStatusTrue(Pageable pageable);
}
