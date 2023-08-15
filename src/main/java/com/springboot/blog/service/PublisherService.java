package com.springboot.blog.service;

import com.springboot.blog.payload.PublisherDto;

import java.util.List;

public interface PublisherService {
    PublisherDto getPublisherById(Long id);
    List<PublisherDto> getPublishers();
    PublisherDto addPublisher(PublisherDto publisherDto);
    PublisherDto updatePublisher(PublisherDto publisherDto, Long id);
    void deletePublisher(Long id);
    List<PublisherDto> addBatch(List<PublisherDto> publisherDto);
    List<PublisherDto> updateBatch(List<PublisherDto> request);
    void deleteBatch(List<Long> publishersId);
}
