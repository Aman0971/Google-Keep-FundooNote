package org.fundoonotes.repository;

import org.fundoonotes.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

    public interface LabelRepository extends JpaRepository<Label,Long> {

        List<Label> findByUserId(Long userId);

        Optional<Label> findByIdAndUserId(Long id,Long userId);

    }
