package org.fundoonotes.service;

import org.fundoonotes.dto.request.LabelRequestDTO;
import org.fundoonotes.dto.response.LabelResponseDTO;

import java.util.List;

public interface LabelService {
        LabelResponseDTO createLabel(LabelRequestDTO dto);

        List<LabelResponseDTO> getLabels();

        LabelResponseDTO updateLabel(Long id,LabelRequestDTO dto);

        String deleteLabel(Long id);

        String addLabelToNote(Long noteId,Long labelId);

    }

