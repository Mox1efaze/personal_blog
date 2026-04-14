package com.perblog.service.impl;

import com.perblog.entity.Tag;
import com.perblog.repository.TagRepository;
import com.perblog.service.TagService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag createTag(Tag tag) {
        if (tagRepository.existsByName(tag.getName())) {
            throw new RuntimeException("Tag name already exists");
        }
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long id, Tag tagDetails) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tag not found"));

        if (!tag.getName().equals(tagDetails.getName()) 
            && tagRepository.existsByName(tagDetails.getName())) {
            throw new RuntimeException("Tag name already exists");
        }

        tag.setName(tagDetails.getName());

        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}
