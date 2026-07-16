package org.fundoonotes.controller;

import org.fundoonotes.dto.request.LabelRequestDTO;
import org.fundoonotes.dto.response.LabelResponseDTO;
import org.fundoonotes.service.LabelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/labels")
    public class LabelController {

        private final LabelService labelService;

        public LabelController(LabelService labelService) {
            this.labelService = labelService;
        }

        @PostMapping
        public ResponseEntity<LabelResponseDTO> createLabel(@RequestBody LabelRequestDTO dto){
            return new ResponseEntity<>(labelService.createLabel(dto), HttpStatus.CREATED
            );
        }

        @GetMapping
        public ResponseEntity<List<LabelResponseDTO>> getLabels(){
            return ResponseEntity.ok(labelService.getLabels());

        }

        @PutMapping("/{id}")
        public ResponseEntity<LabelResponseDTO> updateLabel(@PathVariable Long id, @RequestBody LabelRequestDTO dto){
            return ResponseEntity.ok(labelService.updateLabel(id,dto)
            );
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteLabel(@PathVariable Long id){
            return ResponseEntity.ok(labelService.deleteLabel(id)
            );
        }

        @PostMapping("/{labelId}/notes/{noteId}")
        public ResponseEntity<String> addLabelToNote(@PathVariable Long labelId, @PathVariable Long noteId){
            return ResponseEntity.ok(labelService.addLabelToNote(noteId,labelId)
            );
        }

    }
