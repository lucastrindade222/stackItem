package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Produto;
import com.lucas.stackitem.model.Tag;
import com.lucas.stackitem.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag create(Tag tag) {
        if (tagRepository.existsByNome(tag.getNome())) {
            throw new IllegalArgumentException("Tag já cadastrada");
        }
        return tagRepository.save(tag);
    }

    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    public Optional<Tag> findByNome(String nome) {
        return tagRepository.findByNome(nome);
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Page<Tag> findAllPaged(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    public Tag update(Long id, Tag tag) {
        Tag existingTag = tagRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Tag não encontrada"));
        
        if (!existingTag.getNome().equals(tag.getNome()) && 
            tagRepository.existsByNome(tag.getNome())) {
            throw new IllegalArgumentException("Tag já cadastrada");
        }
        
        tag.setId(id);
        return tagRepository.save(tag);
    }

    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("Tag não encontrada");
        }
        tagRepository.deleteById(id);
    }
}