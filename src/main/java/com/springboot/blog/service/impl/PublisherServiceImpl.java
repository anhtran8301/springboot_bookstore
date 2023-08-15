package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Publisher;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PublisherDto;
import com.springboot.blog.repository.PublisherRepository;
import com.springboot.blog.service.PublisherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    private final ModelMapper mapper;

    @Override
    public PublisherDto getPublisherById(Long id){
        var publisher = publisherRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Publisher", "id", id));
        return mapper.map(publisher, PublisherDto.class);
    }

    @Override
    public List<PublisherDto> getPublishers(){
        var publishers = publisherRepository.findAll();
        return publishers.stream().map(publisher -> mapper.map(publisher, PublisherDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PublisherDto addPublisher(PublisherDto publisherDto){
        var publisher = mapper.map(publisherDto, Publisher.class);
        publisherRepository.save(publisher);
        return mapper.map(publisher, PublisherDto.class);
    }

    @Override
    public PublisherDto updatePublisher(PublisherDto publisherDto, Long id){
        var publisher = publisherRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Publisher", "id", id));

        if(publisherDto.getName().equals(publisher.getName())
                && publisherDto.getDescription().equals(publisher.getDescription())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Nothing to change");
        }

        publisher.setName(publisherDto.getName());
        publisher.setDescription(publisherDto.getDescription());
        publisher.setImageUrl(publisher.getImageUrl());
        publisherRepository.save(publisher);

        return mapper.map(publisher, PublisherDto.class);
    }

    @Override
    public void deletePublisher(Long id){
        var publisher = publisherRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Publisher", "id", id));
        publisherRepository.delete(publisher);
    }

    @Override
    public List<PublisherDto> addBatch(List<PublisherDto> publisherDto){
        var publishersEntity = publisherDto.stream()
                .map(dto -> mapper.map(dto, Publisher.class))
                .toList();

        var result = publisherRepository.saveAll(publishersEntity);

        return result.stream()
                .map(publisher -> mapper.map(publisher, PublisherDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PublisherDto> updateBatch(List<PublisherDto> request) {
        var ids = request.stream().map(PublisherDto::getId).collect(Collectors.toList());
        var publishers = publisherRepository.findByIds(ids);
        if(publishers.size() != ids.size()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Not found with some publisher id");
        }
        for (var publisher: request){
            publisherRepository.updateBatch(publisher.getId(), publisher.getName(), publisher.getDescription(), publisher.getImageUrl());
        }
        return request;
    }

    @Override
    public void deleteBatch(List<Long> publishersId) {
        List<Publisher> publishersEntity = publisherRepository.findAll();
        List<Publisher> result = publishersEntity.stream()
                .filter(u -> publishersId.contains(u.getId()))
                .collect(Collectors.toList());

        publisherRepository.deleteAllInBatch(result);
    }
}
