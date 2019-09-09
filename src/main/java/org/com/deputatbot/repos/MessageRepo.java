package org.com.deputatbot.repos;


import org.com.deputatbot.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {

List<Message> findByTag(String tag);
List<Message> findByText(String text);

}
