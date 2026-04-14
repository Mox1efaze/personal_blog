package com.perblog.service;

import com.perblog.entity.Tag;
import java.util.List;

public interface TagService {
    Tag createTag(Tag tag);
    Tag updateTag(Long id, Tag tag);
    void deleteTag(Long id);
    Tag getTagById(Long id);
    List<Tag> getAllTags();
}
