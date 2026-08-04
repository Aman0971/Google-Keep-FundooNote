package org.fundoonotes.service;

import org.fundoonotes.dto.request.LabelRequestDTO;
import org.fundoonotes.dto.response.LabelResponseDTO;
import org.fundoonotes.dto.response.NoteResponseDTO;

import java.util.List;

public interface LabelService {
        LabelResponseDTO createLabel(LabelRequestDTO dto);

        List<LabelResponseDTO> getLabels();

        LabelResponseDTO updateLabel(Long id,LabelRequestDTO dto);

        String deleteLabel(Long id);

        String addLabelToNote(Long noteId,Long labelId);

        String removeLabelFromNote(Long noteId, Long labelId);

        List<NoteResponseDTO> getNotesByLabel(Long labelId);

    }

