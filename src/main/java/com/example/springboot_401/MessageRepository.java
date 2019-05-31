package com.example.springboot_401;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Iterable<Message> findAllByAuthorId(long id);
}
